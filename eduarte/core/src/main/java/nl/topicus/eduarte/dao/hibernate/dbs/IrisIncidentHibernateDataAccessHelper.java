package nl.topicus.eduarte.dao.hibernate.dbs;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.dbs.IrisIncidentDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.incident.IrisBetrokkene;
import nl.topicus.eduarte.entities.dbs.incident.IrisIncident;
import nl.topicus.eduarte.zoekfilters.dbs.IrisIncidentZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;

public class IrisIncidentHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<IrisIncident, IrisIncidentZoekFilter> implements
		IrisIncidentDataAccessHelper
{
	public IrisIncidentHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(IrisIncidentZoekFilter filter)
	{
		Criteria criteria = createCriteria(IrisIncident.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		if (filter.getDeelnemer() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(IrisBetrokkene.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dc.setProjection(Projections.property("irisIncident"));
			dcBuilder.createAlias("incident", "incident");
			dcBuilder.addEquals("incident.deelnemer", filter.getDeelnemer());
			builder.propertyIn("id", dc);
		}

		builder.addEquals("categorie", filter.getCategorie());

		if (filter.getIrisCategorie() != null)
		{
			builder.createAlias("categorie", "categorie");
			builder.addEquals("categorie.irisVoorval", filter.getIrisCategorie());
		}

		if (filter.getStaatInIris() != null)
		{
			builder.addIsNull("irisIncidentNummer", !filter.getStaatInIris().booleanValue());
		}

		builder.addGreaterOrEquals("begindatum", filter.getVanafDatum());
		builder.addLessOrEquals("begindatum",
			TimeUtil.getInstance().maakEindeVanDagVanDatum(filter.getTotDatum()));

		return criteria;
	}
}
