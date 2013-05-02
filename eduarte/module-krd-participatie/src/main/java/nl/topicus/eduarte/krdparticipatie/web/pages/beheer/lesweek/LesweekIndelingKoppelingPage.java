package nl.topicus.eduarte.krdparticipatie.web.pages.beheer.lesweek;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.datapanel.CollapsableRowFactoryDecorator;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.eduarte.dao.helpers.OrganisatieEenheidDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.LesweekIndelingDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheidLocatie;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.krdparticipatie.principals.beheer.LesweekindelingWrite;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieBeheerMenuItem;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.datapanels.columns.LesweekindelingTreeColumn;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.datapanels.columns.lesweekindelingColumn;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

@InPrincipal(LesweekindelingWrite.class)
@PageInfo(title = "Lesweekkoppeling", menu = "Beheer > Participatie > Lesweekkoppeling")
public class LesweekIndelingKoppelingPage extends AbstractBeheerPage<Void>
{
	private IModel<List<LesweekIndeling>> choicesModel;

	private Form<Void> form;

	private EduArteDataPanel<LesweekKoppelingWrapper> datapanel;

	public LesweekIndelingKoppelingPage()
	{
		super(ParticipatieBeheerMenuItem.Lesweekkoppeling);

		form = new Form<Void>("form");

		LoadableDetachableModel<List<LesweekKoppelingWrapper>> listModel =
			new LoadableDetachableModel<List<LesweekKoppelingWrapper>>()
			{

				private static final long serialVersionUID = 1L;

				@Override
				protected List<LesweekKoppelingWrapper> load()
				{
					OrganisatieEenheid root =
						DataAccessRegistry.getHelper(OrganisatieEenheidDataAccessHelper.class)
							.getRootOrganisatieEenheid();
					List<LesweekKoppelingWrapper> theList =
						new ArrayList<LesweekKoppelingWrapper>();
					return getList(root, theList);
				}
			};

		ListModelDataProvider<LesweekKoppelingWrapper> listProvider =
			new ListModelDataProvider<LesweekKoppelingWrapper>(listModel)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<LesweekKoppelingWrapper> model(LesweekKoppelingWrapper object)
				{
					return new Model<LesweekKoppelingWrapper>(object);
				}
			};

		CollapsableRowFactoryDecorator<LesweekKoppelingWrapper> rowfactory =
			new CollapsableRowFactoryDecorator<LesweekKoppelingWrapper>(
				new CustomDataPanelRowFactory<LesweekKoppelingWrapper>());

		CustomDataPanelContentDescription<LesweekKoppelingWrapper> contentDesc =
			new CustomDataPanelContentDescription<LesweekKoppelingWrapper>(
				"Lesweekindeling koppelen aan Organisatie of Locatie");

		List<LesweekIndeling> list =
			DataAccessRegistry.getHelper(LesweekIndelingDataAccessHelper.class).list();
		list.add(null);

		choicesModel = ModelFactory.getListModel(list);

		contentDesc.addColumn(new LesweekindelingTreeColumn(rowfactory));
		contentDesc.addColumn(new lesweekindelingColumn("lesweek", "lesweek", choicesModel));

		datapanel =
			new EduArteDataPanel<LesweekKoppelingWrapper>("koppeling", listProvider, contentDesc);
		datapanel.setRowFactory(rowfactory);
		datapanel.setReuseItems(true);
		datapanel.setItemsPerPage(Integer.MAX_VALUE);

		form.add(datapanel);
		add(form);
		createComponents();

	}

	private List<LesweekKoppelingWrapper> getList(OrganisatieEenheid org,
			List<LesweekKoppelingWrapper> list)
	{

		list.add(new LesweekKoppelingWrapper(ModelFactory.getModel(org), null));
		for (OrganisatieEenheidLocatie oel : org.getActieveLocaties(TimeUtil.getInstance()
			.currentDate()))
		{
			list.add(new LesweekKoppelingWrapper(ModelFactory.getModel(org), ModelFactory
				.getModel(oel.getLocatie())));
		}

		for (OrganisatieEenheid oe : org.getActieveDirectChildren(TimeUtil.getInstance()
			.currentDate()))
		{
			getList(oe, list);
		}
		return list;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form));
		// panel.addButton(new AnnulerenButton(panel, new
		// LesweekIndelingKoppelingOverzicht()));
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(choicesModel);
	}

}
