package nl.topicus.eduarte.krd.web.pages.beheer.bron.terugkoppeling;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.SortableCollectionDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.IBronTerugkoppeling;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtInzien;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.BronTerugkoppelingTable;
import nl.topicus.eduarte.krd.web.components.panels.filter.BronTerugkoppelingZoekFilterPanel;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.AbstractBronPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.BronAlgemeenPage;
import nl.topicus.eduarte.krd.zoekfilters.BronTerugkoppelingZoekFilter;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.LoadableDetachableModel;

@PageInfo(title = "BRON Terugkoppelingen", menu = "Deelnemer")
@InPrincipal(BronOverzichtInzien.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class BronTerugkoppelbestandenPage extends AbstractBronPage
{
	private BronTerugkoppelingZoekFilter filter;

	private final class TerugkoppelingListModel extends
			LoadableDetachableModel<List<IBronTerugkoppeling>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<IBronTerugkoppeling> load()
		{
			List<IBronTerugkoppeling> list =
				DataAccessRegistry.getHelper(BronDataAccessHelper.class)
					.getTerugkoppelingen(filter);
			return list;
		}
	}

	public BronTerugkoppelbestandenPage(final BronTerugkoppelingZoekFilter filter)
	{
		this.filter = filter;
		SortableCollectionDataProvider<IBronTerugkoppeling> provider =
			new SortableCollectionDataProvider<IBronTerugkoppeling>(new TerugkoppelingListModel());
		BronTerugkoppelingTable table = new BronTerugkoppelingTable();
		EduArteDataPanel<IBronTerugkoppeling> datapanel =
			new EduArteDataPanel<IBronTerugkoppeling>("datapanel", provider, table);
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<IBronTerugkoppeling>(
			BronTerugkoppelbestandDetailsPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<IBronTerugkoppeling> item)
			{
				setResponsePage(new BronTerugkoppelbestandDetailsPage(item.getModelObject(), filter));
			}
		});

		add(datapanel);
		if (filter.getOrderByList().isEmpty())
		{
			filter.addOrderByProperty("bronOnderwijssoort");
			filter.addOrderByProperty("bRONBatchNummer");
		}
		add(new BronTerugkoppelingZoekFilterPanel("filter", filter, datapanel));
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new TerugButton(panel, BronAlgemeenPage.class));
		super.fillBottomRow(panel);
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(BronTerugkoppelingZoekFilter.class);
		ctorArgValues.add(filter);
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(filter);
		super.onDetach();
	}
}
