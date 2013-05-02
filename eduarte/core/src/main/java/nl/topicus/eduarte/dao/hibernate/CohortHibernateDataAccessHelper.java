package nl.topicus.eduarte.dao.hibernate;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.CohortDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

/**
 * @author loite
 */
public class CohortHibernateDataAccessHelper extends HibernateDataAccessHelper<Cohort> implements
		CohortDataAccessHelper
{

	/**
	 * Constructor
	 * 
	 * @param provider
	 */
	public CohortHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public List<Cohort> list()
	{
		Criteria criteria = createCriteria(Cohort.class);
		criteria.addOrder(Order.asc("begindatum"));

		return cachedTypedList(criteria);
	}

	@Override
	public Cohort getCohortOpDatum(Date peildatum)
	{
		Criteria criteria = createCriteria(Cohort.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addLessOrEquals("begindatum", peildatum);
		builder.addGreaterOrEquals("einddatum", peildatum);

		return cachedTypedUnique(criteria);
	}

	@Override
	public Cohort getVolgendeCohort(Cohort bronCohort)
	{
		Criteria criteria = createCriteria(Cohort.class);
		criteria.addOrder(Order.asc("begindatum"));
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addGreaterThan("begindatum", bronCohort.getBegindatum());
		criteria.setMaxResults(1);
		return cachedTypedUnique(criteria);
	}

	@Override
	public List<Cohort> list(Date beginDatum, Date eindDatum)
	{
		Criteria criteria = createCriteria(Cohort.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		criteria.addOrder(Order.asc("begindatum"));
		builder.addGreaterOrEquals("einddatumNotNull", beginDatum);
		builder.addLessOrEquals("begindatum", eindDatum);
		return cachedTypedList(criteria);
	}
}
