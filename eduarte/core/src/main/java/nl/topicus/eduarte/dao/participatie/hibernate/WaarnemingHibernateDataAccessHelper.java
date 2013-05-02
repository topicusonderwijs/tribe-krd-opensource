package nl.topicus.eduarte.dao.participatie.hibernate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.entities.Time;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.GroepDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.WaarnemingDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.AfspraakDeelnemer;
import nl.topicus.eduarte.entities.participatie.ParticipatieMaandOverzicht;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingSoort;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.participatie.zoekfilters.ParticipatieAanwezigheidMaandZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.*;

public class WaarnemingHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Waarneming, WaarnemingZoekFilter> implements
		WaarnemingDataAccessHelper
{

	public WaarnemingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(WaarnemingZoekFilter filter)
	{
		Criteria criteria = createCriteria(Waarneming.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		if (filter.getGroep() != null)
		{
			Groep groep =
				DataAccessRegistry.getHelper(GroepDataAccessHelper.class).get(Groep.class,
					filter.getGroep().getId());
			builder.addIn("deelnemer", groep.getDeelnemersOpPeildatum());
		}
		builder.addEquals("deelnemer", filter.getDeelnemer());
		builder.addEquals("waarnemingSoort", filter.getWaarnemingSoort());
		if (filter.getZonderNvtWaarnemingen() != null && filter.getZonderNvtWaarnemingen())
		{
			builder.addNotEquals("waarnemingSoort", WaarnemingSoort.Nvt);
		}
		if (filter.getDatumTijdExactGelijk() != null && filter.getDatumTijdExactGelijk())
		{
			builder.addEquals("beginDatumTijd", filter.getBeginDatumTijd());
			builder.addEquals("eindDatumTijd", filter.getEindDatumTijd());
		}
		else
		{
			builder.addLessOrEquals("beginDatumTijd", filter.getEindDatumTijd());
			builder.addGreaterOrEquals("eindDatumTijd", filter.getBeginDatumTijd());
		}
		builder.addEquals("afgehandeld", filter.getAfgehandeld());
		builder.addIsNull("absentieMelding", filter.getZonderAbsentieMelding());

		if (filter.isAlleenOngeoorloofd())
		{
			criteria.createAlias("absentieMelding", "absentieMelding",
				CriteriaSpecification.LEFT_JOIN);
			criteria.createAlias("absentieMelding.absentieReden", "absentieReden",
				CriteriaSpecification.LEFT_JOIN);

			SimpleExpression redenOngeoorloofd =
				Restrictions.eq("absentieReden.geoorloofd", Boolean.FALSE);
			Criterion meldingNull = Restrictions.isNull("absentieMelding");
			SimpleExpression waarnemingSoort =
				Restrictions.eq("waarnemingSoort", WaarnemingSoort.Afwezig);
			LogicalExpression ongeoorloofd =
				Restrictions.or(redenOngeoorloofd, Restrictions.and(meldingNull, waarnemingSoort));

			criteria.add(ongeoorloofd);
		}

		if (filter.getAfspraakZoekFilter() != null)
		{
			criteria.createAlias("afspraak", "afspraak");
			builder.addEquals("afspraak.afspraakType", filter.getAfspraakZoekFilter()
				.getAfspraakType());
		}
		else if (filter.getAfspraakOngelijkAan() != null)
		{
			criteria.createAlias("afspraak", "afspraak");
			builder.addNotEquals("afspraak", filter.getAfspraakOngelijkAan());
		}

		return criteria;
	}

	@Override
	public Waarneming getById(Long id)
	{
		Criteria criteria = createCriteria(Waarneming.class);
		criteria.add(Restrictions.eq("id", id));
		return cachedTypedUnique(criteria);
	}

	@Override
	public List<Waarneming> getOverlappendeWaarnemingen(WaarnemingZoekFilter waarnemingZoekFilter)
	{
		WaarnemingZoekFilter filter =
			castZoekFilter(waarnemingZoekFilter, WaarnemingZoekFilter.class);
		Criteria criteria = createCriteria(Waarneming.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		if (filter.getGroep() != null)
		{
			Groep groep =
				DataAccessRegistry.getHelper(GroepDataAccessHelper.class).get(Groep.class,
					filter.getGroep().getId());
			builder.addIn("deelnemer", groep.getDeelnemersOpPeildatum());
		}
		if (filter.getAfspraak() != null)
		{
			List<Deelnemer> deelnemers = new ArrayList<Deelnemer>();
			for (AfspraakDeelnemer afspraakDeelnemer : filter.getAfspraak().getDeelnemers())
			{
				deelnemers.add(afspraakDeelnemer.getDeelnemer());
			}
			builder.addIn("deelnemer", deelnemers);
		}
		if (filter.getContract() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(AfspraakDeelnemer.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("contract", filter.getContract());
			dcBuilder.addEquals("deelnemer", filter.getDeelnemer());
			dc.setProjection(Projections.property("afspraak"));
			criteria.add(Subqueries.propertyIn("afspraak", dc));
			filter.setResultCacheable(false);
		}
		builder.addEquals("deelnemer", filter.getDeelnemer());
		builder.addEquals("waarnemingSoort", filter.getWaarnemingSoort());
		if (filter.getZonderNvtWaarnemingen() != null && filter.getZonderNvtWaarnemingen())
		{
			builder.addNotEquals("waarnemingSoort", WaarnemingSoort.Nvt);
		}
		builder.addIsNull("absentieMelding", filter.getZonderAbsentieMelding());
		builder.addLessThan("beginDatumTijd", filter.getEindDatumTijd());
		builder.addGreaterThan("eindDatumTijd", filter.getBeginDatumTijd());

		if (filter.getAfspraakOngelijkAan() != null)
			builder.addNotEquals("afspraak", filter.getAfspraakOngelijkAan());

		criteria.add(Restrictions.or(Restrictions.or(Restrictions.and(
			Restrictions.gt("beginDatumTijd", waarnemingZoekFilter.getBeginDatumTijd()),
			Restrictions.lt("beginDatumTijd", waarnemingZoekFilter.getEindDatumTijd())),
			Restrictions.and(
				Restrictions.gt("eindDatumTijd", waarnemingZoekFilter.getBeginDatumTijd()),
				Restrictions.lt("eindDatumTijd", waarnemingZoekFilter.getEindDatumTijd()))),
			Restrictions.or(Restrictions.and(
				Restrictions.lt("beginDatumTijd", waarnemingZoekFilter.getBeginDatumTijd()),
				Restrictions.gt("eindDatumTijd", waarnemingZoekFilter.getBeginDatumTijd())),
				Restrictions.and(
					Restrictions.lt("beginDatumTijd", waarnemingZoekFilter.getEindDatumTijd()),
					Restrictions.gt("eindDatumTijd", waarnemingZoekFilter.getEindDatumTijd())))));

		return cachedTypedList(criteria);
	}

	@Override
	public List<Waarneming> getWaarnemingenVanDag(WaarnemingZoekFilter waarnemingZoekFilter)
	{
		WaarnemingZoekFilter filter =
			castZoekFilter(waarnemingZoekFilter, WaarnemingZoekFilter.class);
		Criteria criteria = createCriteria(Waarneming.class);
		criteria.addOrder(Order.desc("beginDatumTijd"));
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("deelnemer", filter.getDeelnemer());
		builder.addEquals("waarnemingSoort", filter.getWaarnemingSoort());
		if (filter.getZonderNvtWaarnemingen() != null && filter.getZonderNvtWaarnemingen())
		{
			builder.addNotEquals("waarnemingSoort", WaarnemingSoort.Nvt);
		}
		builder.addEquals("afgehandeld", filter.getAfgehandeld());
		builder.addIsNull("absentieMelding", filter.getZonderAbsentieMelding());

		Date beginDag =
			TimeUtil.getInstance().setTimeOnDate(filter.getBeginDatumTijd(), Time.valueOf("00:00"));
		Date eindeDag =
			TimeUtil.getInstance().setTimeOnDate(filter.getBeginDatumTijd(), Time.valueOf("23:59"));

		builder.addGreaterOrEquals("beginDatumTijd", beginDag);
		builder.addLessOrEquals("eindDatumTijd", eindeDag);

		return typedList(criteria, false);
	}

	@Override
	public List<Waarneming> getAlleWaarnemingenVanTot(WaarnemingZoekFilter waarnemingZoekFilter)
	{
		WaarnemingZoekFilter filter =
			castZoekFilter(waarnemingZoekFilter, WaarnemingZoekFilter.class);
		Criteria criteria = createCriteria(Waarneming.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addEquals("absentieMelding", filter.getAbsentieMelding());
		builder.addEquals("deelnemer", filter.getDeelnemer());
		builder.addEquals("waarnemingSoort", filter.getWaarnemingSoort());
		if (filter.getZonderNvtWaarnemingen() != null && filter.getZonderNvtWaarnemingen())
		{
			builder.addNotEquals("waarnemingSoort", WaarnemingSoort.Nvt);
		}
		builder.addGreaterOrEquals("beginDatumTijd", filter.getBeginDatumTijd());
		builder.addLessOrEquals("eindDatumTijd", filter.getEindDatumTijd());
		builder.addEquals("afgehandeld", filter.getAfgehandeld());
		builder.addIsNull("absentieMelding", filter.getZonderAbsentieMelding());
		return typedList(criteria, true);
	}

	@Override
	public List<Waarneming> getAlleWaarnemingenGroterOfGelijk(
			WaarnemingZoekFilter waarnemingZoekFilter)
	{
		WaarnemingZoekFilter filter =
			castZoekFilter(waarnemingZoekFilter, WaarnemingZoekFilter.class);
		Criteria criteria = createCriteria(Waarneming.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("absentieMelding", filter.getAbsentieMelding());
		builder.addEquals("deelnemer", filter.getDeelnemer());
		builder.addEquals("waarnemingSoort", filter.getWaarnemingSoort());
		if (filter.getZonderNvtWaarnemingen() != null && filter.getZonderNvtWaarnemingen())
		{
			builder.addNotEquals("waarnemingSoort", WaarnemingSoort.Nvt);
		}
		builder.addLessOrEquals("beginDatumTijd", filter.getBeginDatumTijd());
		builder.addGreaterOrEquals("eindDatumTijd", filter.getEindDatumTijd());
		builder.addEquals("afgehandeld", filter.getAfgehandeld());
		builder.addIsNull("absentieMelding", filter.getZonderAbsentieMelding());
		return typedList(criteria, true);
	}

	@Override
	public List<Waarneming> getWaarnemingenOverlapEnGelijk(WaarnemingZoekFilter waarnemingZoekFilter)
	{

		WaarnemingZoekFilter filter =
			castZoekFilter(waarnemingZoekFilter, WaarnemingZoekFilter.class);
		Criteria criteria = createCriteria(Waarneming.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		if (filter.getGroep() != null)
		{
			Groep groep =
				DataAccessRegistry.getHelper(GroepDataAccessHelper.class).get(Groep.class,
					filter.getGroep().getId());
			builder.addIn("deelnemer", groep.getDeelnemersOpPeildatum());
		}
		if (filter.getContract() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(AfspraakDeelnemer.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("contract", filter.getContract());
			dcBuilder.addEquals("deelnemer", filter.getDeelnemer());
			dc.setProjection(Projections.property("afspraak"));
			criteria.add(Subqueries.propertyIn("afspraak", dc));
			filter.setResultCacheable(false);
		}
		builder.addEquals("deelnemer", filter.getDeelnemer());
		builder.addEquals("waarnemingSoort", filter.getWaarnemingSoort());
		if (filter.getZonderNvtWaarnemingen() != null && filter.getZonderNvtWaarnemingen())
		{
			builder.addNotEquals("waarnemingSoort", WaarnemingSoort.Nvt);
		}
		builder.addIsNull("absentieMelding", filter.getZonderAbsentieMelding());

		// Dit lijkt misschien verkeerd om maar het klopt wel
		builder.addLessThan("beginDatumTijd", filter.getEindDatumTijd());
		builder.addGreaterThan("eindDatumTijd", filter.getBeginDatumTijd());
		return typedList(criteria, false);
	}

	@Override
	public void getMaandOverzicht(ParticipatieAanwezigheidMaandZoekFilter filter,
			ParticipatieMaandOverzicht overzicht)
	{
		Deelnemer deelnemer = filter.getDeelnemer();
		Date beginDatum = overzicht.getMaand().getBegindatum();
		Date eindDatum = overzicht.getMaand().getEinddatum();
		eindDatum = TimeUtil.getInstance().setTimeOnDate(eindDatum, Time.valueOf("23:59"));
		overzicht.setUrenAbsentieWaarnemingen(getAantalUren(deelnemer, beginDatum, eindDatum,
			WaarnemingSoort.Afwezig, filter.getAlleenIIVOAfspraken()));
		overzicht.setUrenPresentieWaarnemingen(getAantalUren(deelnemer, beginDatum, eindDatum,
			WaarnemingSoort.Aanwezig, filter.getAlleenIIVOAfspraken()));
	}

	private BigDecimal getAantalUren(Deelnemer deelnemer, Date beginDatum, Date eindDatum,
			WaarnemingSoort soort, Boolean alleenIIVOAfspraken)
	{
		WaarnemingZoekFilter filter = new WaarnemingZoekFilter();
		filter.setDeelnemer(deelnemer);
		filter.setBeginDatumTijd(beginDatum);
		filter.setEindDatumTijd(eindDatum);
		filter.setWaarnemingSoort(soort);
		List<Waarneming> waarnemingen = new ArrayList<Waarneming>();
		waarnemingen = getOverlappendeWaarnemingen(filter);
		long seconds = 0;
		for (Waarneming waarneming : waarnemingen)
		{
			if (!alleenIIVOAfspraken
				|| (waarneming.getAfspraak() != null && waarneming.getAfspraak().getMinutenIIVO() > 0))
				seconds +=
					(waarneming.getEindDatumTijd().getTime() - waarneming.getBeginDatumTijd()
						.getTime());
		}
		BigDecimal totaal = new BigDecimal(seconds);
		BigDecimal uur = new BigDecimal(3600000);
		BigDecimal aantalUrenAfwezigWaarneming = BigDecimal.ZERO;
		if (seconds > 0)
		{
			aantalUrenAfwezigWaarneming = (totaal.divide(uur, 2, RoundingMode.HALF_UP));
		}
		return aantalUrenAfwezigWaarneming;
	}

	@Override
	public long getAantalWaarnemingen()
	{
		Criteria criteria = createCriteria(Waarneming.class);
		criteria.setProjection(Projections.rowCount());
		return (Long) uncachedUnique(criteria);
	}

	@Override
	public List<Waarneming> getByAfspraak(Afspraak afspraak, Instelling instelling)
	{
		Criteria criteria = createCriteria(Waarneming.class);
		criteria.add(Restrictions.eq("afspraak", afspraak)).add(
			Restrictions.eq("organisatie", instelling));
		return cachedList(criteria);
	}

	@Override
	public List<Waarneming> getByDeelnemerAfspraak(Deelnemer deelnemer, Afspraak afspraak,
			Instelling instelling)
	{
		Criteria criteria = createCriteria(Waarneming.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("deelnemer", deelnemer);
		builder.addEquals("afspraak", afspraak);
		builder.addEquals("organisatie", instelling);
		return cachedList(criteria);
	}
}
