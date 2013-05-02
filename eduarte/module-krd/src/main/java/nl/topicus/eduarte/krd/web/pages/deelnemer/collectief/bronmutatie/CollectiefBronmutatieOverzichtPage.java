package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.bronmutatie;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.eduarte.core.principals.App;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.quartz.JobDataMap;

@PageInfo(title = "Collectief bronmutaties aanmaken overzicht", menu = "Deelnemer")
@InPrincipal(App.class)
public class CollectiefBronmutatieOverzichtPage extends
		AbstractJobBeheerPage<CollectiefBronmutatieJobRun>
{
	public CollectiefBronmutatieOverzichtPage()
	{
		super(CoreMainMenuItem.Deelnemer, CollectiefBronmutatieJob.class, null);
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return null;
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.BronMutatiesAanmaken);
	}

	@Override
	protected AbstractBottomRowButton createTaakStartenButton(BottomRowPanel panel)
	{
		return new PageLinkButton(panel, "BRON-mutaties aanmaken", new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new CollectiefBronmutatiePage();
			}

			@Override
			public Class<CollectiefBronmutatiePage> getPageIdentity()
			{
				return CollectiefBronmutatiePage.class;
			}
		});
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}
}