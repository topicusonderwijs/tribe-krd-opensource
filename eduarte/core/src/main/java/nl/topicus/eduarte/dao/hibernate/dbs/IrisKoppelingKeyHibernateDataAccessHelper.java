package nl.topicus.eduarte.dao.hibernate.dbs;

import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.dbs.IrisKoppelingKeyDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.incident.IrisKoppelingKey;

import org.hibernate.Criteria;

public class IrisKoppelingKeyHibernateDataAccessHelper extends
		HibernateDataAccessHelper<IrisKoppelingKey> implements IrisKoppelingKeyDataAccessHelper
{
	public IrisKoppelingKeyHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public IrisKoppelingKey getKey()
	{
		Criteria criteria = createCriteria(IrisKoppelingKey.class);
		return cachedUnique(criteria);
	}

}
