/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.entities.inschrijving.Schooladvies;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.zoekfilters.SchooladviesZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class SchooladviesHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Schooladvies, SchooladviesZoekFilter> implements
		nl.topicus.eduarte.dao.helpers.SchooladviesDataAccessHelper
{
	public SchooladviesHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(SchooladviesZoekFilter filter)
	{
		Criteria criteria = createCriteria(Schooladvies.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);
		builder.addEquals("actief", filter.getActief());

		return criteria;
	}

	@Override
	public Schooladvies get(Instelling organisatie, String naam)
	{
		Asserts.assertNotEmpty("naam", naam);
		Criteria criteria = createCriteria(Schooladvies.class);
		criteria.add(Restrictions.eq("organisatie", organisatie));
		criteria.add(Restrictions.eq("naam", naam));

		return cachedTypedUnique(criteria);
	}
}
