/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.ContractDataAccessHelper;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.zoekfilters.ContractZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;

public class ContractHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Contract, ContractZoekFilter> implements
		ContractDataAccessHelper
{
	public ContractHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(ContractZoekFilter filter)
	{
		Criteria criteria = createCriteria(Contract.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("code", filter.getCode(), MatchMode.START);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);
		if (!filter.isToonInactief())
		{
			builder.addLessOrEquals("begindatum", filter.getPeildatum());
			builder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
		}
		builder.addGreaterOrEquals("eindeInstroom", filter.getInschrijfdatum());
		builder.addEquals("soortContract", filter.getSoortContract());
		builder.addEquals("beheerder", filter.getBeheerder());
		builder.addEquals("typeFinanciering", filter.getTypeFinanciering());

		builder.createAlias("externeOrganisatie", "externeOrganisatie",
			CriteriaSpecification.LEFT_JOIN);

		filter.addQuickSearchCriteria(builder, "code", "naam");

		return criteria;
	}

	@Override
	public List<Contract> list(Date datum)
	{
		ContractZoekFilter filter = new ContractZoekFilter();
		filter.addOrderByProperty("id");
		filter.addOrderByProperty("naam");
		return list(filter);
	}

	@Override
	public Contract getContract(String code)
	{
		Asserts.assertNotEmpty("code", code);
		Criteria criteria = createCriteria(Contract.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("code", code);

		return (Contract) uncachedUnique(criteria);
	}

	@Override
	public Contract get(Long id)
	{
		return get(Contract.class, id);
	}
}
