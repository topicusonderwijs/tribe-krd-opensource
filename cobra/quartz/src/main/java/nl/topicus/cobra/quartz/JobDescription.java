/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.quartz;

import java.io.Serializable;

import nl.topicus.cobra.entities.IOrganisatie;
import nl.topicus.cobra.util.TimeUtil;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;

public class JobDescription implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final String omschrijving;

	private final String datumTijdOpgestartFormatted;

	private final String status;

	private final int progress;

	private final String jobName;

	private final String jobGroup;

	private final long jobId;

	private final String organisatie;

	public JobDescription(CobraJob job, JobExecutionContext context)
	{
		omschrijving = job.getOmschrijving();
		datumTijdOpgestartFormatted = job.getDatumTijdOpgestartFormatted();
		status = job.getStatus();
		progress = job.getProgress();
		jobName = context.getJobDetail().getName();
		jobGroup = context.getJobDetail().getGroup();
		jobId = job.getJobId();
		organisatie = job.getOrganisatie() == null ? null : job.getOrganisatie().getNaam();
	}

	public JobDescription(Trigger trigger)
	{
		omschrijving = trigger.getJobGroup();
		datumTijdOpgestartFormatted =
			TimeUtil.getInstance().formatDateTime(trigger.getNextFireTime());
		status = "In wachtrij";
		progress = 0;
		jobName = trigger.getJobName();
		jobGroup = trigger.getJobGroup();
		jobId = -1;
		IOrganisatie triggerOrg =
			(IOrganisatie) getFromJobDataMap(trigger, CobraScheduler.INSTELLING_KEY);
		this.organisatie = triggerOrg == null ? "(Geen)" : triggerOrg.getNaam();
	}

	public Object getFromJobDataMap(Trigger trigger, String key)
	{
		return trigger.getJobDataMap().get(key);
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public String getDatumTijdOpgestartFormatted()
	{
		return datumTijdOpgestartFormatted;
	}

	public String getStatus()
	{
		return status;
	}

	public int getProgress()
	{
		return progress;
	}

	public String getJobName()
	{
		return jobName;
	}

	public String getJobGroup()
	{
		return jobGroup;
	}

	public long getJobId()
	{
		return jobId;
	}

	public String getOrganisatie()
	{
		return organisatie;
	}
}
