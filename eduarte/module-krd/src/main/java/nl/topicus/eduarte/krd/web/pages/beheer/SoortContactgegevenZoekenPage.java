package nl.topicus.eduarte.krd.web.pages.beheer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.SoortContactgegevenDataAccessHelper;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.SoortContactgevenPrincipal;
import nl.topicus.eduarte.krd.web.components.panels.filter.SoortContactgegevenZoekFilterPanel;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.SoortContactgegevenTable;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.SoortContactgegevenZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Soort contactgegevens", menu = "Beheer > Soort contactgegevens")
@InPrincipal(SoortContactgevenPrincipal.class)
public class SoortContactgegevenZoekenPage extends AbstractBeheerPage<Void>
{
	private static SoortContactgegevenZoekFilter getDefaultFilter()
	{
		SoortContactgegevenZoekFilter ret = new SoortContactgegevenZoekFilter();
		ret.addOrderByProperty("code");
		ret.setActief(true);
		return ret;
	}

	public SoortContactgegevenZoekenPage()
	{
		super(BeheerMenuItem.SoortContactgegevens);

		SoortContactgegevenZoekFilter filter = getDefaultFilter();
		IDataProvider<SoortContactgegeven> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter,
				SoortContactgegevenDataAccessHelper.class);

		EduArteDataPanel<SoortContactgegeven> datapanel =
			new EduArteDataPanel<SoortContactgegeven>("datapanel", dataprovider,
				new SoortContactgegevenTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<SoortContactgegeven>(
			SoortContactgegevenEditPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<SoortContactgegeven> item)
			{
				setResponsePage(new SoortContactgegevenEditPage(item.getModel(),
					SoortContactgegevenZoekenPage.this));
			}
		});
		add(datapanel);

		SoortContactgegevenZoekFilterPanel filterPanel =
			new SoortContactgegevenZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ToevoegenButton(panel, "Nieuw soort contactgegeven",
			SoortContactgegevenEditPage.class, SoortContactgegevenZoekenPage.this));

	}
}
