/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.ExterneOrganisatieAdresDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieAdres;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

/**
 * @author vandekamp
 */
public class ExterneOrganisatieAdresHibernateDataAccessHelper extends
		HibernateDataAccessHelper<ExterneOrganisatieAdres> implements
		ExterneOrganisatieAdresDataAccessHelper
{

	public ExterneOrganisatieAdresHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public List<ExterneOrganisatie> getBestaandeExtOrganisatiesOpZelfdeAdres(
			ExterneOrganisatie organisatie, ExterneOrganisatieAdres externeOrganisatieAdres)
	{
		Asserts.assertNotNull("externeOrganisatieAdres", externeOrganisatieAdres);
		Criteria criteria = createCriteria(ExterneOrganisatieAdres.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("externeOrganisatie", "externeOrganisatie");
		builder.createAlias("adres", "adres");
		builder.addNotEquals("externeOrganisatie.id", organisatie.getId());
		// Op deze manier wordt niet dezelfde opgehaald
		builder.addNotEquals("id", externeOrganisatieAdres.getId());
		if (externeOrganisatieAdres.getAdres() != null
			&& externeOrganisatieAdres.getAdres().getLand().equals(Land.getNederland()))
		{
			if (externeOrganisatieAdres.getAdres().getPostcode() != null
				&& externeOrganisatieAdres.getAdres().getHuisnummer() != null)
			{
				builder.addEquals("adres.postcode", externeOrganisatieAdres.getAdres()
					.getPostcode());
				builder.addEquals("adres.huisnummer", externeOrganisatieAdres.getAdres()
					.getHuisnummer());
			}
			else
			{
				return new ArrayList<ExterneOrganisatie>();
			}
		}
		else
		{
			if (externeOrganisatieAdres.getAdres() != null
				&& externeOrganisatieAdres.getAdres().getStraat() != null
				&& externeOrganisatieAdres.getAdres().getPlaats() != null)
			{
				builder.addEquals("adres.straat", externeOrganisatieAdres.getAdres().getStraat());
				builder.addEquals("adres.plaats", externeOrganisatieAdres.getAdres().getPlaats());
			}
			else
			{
				// doen we dit niet dan krijgen we 25k+ organisaties terug!
				// hierdoor gaat de isRequired() in de interface zijn werk doen
				return new ArrayList<ExterneOrganisatie>();
			}
		}
		criteria.setProjection(Projections.property("externeOrganisatie"));
		return cachedList(criteria);
	}
}
