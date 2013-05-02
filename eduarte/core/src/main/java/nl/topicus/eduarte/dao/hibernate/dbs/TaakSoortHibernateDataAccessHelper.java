package nl.topicus.eduarte.dao.hibernate.dbs;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.dbs.TaakSoortDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.trajecten.TaakSoort;
import nl.topicus.eduarte.zoekfilters.dbs.TaakSoortZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

public class TaakSoortHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<TaakSoort, TaakSoortZoekFilter> implements
		TaakSoortDataAccessHelper
{
	public TaakSoortHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(TaakSoortZoekFilter filter)
	{
		Criteria criteria = createCriteria(TaakSoort.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		if (!filter.addOrganisatieEenheidLocatieCriteria(criteria))
			return null;

		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.ANYWHERE);
		builder.addEquals("actief", filter.getActief());

		return criteria;
	}
}
