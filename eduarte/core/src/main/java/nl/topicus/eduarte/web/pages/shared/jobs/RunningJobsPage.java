package nl.topicus.eduarte.web.pages.shared.jobs;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.core.principals.beheer.systeem.Jobs;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.JobDescriptionTable;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.util.time.Duration;

/**
 * Pagina met alle lopende taken van de applicatie.
 * 
 * @author papegaaij
 */
@PageInfo(title = "Lopende taken", menu = "Beheer > Systeem > Lopende taken")
@InPrincipal(Jobs.class)
@RechtenSoorten( {RechtenSoort.INSTELLING, RechtenSoort.BEHEER})
public class RunningJobsPage extends AbstractBeheerPage<Void>
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public RunningJobsPage()
	{
		super(BeheerMenuItem.LopendeTaken);

		EduArteDataPanel jobs =
			new EduArteDataPanel("datapanel", EduArteApp.get().getEduarteScheduler()
				.getExecutingAndWaitingJobs(EduArteJob.class), new JobDescriptionTable());
		jobs.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(10)));
		add(jobs);

		createComponents();
	}
}
