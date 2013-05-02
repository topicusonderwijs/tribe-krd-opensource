package nl.topicus.eduarte.dao.participatie.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.dao.participatie.helpers.DeelnemerMedewerkerGroepDataAccesHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.participatie.DeelnemerMedewerkerGroep;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.zoekfilters.DeelnemerMedewerkerGroepZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.MatchMode;

public class DeelnemerMedewerkerGroepHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<DeelnemerMedewerkerGroep, DeelnemerMedewerkerGroepZoekFilter>
		implements DeelnemerMedewerkerGroepDataAccesHelper
{
	public DeelnemerMedewerkerGroepHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(DeelnemerMedewerkerGroepZoekFilter filter)
	{
		Criteria criteria = createCriteria(DeelnemerMedewerkerGroep.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder
			.addILikeFixedMatchMode("omschrijving", filter.getOmschrijving(), MatchMode.ANYWHERE);
		builder.addEquals("type", filter.getType());
		builder.addNotEquals("type", filter.getExcludeType());

		filter.addQuickSearchCriteria(builder, "code", "omschrijving");

		return criteria;
	}

	@Override
	public List<DeelnemerMedewerkerGroep> list()
	{
		DeelnemerMedewerkerGroepZoekFilter filter = new DeelnemerMedewerkerGroepZoekFilter();
		filter.addOrderByProperty("omschrijving");
		filter.addOrderByProperty("type");
		return list(filter);
	}

	@Override
	public DeelnemerMedewerkerGroep getByFullID(String id)
	{
		Criteria criteria = createCriteria(DeelnemerMedewerkerGroep.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addEqualsIgnoringCase("id", id);

		return cachedTypedUnique(criteria);
	}

	@Override
	public DeelnemerMedewerkerGroep get(IdObject object)
	{
		if (object == null)
			return null;

		if (Deelnemer.class.isAssignableFrom(Hibernate.getClass(object)))
			return getByFullID("D_" + object.getIdAsSerializable());

		if (Medewerker.class.isAssignableFrom(Hibernate.getClass(object)))
			return getByFullID("M_" + object.getIdAsSerializable());

		if (Groep.class.isAssignableFrom(Hibernate.getClass(object)))
			return getByFullID("G_" + object.getIdAsSerializable());

		throw new IllegalArgumentException("object heeft een ongeldig type: " + object.getClass());
	}
}
