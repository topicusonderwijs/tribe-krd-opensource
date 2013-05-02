/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.jobs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import nl.topicus.cobra.dao.CobraConfiguratieException;
import nl.topicus.cobra.entities.FieldPersistance;
import nl.topicus.cobra.entities.FieldPersistenceMode;
import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.cobra.quartz.CobraScheduler;
import nl.topicus.cobra.quartz.JobSchedule;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEntiteit;
import nl.topicus.eduarte.jobs.EduArteJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.quartz.JobDataMap;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

/**
 * Schedule wanneer jobs uitgevoerd moeten worden.
 * 
 * @author marrink
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class PersistentJobSchedule extends OrganisatieEntiteit implements JobSchedule
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	@RestrictedAccess(hasSetter = false)
	@FieldPersistance(FieldPersistenceMode.SKIP)
	@AutoForm(include = false)
	private String jobClassName;

	@Column(nullable = true)
	@AutoForm(required = true, order = 3, label = "Uur", htmlClasses = "unit_40")
	private Integer hour;

	@Column(nullable = true)
	@AutoForm(required = true, order = 4, label = "Minuten", htmlClasses = "unit_40")
	private Integer minutes;

	@Column(nullable = false)
	@AutoForm(order = 6, label = "Actief", htmlClasses = "unit_10")
	private boolean enabled = true;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@AutoForm(order = 2, required = true, htmlClasses = "unit_160")
	private ScheduleType scheduleType;

	@Column(nullable = true)
	@AutoForm(required = true, order = 5, label = "Interval minuten", htmlClasses = "unit_40")
	private Integer intervalMinutes;

	public PersistentJobSchedule()
	{
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public String getJobClassName()
	{
		return jobClassName;
	}

	public Class< ? extends EduArteJob> getJobClass()
	{
		try
		{
			return Class.forName(jobClassName).asSubclass(EduArteJob.class);
		}
		catch (ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void setJobClass(Class< ? extends EduArteJob> jobClass)
	{
		jobClassName = jobClass.getName();
	}

	protected void fillTriggerDataMap(JobDataMap dataMap)
	{
		dataMap.put(CobraScheduler.INSTELLING_KEY, getOrganisatie());
		dataMap.put("manual", false);
	}

	public ScheduleType getScheduleType()
	{
		return scheduleType;
	}

	public void setScheduleType(ScheduleType scheduleType)
	{
		this.scheduleType = scheduleType;
	}

	public Integer getHour()
	{
		return hour;
	}

	public void setHour(Integer hour)
	{
		this.hour = hour;
	}

	public Integer getMinutes()
	{
		return minutes;
	}

	public void setMinutes(Integer minutes)
	{
		this.minutes = minutes;
	}

	public Integer getIntervalMinutes()
	{
		return intervalMinutes;
	}

	public void setIntervalMinutes(Integer intervalMinutes)
	{
		this.intervalMinutes = intervalMinutes;
	}

	@Override
	public Trigger createTrigger(String jobName, String groupName)
	{
		Trigger jobTrigger;
		String triggerName;
		if (getScheduleType().equals(ScheduleType.Dagelijks))
		{
			triggerName = jobName + " (dagelijks)";
			if (getOrganisatie() != null)
				triggerName += " " + getOrganisatie().getNaam();
			jobTrigger = TriggerUtils.makeDailyTrigger(triggerName, getHour(), getMinutes());
		}
		else
		{
			triggerName = jobName + " (interval)";
			if (getOrganisatie() != null)
				triggerName += " " + getOrganisatie().getNaam();
			Integer interval = getIntervalMinutes();
			if (interval == null)
			{
				throw new CobraConfiguratieException(
					"Bij een trigger gebaseerd op interval zijn de interval minutes verplicht (heb je soms minutes ingevuld ipv intervalMinutes?)");
			}
			jobTrigger =
				TriggerUtils.makeMinutelyTrigger(triggerName, interval,
					SimpleTrigger.REPEAT_INDEFINITELY);
			jobTrigger.setStartTime(TimeUtil.getInstance().addMinutes(
				TimeUtil.getInstance().currentDateTime(), interval));
		}
		jobTrigger.setName(triggerName);
		fillTriggerDataMap(jobTrigger.getJobDataMap());
		return jobTrigger;
	}
}
