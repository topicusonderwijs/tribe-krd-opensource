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
import nl.topicus.eduarte.dao.helpers.SchaalDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal;
import nl.topicus.eduarte.zoekfilters.NaamActiefZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

public class SchaalHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Schaal, NaamActiefZoekFilter<Schaal>> implements
		SchaalDataAccessHelper
{

	public SchaalHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(NaamActiefZoekFilter<Schaal> filter)
	{
		Criteria criteria = createCriteria(Schaal.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("actief", filter.getActief());
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);
		return criteria;
	}

	@Override
	public Schaal get(String naam)
	{
		Asserts.assertNotNull("naam", naam);
		Criteria criteria = createCriteria(Schaal.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("naam", naam);

		return cachedTypedUnique(criteria);
	}
}
