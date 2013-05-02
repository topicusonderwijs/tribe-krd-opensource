/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.ExterneOrganisatieContactPersoonRolDataAccessHelper;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoonRol;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieContactPersoonRolZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class ExterneOrganisatieContactPersoonRolHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<ExterneOrganisatieContactPersoonRol, ExterneOrganisatieContactPersoonRolZoekFilter>
		implements ExterneOrganisatieContactPersoonRolDataAccessHelper
{
	public ExterneOrganisatieContactPersoonRolHibernateDataAccessHelper(
			HibernateSessionProvider provider, QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(ExterneOrganisatieContactPersoonRolZoekFilter filter)
	{
		Criteria criteria = createCriteria(ExterneOrganisatieContactPersoonRol.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);
		builder.addEquals("praktijkopleiderBPV", filter.getPraktijkopleiderBPV());
		builder.addEquals("contactpersoonBPV", filter.getContactpersoonBPV());
		builder.addEquals("actief", filter.getActief());

		return criteria;
	}

	@Override
	public ExterneOrganisatieContactPersoonRol get(String naam)
	{
		Asserts.assertNotEmpty("naam", naam);
		Criteria criteria = createCriteria(ExterneOrganisatieContactPersoonRol.class);
		criteria.add(Restrictions.eq("naam", naam));

		return cachedTypedUnique(criteria);
	}
}
