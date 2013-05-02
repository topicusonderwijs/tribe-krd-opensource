package nl.topicus.eduarte.krd.web.pages.beheer.contract;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.SoortContractDataAccessHelper;
import nl.topicus.eduarte.entities.contract.SoortContract;
import nl.topicus.eduarte.krd.principals.beheer.SoortContracten;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.SoortContractTable;
import nl.topicus.eduarte.web.components.panels.filter.SoortContractZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.SoortContractZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Soort contracten", menu = "Beheer > Beheer tabellen >Soort contracten")
@InPrincipal(SoortContracten.class)
public class SoortContractZoekenPage extends AbstractBeheerPage<Void>
{
	private static SoortContractZoekFilter getDefaultFilter()
	{
		SoortContractZoekFilter ret = new SoortContractZoekFilter();
		ret.setActief(true);
		return ret;
	}

	public SoortContractZoekenPage()
	{
		super(BeheerMenuItem.SoortContracten);

		SoortContractZoekFilter filter = getDefaultFilter();
		IDataProvider<SoortContract> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter,
				SoortContractDataAccessHelper.class);

		EduArteDataPanel<SoortContract> datapanel =
			new EduArteDataPanel<SoortContract>("datapanel", dataprovider, new SoortContractTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<SoortContract>(
			SoortContractEditPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<SoortContract> item)
			{
				setResponsePage(new SoortContractEditPage(item.getModel(),
					SoortContractZoekenPage.this));
			}
		});
		add(datapanel);

		SoortContractZoekFilterPanel filterPanel =
			new SoortContractZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ToevoegenButton(panel, "Nieuwe soort contract",
			SoortContractEditPage.class, SoortContractZoekenPage.this));
	}
}
