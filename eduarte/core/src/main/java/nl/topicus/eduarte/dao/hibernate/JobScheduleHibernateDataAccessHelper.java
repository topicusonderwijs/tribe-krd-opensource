/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.quartz.CobraJob;
import nl.topicus.cobra.quartz.JobSchedule;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.eduarte.dao.helpers.JobScheduleDataAccessHelper;
import nl.topicus.eduarte.entities.jobs.PersistentJobSchedule;
import nl.topicus.eduarte.jobs.EduArteJob;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class JobScheduleHibernateDataAccessHelper extends
		HibernateDataAccessHelper<PersistentJobSchedule> implements JobScheduleDataAccessHelper
{

	/**
	 * @param provider
	 */
	public JobScheduleHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public PersistentJobSchedule getSchedule(Class< ? extends EduArteJob> type)
	{
		Criteria criteria = createCriteria(PersistentJobSchedule.class);
		criteria.add(Restrictions.eq("jobClassName", type.getName()));

		return (PersistentJobSchedule) uncachedUnique(criteria);
	}

	@Override
	public List<PersistentJobSchedule> listSchedules(Class< ? extends EduArteJob> type)
	{
		Criteria criteria = createCriteria(PersistentJobSchedule.class);
		criteria.add(Restrictions.eq("jobClassName", type.getName()));
		return cachedTypedList(criteria);
	}

	@Override
	public List<PersistentJobSchedule> listEnabledSchedulesForAllInstellingen()
	{
		Criteria criteria = createUninterceptedCriteria(PersistentJobSchedule.class);
		criteria.add(Restrictions.eq("enabled", true));
		return cachedTypedList(criteria);
	}

	@Override
	public PersistentJobSchedule createJobSchedule(Class< ? extends EduArteJob> type)
	{
		Class< ? extends JobSchedule> scheduleClass = CobraJob.getJobScheduleClass(type);
		if (scheduleClass == null)
			throw new IllegalArgumentException("Cannot create schedule for " + type);
		PersistentJobSchedule ret =
			(PersistentJobSchedule) ReflectionUtil.invokeConstructor(scheduleClass);
		ret.setJobClass(type);
		return ret;
	}
}
