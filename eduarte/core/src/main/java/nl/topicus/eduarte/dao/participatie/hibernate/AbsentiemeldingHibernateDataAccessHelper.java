/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.participatie.hibernate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.entities.Time;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.zoekfilters.NullFilter;
import nl.topicus.eduarte.dao.participatie.helpers.AbsentieMeldingDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.participatie.AbsentieMelding;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.ParticipatieMaandOverzicht;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.participatie.zoekfilters.AbsentieMeldingZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.ParticipatieAanwezigheidMaandZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class AbsentiemeldingHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<AbsentieMelding, AbsentieMeldingZoekFilter> implements
		AbsentieMeldingDataAccessHelper
{
	private static final NumberFormat FORMAT = NumberFormat.getNumberInstance();
	static
	{
		FORMAT.setMinimumFractionDigits(1);
		FORMAT.setMaximumFractionDigits(1);
	}

	public AbsentiemeldingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(AbsentieMeldingZoekFilter filter)
	{
		Criteria criteria = createCriteria(AbsentieMelding.class);
		criteria.createAlias("deelnemer", "deelnemer");
		criteria.createAlias("deelnemer.persoon", "persoon");

		addCriteria(criteria, filter);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		if (filter.isBeginEindtijdInclusief())
		{
			builder.addLessOrEquals("beginDatumTijd", filter.getEindDatumTijd());
			builder.addNullOrGreaterOrEquals("eindDatumTijd", filter.getBeginDatumTijd());
		}
		else
		{
			builder.addLessThan("beginDatumTijd", filter.getEindDatumTijd());
			builder.addNullOrGreaterThan("eindDatumTijd", filter.getBeginDatumTijd());
		}

		addVerbintenisGroepCriteria(criteria, filter);

		return criteria;
	}

	@Override
	public void addCriteria(Criteria criteria, AbsentieMeldingZoekFilter filter)
	{
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("deelnemer", filter.getDeelnemer());
		builder.addEquals("afgehandeld", filter.isAfgehandeld());
		builder.createAlias("absentieReden", "absentieReden");
		builder.addEquals("absentieReden.organisatieEenheid", filter.getOrganisatieEenheid());
		builder.addEquals("absentieReden", filter.getAbsentieReden());
		builder.addIn("deelnemer", filter.getDeelnemersList());
	}

	public void addVerbintenisGroepCriteria(Criteria criteria, AbsentieMeldingZoekFilter filter)
	{
		if (filter.getLocatie() != null || filter.getOrganisatieEenheid() != null
			|| filter.getOpleiding() != null)
		{
			DetachedCriteria dcVerbintenis = createDetachedCriteria(Verbintenis.class);
			dcVerbintenis.setProjection(Projections.property("deelnemer"));
			DetachedCriteriaBuilder dcBuilderVerbintenis =
				new DetachedCriteriaBuilder(dcVerbintenis);

			dcBuilderVerbintenis.addEquals("locatie", filter.getLocatie());
			dcBuilderVerbintenis.addEquals("organisatieEenheid", filter.getOrganisatieEenheid());
			dcBuilderVerbintenis.addEquals("opleiding", filter.getOpleiding());

			criteria.add(Subqueries.propertyIn("deelnemer", dcVerbintenis));
		}

		if (filter.getGroep() != null)
		{
			DetachedCriteria dcGroep = createDetachedCriteria(Groepsdeelname.class);
			dcGroep.setProjection(Projections.property("deelnemer"));
			DetachedCriteriaBuilder dcBuilderGroep = new DetachedCriteriaBuilder(dcGroep);

			dcBuilderGroep.addEquals("groep", filter.getGroep());

			criteria.add(Subqueries.propertyIn("deelnemer", dcGroep));
		}

	}

	@Override
	public String getDagenEnUren(AbsentieMeldingZoekFilter filter, boolean metAantal)
	{
		int dagen = getAantalDagen(filter);
		BigDecimal aantalUren = getAantalUren(filter);
		String dagenEnUren = "";
		if (dagen == 1)
			dagenEnUren += "1 dag";
		if (dagen > 1)
			dagenEnUren += dagen + " dagen";
		if (aantalUren != BigDecimal.ZERO && dagen > 0)
			dagenEnUren += ", ";
		if (aantalUren != BigDecimal.ZERO)
			dagenEnUren += FORMAT.format(aantalUren) + " uur";
		if (metAantal)
		{
			int aantal = getAantalMeldingen(filter);
			if (aantal != 0)
				dagenEnUren += " (" + aantal + ")";
		}
		return dagenEnUren;
	}

	private BigDecimal getAantalUren(AbsentieMeldingZoekFilter filter)
	{
		List<AbsentieMelding> absentieMeldingen = getOverlappendeMeldingen(filter);
		long seconds = 0;
		for (AbsentieMelding absentieMelding : absentieMeldingen)
		{
			// De melding is een voor een aantal uur van 1 dag
			if (!absentieMelding.isVoorHeleDagen() && absentieMelding.getEindDatumTijd() != null)
			{
				seconds +=
					(absentieMelding.getEindDatumTijd().getTime() - absentieMelding
						.getBeginDatumTijd().getTime());
			}
		}
		BigDecimal totaal = new BigDecimal(seconds);
		BigDecimal uur = new BigDecimal(3600000);
		BigDecimal aantalUren = BigDecimal.ZERO;
		if (seconds > 0)
		{
			aantalUren = (totaal.divide(uur, 2, RoundingMode.HALF_UP));
		}
		return aantalUren;
	}

	private int getAantalDagen(AbsentieMeldingZoekFilter filter)
	{
		List<AbsentieMelding> absentieMeldingen = getOverlappendeMeldingen(filter);
		int dagen = 0;
		for (AbsentieMelding absentieMelding : absentieMeldingen)
		{
			// Als de absentiemelding voor een aantal volledige dagen is
			if (absentieMelding.isVoorHeleDagen())
			{
				dagen +=
					TimeUtil.getInstance().getDifferenceInWorkDays(
						absentieMelding.getBeginDatumTijd(), absentieMelding.getEindDatumTijd());
				dagen += 1; // altijd een bijoptellen
			}
			// De melding heeft geen einddatum tel alle dagen tot de huidige
			else if (absentieMelding.getEindDatumTijd() == null)
			{
				dagen +=
					TimeUtil.getInstance().getDifferenceInWorkDays(
						absentieMelding.getBeginDatumTijd(), TimeUtil.getInstance().currentDate());
			}
		}
		return dagen;
	}

	@Override
	public AbsentieMelding zonderEindDatum(AbsentieMeldingZoekFilter filter)
	{
		Criteria criteria = createCriteria(AbsentieMelding.class);
		addCriteria(criteria, filter);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addNullFilterExpression("eindDatumTijd", NullFilter.IsNull);
		return cachedTypedUnique(criteria);
	}

	@Override
	public AbsentieMelding meldingVoorDatum(AbsentieMeldingZoekFilter filter)
	{
		Criteria criteria = createCriteria(AbsentieMelding.class);
		addCriteria(criteria, filter);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addGreaterOrEquals("beginDatumTijd", filter.getBeginDatumTijd());
		builder.addNullOrLessOrEquals("eindDatumTijd", filter.getEindDatumTijd());
		if (cachedTypedList(criteria) != null && cachedTypedList(criteria).size() > 0)
			return cachedTypedList(criteria).get(0);
		return null;
	}

	@Override
	public int getAantalMeldingen(AbsentieMeldingZoekFilter filter)
	{
		Criteria criteria = createCriteria(AbsentieMelding.class);
		addCriteria(criteria, filter);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addGreaterOrEquals("beginDatumTijd", filter.getBeginDatumTijd());
		builder.addLessOrEquals("beginDatumTijd", filter.getEindDatumTijd());
		criteria.setProjection(Projections.rowCount());
		return ((Long) uncachedUnique(criteria)).intValue();
	}

	@Override
	public AbsentieMelding getLaatstToegevoegdeMelding(AbsentieMeldingZoekFilter filter)
	{
		Criteria criteria = createCriteria(AbsentieMelding.class);
		addCriteria(criteria, filter);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addGreaterOrEquals("beginDatumTijd", filter.getBeginDatumTijd());
		builder.addLessOrEquals("beginDatumTijd", filter.getEindDatumTijd());
		criteria.addOrder(Order.desc("beginDatumTijd"));
		criteria.setMaxResults(1);
		return cachedTypedUnique(criteria);
	}

	@Override
	public List<AbsentieMelding> getOverlappendeMeldingen(AbsentieMeldingZoekFilter filter)
	{
		Criteria criteria = createCriteria(AbsentieMelding.class);
		addCriteria(criteria, filter);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		if (filter.isBeginEindtijdInclusief())
		{
			builder.addLessOrEquals("beginDatumTijd", filter.getEindDatumTijd());
			builder.addNullOrGreaterOrEquals("eindDatumTijd", filter.getBeginDatumTijd());
		}
		else
		{
			builder.addLessThan("beginDatumTijd", filter.getEindDatumTijd());
			builder.addNullOrGreaterThan("eindDatumTijd", filter.getBeginDatumTijd());
		}
		return cachedTypedList(criteria);
	}

	@Override
	public void getMaandOverzicht(ParticipatieAanwezigheidMaandZoekFilter filter,
			ParticipatieMaandOverzicht overzicht)
	{
		Deelnemer deelnemer = filter.getDeelnemer();
		Date beginDatum = overzicht.getMaand().getBegindatum();
		Date eindDatum = overzicht.getMaand().getEinddatum();
		eindDatum = TimeUtil.getInstance().setTimeOnDate(eindDatum, Time.valueOf("23:59"));
		AbsentieMeldingZoekFilter absentieMeldingZoekFilter = new AbsentieMeldingZoekFilter();
		absentieMeldingZoekFilter.setDeelnemer(deelnemer);
		absentieMeldingZoekFilter.setBeginDatumTijd(beginDatum);
		absentieMeldingZoekFilter.setEindDatumTijd(eindDatum);
		overzicht.setDagenAbsentiemeldingen(getAantalDagen(absentieMeldingZoekFilter));
		overzicht.setUrenAbsentiemeldingen(getAantalUren(absentieMeldingZoekFilter).intValue());
		overzicht.setAantalAbsentiemeldingen(listCount(absentieMeldingZoekFilter));
	}

	@Override
	public Map<Deelnemer, AbsentieMelding> makeMap(List<AbsentieMelding> meldingen)
	{
		Asserts.assertNotNull("meldingen", meldingen);
		Map<Deelnemer, AbsentieMelding> map =
			new HashMap<Deelnemer, AbsentieMelding>(meldingen.size());
		for (AbsentieMelding melding : meldingen)
		{
			map.put(melding.getDeelnemer(), melding);
		}
		return map;
	}

	@Override
	public boolean heeftAbsentieMelding(Deelnemer deelnemer, Afspraak afspraak)
	{
		Asserts.assertNotNull("deelnemer", deelnemer);
		Asserts.assertNotNull("afspraak", afspraak);

		Criteria criteria = createCriteria(AbsentieMelding.class);

		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("deelnemer", deelnemer);
		builder.addLessOrEquals("beginDatumTijd", afspraak.getBeginDatumTijd());
		builder.addGreaterOrEquals("eindDatumTijd", afspraak.getEindDatumTijd());

		builder.addOrs(Restrictions.isNull("eindLesuur"), Restrictions.ge("eindLesuur", afspraak
			.getEindLesuur()));
		builder.addOrs(Restrictions.or(Restrictions.isNull("eindLesuur"), Restrictions.ge(
			"eindLesuur", afspraak.getEindLesuur())));

		criteria.setProjection(Projections.rowCount());

		Long aantal = (Long) unique(criteria, false);

		return aantal > 0;
	}
}
