/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.jobs;

import java.util.Date;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.quartz.CobraScheduler;
import nl.topicus.cobra.quartz.HibernateAwareCobraJob;
import nl.topicus.cobra.quartz.JobDescription;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.security.authentication.EduArteSubject;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.security.authentication.Account;

import org.apache.wicket.Application;
import org.apache.wicket.model.Model;
import org.apache.wicket.security.strategies.StrategyResolver;
import org.apache.wicket.security.strategies.WaspAuthorizationStrategy;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;

/**
 * Base class voor job. Zorgt dat hibernate gedoe goed zit e.d. Subclasses van deze class
 * dienen de JobInfo annotation te hebben.
 * 
 * @author marrink
 */
public abstract class EduArteJob extends HibernateAwareCobraJob
{
	/**
	 * Default group voor EduArte jobs.
	 */
	public static final String DEFAULT_GROUP = "EduArte";

	/**
	 * De peildatum die gebruikt moet worden voor deze job.
	 */
	private Date peildatum;

	private Organisatie organisatie;

	private Medewerker medewerker;

	private Account account;

	private EduArteApp eduArteApp;

	public static Class< ? extends JobRun> getJobRunClass(Class< ? extends EduArteJob> clazz)
	{
		JobRunClass ret = clazz.getAnnotation(JobRunClass.class);
		if (ret == null)
			throw new IllegalArgumentException(clazz + " must have a JobRunClass annotation");
		return ret.value();
	}

	public EduArteJob()
	{
	}

	/**
	 * Initialiseerd aantal zaken, roept vervolgens
	 * {@link #executeJob(JobExecutionContext)} aan.
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */

	@Override
	public final void execute(JobExecutionContext context) throws JobExecutionException
	{
		log.info(getClass().getSimpleName() + " opgestart");
		executingThread = Thread.currentThread();
		try
		{
			Session session =
				DataAccessRegistry.getHelper(SessionDataAccessHelper.class)
					.getHibernateSessionProvider().getSession();
			session.setFlushMode(FlushMode.COMMIT);

			fixDataMap(context.getJobDetail().getJobDataMap());
			fixDataMap(context.getTrigger().getJobDataMap());
			fixDataMap(context.getMergedJobDataMap());

			// get values from DataMap.
			organisatie = getValue(context, CobraScheduler.INSTELLING_KEY);
			peildatum = getValue(context, CobraScheduler.PEILDATUM_KEY);
			medewerker = getValue(context, CobraScheduler.MEDEWERKER_KEY);
			account = getValue(context, CobraScheduler.ACCOUNT_KEY);
			Account user =
				(account != null ? account : (medewerker != null ? medewerker.getAccount() : null));

			// set peildatum en organisatie
			EduArteContext.get().setPeildatumModel(new Model<Date>(peildatum));
			EduArteContext.get().setOrganisatie(organisatie);
			EduArteContext.get().setAccount(user);

			WaspAuthorizationStrategy.setStrategyResolver(new StrategyResolver()
			{
				private JobAuthorizationStrategy strategy;

				@Override
				public WaspAuthorizationStrategy getStrategy()
				{
					if (strategy == null)
						strategy =
							new JobAuthorizationStrategy(new EduArteSubject(EduArteContext.get()
								.getAccount()));
					return strategy;
				}
			});

			try
			{
				ApplicationContext appContext =
					(ApplicationContext) context.getScheduler().getContext().get(
						EduArteApp.APPLICATION_CONTEXT_SCHEDULER_CONTEXT_KEY);
				eduArteApp = (EduArteApp) appContext.getBean(EduArteApp.APPLICATION_KEY);
				Application.set(eduArteApp);

				executeJob(context);
			}
			catch (InterruptedException e)
			{
				log.warn("Job " + getJobName() + " afgebroken", e);
				throw new JobExecutionException("Job " + getJobName() + " afgebroken", e);
			}
			catch (SchedulerException e)
			{
				log.warn("Er is een fout opgetreden bij het uitvoeren van een achtergrondtaak.", e);
				throw new JobExecutionException(
					"Er is een fout opgetreden bij het uitvoeren van een achtergrondtaak.", e);
			}
			catch (Exception e)
			{
				log
					.error("Er is een fout opgetreden bij het uitvoeren van een achtergrondtaak.",
						e);
				if (e instanceof JobExecutionException)
					throw (JobExecutionException) e;
				throw new JobExecutionException(
					"Er is een fout opgetreden bij het uitvoeren van een achtergrondtaak.", e);
			}
		}
		finally
		{
			Application.unset();
			// Clear executing thread.
			executingThread = null;
			try
			{
				DataAccessRegistry.getHelper(SessionDataAccessHelper.class).closeSession();
			}
			catch (RuntimeException e)
			{
				log.error(e.getMessage(), e);
			}

			EduArteContext.clearContext();
			WaspAuthorizationStrategy.setStrategyResolver(null);

			log.info(getClass().getSimpleName() + " afgelopen");
		}
	}

	/**
	 * @param context
	 * @throws JobExecutionException
	 * @throws InterruptedException
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	protected abstract void executeJob(JobExecutionContext context) throws JobExecutionException,
			InterruptedException;

	@Override
	public JobDescription getJobDescription(JobExecutionContext context)
	{
		return new EduArteJobDescription(this, context);
	}

	public Long getMedewerkerId()
	{
		return medewerker == null ? null : medewerker.getId();
	}

	@Override
	public Organisatie getOrganisatie()
	{
		return organisatie;
	}

	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public Account getAccount()
	{
		return account;
	}

	public Date getPeildatum()
	{
		return peildatum;
	}

	public EduArteApp getEduarteApp()
	{
		return eduArteApp;
	}
}
