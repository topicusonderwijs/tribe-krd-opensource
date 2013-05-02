package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.RegioDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Regio;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * @author loite
 */
public class RegioHibernateDataAccessHelper extends HibernateDataAccessHelper<Regio> implements
		RegioDataAccessHelper
{

	/**
	 * Constructor
	 * 
	 * @param provider
	 */
	public RegioHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public Regio get(String code)
	{
		Asserts.assertNotEmpty("code", code);
		Criteria criteria = createCriteria(Regio.class);
		criteria.add(Restrictions.eq("code", code));

		return cachedTypedUnique(criteria);
	}

}
