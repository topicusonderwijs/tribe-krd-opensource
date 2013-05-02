/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.SoortVooropleidingDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding.SoortOnderwijs;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.zoekfilters.SoortVooropleidingZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class SoortVooropleidingHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<SoortVooropleiding, SoortVooropleidingZoekFilter>
		implements SoortVooropleidingDataAccessHelper
{
	public SoortVooropleidingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(SoortVooropleidingZoekFilter filter)
	{
		Criteria criteria = createCriteria(SoortVooropleiding.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);
		builder.addILikeCheckWildcard("code", filter.getCode(), MatchMode.START);
		builder.addEquals("actief", filter.getActief());
		if (filter.getSoortOnderwijs() != null)
			builder.addOrs(Restrictions.eq("soortOnderwijsMetDiploma", filter.getSoortOnderwijs()),
				Restrictions.eq("soortOnderwijsZonderDiploma", filter.getSoortOnderwijs()));

		filter.addQuickSearchCriteria(builder, "code", "naam");

		return criteria;
	}

	@Override
	public SoortVooropleiding get(Instelling organisatie, String naam)
	{
		Asserts.assertNotEmpty("naam", naam);
		Criteria criteria = createCriteria(SoortVooropleiding.class);
		criteria.add(Restrictions.eq("organisatie", organisatie));
		criteria.add(Restrictions.eq("naam", naam));

		return cachedTypedUnique(criteria);
	}

	@Override
	public SoortVooropleiding get(SoortOnderwijs soortOnderwijs)
	{
		Criteria criteria = createCriteria(SoortVooropleiding.class);
		criteria.add(Restrictions.eq("soortOnderwijsMetDiploma", soortOnderwijs));
		List<SoortVooropleiding> soorten = cachedTypedList(criteria);
		if (soorten.size() == 1)
			return soorten.get(0);
		return null;
	}
}
