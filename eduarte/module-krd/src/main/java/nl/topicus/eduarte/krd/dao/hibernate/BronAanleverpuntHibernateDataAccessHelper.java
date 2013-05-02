/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.krd.dao.helpers.BronAanleverpuntDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * @author marrink
 */
public class BronAanleverpuntHibernateDataAccessHelper extends
		HibernateDataAccessHelper<BronAanleverpunt> implements BronAanleverpuntDataAccessHelper
{

	/**
	 * @param provider
	 */
	public BronAanleverpuntHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public List<BronAanleverpunt> getBronAanleverpunten()
	{
		Criteria criteria = createCriteria(BronAanleverpunt.class);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.asc("nummer"));
		return cachedTypedList(criteria);
	}

	@Override
	public boolean existsBronAanleverpunten()
	{
		Criteria criteria = createCriteria(BronAanleverpunt.class);
		criteria.setProjection(Projections.rowCount());
		return (Long) cachedUnique(criteria) > 0;
	}

	@Override
	public BronAanleverpunt getAanleverpunt(int aanleverpuntnummer)
	{
		Criteria criteria = createCriteria(BronAanleverpunt.class);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("nummer", aanleverpuntnummer));
		return cachedTypedUnique(criteria);
	}
}
