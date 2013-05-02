/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.SchaalwaardeDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaalwaarde;
import nl.topicus.eduarte.zoekfilters.SchaalwaardeZoekFilter;

import org.hibernate.Criteria;

public class SchaalwaardeHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Schaalwaarde, SchaalwaardeZoekFilter> implements
		SchaalwaardeDataAccessHelper
{
	public SchaalwaardeHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(SchaalwaardeZoekFilter filter)
	{
		Criteria criteria = createCriteria(Schaalwaarde.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("schaal", filter.getSchaal());
		return criteria;
	}
}
