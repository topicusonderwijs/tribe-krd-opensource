package nl.topicus.eduarte.krd.web.pages.beheer.bron.foto;

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
import nl.topicus.eduarte.krd.dao.helpers.BronFotobestandDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestand;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtInzien;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.BronFotobestandTable;
import nl.topicus.eduarte.krd.web.components.panels.filter.BronFotobestandZoekFilterPanel;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.AbstractBronPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.BronAlgemeenPage;
import nl.topicus.eduarte.krd.zoekfilters.BronFotobestandZoekFilter;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

import org.apache.wicket.markup.repeater.Item;

@PageInfo(title = "BRON Foto's", menu = "Deelnemer > BRON > Fotobestanden")
@InPrincipal(BronOverzichtInzien.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class BronFotobestandenPage extends AbstractBronPage
{
	private static final long serialVersionUID = 1L;

	public BronFotobestandenPage()
	{
		BronFotobestandZoekFilter filter = new BronFotobestandZoekFilter();
		GeneralFilteredSortableDataProvider<BronFotobestand, BronFotobestandZoekFilter> provider =
			GeneralFilteredSortableDataProvider.of(filter,
				BronFotobestandDataAccessHelper.class);
		EduArteDataPanel<BronFotobestand> datapanel =
			new EduArteDataPanel<BronFotobestand>("datapanel", provider, new BronFotobestandTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<BronFotobestand>(
			BronFotobestandPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<BronFotobestand> item)
			{
				BronFotobestand fotobestand = item.getModelObject();
				setResponsePage(new BronFotobestandPage(fotobestand));
			}
		});
		datapanel.setItemsPerPage(20);
		add(datapanel);
		BronFotobestandZoekFilterPanel filterPanel =
			new BronFotobestandZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new PageLinkButton(panel, "Foto inlezen", ButtonAlignment.LEFT,
			BronFotobestandInlezenPage.class));
		panel.addButton(new TerugButton(panel, BronAlgemeenPage.class));
	}

}
