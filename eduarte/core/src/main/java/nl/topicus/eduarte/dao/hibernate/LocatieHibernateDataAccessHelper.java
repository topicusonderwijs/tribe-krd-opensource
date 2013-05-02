package nl.topicus.eduarte.dao.hibernate;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.LocatieDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.zoekfilters.LocatieZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;

public class LocatieHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Locatie, LocatieZoekFilter> implements
		LocatieDataAccessHelper
{
	public LocatieHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(LocatieZoekFilter filter)
	{
		Criteria criteria = createCriteria(Locatie.class);

		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addLessOrEquals("begindatum", filter.getPeildatum());
		builder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
		builder.addEquals("naam", filter.getNaam());
		builder.addILikeCheckWildcard("naam", filter.getNaamZoeken(), MatchMode.ANYWHERE);
		builder.addEquals("afkorting", filter.getAfkorting());
		builder.addILikeCheckWildcard("afkorting", filter.getAfkortingZoeken(), MatchMode.ANYWHERE);

		filter.addQuickSearchCriteria(builder, "afkorting", "naam");

		return criteria;
	}

	@Override
	public List<Locatie> list(Date peildatum)
	{
		Criteria criteria = createCriteria(Locatie.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addLessOrEquals("begindatum", peildatum);
		builder.addGreaterOrEquals("einddatumNotNull", peildatum);
		criteria.addOrder(Order.asc("naam"));

		return cachedTypedList(criteria);
	}

	@Override
	public Locatie get(String afkorting)
	{
		Asserts.assertNotNull("afkorting", afkorting);
		Criteria criteria = createCriteria(Locatie.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("afkorting", afkorting);
		return cachedTypedUnique(criteria);
	}

	@Override
	public Locatie get(Long id)
	{
		return get(Locatie.class, id);
	}
}
