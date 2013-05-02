/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.MeeteenheidDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.opleiding.OpleidingAanbod;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.*;
import nl.topicus.eduarte.zoekfilters.MeeteenheidZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class MeeteenheidHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Meeteenheid, MeeteenheidZoekFilter> implements
		MeeteenheidDataAccessHelper
{
	public MeeteenheidHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(MeeteenheidZoekFilter filter)
	{
		Criteria criteria = createCriteria(Meeteenheid.class);

		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addNullFilterExpression("organisatie", filter.getOrganisatieFilter());
		builder.addILikeFixedMatchMode("naam", filter.getNaam(), MatchMode.ANYWHERE);
		builder
			.addILikeFixedMatchMode("omschrijving", filter.getOmschrijving(), MatchMode.ANYWHERE);

		return criteria;
	}

	@Override
	public String getLabelVoorWaarde(Meeteenheid meeteenheid, int waarde)
	{
		Criteria criteria = createCriteria(MeeteenheidWaarde.class);
		criteria.add(Restrictions.eq("waarde", waarde));
		criteria.add(Restrictions.eq("meeteenheid", meeteenheid));
		MeeteenheidWaarde meeteenheidWaarde = cachedUnique(criteria);
		if (meeteenheidWaarde == null)
			return null;
		return meeteenheidWaarde.getLabel();
	}

	@Override
	public Integer getWaardeVoorLabel(Meeteenheid meeteenheid, String label)
	{
		Criteria criteria = createCriteria(MeeteenheidWaarde.class);
		criteria.add(Restrictions.eq("label", label));
		criteria.add(Restrictions.eq("meeteenheid", meeteenheid));
		MeeteenheidWaarde meeteenheidWaarde = cachedUnique(criteria);
		if (meeteenheidWaarde == null)
			return null;
		return meeteenheidWaarde.getWaarde();
	}

	@Override
	public MeeteenheidWaarde getMinimumWaarde(Meeteenheid meeteenheid)
	{
		Criteria criteria = createCriteria(MeeteenheidWaarde.class);
		criteria.add(Restrictions.eq("meeteenheid", meeteenheid));
		criteria.addOrder(Order.asc("waarde"));
		criteria.setMaxResults(1);
		return cachedUnique(criteria);
	}

	@Override
	public MeeteenheidWaarde getMaximumWaarde(Meeteenheid meeteenheid)
	{
		Criteria criteria = createCriteria(MeeteenheidWaarde.class);
		criteria.add(Restrictions.eq("meeteenheid", meeteenheid));
		criteria.addOrder(Order.desc("waarde"));
		criteria.setMaxResults(1);

		return cachedUnique(criteria);
	}

	@Override
	public MeeteenheidWaarde getMeeteenheidWaarde(Meeteenheid meeteenheid, String label)
	{
		Criteria criteria = createCriteria(MeeteenheidWaarde.class);
		criteria.add(Restrictions.eq("meeteenheid", meeteenheid));
		criteria.add(Restrictions.eq("label", label));
		return cachedUnique(criteria);
	}

	@Override
	public MeeteenheidWaarde getMeeteenheidWaarde(Meeteenheid meeteenheid, Integer waarde)
	{
		Criteria criteria = createCriteria(MeeteenheidWaarde.class);
		criteria.add(Restrictions.eq("meeteenheid", meeteenheid));
		criteria.add(Restrictions.eq("waarde", waarde));
		return cachedUnique(criteria);
	}

	@Override
	public boolean isMeeteenheidInGebruik(Meeteenheid meeteenheid)
	{
		return meeteenheid.isInGebruik();
	}

	@Override
	public Meeteenheid getMeeteenheid(Verbintenis verbintenis, CompetentieMatrix matrix,
			Opleiding opleiding, Cohort cohort)
	{
		MeeteenheidKoppelType type;
		CompetentieMatrix unproxiedMatrix = (CompetentieMatrix) matrix.doUnproxy();

		if (unproxiedMatrix instanceof Uitstroom)
		{
			type = MeeteenheidKoppelType.Algemeen;
		}
		else if (unproxiedMatrix instanceof Kwalificatiedossier)
		{
			type = MeeteenheidKoppelType.Algemeen;
		}
		else if (unproxiedMatrix instanceof LLBMatrix)
		{
			type = MeeteenheidKoppelType.LLB;
		}
		else if (unproxiedMatrix instanceof VrijeMatrix)
		{
			type = MeeteenheidKoppelType.Vrij;
			if (((VrijeMatrix) unproxiedMatrix).getDeelnemer() == null)
			{
				return ((VrijeMatrix) unproxiedMatrix).getMeeteenheid();
			}
		}
		else
		{
			throw new IllegalArgumentException("Cannot determine Meeteenheid for "
				+ matrix.getClass() + " yet");
		}
		return getMeeteenheid(verbintenis, opleiding, cohort, type);
	}

	public Meeteenheid getMeeteenheid(Verbintenis verbintenis, Opleiding opleiding, Cohort cohort,
			MeeteenheidKoppelType type)
	{

		Criteria criteria = createCriteria(MeeteenheidKoppel.class, "meeteenheidKoppel");

		criteria.createAlias("meeteenheid", "meeteenheid");
		CriteriaBuilder mehBuilder = new CriteriaBuilder(criteria);
		mehBuilder.addEquals("opleiding", opleiding);
		mehBuilder.addEquals("type", type);
		mehBuilder.addEquals("cohort", cohort);
		criteria.setProjection(Projections.property("meeteenheid"));
		Meeteenheid meeteenheid = cachedUnique(criteria);
		if (meeteenheid != null)
			return meeteenheid;
		return getMeeteenheid(verbintenis.getOrganisatieEenheid(), type, cohort);
	}

	@Override
	public Meeteenheid getMeeteenheid(CompetentieMatrix matrix, Opleiding opleiding, Cohort cohort)
	{
		MeeteenheidKoppelType type;
		CompetentieMatrix unproxiedMatrix = (CompetentieMatrix) matrix.doUnproxy();

		if (unproxiedMatrix instanceof Uitstroom)
		{
			type = MeeteenheidKoppelType.Algemeen;
		}
		else if (unproxiedMatrix instanceof Kwalificatiedossier)
		{
			type = MeeteenheidKoppelType.Algemeen;
		}
		else if (unproxiedMatrix instanceof LLBMatrix)
		{
			type = MeeteenheidKoppelType.LLB;
		}
		else if (unproxiedMatrix instanceof VrijeMatrix)
		{
			type = MeeteenheidKoppelType.Vrij;
			if (((VrijeMatrix) unproxiedMatrix).getDeelnemer() == null)
			{
				return ((VrijeMatrix) unproxiedMatrix).getMeeteenheid();
			}
		}
		else
		{
			throw new IllegalArgumentException("Cannot determine Meeteenheid for "
				+ matrix.getClass() + " yet");
		}
		return getMeeteenheid(opleiding, cohort, type);
	}

	@Override
	public Meeteenheid getMeeteenheid(Opleiding opleiding, Cohort cohort, MeeteenheidKoppelType type)
	{

		Criteria criteria = createCriteria(MeeteenheidKoppel.class, "meeteenheidKoppel");

		criteria.createAlias("meeteenheid", "meeteenheid");
		CriteriaBuilder mehBuilder = new CriteriaBuilder(criteria);
		mehBuilder.addEquals("opleiding", opleiding);
		mehBuilder.addEquals("type", type);
		mehBuilder.addEquals("cohort", cohort);
		criteria.setProjection(Projections.property("meeteenheid"));

		Meeteenheid meeteenheid = cachedUnique(criteria);
		if (meeteenheid != null)
		{
			return meeteenheid;
		}

		for (OpleidingAanbod oa : opleiding.getAanbod())
		{
			meeteenheid = getMeeteenheid(oa.getOrganisatieEenheid(), type, cohort);
			if (meeteenheid != null)
			{
				break;
			}
		}
		return meeteenheid;
	}

	@Override
	public Meeteenheid getMeeteenheid(OrganisatieEenheid organisatieEenheid,
			MeeteenheidKoppelType type, Cohort cohort)
	{
		Criteria criteria = createCriteria(MeeteenheidKoppel.class, "meeteenheidKoppel");

		criteria.createAlias("meeteenheid", "meeteenheid");
		CriteriaBuilder mehBuilder = new CriteriaBuilder(criteria);
		mehBuilder.addEquals("organisatieEenheid", organisatieEenheid);
		mehBuilder.addEquals("type", type);
		mehBuilder.addEquals("cohort", cohort);
		criteria.setProjection(Projections.property("meeteenheid"));

		Meeteenheid meeteenheid = cachedUnique(criteria);
		if (meeteenheid != null)
			return meeteenheid;

		if (organisatieEenheid != null)
		{
			if (organisatieEenheid.getParent() == null)
				return null;

			return getMeeteenheid(organisatieEenheid.getParent(), type, cohort);
		}

		return null;
	}

	@Override
	public Meeteenheid getDirecteMeeteenheid(Opleiding opleiding, MeeteenheidKoppelType type)
	{
		Criteria criteria = createCriteria(MeeteenheidKoppel.class, "meeteenheidKoppel");

		criteria.createAlias("meeteenheid", "meeteenheid");
		CriteriaBuilder mehBuilder = new CriteriaBuilder(criteria);
		mehBuilder.addEquals("opleiding", opleiding);
		mehBuilder.addEquals("type", type);
		criteria.setProjection(Projections.property("meeteenheid"));

		return cachedUnique(criteria);
	}

	@Override
	public Meeteenheid getDirecteMeeteenheid(OrganisatieEenheid organisatieEenheid, Cohort cohort,
			MeeteenheidKoppelType type)
	{
		Criteria criteria = createCriteria(MeeteenheidKoppel.class, "meeteenheidKoppel");

		criteria.createAlias("meeteenheid", "meeteenheid");
		CriteriaBuilder mehBuilder = new CriteriaBuilder(criteria);
		mehBuilder.addEquals("organisatieEenheid", organisatieEenheid);
		mehBuilder.addEquals("type", type);
		mehBuilder.addEquals("cohort", cohort);
		criteria.setProjection(Projections.property("meeteenheid"));

		return cachedUnique(criteria);
	}

	@Override
	public MeeteenheidKoppel getMeeteenheidKoppeling(OrganisatieEenheid organisatieEenheid,
			Cohort cohort, MeeteenheidKoppelType type)
	{
		Criteria criteria = createCriteria(MeeteenheidKoppel.class);
		criteria.add(Restrictions.eq("organisatieEenheid", organisatieEenheid));
		criteria.add(Restrictions.eq("type", type));
		criteria.add(Restrictions.eq("cohort", cohort));
		return cachedUnique(criteria);
	}

	@Override
	public MeeteenheidKoppel getMeeteenheidKoppeling(Opleiding opleiding, Cohort cohort,
			MeeteenheidKoppelType type)
	{
		Criteria criteria = createCriteria(MeeteenheidKoppel.class);
		criteria.add(Restrictions.eq("opleiding", opleiding));
		criteria.add(Restrictions.eq("cohort", cohort));
		criteria.add(Restrictions.eq("type", type));
		return cachedUnique(criteria);
	}

	@Override
	public Meeteenheid getTaalMeeteenheid(Verbintenis verbintenis, Opleiding opleiding,
			Cohort cohort)
	{
		return getMeeteenheid(verbintenis, opleiding, cohort, MeeteenheidKoppelType.Taal);
	}
}
