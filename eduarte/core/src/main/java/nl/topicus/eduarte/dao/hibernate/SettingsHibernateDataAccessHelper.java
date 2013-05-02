/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.settings.AccountSetting;
import nl.topicus.eduarte.entities.settings.OrganisatieEenheidSetting;
import nl.topicus.eduarte.entities.settings.OrganisatieSetting;

import org.hibernate.criterion.Restrictions;

/**
 * Helper voor organisatie settings.
 * 
 * @author marrink
 */
public class SettingsHibernateDataAccessHelper extends
		HibernateDataAccessHelper<OrganisatieSetting< ? >> implements SettingsDataAccessHelper
{
	public SettingsHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public List<OrganisatieSetting< ? >> getSettings()
	{
		return cachedTypedList(createCriteria(OrganisatieSetting.class));
	}

	@Override
	public List<OrganisatieEenheidSetting< ? >> getSettings(OrganisatieEenheid eenheid)
	{
		return cachedList(createCriteria(OrganisatieEenheidSetting.class).add(
			Restrictions.eq("organisatieEenheid", eenheid)));
	}

	@Override
	public List<AccountSetting< ? >> getSettings(Account account)
	{
		return cachedList(createCriteria(AccountSetting.class).add(
			Restrictions.eq("account", account)));
	}

	@Override
	public <S extends OrganisatieSetting< ? >> S getDefaultSetting(Class<S> clazz, Object... args)
	{
		return ReflectionUtil.invokeConstructor(clazz, args);
	}

	@Override
	public <S extends OrganisatieSetting< ? >> S getSetting(Class<S> clazz)
	{
		S setting = this.<S> cachedUnique(createCriteria(clazz));
		return setting == null ? getDefaultSetting(clazz) : setting;
	}

	@Override
	public <S extends OrganisatieEenheidSetting< ? >> S getSetting(Class<S> clazz,
			OrganisatieEenheid eenheid)
	{
		S setting =
			this.<S> cachedUnique(createCriteria(clazz).add(
				Restrictions.eq("organisatieEenheid", eenheid)));
		return setting == null ? getDefaultSetting(clazz, eenheid) : setting;
	}

	@Override
	public <S extends AccountSetting< ? >> S getSetting(Class<S> clazz, Account account)
	{
		S setting =
			this.<S> cachedUnique(createCriteria(clazz).add(Restrictions.eq("account", account)));
		return setting == null ? getDefaultSetting(clazz, account) : setting;
	}
}
