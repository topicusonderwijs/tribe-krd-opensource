/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldEntiteit;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldOptieKeuze;
import nl.topicus.eduarte.zoekfilters.VrijVeldZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class VrijVeldHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<VrijVeld, VrijVeldZoekFilter> implements
		nl.topicus.eduarte.dao.helpers.VrijVeldDataAccessHelper
{
	public VrijVeldHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(VrijVeldZoekFilter filter)
	{
		Criteria criteria = createCriteria(VrijVeld.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);
		builder.addEquals("actief", filter.getActief());
		builder.addEquals("type", filter.getType());
		builder.addEquals("categorie", filter.getCategorie());

		if (filter.getFilterOpTaxonomie() != null && filter.getFilterOpTaxonomie())
		{
			if (filter.getOpleiding() != null && filter.getOpleiding().getObject() != null)
			{
				Opleiding opl = filter.getOpleiding().getObject();
				Taxonomie taxonomie = opl.getVerbintenisgebied().getTaxonomie();
				builder.addNullOrEquals("taxonomie", taxonomie);
			}
			else
			{
				builder.addIsNull("taxonomie", true);
			}
		}

		List<Criterion> ors = new ArrayList<Criterion>();
		if (filter.getIntakeScherm() != null)
			ors.add(Restrictions.eq("intakescherm", filter.getIntakeScherm()));
		if (filter.getDossierScherm() != null)
			ors.add(Restrictions.eq("dossierscherm", filter.getDossierScherm()));
		if (filter.getUitgebreidZoekenScherm() != null)
			ors.add(Restrictions.eq("uitgebreidzoeken", filter.getUitgebreidZoekenScherm()));
		if (ors.size() > 0)
			builder.addOrs(ors);

		builder.addNotIn("id", filter.getExcludeIds());

		return criteria;
	}

	@Override
	public DetachedCriteria buildCriteria(List< ? extends VrijVeldEntiteit> vrijeVelden,
			Boolean gearchiveerd, Class< ? extends VrijVeldEntiteit> koppelClass,
			String projectieProperty)
	{
		List<Criterion> criterions = new ArrayList<Criterion>();
		for (VrijVeldEntiteit vrijveldEntiteit : vrijeVelden)
		{
			if (vrijveldEntiteit.getCheckWaarde() != null
				|| vrijveldEntiteit.getDateWaarde() != null
				|| !vrijveldEntiteit.getKeuzes().isEmpty()
				|| vrijveldEntiteit.getLongTextWaarde() != null
				|| vrijveldEntiteit.getNumberWaarde() != null
				|| vrijveldEntiteit.getTextWaarde() != null || vrijveldEntiteit.getKeuze() != null)
			{
				Criterion vrijveldEntiteitCrit =
					Restrictions.eq("vrijVeld", vrijveldEntiteit.getVrijVeld());
				Criterion vrijveldWaardeCrit = null;

				switch (vrijveldEntiteit.getVrijVeld().getType())
				{
					case AANKRUISVAK:
					{
						vrijveldWaardeCrit =
							Restrictions.eq("checkWaarde", vrijveldEntiteit.getCheckWaarde());
						break;
					}
					case DATUM:
					{
						vrijveldWaardeCrit =
							Restrictions.eq("dateWaarde", vrijveldEntiteit.getDateWaarde());
						break;
					}
					case KEUZELIJST:
					{
						vrijveldWaardeCrit = Restrictions.eq("keuze", vrijveldEntiteit.getKeuze());
						break;
					}
					case MULTISELECTKEUZELIJST:
					{
						StringBuilder builder = new StringBuilder();
						for (VrijVeldOptieKeuze optie : vrijveldEntiteit.getKeuzes())
						{
							builder.append(optie.getOptie().getId() + ", ");
						}
						if (builder.length() > 0)
							builder.deleteCharAt(builder.lastIndexOf(","));
						vrijveldWaardeCrit =
							Restrictions
								.sqlRestriction("id in(Select entiteit from VRIJVELDOPTIEKEUZE where optie in("
									+ builder.toString() + "))");
						break;
					}
					case LANGETEKST:
					{
						vrijveldWaardeCrit =
							Restrictions.eq("longTextWaarde", vrijveldEntiteit.getLongTextWaarde());
						break;
					}
					case NUMERIEK:
					{
						vrijveldWaardeCrit =
							Restrictions.eq("numberWaarde", vrijveldEntiteit.getNumberWaarde());
						break;
					}
					case TEKST:
					{
						vrijveldWaardeCrit =
							Restrictions.eq("textWaarde", vrijveldEntiteit.getTextWaarde());
						break;
					}
				}

				criterions.add(Restrictions.and(vrijveldEntiteitCrit, vrijveldWaardeCrit));
			}
		}

		if (criterions.size() > 0)
		{
			DetachedCriteria dc = createDetachedCriteria(koppelClass);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("gearchiveerd", gearchiveerd);
			Disjunction disjunction = Restrictions.disjunction();
			for (Criterion criterion : criterions)
				disjunction.add(criterion);
			dc.add(disjunction);
			dc.setProjection(Projections.property(projectieProperty));
			return dc;
		}
		return null;
	}
}
