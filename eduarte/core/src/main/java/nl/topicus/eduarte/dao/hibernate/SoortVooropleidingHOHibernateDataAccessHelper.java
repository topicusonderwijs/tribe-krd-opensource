package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.SoortVooropleidingHODataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleidingHO;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class SoortVooropleidingHOHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<SoortVooropleidingHO, LandelijkCodeNaamZoekFilter<SoortVooropleidingHO>>
		implements SoortVooropleidingHODataAccessHelper
{
	public SoortVooropleidingHOHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(LandelijkCodeNaamZoekFilter<SoortVooropleidingHO> filter)
	{
		Criteria criteria = createCriteria(SoortVooropleidingHO.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.ANYWHERE);
		builder.addEquals("code", filter.getCode());

		filter.addQuickSearchCriteria(builder, "code", "naam");

		return criteria;
	}

	@Override
	public SoortVooropleidingHO get(String code)
	{
		Asserts.assertNotEmpty("code", code);
		Criteria criteria = createCriteria(SoortVooropleidingHO.class);
		criteria.add(Restrictions.eq("code", code));
		return cachedTypedUnique(criteria);
	}
}
