package nl.topicus.eduarte.krd.web.pages.beheer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.dao.helpers.UitkomstIntakegesprekDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.UitkomstIntakegesprek;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.UitkomtIntakegesprekPrincipal;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.UitkomstIntakegesprekTable;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamActiefZoekFilterPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.UitkomstIntakegesprekZoekFilter;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Uitkomst Intakegesprek", menu = "Beheer > Beheer tabellen > Soort financieringen")
@InPrincipal(UitkomtIntakegesprekPrincipal.class)
public class UitkomstIntakegesprekZoekenPage extends AbstractBeheerPage<Void>
{
	private static UitkomstIntakegesprekZoekFilter getDefaultFilter()
	{
		UitkomstIntakegesprekZoekFilter ret = new UitkomstIntakegesprekZoekFilter();
		ret.setActief(true);
		return ret;
	}

	public UitkomstIntakegesprekZoekenPage()
	{
		super(BeheerMenuItem.UitkomstIntakegesprek);

		UitkomstIntakegesprekZoekFilter filter = getDefaultFilter();
		IDataProvider<UitkomstIntakegesprek> dataprovider =
			GeneralFilteredSortableDataProvider.of(filter,
				UitkomstIntakegesprekDataAccessHelper.class);

		EduArteDataPanel<UitkomstIntakegesprek> datapanel =
			new EduArteDataPanel<UitkomstIntakegesprek>("datapanel", dataprovider,
				new UitkomstIntakegesprekTable());
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<UitkomstIntakegesprek>(
			UitkomstIntakegesprekEditPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(Item<UitkomstIntakegesprek> item)
			{
				setResponsePage(new UitkomstIntakegesprekEditPage(item.getModel(),
					UitkomstIntakegesprekZoekenPage.this));
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
		panel.addButton(new ToevoegenButton(panel, "Nieuwe uitkomst",
			UitkomstIntakegesprekEditPage.class, UitkomstIntakegesprekZoekenPage.this));
	}
}
