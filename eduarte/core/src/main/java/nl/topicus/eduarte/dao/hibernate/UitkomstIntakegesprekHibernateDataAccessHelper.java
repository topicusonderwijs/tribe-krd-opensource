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
import nl.topicus.eduarte.dao.helpers.UitkomstIntakegesprekDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.UitkomstIntakegesprek;
import nl.topicus.eduarte.zoekfilters.UitkomstIntakegesprekZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

public class UitkomstIntakegesprekHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<UitkomstIntakegesprek, UitkomstIntakegesprekZoekFilter>
		implements UitkomstIntakegesprekDataAccessHelper
{
	public UitkomstIntakegesprekHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(UitkomstIntakegesprekZoekFilter filter)
	{
		Criteria criteria = createCriteria(UitkomstIntakegesprek.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("code", filter.getCode(), MatchMode.START);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);
		builder.addEquals("actief", filter.getActief());
		return criteria;
	}

	@Override
	public List<UitkomstIntakegesprek> list()
	{
		UitkomstIntakegesprekZoekFilter filter = new UitkomstIntakegesprekZoekFilter();
		filter.addOrderByProperty("id");
		filter.addOrderByProperty("naam");
		return list(filter);
	}

	@Override
	public UitkomstIntakegesprek getUitkomstIntakegesprek(String code)
	{
		Asserts.assertNotEmpty("code", code);
		Criteria criteria = createCriteria(UitkomstIntakegesprek.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("code", code);

		return (UitkomstIntakegesprek) uncachedUnique(criteria);
	}

	@Override
	public UitkomstIntakegesprek get(Long id)
	{
		return get(UitkomstIntakegesprek.class, id);
	}
}
