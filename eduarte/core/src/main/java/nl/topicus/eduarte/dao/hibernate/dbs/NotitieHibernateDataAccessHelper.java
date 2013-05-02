package nl.topicus.eduarte.dao.hibernate.dbs;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.dbs.NotitieDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.gedrag.Notitie;
import nl.topicus.eduarte.entities.dbs.gedrag.NotitieNietTonenInZorgvierkant;
import nl.topicus.eduarte.zoekfilters.dbs.NotitieZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;

public class NotitieHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Notitie, NotitieZoekFilter> implements
		NotitieDataAccessHelper
{
	public NotitieHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(NotitieZoekFilter filter)
	{
		Criteria criteria = createCriteria(Notitie.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		filter.addCriteria(builder);
		builder.addNullOrGreaterOrEquals("dossierEindDatum", TimeUtil.getInstance().currentDate());
		builder.addEquals("deelnemer", filter.getDeelnemer());

		if (filter.getZorgvierkant() != null && filter.getZorgvierkant())
		{
			DetachedCriteria tonenInZorgvierkant =
				createDetachedCriteria(NotitieNietTonenInZorgvierkant.class);
			tonenInZorgvierkant.setProjection(Projections.property("notitie"));
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(tonenInZorgvierkant);
			dcBuilder.addEquals("medewerker", filter.getMedewerker());

			builder.propertyNotIn("id", tonenInZorgvierkant);
		}

		return criteria;
	}
}
