package nl.topicus.eduarte.krd.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.eduarte.krd.dao.helpers.MutatieLogVerwerkersDataAccessHelper;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerker;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerkersLog;
import nl.topicus.eduarte.krd.zoekfilters.MutatieLogVerwerkersZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class MutatieLogVerwerkersHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<MutatieLogVerwerker, MutatieLogVerwerkersZoekFilter>
		implements MutatieLogVerwerkersDataAccessHelper
{
	public MutatieLogVerwerkersHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(MutatieLogVerwerkersZoekFilter filter)
	{
		Criteria criteria = createCriteria(MutatieLogVerwerker.class, "v");

		if (filter.isNogNietUitgegeven())
		{
			DetachedCriteria uitgegevenVerwerkers =
				createDetachedCriteria(MutatieLogVerwerkersLog.class, "l");
			uitgegevenVerwerkers.add(Restrictions.eqProperty("l.verwerker", "v.id"));
			uitgegevenVerwerkers.setProjection(Projections.id());
			criteria.add(Subqueries.notExists(uitgegevenVerwerkers));
		}
		return criteria;
	}
}
