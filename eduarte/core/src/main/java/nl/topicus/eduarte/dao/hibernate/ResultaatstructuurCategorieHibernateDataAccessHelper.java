/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.zoekfilters.NullFilter;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurCategorieDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.ResultaatstructuurCategorie;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurCategorieZoekFilter;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

public class ResultaatstructuurCategorieHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<ResultaatstructuurCategorie, ResultaatstructuurCategorieZoekFilter>
		implements ResultaatstructuurCategorieDataAccessHelper
{
	public ResultaatstructuurCategorieHibernateDataAccessHelper(
			HibernateSessionProvider sessionProvider, QueryInterceptor interceptor)
	{
		super(sessionProvider, interceptor);
	}

	@Override
	protected Criteria createCriteria(ResultaatstructuurCategorieZoekFilter filter)
	{
		Criteria criteria = createCriteria(ResultaatstructuurCategorie.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("actief", filter.getActief());
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.ANYWHERE);
		return criteria;
	}

	@Override
	public List<ResultaatstructuurCategorie> getCategorien(ResultaatstructuurZoekFilter filter)
	{
		Criteria criteria = createCriteria(Resultaatstructuur.class, "resultaatstructuur");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class).addCriteria(builder,
			filter, "resultaatstructuur", false);
		builder.addNullFilterExpression("categorie", NullFilter.IsNotNull);
		criteria.setProjection(Projections.distinct(Projections.property("categorie")));
		return uncachedList(criteria);
	}

	@Override
	public ResultaatstructuurCategorie get(String naam)
	{
		Criteria criteria = createCriteria(ResultaatstructuurCategorie.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("naam", naam);
		return cachedTypedUnique(criteria);
	}
}
