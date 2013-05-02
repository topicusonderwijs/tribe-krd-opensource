package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.TaalTypeDataAccessHelper;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.TaalType;
import nl.topicus.eduarte.zoekfilters.TaalTypeZoekFilter;

import org.hibernate.Criteria;

public class TaalTypeHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<TaalType, TaalTypeZoekFilter> implements
		TaalTypeDataAccessHelper
{
	public TaalTypeHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(TaalTypeZoekFilter filter)
	{
		Criteria criteria = createCriteria(TaalType.class);

		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("titel", filter.getTitel());
		builder.addNotIn("id", filter.getTaalTypesUitgezonderd());
		return criteria;
	}
}
