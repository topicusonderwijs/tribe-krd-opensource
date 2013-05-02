package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.bpv;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerBPVWrite;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.quartz.JobDataMap;

@PageInfo(title = "BPV inschrijvingen collectief wijzigen", menu = "Deelnemer")
@InPrincipal(DeelnemerBPVWrite.class)
public class BPVInschrijvingCollectiefEditOverzichtPage extends
		AbstractJobBeheerPage<BPVInschrijvingCollectiefEditJobRun>
{
	public BPVInschrijvingCollectiefEditOverzichtPage()
	{
		super(CoreMainMenuItem.Deelnemer, BPVInschrijvingCollectiefEditJob.class, null);
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return null;
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.BPVs);
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
				return new BPVInschrijvingCollectiefStatusovergangPage();
			}

			@Override
			public Class<BPVInschrijvingCollectiefStatusovergangPage> getPageIdentity()
			{
				return BPVInschrijvingCollectiefStatusovergangPage.class;
			}
		});
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new PageLinkButton(panel, "Collectief afdrukken",
			BPVInschrijvingCollectiefAfdrukkenPage.class));
		panel.addButton(new PageLinkButton(panel, "Collectief beëindigen",
			BPVInschrijvingCollectiefBeeindigenPage.class));
		super.fillBottomRow(panel);
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}
}