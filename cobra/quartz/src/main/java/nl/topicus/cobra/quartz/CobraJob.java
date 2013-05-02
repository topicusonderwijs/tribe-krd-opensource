package nl.topicus.cobra.quartz;

import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;

import nl.topicus.cobra.entities.IOrganisatie;
import nl.topicus.cobra.util.TimeUtil;

import org.quartz.InterruptableJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.UnableToInterruptJobException;

/**
 * Abstract class voor Quartz jobs binnen Cobraprojecten.
 * 
 * @author loite
 */
public abstract class CobraJob implements InterruptableJob
{
	private static long JOB_ID_COUNTER = 0;

	private long jobId;

	/**
	 * Hoe ver is de job in de uitvoering.
	 */
	private int progress;

	/**
	 * Waar is de job nu mee bezig.
	 */
	private String status;

	/**
	 * De datum/tijd dat de job is opgestart.
	 */
	protected final Date datumTijdOpgestart;

	/**
	 * De thread die deze job uitvoert.
	 */
	protected Thread executingThread;

	/**
	 * @return een boolean die aangeeft dat deze thread is verzocht te stoppen.
	 *         Interrupting a thread that is not alive need not have any effect.
	 *         <p>
	 *         zie ook {@link Thread#interrupt()}
	 *         </p>
	 * 
	 */
	public boolean isInterrupted()
	{
		if (executingThread == null)
			return false;

		return executingThread.isInterrupted();
	}

	public abstract IOrganisatie getOrganisatie();

	public CobraJob()
	{
		synchronized (CobraJob.class)
		{
			jobId = JOB_ID_COUNTER++;
		}
		datumTijdOpgestart = TimeUtil.getInstance().currentDateTime();
	}

	/**
	 * @param clazz
	 * @return De jobinfo annotation van de gegeven class
	 */
	public static JobInfo getJobInfo(Class< ? extends CobraJob> clazz)
	{
		JobInfo ret = clazz.getAnnotation(JobInfo.class);
		if (ret == null)
			throw new IllegalArgumentException(clazz + " must have a JobInfo annotation");
		return ret;
	}

	/**
	 * @param clazz
	 * @return De jobinfo annotation van de gegeven class
	 */
	public static Class< ? extends JobSchedule> getJobScheduleClass(Class< ? extends CobraJob> clazz)
	{
		JobScheduleClass ret = clazz.getAnnotation(JobScheduleClass.class);
		return ret == null ? null : ret.value();
	}

	public int getProgress()
	{
		return progress;
	}

	/**
	 * Set de progressie van deze job. Controleert of de thread in de tussentijd is
	 * interrupted, en als dat het geval is, wordt een InterruptedException gegooid.
	 * Hierdoor is het mogelijk om van buitenaf de job te stoppen.
	 * 
	 * @param progress
	 *            The progress to set.
	 * @throws InterruptedException
	 *             Als deze thread is interrupted
	 */
	protected void setProgress(int progress) throws InterruptedException
	{
		// Controleer of deze thread is interrupted
		if (Thread.interrupted())
		{
			throw new InterruptedException();
		}
		this.progress = progress;
	}

	public String getStatus()
	{
		return status;
	}

	protected void setStatus(String status)
	{
		this.status = status;
	}

	public final String getJobName()
	{
		return getJobInfo(getClass()).name();
	}

	public String getOmschrijving()
	{
		return getJobName();
	}

	public Date getDatumTijdOpgestart()
	{
		return datumTijdOpgestart;
	}

	public String getDatumTijdOpgestartFormatted()
	{
		return TimeUtil.getInstance().formatDateTime(getDatumTijdOpgestart());
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException
	{
		if (executingThread == null)
		{
			throw new UnableToInterruptJobException("No executing thread found");
		}
		executingThread.interrupt();
	}

	protected abstract void fixDataMap(JobDataMap jobDataMap);

	/**
	 * @return Waarde van het object met de gegeven naam in de job- dan wel triggerdatamap
	 *         van de gegeven execution context.
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getValue(JobExecutionContext context, String name)
	{
		Object ret = context.getMergedJobDataMap().get(name);
		return (T) ret;
	}

	/**
	 * @param context
	 * @param name
	 * @return true indien de job- dan wel triggerdatamap van de execution context een
	 *         waarde bevat voor de gegeven naam.
	 */
	protected boolean hasValue(JobExecutionContext context, String name)
	{
		return context.getMergedJobDataMap().get(name) != null;
	}

	/**
	 * @param context
	 * @return een acurate description van deze job. Door de mogelijk vele subclasses
	 *         maakt de job een description die past bij de job.
	 */
	public JobDescription getJobDescription(JobExecutionContext context)
	{
		JobDescription desc = new JobDescription(this, context);
		return desc;
	}

	public long getJobId()
	{
		return jobId;
	}

	/**
	 * Zet de voortgang van de job voor count out of total.
	 */
	protected void setProgress(int count, int total) throws InterruptedException
	{
		setProgress(count, total, SingleSegment.ENTIRE_JOB);
	}

	/**
	 * Zet de voortgang van de job voor het gegeven segment voor count out of total.
	 */
	protected void setProgress(int count, int total, JobSegment segment)
			throws InterruptedException
	{
		setProgress(calcPreceedingSegmentsSize(segment) + segment.getPercent() * count / total);
	}

	protected int calcPreceedingSegmentsSize(JobSegment segment)
	{
		if (segment instanceof Enum< ? >)
		{
			Enum< ? > segmentEnum = (Enum< ? >) segment;
			Iterator< ? > valsIterator = EnumSet.allOf(segmentEnum.getDeclaringClass()).iterator();
			int ordinal = segmentEnum.ordinal();
			int total = 0;
			for (int count = 0; count < ordinal; count++)
			{
				JobSegment curPreceedingSegment = (JobSegment) valsIterator.next();
				total += curPreceedingSegment.getPercent();
			}
			return total;
		}
		throw new IllegalArgumentException("Can only calculate progress for enums. " + segment
			+ " is not an enum. Override calcPreceedingSegmentsSize to provide a"
			+ " custom implementation.");
	}
}
