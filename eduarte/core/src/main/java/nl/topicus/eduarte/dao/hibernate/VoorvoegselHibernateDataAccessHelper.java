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
import nl.topicus.eduarte.dao.helpers.VoorvoegselDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Voorvoegsel;
import nl.topicus.eduarte.zoekfilters.VoorvoegselZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class VoorvoegselHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Voorvoegsel, VoorvoegselZoekFilter> implements
		VoorvoegselDataAccessHelper
{
	public VoorvoegselHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(VoorvoegselZoekFilter filter)
	{
		Criteria criteria = createCriteria(Voorvoegsel.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);

		filter.addQuickSearchCriteria(builder, "naam");

		return criteria;
	}

	@Override
	public Voorvoegsel get(String naam)
	{
		Criteria criteria = createCriteria(Voorvoegsel.class);
		criteria.add(Restrictions.eq("naam", naam));

		/*
		 * @mantis 0055746 Hack om er voor te zorgen dat als de database standaard case
		 * insensitive zoekt hier wel het juiste voorvoegsel wordt teruggegeven.
		 */
		List<Voorvoegsel> voorvoegsels = cachedTypedList(criteria);
		for (Voorvoegsel voorvoegsel : voorvoegsels)
		{
			if (voorvoegsel.getNaam().equals(naam))
				return voorvoegsel;
		}

		return null;
	}

	@Override
	public Voorvoegsel get(Long id)
	{
		return get(Voorvoegsel.class, id);
	}
}
