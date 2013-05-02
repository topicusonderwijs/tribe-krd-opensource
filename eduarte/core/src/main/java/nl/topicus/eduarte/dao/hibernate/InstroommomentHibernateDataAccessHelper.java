package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.InstroommomentDataAccessHelper;
import nl.topicus.eduarte.entities.hogeronderwijs.Instroommoment;
import nl.topicus.eduarte.zoekfilters.InstroommomentZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class InstroommomentHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Instroommoment, InstroommomentZoekFilter> implements
		InstroommomentDataAccessHelper
{
	public InstroommomentHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(InstroommomentZoekFilter filter)
	{
		Criteria criteria = createCriteria(Instroommoment.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addILikeCheckWildcard("code", filter.getCode(), MatchMode.START);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);
		builder.addEquals("actief", filter.getActief());

		return criteria;
	}

	@Override
	public Instroommoment get(String code)
	{
		Asserts.assertNotEmpty("code", code);
		Criteria criteria = createCriteria(Instroommoment.class);
		criteria.add(Restrictions.eq("code", code));

		return cachedTypedUnique(criteria);
	}
}
