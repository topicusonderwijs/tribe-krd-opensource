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
import nl.topicus.eduarte.dao.helpers.IntakegesprekDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Intakegesprek;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.zoekfilters.IntakegesprekZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class IntakegesprekHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Intakegesprek, IntakegesprekZoekFilter> implements
		IntakegesprekDataAccessHelper
{
	public IntakegesprekHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(IntakegesprekZoekFilter filter)
	{
		Criteria criteria = createCriteria(Intakegesprek.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("verbintenis", "verbintenis");
		builder.createAlias("verbintenis.deelnemer", "deelnemer");
		builder.createAlias("deelnemer.persoon", "persoon");

		addCriteria(builder, filter);

		return criteria;
	}

	@Override
	public void addCriteria(CriteriaBuilder builder, IntakegesprekZoekFilter filter)
	{
		builder.addEquals("intaker", filter.getIntaker());
		builder.addGreaterOrEquals("datumTijd", filter.getDatumTijd());
		builder.addEquals("status", filter.getStatus());

		builder.addEquals("deelnemer.id", filter.getDeelnemerId());
		builder.addEquals("persoon.achternaam", filter.getAchternaam());
		builder.addEquals("verbintenis.status", filter.getVerbintenisStatus());
	}

	@Override
	public Intakegesprek get(Long id)
	{
		return get(Intakegesprek.class, id);
	}

	@Override
	public List<Intakegesprek> getByDeelnemer(Deelnemer deelnemer)
	{
		Criteria criteria = createCriteria(Intakegesprek.class);
		criteria.add(Restrictions.eq("deelnemer", deelnemer));
		return cachedList(criteria);
	}
}
