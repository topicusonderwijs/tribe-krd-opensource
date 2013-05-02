package nl.topicus.eduarte.krd.bron.jobs;

import static java.lang.String.*;
import static nl.topicus.cobra.util.StringUtil.*;
import static nl.topicus.onderwijs.duo.bron.BRONConstants.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.krd.bron.BronController;
import nl.topicus.eduarte.krd.bron.BronUtils;
import nl.topicus.eduarte.krd.dao.helpers.BronAanleverpuntDataAccessHelper;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.BronTerugkoppelbestandInlezenJobRun;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchBVE;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.BronBveBatchgegevens;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.BronBveTerugkoppelMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.BronBveTerugkoppelRecord;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.BronBveTerugkoppelbestand;
import nl.topicus.onderwijs.duo.bron.BronException;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.VoorlooprecordTerugkoppeling;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Sectordeel;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.StatusMelding;

class BronBveTerugkoppelbestandProcessor extends BronTerugkoppelbestandProcessor
{
	private final BronTerugkoppelbestandInlezenJob job;

	private final BronTerugkoppelbestandInlezenJobRun run;

	private boolean hasError = false;

	BronBveTerugkoppelbestandProcessor(BronTerugkoppelbestandInlezenJob job,
			BronTerugkoppelbestandInlezenJobRun run)
	{
		this.job = job;
		this.run = run;
	}

	@Override
	public boolean processBestand(byte[] contents) throws InterruptedException, BronException
	{
		BronParseResult<VoorlooprecordTerugkoppeling> result =
			parseTerugkoppelbestand(contents, VoorlooprecordTerugkoppeling.class);

		if (result.hasError())
		{
			job.failJob(run, result.getException());
			return false;
		}
		VoorlooprecordTerugkoppeling voorlooprecord = result.getResult();

		job.setStatus("Verwerken van MBO terugkoppelbestand");
		run.info("Verwerken van MBO terugkoppelbestand " + job.getFilename());

		BronBveTerugkoppelbestand terugkoppeling = (BronBveTerugkoppelbestand) voorlooprecord;
		terugkoppeling.setBestandsnaam(run.getFilename());

		Brin brinHuidigeOrganisatie = EduArteContext.get().getInstelling().getBrincode();
		if (!terugkoppeling.getBrinNummer().equals(brinHuidigeOrganisatie.getCode()))
		{
			throw new BronException(
				"BRIN nummer in terugkoppeling komt niet overeen met BRIN nummer instelling.");
		}

		repareerRelaties(terugkoppeling);

		int aantalTerugkoppelmeldingen = getTotaalAantalTerugkoppelmeldingen(voorlooprecord);
		int verwerkteTerugkoppelmeldingen = 0;

		Sectordeel sectordeel = voorlooprecord.getSectordeel();
		Integer aanleverpuntnummer = voorlooprecord.getAanleverpuntNummer();

		BronAanleverpuntDataAccessHelper aanleverpunten =
			DataAccessRegistry.getHelper(BronAanleverpuntDataAccessHelper.class);
		BronAanleverpunt aanleverpunt = aanleverpunten.getAanleverpunt(aanleverpuntnummer);

		if (aanleverpunt == null)
		{
			throw new BronException(format(
				"Aanleverpunt in melding (%d) is niet gedefinieerd bij deze instelling.",
				aanleverpuntnummer));
		}

		Integer batchnummer = voorlooprecord.getBRONBatchNummer();
		Integer verwachteBatchnummer = getVerwachteTerugkoppelingNummer(sectordeel, aanleverpunt);

		if (verwachteBatchnummer != null && !batchnummer.equals(verwachteBatchnummer))
		{
			throw new BronException(
				String
					.format(
						"Batchnummer terugkoppeling (%03d) komt niet overeen met verwachte batchnummer (%03d) voor sector %s.",
						batchnummer, verwachteBatchnummer, sectordeel.name()));
		}

		BronDataAccessHelper bron = DataAccessRegistry.getHelper(BronDataAccessHelper.class);

		List<BronBveBatchgegevens> batchgegevens = terugkoppeling.getBatchgegevens();
		for (BronBveBatchgegevens batchgegeven : batchgegevens)
		{
			BronBatchBVE batch =
				bron.findBveBatch(sectordeel, aanleverpunt, batchgegeven
					.getBatchnummerAanleverbestand());

			batchgegeven.setBatch(batch);
			batchgegeven.saveOrUpdate();

			if (batch == null)
			{
				// er kon geen batch gevonden worden bij het batchnummer, aanleverpunt
				throw new BronException(format(
					"Kon batch %03d voor %s bij aanleverpunt %s niet vinden", batchnummer,
					terugkoppeling.getBronOnderwijssoort(), aanleverpuntnummer));
			}
			run
				.info(format(
					"Verwerken terugkoppelmeldingen voor batch %03d voor %s bij aanleverpunt %s",
					batchnummer, terugkoppeling.getBronOnderwijssoort().toString(),
					aanleverpuntnummer));
			if (!batchgegeven.getMeldingen().isEmpty())
			{
				BronBveTerugkoppelMelding laatste =
					batchgegeven.getMeldingen().get(batchgegeven.getMeldingen().size() - 1);

				if (laatste.getMeldingnummer() > batch.getMeldingen().size())
				{
					// aantal meldingen voor batchgegevens komt niet overeen met het
					// aantal meldingen in de batch

					run
						.error(String
							.format(
								"Batch %03d heeft minder meldingen (totaal %d) als het terugkoppelbestand verwacht (%d)",
								batchgegeven.getBatchnummerAanleverbestand(), batch.getMeldingen()
									.size(), laatste.getMeldingnummer()));
				}
			}
			List<BronBveTerugkoppelMelding> meldingen = batchgegeven.getMeldingen();
			for (BronBveTerugkoppelMelding terugkoppelmelding : meldingen)
			{
				job.setStatus("Verwerken melding van deelnemer "
					+ terugkoppelmelding.getLeerlingnummer());

				Comparator<BronAanleverMelding> c = new Comparator<BronAanleverMelding>()
				{
					public int compare(BronAanleverMelding o1, BronAanleverMelding o2)
					{
						return o1.getMeldingnummer().compareTo(o2.getMeldingnummer());
					}
				};
				BronAanleverMelding dummyMelding = new BronAanleverMelding();
				dummyMelding.setMeldingnummer(terugkoppelmelding.getMeldingnummer());
				int index = Collections.binarySearch(batch.getMeldingen(), dummyMelding, c);
				if (index < 0)
				{
					run
						.error(String
							.format(
								"Er is geen aanlevermelding gevonden voor terugkoppelmelding %d in batch %03d",
								terugkoppelmelding.getMeldingnummer(), batchgegeven
									.getBatchnummerAanleverbestand()));
					continue;
				}
				BronAanleverMelding aanlevermelding = batch.getMeldingen().get(index);

				controleerLeerlingnummer(terugkoppelmelding, aanlevermelding);
				controleerEnVerwerkBsn(terugkoppelmelding, aanlevermelding);
				controleerEnVerwerkOnderwijsnummer(terugkoppelmelding, aanlevermelding);

				terugkoppelmelding.setAanlevermelding(aanlevermelding);
				aanlevermelding.setTerugkoppelmelding(terugkoppelmelding);
				aanlevermelding.setBronMeldingStatus(BronMeldingStatus.valueOf(terugkoppelmelding
					.getStatusMelding().getIdentifier()));
				terugkoppelmelding.update();
				aanlevermelding.update();

				boolean goedgekeurd =
					terugkoppelmelding.getStatusMelding() == StatusMelding.GOEDGEKEURD;

				for (BronBveTerugkoppelRecord terugkoppelRecord : terugkoppelmelding.getRecords())
				{
					verwerkTerugkoppelRecord(terugkoppelmelding, terugkoppelRecord);
				}
				BronUtils.updateStatussenNaTerugkoppeling(aanlevermelding, goedgekeurd);

				if (!goedgekeurd)
				{
					try
					{
						terugkoppelmelding.getAanlevermelding().voegOpnieuwToeAanWachtrij();
					}
					catch (Exception e)
					{
						run
							.error("Kon aanlevermelding "
								+ terugkoppelmelding.getAanlevermelding().getMeldingnummer()
								+ " niet opnieuw toevoegen aan de wachtrij: "
								+ e.getLocalizedMessage());
					}
				}
				job.setProgress(++verwerkteTerugkoppelmeldingen, aantalTerugkoppelmeldingen);
			}
			batch.berekenControleTotalen();
			batch.saveOrUpdate();
		}
		run.info(format("%d terugkoppelmeldingen verwerkt", aantalTerugkoppelmeldingen));
		job.setStatus("Laatste wijzigingen opslaan");
		terugkoppeling.berekenControleTotalen();
		terugkoppeling.saveOrUpdate();

		return !hasError;
	}

	/**
	 * Controleert of het leerlingnummer in de terugkoppelmelding overeenkomt met het
	 * aangeleverde leerlingnummer.
	 */
	private void controleerLeerlingnummer(BronBveTerugkoppelMelding terugkoppelmelding,
			BronAanleverMelding aanlevermelding)
	{
		if (!equalOrBothNull(terugkoppelmelding.getLeerlingnummer(), aanlevermelding
			.getLeerlingnummer()))
		{
			run
				.error(format(
					"Terugkoppelmelding %d betreft niet hetzelfde leerlingnummer als de gekoppelde aanlevermelding %d: aangeleverd %s, teruggekoppeld %s",
					terugkoppelmelding.getMeldingnummer(), aanlevermelding.getMeldingnummer(),
					aanlevermelding.getLeerlingnummer(), terugkoppelmelding.getLeerlingnummer()));
		}
	}

	/**
	 * Controleert of het BSN overeenkomt, of zet deze op de deelnemer indien nog niet
	 * bekend binnen KRD en wel aangeleverd door BRON. In dit laatste geval wordt de
	 * verdere verwerking van het BSN in de wachtrij verzorgt door de
	 * {@link BronController}
	 */
	private void controleerEnVerwerkBsn(BronBveTerugkoppelMelding terugkoppelmelding,
			BronAanleverMelding aanlevermelding)
	{
		if (isEmpty(aanlevermelding.getSofinummer())
			&& isNotEmpty(terugkoppelmelding.getSofinummer()))
		{
			verwerkAchterhaaldBsn(terugkoppelmelding, aanlevermelding);
		}
		else
		{
			String aanleverBsn = verwijderVoorloopnullen(aanlevermelding.getSofinummer());
			String terugkoppelBsn = verwijderVoorloopnullen(terugkoppelmelding.getSofinummer());
			if (!equalOrBothEmpty(aanleverBsn, terugkoppelBsn))
			{
				// zijn kennelijk niet dezelfde deelnemers
				run
					.error(format(
						"Terugkoppelmelding %d bevat ander BSN dan de gekoppelde aanlevermelding %d — aangeleverd: %s, teruggekoppeld: %s",
						terugkoppelmelding.getMeldingnummer(), aanlevermelding.getMeldingnummer(),
						terugkoppelmelding.getSofinummer(), aanlevermelding.getSofinummer()));
			}
		}
	}

	/**
	 * Zet het achterhaalde BSN op de deelnemer indien deze nog geen BSN heeft (het kan
	 * zijn dat de instelling een BSN achterhaald heeft terwijl BRON in het GBA loopt te
	 * grasduinen.
	 */
	private void verwerkAchterhaaldBsn(BronBveTerugkoppelMelding melding,
			BronAanleverMelding aanlevermelding)
	{
		Deelnemer deelnemer = aanlevermelding.getDeelnemer();
		Persoon persoon = deelnemer.getPersoon();
		Long bsnAchterhaald = Long.valueOf(verwijderVoorloopnullen(melding.getSofinummer()));
		if (persoon.getBsn() == null)
		{
			persoon.setBsn(bsnAchterhaald);
			persoon.update();
			run
				.info(format(
					"Terugkoppeling voor melding %d heeft sofinummer achterhaald bij GBA voor deelnemer %s. Het sofinummer is verwerkt in het KRD.",
					aanlevermelding.getMeldingnummer(), melding.getLeerlingnummer()));
		}
		else if (!persoon.getBsn().equals(bsnAchterhaald))
		{
			// BRON kan het BSN nummer achterhaald hebben, terwijl in KRD ook een BSN
			// nummer is ingevuld. Deze nummers moeten dan wel overeenkomen.
			run
				.error(format(
					"Achterhaald BSN %d vanuit GBA in melding %d komt niet overeen met deelnemer BSN %d, dit is niet aangepast",
					bsnAchterhaald, melding.getMeldingnummer(), persoon.getBsn()));
		}
	}

	/**
	 * Controleert of het onderwijsnummer van de terugkoppeling overeenkomt met het
	 * aangeleverde onderwijsnummer, of wanneer een onderwijsnummer uitgegeven is door
	 * BRON, registreert deze op de deelnemer.
	 */
	private void controleerEnVerwerkOnderwijsnummer(BronBveTerugkoppelMelding terugkoppelmelding,
			BronAanleverMelding aanlevermelding)
	{
		if (isEmpty(aanlevermelding.getOnderwijsnummer())
			&& isNotEmpty(terugkoppelmelding.getOnderwijsnummer()))
		{
			verwerkUitgegevenOnderwijsnummer(terugkoppelmelding, aanlevermelding);
		}
		else
		{
			if (!equalOrBothEmpty(verwijderVoorloopnullen(aanlevermelding.getOnderwijsnummer()),
				verwijderVoorloopnullen(StringUtil.valueOrEmptyIfNull(terugkoppelmelding
					.getOnderwijsnummer()))))
			{
				run
					.error(String
						.format(
							"Terugkoppeling voor melding %d betreft niet dezelfde deelnemer (%s) als aanlevermelding (%s)",
							aanlevermelding.getMeldingnummer(), terugkoppelmelding
								.getOnderwijsnummer(), aanlevermelding.getOnderwijsnummer()));
			}
		}
	}

	/**
	 * Wanneer BRON een onderwijsnummer toekent aan de deelnemer dient dit ook in KRD
	 * verwerkt te worden. De {@link BronController} zorgt voor verdere verwerking in de
	 * wachtrij nadat de deelnemer opgeslagen is.
	 */
	private void verwerkUitgegevenOnderwijsnummer(BronBveTerugkoppelMelding terugkoppelmelding,
			BronAanleverMelding aanlevermelding)
	{
		Deelnemer deelnemer = aanlevermelding.getDeelnemer();
		Long uitgegevenOnderwijsnummer =
			Long.valueOf(verwijderVoorloopnullen(terugkoppelmelding.getOnderwijsnummer()));
		if (deelnemer.getOnderwijsnummer() == null)
		{
			deelnemer.setOnderwijsnummer(uitgegevenOnderwijsnummer);
			deelnemer.update();
			run
				.info(String
					.format(
						"Terugkoppeling voor melding %d heeft onderwijsnummer %s toegekend aan deelnemer %s. Het onderwijsnummer is verwerkt in het KRD.",
						aanlevermelding.getMeldingnummer(),
						terugkoppelmelding.getOnderwijsnummer(), terugkoppelmelding
							.getLeerlingnummer()));
		}
		else if (!deelnemer.getOnderwijsnummer().equals(uitgegevenOnderwijsnummer))
		{
			// Dit kan waarschijnlijk niet voorkomen: BRON geeft enkel de eerste keer een
			// onderwijsnummer uit, en de instelling kan niet zo'n nummer zelf verzinnen.
			run
				.error(String
					.format(
						"Uitgegeven onderwijsnummer %d vanuit BRON in melding %d komt niet overeen met in KRD geregistreerd onderwijsnummer %d, dit is niet aangepast",
						uitgegevenOnderwijsnummer, terugkoppelmelding.getMeldingnummer(), deelnemer
							.getOnderwijsnummer()));
		}
	}

	private void verwerkTerugkoppelRecord(BronBveTerugkoppelMelding melding,
			BronBveTerugkoppelRecord terugkoppelRecord)
	{
		BronBveAanleverRecord aanleverRecord =
			getAanleverrecord(melding.getAanlevermelding(), terugkoppelRecord);
		if (aanleverRecord != null)
		{
			aanleverRecord.setTerugkoppelrecord(terugkoppelRecord);
			terugkoppelRecord.setAanleverrecord(aanleverRecord);
			aanleverRecord.saveOrUpdate();
		}
		if (terugkoppelRecord.getRecordType() == 499)
			terugkoppelRecord.setGeaccordeerd(false);
		terugkoppelRecord.saveOrUpdate();
	}

	/**
	 * [... insert explitive here ...] Hibernate zet niet automatisch de reverse
	 * relatie... Dus dat moeten we zelf doen... Toch prettig een ORM die weinig aan ORM
	 * doet.
	 */
	private void repareerRelaties(BronBveTerugkoppelbestand terugkoppeling)
	{
		for (BronBveBatchgegevens batchgegevens : terugkoppeling.getBatchgegevens())
		{
			batchgegevens.setTerugkoppelbestand(terugkoppeling);
			for (BronBveTerugkoppelMelding melding : batchgegevens.getMeldingen())
			{
				melding.setBatchgegevens(batchgegevens);
				for (BronBveTerugkoppelRecord record : melding.getRecords())
				{
					record.setMelding(melding);
				}
			}
		}
		terugkoppeling.saveOrUpdate();
	}

	private int getTotaalAantalTerugkoppelmeldingen(VoorlooprecordTerugkoppeling voorlooprecord)
	{
		int totaalAantalMeldingen = 0;

		BronBveTerugkoppelbestand terugkoppeling = (BronBveTerugkoppelbestand) voorlooprecord;
		List<BronBveBatchgegevens> batchgegevens = terugkoppeling.getBatchgegevens();
		for (BronBveBatchgegevens batchgegeven : batchgegevens)
		{
			totaalAantalMeldingen += batchgegeven.getMeldingen().size();
		}
		return totaalAantalMeldingen;
	}

	private BronBveAanleverRecord getAanleverrecord(BronAanleverMelding aanleverMelding,
			BronBveTerugkoppelRecord terugkoppelRecord)
	{
		for (BronBveAanleverRecord aanleverRecord : aanleverMelding.getMeldingen())
		{
			if (isRecordEqual(aanleverRecord, terugkoppelRecord))
			{
				return aanleverRecord;
			}
		}
		return null;
	}

	private Integer getVerwachteTerugkoppelingNummer(Sectordeel sector,
			BronAanleverpunt aanleverpunt)
	{
		BronDataAccessHelper bron = DataAccessRegistry.getHelper(BronDataAccessHelper.class);

		Map<BronOnderwijssoort, Integer> nummerPerOnderwijssoort =
			bron.getVerwachteTerugkoppelBatchnummers(aanleverpunt);

		for (BronOnderwijssoort soort : nummerPerOnderwijssoort.keySet())
		{
			if (soort.equals(BronOnderwijssoort.valueOf(sector)))
			{
				return nummerPerOnderwijssoort.get(soort);
			}
		}
		return null;
	}

	private boolean isRecordEqual(BronBveAanleverRecord aanleverRecord,
			BronBveTerugkoppelRecord terugkoppelRecord)
	{
		int aanleverType = aanleverRecord.getRecordType();
		int terugkoppelType = terugkoppelRecord.getRecordType();
		if (terugkoppelType == BVE_TERUGKOPPELING_SIGNAAL)
		{
			if (aanleverType + 100 == terugkoppelRecord.getRecordsoortTerugkoppeling())
				return true;
			else
				return false;
		}

		// record types zijn gelijk genummerd in de range 300 en 400. Dus aanleverType +
		// 100 komt overeen met het terugkoppelType volgens het PvE. Is dit niet het
		// geval, dan zijn de records verschillend.
		if (aanleverType + 100 != terugkoppelType)
			return false;

		if (aanleverType == BVE_AANLEVERING_PERSOONSGEGEVENS
			|| aanleverType == BVE_AANLEVERING_WIJZIGING_SLEUTELGEGEVENS)
			return true;

		// vanaf dit moment geldt dat de verbintenis gelijk moet zijn
		boolean verbintenisGelijk =
			aanleverRecord.getInschrijvingsvolgnummer().equals(
				terugkoppelRecord.getInschrijvingsvolgnummer());
		if (!verbintenisGelijk)
			return false;

		switch (aanleverType)
		{
			case BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS:
			case BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS:
			case BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS:
				return true;
			case BVE_AANLEVERING_BO_PERIODEGEGEVENS:
				return TimeUtil.getInstance().datesEqual(
					aanleverRecord.getDatumIngangPeriodegegevensInschrijving(),
					terugkoppelRecord.getDatumIngangPeriodegegevensInschrijving());
			case BVE_AANLEVERING_BO_BPVGEGEVENS:
				return aanleverRecord.getBpvVolgnummer().equals(
					terugkoppelRecord.getBpvVolgnummer());
			case BVE_AANLEVERING_BO_EXAMENGEGEVENS:
				return aanleverRecord.getBehaaldeDeelKwalificatie().equals(
					terugkoppelRecord.getBehaaldeDeelKwalificatie());
			case BVE_AANLEVERING_ED_RESULTAATGEGEVENS:
				return aanleverRecord.getVoltooideOpleiding().equals(
					terugkoppelRecord.getVoltooideOpleiding());
			case BVE_AANLEVERING_ED_VAKGEGEVENS:
				return aanleverRecord.getVakvolgnummer().equals(
					terugkoppelRecord.getVakvolgnummer());
			case BVE_AANLEVERING_ED_NT2VAARDIGHEDEN:
				return aanleverRecord.getVakvolgnummer().equals(
					terugkoppelRecord.getVakvolgnummer())
					&& aanleverRecord.getNT2Vaardigheid().equals(
						terugkoppelRecord.getNT2Vaardigheid());
			case BVE_AANLEVERING_VAVO_EXAMENGEGEVENS:
				return aanleverRecord.getExamen().equals(terugkoppelRecord.getExamen())
					&& aanleverRecord.getExamenjaar().equals(terugkoppelRecord.getExamenjaar());
			case BVE_AANLEVERING_VAVO_VAKGEGEVENS:
				return (aanleverRecord.getExamen() == null || aanleverRecord.getExamen().equals(
					terugkoppelRecord.getExamen()))
					&& (aanleverRecord.getExamenjaar() == null || aanleverRecord.getExamenjaar()
						.equals(terugkoppelRecord.getExamenjaar()))
					&& aanleverRecord.getExamenvak().equals(terugkoppelRecord.getExamenvak());
		}
		return false;
	}
}