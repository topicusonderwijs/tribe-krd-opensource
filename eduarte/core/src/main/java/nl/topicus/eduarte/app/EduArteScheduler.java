/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.app;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.dataproviders.IModelDataProvider;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.quartz.CobraJob;
import nl.topicus.cobra.quartz.CobraScheduler;
import nl.topicus.cobra.quartz.JobDescription;
import nl.topicus.cobra.quartz.JobDescriptionFilter;
import nl.topicus.cobra.quartz.JobSchedule;
import nl.topicus.eduarte.dao.helpers.JobScheduleDataAccessHelper;
import nl.topicus.eduarte.entities.jobs.PersistentJobSchedule;
import nl.topicus.eduarte.entities.organisatie.Beheer;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.jobs.EduArteJobDescription;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.quartz.JobDataMap;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.scheduling.quartz.JobDetailBean;

/**
 * @author loite
 */
public class EduArteScheduler extends CobraScheduler
{

	/**
	 * The key of an ApplicationContext reference to expose in the SchedulerContext, for
	 * example "applicationContext". Only applicable when running in a Spring
	 * ApplicationContext.
	 * <p>
	 * Note: When using persistent Jobs whose JobDetail will be kept in the database, do
	 * not put an ApplicationContext reference into the JobDataMap but rather into the
	 * SchedulerContext.
	 * <p>
	 * In case of a QuartzJobBean, the reference will be applied to the Job instance as
	 * bean property. An "applicationContext" attribute will correspond to a
	 * "setApplicationContext" method in that scenario.
	 * <p>
	 * Note that BeanFactory callback interfaces like ApplicationContextAware are not
	 * automatically applied to Quartz Job instances, because Quartz itself is reponsible
	 * for the lifecycle of its Jobs.
	 * 
	 * @see JobDetailBean#setApplicationContextJobDataKey
	 * @see org.springframework.context.ApplicationContext
	 */
	public static final String APPLICATION_CONTEXT_SCHEDULER_CONTEXT_KEY = "applicationSettings";

	public EduArteScheduler()
	{
	}

	/**
	 * Constructor
	 * 
	 * @param scheduler
	 */
	public EduArteScheduler(Scheduler scheduler)
	{
		super(scheduler);
	}

	@Override
	public void init()
	{
		super.init();

		try
		{
			JobScheduleDataAccessHelper helper =
				DataAccessRegistry.getHelper(JobScheduleDataAccessHelper.class);

			for (PersistentJobSchedule curSchedule : helper
				.listEnabledSchedulesForAllInstellingen())
			{
				try
				{
					reschedule(curSchedule);
				}
				catch (Exception e)
				{
					log.error(e.toString(), e);
				}
			}
		}
		finally
		{
			DataAccessRegistry.getHelper(SessionDataAccessHelper.class).closeSession();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected JobSchedule getSchedule(Class< ? extends JobSchedule> scheduleClass,
			Class< ? extends CobraJob> jobClass)
	{
		JobScheduleDataAccessHelper helper =
			DataAccessRegistry.getHelper(JobScheduleDataAccessHelper.class);
		return helper.getSchedule((Class< ? extends EduArteJob>) jobClass);
	}

	/**
	 * Trigger de gegeven job
	 * 
	 * @param jobClass
	 * @throws SchedulerException
	 */
	public void triggerJob(Class< ? extends CobraJob> jobClass) throws SchedulerException
	{
		triggerJob(jobClass, new JobDataMap());
	}

	/**
	 * Trigger de gegeven job
	 * 
	 * @param jobClass
	 * @param datamap
	 * @throws SchedulerException
	 */
	@Override
	public <T extends CobraJob> void triggerJob(Class<T> jobClass, JobDataMap datamap)
			throws SchedulerException
	{
		datamap.put(PEILDATUM_KEY, EduArteContext.get().getPeildatum());
		datamap.put(INSTELLING_KEY, EduArteContext.get().getOrganisatie());
		datamap.put(MEDEWERKER_KEY, EduArteContext.get().getMedewerker());
		datamap.put(ACCOUNT_KEY, EduArteContext.get().getAccount());

		super.triggerJob(jobClass, datamap);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Object preprocessDataMapValue(Object value)
	{
		if (value instanceof IdObject)
		{
			IModel ret = ModelFactory.getModel(value);
			ret.detach();
			return ret;
		}
		if (value instanceof List)
		{
			IModel ret = ModelFactory.getListModel((List<Object>) value);
			ret.detach();
			return ret;
		}
		return value;
	}

	@Override
	protected JobDescription createJobDescription(Trigger trigger)
	{
		return new EduArteJobDescription(trigger);
	}

	/**
	 * @param jobClasses
	 *            de class waar de jobs minimaal aan moeten voldoen
	 * @return geeft een lijst van alle jobs van de opgegeven instelling
	 */
	public IDataProvider<JobDescription> getExecutingAndWaitingJobs(
			final Class< ? extends CobraJob>... jobClasses)
	{
		return getExecutingAndWaitingJobs(null, jobClasses);
	}

	/**
	 * @param filter
	 *            wordt als filter voor de jobs gebruikt.
	 * @param jobClass
	 *            de class waar de jobs minimaal aan moeten voldoen
	 * @return geeft een lijst van alle jobs of alle jobs van een medewerker van de
	 *         opgegeven instelling
	 */
	@SuppressWarnings("unchecked")
	public IDataProvider<JobDescription> getExecutingAndWaitingJobs(JobDescriptionFilter filter,
			final Class< ? extends CobraJob> jobClass)
	{
		return new IModelDataProvider<JobDescription>(new JobModel(filter, jobClass));
	}

	/**
	 * @param filter
	 *            wordt als filter voor de jobs gebruikt.
	 * @param jobClasses
	 *            de classes waar de jobs minimaal aan moeten voldoen
	 * @return geeft een lijst van alle jobs of alle jobs van een medewerker van de
	 *         opgegeven instelling
	 */
	public IDataProvider<JobDescription> getExecutingAndWaitingJobs(JobDescriptionFilter filter,
			final Class< ? extends CobraJob>... jobClasses)
	{
		return new IModelDataProvider<JobDescription>(new JobModel(filter, jobClasses));
	}

	private static class JobModel extends LoadableDetachableModel<List<JobDescription>>
	{
		private static final long serialVersionUID = 1L;

		private JobDescriptionFilter filter;

		private Class< ? extends CobraJob>[] jobClasses;

		public JobModel(JobDescriptionFilter filter, Class< ? extends CobraJob>... jobClasses)
		{
			this.filter = filter;
			this.jobClasses = jobClasses;
		}

		@Override
		protected List<JobDescription> load()
		{
			List<JobDescription> medewerkerJobs = new ArrayList<JobDescription>();
			for (Class< ? extends CobraJob> clazz : jobClasses)
			{
				Organisatie org = EduArteContext.get().getOrganisatie();
				org = (Organisatie) org.doUnproxy();
				if (org instanceof Beheer)
					org = null;
				List<JobDescription> jobs =
					EduArteApp.get().getEduarteScheduler().getExecutingAndWaitingJobs(clazz, org,
						filter);

				for (JobDescription job : jobs)
				{
					medewerkerJobs.add(job);
				}
			}

			return medewerkerJobs;
		}
	}
}
