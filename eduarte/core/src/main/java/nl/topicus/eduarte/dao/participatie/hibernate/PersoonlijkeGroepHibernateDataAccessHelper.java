package nl.topicus.eduarte.dao.participatie.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.participatie.helpers.PersoonlijkeGroepDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.DeelnemerPersoonlijkeGroep;
import nl.topicus.eduarte.entities.participatie.PersoonlijkeGroep;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.participatie.zoekfilters.PersoonlijkeGroepZoekFilter;
import nl.topicus.eduarte.providers.PersoonProvider;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class PersoonlijkeGroepHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<PersoonlijkeGroep, PersoonlijkeGroepZoekFilter>
		implements PersoonlijkeGroepDataAccessHelper
{
	public PersoonlijkeGroepHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(PersoonlijkeGroepZoekFilter filter)
	{
		Criteria criteria = createCriteria(PersoonlijkeGroep.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		if (!filter.addOrganisatieEenheidLocatieCriteria(criteria))
			return null;

		builder.addILikeCheckWildcard("code", filter.getCode(), MatchMode.START);
		builder.addILikeCheckWildcard("omschrijving", filter.getOmschrijving(), MatchMode.START);
		builder.addLessOrEquals("begindatum", filter.getPeildatum());
		builder.addNullOrGreaterOrEquals("einddatum", filter.getPeildatum());
		if (filter.getDeelnemerEigenaar() != null || filter.getMedewerkerEigenaar() != null)
		{
			if (filter.isToonGroepenVanAnderen())
			{
				criteria.add(Restrictions.or(Restrictions.or(
					Restrictions.eq("deelnemer", filter.getDeelnemerEigenaar()),
					Restrictions.eq("medewerker", filter.getMedewerkerEigenaar())), Restrictions
					.eq("gedeeld", Boolean.TRUE)));
			}
			else
			{
				criteria.add(Restrictions.or(
					Restrictions.eq("deelnemer", filter.getDeelnemerEigenaar()),
					Restrictions.eq("medewerker", filter.getMedewerkerEigenaar())));
			}
		}
		if (filter.getDeelnemer() != null)
		{
			DetachedCriteria dcDeelnemers =
				createDetachedCriteria(DeelnemerPersoonlijkeGroep.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dcDeelnemers);
			dcBuilder.addEquals("deelnemer", filter.getDeelnemer());
			dcDeelnemers.setProjection(Projections.property("groep"));
			criteria.add(Subqueries.propertyIn("id", dcDeelnemers));
			filter.setResultCacheable(false);
		}
		return criteria;
	}

	@Override
	public List<PersoonlijkeGroep> list()
	{
		Criteria criteria = createCriteria(PersoonlijkeGroep.class);
		List<PersoonlijkeGroep> persoonlijkeGroepen = cachedList(criteria);
		return persoonlijkeGroepen;
	}

	@Override
	public List<PersoonlijkeGroep> list(Deelnemer deelnemer)
	{
		Criteria criteria = createCriteria(DeelnemerPersoonlijkeGroep.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("deelnemer", deelnemer);
		List<DeelnemerPersoonlijkeGroep> persoonlijkeGroepDeelnemers = cachedList(criteria);
		Set<PersoonlijkeGroep> persoonlijkeGroepen = new HashSet<PersoonlijkeGroep>();
		for (DeelnemerPersoonlijkeGroep pgd : persoonlijkeGroepDeelnemers)
		{
			persoonlijkeGroepen.add(pgd.getGroep());
		}
		return new ArrayList<PersoonlijkeGroep>(persoonlijkeGroepen);
	}

	@Override
	public List<Deelnemer> getDeelnemers(PersoonlijkeGroep groep, Date peilDatum)
	{
		Criteria criteria = createCriteria(DeelnemerPersoonlijkeGroep.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addLessOrEquals("begindatum", peilDatum);
		builder.addNullOrGreaterOrEquals("einddatum", peilDatum);
		builder.addEquals("groep", groep);
		criteria.setProjection(Projections.property("deelnemer"));
		return cachedList(criteria);
	}

	@Override
	public boolean isConflicterend(PersoonlijkeGroep groep, String code, boolean gedeeld,
			PersoonProvider eigenaar)
	{
		Criteria criteria = createCriteria(PersoonlijkeGroep.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("organisatie", groep.getOrganisatie());
		builder.addNotEquals("id", groep.getId());
		builder.addEquals("code", code);
		builder.addEquals("gedeeld", gedeeld);
		if (!gedeeld)
		{
			if (eigenaar instanceof Deelnemer)
				builder.addEquals("deelnemer", eigenaar);
			else
				builder.addEquals("medewerker", eigenaar);
		}
		criteria.setProjection(Projections.rowCount());
		return (Long) cachedUnique(criteria) > 0;
	}
}
