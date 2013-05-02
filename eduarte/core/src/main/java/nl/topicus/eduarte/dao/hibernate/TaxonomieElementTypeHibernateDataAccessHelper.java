package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.TaxonomieElementTypeDataAccessHelper;
import nl.topicus.eduarte.entities.taxonomie.SoortTaxonomieElement;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElementType;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * @author loite
 */
public class TaxonomieElementTypeHibernateDataAccessHelper extends
		HibernateDataAccessHelper<TaxonomieElementType> implements
		TaxonomieElementTypeDataAccessHelper
{

	/**
	 * Constructor
	 * 
	 * @param provider
	 */
	public TaxonomieElementTypeHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public TaxonomieElementType getTaxonomieType()
	{
		Criteria criteria = createCriteria(TaxonomieElementType.class);
		criteria.add(Restrictions.eq("soort", SoortTaxonomieElement.Taxonomie));
		return cachedTypedUnique(criteria);
	}

	@Override
	public List<TaxonomieElementType> list(Taxonomie taxonomie, SoortTaxonomieElement soort)
	{
		Asserts.assertNotNull("taxonomie", taxonomie);
		Criteria criteria = createCriteria(TaxonomieElementType.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("taxonomie", taxonomie);
		builder.addEquals("soort", soort);
		criteria.addOrder(Order.asc("volgnummer"));

		return cachedTypedList(criteria);
	}

	@Override
	public boolean isInGebruik(TaxonomieElementType type)
	{
		Asserts.assertNotNull("type", type);
		Criteria criteria = createCriteria(TaxonomieElementType.class);
		criteria.add(Restrictions.eq("parent", type));
		criteria.setProjection(Projections.rowCount());
		Long count = uncachedUnique(criteria);
		if (count.intValue() > 0)
		{
			return true;
		}
		criteria = createCriteria(TaxonomieElement.class);
		criteria.add(Restrictions.eq("taxonomieElementType", type));
		criteria.setProjection(Projections.rowCount());
		count = uncachedUnique(criteria);
		if (count.intValue() > 0)
		{
			return true;
		}

		return false;
	}

	@Override
	public TaxonomieElementType get(String naam, Taxonomie taxonomie)
	{
		Criteria criteria = createCriteria(TaxonomieElementType.class);
		criteria.add(Restrictions.eq("taxonomie", taxonomie));
		criteria.add(Restrictions.eq("naam", naam));

		return cachedTypedUnique(criteria);
	}
}
