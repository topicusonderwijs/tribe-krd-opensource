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
import nl.topicus.eduarte.dao.helpers.PersoonExterneOrganisatieDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieAdres;
import nl.topicus.eduarte.entities.personen.PersoonExterneOrganisatie;
import nl.topicus.eduarte.zoekfilters.PersoonExterneOrganisatieZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

public class PersoonExterneOrganisatieHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<PersoonExterneOrganisatie, PersoonExterneOrganisatieZoekFilter>
		implements PersoonExterneOrganisatieDataAccessHelper
{
	public PersoonExterneOrganisatieHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(PersoonExterneOrganisatieZoekFilter filter)
	{
		Criteria criteria =
			createCriteria(PersoonExterneOrganisatie.class, "persoonExterneOrganisatie");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("persoonExterneOrganisatie.relatie", "externeOrganisatie");
		addCriteria(builder, filter, "persoonExterneOrganisatie", "externeOrganisatie", "deelnemer");

		return criteria;
	}

	@Override
	public void addCriteria(CriteriaBuilder builder, PersoonExterneOrganisatieZoekFilter filter,
			String persoonExterneOrganisatieAlias, String externeOrganisatieAlias,
			String persoonAlias)
	{
		builder.addILikeCheckWildcard(externeOrganisatieAlias + ".naam", filter.getNaam(),
			MatchMode.START);
		builder.addILikeCheckWildcard(externeOrganisatieAlias + ".afkorting",
			filter.getAfkorting(), MatchMode.START);
		builder.addNotEquals(persoonAlias + ".id", filter.getPersoonIdNot());
		builder.addNotIn(externeOrganisatieAlias + ".id", filter.getIdNotIn());
		builder.addIn(externeOrganisatieAlias + ".soortExterneOrganisatie",
			filter.getSoortExterneOrganisaties());

		if (filter.heeftAdresCriteria())
		{
			DetachedCriteria dc = createDetachedCriteria(ExterneOrganisatieAdres.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);

			dcBuilder.createAlias("adres", "adres");
			dcBuilder
				.addILikeCheckWildcard("adres.postcode", filter.getPostcode(), MatchMode.START);
			dcBuilder.addILikeCheckWildcard("adres.plaats", filter.getPlaats(), MatchMode.START);
			dcBuilder.addEquals("relatieSoort", filter.getRelatieSoort());
			dcBuilder.addEquals("relatie", filter.getRelatie());
			dc.setProjection(Projections.property("relatie"));
			builder.propertyIn(externeOrganisatieAlias, dc);
		}

	}

	@Override
	public PersoonExterneOrganisatie get(Long id)
	{
		return get(PersoonExterneOrganisatie.class, id);
	}
}
