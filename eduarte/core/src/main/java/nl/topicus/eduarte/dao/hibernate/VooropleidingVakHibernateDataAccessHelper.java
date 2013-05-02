package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.VooropleidingVakDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.VooropleidingVak;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class VooropleidingVakHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<VooropleidingVak, LandelijkCodeNaamZoekFilter<VooropleidingVak>>
		implements VooropleidingVakDataAccessHelper
{
	public VooropleidingVakHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(LandelijkCodeNaamZoekFilter<VooropleidingVak> filter)
	{
		Criteria criteria = createCriteria(VooropleidingVak.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.ANYWHERE);
		builder.addEquals("code", filter.getCode());

		filter.addQuickSearchCriteria(builder, "code", "naam");

		return criteria;
	}

	@Override
	public VooropleidingVak get(String code)
	{
		Asserts.assertNotEmpty("code", code);
		Criteria criteria = createCriteria(VooropleidingVak.class);
		criteria.add(Restrictions.eq("code", code));

		return cachedTypedUnique(criteria);
	}
}
