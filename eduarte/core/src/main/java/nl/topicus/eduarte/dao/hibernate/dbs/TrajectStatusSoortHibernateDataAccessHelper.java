package nl.topicus.eduarte.dao.hibernate.dbs;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.dbs.TrajectStatusSoortDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.trajecten.ToegestaneStatusSoort;
import nl.topicus.eduarte.entities.dbs.trajecten.TrajectStatusSoort;
import nl.topicus.eduarte.zoekfilters.dbs.TrajectStatusSoortZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;

public class TrajectStatusSoortHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<TrajectStatusSoort, TrajectStatusSoortZoekFilter>
		implements TrajectStatusSoortDataAccessHelper
{
	public TrajectStatusSoortHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(TrajectStatusSoortZoekFilter filter)
	{
		Criteria criteria = createCriteria(TrajectStatusSoort.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		if (filter.getTrajectSoort() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(ToegestaneStatusSoort.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("trajectsoort", filter.getTrajectSoort());
			dc.setProjection(Projections.property("trajectStatusSoort"));
			builder.propertyIn("id", dc);
		}

		builder.addEquals("actief", filter.getActief());

		return criteria;
	}
}
