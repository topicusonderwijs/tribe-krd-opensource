/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.BrinDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieAdres;
import nl.topicus.eduarte.zoekfilters.BrinZoekFilter;

import org.apache.wicket.util.string.Strings;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class BrinHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Brin, BrinZoekFilter> implements BrinDataAccessHelper
{
	public BrinHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(BrinZoekFilter filter)
	{
		Criteria criteria = createCriteria(Brin.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addILikeCheckWildcard("code", filter.getCode(), MatchMode.START);
		builder.addILikeFixedMatchMode("naam", filter.getOrganisatie(), MatchMode.ANYWHERE);

		if (!Strings.isEmpty(filter.getPlaats()))
		{
			DetachedCriteria dc = createDetachedCriteria(ExterneOrganisatieAdres.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);

			dcBuilder.createAlias("adres", "adres");
			dcBuilder.addILikeCheckWildcard("adres.plaats", filter.getPlaats(), MatchMode.START);
			dc.setProjection(Projections.property("externeOrganisatie"));
			criteria.add(Subqueries.propertyIn("id", dc));
			filter.setResultCacheable(false);
		}
		if (!filter.isGebruikLandelijkeExterneOrganisaties())
		{
			builder.addEquals("organisatie", EduArteContext.get().getInstelling());
		}

		filter.addQuickSearchCriteria(builder, "code", "naam");
		builder.addEquals("onderwijssector", filter.getOnderwijssector());

		return criteria;

	}

	@Override
	public Brin get(String code)
	{
		Asserts.assertNotEmpty("code", code);
		Criteria criteria = createCriteria(Brin.class);
		criteria.add(Restrictions.eq("code", code));

		return cachedTypedUnique(criteria);
	}

	@Override
	public List<Brin> getAlleBrins()
	{
		Criteria criteria = createCriteria(Brin.class);
		return cachedList(criteria);
	}

	@Override
	public Brin getBrinByNaam(String naam)
	{
		Criteria criteria = createCriteria(Brin.class);
		criteria.add(Restrictions.eq("naam", naam));
		return cachedTypedUnique(criteria);
	}
}