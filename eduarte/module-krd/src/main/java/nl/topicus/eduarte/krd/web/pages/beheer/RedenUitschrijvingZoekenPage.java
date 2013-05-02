package nl.topicus.eduarte.krd.web.pages.beheer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.RedenUitschrijvingDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.RedenUitschrijvingPrincipal;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.RedenUitschrijvingTable;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.filter.RedenUitschrijvingZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.RedenUitschrijvingZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Reden uitschrijving", menu = "Beheer > Reden uitschrijving")
@InPrincipal(RedenUitschrijvingPrincipal.class)
public class RedenUitschrijvingZoekenPage extends AbstractBeheerPage<Void>
{
	private static RedenUitschrijvingZoekFilter getDefaultFilter()
	{
		RedenUitschrijvingZoekFilter ret = new RedenUitschrijvingZoekFilter();
		ret.addOrderByProperty("naam");
		ret.setActief(true);
		return ret;
	}

	public RedenUitschrijvingZoekenPage()
	{
		super(BeheerMenuItem.RedenUitschrijving);

		RedenUitschrijvingZoekFilter filter = getDefaultFilter();
		IDataProvider<RedenUitschrijving> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter,
				RedenUitschrijvingDataAccessHelper.class);

		EduArteDataPanel<RedenUitschrijving> datapanel =
			new EduArteDataPanel<RedenUitschrijving>("datapanel", dataprovider,
				new RedenUitschrijvingTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<RedenUitschrijving>(
			RedenUitschrijvingEditPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<RedenUitschrijving> item)
			{
				setResponsePage(new RedenUitschrijvingEditPage(item.getModel(),
					RedenUitschrijvingZoekenPage.this));
			}
		});
		add(datapanel);

		RedenUitschrijvingZoekFilterPanel filterPanel =
			new RedenUitschrijvingZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ToevoegenButton(panel, "Nieuwe reden uitschrijving",
			RedenUitschrijvingEditPage.class, RedenUitschrijvingZoekenPage.this));
	}
}