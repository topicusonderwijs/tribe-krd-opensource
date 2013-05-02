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
import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.BatchExtendableDataViewDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.entities.dataview.IExtendableDataViewComponentSetting;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelId;
import nl.topicus.eduarte.dao.helpers.AccountDataAccessHelper;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.settings.ExtendableDataViewComponentSetting;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class ExtendableDataViewHibernateDataAccessHelper extends
		HibernateDataAccessHelper<IExtendableDataViewComponentSetting> implements
		BatchExtendableDataViewDataAccessHelper
{
	public ExtendableDataViewHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	public List<IExtendableDataViewComponentSetting> getSettings(CustomDataPanelId id,
			Serializable accountId)
	{
		if (accountId == null)
			return Collections.emptyList();

		Criteria criteria = createCriteria(ExtendableDataViewComponentSetting.class);
		criteria.add(Restrictions.eq("panelId", id.generateId()));
		if (id.isAccountSpecific())
			criteria.add(Restrictions.eq("account.id", accountId));

		return cachedTypedList(criteria);
	}

	@Override
	public List<IExtendableDataViewComponentSetting> getSettings(Serializable accountId)
	{
		if (accountId == null)
			return Collections.emptyList();

		Criteria criteria = createCriteria(ExtendableDataViewComponentSetting.class);
		criteria.add(Restrictions.eq("account.id", accountId));
		return cachedTypedList(criteria);
	}

	@Override
	public void saveSettings(CustomDataPanelId id, Serializable accountId,
			List<String> visibleColumns, List<String> invisibleColumns)
	{
		for (IExtendableDataViewComponentSetting curOldSetting : getSettings(id, accountId))
			delete(curOldSetting);
		flush();

		Account account =
			DataAccessRegistry.getHelper(AccountDataAccessHelper.class).get(Account.class,
				accountId);
		int position = 0;
		for (String curVisColumn : visibleColumns)
		{
			ExtendableDataViewComponentSetting curSetting =
				new ExtendableDataViewComponentSetting(account, id, curVisColumn, position++, true);
			save(curSetting);
		}
		for (String curInvisColumn : invisibleColumns)
		{
			ExtendableDataViewComponentSetting curSetting =
				new ExtendableDataViewComponentSetting(account, id, curInvisColumn, position++,
					false);
			save(curSetting);
		}
	}

	@Override
	public void deleteSettings(CustomDataPanelId id, Serializable accountId)
	{
		for (IExtendableDataViewComponentSetting curOldSetting : getSettings(id, accountId))
			delete(curOldSetting);
	}
}
