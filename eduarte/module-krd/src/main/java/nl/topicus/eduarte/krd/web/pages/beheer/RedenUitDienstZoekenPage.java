package nl.topicus.eduarte.krd.web.pages.beheer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.RedenUitDienstDataAccessHelper;
import nl.topicus.eduarte.entities.personen.RedenUitDienst;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.RedenUitDienstPrincipal;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CodeNaamActiefTable;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamActiefZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.RedenUitDienstZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Reden uit dienst", menu = "Beheer > RedenUitDienst")
@InPrincipal(RedenUitDienstPrincipal.class)
public class RedenUitDienstZoekenPage extends AbstractBeheerPage<Void>
{
	private static RedenUitDienstZoekFilter getDefaultFilter()
	{
		RedenUitDienstZoekFilter ret = new RedenUitDienstZoekFilter();
		ret.setActief(true);
		return ret;
	}

	public RedenUitDienstZoekenPage()
	{
		super(BeheerMenuItem.RedenUitDienst);

		RedenUitDienstZoekFilter filter = getDefaultFilter();
		IDataProvider<RedenUitDienst> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter,
				RedenUitDienstDataAccessHelper.class);

		EduArteDataPanel<RedenUitDienst> datapanel =
			new EduArteDataPanel<RedenUitDienst>("datapanel", dataprovider,
				new CodeNaamActiefTable<RedenUitDienst>("Reden uit dienst"));
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<RedenUitDienst>(
			RedenUitDienstEditPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<RedenUitDienst> item)
			{
				setResponsePage(new RedenUitDienstEditPage(item.getModel(),
					RedenUitDienstZoekenPage.this));
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
		panel.addButton(new ToevoegenButton(panel, "Nieuwe reden uit dienst",
			RedenUitDienstEditPage.class, RedenUitDienstZoekenPage.this));
	}
}
