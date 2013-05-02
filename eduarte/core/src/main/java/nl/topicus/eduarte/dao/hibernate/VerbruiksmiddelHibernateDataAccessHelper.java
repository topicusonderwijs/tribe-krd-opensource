package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.VerbruiksmiddelDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductVerbruiksmiddel;
import nl.topicus.eduarte.entities.onderwijsproduct.Verbruiksmiddel;
import nl.topicus.eduarte.zoekfilters.VerbruiksmiddelZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class VerbruiksmiddelHibernateDataAccessHelper
		extends
		CodeNaamActiefLandelijkOfInstellingEntiteitHibernateDataAccessHelper<Verbruiksmiddel, VerbruiksmiddelZoekFilter>
		implements VerbruiksmiddelDataAccessHelper
{
	public VerbruiksmiddelHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(VerbruiksmiddelZoekFilter filter)
	{
		Criteria criteria = createCriteria(Verbruiksmiddel.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("code", filter.getCode());
		builder.addEquals("naam", filter.getNaam());
		builder.addEquals("actief", filter.getActief());
		if (filter.getOnderwijsproduct() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(OnderwijsproductVerbruiksmiddel.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("onderwijsproduct", filter.getOnderwijsproduct());
			dc.setProjection(Projections.property("verbruiksmiddel"));
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
	public Verbruiksmiddel get(String code)
	{
		Asserts.assertNotEmpty("code", code);
		Criteria criteria = createCriteria(Verbruiksmiddel.class);
		criteria.add(Restrictions.eq("code", code));

		return cachedTypedUnique(criteria);
	}

	@Override
	public List<Verbruiksmiddel> list()
	{
		Criteria criteria = createCriteria(Verbruiksmiddel.class);
		criteria.add(Restrictions.eq("actief", Boolean.TRUE));
		criteria.addOrder(Order.asc("naam"));

		return cachedTypedList(criteria);
	}
}
