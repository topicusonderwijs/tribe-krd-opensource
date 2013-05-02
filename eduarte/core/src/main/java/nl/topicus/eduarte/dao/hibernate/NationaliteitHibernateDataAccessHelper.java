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
import nl.topicus.eduarte.dao.helpers.NationaliteitDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Nationaliteit;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class NationaliteitHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<Nationaliteit, LandelijkCodeNaamZoekFilter<Nationaliteit>>
		implements NationaliteitDataAccessHelper
{
	public NationaliteitHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(LandelijkCodeNaamZoekFilter<Nationaliteit> filter)
	{
		Criteria criteria = createCriteria(Nationaliteit.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("code", filter.getCode(), MatchMode.START);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);

		filter.addQuickSearchCriteria(builder, "code", "naam");

		return criteria;
	}

	@Override
	public Nationaliteit get(String code)
	{
		Asserts.assertNotEmpty("code", code);
		Criteria criteria = createCriteria(Nationaliteit.class);
		criteria.add(Restrictions.eq("code", code));

		return cachedTypedUnique(criteria);
	}
}
