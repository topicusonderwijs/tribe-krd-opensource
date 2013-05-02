/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.signalering.EventAbonnementType;
import nl.topicus.eduarte.app.signalering.EventReceiver;
import nl.topicus.eduarte.dao.helpers.DBSMedewerkerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.LocatieDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.MedewerkerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.OrganisatieEenheidDataAccessHelper;
import nl.topicus.eduarte.entities.groep.GroepDocent;
import nl.topicus.eduarte.entities.groep.GroepMentor;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.OrganisatieMedewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.security.authentication.MedewerkerAccount;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.entities.signalering.settings.MedewerkerDeelnemerAbonnering;
import nl.topicus.eduarte.entities.signalering.settings.MedewerkerGroepAbonnering;
import nl.topicus.eduarte.zoekfilters.MedewerkerZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.*;

public class MedewerkerHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Medewerker, MedewerkerZoekFilter> implements
		MedewerkerDataAccessHelper
{
	public MedewerkerHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(MedewerkerZoekFilter filter)
	{
		Criteria criteria = createCriteria(Medewerker.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.createAlias("persoon", "persoon");

		if (filter.getHeeftAccount() != null)
		{
			DetachedCriteria dcAccount = createDetachedCriteria(MedewerkerAccount.class);
			dcAccount.setProjection(Projections.property("medewerker"));
			DetachedCriteriaBuilder dcAccountBuilder = new DetachedCriteriaBuilder(dcAccount);
			dcAccountBuilder.addIsNull("medewerker", false);

			if (filter.getHeeftAccount())
				criteria.add(Subqueries.propertyIn("id", dcAccount));
			else
				criteria.add(Subqueries.propertyNotIn("id", dcAccount));
			filter.setResultCacheable(false);
		}
		if (!filter.addOrganisatieEenheidLocatieDetachedCriteria(this, criteria,
			OrganisatieMedewerker.class, "medewerker"))
			return null;

		builder.addLessOrEquals("begindatum", filter.getPeildatum());
		builder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
		builder.addILikeCheckWildcard("afkorting", filter.getAfkorting(), MatchMode.START);
		builder.addILikeCheckWildcard("persoon.achternaam", filter.getAchternaam(),
			MatchMode.ANYWHERE);
		builder.addILikeCheckWildcard("persoon.voorvoegsel", filter.getVoorvoegsel(),
			MatchMode.START);
		builder
			.addILikeCheckWildcard("persoon.voornamen", filter.getVoornaam(), MatchMode.ANYWHERE);
		builder.addEquals("persoon.geslacht", filter.getGeslacht());
		builder.addEquals("persoon.bsn", filter.getBsn());
		builder.addEquals("functie", filter.getFunctie());
		if (filter.getMultizoek() != null)
		{
			Criterion volledigeNaam =
				builder.getVolledigeNaamILike("persoon.berekendeZoeknaam", filter.getMultizoek());
			builder.addOrs(volledigeNaam,
				Restrictions.ilike("afkorting", filter.getMultizoek(), MatchMode.START));
		}

		if (filter.getRollen() != null && !filter.getRollen().isEmpty())
		{
			DetachedCriteria dc = createDetachedCriteria(MedewerkerAccount.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);

			List<Long> rolIds = new ArrayList<Long>();
			for (Rol rol : filter.getRollen())
				rolIds.add(rol.getId());

			dcBuilder.createAlias("roles", "rol");
			dc.add(Restrictions.in("rol.id", rolIds));
			dc.add(Restrictions.isNotNull("medewerker"));
			dc.setProjection(Projections.property("medewerker"));
			builder.getCriteria().add(Subqueries.propertyIn("id", dc));
		}

		filter.addQuickSearchCriteria(builder, "afkorting", "persoon.achternaam",
			"persoon.officieleAchternaam", "persoon.roepnaam", "persoon.berekendeZoeknaam");

		if (filter.getSnelZoekenString() != null)
		{
			builder.addOrs(getSnelZoekCriteria(filter.getSnelZoekenString(), "persoon"));
		}

		return criteria;
	}

	public List<Criterion> getSnelZoekCriteria(String snelZoekenString, String persoonAlias)
	{
		List<Criterion> whereList = new ArrayList<Criterion>();

		whereList.add(Restrictions.ilike(persoonAlias + ".achternaam", snelZoekenString,
			MatchMode.ANYWHERE));
		whereList.add(Restrictions.ilike(persoonAlias + ".officieleAchternaam", snelZoekenString,
			MatchMode.ANYWHERE));
		whereList.add(Restrictions.ilike(persoonAlias + ".roepnaam", snelZoekenString,
			MatchMode.ANYWHERE));
		whereList.add(Restrictions.ilike(persoonAlias + ".berekendeZoeknaam", snelZoekenString,
			MatchMode.ANYWHERE));

		Date geboorteDatum = TimeUtil.getInstance().parseDateString(snelZoekenString);
		if (geboorteDatum != null)
			whereList.add(Restrictions.eq(persoonAlias + ".geboortedatum", geboorteDatum));

		return whereList;
	}

	@Override
	public Conjunction addMultiZoekCriteria(String multiZoek)
	{
		boolean addedCheck = false;
		String[] zoekwaardes = multiZoek.split(",");
		Conjunction zoekwaardesTotaal = Restrictions.conjunction();
		Disjunction zoekwaardesIteratie = Restrictions.disjunction();
		Date geboorteDatum = null;

		for (int i = 0; i < zoekwaardes.length; i++)
		{
			if (StringUtil.isEmpty(zoekwaardes[i]))
				continue;
			if (!StringUtil.isNumeric(zoekwaardes[i]))
			{
				addedCheck = true;
				zoekwaardesIteratie.add(Restrictions.ilike("persoon.roepnaam", zoekwaardes[i],
					MatchMode.ANYWHERE));
				zoekwaardesIteratie.add(Restrictions.ilike("persoon.achternaam", zoekwaardes[i],
					MatchMode.ANYWHERE));
			}
			// geboortedatum kan zowel numeriek als niet numeriek zijn (streepjes e.d)
			geboorteDatum = TimeUtil.getInstance().parseDateString(zoekwaardes[i]);
			if (geboorteDatum != null)
			{
				zoekwaardesIteratie.add(Restrictions.eq("persoon.geboortedatum", geboorteDatum));
				addedCheck = true;
			}

			zoekwaardesTotaal.add(zoekwaardesIteratie);
			zoekwaardesIteratie = Restrictions.disjunction();
		}
		return addedCheck ? zoekwaardesTotaal : null;
	}

	@Override
	public Medewerker get(Long id)
	{
		return get(Medewerker.class, id);
	}

	@Override
	public Medewerker get(Persoon persoon)
	{
		Asserts.assertNotNull("persoon", persoon);
		return cachedTypedUnique(createCriteria(Medewerker.class).add(
			Restrictions.eq("persoon", persoon)));
	}

	@Override
	public boolean isDocentVan(Medewerker docent, Deelnemer deelnemer, Date peilDatum)
	{
		Criteria criteria = createCriteria(GroepDocent.class);
		createDocentBegeleiderCriteria(docent, deelnemer, peilDatum, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) unique(criteria, true) > 0;
	}

	@Override
	public boolean isBegeleiderVan(Medewerker begeleider, Deelnemer deelnemer, Date peilDatum)
	{
		Criteria criteria = createCriteria(GroepMentor.class);
		createDocentBegeleiderCriteria(begeleider, deelnemer, peilDatum, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) unique(criteria, true) > 0;
	}

	@Override
	public List<Medewerker> getDocentenVan(Deelnemer deelnemer, Date peilDatum)
	{
		Criteria criteria = createCriteria(GroepDocent.class);
		createDocentBegeleiderCriteria(null, deelnemer, peilDatum, criteria);
		criteria.setProjection(Projections.property("medewerker"));
		return cachedTypedList(criteria);
	}

	@Override
	public List<Medewerker> getBegeleidersVan(Deelnemer deelnemer, Date peilDatum)
	{
		Criteria criteria = createCriteria(GroepMentor.class);
		createDocentBegeleiderCriteria(null, deelnemer, peilDatum, criteria);
		criteria.setProjection(Projections.property("medewerker"));
		return cachedTypedList(criteria);
	}

	/**
	 * Het gemeenschappelijke deel van de query of iemand docent of begeleider is van een
	 * deelnemer.
	 */
	private void createDocentBegeleiderCriteria(Medewerker medewerker, Deelnemer deelnemer,
			Date peilDatum, Criteria criteria)
	{
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		if (medewerker == null)
		{
			criteria.createAlias("medewerker", "medewerker");
			builder.addLessOrEquals("medewerker.begindatum", peilDatum);
			builder.addGreaterOrEquals("medewerker.einddatumNotNull", peilDatum);
		}
		else
			builder.addEquals("medewerker", medewerker);

		if (deelnemer != null)
		{
			criteria.createAlias("groep", "groep");
			criteria.createAlias("groep.deelnemers", "deelname");
			builder.addEquals("deelname.deelnemer", deelnemer);
			builder.addLessOrEquals("deelname.begindatum", peilDatum);
			builder.addGreaterOrEquals("deelname.einddatumNotNull", peilDatum);
			builder.addLessOrEquals("groep.begindatum", peilDatum);
			builder.addGreaterOrEquals("groep.einddatumNotNull", peilDatum);
		}
	}

	@Override
	public Medewerker batchGetByAfkorting(String afkorting)
	{
		Asserts.assertNotEmpty("afkorting", afkorting);
		return cachedTypedUnique(createCriteria(Medewerker.class).add(
			Restrictions.eq("afkorting", afkorting)));
	}

	@Override
	public List<OrganisatieMedewerker> getOrganisatieMedewerkers(Medewerker medewerker,
			boolean instellingAuthorized, boolean orgEhdAuthorized)
	{
		OrganisatieEenheidDataAccessHelper helper =
			DataAccessRegistry.getHelper(OrganisatieEenheidDataAccessHelper.class);
		LocatieDataAccessHelper locatieHelper =
			DataAccessRegistry.getHelper(LocatieDataAccessHelper.class);
		if (instellingAuthorized)
		{
			List<OrganisatieEenheid> orgEhds = helper.list();
			List<Locatie> locaties = locatieHelper.list(EduArteContext.get().getPeildatum());
			List<OrganisatieMedewerker> res = new ArrayList<OrganisatieMedewerker>();
			for (OrganisatieEenheid ehd : orgEhds)
			{
				for (Locatie loc : locaties)
				{
					res.add(new OrganisatieMedewerker(ehd, loc));
				}
			}
			return res;
		}
		else
		{
			if (orgEhdAuthorized && medewerker != null)
			{
				return medewerker.getOrganisatieMedewerkers();
			}
		}
		return Collections.emptyList();
	}

	@Override
	public Map<EventAbonnementType, List< ? extends EventReceiver>> getEventReceivers(
			Deelnemer deelnemer)
	{
		Map<EventAbonnementType, List< ? extends EventReceiver>> ret =
			new HashMap<EventAbonnementType, List< ? extends EventReceiver>>();
		Date nu = TimeUtil.getInstance().currentDate();
		ret.put(EventAbonnementType.GeselecteerdeDeelnemers, getDeelnemerMedewerkers(deelnemer));
		ret.put(EventAbonnementType.GeselecteerdeGroepen, getGroepMedewerkers(deelnemer, nu));
		ret.put(EventAbonnementType.Docent, getDocentenVan(deelnemer, nu));
		ret.put(EventAbonnementType.Mentor, getBegeleidersVan(deelnemer, nu));
		ret.put(
			EventAbonnementType.Uitvoerende,
			DataAccessRegistry.getHelper(DBSMedewerkerDataAccessHelper.class).getUitvoerendenVan(
				deelnemer, nu));
		ret.put(EventAbonnementType.Verantwoordelijke,
			DataAccessRegistry.getHelper(DBSMedewerkerDataAccessHelper.class)
				.getVerantwoordelijkenVan(deelnemer, nu));
		return ret;
	}

	private List<Medewerker> getDeelnemerMedewerkers(Deelnemer deelnemer)
	{
		Criteria criteria = createCriteria(MedewerkerDeelnemerAbonnering.class);
		criteria.add(Restrictions.eq("deelnemer", deelnemer));
		criteria.setProjection(Projections.property("medewerker"));
		return cachedTypedList(criteria);
	}

	private List<Medewerker> getGroepMedewerkers(Deelnemer deelnemer, Date peildatum)
	{
		Criteria criteria = createCriteria(MedewerkerGroepAbonnering.class);
		criteria.createAlias("groep", "groep");
		criteria.createAlias("groep.deelnamesUnordered", "deelname");
		// criteria.createAlias("deelname.deelnemer", "deelnemer");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("deelname.deelnemer", deelnemer);
		builder.addLessOrEquals("deelname.begindatum", peildatum);
		builder.addGreaterOrEquals("deelname.einddatumNotNull", peildatum);
		criteria.setProjection(Projections.property("medewerker"));
		return cachedTypedList(criteria);
	}
}
