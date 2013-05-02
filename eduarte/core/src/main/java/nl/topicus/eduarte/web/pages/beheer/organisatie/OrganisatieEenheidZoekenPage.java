package nl.topicus.eduarte.web.pages.beheer.organisatie;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.core.principals.beheer.organisatie.OrganisatiemodelPrincipal;
import nl.topicus.eduarte.dao.helpers.OrganisatieEenheidDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OrganisatieEenheidTable;
import nl.topicus.eduarte.web.components.panels.filter.OrganisatieEenheidZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Organisatie-eenheid", menu = "Beheer > Organisatie-eenheid")
@InPrincipal(OrganisatiemodelPrincipal.class)
public class OrganisatieEenheidZoekenPage extends AbstractBeheerPage<Void>
{
	private static OrganisatieEenheidZoekFilter getDefaultFilter()
	{
		OrganisatieEenheidZoekFilter ret = new OrganisatieEenheidZoekFilter();
		ret.addOrderByProperty("naam");
		return ret;
	}

	public OrganisatieEenheidZoekenPage()
	{
		super(BeheerMenuItem.Organisatie_eenheden);
		OrganisatieEenheidZoekFilter filter = getDefaultFilter();
		IDataProvider<OrganisatieEenheid> dataprovider =
			GeneralFilteredSortableDataProvider
				.of(filter, OrganisatieEenheidDataAccessHelper.class);

		final EduArteDataPanel<OrganisatieEenheid> datapanel =
			new EduArteDataPanel<OrganisatieEenheid>("datapanel", dataprovider,
				new OrganisatieEenheidTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<OrganisatieEenheid>(
			OrganisatieEenheidKaartPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<OrganisatieEenheid> item)
			{
				pushSearchResultToNavigationLevel(datapanel, item.getIndex());
				setResponsePage(new OrganisatieEenheidKaartPage(item.getModel(),
					OrganisatieEenheidZoekenPage.this));
			}
		});
		add(datapanel);

		OrganisatieEenheidZoekFilterPanel filterPanel =
			new OrganisatieEenheidZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ToevoegenButton(panel, "Nieuwe organisatie-eenheid",
			OrganisatieEenheidEditPage.class, OrganisatieEenheidZoekenPage.this));
		panel.addButton(new PageLinkButton(panel, "Logo instellen", LogoInstellenPage.class));

	}
}
