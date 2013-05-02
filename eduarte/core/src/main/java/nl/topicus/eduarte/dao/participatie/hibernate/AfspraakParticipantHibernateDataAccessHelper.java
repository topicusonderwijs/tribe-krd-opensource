package nl.topicus.eduarte.dao.participatie.hibernate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.MedewerkerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.AfspraakParticpantDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.participatie.AfspraakDeelnemer;
import nl.topicus.eduarte.entities.participatie.AfspraakParticipant;
import nl.topicus.eduarte.entities.participatie.ExternPersoon;
import nl.topicus.eduarte.entities.participatie.PersoonlijkeGroep;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.participatie.zoekfilters.AfspraakParticipantMultiZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.AfspraakParticipantZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.AfspraakZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class AfspraakParticipantHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<AfspraakParticipant, AfspraakParticipantZoekFilter>
		implements AfspraakParticpantDataAccessHelper
{
	public AfspraakParticipantHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(AfspraakParticipantZoekFilter filter)
	{
		Criteria criteria = createCriteria(AfspraakParticipant.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addEquals("afspraak", filter.getAfspraak());
		if (filter.getAfspraakZoekFilter() != null)
		{
			AfspraakZoekFilter afspraakFilter = filter.getAfspraakZoekFilter();
			builder.createAlias("afspraak", "afspraak");
			builder
				.addEquals("afspraak.organisatieEenheid", afspraakFilter.getOrganisatieEenheid());
			builder.addEquals("afspraak.presentieRegistratieVerplicht",
				afspraakFilter.getPresentieRegistratieVerplicht());
			builder.addEquals("afspraak.presentieRegistratieVerwerkt",
				afspraakFilter.getPresentieRegistratieVerwerkt());
			builder.addGreaterOrEquals("afspraak.beginDatumTijd",
				afspraakFilter.getBeginDatumTijd());
			builder.addLessOrEquals("afspraak.eindDatumTijd", afspraakFilter.getEindDatumTijd());
		}
		builder.addEquals("groep", filter.getGroep());
		builder.addEquals("persoon", filter.getPersoon());
		builder.addEquals("afspraakRol", filter.getAfspraakRol());
		builder.addEquals("auteur", filter.getAuteur());

		return criteria;
	}

	@Override
	public List< ? extends IdObject> getMultiZoekEntiteiten(
			AfspraakParticipantMultiZoekFilter filter)
	{
		final List<Class< ? extends IdObject>> classOrder =
			new ArrayList<Class< ? extends IdObject>>();
		classOrder.add(ExternPersoon.class);
		classOrder.add(Groep.class);
		classOrder.add(PersoonlijkeGroep.class);
		classOrder.add(Medewerker.class);
		classOrder.add(Deelnemer.class);
		SortedSet<IdObject> ret = new TreeSet<IdObject>(new Comparator<IdObject>()
		{
			@Override
			public int compare(IdObject o1, IdObject o2)
			{
				int classDiff =
					classOrder.indexOf(o1.getClass()) - classOrder.indexOf(o2.getClass());
				if (classDiff != 0)
					return classDiff;
				return o1.toString().compareTo(o2.toString());
			}
		});

		Criteria groepCriteria = createCriteria(Groep.class, "groep");
		CriteriaBuilder groepCritBuilder = new CriteriaBuilder(groepCriteria);
		groepCriteria.addOrder(Order.asc("omschrijving"));
		groepCriteria.addOrder(Order.asc("id"));
		groepCritBuilder.addLessOrEquals("datumIngang", filter.getDatum());
		groepCritBuilder.addNullOrGreaterOrEquals("datumEinde", filter.getDatum());
		groepCriteria.add(Restrictions.or(
			Restrictions.ilike("groepscode", filter.getInput(), MatchMode.START),
			Restrictions.ilike("omschrijving", filter.getInput(), MatchMode.ANYWHERE)));
		groepCriteria.setMaxResults(filter.getMaxResults());
		ret.addAll(this.<IdObject> cachedList(groepCriteria));

		Criteria pgroepCriteria = createCriteria(PersoonlijkeGroep.class, "pgroep");
		CriteriaBuilder pgroepCritBuilder = new CriteriaBuilder(pgroepCriteria);
		pgroepCriteria.addOrder(Order.asc("omschrijving"));
		pgroepCriteria.addOrder(Order.asc("id"));
		pgroepCritBuilder.addLessOrEquals("beginDatum", filter.getDatum());
		pgroepCritBuilder.addNullOrGreaterOrEquals("eindDatum", filter.getDatum());
		pgroepCriteria.add(Restrictions.or(Restrictions.or(
			Restrictions.eq("deelnemer", filter.getDeelnemerEigenaar()),
			Restrictions.eq("medewerker", filter.getMedewerkerEigenaar())), Restrictions.eq(
			"gedeeld", Boolean.TRUE)));
		pgroepCriteria.add(Restrictions.or(
			Restrictions.ilike("code", filter.getInput(), MatchMode.START),
			Restrictions.ilike("omschrijving", filter.getInput(), MatchMode.ANYWHERE)));
		pgroepCriteria.setMaxResults(filter.getMaxResults());
		ret.addAll(this.<IdObject> cachedList(pgroepCriteria));

		Criteria deelnemerCriteria = createCriteria(Deelnemer.class, "deelnemer");
		deelnemerCriteria.addOrder(Order.asc("deelnemerNummer"));
		deelnemerCriteria.addOrder(Order.asc("id"));
		deelnemerCriteria.createAlias("persoon", "persoon");
		Conjunction dnMultiZoek =
			DataAccessRegistry.getHelper(VerbintenisDataAccessHelper.class).addQuickSearchCriteria(
				filter.getQuickSearchQuery(), false, filter.getGearchiveerd());
		deelnemerCriteria.add(dnMultiZoek);
		DetachedCriteria dc = createDetachedCriteria(Verbintenis.class);
		dc.setProjection(Projections.property("deelnemer"));
		deelnemerCriteria.add(Subqueries.propertyIn("deelnemer.id", dc));
		DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
		dcBuilder.addLessOrEquals("datumInschrijving", filter.getDatum());
		dcBuilder.addNullOrGreaterOrEquals("datumUitschrijving", filter.getDatum());
		filter.setResultCacheable(false);
		ret.addAll(this.<IdObject> uncachedList(deelnemerCriteria));

		Criteria medewerkerCriteria = createCriteria(Medewerker.class, "medewerker");
		medewerkerCriteria.createAlias("persoon", "persoon");
		medewerkerCriteria.addOrder(Order.asc("persoon.achternaam"));
		medewerkerCriteria.addOrder(Order.asc("id"));
		medewerkerCriteria.add(Restrictions.eq("indicatieActief", "J"));
		Conjunction mwMultiZoek =
			DataAccessRegistry.getHelper(MedewerkerDataAccessHelper.class).addMultiZoekCriteria(
				filter.getInput());
		if (mwMultiZoek != null)
		{
			medewerkerCriteria.add(mwMultiZoek);
			ret.addAll(this.<IdObject> cachedList(medewerkerCriteria));
		}

		if (StringUtil.isEmail(filter.getInput()))
			ret.add(new ExternPersoon(filter.getInput()));

		return new ArrayList<IdObject>(ret)
			.subList(0, Math.min(filter.getMaxResults(), ret.size()));
	}

	@Override
	public String getRenderString(IdObject participantEntiteit)
	{
		if (participantEntiteit instanceof Groep)
		{
			Groep groep = (Groep) participantEntiteit;
			return groep.getCode() + " - " + groep.getNaam();
		}
		return participantEntiteit == null ? "" : participantEntiteit.toString();
	}

	@Override
	public String getImageName(IdObject participantEntiteit)
	{
		if (participantEntiteit instanceof Groep)
			return "assets/img/agenda/Groep.gif";
		else if (participantEntiteit instanceof PersoonlijkeGroep)
			return "assets/img/agenda/PGroep.gif";
		else if (participantEntiteit instanceof Deelnemer)
			return "assets/img/agenda/Deelnemer.gif";
		else if (participantEntiteit instanceof Medewerker)
			return "assets/img/agenda/Medewerker.gif";
		else if (participantEntiteit instanceof ExternPersoon)
			return "assets/img/agenda/extern.gif";
		throw new RuntimeException("Onbekende entiteit: " + participantEntiteit);
	}

	@Override
	public List<AfspraakDeelnemer> getAfspraakDeelnemers(AfspraakZoekFilter filter)
	{
		Criteria criteria = createCriteria(AfspraakDeelnemer.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		criteria.createAlias("afspraak", "afspraak");
		builder.addEquals("afspraak.organisatieEenheid", filter.getOrganisatieEenheid());
		builder.addEquals("afspraak.presentieRegistratieVerplicht",
			filter.getPresentieRegistratieVerplicht());
		builder.addEquals("afspraak.presentieRegistratieVerwerkt",
			filter.getPresentieRegistratieVerwerkt());
		// Ja dit klopt!
		builder.addLessOrEquals("afspraak.beginDatumTijd", filter.getEindDatumTijd());
		builder.addGreaterOrEquals("afspraak.eindDatumTijd", filter.getBeginDatumTijd());

		return cachedList(criteria);
	}

	@Override
	public List<IdObject> findPossibleParticipanten(Persoon persoon)
	{
		List<IdObject> ret = new ArrayList<IdObject>();
		Medewerker medewerker =
			DataAccessRegistry.getHelper(MedewerkerDataAccessHelper.class).get(persoon);
		if (medewerker != null)
			ret.add(medewerker);
		Deelnemer deelnemer =
			DataAccessRegistry.getHelper(DeelnemerDataAccessHelper.class).getByPersoon(persoon);
		if (deelnemer != null)
			ret.add(deelnemer);
		return ret;
	}
}
