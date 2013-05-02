package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.SSLCertificaatDataAccessHelper;
import nl.topicus.eduarte.entities.ibgverzuimloket.SSLCertificaat;

import org.hibernate.Criteria;

public class SSLCertificaatHibernateDataAccessHelper extends
		HibernateDataAccessHelper<SSLCertificaat> implements SSLCertificaatDataAccessHelper
{

	public SSLCertificaatHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public SSLCertificaat findCertificaatOfInstelling()
	{
		Criteria criteria = createCriteria(SSLCertificaat.class);
		return cachedUnique(criteria);
	}
}
