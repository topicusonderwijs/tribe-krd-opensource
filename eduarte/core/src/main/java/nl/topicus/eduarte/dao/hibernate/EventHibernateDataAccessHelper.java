package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.EventDataAccessHelper;
import nl.topicus.eduarte.entities.signalering.Event;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;

public class EventHibernateDataAccessHelper<T extends Event> extends HibernateDataAccessHelper<T>
		implements EventDataAccessHelper<T>
{

	public EventHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public boolean isEerderOpgetreden(T event)
	{
		Criteria criteria = createCriteria(Hibernate.getClass(event));
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("hash", event.getHash());
		return cachedTypedUnique(criteria) != null;
	}
}
