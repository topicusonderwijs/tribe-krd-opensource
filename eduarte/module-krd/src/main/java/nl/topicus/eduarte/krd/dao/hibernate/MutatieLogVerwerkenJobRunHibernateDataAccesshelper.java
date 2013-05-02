package nl.topicus.eduarte.krd.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.hibernate.JobRunHibernateDataAccessHelper;
import nl.topicus.eduarte.krd.dao.helpers.MutatieLogVerwerkenJobRunDataAccessHelper;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerkenJobRun;
import nl.topicus.eduarte.krd.zoekfilters.MutatieLogVerwerkenJobRunZoekFilter;

import org.hibernate.Criteria;

public class MutatieLogVerwerkenJobRunHibernateDataAccesshelper
		extends
		JobRunHibernateDataAccessHelper<MutatieLogVerwerkenJobRun, MutatieLogVerwerkenJobRunZoekFilter>
		implements MutatieLogVerwerkenJobRunDataAccessHelper
{
	public MutatieLogVerwerkenJobRunHibernateDataAccesshelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(MutatieLogVerwerkenJobRunZoekFilter filter)
	{
		Criteria criteria = super.createCriteria(filter);
		return criteria;
	}
}
