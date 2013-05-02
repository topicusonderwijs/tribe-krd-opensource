package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.InstellingsLogoDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.InstellingsLogo;

import org.hibernate.Criteria;

/**
 * @author vandekamp
 */
public class InstellingsLogoHibernateDataAccessHelper extends
		HibernateDataAccessHelper<InstellingsLogo> implements InstellingsLogoDataAccessHelper
{

	public InstellingsLogoHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public InstellingsLogo getInstellingsLogo()
	{
		Criteria criteria = createCriteria(InstellingsLogo.class);
		return cachedUnique(criteria);
	}

}
