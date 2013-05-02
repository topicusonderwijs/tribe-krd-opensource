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
import nl.topicus.eduarte.dao.helpers.VooropleidingDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.zoekfilters.VooropleidingZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class VooropleidingHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Vooropleiding, VooropleidingZoekFilter> implements
		VooropleidingDataAccessHelper
{
	public VooropleidingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(VooropleidingZoekFilter filter)
	{
		Criteria criteria = createCriteria(Brin.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addEquals("id", filter.getId());

		return criteria;
	}

	@Override
	public Vooropleiding get(Long id)
	{
		Asserts.assertNotEmpty("id", id);
		Criteria criteria = createCriteria(Vooropleiding.class);
		criteria.add(Restrictions.eq("id", id));

		return cachedTypedUnique(criteria);
	}
}