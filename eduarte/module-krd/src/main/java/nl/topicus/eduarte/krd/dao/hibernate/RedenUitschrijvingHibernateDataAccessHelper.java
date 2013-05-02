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
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.zoekfilters.RedenUitschrijvingZoekFilter;
import nl.topicus.eduarte.zoekfilters.RedenUitschrijvingZoekFilter.SoortRedenUitschrijvingTonen;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class RedenUitschrijvingHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<RedenUitschrijving, RedenUitschrijvingZoekFilter>
		implements nl.topicus.eduarte.dao.helpers.RedenUitschrijvingDataAccessHelper
{
	public RedenUitschrijvingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(RedenUitschrijvingZoekFilter filter)
	{
		Criteria criteria = createCriteria(RedenUitschrijving.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);
		builder.addILikeCheckWildcard("code", filter.getCode(), MatchMode.START);

		if (filter.getSoort() != null && filter.getSoort() != SoortRedenUitschrijvingTonen.Alle)
		{
			if (filter.getSoort().equals(SoortRedenUitschrijvingTonen.BPV))
				builder.addEquals("tonenBijBPV", true);
			else if (filter.getSoort().equals(SoortRedenUitschrijvingTonen.Verbintenis))
				builder.addEquals("tonenBijVerbintenis", true);
		}

		if (filter.getActief() != null)
			builder.addEquals("actief", filter.getActief());

		if (filter.getRedenUitval() != null)
			builder.addEquals("redenUitval", filter.getRedenUitval());

		if (filter.getUitstroomredenWI() != null)
			builder.addEquals("uitstroomredenWI", filter.getUitstroomredenWI());

		return criteria;
	}

	@Override
	public RedenUitschrijving get(Instelling organisatie, String naam)
	{
		Asserts.assertNotEmpty("naam", naam);
		Criteria criteria = createCriteria(RedenUitschrijving.class);
		criteria.add(Restrictions.eq("organisatie", organisatie));
		criteria.add(Restrictions.eq("naam", naam));

		return cachedTypedUnique(criteria);
	}

	@Override
	public RedenUitschrijving getOverlijden()
	{
		Criteria criteria = createCriteria(RedenUitschrijving.class);
		criteria.add(Restrictions.eq("actief", true));
		criteria.add(Restrictions.eq("overlijden", true));
		criteria.setMaxResults(1);

		return cachedTypedUnique(criteria);
	}
}
