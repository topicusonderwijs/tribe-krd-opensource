package nl.topicus.eduarte.jobs.rapportage;

import java.util.Date;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.quartz.JobSegment;
import nl.topicus.cobra.quartz.SingleSegment;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.JobRunDataAccessHelper;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.entities.jobs.logging.RapportageCleanJobRun;
import nl.topicus.eduarte.entities.jobs.logging.RapportageJobRun;
import nl.topicus.eduarte.jobs.EduArteJob;

import org.hibernate.FlushMode;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Taak om de RapportageJobRun tabel elke 48 uur leeg te maken.
 * 
 * @author hoeve
 */
@JobInfo(name = RapportageCleanJob.JOB_NAME)
@JobRunClass(RapportageCleanJobRun.class)
public class RapportageCleanJob extends EduArteJob
{
	public static final String JOB_NAME = "Rapportage Clean";

	@SuppressWarnings("hiding")
	private static final Logger log = LoggerFactory.getLogger(RapportageCleanJob.class);

	private RapportageJobRun run;

	protected JobSegment currentJobSegment = SingleSegment.ENTIRE_JOB;

	@SuppressWarnings("unchecked")
	@Override
	protected void executeJob(JobExecutionContext context)
	{
		try
		{
			prepareJob(context);
			setStatus("Taak uitvoeren");

			JobRunDataAccessHelper<RapportageJobRun, ? > helper =
				DataAccessRegistry.getHelper(JobRunDataAccessHelper.class);

			int deleteCount = helper.cleanupOldRuns(RapportageJobRun.class, new Date());

			JobRunDetail finishDetail = new JobRunDetail(run);
			finishDetail.setUitkomst("De taak heeft " + deleteCount + " items verwijderd.");
			finishDetail.setJobRun(run);
			run.getDetails().add(finishDetail);
		}
		catch (Exception e)
		{
			rollbackJob(e);
			log.error(e.getMessage(), e);
		}

		setStatus("Taak afgerond");
		finishJob();
	}

	/**
	 * Voert een batchRollback uit en voegt een detail item toe aan de JobRun om meer
	 * informatie te bieden aan de gebruiker.
	 * 
	 * @param e
	 */
	private void rollbackJob(Exception e)
	{
		JobRunDetail failDetail = new JobRunDetail(run);
		failDetail.setUitkomst("Er is een fout op getreden: " + e.getMessage());
		failDetail.setJobRun(run);
		run.getDetails().add(failDetail);

		run.rollback();
	}

	/**
	 * Stelt het resultaat in en commit alles.
	 */
	private void finishJob()
	{
		run.setRunEinde(TimeUtil.getInstance().currentDateTime());

		run.saveOrUpdate();
		run.commit();

		DataAccessRegistry.getHelper(SessionDataAccessHelper.class).getHibernateSessionProvider()
			.getSession().setFlushMode(FlushMode.AUTO);
	}

	/**
	 * Bereid de job voor op het genereren, maakt bv de JobRun en stelt de FlushMode in op
	 * MANUAL.
	 * 
	 * @param context
	 */
	protected void prepareJob(JobExecutionContext context)
	{
		setStatus("Taak voorbereiden");

		run = new RapportageJobRun();
		run.setRunStart(TimeUtil.getInstance().currentDateTime());
		run.setGestartDoor(getMedewerker());
		run.setGestartDoorAccount(getAccount());

		DataAccessRegistry.getHelper(SessionDataAccessHelper.class).getHibernateSessionProvider()
			.getSession().setFlushMode(FlushMode.COMMIT);
	}
}
