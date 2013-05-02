package nl.topicus.eduarte.dao.participatie.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.participatie.helpers.LesdagindelingDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.LesdagIndeling;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.participatie.zoekfilters.LesdagindelingZoekFilter;

import org.hibernate.Criteria;

public class LesdagindelingHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<LesdagIndeling, LesdagindelingZoekFilter> implements
		LesdagindelingDataAccessHelper
{

	public LesdagindelingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(LesdagindelingZoekFilter filter)
	{
		Criteria criteria = createCriteria(LesdagIndeling.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		if (filter.getCode() != null)
			builder.addEquals("code", filter.getCode());

		if (filter.getCode() != null)
			builder.addEquals("dag", filter.getDag());

		if (filter.getCode() != null)
			builder.addEquals("lesweekIndeling", filter.getLesweekIndeling());

		return criteria;
	}

	@Override
	public LesdagIndeling getlesdagIndeling(LesdagindelingZoekFilter abstractFilter)
	{
		Criteria criteria = createCriteria(abstractFilter);
		return (LesdagIndeling) uncachedUnique(criteria);
	}

	@Override
	public List<LesdagIndeling> list()
	{
		Criteria criteria = createCriteria(LesweekIndeling.class);
		return cachedTypedList(criteria);
	}

	@Override
	public List<LesdagIndeling> getLesdagindeling(LesweekIndeling lesweekIndeling)
	{
		LesdagindelingZoekFilter abstractFilter = new LesdagindelingZoekFilter();
		abstractFilter.setLesweekIndeling(lesweekIndeling);
		Criteria criteria = createCriteria(abstractFilter);
		return cachedTypedList(criteria);
	}

	@Override
	public LesdagIndeling getLesdagOpDag(LesweekIndeling lesweekIndeling, String weekDag)
	{
		LesdagindelingZoekFilter abstractFilter = new LesdagindelingZoekFilter();
		abstractFilter.setLesweekIndeling(lesweekIndeling);
		abstractFilter.setDag(weekDag);
		Criteria criteria = createCriteria(abstractFilter);
		return cachedTypedList(criteria).get(0);

	}

}
