/*
 * $Id: ExtendableDataViewDataAccessHelper.java,v 1.1 2007/07/24 08:15:23 marrink Exp $
 * $Revision: 1.1 $
 * $Date: 2007/07/24 08:15:23 $
 *
 * ====================================================================
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import java.io.Serializable;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.BatchGroupPropertySettingDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelId;
import nl.topicus.eduarte.dao.helpers.AccountDataAccessHelper;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.settings.GroupPropertySetting;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * @author loite
 */
public class GroupPropertySettingHibernateDataAccessHelper extends
		HibernateDataAccessHelper<GroupPropertySetting> implements
		BatchGroupPropertySettingDataAccessHelper<GroupPropertySetting>
{

	/**
	 * @param provider
	 */
	public GroupPropertySettingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	private GroupPropertySetting findSetting(CustomDataPanelId panelId, Serializable accountId)
	{
		if (accountId == null)
			return null;

		Criteria criteria = createCriteria(GroupPropertySetting.class);
		criteria.add(Restrictions.eq("panelId", panelId.generateId()));
		criteria.add(Restrictions.eq("account.id", accountId));

		return cachedTypedUnique(criteria);
	}

	@Override
	public String getSetting(CustomDataPanelId panelId, Serializable accountId)
	{
		GroupPropertySetting ret = findSetting(panelId, accountId);
		return ret == null ? null : ret.getProperty();
	}

	@Override
	public void saveGroupPropertySetting(CustomDataPanelId panelId, Serializable accountId,
			String property)
	{
		GroupPropertySetting setting = findSetting(panelId, accountId);
		if (setting == null)
		{
			Account account =
				DataAccessRegistry.getHelper(AccountDataAccessHelper.class).get(Account.class,
					accountId);
			setting = new GroupPropertySetting(account, panelId, property);
		}
		else
		{
			setting.setProperty(property);
		}
		setting.saveOrUpdate();
	}
}
