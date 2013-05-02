package nl.topicus.eduarte.dao.participatie.hibernate;

import java.io.Serializable;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.participatie.helpers.ExterneWaarnemingDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.ExterneWaarneming;
import nl.topicus.eduarte.participatie.zoekfilters.ExterneWaarnemingZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class ExterneWaarnemingHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<ExterneWaarneming, ExterneWaarnemingZoekFilter>
		implements ExterneWaarnemingDataAccessHelper
{
	public ExterneWaarnemingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(ExterneWaarnemingZoekFilter filter)
	{
		Criteria criteria = createCriteria(ExterneWaarneming.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addGreaterOrEquals("datum", filter.getBeginDatum());
		builder.addLessOrEquals("datum", filter.getEindDatum());
		builder.addEquals("verwerkt", filter.isVerwerkt());

		return criteria;
	}

	@Override
	public ExterneWaarneming getById(Serializable id)
	{
		Criteria criteria = createCriteria(ExterneWaarneming.class);
		criteria.add(Restrictions.eq("id", id));
		return cachedTypedUnique(criteria);
	}

	@Override
	public Long getAantalExterneWaarnemingenInWachtrij()
	{
		Criteria criteria = createCriteria(ExterneWaarneming.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("verwerkt", Boolean.FALSE);

		criteria.setProjection(Projections.rowCount());
		return (Long) uncachedUnique(criteria);
	}
}
