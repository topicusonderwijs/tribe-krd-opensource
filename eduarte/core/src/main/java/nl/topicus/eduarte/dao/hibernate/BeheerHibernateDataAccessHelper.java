/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.BeheerDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Beheer;

/**
 * @author marrink
 */
public class BeheerHibernateDataAccessHelper extends HibernateDataAccessHelper<Beheer> implements
		BeheerDataAccessHelper
{

	public BeheerHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public Beheer getBeheer()
	{
		return cachedTypedUnique(createCriteria(Beheer.class));
	}
}
