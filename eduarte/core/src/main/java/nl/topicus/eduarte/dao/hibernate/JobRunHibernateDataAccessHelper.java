/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import java.util.Date;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.JobRunDataAccessHelper;
import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.zoekfilters.JobRunZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class JobRunHibernateDataAccessHelper<T extends JobRun, ZF extends JobRunZoekFilter<T>>
		extends AbstractZoekFilterDataAccessHelper<T, ZF> implements JobRunDataAccessHelper<T, ZF>
{
	public JobRunHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(ZF filter)
	{
		Asserts.assertNotNull("filter.subClass", filter.getSubclass());
		Criteria criteria = createCriteria(filter.getSubclass());
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addGreaterOrEquals("runStart", filter.getBeginDatum());
		if (filter.getEindDatum() != null)
		{
			Date totDatum = filter.getEindDatum();
			totDatum = TimeUtil.getInstance().maakEindeVanDagVanDatum(totDatum);
			criteria.add(Restrictions.le("runEinde", totDatum));
		}
		builder.addILikeCheckWildcard("samenvatting", filter.getSamenvatting(), MatchMode.START);

		if (filter.getMedewerker() != null && filter.getAccount() != null)
			builder.addOrs(Restrictions.eq("gestartDoor", filter.getMedewerker()),
				Restrictions.eq("gestartDoorAccount", filter.getAccount()));
		else if (filter.getMedewerker() != null)
			builder.addOrs(Restrictions.eq("gestartDoor", filter.getMedewerker()));
		else if (filter.getAccount() != null)
			builder.addOrs(Restrictions.eq("gestartDoorAccount", filter.getAccount()));

		return criteria;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <R extends T> R getLaatsteRun(Class<R> jobrunClass)
	{
		Criteria criteria = createCriteria(jobrunClass);
		criteria.addOrder(Order.desc("runStart"));
		criteria.setMaxResults(1);
		return (R) uncachedUnique(criteria);
	}

	@Override
	public int cleanupOldRuns(Class< ? extends JobRun> jobrunClass, Date date)
	{
		SQLQuery deleteJobRunDetails =
			createSQLQuery("DELETE FROM JobRunDetail "
				+ "WHERE jobrun IN (SELECT ID FROM jobrun WHERE dtype = :dtype AND runstart < :datum)");
		deleteJobRunDetails.setString("dtype", jobrunClass.getSimpleName());
		deleteJobRunDetails.setDate("datum", date);
		deleteJobRunDetails.executeUpdate();

		SQLQuery deleteJobRuns =
			createSQLQuery("DELETE FROM JobRun " + "WHERE DTYPE = :dtype AND runStart < :datum");
		deleteJobRuns.setString("dtype", jobrunClass.getSimpleName());
		deleteJobRuns.setDate("datum", date);
		return deleteJobRuns.executeUpdate();
	}
}
