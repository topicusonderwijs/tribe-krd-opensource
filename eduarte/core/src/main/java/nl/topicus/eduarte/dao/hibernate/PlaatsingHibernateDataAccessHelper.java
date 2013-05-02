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
import nl.topicus.eduarte.dao.helpers.PlaatsingDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.VrijVeldDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.vrijevelden.PlaatsingVrijVeld;
import nl.topicus.eduarte.zoekfilters.PlaatsingZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class PlaatsingHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Plaatsing, PlaatsingZoekFilter> implements
		PlaatsingDataAccessHelper
{
	public PlaatsingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(PlaatsingZoekFilter filter)
	{
		Criteria criteria = createCriteria(Plaatsing.class, "plaatsing");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.createAlias("verbintenis", "verbintenis");
		builder.createAlias("deelnemer", "deelnemer");
		builder.addEquals("lwoo", filter.isLwoo());

		addCriteria(builder, filter, "deelnemer");

		return criteria;
	}

	@Override
	public void addCriteria(CriteriaBuilder builder, PlaatsingZoekFilter filter,
			String deelnemerAlias)
	{
		builder.addEquals(deelnemerAlias + ".id", filter.getDeelnemerId());
		builder.addNotIn("verbintenis.status", filter.getVerbintenisStatusNot());

		if (filter.getVrijVelden().size() > 0)
		{
			DetachedCriteria dc =
				DataAccessRegistry.getHelper(VrijVeldDataAccessHelper.class).buildCriteria(
					filter.getVrijVelden(), filter.getGearchiveerd(), PlaatsingVrijVeld.class,
					"plaatsing");
			if (dc != null)
			{
				builder.propertyIn("plaatsing" + ".id", dc);
			}
		}
	}

	@Override
	public Plaatsing get(Long id)
	{
		return get(Plaatsing.class, id);
	}

	@Override
	public List<Plaatsing> getByDeelnemer(Deelnemer deelnemer)
	{
		Criteria criteria = createCriteria(Plaatsing.class);
		criteria.add(Restrictions.eq("deelnemer", deelnemer));
		return cachedList(criteria);
	}
}
