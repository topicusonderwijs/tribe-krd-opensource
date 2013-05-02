/*
 * Copyright (c) 2009, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.tester;

import nl.topicus.cobra.quartz.CobraJob;
import nl.topicus.cobra.quartz.JobSchedule;
import nl.topicus.eduarte.app.EduArteScheduler;

import org.quartz.JobDataMap;

/**
 * Scheduler dat niets doet.
 * 
 * @author hoeve
 */
public class MockEduArteScheduler extends EduArteScheduler
{
	/**
	 * Spring Constructor
	 */
	public MockEduArteScheduler()
	{
		super(null);
	}

	@Override
	public void init()
	{
		// niet doen dus
	}

	/**
	 * Trigger de gegeven job, NOT!
	 * 
	 * @param jobClass
	 * @param datamap
	 */
	@Override
	public <T extends CobraJob> void triggerJob(Class<T> jobClass, JobDataMap datamap)
	{
	}

	@Override
	public void reschedule(JobSchedule schedulable)
	{
	}
}
