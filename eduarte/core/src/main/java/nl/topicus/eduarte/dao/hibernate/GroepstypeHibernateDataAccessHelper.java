/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.GroepstypeDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groepstype;
import nl.topicus.eduarte.zoekfilters.GroepstypeZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

public class GroepstypeHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Groepstype, GroepstypeZoekFilter> implements
		GroepstypeDataAccessHelper
{
	public GroepstypeHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(GroepstypeZoekFilter filter)
	{
		Criteria criteria = createCriteria(Groepstype.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addILikeCheckWildcard("code", filter.getCode(), MatchMode.START);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);
		builder.addEquals("actief", filter.getActief());
		builder.addEquals("plaatsingsgroep", filter.getPlaatsingsgroep());

		return criteria;
	}
}
