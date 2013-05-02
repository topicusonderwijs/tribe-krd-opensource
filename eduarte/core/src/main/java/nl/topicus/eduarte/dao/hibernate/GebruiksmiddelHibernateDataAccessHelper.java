package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.GebruiksmiddelDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Gebruiksmiddel;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductGebruiksmiddel;
import nl.topicus.eduarte.zoekfilters.GebruiksmiddelZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class GebruiksmiddelHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Gebruiksmiddel, GebruiksmiddelZoekFilter> implements
		GebruiksmiddelDataAccessHelper
{
	public GebruiksmiddelHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(GebruiksmiddelZoekFilter filter)
	{
		Criteria criteria = createCriteria(Gebruiksmiddel.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("code", filter.getCode());
		builder.addEquals("naam", filter.getNaam());
		builder.addEquals("actief", filter.getActief());
		if (filter.getOnderwijsproduct() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(OnderwijsproductGebruiksmiddel.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("onderwijsproduct", filter.getOnderwijsproduct());
			dc.setProjection(Projections.property("gebruiksmiddel"));
			if (filter.isAlleenOngekoppeld())
				criteria.add(Subqueries.propertyNotIn("id", dc));
			else
				criteria.add(Subqueries.propertyIn("id", dc));
			// zo'n simpele query, en dan gaat de hibernate cache al de mist in....
			filter.setResultCacheable(false);
		}

		return criteria;
	}

	@Override
	public List<Gebruiksmiddel> list()
	{
		Criteria criteria = createCriteria(Gebruiksmiddel.class);
		criteria.add(Restrictions.eq("actief", Boolean.TRUE));
		criteria.addOrder(Order.asc("naam"));

		return cachedTypedList(criteria);
	}

}
