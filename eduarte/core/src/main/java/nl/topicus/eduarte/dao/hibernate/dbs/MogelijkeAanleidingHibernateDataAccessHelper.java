package nl.topicus.eduarte.dao.hibernate.dbs;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.dbs.MogelijkeAanleidingDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.trajecten.MogelijkeAanleiding;
import nl.topicus.eduarte.zoekfilters.dbs.MogelijkeAanleidingZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

public class MogelijkeAanleidingHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<MogelijkeAanleiding, MogelijkeAanleidingZoekFilter>
		implements MogelijkeAanleidingDataAccessHelper
{
	public MogelijkeAanleidingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(MogelijkeAanleidingZoekFilter filter)
	{
		Criteria criteria = createCriteria(MogelijkeAanleiding.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		filter.addCriteria(builder);

		builder.addEquals("datum", filter.getDatum());
		builder.addEquals("type", filter.getType());
		builder.addILikeCheckWildcard("omschrijving", filter.getOmschrijving(), MatchMode.ANYWHERE);

		filter.addQuickSearchCriteria(builder, "omschrijving");

		return criteria;
	}
}
