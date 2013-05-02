/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.ExterneOrganisatieDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieAdres;
import nl.topicus.eduarte.entities.organisatie.SoortExterneOrganisatie;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class ExterneOrganisatieHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<ExterneOrganisatie, ExterneOrganisatieZoekFilter>
		implements ExterneOrganisatieDataAccessHelper
{
	public ExterneOrganisatieHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(ExterneOrganisatieZoekFilter filter)
	{
		Criteria criteria = createCriteria(ExterneOrganisatie.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.ANYWHERE);
		builder.addILikeCheckWildcard("verkorteNaam", filter.getVerkorteNaam(), MatchMode.ANYWHERE);
		builder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
		builder.addEquals("nogControleren", filter.getNogControleren());
		builder.addEquals("controleResultaat", filter.getControleResultaat());
		filter.addQuickSearchCriteria(builder, "code", "naam");
		builder.addEquals("bpvBedrijf", filter.getBpvBedrijf());
		if (filter.getPlaats() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(ExterneOrganisatieAdres.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);

			dcBuilder.createAlias("adres", "adres");
			dcBuilder.addILikeCheckWildcard("adres.plaats", filter.getPlaats(), MatchMode.START);
			dc.setProjection(Projections.distinct(Projections.property("externeOrganisatie")));
			criteria.add(Subqueries.propertyIn("id", dc));
		}
		if (filter.getAdres() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(ExterneOrganisatieAdres.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);

			dcBuilder.createAlias("adres", "adres");
			dcBuilder.addILikeCheckWildcard("adres.straat", filter.getAdres(), MatchMode.START);
			dc.setProjection(Projections.distinct(Projections.property("externeOrganisatie")));
			criteria.add(Subqueries.propertyIn("id", dc));
		}
		if (filter.getCodeLeerbedrijf() != null || filter.getRelatienummer() != null
			|| filter.getKenniscentrum() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(BPVBedrijfsgegeven.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("codeLeerbedrijf", filter.getCodeLeerbedrijf());
			dcBuilder.addEquals("relatienummer", filter.getRelatienummer());
			dcBuilder.addEquals("kenniscentrum", filter.getKenniscentrum());
			dc.setProjection(Projections.distinct(Projections.property("externeOrganisatie")));
			criteria.add(Subqueries.propertyIn("id", dc));
		}
		if (!filter.isGebruikLandelijkeExterneOrganisaties())
		{
			builder.addEquals("organisatie", EduArteContext.get().getInstelling());
		}
		if (filter.isBijVooropleiding() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(SoortExterneOrganisatie.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("tonenBijVooropleiding", filter.isBijVooropleiding());
			dc.setProjection(Projections.property("id"));

			if (!filter.isGebruikLandelijkeExterneOrganisaties())
			{
				builder.propertyIn("soortExterneOrganisatie", dc);
			}
			else
			{
				builder.addOrs(Subqueries.propertyIn("soortExterneOrganisatie", dc), Restrictions
					.isNull("organisatie"));
			}
		}

		builder.addIn("soortExterneOrganisatie", filter.getSoortExterneOrganisaties());
		if (filter.getHasDebiteurennummer() != null)
			builder.addIsNull("debiteurennummer", !filter.getHasDebiteurennummer());

		return criteria;
	}
}
