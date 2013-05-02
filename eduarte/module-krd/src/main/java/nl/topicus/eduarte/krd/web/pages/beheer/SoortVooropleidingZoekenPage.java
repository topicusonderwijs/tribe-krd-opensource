package nl.topicus.eduarte.krd.web.pages.beheer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.SoortVooropleidingDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.SoortVooropleidingPrincipal;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.SoortVooropleidingTable;
import nl.topicus.eduarte.web.components.panels.filter.SoortVooropleidingZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.SoortVooropleidingZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Soort vooropleiding", menu = "Beheer > Soort vooropleiding")
@InPrincipal(SoortVooropleidingPrincipal.class)
public class SoortVooropleidingZoekenPage extends AbstractBeheerPage<Void>
{
	private static SoortVooropleidingZoekFilter getDefaultFilter()
	{
		SoortVooropleidingZoekFilter ret = new SoortVooropleidingZoekFilter();
		ret.setActief(true);
		return ret;
	}

	public SoortVooropleidingZoekenPage()
	{
		super(BeheerMenuItem.SoortVooropleiding);

		SoortVooropleidingZoekFilter filter = getDefaultFilter();
		IDataProvider<SoortVooropleiding> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter,
				SoortVooropleidingDataAccessHelper.class);

		EduArteDataPanel<SoortVooropleiding> datapanel =
			new EduArteDataPanel<SoortVooropleiding>("datapanel", dataprovider,
				new SoortVooropleidingTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<SoortVooropleiding>(
			SoortVooropleidingEditPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<SoortVooropleiding> item)
			{
				setResponsePage(new SoortVooropleidingEditPage(item.getModel(),
					SoortVooropleidingZoekenPage.this));
			}
		});
		add(datapanel);

		SoortVooropleidingZoekFilterPanel filterPanel =
			new SoortVooropleidingZoekFilterPanel("filter", filter, datapanel, true);
		add(filterPanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ToevoegenButton(panel, "Nieuwe soort vooropleiding",
			SoortVooropleidingEditPage.class, SoortVooropleidingZoekenPage.this));
	}
}
