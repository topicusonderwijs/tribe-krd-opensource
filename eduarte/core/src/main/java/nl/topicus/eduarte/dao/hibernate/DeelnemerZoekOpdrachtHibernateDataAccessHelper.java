package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.eduarte.dao.helpers.DeelnemerZoekOpdrachtDataAccessHelper;
import nl.topicus.eduarte.entities.rapportage.DeelnemerZoekOpdracht;
import nl.topicus.eduarte.entities.rapportage.DeelnemerZoekOpdrachtRecht;
import nl.topicus.eduarte.zoekfilters.DeelnemerZoekOpdrachtZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class DeelnemerZoekOpdrachtHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<DeelnemerZoekOpdracht, DeelnemerZoekOpdrachtZoekFilter>
		implements DeelnemerZoekOpdrachtDataAccessHelper
{

	public DeelnemerZoekOpdrachtHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(DeelnemerZoekOpdrachtZoekFilter filter)
	{
		Criteria criteria = createCriteria(DeelnemerZoekOpdracht.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addILikeCheckWildcard("omschrijving", filter.getOmschrijving(), MatchMode.ANYWHERE);
		if (filter.isPersoonlijk() != null)
		{
			builder.addIsNull("account", !filter.isPersoonlijk());
		}
		if (filter.getAccount() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(DeelnemerZoekOpdrachtRecht.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			if (!filter.isPublicerenRecht())
				dcBuilder.addIn("rol", filter.getAccount().getRollenAsRol());
			dc.setProjection(Projections.property("zoekOpdracht"));
			builder.addOrs(Restrictions.eq("account", filter.getAccount()), Subqueries.propertyIn(
				"id", dc));
		}

		return criteria;
	}
}
