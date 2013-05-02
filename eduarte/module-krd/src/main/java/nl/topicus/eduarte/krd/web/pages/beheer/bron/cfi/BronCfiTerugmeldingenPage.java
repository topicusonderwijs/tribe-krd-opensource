package nl.topicus.eduarte.krd.web.pages.beheer.bron.cfi;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.krd.dao.helpers.BronCfiTerugmeldingDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.cfi.BronCfiTerugmelding;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtInzien;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.BronCfiTerugmeldingTable;
import nl.topicus.eduarte.krd.web.components.panels.filter.BronCfiTerugmeldingZoekFilterPanel;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.AbstractBronPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.BronAlgemeenPage;
import nl.topicus.eduarte.krd.zoekfilters.BronCfiTerugmeldingZoekFilter;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

import org.apache.wicket.markup.repeater.Item;

@PageInfo(title = "BRON CFI-terugmeldingen", menu = "Deelnemer > BRON > CFI-terugmeldingen")
@InPrincipal(BronOverzichtInzien.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class BronCfiTerugmeldingenPage extends AbstractBronPage
{
	private static final long serialVersionUID = 1L;

	public BronCfiTerugmeldingenPage()
	{
		BronCfiTerugmeldingZoekFilter filter = new BronCfiTerugmeldingZoekFilter();
		GeneralFilteredSortableDataProvider<BronCfiTerugmelding, BronCfiTerugmeldingZoekFilter> provider =
			GeneralFilteredSortableDataProvider.of(filter,
				BronCfiTerugmeldingDataAccessHelper.class);
		EduArteDataPanel<BronCfiTerugmelding> datapanel =
			new EduArteDataPanel<BronCfiTerugmelding>("datapanel", provider,
				new BronCfiTerugmeldingTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<BronCfiTerugmelding>(
			BronCfiTerugmeldingPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<BronCfiTerugmelding> item)
			{
				BronCfiTerugmelding terugmelding = item.getModelObject();
				setResponsePage(new BronCfiTerugmeldingPage(terugmelding));
			}
		});
		datapanel.setItemsPerPage(20);
		add(datapanel);
		BronCfiTerugmeldingZoekFilterPanel filterPanel =
			new BronCfiTerugmeldingZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new PageLinkButton(panel, "CFI-terugmelding inlezen", ButtonAlignment.LEFT,
			BronCfiTerugmeldingInlezenPage.class));
		panel.addButton(new TerugButton(panel, BronAlgemeenPage.class));
	}

}
