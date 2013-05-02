package nl.topicus.eduarte.web.components.resultaat;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.ResultaatZoekFilterInstellingDataAccessHelper;
import nl.topicus.eduarte.dao.hibernate.IllegalResultaatException;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.resultaten.DeelnemerResultatenboomPage;
import nl.topicus.eduarte.zoekfilters.ResultaatZoekFilter;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractResultatenPanel<T, M extends ResultatenModel> extends Panel
{
	private static final Logger log = LoggerFactory.getLogger(AbstractResultatenPanel.class);

	private static final long serialVersionUID = 1L;

	protected ResultatenUIFactory<T, M> uiFactory;

	private ToetsZoekFilter toetsFilter;

	private ResultaatZoekFilter resultaatFilter;

	private M resultatenModel;

	private Form<Void> form;

	public AbstractResultatenPanel(String id, ResultatenUIFactory<T, M> uiFactory,
			ToetsZoekFilter toetsFilter, boolean vulGekoppeld, Groep groep)
	{
		super(id);

		this.uiFactory = uiFactory;
		this.toetsFilter = toetsFilter;

		DataAccessRegistry.getHelper(ResultaatZoekFilterInstellingDataAccessHelper.class)
			.vulZoekFilter(toetsFilter, EduArteContext.get().getMedewerker(), groep, vulGekoppeld);
	}

	protected void createComponents()
	{
		add(form = new Form<Void>("form"));
		resultaatFilter = new ResultaatZoekFilter(toetsFilter, getDeelnemers());
		resultatenModel = createResultatenModel();

		EditorJavascriptRenderer<T> javascriptRenderer = new EditorJavascriptRenderer<T>();
		final AbstractResultatenTable<T, M> table =
			createTable(uiFactory.createColumnCreator(javascriptRenderer));
		final EduArteDataPanel<T> datapanel =
			new EduArteDataPanel<T>("resultaten", createDataProvder(), table)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected Object getRowValue(T rowValue)
				{
					if (rowValue instanceof Toets)
					{
						return ((Toets) rowValue).getResultaatstructuur();
					}
					return rowValue;
				}

				@Override
				public boolean isSortable()
				{
					return false;
				}
			};
		datapanel.setReuseItems(true);
		datapanel.setSelecteerKolommenButtonVisible(false);
		datapanel.setGroeperenButtonVisible(false);
		datapanel.setExportButtonVisible(!resultatenModel.isEditable());
		datapanel.setPrintButtonVisible(!resultatenModel.isEditable());
		datapanel.setItemsPerPage(Integer.MAX_VALUE);
		datapanel.addBehaviorToTable(new AppendingAttributeModifier("class", "tblResultaat"));
		postProcessDatapanel(datapanel);
		WebMarkupContainer contentBox = new WebMarkupContainer("contentBox");
		form.add(contentBox);
		contentBox.add(datapanel);
		contentBox.add(new AttributeAppender("style", new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				return "width: " + table.calculateWidth(datapanel) + "px";
			}
		}, ";"));
		if (isMultiToets())
			datapanel.addBehaviorToTable(new AppendingAttributeModifier("class", "multiToets"));
		contentBox.add(new ResultaatEditorPanel("editor", datapanel.getTable(), resultatenModel,
			isMultiToets(), resultatenModel.isEditable(), javascriptRenderer));

		add(createZoekFilterPanel("filter", datapanel).setVisible(!resultatenModel.isEditable()));

		form.add(uiFactory.createInputfields("inputfields"));
	}

	private M createResultatenModel()
	{
		return uiFactory.createResultatenModel(resultaatFilter,
			new LoadableDetachableModel<List<Toets>>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Toets> load()
				{
					return getToetsen();
				}
			}, new LoadableDetachableModel<List<Deelnemer>>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Deelnemer> load()
				{
					return getDeelnemers();
				}
			});
	}

	public ToetsZoekFilter getToetsFilter()
	{
		return toetsFilter;
	}

	public ResultaatstructuurZoekFilter getResultaatstructuurFilter()
	{
		return toetsFilter.getResultaatstructuurFilter();
	}

	public M getResultatenModel()
	{
		return resultatenModel;
	}

	public Form<Void> getForm()
	{
		return form;
	}

	public void checkResultatenOnOpen(SecurePage returnPage)
	{
		try
		{
			getResultatenModel().checkResultaten(false);
		}
		catch (IllegalResultaatException e)
		{
			log.error(e.getMessage(), e);
			returnPage.error("Er is een fout gevonden in de te bewerken resultaten: "
				+ e.getMessage());
			if (returnPage instanceof DeelnemerResultatenboomPage)
			{
				((DeelnemerResultatenboomPage) returnPage).setStructuurOngeldig(true);
			}
			// wiquery keeps references to the edit page, make sure the page is properly
			// detached
			Page page = getPage();
			remove();
			page.detach();
			throw new RestartResponseException(returnPage);
		}
	}

	protected abstract AbstractResultatenTable<T, M> createTable(
			ResultaatColumnCreator<T, M> columnCreator);

	protected abstract IDataProvider<T> createDataProvder();

	public abstract List<Deelnemer> getDeelnemers();

	public abstract List<Toets> getToetsen();

	protected abstract AutoZoekFilterPanel<ToetsZoekFilter> createZoekFilterPanel(String id,
			EduArteDataPanel<T> datapanel);

	abstract protected boolean isMultiToets();

	protected void postProcessDatapanel(@SuppressWarnings("unused") EduArteDataPanel<T> datapanel)
	{
	}
}
