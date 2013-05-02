package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.SoortOnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.SoortOnderwijsproduct;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * @author vandkamp
 */
public class SoortOnderwijsproductHibernateDataAccessHelper extends
		HibernateDataAccessHelper<SoortOnderwijsproduct> implements
		SoortOnderwijsproductDataAccessHelper
{

	/**
	 * Constructor
	 * 
	 * @param provider
	 */
	public SoortOnderwijsproductHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public SoortOnderwijsproduct get(String code)
	{
		Asserts.assertNotEmpty("code", code);
		Criteria criteria = createCriteria(SoortOnderwijsproduct.class);
		criteria.add(Restrictions.eq("code", code));

		return cachedTypedUnique(criteria);
	}

	@Override
	public List<SoortOnderwijsproduct> list()
	{
		Criteria criteria = createCriteria(SoortOnderwijsproduct.class);
		criteria.add(Restrictions.eq("actief", Boolean.TRUE));
		criteria.addOrder(Order.asc("naam"));

		return cachedTypedList(criteria);
	}

}
