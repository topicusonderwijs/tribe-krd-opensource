package nl.topicus.eduarte.web.pages.beheer.onderwijs;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ToevoegenButton;
import nl.topicus.eduarte.core.principals.beheer.onderwijs.Teams;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.opleiding.Team;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CodeNaamActiefTable;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamActiefZoekFilterPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.CodeNaamActiefZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.data.IDataProvider;

@PageInfo(title = "Teams", menu = "Beheer > Onderwijs > Teams")
@InPrincipal(Teams.class)
public class TeamZoekenPage extends AbstractBeheerPage<Void>
{
	private static CodeNaamActiefZoekFilter<Team> getDefaultFilter()
	{
		CodeNaamActiefZoekFilter<Team> ret = new CodeNaamActiefZoekFilter<Team>(Team.class);
		ret.setActief(true);
		return ret;
	}

	@SuppressWarnings("unchecked")
	public TeamZoekenPage()
	{
		super(BeheerMenuItem.Teams);
		CodeNaamActiefZoekFilter<Team> filter = getDefaultFilter();
		IDataProvider<Team> dataprovider =
			new GeneralFilteredSortableDataProvider(filter,
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);

		CodeNaamActiefTable<Team> table = new CodeNaamActiefTable<Team>("Teams");
		EduArteDataPanel<Team> datapanel =
			new EduArteDataPanel<Team>("datapanel", dataprovider, table);
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<Team>(TeamEditPage.class));
		add(datapanel);
		CodeNaamActiefZoekFilterPanel filterPanel =
			new CodeNaamActiefZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ToevoegenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				Team team = new Team();
				return new TeamEditPage(team, TeamZoekenPage.this);
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return TeamEditPage.class;
			}
		}));
	}
}
