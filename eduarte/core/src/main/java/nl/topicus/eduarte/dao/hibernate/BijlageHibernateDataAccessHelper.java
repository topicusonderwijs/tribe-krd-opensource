package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.eduarte.dao.helpers.BijlageDataAccessHelper;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductBijlage;
import nl.topicus.eduarte.entities.personen.DeelnemerBijlage;
import nl.topicus.eduarte.zoekfilters.BijlageZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Subqueries;

public class BijlageHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Bijlage, BijlageZoekFilter> implements
		BijlageDataAccessHelper
{
	public BijlageHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(BijlageZoekFilter filter)
	{
		Criteria criteria = createCriteria(Bijlage.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("omschrijving", filter.getOmschrijving());
		builder.addEquals("typeBijlage", filter.getTypeBijlage());

		if (filter.getOnderwijsproduct() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(OnderwijsproductBijlage.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("onderwijsproduct", filter.getOnderwijsproduct());
			dc.setProjection(Projections.property("bijlage"));
			criteria.add(Subqueries.propertyIn("id", dc));
		}

		return criteria;
	}

	@Override
	public List<DeelnemerBijlage> getDeelnemerBijlagesZonderDeelnemer()
	{
		Criteria criteria = createCriteria(DeelnemerBijlage.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addIsNull("deelnemer", true);
		return list(criteria, false);
	}
}
