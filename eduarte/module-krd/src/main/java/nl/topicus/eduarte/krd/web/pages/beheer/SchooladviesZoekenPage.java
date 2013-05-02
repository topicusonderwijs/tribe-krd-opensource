package nl.topicus.eduarte.krd.web.pages.beheer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.SchooladviesDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Schooladvies;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.SchooladviesPrincipal;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.SchooladviesTable;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.filter.NaamActiefZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.SchooladviesZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Schooladvies", menu = "Beheer > Schooladvies")
@InPrincipal(SchooladviesPrincipal.class)
public class SchooladviesZoekenPage extends AbstractBeheerPage<Void>
{
	private static SchooladviesZoekFilter getDefaultFilter()
	{
		SchooladviesZoekFilter ret = new SchooladviesZoekFilter();
		ret.setActief(true);
		return ret;
	}

	public SchooladviesZoekenPage()
	{
		super(BeheerMenuItem.Schooladvies);

		SchooladviesZoekFilter filter = getDefaultFilter();
		IDataProvider<Schooladvies> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter,
				SchooladviesDataAccessHelper.class);

		EduArteDataPanel<Schooladvies> datapanel =
			new EduArteDataPanel<Schooladvies>("datapanel", dataprovider, new SchooladviesTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Schooladvies>(
			SchooladviesEditPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<Schooladvies> item)
			{
				setResponsePage(new SchooladviesEditPage(item.getModel(),
					SchooladviesZoekenPage.this));
			}
		});
		add(datapanel);

		NaamActiefZoekFilterPanel filterPanel =
			new NaamActiefZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ToevoegenButton(panel, "Nieuw schooladvies",
			SchooladviesEditPage.class, SchooladviesZoekenPage.this));
	}
}
