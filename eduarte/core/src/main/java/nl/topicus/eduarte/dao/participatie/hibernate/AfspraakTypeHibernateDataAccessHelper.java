package nl.topicus.eduarte.dao.participatie.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.participatie.helpers.AfspraakTypeDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.AfspraakType;
import nl.topicus.eduarte.participatie.zoekfilters.AfspraakTypeZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class AfspraakTypeHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<AfspraakType, AfspraakTypeZoekFilter> implements
		AfspraakTypeDataAccessHelper
{
	public AfspraakTypeHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(AfspraakTypeZoekFilter filter)
	{
		String afspraakTypeAlias = "afspraakType";
		Criteria criteria = createCriteria(AfspraakType.class, afspraakTypeAlias);
		addCriteria(criteria, filter, afspraakTypeAlias);

		return criteria;
	}

	@Override
	public Criteria addCriteria(Criteria criteria, AfspraakTypeZoekFilter filter,
			String afspraakTypeAlias)
	{
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		if (!filter.addOrganisatieEenheidLocatieCriteria(criteria))
			return null;

		builder.addEquals(afspraakTypeAlias + ".actief", filter.isActief());
		builder.addIn(afspraakTypeAlias + ".category", filter.getCategories());
		builder.addILikeCheckWildcard(afspraakTypeAlias + ".naam", filter.getNaam(),
			MatchMode.START);
		builder.addILikeCheckWildcard(afspraakTypeAlias + ".omschrijving",
			filter.getOmschrijving(), MatchMode.START);
		if (!filter.getIncludeMedewerkerOnly())
		{
			builder.addEquals(afspraakTypeAlias + ".medewerkerOnly", false);
		}
		return criteria;
	}

	@Override
	public boolean conflicts(AfspraakType type)
	{
		if (!type.isActief())
			return false;
		Criteria criteria = createCriteria(AfspraakType.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addNotEquals("id", type.getId());
		builder.addEquals("naam", type.getNaam());

		Conjunction hiearchyCheck = new Conjunction();
		hiearchyCheck.add(Restrictions.in("organisatieEenheid", type.getOrganisatieEenheid()
			.getParentsEnChildren(TimeUtil.getInstance().currentDate())));
		hiearchyCheck.add(Restrictions.eq("actief", true));

		criteria.add(Restrictions.or(hiearchyCheck,
			Restrictions.eq("organisatieEenheid", type.getOrganisatieEenheid())));

		criteria.setProjection(Projections.rowCount());
		return (Long) cachedUnique(criteria) > 0;
	}
}
