package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.ProvincieDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Provincie;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class ProvincieHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Provincie, LandelijkCodeNaamZoekFilter<Provincie>>
		implements ProvincieDataAccessHelper
{
	public ProvincieHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public Provincie get(String code)
	{
		Asserts.assertNotEmpty("code", code);
		Criteria criteria = createCriteria(Provincie.class);
		criteria.add(Restrictions.eq("code", code));

		return cachedTypedUnique(criteria);
	}

	@Override
	protected Criteria createCriteria(LandelijkCodeNaamZoekFilter<Provincie> filter)
	{
		Criteria criteria = createCriteria(Provincie.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.ANYWHERE);
		builder.addEquals("code", filter.getCode());

		return criteria;
	}

	@Override
	public Provincie getByNaam(String naam)
	{
		Criteria criteria = createCriteria(Provincie.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("naam", naam);

		return cachedTypedUnique(criteria);
	}
}
