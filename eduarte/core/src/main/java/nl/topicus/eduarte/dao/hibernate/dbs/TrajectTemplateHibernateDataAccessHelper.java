package nl.topicus.eduarte.dao.hibernate.dbs;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.dbs.TrajectTemplateDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.trajecten.templates.TrajectTemplate;
import nl.topicus.eduarte.zoekfilters.dbs.TrajectTemplateZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class TrajectTemplateHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<TrajectTemplate, TrajectTemplateZoekFilter> implements
		TrajectTemplateDataAccessHelper
{
	public TrajectTemplateHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(TrajectTemplateZoekFilter filter)
	{
		Criteria criteria = createCriteria(TrajectTemplate.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		if (!filter.addOrganisatieEenheidLocatieCriteria(criteria))
			return null;

		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.ANYWHERE);
		if (filter.getActief() != null)
		{
			builder.createAlias("trajectSoort", "trajectSoort");
			if (filter.getActief())
				criteria.add(Restrictions.and(Restrictions.eq("actief", true),
					Restrictions.eq("trajectSoort.actief", true)));
			else
				criteria.add(Restrictions.or(Restrictions.eq("actief", false),
					Restrictions.eq("trajectSoort.actief", false)));
		}

		if (filter.getMetAutomatischeKoppeling() != null)
			builder.addIsNull("automatischeKoppeling",
				!(filter.getMetAutomatischeKoppeling().booleanValue()));
		if (filter.getBeginDatumAutomatischeKoppeling() != null)
		{
			builder.createAlias("automatischeKoppeling", "koppeling");
			builder.addLessOrEquals("koppeling.datumBeschikbaar",
				filter.getBeginDatumAutomatischeKoppeling());
		}

		filter.addQuickSearchCriteria(builder, "naam");

		return criteria;
	}
}
