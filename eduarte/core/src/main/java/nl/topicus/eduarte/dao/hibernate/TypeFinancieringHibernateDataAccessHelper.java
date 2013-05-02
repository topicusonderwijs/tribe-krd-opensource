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
import nl.topicus.eduarte.dao.helpers.TypeFinancieringDataAccessHelper;
import nl.topicus.eduarte.entities.contract.TypeFinanciering;
import nl.topicus.eduarte.zoekfilters.TypeFinancieringZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

public class TypeFinancieringHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<TypeFinanciering, TypeFinancieringZoekFilter> implements
		TypeFinancieringDataAccessHelper
{
	public TypeFinancieringHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(TypeFinancieringZoekFilter filter)
	{
		Criteria criteria = createCriteria(TypeFinanciering.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("code", filter.getCode(), MatchMode.START);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);
		builder.addEquals("actief", filter.getActief());

		return criteria;
	}

	@Override
	public List<TypeFinanciering> list()
	{
		TypeFinancieringZoekFilter filter = new TypeFinancieringZoekFilter();
		filter.addOrderByProperty("id");
		filter.addOrderByProperty("naam");
		return list(filter);
	}

	@Override
	public TypeFinanciering getTypeFinanciering(String code)
	{
		Asserts.assertNotEmpty("code", code);
		Criteria criteria = createCriteria(TypeFinanciering.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("code", code);

		return (TypeFinanciering) uncachedUnique(criteria);
	}

	@Override
	public TypeFinanciering get(Long id)
	{
		return get(TypeFinanciering.class, id);
	}
}
