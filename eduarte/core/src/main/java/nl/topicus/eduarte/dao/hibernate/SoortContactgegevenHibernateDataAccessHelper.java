/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.entities.adres.StandaardContactgegeven;
import nl.topicus.eduarte.entities.adres.TypeContactgegeven;
import nl.topicus.eduarte.zoekfilters.SoortContactgegevenZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class SoortContactgegevenHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<SoortContactgegeven, SoortContactgegevenZoekFilter>
		implements nl.topicus.eduarte.dao.helpers.SoortContactgegevenDataAccessHelper
{
	public SoortContactgegevenHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(SoortContactgegevenZoekFilter filter)
	{
		Criteria criteria = createCriteria(SoortContactgegeven.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);
		builder.addILikeCheckWildcard("code", filter.getCode(), MatchMode.START);
		builder.addEquals("typeContactgegeven", filter.getTypeContactgegeven());
		if (filter.isGeenStandaardContactgegevenIngevuldMeenemen())
			builder.addNullOrIn("standaardContactgegeven", filter.getStandaardContactgegeven());
		else
			builder.addIn("standaardContactgegeven", filter.getStandaardContactgegeven());
		builder.addEquals("actief", filter.getActief());

		builder.addNotIn("id", filter.getExcludeIds());

		return criteria;
	}

	@Override
	public SoortContactgegeven get(String code)
	{
		Asserts.assertNotEmpty("code", code);

		Criteria criteria = createCriteria(SoortContactgegeven.class);
		criteria.add(Restrictions.eq("code", code));

		return cachedTypedUnique(criteria);
	}

	@Override
	public SoortContactgegeven getByNaam(String naam)
	{
		Asserts.assertNotEmpty("naam", naam);

		Criteria criteria = createCriteria(SoortContactgegeven.class);
		criteria.add(Restrictions.eq("naam", naam));

		return cachedTypedUnique(criteria);
	}

	@Override
	public List<SoortContactgegeven> list(List<StandaardContactgegeven> standaarden,
			boolean geenStandaardContactgegevenIngevuldMeenemen)
	{
		SoortContactgegevenZoekFilter filter = new SoortContactgegevenZoekFilter();
		filter.setActief(true);
		filter.setStandaardContactgegeven(standaarden);
		filter
			.setGeenStandaardContactgegevenIngevuldMeenemen(geenStandaardContactgegevenIngevuldMeenemen);

		return super.list(filter);
	}

	@Override
	public SoortContactgegeven get(TypeContactgegeven typegegeven)
	{
		Asserts.assertNotEmpty("typegegeven", typegegeven);

		SoortContactgegeven soortContactgegeven = null;

		Criteria criteria = createCriteria(SoortContactgegeven.class);
		criteria.add(Restrictions.eq("typeContactgegeven", typegegeven));

		List<SoortContactgegeven> soortContactgegevens = cachedTypedList(criteria);
		for (SoortContactgegeven item : soortContactgegevens)
		{
			if (item.getStandaardContactgegeven() != null)
			{
				soortContactgegeven = item;
			}
		}

		if (soortContactgegeven == null && !soortContactgegevens.isEmpty())
		{
			for (SoortContactgegeven cg : soortContactgegevens)
			{
				if (cg.isActief())
					return cg;
			}
		}

		return soortContactgegeven;
	}
}
