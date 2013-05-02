package nl.topicus.cobra.quartz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.entities.IOrganisatie;
import nl.topicus.cobra.util.TimeUtil;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.UnableToInterruptJobException;
import org.quartz.impl.StdScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Wrapper class om de echte scheduler om het voor cobra-aware projecten makkelijker te
 * maken om quartz te gebruiken. Standaard is er in cobra-kitchensink een
 * {@link SchedulerFactoryBean} gedefinieerd waar men tegen kan praten om een
 * {@link StdScheduler} te krijgen. Deze bean heet "quartzSchedulerFactory".
 * 
 * @author hoeve
 * 
 */
public abstract class CobraScheduler
{
	/**
	 * De key waarmee de instelling van de job in de datamap geplaatst moet worden.
	 */
	public static final String INSTELLING_KEY = "organisatie";

	/**
	 * De key waarmee de medewerker van de job in de datamap geplaatst moet worden.
	 */
	public static final String MEDEWERKER_KEY = "medewerker";

	/**
	 * De key waarmee de account van de job in de datamap geplaatst moet worden.
	 */
	public static final String ACCOUNT_KEY = "account";

	/**
	 * De key waarmee de peildatum van de job in de datamap geplaatst moet worden.
	 */
	public static final String PEILDATUM_KEY = "peildatum";

	/**
	 * De key waarmee de terug-te-draaien jobrun in de datamap geplaatst moet worden.
	 */
	public static final String ROLL_BACK_KEY = "rollBack";

	private static long EXECUTIE_NUMMER_COUNT = 0;

	protected static final Logger log = LoggerFactory.getLogger(CobraScheduler.class);

	protected Scheduler scheduler;

	public CobraScheduler()
	{
	}

	public CobraScheduler(Scheduler scheduler)
	{
		this.scheduler = scheduler;
	}

	public void init()
	{

	}

	protected abstract JobSchedule getSchedule(Class< ? extends JobSchedule> scheduleClass,
			Class< ? extends CobraJob> jobClass);

	public <T extends CobraJob> void triggerJob(Class<T> jobClass, JobDataMap datamap)
			throws SchedulerException
	{
		JobInfo jobInfo = CobraJob.getJobInfo(jobClass);
		datamap.put("manual", true);

		log.info(jobInfo.name() + " triggered");
		triggerJob(generateJobName(), jobInfo.name(), jobClass, datamap);
	}

	public void reschedule(JobSchedule schedulable) throws SchedulerException
	{
		JobInfo jobInfo = CobraJob.getJobInfo(schedulable.getJobClass());
		String name = schedulable.getOrganisatie().getIdAsSerializable().toString();
		String group = jobInfo.name();

		Trigger[] oldTriggers = getScheduler().getTriggersOfJob(name, group);
		for (Trigger curOldTrigger : oldTriggers)
			unscheduleJob(curOldTrigger);

		createJobDetail(name, group, schedulable.getJobClass());
		Trigger jobTrigger = schedulable.createTrigger(name, group);
		jobTrigger.setGroup(group);
		jobTrigger.setJobGroup(group);
		jobTrigger.setJobName(name);

		log.info(group + " rescheduled voor " + schedulable.getOrganisatie());

		if (schedulable.isEnabled())
			rescheduleJob(jobTrigger);
	}

	/**
	 * Interrupt of unscheduled de gegeven job.
	 * 
	 * @throws UnableToInterruptJobException
	 */
	@SuppressWarnings("unchecked")
	public void interruptJob(JobDescription job) throws UnableToInterruptJobException,
			SchedulerException
	{
		JobDetail jobDetail = getScheduler().getJobDetail(job.getJobName(), job.getJobGroup());
		Class< ? extends CobraJob> jobClass = jobDetail.getJobClass();
		Class< ? extends JobSchedule> scheduleClass = CobraJob.getJobScheduleClass(jobClass);
		Trigger[] triggers = getScheduler().getTriggersOfJob(job.getJobName(), job.getJobGroup());
		for (Trigger curTrigger : triggers)
		{
			unscheduleJob(curTrigger);
		}
		if (scheduleClass != null)
			reschedule(getSchedule(scheduleClass, jobClass));

		boolean found = false;
		for (Object curJobContextObj : getScheduler().getCurrentlyExecutingJobs())
		{
			JobExecutionContext curJobContex = (JobExecutionContext) curJobContextObj;
			if (curJobContex.getJobInstance() instanceof CobraJob)
			{
				CobraJob curJob = (CobraJob) curJobContex.getJobInstance();
				if (curJob.getJobId() == job.getJobId())
				{
					found = true;
					curJob.interrupt();
					break;
				}
			}
		}
		// fall back to default interrupt when no job has been found
		if (!found)
			getScheduler().interrupt(job.getJobName(), job.getJobGroup());
	}

	/**
	 * @return the quartz scheduler created by the {@link SchedulerFactoryBean}, unless
	 *         there is no {@link SchedulerFactoryBean} linked to this Scheduler.
	 */
	public Scheduler getScheduler() throws SchedulerException
	{
		if (scheduler == null)
			throw new SchedulerException(
				"No Quartz scheduler found, did you mean to set this at startup?");

		return scheduler;
	}

	public void setScheduler(Scheduler scheduler)
	{
		this.scheduler = scheduler;
	}

	/**
	 * 
	 * CobraQuartzUtil vanaf hier.
	 * 
	 */

	/**
	 * Start de scheduler, dit is niet nodig wanneer de scheduler via spring is
	 * aangemaakt.
	 * 
	 * @throws SchedulerException
	 */
	public void startScheduler() throws SchedulerException
	{
		if (getScheduler() != null)
			getScheduler().start();
	}

	/**
	 * Stopt de scheduler, kan in de spring destroy-method van de bean opgenomen worden.
	 * 
	 * @throws SchedulerException
	 */
	public void stopScheduler() throws SchedulerException
	{
		if (getScheduler() != null)
			getScheduler().shutdown();
	}

	/**
	 * Zoekt de job in de scheduler, of voegt hem toe als hij nog niet bestond.
	 * 
	 * @param jobName
	 * @param jobGroup
	 * @param jobClass
	 * @return De job
	 * @throws SchedulerException
	 */
	public JobDetail createJobDetail(String jobName, String jobGroup,
			Class< ? extends CobraJob> jobClass) throws SchedulerException
	{
		JobDetail job = getScheduler().getJobDetail(jobName, jobGroup);
		if (job == null)
		{
			job = new JobDetail(jobName, jobGroup, jobClass);
			job.setDurability(true);
			getScheduler().addJob(job, false);
		}
		return job;
	}

	/**
	 * Trigger een job immediately
	 * 
	 * @param jobName
	 * @param jobGroup
	 * @param jobClass
	 * @param data
	 * @throws SchedulerException
	 */
	public void triggerJob(String jobName, String jobGroup, Class< ? extends CobraJob> jobClass,
			JobDataMap data) throws SchedulerException
	{
		try
		{
			createJobDetail(jobName, jobGroup, jobClass);
			preprocessDataMap(data);
			getScheduler().triggerJob(jobName, jobGroup, data);
		}
		catch (SchedulerException e)
		{
			log.error("Fout tijdens handmatige trigger van job", e);
			throw e;
		}
	}

	/**
	 * @param jobClass
	 *            De clazz van jobs die opgevraagd moeten worden.
	 * @param organisatie
	 *            De organisatie waarvoor de jobs opgevraagd moet worden.
	 * @return Een lijst met alle jobs van het gegeven type die op dit moment draaien.
	 */
	@SuppressWarnings("unchecked")
	public List<JobExecutionContext> getCurrentlyExecutingJobs(Class< ? extends CobraJob> jobClass,
			IOrganisatie organisatie)
	{
		if (jobClass == null)
			throw new IllegalArgumentException("jobClass");

		List<JobExecutionContext> res = new ArrayList<JobExecutionContext>();
		try
		{
			List<JobExecutionContext> list = getScheduler().getCurrentlyExecutingJobs();
			for (JobExecutionContext context : list)
			{
				if (jobClass.isAssignableFrom(context.getJobInstance().getClass()))
				{
					CobraJob jobInstance = jobClass.cast(context.getJobInstance());
					if (organisatie == null || organisatie.equals(jobInstance.getOrganisatie()))
					{
						res.add(context);
					}
				}
			}
		}
		catch (SchedulerException e)
		{
			return Collections.emptyList();
		}
		return res;
	}

	/**
	 * @return Alle triggers die eigenlijk al hadden moeten draaien, maar die nu staan te
	 *         wachten op een beschikbare thread.
	 */
	public <T extends CobraJob> List<Trigger> getTriggersWaitingForThread(Class<T> jobClass,
			IOrganisatie organisatie)
	{
		if (jobClass == null)
			throw new IllegalArgumentException("jobClass");

		Date now = TimeUtil.getInstance().addMinutes(TimeUtil.getInstance().currentDateTime(), 1);
		List<Trigger> res = new ArrayList<Trigger>();
		try
		{
			String[] groups = getScheduler().getJobGroupNames();
			for (String group : groups)
			{
				String[] jobs = getScheduler().getJobNames(group);
				for (String job : jobs)
				{
					Trigger[] triggers = getScheduler().getTriggersOfJob(job, group);
					for (Trigger trigger : triggers)
					{
						if (trigger.getNextFireTime() != null
							&& trigger.getNextFireTime().before(now))
						{
							JobDetail detail = getScheduler().getJobDetail(job, group);
							if (jobClass.isAssignableFrom(detail.getJobClass()))
							{
								if (organisatie == null
									|| organisatie.equals(getOrganisatie(trigger, detail)))
								{
									res.add(trigger);
								}
							}
						}
					}
				}
			}
		}
		catch (SchedulerException e)
		{
			return Collections.emptyList();
		}
		return res;
	}

	protected IOrganisatie getOrganisatie(Trigger trigger, JobDetail detail)
	{
		Object jobOrganisatie = trigger.getJobDataMap().get(INSTELLING_KEY);
		if (jobOrganisatie == null)
		{
			jobOrganisatie = detail.getJobDataMap().get(INSTELLING_KEY);
		}
		if (jobOrganisatie instanceof IOrganisatie)
		{
			return (IOrganisatie) jobOrganisatie;
		}
		return null;
	}

	public <T extends CobraJob> List<JobDescription> getExecutingAndWaitingJobs(Class<T> jobClass,
			IOrganisatie organisatie)
	{
		return getExecutingAndWaitingJobs(jobClass, organisatie, new MatchAllFilter());
	}

	public <T extends CobraJob> List<JobDescription> getExecutingAndWaitingJobs(Class<T> jobClass,
			IOrganisatie organisatie, JobDescriptionFilter filter)
	{
		if (jobClass == null)
			throw new IllegalArgumentException("jobClass mag niet leeg zijn.");

		List<Trigger> triggers = getTriggersWaitingForThread(jobClass, organisatie);
		List<JobExecutionContext> jobs = getCurrentlyExecutingJobs(jobClass, organisatie);
		List<JobDescription> res = new ArrayList<JobDescription>(triggers.size() + jobs.size());
		for (JobExecutionContext context : jobs)
		{
			if (filter == null || filter.matches((CobraJob) context.getJobInstance(), context))
				res.add(((CobraJob) context.getJobInstance()).getJobDescription(context));
		}
		for (Trigger trigger : triggers)
		{
			if (filter == null || filter.matches(trigger))
				res.add(createJobDescription(trigger));
		}
		return res;
	}

	protected JobDescription createJobDescription(Trigger trigger)
	{
		return new JobDescription(trigger);
	}

	private void rescheduleJob(Trigger jobTrigger) throws SchedulerException
	{
		preprocessDataMap(jobTrigger.getJobDataMap());
		if (getScheduler().getTrigger(jobTrigger.getName(), jobTrigger.getGroup()) == null)
			getScheduler().scheduleJob(jobTrigger);
		else
			getScheduler().rescheduleJob(jobTrigger.getName(), jobTrigger.getGroup(), jobTrigger);
	}

	private synchronized String generateJobName()
	{
		return Long.toString(EXECUTIE_NUMMER_COUNT++);
	}

	/**
	 * Unscheduled de job, wat in de praktijk inhoudt dat deze stopt met draaien. Handig
	 * om oneindige jobs met een interval te stoppen
	 */
	private void unscheduleJob(Trigger jobTrigger)
	{
		try
		{
			getScheduler().unscheduleJob(jobTrigger.getName(), jobTrigger.getGroup());
		}
		catch (SchedulerException e)
		{
			log.error(e.getMessage());
		}
	}

	private void preprocessDataMap(JobDataMap dataMap)
	{
		for (Object key : dataMap.keySet())
		{
			Object value = dataMap.get(key);
			dataMap.put(key, preprocessDataMapValue(value));
		}
	}

	protected Object preprocessDataMapValue(Object value)
	{
		return value;
	}
}
