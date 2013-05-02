package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductAanbodDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAanbod;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductAanbodZoekFilter;

import org.hibernate.Criteria;

public class OnderwijsproductAanbodHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<OnderwijsproductAanbod, OnderwijsproductAanbodZoekFilter>
		implements OnderwijsproductAanbodDataAccessHelper
{
	public OnderwijsproductAanbodHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(OnderwijsproductAanbodZoekFilter filter)
	{
		Criteria criteria = createCriteria(OnderwijsproductAanbod.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("onderwijsproduct", filter.getOnderwijsproduct());

		return criteria;
	}
}
