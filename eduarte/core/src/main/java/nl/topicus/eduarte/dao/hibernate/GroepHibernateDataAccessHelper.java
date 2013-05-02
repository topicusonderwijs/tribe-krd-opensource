/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.signalering.EventAbonnementType;
import nl.topicus.eduarte.app.signalering.EventReceiver;
import nl.topicus.eduarte.dao.helpers.GroepDataAccessHelper;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.GroepDocent;
import nl.topicus.eduarte.entities.groep.GroepMentor;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;
import nl.topicus.eduarte.zoekfilters.IMentorDocentZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class GroepHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Groep, GroepZoekFilter> implements GroepDataAccessHelper
{
	public GroepHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(GroepZoekFilter filter)
	{
		Criteria criteria = createCriteria(Groep.class);
		if (!filter.addOrganisatieEenheidLocatieCriteria(criteria))
			return null;

		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.createAlias("groepstype", "groepstype");
		builder.addILikeCheckWildcard("code", filter.getCode(), MatchMode.START);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);
		builder.addEqualsIgnoringCase("code", filter.getExactCaseInsensitiveMatch());

		builder.addEquals("groepstype", filter.getType());
		builder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
		builder.addEquals("groepstype.plaatsingsgroep", filter.getPlaatsingsgroep());

		filter.addQuickSearchCriteria(builder, "code", "naam");

		if (filter.getMentorOrDocent() != null)
		{
			/*
			 * Hier voegen we de zoektocht naar de groepen die de gegeven medewerker als
			 * docent of mentor hebben.
			 */
			DetachedCriteria dcritMentor = createDetachedCriteria(GroepMentor.class);
			DetachedCriteriaBuilder dcritMentorBuilder = new DetachedCriteriaBuilder(dcritMentor);
			dcritMentorBuilder.addEquals("medewerker", filter.getMentorOrDocent());
			dcritMentorBuilder.addLessOrEquals("begindatum", filter.getPeildatum());
			dcritMentorBuilder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
			dcritMentor.setProjection(Projections.property("groep"));

			DetachedCriteria dcritDocent = createDetachedCriteria(GroepDocent.class);
			DetachedCriteriaBuilder dcritDocentBuilder = new DetachedCriteriaBuilder(dcritDocent);
			dcritDocentBuilder.addEquals("medewerker", filter.getMentorOrDocent());
			dcritDocentBuilder.addLessOrEquals("begindatum", filter.getPeildatum());
			dcritDocentBuilder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
			dcritDocent.setProjection(Projections.property("groep"));

			builder.addOrs(Subqueries.propertyIn("id", dcritDocent), Subqueries.propertyIn("id",
				dcritMentor));
			filter.setResultCacheable(false);
		}

		if (filter.getSnelZoekenString() != null)
		{
			builder.addOrs(Restrictions.eq("code", filter.getSnelZoekenString()).ignoreCase(),
				Restrictions.ilike("naam", filter.getSnelZoekenString(), MatchMode.ANYWHERE));
		}

		if (filter.getOpleiding() != null)
		{
			/*
			 * We zoeken alle groepen waarvan minstens 1 deelnemer een verbintenis heeft
			 * met deze opleiding
			 */
			DetachedCriteria verbintenisCriteria = createDetachedCriteria(Verbintenis.class);
			DetachedCriteriaBuilder verbintenisBuilder =
				new DetachedCriteriaBuilder(verbintenisCriteria);
			verbintenisBuilder.addEquals("opleiding", filter.getOpleiding());
			verbintenisCriteria.setProjection(Projections.property("deelnemer"));
			builder.createAlias("deelnemers", "deelnemers");
			criteria.add(Subqueries.propertyIn("deelnemers.deelnemer", verbintenisCriteria));

			/*
			 * Duplicates filteren: Groepen waarvan meerdere deelnemers deze opleiding
			 * volgen worden meerdere keren gereturned. Deze 'oplossing' gaat fout met
			 * paginering:
			 * criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			 * Daarom wordt onderstaande methode gebruikt.
			 */
			criteria.setProjection(Projections.distinct(Projections.id()));
			List<Groep> list = uncachedList(criteria);

			if (list.size() > 0)
			{
				criteria = createCriteria(Groep.class);

				// De lijst verbonden aan een IN statement mag niet meer dan 1000
				// expressies bevatten, vandaar de splitsing in kleinere lijsten:
				int chunk = 1000;
				if (list.size() < chunk)
				{
					criteria.add(Restrictions.in("id", list));
				}
				else
				{
					builder = new CriteriaBuilder(criteria);
					List<Criterion> criterions = new ArrayList<Criterion>();
					int cnt = 0;
					while (list.size() > cnt)
					{
						int end = list.size() > (cnt + chunk) ? cnt + chunk : list.size();
						criterions.add(Restrictions.in("id", list.subList(cnt, end - 1)));
						cnt += chunk;
					}
					builder.addOrs(criterions);
				}
			}
			filter.setResultCacheable(false);
		}

		return criteria;
	}

	@Override
	public Groep get(Long id)
	{
		return get(Groep.class, id);
	}

	@Override
	public Groep getByGroepcode(String code)
	{
		Criteria criteria = createCriteria(Groep.class);
		criteria.add(Restrictions.eq("code", code));
		return cachedUnique(criteria);
	}

	@Override
	public List<Groep> getGroepen(List<String> roostercodes, Date peildatum)
	{
		Criteria criteria = createCriteria(Groep.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addIn("code", roostercodes);
		builder.addLessOrEquals("begindatum", peildatum);
		builder.addNullOrGreaterOrEquals("einddatum", peildatum);

		return cachedTypedList(criteria);
	}

	@Override
	public List<GroepDocent> getGroepenMetDocent(Medewerker docent, Date peildatum)
	{
		Criteria criteria = createCriteria(GroepDocent.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addLessOrEquals("begindatum", peildatum);
		builder.addNullOrGreaterOrEquals("einddatum", peildatum);
		builder.addEquals("medewerker", docent);
		return cachedList(criteria);
	}

	@Override
	public List<GroepMentor> getGroepenMetMentor(Medewerker mentor, Date peildatum)
	{
		Criteria criteria = createCriteria(GroepMentor.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addLessOrEquals("begindatum", peildatum);
		builder.addNullOrGreaterOrEquals("einddatum", peildatum);
		builder.addEquals("medewerker", mentor);
		return cachedList(criteria);
	}

	@Override
	public List<Medewerker> getMentorenVanGroep(Groep groep, Date peildatum)
	{
		Criteria criteria = createCriteria(GroepMentor.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addLessOrEquals("begindatum", peildatum);
		builder.addNullOrGreaterOrEquals("einddatum", peildatum);
		builder.addEquals("groep", groep);
		List<GroepMentor> list = cachedList(criteria);
		List<Medewerker> ret = new ArrayList<Medewerker>();
		for (GroepMentor gm : list)
		{
			ret.add(gm.getMedewerker());
		}
		return ret;
	}

	@Override
	public Map<EventAbonnementType, List< ? extends EventReceiver>> getEventReceivers(Groep groep)
	{
		Map<EventAbonnementType, List< ? extends EventReceiver>> ret =
			new HashMap<EventAbonnementType, List< ? extends EventReceiver>>();
		Date nu = TimeUtil.getInstance().currentDate();
		ret.put(EventAbonnementType.Mentor, getMentorenVanGroep(groep, nu));
		return ret;
	}

	@Override
	public List<Criterion> createMentorDocent(IMentorDocentZoekFilter< ? > filter,
			String deelnemerAlias)
	{
		List<Criterion> ret = new ArrayList<Criterion>();
		if (filter.getMentorOfDocent() != null)
		{
			DetachedCriteria dcritMentor =
				createGroepKoppelingCriteria(GroepMentor.class, filter.getMentorOfDocent(), filter
					.getPeildatum(), filter.getPeilEindDatum());
			DetachedCriteria dcritDocent =
				createGroepKoppelingCriteria(GroepDocent.class, filter.getMentorOfDocent(), filter
					.getPeildatum(), filter.getPeilEindDatum());

			ret.add(Restrictions.or(Subqueries.propertyIn(deelnemerAlias + ".id", dcritMentor),
				Subqueries.propertyIn(deelnemerAlias + ".id", dcritDocent)));
			filter.setResultCacheable(false);
		}
		if (filter.getDocent() != null)
		{
			DetachedCriteria dcritDocent =
				createGroepKoppelingCriteria(GroepDocent.class, filter.getDocent(), filter
					.getPeildatum(), filter.getPeilEindDatum());
			ret.add(Subqueries.propertyIn(deelnemerAlias + ".id", dcritDocent));
			filter.setResultCacheable(false);
		}
		if (filter.getMentor() != null)
		{
			DetachedCriteria dcritMentor =
				createGroepKoppelingCriteria(GroepMentor.class, filter.getMentor(), filter
					.getPeildatum(), filter.getPeilEindDatum());
			ret.add(Subqueries.propertyIn(deelnemerAlias + ".id", dcritMentor));
			filter.setResultCacheable(false);
		}
		return ret;
	}

	private DetachedCriteria createGroepKoppelingCriteria(
			Class< ? extends BeginEinddatumInstellingEntiteit> koppeling, Medewerker medewerker,
			Date peildatum, Date peilEinddatum)
	{
		DetachedCriteria ret = createDetachedCriteria(koppeling);
		DetachedCriteriaBuilder dcritDocentBuilder = new DetachedCriteriaBuilder(ret);

		dcritDocentBuilder.createAlias("groep", "groep");
		dcritDocentBuilder.createAlias("groep.deelnamesUnordered", "groepsdeelname");
		dcritDocentBuilder.addEquals("groep.organisatie", EduArteContext.get().getInstelling());
		dcritDocentBuilder.addEquals("groepsdeelname.organisatie", EduArteContext.get()
			.getInstelling());
		dcritDocentBuilder.addEquals("medewerker", medewerker);
		dcritDocentBuilder.addLessOrEquals("begindatum", peilEinddatum);
		dcritDocentBuilder.addGreaterOrEquals("einddatumNotNull", peildatum);
		dcritDocentBuilder.addLessOrEquals("groepsdeelname.begindatum", peilEinddatum);
		dcritDocentBuilder.addGreaterOrEquals("groepsdeelname.einddatumNotNull", peildatum);
		dcritDocentBuilder.addLessOrEquals("groep.begindatum", peilEinddatum);
		dcritDocentBuilder.addGreaterOrEquals("groep.einddatumNotNull", peildatum);
		ret.setProjection(Projections.property("groepsdeelname.deelnemer"));
		return ret;
	}
}
