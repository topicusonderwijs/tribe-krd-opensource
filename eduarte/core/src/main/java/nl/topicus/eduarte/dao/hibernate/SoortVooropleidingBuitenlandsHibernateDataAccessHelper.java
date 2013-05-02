package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.SoortVooropleidingBuitenlandsDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleidingBuitenlands;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.zoekfilters.SoortVooropleidingBuitenlandsZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class SoortVooropleidingBuitenlandsHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<SoortVooropleidingBuitenlands, SoortVooropleidingBuitenlandsZoekFilter>
		implements SoortVooropleidingBuitenlandsDataAccessHelper
{

	public SoortVooropleidingBuitenlandsHibernateDataAccessHelper(
			HibernateSessionProvider provider, QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(SoortVooropleidingBuitenlandsZoekFilter filter)
	{
		Criteria criteria = createCriteria(SoortVooropleidingBuitenlands.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.ANYWHERE);
		builder.addEquals("code", filter.getCode());
		builder.addEquals("actief", filter.getActief());
		builder.addEquals("land", filter.getLand());

		// filter.addQuickSearchCriteria(builder, "code", "naam");

		return criteria;
	}

	@Override
	public SoortVooropleidingBuitenlands get(Land land, String code)
	{
		Asserts.assertNotEmpty("land", land);
		Asserts.assertNotEmpty("code", code);
		Criteria criteria = createCriteria(SoortVooropleidingBuitenlands.class);
		criteria.add(Restrictions.eq("code", code));
		criteria.add(Restrictions.eq("land", land));

		return cachedTypedUnique(criteria);
	}

}
