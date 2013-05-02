package nl.topicus.eduarte.dao.participatie.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.entities.Time;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.AfspraakDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.AfspraakTypeDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.ExterneAgendaDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.*;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakHerhalingDag;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakTypeCategory;
import nl.topicus.eduarte.entities.participatie.enums.UitnodigingStatus;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.participatie.zoekfilters.AfspraakZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.IIVOTijdZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.ParticipatieAanwezigheidMaandZoekFilter;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.providers.PersoonProvider;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.*;
import org.hibernate.transform.ResultTransformer;

public class AfspraakHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Afspraak, AfspraakZoekFilter> implements
		AfspraakDataAccessHelper
{
	public AfspraakHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(AfspraakZoekFilter filter)
	{
		List<Persoon> personen = new ArrayList<Persoon>();

		Criteria criteria = createCriteria(Afspraak.class, "afspraak");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder
			.createAlias("onderwijsproduct", "onderwijsproduct", CriteriaSpecification.LEFT_JOIN);

		if (!filter.addOrganisatieEenheidLocatieCriteria(criteria))
			return null;

		builder.addEquals("presentieRegistratieVerplicht", filter
			.getPresentieRegistratieVerplicht());
		builder.addEquals("presentieRegistratieVerwerkt", filter.getPresentieRegistratieVerwerkt());
		builder.addEquals("presentieRegistratieDoorDeelnemer", filter.getPresentiedoordeelnemer());

		if (filter.isIngeplandeAfspraken())
		{
			// ja, dit klopt echt, denk er maar eens over na. Zolang je alle de afspraken
			// wil die binnen de gestelde grens vallen, zoals alle afspraken in een week,
			// zelfs als een afspraak niet in zijn geheel in deze week valt
			builder.addLessOrEquals("beginDatumTijd", filter.getEindDatumTijd());
			builder.addGreaterOrEquals("eindDatumTijd", filter.getBeginDatumTijd());
			builder.addLessOrEquals("beginLesuur", filter.getEindLesuur());
			builder.addGreaterOrEquals("eindLesuur", filter.getBeginLesuur());

			// Te gebruiken als je alleen de afsrpaken wilt die voor of na een bepaalde
			// tijd vallen
			builder.addNullOrGreaterThan("beginDatumTijd", filter
				.getAfsprakenNaDezeDatumBeginTijd());
			builder.addLessThan("beginDatumTijd", filter.getAfsprakenVoorDezeDatumBeginTijd());
			builder.addGreaterThan("eindDatumTijd", filter.getAfsprakenNaDezeDatumEindTijd());
			builder.addNullOrLessOrEquals("eindDatumTijd", filter
				.getAfsprakenVoorDezeDatumEindTijd());

			builder.addIsNull("beginDatumTijd", false);
		}
		else
		{
			builder.addIsNull("beginDatumTijd", true);
			// builder.addLessThan("beginDatumTijd", true);
		}
		builder.addEquals("basisrooster", filter.getBasisrooster());

		builder.addEquals("onderwijsproduct", filter.getOnderwijsproduct());

		builder.addILikeFixedMatchMode("onderwijsproduct.code", filter.getOnderwijsproductCode(),
			MatchMode.START);
		builder.addEquals("afspraakType", filter.getAfspraakType());
		builder.addILikeCheckWildcard("titel", filter.getTitel(), MatchMode.START);
		builder.addGreaterThan("minutenIIVO", filter.getAantalMinutenIIVOGroterDan());

		if (filter.getAfspraakTypeZoekFilter() != null)
		{
			AfspraakTypeDataAccessHelper afspraakTypeHelper =
				DataAccessRegistry.getHelper(AfspraakTypeDataAccessHelper.class);
			String afspraakTypeAlias = "afspraakType";
			builder.createAlias("afspraakType", afspraakTypeAlias);
			afspraakTypeHelper.addCriteria(criteria, filter.getAfspraakTypeZoekFilter(),
				afspraakTypeAlias);
		}
		if (filter.getDeelnemer() != null)
		{
			DetachedCriteria dcDeelnemers = createDetachedCriteria(AfspraakDeelnemer.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dcDeelnemers);

			dcBuilder.addEquals("deelnemer", filter.getDeelnemer());
			dcBuilder.addEquals("contract", filter.getContract());
			dcBuilder.addIn("uitnodigingStatus", filter.getUitnodigingStatussen());
			dcDeelnemers.setProjection(Projections.property("afspraak"));
			criteria.add(Subqueries.propertyIn("id", dcDeelnemers));
			personen.add(filter.getDeelnemer().getPersoon());
			filter.setResultCacheable(false);
		}
		if (filter.getMedewerker() != null)
		{
			DetachedCriteria dcParticipanten = createDetachedCriteria(AfspraakParticipant.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dcParticipanten);

			dcBuilder.addEquals("medewerker", filter.getMedewerker());
			dcParticipanten.setProjection(Projections.property("afspraak"));
			criteria.add(Subqueries.propertyIn("id", dcParticipanten));
			personen.add(filter.getMedewerker().getPersoon());
			filter.setResultCacheable(false);
		}
		else if (filter.isMedewerkerNull())
		{
			DetachedCriteria dcParticipanten = createDetachedCriteria(AfspraakParticipant.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dcParticipanten);

			dcBuilder.addIsNull("medewerker", false);
			dcParticipanten.setProjection(Projections.property("afspraak"));
			criteria.add(Subqueries.propertyNotIn("id", dcParticipanten));
			filter.setResultCacheable(false);
		}
		if (filter.getGroep() != null)
		{
			DetachedCriteria dcParticipanten = createDetachedCriteria(AfspraakParticipant.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dcParticipanten);
			dcBuilder.addEquals("groep", filter.getGroep());

			dcParticipanten.setProjection(Projections.property("afspraak"));
			criteria.add(Subqueries.propertyIn("id", dcParticipanten));
			filter.setResultCacheable(false);
		}

		if ((filter.getAfspraakTypeCategorie() == null
			|| filter.getAfspraakTypeCategorie().isEmpty() || filter.getAfspraakTypeCategorie()
			.contains(AfspraakTypeCategory.EXTERN))
			&& filter.getBeginDatumTijd() != null && filter.getEindDatumTijd() != null)
		{
			DataAccessRegistry.getHelper(ExterneAgendaDataAccessHelper.class).update(personen,
				filter.getBeginDatumTijd(), filter.getEindDatumTijd());
		}

		if (filter.getPresentieRegistratieIngevuldVoorDeelnemer() != null)
		{
			DetachedCriteria dcWaarneming = createDetachedCriteria(Waarneming.class);
			dcWaarneming.setProjection(Projections.property("afspraak"));

			if (filter.getPresentieRegistratieIngevuldVoorDeelnemer())
				criteria.add(Subqueries.propertyIn("id", dcWaarneming));
			filter.setResultCacheable(false);
		}
		return criteria;
	}

	@Override
	public Map<IdObject, List<Afspraak>> getAfspraken(AfspraakZoekFilter filter,
			List<IdObject> participanten)
	{
		List<Persoon> personen = new ArrayList<Persoon>();
		List<Deelnemer> deelnemers = new ArrayList<Deelnemer>();
		List<Medewerker> medewerkers = new ArrayList<Medewerker>();
		for (IdObject curParticipant : participanten)
		{
			if (curParticipant instanceof Deelnemer)
				deelnemers.add((Deelnemer) curParticipant);
			else
				medewerkers.add((Medewerker) curParticipant);
			personen.add(((PersoonProvider) curParticipant).getPersoon());
		}

		if (filter.getAfspraakTypeCategorie() == null
			|| filter.getAfspraakTypeCategorie().isEmpty()
			|| filter.getAfspraakTypeCategorie().contains(AfspraakTypeCategory.EXTERN))
			DataAccessRegistry.getHelper(ExterneAgendaDataAccessHelper.class).update(personen,
				filter.getBeginDatumTijd(), filter.getEindDatumTijd());

		Map<IdObject, List<Afspraak>> ret = new HashMap<IdObject, List<Afspraak>>();

		if (!deelnemers.isEmpty())
		{
			Criteria deelnemerCriteria = createCriteria(AfspraakDeelnemer.class, "ad");
			CriteriaBuilder dcBuilder = new CriteriaBuilder(deelnemerCriteria);
			deelnemerCriteria.createAlias("afspraak", "afspraak");
			dcBuilder.addEquals("afspraak.presentieRegistratieVerplicht", filter
				.getPresentieRegistratieVerplicht());
			dcBuilder.addEquals("afspraak.presentieRegistratieVerwerkt", filter
				.getPresentieRegistratieVerwerkt());
			dcBuilder.addLessThan("afspraak.beginDatumTijd", filter.getEindDatumTijd());
			dcBuilder.addGreaterThan("afspraak.eindDatumTijd", filter.getBeginDatumTijd());
			dcBuilder.addNotEquals("uitnodigingStatus", UitnodigingStatus.GEWEIGERD);
			dcBuilder.addIn("deelnemer", deelnemers);
			deelnemerCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			for (AfspraakDeelnemer curAD : this.<AfspraakDeelnemer> cachedList(deelnemerCriteria))
			{
				addAfspraak(ret, curAD.getDeelnemer(), curAD.getAfspraak());
			}
		}

		if (!medewerkers.isEmpty())
		{
			Criteria medewerkerCriteria = createCriteria(AfspraakParticipant.class, "ap");
			CriteriaBuilder dcBuilder = new CriteriaBuilder(medewerkerCriteria);
			medewerkerCriteria.createAlias("afspraak", "afspraak");
			dcBuilder.addEquals("afspraak.presentieRegistratieVerplicht", filter
				.getPresentieRegistratieVerplicht());
			dcBuilder.addEquals("afspraak.presentieRegistratieVerwerkt", filter
				.getPresentieRegistratieVerwerkt());
			dcBuilder.addLessThan("afspraak.beginDatumTijd", filter.getEindDatumTijd());
			dcBuilder.addGreaterThan("afspraak.eindDatumTijd", filter.getBeginDatumTijd());
			dcBuilder.addNotEquals("uitnodigingStatus", UitnodigingStatus.GEWEIGERD);
			dcBuilder.addIn("medewerker", medewerkers);
			medewerkerCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			for (AfspraakParticipant curAP : this
				.<AfspraakParticipant> cachedList(medewerkerCriteria))
			{
				addAfspraak(ret, curAP.getMedewerker(), curAP.getAfspraak());
			}
		}
		return ret;
	}

	private void addAfspraak(Map<IdObject, List<Afspraak>> map, IdObject participant,
			Afspraak afspraak)
	{
		List<Afspraak> afspraken = map.get(participant);
		if (afspraken == null)
		{
			afspraken = new ArrayList<Afspraak>();
			map.put(participant, afspraken);
		}
		afspraken.add(afspraak);
	}

	@Override
	public Afspraak getById(Long id)
	{
		Criteria criteria = createCriteria(Afspraak.class);
		criteria.add(Restrictions.eq("id", id));
		return cachedTypedUnique(criteria);
	}

	@Override
	public List<Afspraak> getByExternId(String externId, ExternSysteem externSysteem,
			Basisrooster basisrooster)
	{
		Criteria criteria = createCriteria(Afspraak.class);
		criteria.add(Restrictions.eq("externId", externId));
		criteria.add(Restrictions.eq("externSysteem", externSysteem));
		criteria.add(Restrictions.eq("basisrooster", basisrooster));
		return cachedTypedList(criteria);
	}

	@Override
	public void calculateRecurrence(Afspraak base)
	{
		List<Afspraak> afspraken = new LinkedList<Afspraak>();
		for (Date curDate : calculateDates(base))
		{
			if (afspraken.isEmpty())
			{
				base.setBeginDatum(curDate);
				base.setEindDatum(curDate);
				afspraken.add(base);
			}
			else
			{
				Afspraak newAfspraak = new Afspraak();
				newAfspraak.setOrganisatieEenheid(base.getOrganisatieEenheid());
				newAfspraak.setOnderwijsproduct(base.getOnderwijsproduct());
				newAfspraak.setAfspraakType(base.getAfspraakType());
				newAfspraak.setAuteur(base.getAuteur());
				newAfspraak.setBeginDatumTijd(curDate);
				newAfspraak.setBeginTijd(base.getBeginTijd());
				newAfspraak.setEindDatumTijd(curDate);
				newAfspraak.setEindTijd(base.getEindTijd());
				newAfspraak.setHerhalendeAfspraak(base.getHerhalendeAfspraak());
				newAfspraak.setAfspraakLocatie(base.getAfspraakLocatie());
				newAfspraak.setMinutenIIVO(base.getMinutenIIVO());
				newAfspraak.setOmschrijving(base.getOmschrijving());
				for (AfspraakParticipant curParticipant : base.getParticipanten())
				{
					AfspraakParticipant newParticipant = new AfspraakParticipant();
					newParticipant.setAfspraak(newAfspraak);
					newParticipant.setParticipantEntiteit(curParticipant.getParticipantEntiteit());
					newParticipant.setUitnodigingStatus(curParticipant.getUitnodigingStatus());
					newParticipant.setUitnodigingVerstuurd(curParticipant.isUitnodigingVerstuurd());
					newAfspraak.addParticipant(newParticipant);
				}
				newAfspraak.setPresentieRegistratieDoorDeelnemer(base
					.isPresentieRegistratieDoorDeelnemer());
				newAfspraak
					.setPresentieRegistratieVerplicht(base.isPresentieRegistratieVerplicht());
				newAfspraak.setTitel(base.getTitel());
				if (base.getInloopCollege() != null)
				{
					if (base.getInloopCollege().isHeleHerhaling())
						newAfspraak.setInloopCollege(base.getInloopCollege());
					else
						newAfspraak.setInloopCollege(base.getInloopCollege().copy());
				}
				afspraken.add(newAfspraak);
			}
		}
		base.getHerhalendeAfspraak().setAfspraken(afspraken);
	}

	private List<Date> calculateDates(Afspraak afspraak)
	{
		switch (afspraak.getHerhalendeAfspraak().getType())
		{
			case DAGELIJKS:
				if (AfspraakHerhalingDag.DAG
					.equals(afspraak.getHerhalendeAfspraak().getEnkeleDag()))
					return calculateDatesDayStrategy(afspraak);
				return calculateDatesWorkingDayStrategy(afspraak);
			case WEKELIJKS:
				return calculateDatesWeekStrategy(afspraak);
			case MAANDELIJKS:
				if (AfspraakHerhalingDag.DAG
					.equals(afspraak.getHerhalendeAfspraak().getEnkeleDag()))
					return calculateDatesDayOfMonthStrategy(afspraak);
				else if (AfspraakHerhalingDag.WERKDAG.equals(afspraak.getHerhalendeAfspraak()
					.getEnkeleDag()))
					return calculateDatesWorkingDayOfMonthStrategy(afspraak);
				else
					return calculateDatesDayOfWeekInMonthStrategy(afspraak);
		}
		throw new IllegalArgumentException("Cannot calculate recurrence for " + afspraak);
	}

	private List<Date> calculateDatesDayOfWeekInMonthStrategy(Afspraak afspraak)
	{
		LinkedList<Date> ret = new LinkedList<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setLenient(false);
		cal.setTime(afspraak.getHerhalendeAfspraak().getBeginDatum());
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int count = -2;
		while (inRange(count, cal, afspraak))
		{
			int firstDay = cal.get(Calendar.DAY_OF_WEEK);
			int calDay = afspraak.getHerhalendeAfspraak().getEnkeleDag().getCalendarDay();
			int skipDays = afspraak.getHerhalendeAfspraak().getSkip() * 7;
			if (calDay >= firstDay)
				skipDays -= 7;
			skipDays += calDay - firstDay;
			cal.set(Calendar.DAY_OF_MONTH, skipDays + 1);
			try
			{
				ret.add(cal.getTime());
				count++;
			}
			catch (IllegalArgumentException e)
			{
				// skip this date
			}
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.MONTH, afspraak.getHerhalendeAfspraak().getCyclus());
		}
		return clipReturnList(afspraak, ret);
	}

	private List<Date> calculateDatesWorkingDayOfMonthStrategy(Afspraak afspraak)
	{
		LinkedList<Date> ret = new LinkedList<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setLenient(false);
		cal.setTime(afspraak.getHerhalendeAfspraak().getBeginDatum());
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int count = -2;
		while (inRange(count, cal, afspraak))
		{
			cal.set(Calendar.DAY_OF_MONTH, calculateWorkingDayIndex(cal.get(Calendar.DAY_OF_WEEK),
				afspraak.getHerhalendeAfspraak().getSkip()));
			try
			{
				ret.add(cal.getTime());
				count++;
			}
			catch (IllegalArgumentException e)
			{
				// skip this date
			}
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.MONTH, afspraak.getHerhalendeAfspraak().getCyclus());
		}
		return clipReturnList(afspraak, ret);
	}

	private List<Date> calculateDatesDayOfMonthStrategy(Afspraak afspraak)
	{
		LinkedList<Date> ret = new LinkedList<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setLenient(false);
		cal.setTime(afspraak.getHerhalendeAfspraak().getBeginDatum());
		int count = -2;
		while (inRange(count, cal, afspraak))
		{
			cal.set(Calendar.DAY_OF_MONTH, afspraak.getHerhalendeAfspraak().getSkip());
			try
			{
				ret.add(cal.getTime());
				count++;
			}
			catch (IllegalArgumentException e)
			{
				// skip this date
			}
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.MONTH, afspraak.getHerhalendeAfspraak().getCyclus());
		}
		return clipReturnList(afspraak, ret);
	}

	private List<Date> calculateDatesWeekStrategy(Afspraak afspraak)
	{
		LinkedList<Date> ret = new LinkedList<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(afspraak.getHerhalendeAfspraak().getBeginDatum());
		// make sure enough dates remain after clipping on begin and end date
		int count = -14;
		Set<AfspraakHerhalingDag> dagen = afspraak.getHerhalendeAfspraak().getDagenSet();
		dagen.remove(AfspraakHerhalingDag.DAG);
		dagen.remove(AfspraakHerhalingDag.WERKDAG);
		while (inRange(count, cal, afspraak))
		{
			for (AfspraakHerhalingDag curDag : dagen)
			{
				cal.set(Calendar.DAY_OF_WEEK, curDag.getCalendarDay());
				ret.add(cal.getTime());
				count++;
			}
			cal.add(Calendar.DAY_OF_YEAR, 7 * afspraak.getHerhalendeAfspraak().getCyclus());
		}
		return clipReturnList(afspraak, ret);
	}

	private List<Date> clipReturnList(Afspraak afspraak, LinkedList<Date> list)
	{
		// clip on begin date
		while (!list.isEmpty()
			&& list.getFirst().compareTo(afspraak.getHerhalendeAfspraak().getBeginDatum()) < 0)
			list.removeFirst();
		// clip on end date
		Date eindDatum = afspraak.getHerhalendeAfspraak().getEindDatum();
		while (eindDatum != null && !list.isEmpty() && list.getLast().compareTo(eindDatum) > 0)
			list.removeLast();
		// clip on max herhaling
		if (list.size() > afspraak.getHerhalendeAfspraak().getMaxHerhalingen())
			return list.subList(0, afspraak.getHerhalendeAfspraak().getMaxHerhalingen());
		return list;
	}

	private List<Date> calculateDatesWorkingDayStrategy(Afspraak afspraak)
	{
		List<Date> ret = new LinkedList<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(afspraak.getHerhalendeAfspraak().getBeginDatum());
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			cal.add(Calendar.DAY_OF_YEAR, 2);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			cal.add(Calendar.DAY_OF_YEAR, 1);
		int count = 0;
		while (inRange(count, cal, afspraak))
		{
			ret.add(cal.getTime());
			cal.add(Calendar.DAY_OF_YEAR, calculateWorkingDayIndex(cal.get(Calendar.DAY_OF_WEEK),
				afspraak.getHerhalendeAfspraak().getCyclus()));
			count++;
		}
		return ret;
	}

	private int calculateWorkingDayIndex(int currentDay, int offset)
	{
		int ret = currentDay + offset;
		if (currentDay == Calendar.SUNDAY)
			ret++;
		int prevWeekends = 0;
		int newWeekends = 0;
		do
		{
			ret += (newWeekends - prevWeekends) * 2;
			prevWeekends = newWeekends;
			newWeekends = ret / 7;
		}
		while (prevWeekends != newWeekends);
		return ret - currentDay;
	}

	private List<Date> calculateDatesDayStrategy(Afspraak afspraak)
	{
		List<Date> ret = new LinkedList<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(afspraak.getHerhalendeAfspraak().getBeginDatum());
		int count = 0;
		while (inRange(count, cal, afspraak))
		{
			ret.add(cal.getTime());
			cal.add(Calendar.DAY_OF_YEAR, afspraak.getHerhalendeAfspraak().getCyclus());
			count++;
		}
		return ret;
	}

	private boolean inRange(int count, Calendar cal, Afspraak afspraak)
	{
		HerhalendeAfspraak herhaling = afspraak.getHerhalendeAfspraak();
		if (herhaling.getEindDatum() != null
			&& herhaling.getEindDatum().compareTo(cal.getTime()) < 0)
			return false;
		if (herhaling.getMaxHerhalingen() != null && herhaling.getMaxHerhalingen() > count)
			return true;
		return false;
	}

	@Override
	public void getMaandOverzicht(ParticipatieAanwezigheidMaandZoekFilter filter,
			ParticipatieMaandOverzicht overzicht)
	{
		Deelnemer deelnemer = filter.getDeelnemer();
		Date beginDatum = overzicht.getMaand().getBegindatum();
		Date eindDatum = overzicht.getMaand().getEinddatum();
		eindDatum = TimeUtil.getInstance().setTimeOnDate(eindDatum, Time.valueOf("23:59"));
		AfspraakZoekFilter afspraakZoekFilter = new AfspraakZoekFilter();
		afspraakZoekFilter.setAuthorizationContext(filter.getAuthorizationContext());
		afspraakZoekFilter.setDeelnemer(deelnemer);
		afspraakZoekFilter.setBeginDatumTijd(beginDatum);
		afspraakZoekFilter.setEindDatumTijd(eindDatum);
		if (filter.getAlleenIIVOAfspraken())
			afspraakZoekFilter.setAantalMinutenIIVOGroterDan(0);
		overzicht.setAantalAfspraken(listCount(afspraakZoekFilter));
		overzicht.setTotaalUrenAfspraken(getTotaalUrenAfspraken(afspraakZoekFilter));
		afspraakZoekFilter.setPresentieRegistratieVerplicht(true);
		overzicht.setAanwezigheidVereistAfspraken(getTotaalUrenAfspraken(afspraakZoekFilter));
		afspraakZoekFilter.setPresentieRegistratieVerwerkt(false);
		overzicht.setOpenAfspraken(getTotaalUrenAfspraken(afspraakZoekFilter));
	}

	private BigDecimal getTotaalUrenAfspraken(AfspraakZoekFilter filter)
	{

		Criteria criteria = createCriteria(filter);
		List<Afspraak> afspraken = cachedTypedList(criteria);
		long seconds = 0;
		for (Afspraak afspraak : afspraken)
		{
			seconds +=
				(afspraak.getEindDatumTijd().getTime() - afspraak.getBeginDatumTijd().getTime());
		}
		BigDecimal totaal = new BigDecimal(seconds);
		BigDecimal uur = new BigDecimal(3600000);
		BigDecimal aantalUren = BigDecimal.ZERO;
		if (seconds > 0)
		{
			aantalUren = (totaal.divide(uur, 1, RoundingMode.HALF_UP));
		}
		return aantalUren;
	}

	@Override
	public List<Afspraak> getAfspraken(Groep groep, Date begintijd, Date eindtijd)
	{
		Asserts.assertNotNull("groep", groep);
		Asserts.assertNotNull("begintijd", begintijd);
		Asserts.assertNotNull("eindtijd", eindtijd);
		Criteria criteria = createCriteria(AfspraakParticipant.class);
		criteria.createAlias("afspraak", "afspraak");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("groep", groep);
		builder.addLessOrEquals("afspraak.beginDatumTijd", begintijd);
		builder.addGreaterOrEquals("afspraak.eindDatumTijd", eindtijd);

		criteria.setProjection(Projections.property("afspraak"));
		return cachedTypedList(criteria);
	}

	@Override
	public List<Afspraak> getAfspraken(Deelnemer deelnemer, Date begintijd, Date eindtijd)
	{
		Asserts.assertNotNull("deelnemer", deelnemer);
		Asserts.assertNotNull("begintijd", begintijd);
		Asserts.assertNotNull("eindtijd", eindtijd);
		Criteria criteria = createCriteria(Afspraak.class);
		criteria.createAlias("deelnemers", "deelnemers");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("deelnemers.deelnemer", deelnemer);
		builder.addLessOrEquals("beginDatumTijd", begintijd);
		builder.addGreaterOrEquals("eindDatumTijd", eindtijd);
		return cachedTypedList(criteria);
	}

	@Override
	public void verwijderAfspraken(List<Afspraak> afspraken, boolean removeWaarnemingen)
	{
		if (afspraken.isEmpty())
			return;

		// Verwijder alle afspraakparticipanten.
		String hql = "delete from AfspraakParticipant where afspraak in (:afspraken)";
		Query query = createQuery(hql, Afspraak.class);
		query.setParameterList("afspraken", afspraken);
		query.executeUpdate();
		// Verwijder alle afspraakbijlagen.
		hql = "delete from AfspraakBijlage where afspraak in (:afspraken)";
		query = createQuery(hql, Afspraak.class);
		query.setParameterList("afspraken", afspraken);
		query.executeUpdate();
		if (removeWaarnemingen)
		{
			// verwijder eventuele waarnemingen.
			hql = "delete from Waarneming where afspraak in (:afspraken)";
			query = createQuery(hql, Afspraak.class);
			query.setParameterList("afspraken", afspraken);
			query.executeUpdate();
		}
		else
		{
			// Loskoppel eventuele waarnemingen.
			hql = "update Waarneming set afspraak=null where afspraak in (:afspraken)";
			query = createQuery(hql, Afspraak.class);
			query.setParameterList("afspraken", afspraken);
			query.executeUpdate();
		}

		List<Long> ids = new ArrayList<Long>();
		for (Afspraak af : afspraken)
			ids.add(af.getId());

		// Verwijder de afspraken
		hql = "delete from Afspraak where id in (:afspraken)";
		query = createQuery(hql, Afspraak.class);
		query.setParameterList("afspraken", ids);
		query.executeUpdate();
		// Commit.
		batchExecute();
	}

	@Override
	public Afspraak getVolgendeAfspraak(Deelnemer deelnemer, Date after)
	{
		Asserts.assertNotNull("deelnemer", deelnemer);
		Asserts.assertNotNull("datum", after);
		Criteria criteria = createCriteria(Afspraak.class, "afspraak");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		criteria.createAlias("deelnemers", "deelnemers");
		builder.addEquals("deelnemers.deelnemer", deelnemer);
		builder.addGreaterThan("beginDatumTijd", after);
		criteria.createAlias("afspraakType", "afspraakType");
		builder.addNotEquals("afspraakType.category", AfspraakTypeCategory.BESCHERMD);
		builder.addNotEquals("afspraakType.category", AfspraakTypeCategory.PRIVE);
		builder.addNotEquals("afspraakType.category", AfspraakTypeCategory.EXTERN);
		criteria.addOrder(Order.asc("beginDatumTijd"));
		criteria.setMaxResults(1);
		return cachedUnique(criteria);
	}

	@Override
	public List<Afspraak> getAfspraken(Basisrooster basisrooster, Date beginDatumTijd,
			Date eindDatumTijd)
	{
		Asserts.assertNotNull("basisrooster", basisrooster);
		Asserts.assertNotNull("beginDatumTijd", beginDatumTijd);
		Asserts.assertNotNull("eindDatumTijd", eindDatumTijd);
		Criteria criteria = createCriteria(Afspraak.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("organisatie", basisrooster.getOrganisatie());
		builder.addEquals("basisrooster", basisrooster);
		builder.addGreaterOrEquals("eindDatumTijd", beginDatumTijd);
		builder.addLessOrEquals("beginDatumTijd", eindDatumTijd);

		return typedList(criteria, false);
	}

	/**
	 * Class die de totale iivo-tijd van een deelnemer representeert.
	 * 
	 * @author loite
	 */
	public static class IIVOTijd implements Serializable, DeelnemerProvider
	{
		private static final long serialVersionUID = 1L;

		private Integer deelnemernummer;

		private String roepnaam;

		private String voorvoegsel;

		private String achternaam;

		private String opleiding;

		private String organisatieEenheid;

		private String locatie;

		private long iivo;

		private long verbintenisId;

		private Date begindatum;

		private Date einddatum;

		private int dagen;

		public IIVOTijd()
		{
		}

		public String getOpleiding()
		{
			return opleiding;
		}

		public void setOpleiding(String opleiding)
		{
			this.opleiding = opleiding;
		}

		public long getIivo()
		{
			return iivo;
		}

		public void setIivo(long iivo)
		{
			this.iivo = iivo;
		}

		public Date getBegindatum()
		{
			return begindatum;
		}

		public void setBegindatum(Date begindatum)
		{
			this.begindatum = begindatum;
		}

		public Date getEinddatum()
		{
			return einddatum;
		}

		public void setEinddatum(Date einddatum)
		{
			this.einddatum = einddatum;
		}

		public int getDagen()
		{
			return dagen;
		}

		public void setDagen(int dagen)
		{
			this.dagen = dagen;
		}

		public void setVerbintenisId(long verbintenisId)
		{
			this.verbintenisId = verbintenisId;
		}

		public long getVerbintenisId()
		{
			return verbintenisId;
		}

		public void setLocatie(String locatie)
		{
			this.locatie = locatie;
		}

		public String getLocatie()
		{
			return locatie;
		}

		public Integer getDeelnemernummer()
		{
			return deelnemernummer;
		}

		public void setDeelnemernummer(Integer deelnemernummer)
		{
			this.deelnemernummer = deelnemernummer;
		}

		public String getRoepnaam()
		{
			return roepnaam;
		}

		public void setRoepnaam(String roepnaam)
		{
			this.roepnaam = roepnaam;
		}

		public String getVoorvoegsel()
		{
			return voorvoegsel;
		}

		public void setVoorvoegsel(String voorvoegsel)
		{
			this.voorvoegsel = voorvoegsel;
		}

		public String getAchternaam()
		{
			return achternaam;
		}

		public void setAchternaam(String achternaam)
		{
			this.achternaam = achternaam;
		}

		public String getOrganisatieEenheid()
		{
			return organisatieEenheid;
		}

		public void setOrganisatieEenheid(String tuple)
		{
			this.organisatieEenheid = tuple;
		}

		@Override
		public Deelnemer getDeelnemer()
		{
			return DataAccessRegistry.getHelper(DeelnemerDataAccessHelper.class)
				.getByDeelnemernummer(deelnemernummer);
		}

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<IIVOTijd> getIIVOTijd(IIVOTijdZoekFilter filter)
	{
		if (filter == null)
		{
			return Collections.emptyList();
		}
		if (filter.getAuthorizationContext() != null
			&& !filter.getAuthorizationContext().isInstellingClearance()
			&& filter.getAuthorizationContext().isOrganisatieEenheidClearance()
			&& filter.getAuthorizationContext().getAllowedElements().isEmpty())
		{
			return Collections.emptyList();
		}
		String sql =
			"select deelnemer.deelnemernummer,persoon.roepnaam,persoon.voorvoegsel,persoon.achternaam,opleiding.naam,"
				+ "organisatieeenheid.naam as organisatieeenheid, "
				+ "coalesce(iivo.iivo, 0) as iivo,"
				+ "verbintenis.id,greatest(:begindatum,verbintenis.begindatum) as begindatum, least(:einddatum,"
				+ "coalesce(verbintenis.einddatum,:einddatum)) as einddatum,least(:einddatum,"
				+ "coalesce(verbintenis.einddatum, :einddatum)) - greatest(:begindatum,"
				+ "verbintenis.begindatum) as dagen  "
				+ "from deelnemer "
				+ "inner join persoon persoon on persoon.id = deelnemer.persoon and deelnemer.organisatie = :organisatie "
				+ "inner join verbintenis verbintenis on verbintenis.deelnemer = deelnemer.id and verbintenis.organisatie = :organisatie "
				+ "inner join opleiding opleiding on opleiding.id = verbintenis.opleiding "
				+ "inner join organisatieeenheid organisatieeenheid on organisatieeenheid.id = verbintenis.organisatieeenheid "
				+ "left outer join "
				+ "( "
				+ "  select deelnemer, sum(minuteniivo) as iivo "
				+ "  from afspraakdeelnemer "
				+ "  inner join afspraak on afspraakdeelnemer.afspraak = afspraak.id "
				+ "  where uitnodigingstatus in ('DIRECTE_PLAATSING', 'GEACCEPTEERD', 'INGETEKEND') "
				+ "  and afspraakdeelnemer.organisatie = :organisatie "
				+ "  and afspraak.begindatumtijd>=:begindatum "
				+ "  and afspraak.einddatumtijd<=:einddatum "
				+ "  group by deelnemer "
				+ ") iivo on verbintenis.deelnemer = iivo.deelnemer "
				+ "where verbintenis.organisatie = :organisatie "
				+ "and (verbintenis.einddatumNotNull >= :begindatum ) "
				+ "and verbintenis.begindatum<=:einddatum ";
		if (filter.getOpleiding() != null)
			sql = sql + "and opleiding.id=:opleiding ";
		if (filter.getOrganisatieEenheid() != null)
			sql = sql + "and organisatieeenheid.id in (:organisatieEenheden) ";
		else if (filter.getAuthorizationContext() != null
			&& !filter.getAuthorizationContext().isInstellingClearance()
			&& filter.getAuthorizationContext().isOrganisatieEenheidClearance())
		{
			sql = sql + "and organisatieeenheid.id in (:organisatieEenheden) ";
		}
		SQLQuery query = createSQLQuery(sql);
		query.setDate("begindatum", filter.getBegindatum());
		query.setDate("einddatum", filter.getEinddatum());
		query.setLong("organisatie", EduArteContext.get().getInstelling().getId());
		if (filter.getOpleiding() != null)
			query.setLong("opleiding", filter.getOpleiding().getId());
		if (filter.getOrganisatieEenheid() != null
			|| (filter.getAuthorizationContext() != null
				&& !filter.getAuthorizationContext().isInstellingClearance() && filter
				.getAuthorizationContext().isOrganisatieEenheidClearance()))
		{
			List<OrganisatieEenheid> list;
			if (filter.getOrganisatieEenheid() != null)
				list = filter.getGeselecteerdeOrganisatieEenheden();
			else
			{
				list = filter.getGeselecteerdeOrganisatieEenheden();
			}
			List<Long> ids = new ArrayList<Long>(list.size());
			for (OrganisatieEenheid ehd : list)
				ids.add(ehd.getId());
			query.setParameterList("organisatieEenheden", ids);
		}
		query.setResultTransformer(new ResultTransformer()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public List transformList(List collection)
			{
				return collection;
			}

			@Override
			public Object transformTuple(Object[] tuple, String[] aliases)
			{

				IIVOTijd res = new IIVOTijd();

				res.setDeelnemernummer(((BigDecimal) tuple[0]).intValue());
				res.setRoepnaam((String) tuple[1]);
				res.setVoorvoegsel((String) tuple[2]);
				res.setAchternaam((String) tuple[3]);
				res.setOpleiding((String) tuple[4]);
				res.setOrganisatieEenheid((String) tuple[5]);

				res.setIivo(((BigDecimal) tuple[6]).longValue() / 60);
				res.setVerbintenisId(((BigDecimal) tuple[7]).longValue());
				res.setBegindatum((Date) tuple[8]);
				res.setEinddatum((Date) tuple[9]);
				res.setDagen(((BigDecimal) tuple[10]).intValue());
				return res;
			}

		});
		return query.list();
	}

	@Override
	public AfspraakDeelnemer getAfspraakDeelnemer(Afspraak afspraak, Deelnemer deelnemer)
	{
		Asserts.assertNotNull("deelnemer", deelnemer);
		Asserts.assertNotNull("afspraak", afspraak);
		Criteria criteria = createCriteria(AfspraakDeelnemer.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("deelnemer", deelnemer);
		builder.addEquals("afspraak", afspraak);
		criteria.setMaxResults(1);
		return (AfspraakDeelnemer) uncachedUnique(criteria);
	}

	@Override
	public List<Afspraak> getOverlappendeAfspraken(Deelnemer deelnemer, Date begintijd,
			Date eindtijd)
	{
		Asserts.assertNotNull("deelnemer", deelnemer);
		Asserts.assertNotNull("begintijd", begintijd);
		Asserts.assertNotNull("eindtijd", eindtijd);
		Criteria criteria = createCriteria(Afspraak.class);
		criteria.createAlias("deelnemers", "deelnemers");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("deelnemers.deelnemer", deelnemer);

		builder.addOrs(Restrictions.and(Restrictions.le("beginDatumTijd", begintijd), Restrictions
			.ge("eindDatumTijd", begintijd)),

		Restrictions.and(Restrictions.le("beginDatumTijd", eindtijd), Restrictions.ge(
			"eindDatumTijd", eindtijd)),

		Restrictions.and(Restrictions.ge("beginDatumTijd", begintijd), Restrictions.le(
			"eindDatumTijd", eindtijd)));

		criteria.addOrder(Order.asc("beginDatumTijd"));
		// builder.addLessOrEquals("beginDatumTijd", begintijd);
		// builder.addGreaterOrEquals("eindDatumTijd", eindtijd);

		return cachedTypedList(criteria);
	}

	@Override
	public List<Afspraak> getRoosterAfsprakenGekoppeldAanGroepGesorteerdOpBeginTijd(
			OrganisatieEenheid organisatieEenheid, Locatie locatie, Date peilDatum)
	{
		Asserts.assertNotNull("organisatieEenheid", organisatieEenheid);
		Asserts.assertNotNull("peildatum", peilDatum);

		Criteria criteria = createCriteria(Afspraak.class);
		criteria.addOrder(Order.asc("beginDatumTijd"));
		criteria.createAlias("afspraakType", "afspraakType");
		criteria.createAlias("participanten", "participanten");
		criteria.createAlias("participanten.groep", "groep");

		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("afspraakType.category", AfspraakTypeCategory.ROOSTER);

		// Als locatie ingevuld is, dan alleen afspraken selecteren waarvan de groepen
		// direct aan gegeven org.enh/locatie gekoppeld zijn.
		if (locatie != null)
		{
			builder.addEquals("groep.organisatieEenheid", organisatieEenheid);
			builder.addEquals("groep.locatie", locatie);
		}
		else
		{
			// Geen locatie ingevuld, ook alle afspraken met groepen gekoppelend aan
			// onderliggende org.enhden selecteren.
			List<Criterion> orgEnhCriteria = new ArrayList<Criterion>();
			for (OrganisatieEenheid orgEnh : organisatieEenheid.getActieveChildren(peilDatum))
			{
				Criterion orgEhdCrit = Restrictions.eq("groep.organisatieEenheid", orgEnh);
				orgEnhCriteria.add(orgEhdCrit);
			}
			builder.addOrs(orgEnhCriteria);
		}

		Date eindDatum = TimeUtil.getInstance().addDays(peilDatum, 1);
		builder.addGreaterOrEquals("beginDatumTijd", peilDatum);
		builder.addLessThan("eindDatumTijd", eindDatum);

		return cachedTypedList(criteria);
	}

	@Override
	public Afspraak getRoosterAfspraakBijDocent(OrganisatieEenheid organisatieEenheid,
			Date peilDatum, int beginLesuur, int eindLesuur, Long docentId)
	{
		Asserts.assertNotNull("organisatieEenheid", organisatieEenheid);
		Asserts.assertNotNull("peildatum", peilDatum);
		Asserts.assertNotNull("docentid", docentId);

		Criteria criteria = createCriteria(AfspraakParticipant.class);
		criteria.createAlias("medewerker", "medewerker");
		criteria.createAlias("afspraak", "afspraak");
		criteria.createAlias("afspraak.afspraakType", "afspraakType");

		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("afspraakType.category", AfspraakTypeCategory.ROOSTER);

		builder.addEquals("afspraak.organisatieEenheid", organisatieEenheid);
		builder.addEquals("medewerker.id", docentId);

		builder.addGreaterOrEquals("afspraak.beginDatumTijd", peilDatum);
		builder.addGreaterOrEquals("afspraak.eindDatumTijd", peilDatum);

		builder.addNullOrGreaterOrEquals("afspraak.eindLesuur", eindLesuur);
		builder.addNullOrLessOrEquals("afspraak.beginLesuur", beginLesuur);

		criteria.setProjection(Projections.property("afspraak"));

		criteria.setMaxResults(1);

		return cachedUnique(criteria);
	}
}
