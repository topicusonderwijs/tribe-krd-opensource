/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.SoortOrganisatieEenheidDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.SoortOrganisatieEenheid;
import nl.topicus.eduarte.zoekfilters.SoortOrganisatieEenheidZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

public class SoortOrganisatieEenheidHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<SoortOrganisatieEenheid, SoortOrganisatieEenheidZoekFilter>
		implements SoortOrganisatieEenheidDataAccessHelper
{
	public SoortOrganisatieEenheidHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(SoortOrganisatieEenheidZoekFilter filter)
	{
		Criteria criteria = createCriteria(SoortOrganisatieEenheid.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("code", filter.getCode(), MatchMode.START);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);
		builder.addEquals("actief", filter.getActief());

		return criteria;
	}

	@Override
	public List<SoortOrganisatieEenheid> list(boolean alleenActieve)
	{
		SoortOrganisatieEenheidZoekFilter filter = new SoortOrganisatieEenheidZoekFilter();
		if (alleenActieve)
			filter.setActief(Boolean.TRUE);
		filter.addOrderByProperty("id");
		filter.addOrderByProperty("naam");
		return list(filter);
	}

	@Override
	public SoortOrganisatieEenheid getSoortOrganisatieEenheid(String code)
	{
		Asserts.assertNotEmpty("code", code);
		Criteria criteria = createCriteria(SoortOrganisatieEenheid.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("code", code);

		return (SoortOrganisatieEenheid) uncachedUnique(criteria);
	}

	@Override
	public SoortOrganisatieEenheid get(Long id)
	{
		return get(SoortOrganisatieEenheid.class, id);
	}
}
