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
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.krd.dao.helpers.BronSchooljaarStatusDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.BronSchooljaarStatus;
import nl.topicus.eduarte.krd.zoekfilters.BronSchooljaarStatusZoekFilter;

import org.hibernate.Criteria;

public class BronSchooljaarStatusHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<BronSchooljaarStatus, BronSchooljaarStatusZoekFilter>
		implements BronSchooljaarStatusDataAccessHelper
{
	public BronSchooljaarStatusHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(BronSchooljaarStatusZoekFilter filter)
	{
		Criteria criteria = createCriteria(BronSchooljaarStatus.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addIn("schooljaar", filter.getSchooljaren());
		builder.addEquals("aanleverpunt", filter.getAanleverpunt());
		return criteria;
	}

	@Override
	public BronSchooljaarStatus getSchooljaarStatus(BronAanleverpunt aanleverpunt,
			Schooljaar schooljaar)
	{
		Asserts.assertNotNull("aanleverpunt", aanleverpunt);
		Asserts.assertNotNull("schooljaar", schooljaar);
		Criteria criteria = createCriteria(BronSchooljaarStatus.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("schooljaar", schooljaar);
		builder.addEquals("aanleverpunt", aanleverpunt);
		return cachedUnique(criteria);
	}
}