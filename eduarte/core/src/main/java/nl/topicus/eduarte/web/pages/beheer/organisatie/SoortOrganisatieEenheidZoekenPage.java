package nl.topicus.eduarte.web.pages.beheer.organisatie;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.core.principals.beheer.organisatie.OrganisatiemodelPrincipal;
import nl.topicus.eduarte.dao.helpers.SoortOrganisatieEenheidDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.SoortOrganisatieEenheid;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.SoortOrganisatieEenheidTable;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamActiefZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.SoortOrganisatieEenheidZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Soort organisatie-eenheid", menu = "Beheer > Beheer tabellen > [SoortOrganisatieEenheid]")
@InPrincipal(OrganisatiemodelPrincipal.class)
public class SoortOrganisatieEenheidZoekenPage extends AbstractBeheerPage<Void>
{
	private static SoortOrganisatieEenheidZoekFilter getDefaultFilter()
	{
		SoortOrganisatieEenheidZoekFilter ret = new SoortOrganisatieEenheidZoekFilter();
		ret.setActief(true);
		return ret;
	}

	public SoortOrganisatieEenheidZoekenPage()
	{
		super(BeheerMenuItem.SoortOrganisatie_Eenheden);

		SoortOrganisatieEenheidZoekFilter filter = getDefaultFilter();
		IDataProvider<SoortOrganisatieEenheid> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter,
				SoortOrganisatieEenheidDataAccessHelper.class);

		EduArteDataPanel<SoortOrganisatieEenheid> datapanel =
			new EduArteDataPanel<SoortOrganisatieEenheid>("datapanel", dataprovider,
				new SoortOrganisatieEenheidTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<SoortOrganisatieEenheid>(
			SoortOrganisatieEenheidEditPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<SoortOrganisatieEenheid> item)
			{
				setResponsePage(new SoortOrganisatieEenheidEditPage(item.getModel(),
					SoortOrganisatieEenheidZoekenPage.this));
			}
		});
		add(datapanel);

		CodeNaamActiefZoekFilterPanel filterPanel =
			new CodeNaamActiefZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ToevoegenButton(panel, "Nieuw soort organisatie-eenheid",
			SoortOrganisatieEenheidEditPage.class, SoortOrganisatieEenheidZoekenPage.this));

	}
}
