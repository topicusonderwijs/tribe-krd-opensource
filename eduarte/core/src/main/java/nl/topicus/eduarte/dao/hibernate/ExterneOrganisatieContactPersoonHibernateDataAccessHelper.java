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
import nl.topicus.eduarte.dao.helpers.ExterneOrganisatieContactPersoonDataAccessHelper;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.entities.security.authentication.ExterneOrganisatieContactPersoonAccount;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieContactPersoonZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Subqueries;

public class ExterneOrganisatieContactPersoonHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<ExterneOrganisatieContactPersoon, ExterneOrganisatieContactPersoonZoekFilter>
		implements ExterneOrganisatieContactPersoonDataAccessHelper
{
	public ExterneOrganisatieContactPersoonHibernateDataAccessHelper(
			HibernateSessionProvider provider, QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(ExterneOrganisatieContactPersoonZoekFilter filter)
	{
		Criteria criteria = createCriteria(ExterneOrganisatieContactPersoon.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.createAlias("rol", "rol");
		builder.addEquals("rol.praktijkopleiderBPV", filter.getPraktijkopleiderBPV());
		builder.addEquals("rol.contactpersoonBPV", filter.getContactpersoonBPV());
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);
		builder.addEquals("externeOrganisatie", filter.getExterneOrganisatie());
		if (filter.getHeeftAccount() != null)
		{
			DetachedCriteria dcAccount =
				createDetachedCriteria(ExterneOrganisatieContactPersoonAccount.class);
			dcAccount.setProjection(Projections.property("externeOrganisatieContactPersoon"));
			DetachedCriteriaBuilder dcAccountBuilder = new DetachedCriteriaBuilder(dcAccount);
			dcAccountBuilder.addIsNull("externeOrganisatieContactPersoon", false);

			if (filter.getHeeftAccount())
				criteria.add(Subqueries.propertyIn("id", dcAccount));
			else
				criteria.add(Subqueries.propertyNotIn("id", dcAccount));
			filter.setResultCacheable(false);
		}
		filter.addQuickSearchCriteria(builder, "naam");

		return criteria;
	}
}
