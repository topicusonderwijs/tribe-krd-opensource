package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.StandaardToetsCodeFilterDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.resultaatstructuur.StandaardToetsCodeFilter;
import nl.topicus.eduarte.zoekfilters.StandaardToetsCodeFilterZoekFilter;

import org.hibernate.Criteria;

public class StandaardToetsCodeFilterHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<StandaardToetsCodeFilter, StandaardToetsCodeFilterZoekFilter>
		implements StandaardToetsCodeFilterDataAccessHelper
{
	public StandaardToetsCodeFilterHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(StandaardToetsCodeFilterZoekFilter filter)
	{
		Criteria criteria = createCriteria(StandaardToetsCodeFilter.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("opleiding", filter.getOpleiding());
		return criteria;
	}

	@Override
	public StandaardToetsCodeFilter getStandaardFilter(Opleiding opleiding, Cohort cohort)
	{
		if (opleiding == null || cohort == null)
			return null;
		Criteria criteria = createCriteria(StandaardToetsCodeFilter.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("opleiding", opleiding);
		builder.addEquals("cohort", cohort);
		return cachedTypedUnique(criteria);
	}
}
