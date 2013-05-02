package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.VerblijfsvergunningDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Verblijfsvergunning;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class VerblijfsvergunningHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<Verblijfsvergunning, LandelijkCodeNaamZoekFilter<Verblijfsvergunning>>
		implements VerblijfsvergunningDataAccessHelper
{

	public VerblijfsvergunningHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(LandelijkCodeNaamZoekFilter<Verblijfsvergunning> filter)
	{
		Criteria criteria = createCriteria(Verblijfsvergunning.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.ANYWHERE);
		builder.addEquals("code", filter.getCode());

		filter.addQuickSearchCriteria(builder, "code", "naam");

		return criteria;
	}

	@Override
	public Verblijfsvergunning get(String code)
	{
		Asserts.assertNotEmpty("code", code);
		Criteria criteria = createCriteria(Verblijfsvergunning.class);
		criteria.add(Restrictions.eq("code", code));
		return cachedTypedUnique(criteria);
	}

}
