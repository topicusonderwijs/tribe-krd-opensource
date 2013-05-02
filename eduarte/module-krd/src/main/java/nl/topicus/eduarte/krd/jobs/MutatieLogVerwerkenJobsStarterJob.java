package nl.topicus.eduarte.krd.jobs;

import static nl.topicus.cobra.dao.DataAccessRegistry.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.topicus.cobra.quartz.CobraScheduler;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteScheduler;
import nl.topicus.eduarte.converters.NaamEntiteitStringConverter;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krd.dao.helpers.MutatieLogVerwerkenJobRunDataAccessHelper;
import nl.topicus.eduarte.krd.dao.helpers.MutatieLogVerwerkersLogDataAccessHelper;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerkenJobRun;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerkenJobsStarterJobRun;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerkersLog;
import nl.topicus.eduarte.krd.zoekfilters.MutatieLogVerwerkersLogZoekFilter;

import org.apache.wicket.model.Model;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

@JobInfo(name = MutatieLogVerwerkenJobsStarterJob.JOB_NAME)
@JobRunClass(MutatieLogVerwerkenJobsStarterJobRun.class)
public class MutatieLogVerwerkenJobsStarterJob extends EduArteJob
{
	public static final String JOB_NAME = "Mutatielogverwerken jobs starter";

	private static boolean hasRun = false;

	@Override
	protected void executeJob(JobExecutionContext context) throws InterruptedException
	{
		if (EduArteApp.get().getConfiguration().isOntwikkel())
		{
			if (!hasRun)
			{
				log.info("MutatieLogVerwerkenJobsStarterJob draait niet in development, "
					+ "start de jobs via Beheer > Systeem > Mutatielogverwerkers");
				hasRun = true;
			}
			return;
		}
		setStatus("Starten van de actieve mutatielog jobs voor alle organisaties");
		Set<Organisatie> organisaties = getOrganisatiesVoorMutatieLogVerwerking();
		int i = 0, aantal = organisaties.size();
		for (Organisatie organisatie : organisaties)
		{
			setProgress(i, aantal);
			startVerwerkerJob(context, organisatie);
		}

		logJobRun(organisaties);
		MutatieLogVerwerkenJobRunDataAccessHelper helper =
			getHelper(MutatieLogVerwerkenJobRunDataAccessHelper.class);

		TimeUtil util = TimeUtil.getInstance();
		Date vorigeWeek = util.addWeeks(util.currentDate(), -1);
		helper.cleanupOldRuns(MutatieLogVerwerkenJobRun.class, vorigeWeek);
		helper.cleanupOldRuns(MutatieLogVerwerkenJobsStarterJobRun.class, vorigeWeek);
		helper.batchExecute();
	}

	private Set<Organisatie> getOrganisatiesVoorMutatieLogVerwerking()
	{
		MutatieLogVerwerkersLogDataAccessHelper verwerkersHelper =
			getHelper(MutatieLogVerwerkersLogDataAccessHelper.class);

		MutatieLogVerwerkersLogZoekFilter filter = new MutatieLogVerwerkersLogZoekFilter();
		filter.setActief(true);
		List<MutatieLogVerwerkersLog> verwerkers = verwerkersHelper.list(filter);

		Set<Organisatie> organisaties = new HashSet<Organisatie>();
		for (MutatieLogVerwerkersLog verwerker : verwerkers)
		{
			Organisatie organisatie = verwerker.getOrganisatie();

			// alleen actieve organisaties meenemen
			if (organisatie.isActief())
				organisaties.add(organisatie);
		}
		return organisaties;
	}

	private void startVerwerkerJob(JobExecutionContext context, Organisatie organisatie)
	{
		EduArteContext subContext = EduArteContext.get().copy();
		subContext.setOrganisatie(organisatie);
		Date peildatum = getValue(context, CobraScheduler.PEILDATUM_KEY);
		if (peildatum == null)
		{
			peildatum = TimeUtil.getInstance().currentDate();
		}
		subContext.setPeildatumModel(new Model<Date>(peildatum));

		final EduArteScheduler scheduler = EduArteApp.get().getEduarteScheduler();

		// start alleen een job als er nog geen in de queue staat of al verwerkt wordt
		if (scheduler.getExecutingAndWaitingJobs(MutatieLogVerwerkenJob.class, organisatie)
			.isEmpty())
		{
			EduArteContext.get().runInContext(new Runnable()
			{

				@Override
				public void run()
				{
					try
					{
						log.info("Triggering mutatielogverwerkerjob voor "
							+ EduArteContext.get().getOrganisatie().getNaam());
						scheduler.triggerJob(MutatieLogVerwerkenJob.class);
					}
					catch (SchedulerException e)
					{
						log.error(e.toString(), e);
					}
				}
			}, subContext);
		}
	}

	private void logJobRun(Set<Organisatie> organisaties)
	{
		MutatieLogVerwerkenJobsStarterJobRun jobrun = new MutatieLogVerwerkenJobsStarterJobRun();
		jobrun.setRunStart(getDatumTijdOpgestart());
		jobrun.setRunEinde(new Date());
		jobrun.setSamenvatting("Mutatielogverwerker jobs gestart voor "
			+ StringUtil.toString(organisaties, "", new NaamEntiteitStringConverter()));
		jobrun.save();
		jobrun.commit();
	}
}
