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
import nl.topicus.eduarte.dao.helpers.FunctieDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Functie;
import nl.topicus.eduarte.zoekfilters.FunctieZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class FunctieHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Functie, FunctieZoekFilter> implements
		FunctieDataAccessHelper
{
	public FunctieHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(FunctieZoekFilter filter)
	{
		Criteria criteria = createCriteria(Functie.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);
		builder.addILikeCheckWildcard("code", filter.getCode(), MatchMode.START);
		builder.addEquals("actief", filter.getActief());

		return criteria;
	}

	@Override
	public Functie get(String naam)
	{
		Asserts.assertNotEmpty("naam", naam);
		Criteria criteria = createCriteria(Functie.class);
		criteria.add(Restrictions.eq("naam", naam));

		return cachedTypedUnique(criteria);
	}
}
