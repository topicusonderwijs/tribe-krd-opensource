/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.LandDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class LandHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Land, LandelijkCodeNaamZoekFilter<Land>> implements
		LandDataAccessHelper
{
	public LandHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(LandelijkCodeNaamZoekFilter<Land> filter)
	{
		Criteria criteria = createCriteria(Land.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.ANYWHERE);
		builder.addEquals("code", filter.getCode());

		filter.addQuickSearchCriteria(builder, "code", "naam");

		return criteria;
	}

	@Override
	public Land get(String code)
	{
		Asserts.assertNotEmpty("code", code);
		Criteria criteria = createCriteria(Land.class);
		criteria.add(Restrictions.eq("code", code));

		return cachedTypedUnique(criteria);
	}

	@Override
	public Land getByISO(String isoCode)
	{
		Asserts.assertNotEmpty("isoCode", isoCode);
		Criteria criteria = createCriteria(Land.class);
		criteria.add(Restrictions.ilike("isoCode", isoCode));

		return cachedTypedUnique(criteria);
	}

	@Override
	public Land getByNaam(String naam)
	{
		Asserts.assertNotEmpty("naam", naam);
		Criteria criteria = createCriteria(Land.class);
		criteria.add(Restrictions.ilike("naam", naam));

		return cachedTypedUnique(criteria);
	}
}
