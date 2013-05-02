package nl.topicus.eduarte.dao.hibernate.dbs;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.dbs.IncidentDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.gedrag.Incident;
import nl.topicus.eduarte.entities.dbs.gedrag.IrisIncidentNietTonenInZorgvierkant;
import nl.topicus.eduarte.zoekfilters.dbs.IncidentZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;

public class IncidentHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Incident, IncidentZoekFilter> implements
		IncidentDataAccessHelper
{
	public IncidentHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(IncidentZoekFilter filter)
	{
		Criteria criteria = createCriteria(Incident.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		filter.addCriteria(builder);
		builder.addEquals("deelnemer", filter.getDeelnemer());

		if (filter.getZorgvierkant() != null && filter.getZorgvierkant())
		{
			DetachedCriteria tonenInZorgvierkant =
				createDetachedCriteria(IrisIncidentNietTonenInZorgvierkant.class);
			tonenInZorgvierkant.setProjection(Projections.property("irisIncident"));
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(tonenInZorgvierkant);
			dcBuilder.addEquals("medewerker", filter.getMedewerker());

			builder.propertyNotIn("id", tonenInZorgvierkant);
		}
		return criteria;
	}
}
