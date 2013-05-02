package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.verbintenis;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerVerbintenissenWrite;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.quartz.JobDataMap;

@PageInfo(title = "Verbintenissen collectief wijzigen", menu = "Deelnemer")
@InPrincipal(DeelnemerVerbintenissenWrite.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class VerbintenisCollectiefEditOverzichtPage extends
		AbstractJobBeheerPage<VerbintenissenStatusEditJobRun>
{
	public VerbintenisCollectiefEditOverzichtPage()
	{
		super(CoreMainMenuItem.Deelnemer, VerbintenissenStatusEditJob.class, null);
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return null;
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.Verbintenissen);
	}

	@Override
	protected AbstractBottomRowButton createTaakStartenButton(BottomRowPanel panel)
	{
		return new PageLinkButton(panel, "Collectieve statusovergang", new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new VerbintenisCollectiefStatusovergangPage();
			}

			@Override
			public Class<VerbintenisCollectiefStatusovergangPage> getPageIdentity()
			{
				return VerbintenisCollectiefStatusovergangPage.class;
			}
		});
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new PageLinkButton(panel, "Collectief afdrukken",
			VerbintenisCollectiefAfdrukkenPage.class));
		panel.addButton(new PageLinkButton(panel, "Collectief beëindigen",
			VerbintenisCollectiefBeeindigenPage.class));
		super.fillBottomRow(panel);

	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}
}