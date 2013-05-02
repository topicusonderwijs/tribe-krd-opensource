package nl.topicus.eduarte.krd.web.pages.beheer.bron.overzichten;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.SortableCollectionDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.datapanel.columns.ClickableCustomPropertyColumn;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtInzien;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.BronMeldingTable;
import nl.topicus.eduarte.krd.web.components.panels.filter.BronMeldingZoekFilterPanel;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.AbstractBronPage;
import nl.topicus.eduarte.krd.zoekfilters.BronBatchZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.pages.deelnemer.deelnemerkaart.DeelnemerkaartPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

@PageInfo(title = "BRON Meldingen", menu = "Deelnemer")
@InPrincipal(BronOverzichtInzien.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class BronMeldingenPage extends AbstractBronPage
{
	private BronMeldingZoekFilter filter;

	private final class MeldingListModel extends LoadableDetachableModel<List<IBronMelding>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<IBronMelding> load()
		{
			List<IBronMelding> meldingen =
				DataAccessRegistry.getHelper(BronDataAccessHelper.class).getBronMeldingen(filter);
			Collections.sort(meldingen, new Comparator<IBronMelding>()
			{
				@Override
				public int compare(IBronMelding melding1, IBronMelding melding2)
				{
					return melding2.getCreatedAt().compareTo(melding1.getCreatedAt());
				}
			});
			return meldingen;
		}
	}

	public BronMeldingenPage(BronMeldingZoekFilter filter)
	{
		this.filter = filter;
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		SortableCollectionDataProvider<IBronMelding> provider =
			new SortableCollectionDataProvider<IBronMelding>(new MeldingListModel());
		BronMeldingTable table = new BronMeldingTable();
		table.addColumn(new ClickableCustomPropertyColumn<IBronMelding>("Deelnemer", "Deelnemer",
			"deelnemer.deelnemernummer", "deelnemer.deelnemernummer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(IModel<IBronMelding> model)
			{
				IBronMelding melding = model.getObject();
				setResponsePage(new DeelnemerkaartPage(melding.getDeelnemer()));
			}
		});
		EduArteDataPanel<IBronMelding> datapanel =
			new EduArteDataPanel<IBronMelding>("datapanel", provider, table);
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<IBronMelding>(
			BronMeldingDetailsPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<IBronMelding> item)
			{
				IBronMelding melding = item.getModelObject();
				setResponsePage(new BronMeldingDetailsPage(melding, BronMeldingenPage.this));
			}

		});
		add(datapanel);
		add(new BronMeldingZoekFilterPanel("filter", filter, datapanel));
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new TerugButton(panel, new IPageLink()
		{

			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new BronBatchesPage(new BronBatchZoekFilter(filter.getAanleverpunt(), filter
					.getSchooljaar()));
			}

			@Override
			public Class<BronBatchesPage> getPageIdentity()
			{
				return BronBatchesPage.class;
			}

		}));
		super.fillBottomRow(panel);
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(BronMeldingZoekFilter.class);
		ctorArgValues.add(filter);
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(filter);
		super.onDetach();
	}
}
