/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.EncryptionProvider;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.dao.helpers.OrganisatieDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.entities.organisatie.ModuleAfname;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.entities.settings.APIKeySetting;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * @author marrink
 */
public class OrganisatieHibernateDataAccessHelper extends HibernateDataAccessHelper<Organisatie>
		implements OrganisatieDataAccessHelper
{

	/**
	 * @param provider
	 */
	public OrganisatieHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public List< ? extends Organisatie> getInlogDomeinen()
	{
		return cachedTypedList(createCriteria(Organisatie.class).add(
			Restrictions.eq("actief", true)).addOrder(Order.asc("naam")));
	}

	@Override
	public Organisatie getOrganisatie(String naam)
	{
		if (naam != null)
			return cachedTypedUnique(createCriteria(Organisatie.class).add(
				Restrictions.eq("naam", naam)));
		return null;
	}

	@Override
	public Organisatie getOrganisatieByAPIKey(String apiKey)
	{
		if (apiKey != null)
		{
			// Deze methode wordt ook aangeroepen door webservices. Er is dan geen
			// organisatie beschikbaar. Vandaar createUninterceptedCriteria().
			return cachedTypedUnique(createUninterceptedCriteria(APIKeySetting.class).add(
				Restrictions.eq("value.apiKey", apiKey)).setProjection(
				Projections.property("organisatie")));
		}
		return null;
	}

	@Override
	public boolean isModuleAfgenomen(EduArteModuleKey key, EncryptionProvider encryptionProvider)
	{
		Asserts.assertNotEmpty("key", key);
		Criteria criteria = createCriteria(ModuleAfname.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("moduleName", key.getName());
		builder.addEquals("actief", Boolean.TRUE);
		builder.addEquals("organisatie", EduArteContext.get().getOrganisatie());
		ModuleAfname afname = (ModuleAfname) unique(criteria, true);
		if (afname != null)
		{
			return afname.isChecksumValid(encryptionProvider);
		}
		return false;
	}

	@Override
	public List<Instelling> getInstellingen()
	{
		return cachedList(createCriteria(Instelling.class));
	}
}
