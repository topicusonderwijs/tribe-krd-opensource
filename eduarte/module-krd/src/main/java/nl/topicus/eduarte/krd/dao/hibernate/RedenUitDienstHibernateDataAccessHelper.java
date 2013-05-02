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
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.entities.personen.RedenUitDienst;
import nl.topicus.eduarte.zoekfilters.RedenUitDienstZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class RedenUitDienstHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<RedenUitDienst, RedenUitDienstZoekFilter> implements
		nl.topicus.eduarte.dao.helpers.RedenUitDienstDataAccessHelper
{
	public RedenUitDienstHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(RedenUitDienstZoekFilter filter)
	{
		Criteria criteria = createCriteria(RedenUitDienst.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);
		builder.addILikeCheckWildcard("code", filter.getCode(), MatchMode.START);
		builder.addEquals("actief", filter.getActief());
		return criteria;
	}

	@Override
	public RedenUitDienst get(Instelling organisatie, String naam)
	{
		Asserts.assertNotEmpty("naam", naam);
		Criteria criteria = createCriteria(RedenUitDienst.class);
		criteria.add(Restrictions.eq("organisatie", organisatie));
		criteria.add(Restrictions.eq("naam", naam));

		return cachedTypedUnique(criteria);
	}
}
