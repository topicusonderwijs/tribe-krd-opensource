package nl.topicus.eduarte.web.pages.deelnemer.groep;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ExtendedHibernateModel;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.datapanel.columns.AjaxButtonColumn;
import nl.topicus.cobra.web.components.panels.datapanel.columns.AjaxDeleteColumn;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.core.principals.deelnemer.groep.DeelnemerGroepen;
import nl.topicus.eduarte.dao.helpers.GroepsdeelnameDataAccessHelper;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.groep.GroepsdeelnameModalWindow;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DeelnemerGroepsdeelnameTable;
import nl.topicus.eduarte.web.components.panels.filter.DeelnemerGroepsdeelnameZoekFilterPanel;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.groep.GroepKaartPage;
import nl.topicus.eduarte.zoekfilters.GroepsdeelnameZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;

/**
 * Pagina met alle groepsdeelnames van een deelnemer.
 * 
 * @author loite
 * 
 */
@PageInfo(title = "Groepen", menu = {"Deelnemer > [deelnemer] > Verbintenis > Groepen",
	"Groep > [groep] > [deelnemer] > Verbintenis > Groepen"})
@InPrincipal(DeelnemerGroepen.class)
public class DeelnemerGroepenPage extends AbstractDeelnemerPage
{
	private static final long serialVersionUID = 1L;

	private static final GroepsdeelnameZoekFilter getDefaultFilter(Deelnemer deelnemer)
	{
		GroepsdeelnameZoekFilter filter = new GroepsdeelnameZoekFilter(deelnemer);
		filter.addOrderByProperty("groep.code");
		filter.addOrderByProperty("begindatum");
		filter.setAscending(false);
		return filter;
	}

	private GroepsdeelnameModalWindow editWindow;

	private EduArteDataPanel<Groepsdeelname> datapanel;

	public DeelnemerGroepenPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer(), provider.getDeelnemer()
			.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerGroepenPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerGroepenPage(Verbintenis inschrijving)
	{
		this(inschrijving.getDeelnemer(), inschrijving);
	}

	public DeelnemerGroepenPage(Deelnemer deelnemer, Verbintenis inschrijving)
	{
		super(DeelnemerMenuItem.Groepen, deelnemer, inschrijving);

		final boolean editDeelnameRecht =
			new DataSecurityCheck(Groep.EDIT_DEELNAME).isActionAuthorized(Enable.class)
				&& EduArteApp.get().isModuleActive(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS);
		final boolean removeDeelnameRecht =
			new DataSecurityCheck(Groep.DEELNAME_REMOVE).isActionAuthorized(Enable.class)
				&& EduArteApp.get().isModuleActive(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS);

		GroepsdeelnameZoekFilter filter = getDefaultFilter(deelnemer);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		filter.getGroepFilter().setPlaatsingsgroep(false);
		IDataProvider<Groepsdeelname> provider =
			GeneralFilteredSortableDataProvider.of(filter, GroepsdeelnameDataAccessHelper.class);
		DeelnemerGroepsdeelnameTable table = new DeelnemerGroepsdeelnameTable();
		table.addColumn(new AjaxButtonColumn<Groepsdeelname>("Bewerken", "")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(WebMarkupContainer item, IModel<Groepsdeelname> rowModel,
					AjaxRequestTarget target)
			{
				editWindow.setDefaultModelObject(rowModel.getObject());
				editWindow.show(target);
			}

			@Override
			protected String getCssEnabled()
			{
				return "ui-icon ui-icon-wrench";
			}

			@Override
			protected String getCssDisabled()
			{
				return "ui-icon ui-icon-wrench";
			}

			@Override
			public boolean isVisible()
			{
				return editDeelnameRecht;
			}
		});
		table.addColumn(new AjaxDeleteColumn<Groepsdeelname>("Verwijderen", "")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(WebMarkupContainer item, IModel<Groepsdeelname> rowModel,
					AjaxRequestTarget target)
			{
				super.onClick(item, rowModel, target);
				target.addComponent(datapanel);
			}

			@Override
			public boolean isVisible()
			{
				return removeDeelnameRecht;
			}
		});
		datapanel = new EduArteDataPanel<Groepsdeelname>("datapanel", provider, table);
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Groepsdeelname>(
			GroepKaartPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<Groepsdeelname> item)
			{
				pushSearchResultToNavigationLevel(datapanel, item.getIndex());
				setResponsePage(new GroepKaartPage(item.getModelObject()));
			}
		});
		datapanel.setItemsPerPage(20);
		add(datapanel);
		DeelnemerGroepsdeelnameZoekFilterPanel filterPanel =
			new DeelnemerGroepsdeelnameZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);

		editWindow =
			new GroepsdeelnameModalWindow("editWindow", new ExtendedHibernateModel<Groepsdeelname>(
				null, new DefaultModelManager(Groepsdeelname.class)));
		editWindow.setWindowClosedCallback(new WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				Object selected = editWindow.getDefaultModelObject();
				if (selected instanceof Entiteit)
				{
					Entiteit selectedEntity = (Entiteit) selected;
					if (selectedEntity.isSaved())
						selectedEntity.refresh();
				}
				target.addComponent(datapanel);
			}
		});
		editWindow.setVisible(editDeelnameRecht);
		add(editWindow);

		add(new AjaxLink<Void>("toevoegen")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				Groepsdeelname nieuw = new Groepsdeelname();
				nieuw.setDeelnemer(getContextDeelnemer());
				editWindow.setDefaultModelObject(nieuw);
				editWindow.show(target);
			}
		}.setVisible(editDeelnameRecht));

		createComponents();
	}
}
