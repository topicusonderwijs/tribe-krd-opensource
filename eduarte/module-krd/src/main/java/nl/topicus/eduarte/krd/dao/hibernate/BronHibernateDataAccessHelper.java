package nl.topicus.eduarte.krd.dao.hibernate;

import static org.hibernate.criterion.CriteriaSpecification.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.BronMeldingOnderdeel;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.dao.helpers.BronSchooljaarStatusDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.BronSchooljaarStatus;
import nl.topicus.eduarte.krd.entities.bron.BronStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.AbstractBronBatchVO;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchBVE;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.AbstractBronVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronExamenresultaatVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronExamenMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.*;
import nl.topicus.eduarte.krd.zoekfilters.BronBatchZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronExamenmeldingZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronSignaalZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronTerugkoppelingZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter.TypeMelding;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.IBronBatch;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Sectordeel;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class BronHibernateDataAccessHelper extends HibernateDataAccessHelper<Void> implements
		BronDataAccessHelper
{
	public BronHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public List<IBronBatch> getBronBatches(BronBatchZoekFilter filter)
	{
		List<IBronBatch> batches = new ArrayList<IBronBatch>();
		if (filter.getOnderwijssoort() == null
			|| !filter.getOnderwijssoort().equals(BronOnderwijssoort.VOORTGEZETONDERWIJS))
		{
			Criteria criteriaBVE = createCriteria(BronBatchBVE.class);
			CriteriaBuilder builderBVE = new CriteriaBuilder(criteriaBVE);
			builderBVE.addEquals("aanleverpunt", filter.getBronAanleverpunt());
			builderBVE.addEquals("schooljaar", filter.getSchooljaar());
			builderBVE.addEquals("batchNummer", filter.getBatchnummer());
			if (filter.getOnderwijssoort() != null)
			{
				switch (filter.getOnderwijssoort())
				{
					case BEROEPSONDERWIJS:
						builderBVE.addEquals("sectordeel", Sectordeel.Beroepsonderwijs);
						break;
					case EDUCATIE:
						builderBVE.addEquals("sectordeel", Sectordeel.Basiseducatie);
						break;
					case VAVO:
						builderBVE.addEquals("sectordeel", Sectordeel.VAVO);
						break;
					default:
						break;
				}
			}

			if (!filter.isUnique())
				criteriaBVE.addOrder(Order.desc("createdAt"));
			batches.addAll(this.<IBronBatch> cachedList(criteriaBVE));
		}
		if (filter.getOnderwijssoort() == null
			|| filter.getOnderwijssoort().equals(BronOnderwijssoort.VOORTGEZETONDERWIJS))
		{
			Criteria criteriaVO = createCriteria(AbstractBronBatchVO.class);
			CriteriaBuilder builderVO = new CriteriaBuilder(criteriaVO);
			builderVO.addEquals("aanleverpunt", filter.getBronAanleverpunt());
			builderVO.addEquals("schooljaar", filter.getSchooljaar());
			builderVO.addEquals("batchNummer", filter.getBatchnummer());

			if (!filter.isUnique())
				criteriaVO.addOrder(Order.desc("createdAt"));
			batches.addAll(this.<IBronBatch> cachedList(criteriaVO));
		}
		if (filter.isHeeftMeldingenInBehandeling() != null)
		{
			for (Iterator<IBronBatch> batchIterator = batches.iterator(); batchIterator.hasNext();)
			{
				IBronBatch batch = batchIterator.next();
				if (filter.isHeeftMeldingenInBehandeling()
					&& batch.getAantalMeldingenInBehandeling() == 0)
				{
					batchIterator.remove();
				}
				else if (!filter.isHeeftMeldingenInBehandeling()
					&& batch.getAantalMeldingenInBehandeling() != 0)
				{
					batchIterator.remove();
				}
			}
		}
		return batches;
	}

	private Criteria maakBVECriteria(BronMeldingZoekFilter filter)
	{
		Criteria criteria = createCriteria(BronAanleverMelding.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.createAlias("batch", "batch", CriteriaSpecification.LEFT_JOIN);
		builder.createAlias("verbintenis", "verbintenis", LEFT_JOIN);
		builder.createAlias("verbintenis.organisatieEenheid", "organisatieEenheid", LEFT_JOIN);
		builder.createAlias("verbintenis.locatie", "locatie", LEFT_JOIN);

		if (!filter.addOrganisatieEenheidLocatieCriteria("verbintenis.", criteria))
			return null;

		if (filter.getGeblokkeerd() != null)
		{
			builder.addEquals("geblokkeerd", filter.getGeblokkeerd());
		}

		builder.addEquals("bronOnderwijssoort", filter.getBronOnderwijssoort());
		builder.addEquals("deelnemer", filter.getDeelnemer());

		// als er gezocht wordt naar sleutelgegevens, dan de verbintenis niet meenemen. De
		// wijziging van de sleutel kan binnen de context van een compleet andere
		// verbintenis plaatsvinden dan geregistreerd is bij de aanlevermelding.
		if (filter.getBronMeldingOnderdeel() != BronMeldingOnderdeel.Sleutel)
		{
			builder.addEquals("verbintenis", filter.getVerbintenis());
		}
		builder.addEquals("bronMeldingStatus", filter.getMeldingStatus());
		if (filter.getMeldingStatus() == null
			|| !filter.getMeldingStatus().equals(BronMeldingStatus.WACHTRIJ))
		{
			builder.addEquals("batch", filter.getBatch());
			builder.addEquals("batch.aanleverpunt", filter.getAanleverpunt());
			builder.addEquals("batch.batchNummer", filter.getBatchnummer());
		}
		if (filter.getBronMeldingOnderdeelNot() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(BronBveAanleverRecord.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addNotIn("recordType", filter.getBronMeldingOnderdeelNot().getRecordTypes());
			dc.setProjection(Projections.property("melding"));
			criteria.add(Subqueries.propertyIn("id", dc));
		}
		if (filter.getBronMeldingOnderdeel() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(BronBveAanleverRecord.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addIn("recordType", filter.getBronMeldingOnderdeel().getRecordTypes());
			dc.setProjection(Projections.property("melding"));
			criteria.add(Subqueries.propertyIn("id", dc));
		}

		if (filter.getSoortMutatie() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(BronBveAanleverRecord.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("soortMutatie", filter.getSoortMutatie());
			dc.setProjection(Projections.property("melding"));
			criteria.add(Subqueries.propertyIn("id", dc));
		}
		if (filter.getExamendeelname() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(BronBveAanleverRecord.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("examenDeelname", filter.getExamendeelname());
			dc.setProjection(Projections.property("melding"));
			criteria.add(Subqueries.propertyIn("id", dc));
		}
		if (filter.getTerugkoppelbestand() != null)
		{
			criteria.createAlias("terugkoppelmelding", "terugkoppelmelding", INNER_JOIN);
			criteria.createAlias("terugkoppelmelding.batchgegevens", "batchgegevens", INNER_JOIN);
			builder.addEquals("batchgegevens.terugkoppelbestand", filter.getTerugkoppelbestand());
		}

		if (filter.getBPV() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(BronBveAanleverRecord.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("bpvInschrijving", filter.getBPV());
			dc.setProjection(Projections.property("melding"));
			criteria.add(Subqueries.propertyIn("id", dc));
		}

		if (filter.getBekostigingsRelevant() != null)
		{
			builder.addEquals("bekostigingsRelevant", filter.getBekostigingsRelevant());
		}
		if (filter.getBronSchooljaarStatus() != null)
		{
			BronSchooljaarStatus schooljaar = filter.getBronSchooljaarStatus();
			builder.addNullOrGreaterOrEquals("ingangsDatum", schooljaar.getSchooljaar()
				.getBegindatum());
			builder
				.addNullOrLessOrEquals("ingangsDatum", schooljaar.getSchooljaar().getEinddatum());
			BronStatus status = schooljaar.getStatus(filter.getBronOnderwijssoort());
			if (status != null)
			{
				// if (!status.isMutatieToegestaan())
				// return null;
				if (filter.getTypeMelding() == TypeMelding.Regulier)
				{
					if (status.isAccountantMutatieToegestaan()
						|| status == BronStatus.AssurancerapportOpgesteld
						|| status == BronStatus.Historisch)
					{
						builder.addEquals("bekostigingsRelevant", false);
					}
				}
				if (filter.getTypeMelding() == TypeMelding.Accountant)
				{
					if (status.isAccountantMutatieToegestaan())
					{
						builder.addEquals("bekostigingsRelevant", true);
					}
					else
						return null;
				}
			}
		}

		if (filter.getOrderBy() != null)
		{
			criteria.addOrder(Order.asc(filter.getOrderBy()));
		}
		else if (!filter.isUnique())
			criteria.addOrder(Order.desc("createdAt"));
		return criteria;
	}

	private Criteria maakVOCriteria(BronMeldingZoekFilter filter,
			Class< ? extends AbstractBronVOMelding> clz)
	{
		// als het type melding regulier of accountant is, dan wordt er een query op de
		// inschrijvingsmeldingen gedaan, anders op alle meldingen.
		Criteria criteria;
		if (clz != null)
			criteria = createCriteria(clz);
		else if (filter.getTypeMelding() != null)
			criteria = createCriteria(BronInschrijvingsgegevensVOMelding.class);
		else
			criteria = createCriteria(AbstractBronVOMelding.class);

		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.createAlias("batch", "batch", CriteriaSpecification.LEFT_JOIN);
		builder.addEquals("deelnemer", filter.getDeelnemer());
		builder.addEquals("verbintenis", filter.getVerbintenis());
		builder.addEquals("examendeelname", filter.getExamendeelname());
		builder.addEquals("soortMutatie", filter.getSoortMutatie());
		builder.addEquals("soort", filter.getVoMeldingSoort());
		builder.addEquals("bronMeldingStatus", filter.getMeldingStatus());

		if (filter.getMeldingStatus() != BronMeldingStatus.WACHTRIJ)
		{
			builder.addEquals("batch", filter.getBatch());
			builder.addEquals("batch.aanleverpunt", filter.getAanleverpunt());
			builder.addEquals("batch.batchNummer", filter.getBatchnummer());
		}

		if (filter.getGeblokkeerd() != null)
		{
			builder.addEquals("geblokkeerd", filter.getGeblokkeerd());
		}

		criteria.createAlias("verbintenis", "verbintenis", LEFT_JOIN);
		criteria.createAlias("verbintenis.organisatieEenheid", "organisatieEenheid", LEFT_JOIN);
		criteria.createAlias("verbintenis.locatie", "locatie", LEFT_JOIN);
		if (!filter.addOrganisatieEenheidLocatieCriteria("verbintenis.", criteria))
			return null;

		if (filter.getBronMeldingOnderdeel() != null)
			builder.addIn("DTYPE", filter.getBronMeldingOnderdeel().getRecordTypes());

		if (filter.getTerugkoppelbestand() != null)
		{
			criteria.createAlias("terugkoppelmelding", "terugkoppelmelding", INNER_JOIN);
			criteria.createAlias("terugkoppelmelding.batchgegevens", "batchgegevens", INNER_JOIN);
			builder.addEquals("batchgegevens.terugkoppelbestand", filter.getTerugkoppelbestand());
		}
		if (filter.getBronSchooljaarStatus() != null)
		{
			BronSchooljaarStatus schooljaar = filter.getBronSchooljaarStatus();
			// De peildatum voor VO is 1 oktober
			Date eenOktober =
				TimeUtil.getInstance().getFirstDayOfMonth(
					schooljaar.getSchooljaar().getStartJaar(), Calendar.OCTOBER);

			builder.addNullOrGreaterOrEquals("ingangsDatum", schooljaar.getSchooljaar()
				.getBegindatum());
			builder
				.addNullOrLessOrEquals("ingangsDatum", schooljaar.getSchooljaar().getEinddatum());
			if (filter.getTypeMelding() == TypeMelding.Regulier)
			{
				if (schooljaar.getStatusVO().isReguliereMutatieToegestaan())
				{
					// doe niks er mogen nog voor het hele schooljaar reguliere meldingen
					// worden aangeleverd
				}
				if (schooljaar.getStatusVO().isAccountantMutatieToegestaan())
				{
					/**
					 * Er wordt een criteria toegevoegd waarbij de ingangsdatum null of
					 * groter of gelijk aan 1 oktober van het schooljaar moet zijn, of
					 * melding moet niet bekostigingsrelevant zijn
					 */
					builder
						.addOrs(Restrictions.isNull("ingangsDatum"), Restrictions.ge(
							"ingangsDatum", eenOktober), Restrictions.eq("bekostigingsRelevant",
							false));
				}
				if (schooljaar.getStatusVO().isGeenMutatieToegestaan())
				{
					BronSchooljaarStatus volgendeSchooljaarStatus =
						DataAccessRegistry.getHelper(BronSchooljaarStatusDataAccessHelper.class)
							.getSchooljaarStatus(schooljaar.getAanleverpunt(),
								schooljaar.getSchooljaar().getVolgendSchooljaar());
					if (volgendeSchooljaarStatus == null
						|| volgendeSchooljaarStatus.getStatusVO().isReguliereMutatieToegestaan())
						builder.addNullOrGreaterOrEquals("ingangsDatum", eenOktober);
					else
						return null;

				}
			}
			if (filter.getTypeMelding() == TypeMelding.Accountant)
			{
				builder.addEquals("bekostigingsRelevant", true);
				if (schooljaar.getStatusVO().isReguliereMutatieToegestaan())
					return null;
				if (schooljaar.getStatusVO().isAccountantMutatieToegestaan())
					builder.addLessThan("ingangsDatum", eenOktober);
				if (schooljaar.getStatusVO().isGeenMutatieToegestaan())
				{
					BronSchooljaarStatus volgendeSchooljaarStatus =
						DataAccessRegistry.getHelper(BronSchooljaarStatusDataAccessHelper.class)
							.getSchooljaarStatus(schooljaar.getAanleverpunt(),
								schooljaar.getSchooljaar().getVolgendSchooljaar());
					if (volgendeSchooljaarStatus != null
						&& volgendeSchooljaarStatus.getStatusVO().isAccountantMutatieToegestaan())
						builder.addGreaterOrEquals("ingangsDatum", eenOktober);
					else
						return null;

				}
			}
		}
		if (filter.getOrderBy() != null)
		{
			criteria.addOrder(Order.asc(filter.getOrderBy()));
		}
		else if (!filter.isUnique())
			criteria.addOrder(Order.desc("createdAt"));

		return criteria;
	}

	@Override
	public List<IBronMelding> getBronMeldingen(BronMeldingZoekFilter filter)
	{
		List<IBronMelding> meldingen = new ArrayList<IBronMelding>();

		BronOnderwijssoort sector = filter.getBronOnderwijssoort();
		if (sector == null || sector != BronOnderwijssoort.VOORTGEZETONDERWIJS)
		{
			meldingen.addAll(getBronBveMeldingen(filter));
		}
		if (sector == null || sector == BronOnderwijssoort.VOORTGEZETONDERWIJS)
		{
			meldingen.addAll(getBronVoMeldingen(filter, AbstractBronVOMelding.class));
		}
		return meldingen;
	}

	@Override
	public List<BronAanleverMelding> getBronBveMeldingen(BronMeldingZoekFilter filter)
	{
		Criteria bVECriteria = maakBVECriteria(filter);
		return uncachedList(bVECriteria);
	}

	@Override
	public <T extends AbstractBronVOMelding> List<T> getBronVoMeldingen(
			BronMeldingZoekFilter filter, Class<T> clz)
	{
		Criteria criteria = maakVOCriteria(filter, clz);
		return uncachedList(criteria);
	}

	@Override
	public long getAantalMeldingen(BronMeldingZoekFilter filter)
	{
		filter.setUnique(true);

		long aantalBVEMedlingen = getAantalBVEMeldingen(filter);
		long aantalVOMeldingen = getAantalVOMeldingen(filter);

		filter.setUnique(false);
		return aantalBVEMedlingen + aantalVOMeldingen;
	}

	@Override
	public long getAantalVOMeldingen(BronMeldingZoekFilter filter)
	{
		filter.setUnique(true);

		Long aantalVOMeldingen = 0L;
		if (filter.getBronOnderwijssoort() == null
			|| filter.getBronOnderwijssoort().equals(BronOnderwijssoort.VOORTGEZETONDERWIJS))
		{
			Criteria voCriteria = maakVOCriteria(filter, AbstractBronVOMelding.class);
			if (voCriteria != null)
			{
				voCriteria.setProjection(Projections.rowCount());
				aantalVOMeldingen = uncachedUnique(voCriteria);
			}
		}
		filter.setUnique(false);
		return aantalVOMeldingen;
	}

	@Override
	public long getAantalBVEMeldingen(BronMeldingZoekFilter filter)
	{
		filter.setUnique(true);

		Long aantalBVEMedlingen = 0L;
		if (filter.getBronOnderwijssoort() == null
			|| !filter.getBronOnderwijssoort().equals(BronOnderwijssoort.VOORTGEZETONDERWIJS))
		{
			Criteria bVECriteria = maakBVECriteria(filter);
			if (bVECriteria != null)
			{
				bVECriteria.setProjection(Projections.rowCount());
				aantalBVEMedlingen = uncachedUnique(bVECriteria);
			}
		}
		filter.setUnique(false);
		return aantalBVEMedlingen;
	}

	@Override
	public BronBatchBVE findBveBatch(Sectordeel sectordeel, BronAanleverpunt aanleverpunt,
			int batchnummer)
	{
		Criteria criteria = createCriteria(BronBatchBVE.class);
		criteria.add(Restrictions.eq("aanleverpunt", aanleverpunt));
		criteria.add(Restrictions.eq("batchNummer", batchnummer));
		criteria.add(Restrictions.eq("sectordeel", sectordeel));
		return cachedUnique(criteria);
	}

	@Override
	public AbstractBronBatchVO< ? > findVoBatch(BronAanleverpunt aanleverpunt, int batchnummer)
	{
		Criteria criteria = createCriteria(AbstractBronBatchVO.class);
		criteria.add(Restrictions.eq("aanleverpunt", aanleverpunt));
		criteria.add(Restrictions.eq("batchNummer", batchnummer));
		return cachedUnique(criteria);
	}

	@Override
	public List<IBronSignaal> getSignalen(BronSignaalZoekFilter filter)
	{
		Asserts.assertNotNull("filter", filter);
		List<IBronSignaal> signalen = new ArrayList<IBronSignaal>();
		if (filter.getBronOnderwijssoort() == null
			|| filter.getBronOnderwijssoort() == BronOnderwijssoort.VOORTGEZETONDERWIJS)
		{
			Criteria criteria = createCriteria(BronVoSignaal.class);
			CriteriaBuilder builder = new CriteriaBuilder(criteria);
			builder.createAlias("melding", "melding", CriteriaSpecification.LEFT_JOIN);
			builder.createAlias("melding.batchgegevens", "batchgegevens",
				CriteriaSpecification.LEFT_JOIN);
			builder.createAlias("melding.aanlevermelding", "aanlevermelding",
				CriteriaSpecification.LEFT_JOIN);
			builder.createAlias("aanlevermelding.batch", "batch", CriteriaSpecification.LEFT_JOIN);
			builder.createAlias("aanlevermelding.verbintenis", "verbintenis",
				CriteriaSpecification.LEFT_JOIN);
			builder.createAlias("batch.aanleverpunt", "aanleverpunt",
				CriteriaSpecification.LEFT_JOIN);

			builder.addEquals("geaccordeerd", filter.getGeaccordeerd());
			builder.addEquals("batch.schooljaar", filter.getSchooljaar());
			builder.addEquals("batch.aanleverpunt", filter.getBronAanleverpunt());
			builder.addEquals("batch.batchNummer", filter.getBatchNummer());
			builder.addEquals("batchgegevens.terugkoppelbestand", filter.getTerugkoppelbestand());
			builder.addEquals("melding.meldingNummer", filter.getTerugkoppelingNummer());
			builder.addEquals("verbintenis.locatie", filter.getLocatie());

			if (filter.getDeelnemernNummer() != null)
				builder.addEquals("melding.leerlingNummerInstelling", filter.getDeelnemernNummer()
					.toString());
			builder.addEquals("signaalcode", filter.getSignaalcode());
			builder.addEquals("ernst", filter.getErnst());
			if (!filter.isUnique())
				criteria.addOrder(Order.desc("createdAt"));
			signalen.addAll(this.<IBronSignaal> cachedList(criteria));
		}
		if (filter.getBronOnderwijssoort() == null
			|| !filter.getBronOnderwijssoort().equals(BronOnderwijssoort.VOORTGEZETONDERWIJS))
		{
			Criteria criteria = createCriteria(BronBveTerugkoppelRecord.class);
			CriteriaBuilder builder = new CriteriaBuilder(criteria);
			builder.createAlias("melding", "terugkoppelMelding");
			builder.createAlias("melding.batchgegevens", "batchgegevens");

			builder.createAlias("terugkoppelMelding.aanlevermelding", "aanlevermelding",
				CriteriaSpecification.LEFT_JOIN);
			builder.createAlias("aanlevermelding.batch", "batch", CriteriaSpecification.LEFT_JOIN);
			builder.createAlias("aanlevermelding.verbintenis", "verbintenis",
				CriteriaSpecification.LEFT_JOIN);
			builder.createAlias("batch.aanleverpunt", "aanleverpunt",
				CriteriaSpecification.LEFT_JOIN);

			builder.addEquals("geaccordeerd", filter.getGeaccordeerd());
			builder.addEquals("recordType", 499);
			builder.addEquals("verbintenis.locatie", filter.getLocatie());

			if (filter.getBronOnderwijssoort() != null)
				switch (filter.getBronOnderwijssoort())
				{
					case BEROEPSONDERWIJS:
						builder.addEquals("batch.sectordeel", Sectordeel.Beroepsonderwijs);
						break;
					case EDUCATIE:
						builder.addEquals("batch.sectordeel", Sectordeel.Basiseducatie);
						break;
					case VAVO:
						builder.addEquals("batch.sectordeel", Sectordeel.VAVO);
						break;
					default:
						break;
				}

			builder.addEquals("batch.schooljaar", filter.getSchooljaar());
			builder.addEquals("batch.aanleverpunt", filter.getBronAanleverpunt());
			builder.addEquals("batch.batchNummer", filter.getBatchNummer());
			builder.addEquals("batchgegevens.terugkoppelbestand", filter.getTerugkoppelbestand());
			builder.addEquals("terugkoppelMelding.meldingnummer", filter.getTerugkoppelingNummer());
			if (filter.getDeelnemernNummer() != null)
				builder.addEquals("terugkoppelMelding.leerlingnummer", filter.getDeelnemernNummer()
					.toString());
			builder.addEquals("signaalcode", filter.getSignaalcode());
			builder.addEquals("ernst", filter.getErnst());
			if (!filter.isUnique())
				criteria.addOrder(Order.desc("createdAt"));
			signalen.addAll(this.<IBronSignaal> cachedList(criteria));
		}
		return signalen;
	}

	@Override
	public long getAantalOngeaccordeerdeSignalen(BronOnderwijssoort onderwijssoort,
			BronAanleverpunt aanleverpunt, Schooljaar schooljaar)
	{
		if (BronOnderwijssoort.VOORTGEZETONDERWIJS.equals(onderwijssoort))
		{
			Criteria voCriteria = createCriteria(BronVoSignaal.class);
			CriteriaBuilder voBuilder = new CriteriaBuilder(voCriteria);
			voBuilder.createAlias("melding", "melding", CriteriaSpecification.LEFT_JOIN);
			voBuilder.createAlias("melding.aanlevermelding", "aanlevermelding",
				CriteriaSpecification.LEFT_JOIN);
			voBuilder
				.createAlias("aanlevermelding.batch", "batch", CriteriaSpecification.LEFT_JOIN);
			voBuilder.addEquals("geaccordeerd", false);
			voBuilder.addEquals("batch.aanleverpunt", aanleverpunt);
			voBuilder.addEquals("batch.schooljaar", schooljaar);
			voCriteria.setProjection(Projections.rowCount());
			return (Long) cachedUnique(voCriteria);
		}
		else
		{
			Criteria bveCriteria = createCriteria(BronBveTerugkoppelRecord.class);
			bveCriteria.createAlias("melding", "terugkoppelMelding");
			bveCriteria.createAlias("terugkoppelMelding.aanlevermelding", "aanlevermelding");
			bveCriteria.createAlias("aanlevermelding.batch", "batch");
			CriteriaBuilder bveBuilder = new CriteriaBuilder(bveCriteria);
			bveBuilder.addEquals("aanlevermelding.bronOnderwijssoort", onderwijssoort);
			bveBuilder.addEquals("batch.aanleverpunt", aanleverpunt);
			bveBuilder.addEquals("batch.schooljaar", schooljaar);
			bveBuilder.addEquals("recordType", 499);
			bveBuilder.addEquals("geaccordeerd", false);
			bveCriteria.setProjection(Projections.rowCount());
			return (Long) cachedUnique(bveCriteria);
		}
	}

	@Override
	public List<IBronTerugkoppeling> getTerugkoppelingen(BronTerugkoppelingZoekFilter filter)
	{
		Asserts.assertNotNull("filter", filter);
		List<IBronTerugkoppeling> terugkoppelingen = new ArrayList<IBronTerugkoppeling>();
		if (filter.getBronOnderwijssoort() == null
			|| filter.getBronOnderwijssoort().equals(BronOnderwijssoort.VOORTGEZETONDERWIJS))
		{
			Criteria criteria = createCriteria(BronVoTerugkoppelbestand.class);
			CriteriaBuilder builder = new CriteriaBuilder(criteria);
			if (filter.getBronAanleverpunt() != null)
				builder.addEquals("aanleverpuntNummer", filter.getBronAanleverpunt().getNummer());
			if (!filter.isUnique())
				criteria.addOrder(Order.desc("createdAt"));
			terugkoppelingen.addAll(this.<IBronTerugkoppeling> cachedList(criteria));
		}
		if (filter.getBronOnderwijssoort() == null
			|| !filter.getBronOnderwijssoort().equals(BronOnderwijssoort.VOORTGEZETONDERWIJS))
		{
			Criteria criteria = createCriteria(BronBveTerugkoppelbestand.class);
			CriteriaBuilder builder = new CriteriaBuilder(criteria);
			if (filter.getBronAanleverpunt() != null)
				builder.addEquals("aanleverpuntNummer", filter.getBronAanleverpunt().getNummer());
			if (filter.getBronOnderwijssoort() != null)
				switch (filter.getBronOnderwijssoort())
				{
					case BEROEPSONDERWIJS:
						builder.addEquals("sectordeel", Sectordeel.Beroepsonderwijs);
						break;
					case EDUCATIE:
						builder.addEquals("sectordeel", Sectordeel.Basiseducatie);
						break;
					case VAVO:
						builder.addEquals("sectordeel", Sectordeel.VAVO);
						break;
					default:
						break;
				}

			if (!filter.isUnique())
			{
				criteria.addOrder(Order.asc("sectordeel"));
				criteria.addOrder(Order.desc("createdAt"));
			}
			terugkoppelingen.addAll(this.<IBronTerugkoppeling> cachedList(criteria));
		}
		return terugkoppelingen;
	}

	@Override
	public List<IBronExamenMelding> getBronExamenMeldingen(BronExamenmeldingZoekFilter filter)
	{
		Asserts.assertNotNull("filter", filter);
		List<IBronExamenMelding> examenMeldingen = new ArrayList<IBronExamenMelding>();
		if (filter.getBronOnderwijssoort() == null
			|| filter.getBronOnderwijssoort().equals(BronOnderwijssoort.VOORTGEZETONDERWIJS))
		{
			Criteria criteria = createCriteria(BronExamenresultaatVOMelding.class);
			CriteriaBuilder builder = new CriteriaBuilder(criteria);
			if (filter.getAanleverpunt() != null)
			{
				builder.createAlias("batch", "batch", CriteriaSpecification.LEFT_JOIN);
				builder.addEquals("batch.aanleverpunt", filter.getAanleverpunt());
			}
			builder.addEquals("examenverzameling", filter.getExamenverzameling());
			if (!filter.isUnique())
				criteria.addOrder(Order.desc("createdAt"));
			examenMeldingen.addAll(this.<IBronExamenMelding> cachedList(criteria));
		}
		if (filter.getBronOnderwijssoort() == null
			|| !filter.getBronOnderwijssoort().equals(BronOnderwijssoort.VOORTGEZETONDERWIJS))
		{
			Criteria criteria = createCriteria(BronAanleverMelding.class);
			CriteriaBuilder builder = new CriteriaBuilder(criteria);
			if (filter.getAanleverpunt() != null)
			{
				criteria.createAlias("batch", "batch", CriteriaSpecification.LEFT_JOIN);
				builder.addEquals("batch.aanleverpunt", filter.getAanleverpunt());
			}
			builder.addEquals("bronOnderwijssoort", filter.getBronOnderwijssoort());
			builder.addEquals("examenverzameling", filter.getExamenverzameling());
			if (!filter.isUnique())
				criteria.addOrder(Order.desc("createdAt"));
			examenMeldingen.addAll(this.<IBronExamenMelding> cachedList(criteria));
		}
		return examenMeldingen;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<BronOnderwijssoort, Integer> getVerwachteTerugkoppelBatchnummers(
			BronAanleverpunt aanleverpunt)
	{
		Session session = getSessionProvider().getSession();
		SQLQuery query =
			session
				.createSQLQuery("select sectordeel, max(batchnummer) "
					+ "from bron_bve_terugkoppelbatches "
					+ "where (sectordeel, created_at) in ("
					+ "select sectordeel, max(created_at) from bron_bve_terugkoppelbatches "
					+ "where organisatie = :organisatie and aanleverpuntnummer = :aanleverpunt "
					+ "group by sectordeel"
					+ ") "
					+ "and organisatie = :organisatie "
					+ "and aanleverpuntnummer = :aanleverpunt "
					+ "group by sectordeel "
					+ "union all "
					+ "select '"
					+ BronOnderwijssoort.VOORTGEZETONDERWIJS.name()
					+ "', bronbatchnummer from bron_vo_terugkoppelbatches "
					+ "where created_at = ("
					+ "select max(created_at) from bron_vo_terugkoppelbatches where organisatie = :organisatie and aanleverpuntnummer = :aanleverpunt"
					+ ") " //
					+ "and organisatie = :organisatie " //
					+ "and aanleverpuntnummer = :aanleverpunt");

		Instelling instelling = EduArteContext.get().getInstelling();

		Asserts.assertNotNull("instelling", instelling);
		Asserts.assertNotNull("aanleverpunt", aanleverpunt);

		query.setEntity("organisatie", instelling);
		query.setInteger("aanleverpunt", aanleverpunt.getNummer());

		Map<BronOnderwijssoort, Integer> result = new HashMap<BronOnderwijssoort, Integer>();
		List<Object[]> results = query.list();
		for (Object[] values : results)
		{
			String sectordeel = (String) values[0];
			int batchnummer = (((BigDecimal) values[1]).intValue() + 1) % 1000;
			BronOnderwijssoort onderwijssoort = convert(sectordeel);
			result.put(onderwijssoort, batchnummer);
		}
		return result;
	}

	private BronOnderwijssoort convert(String sectordeel)
	{
		for (Sectordeel sector : Sectordeel.values())
		{
			if (sector.name().equalsIgnoreCase(sectordeel))
			{
				return BronOnderwijssoort.valueOf(sector);
			}
		}

		return BronOnderwijssoort.valueOf(sectordeel);
	}

	@Override
	public boolean isTerugkoppelingIngelezenVoorBatch(IBronBatch bronBatch)
	{
		Asserts.assertNotNull("bronBatchBVE", bronBatch);
		if (bronBatch instanceof BronBatchBVE)
		{
			Criteria criteria = createCriteria(BronBveBatchgegevens.class);
			CriteriaBuilder builder = new CriteriaBuilder(criteria);
			builder.addEquals("batch", bronBatch);
			criteria.setProjection(Projections.rowCount());
			Long aantalTerugkoppelingen = cachedUnique(criteria);
			return aantalTerugkoppelingen > 0;
		}
		else if (bronBatch instanceof AbstractBronBatchVO< ? >)
		{
			Criteria criteria = createCriteria(BronVoBatchgegevens.class);
			CriteriaBuilder builder = new CriteriaBuilder(criteria);
			builder.addEquals("batch", bronBatch);
			criteria.setProjection(Projections.rowCount());
			Long aantalTerugkoppelingen = cachedUnique(criteria);
			return aantalTerugkoppelingen > 0;
		}
		return false;
	}

	@Override
	public List<BronBveAanleverRecord> getAanleverRecords(OnderwijsproductAfnameContext context)
	{
		Criteria criteria = createCriteria(BronBveAanleverRecord.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("afnameContext", context);
		return cachedList(criteria);
	}
}
