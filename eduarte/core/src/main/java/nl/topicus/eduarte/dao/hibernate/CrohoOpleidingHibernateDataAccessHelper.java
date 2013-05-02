package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.dao.helpers.CrohoOpleidingDataAccessHelper;
import nl.topicus.eduarte.entities.taxonomie.ho.CrohoOpleiding;

import org.hibernate.Criteria;

public class CrohoOpleidingHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<CrohoOpleiding, DetachableZoekFilter<CrohoOpleiding>>
		implements CrohoOpleidingDataAccessHelper
{
	public CrohoOpleidingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(DetachableZoekFilter<CrohoOpleiding> filter)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CrohoOpleiding getCrohoOpleiding(String isatCode)
	{
		Asserts.assertNotNull("isatCode", isatCode);

		Criteria criteria = createCriteria(CrohoOpleiding.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("externeCode", isatCode);
		return cachedUnique(criteria);
	}
}
