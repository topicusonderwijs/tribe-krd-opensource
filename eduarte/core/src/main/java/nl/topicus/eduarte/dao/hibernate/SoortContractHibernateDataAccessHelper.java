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
import nl.topicus.eduarte.dao.helpers.SoortContractDataAccessHelper;
import nl.topicus.eduarte.entities.contract.SoortContract;
import nl.topicus.eduarte.zoekfilters.SoortContractZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

public class SoortContractHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<SoortContract, SoortContractZoekFilter> implements
		SoortContractDataAccessHelper
{
	public SoortContractHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(SoortContractZoekFilter filter)
	{
		Criteria criteria = createCriteria(SoortContract.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("code", filter.getCode(), MatchMode.START);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);
		builder.addEquals("inburgering", filter.getInburgering());
		builder.addEquals("actief", filter.getActief());

		return criteria;
	}

	@Override
	public List<SoortContract> list()
	{
		SoortContractZoekFilter filter = new SoortContractZoekFilter();
		filter.addOrderByProperty("id");
		filter.addOrderByProperty("naam");
		return list(filter);
	}

	@Override
	public SoortContract getSoortContract(String code)
	{
		Asserts.assertNotEmpty("code", code);
		Criteria criteria = createCriteria(SoortContract.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("code", code);

		return (SoortContract) uncachedUnique(criteria);
	}

	@Override
	public SoortContract get(Long id)
	{
		return get(SoortContract.class, id);
	}
}
