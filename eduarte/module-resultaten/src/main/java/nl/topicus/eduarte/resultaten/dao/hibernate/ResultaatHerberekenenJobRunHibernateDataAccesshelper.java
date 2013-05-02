package nl.topicus.eduarte.resultaten.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.hibernate.JobRunHibernateDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.resultaten.dao.helpers.ResultaatHerberekenenJobRunDataAccesshelper;
import nl.topicus.eduarte.resultaten.entities.ResultatenHerberekenenJobRun;
import nl.topicus.eduarte.resultaten.zoekfilters.ResultaatHerberekenenJobRunZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Subqueries;

public class ResultaatHerberekenenJobRunHibernateDataAccesshelper
		extends
		JobRunHibernateDataAccessHelper<ResultatenHerberekenenJobRun, ResultaatHerberekenenJobRunZoekFilter>
		implements ResultaatHerberekenenJobRunDataAccesshelper
{
	public ResultaatHerberekenenJobRunHibernateDataAccesshelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(ResultaatHerberekenenJobRunZoekFilter filter)
	{
		Criteria criteria = super.createCriteria(filter);

		if (filter.getOnderwijsproduct() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(Resultaatstructuur.class);
			DetachedCriteriaBuilder builder = new DetachedCriteriaBuilder(dc);
			builder.addEquals("onderwijsproduct", filter.getOnderwijsproduct());
			dc.setProjection(Projections.property("id"));
			criteria.add(Subqueries.propertyIn("resultaatstructuurId", dc));
		}

		return criteria;
	}
}
