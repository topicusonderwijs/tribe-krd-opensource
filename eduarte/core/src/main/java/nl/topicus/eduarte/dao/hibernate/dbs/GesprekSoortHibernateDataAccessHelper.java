package nl.topicus.eduarte.dao.hibernate.dbs;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.dbs.GesprekSoortDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.trajecten.GesprekSoort;
import nl.topicus.eduarte.zoekfilters.dbs.GesprekSoortZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

public class GesprekSoortHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<GesprekSoort, GesprekSoortZoekFilter> implements
		GesprekSoortDataAccessHelper
{
	public GesprekSoortHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(GesprekSoortZoekFilter filter)
	{
		Criteria criteria = createCriteria(GesprekSoort.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		if (!filter.addOrganisatieEenheidLocatieCriteria(criteria))
			return null;

		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.ANYWHERE);
		builder.addEquals("actief", filter.getActief());

		return criteria;
	}
}
