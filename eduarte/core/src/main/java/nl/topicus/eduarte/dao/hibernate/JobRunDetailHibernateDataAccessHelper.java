package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.JobRunDetailDataAccessHelper;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.zoekfilters.JobRunDetailZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

public class JobRunDetailHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<JobRunDetail, JobRunDetailZoekFilter<JobRunDetail>>
		implements JobRunDetailDataAccessHelper
{
	public JobRunDetailHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(JobRunDetailZoekFilter<JobRunDetail> filter)
	{
		Criteria criteria = createCriteria(JobRunDetail.class, "jobDetail");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addILikeFixedMatchMode("uitkomst", filter.getUitkomst(), MatchMode.ANYWHERE);
		builder.addEquals("jobRun", filter.getJobRun());

		return criteria;
	}
}
