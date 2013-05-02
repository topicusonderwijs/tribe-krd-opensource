package nl.topicus.eduarte.web.pages.beheer.organisatie;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.core.principals.beheer.organisatie.OrganisatiemodelPrincipal;
import nl.topicus.eduarte.dao.helpers.LocatieDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.LocatieTable;
import nl.topicus.eduarte.web.components.panels.filter.LocatieZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.LocatieZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Locatie", menu = "Beheer > Locatie")
@InPrincipal(OrganisatiemodelPrincipal.class)
public class LocatieZoekenPage extends AbstractBeheerPage<Void>
{
	private static LocatieZoekFilter getDefaultFilter()
	{
		LocatieZoekFilter ret = new LocatieZoekFilter();
		return ret;
	}

	public LocatieZoekenPage()
	{
		super(BeheerMenuItem.Locaties);

		LocatieZoekFilter filter = getDefaultFilter();
		IDataProvider<Locatie> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter, LocatieDataAccessHelper.class);

		final EduArteDataPanel<Locatie> datapanel =
			new EduArteDataPanel<Locatie>("datapanel", dataprovider, new LocatieTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Locatie>(
			LocatieKaartPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<Locatie> item)
			{
				pushSearchResultToNavigationLevel(datapanel, item.getIndex());
				setResponsePage(new LocatieKaartPage(item.getModel(), LocatieZoekenPage.this));
			}
		});
		add(datapanel);

		LocatieZoekFilterPanel filterPanel =
			new LocatieZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ToevoegenButton(panel, "Nieuwe locatie", LocatieEditPage.class,
			LocatieZoekenPage.this));
	}
}
