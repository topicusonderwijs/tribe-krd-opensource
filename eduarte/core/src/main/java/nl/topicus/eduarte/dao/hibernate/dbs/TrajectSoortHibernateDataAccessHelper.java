package nl.topicus.eduarte.dao.hibernate.dbs;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.dbs.TrajectSoortDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.trajecten.TrajectSoort;
import nl.topicus.eduarte.zoekfilters.dbs.TrajectSoortZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

public class TrajectSoortHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<TrajectSoort, TrajectSoortZoekFilter> implements
		TrajectSoortDataAccessHelper
{
	public TrajectSoortHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(TrajectSoortZoekFilter filter)
	{
		Criteria criteria = createCriteria(TrajectSoort.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		if (!filter.addOrganisatieEenheidLocatieCriteria(criteria))
			return null;

		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.ANYWHERE);
		builder.addEquals("actief", filter.getActief());

		return criteria;
	}
}
