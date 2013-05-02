package nl.topicus.eduarte.krd.web.pages.beheer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.RelatieSoortDataAccesHelper;
import nl.topicus.eduarte.entities.personen.RelatieSoort;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.RelatieSoortPrincipal;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.RelatieSoortTable;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamActiefZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.RelatieSoortZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

/**
 * 
 * 
 * @author vanharen
 */
@PageInfo(title = "Relatie soort", menu = "Beheer > Relatie soort")
@InPrincipal(RelatieSoortPrincipal.class)
public class RelatieSoortZoekenPage extends AbstractBeheerPage<Void>
{
	private static RelatieSoortZoekFilter getDefaultFilter()
	{
		RelatieSoortZoekFilter ret = new RelatieSoortZoekFilter();
		ret.setActief(true);
		return ret;
	}

	public RelatieSoortZoekenPage()
	{
		super(BeheerMenuItem.Relatiesoort);

		RelatieSoortZoekFilter filter = getDefaultFilter();
		IDataProvider<RelatieSoort> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter,
				RelatieSoortDataAccesHelper.class);

		EduArteDataPanel<RelatieSoort> datapanel =
			new EduArteDataPanel<RelatieSoort>("datapanel", dataprovider, new RelatieSoortTable(
				"Relatie soort"));
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<RelatieSoort>(
			RelatieSoortEditPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<RelatieSoort> item)
			{
				setResponsePage(new RelatieSoortEditPage(item.getModel(),
					RelatieSoortZoekenPage.this));
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
		panel.addButton(new ToevoegenButton(panel, "Nieuwe relatie soort",
			RelatieSoortEditPage.class, RelatieSoortZoekenPage.this));
	}
}
