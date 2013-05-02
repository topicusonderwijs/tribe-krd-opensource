/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.eduarte.dao.helpers.RolDataAccessHelper;
import nl.topicus.eduarte.entities.security.authorization.AuthorisatieNiveau;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.zoekfilters.RolZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class RolHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Rol, RolZoekFilter> implements RolDataAccessHelper
{
	public RolHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(RolZoekFilter filter)
	{
		Criteria criteria = createCriteria(Rol.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		if (filter.getAuthorisatieNiveau() != null)
			builder.addIn("authorisatieNiveau", filter.getAuthorisatieNiveau().implied());
		builder.addILikeFixedMatchMode("naam", filter.getNaam(), MatchMode.ANYWHERE);
		builder.addEquals("rechtenSoort", filter.getRechtenSoort());
		builder.addEquals("categorie", filter.getCategorie());

		if (filter.getAccount() != null)
		{
			builder.createAlias("accounts", "accounts");
			builder.addEquals("accounts.account", filter.getAccount());
		}

		filter.addQuickSearchCriteria(builder, "naam");

		return criteria;

	}

	@Override
	public List<Rol> getRollen(AuthorisatieNiveau niveau)
	{
		Criteria criteria = createCriteria(Rol.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("authorisatieNiveau", niveau);
		return cachedList(criteria);
	}

	@Override
	public Rol getRol(String naam)
	{
		Criteria criteria = createCriteria(Rol.class);
		criteria.add(Restrictions.eq("naam", naam));
		return cachedTypedUnique(criteria);
	}

	@Override
	public List<String> getCategorieen(String fragment)
	{
		Criteria criteria = createCriteria(Rol.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeFixedMatchMode("categorie", fragment, MatchMode.START);
		criteria.setProjection(Projections.distinct(Projections.property("categorie")));
		return cachedList(criteria);
	}

	@Override
	public List<Integer> getZorglijnen(Integer max)
	{
		if (max == null)
			return Collections.emptyList();
		Criteria criteria = createCriteria(Rol.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addLessOrEquals("zorglijn", max);
		criteria.setProjection(Projections.distinct(Projections.property("zorglijn")));
		List<Integer> ret = cachedList(criteria);
		ret.remove(null);
		Collections.sort(ret);
		return ret;
	}
}
