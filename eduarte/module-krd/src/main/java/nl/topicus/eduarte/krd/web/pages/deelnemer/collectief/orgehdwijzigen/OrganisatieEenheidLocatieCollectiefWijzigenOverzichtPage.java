package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.orgehdwijzigen;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerVerbintenissenWrite;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.quartz.JobDataMap;

@PageInfo(title = "Organisatie-eenheid en locatie collectief wijzigen overzicht", menu = "Deelnemer")
@InPrincipal(DeelnemerVerbintenissenWrite.class)
public class OrganisatieEenheidLocatieCollectiefWijzigenOverzichtPage extends
		AbstractJobBeheerPage<OrganisatieEenheidLocatieCollectiefWijzigenJobRun>
{
	public OrganisatieEenheidLocatieCollectiefWijzigenOverzichtPage()
	{
		super(CoreMainMenuItem.Deelnemer, OrganisatieEenheidLocatieCollectiefWijzigenJob.class,
			null);
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return null;
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.OrgEhdLocatieWijzigen);
	}

	@Override
	protected AbstractBottomRowButton createTaakStartenButton(BottomRowPanel panel)
	{
		return new PageLinkButton(panel, "Organisatie-eenheid wijzigen", new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new OrganisatieEenheidLocatieCollectiefWijzigenPage();
			}

			@Override
			public Class<OrganisatieEenheidLocatieCollectiefWijzigenPage> getPageIdentity()
			{
				return OrganisatieEenheidLocatieCollectiefWijzigenPage.class;
			}
		});
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}
}