package nl.topicus.eduarte.krd.web.pages.deelnemer.examens;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.krd.entities.CriteriumbankControleJobRun;
import nl.topicus.eduarte.krd.jobs.CriteriumbankControleJob;
import nl.topicus.eduarte.krd.principals.deelnemer.examen.DeelnemerExamensCollectief;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.quartz.JobDataMap;

/**
 * @author vandekamp
 */
@PageInfo(title = "Criteriumbankcontrole", menu = "Deelnemer > Examens > Criteriumbankcontrole")
@InPrincipal(DeelnemerExamensCollectief.class)
public class CriteriumbankControleOverzichtPage extends
		AbstractJobBeheerPage<CriteriumbankControleJobRun>
{
	public CriteriumbankControleOverzichtPage()
	{
		super(CoreMainMenuItem.Deelnemer, CriteriumbankControleJob.class, null);
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return null;
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.Criteriumbankcontrole);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		// overschreven zodat er geen buttons gemaakt worden
	}
}
