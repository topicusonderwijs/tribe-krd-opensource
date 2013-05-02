package nl.topicus.eduarte.krd.bron.jobs;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.validation.ElfProef;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.bron.BronUtils;
import nl.topicus.eduarte.krd.dao.helpers.BronAanleverpuntDataAccessHelper;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.BronTerugkoppelbestandInlezenJobRun;
import nl.topicus.eduarte.krd.entities.bron.meldingen.AbstractBronBatchVO;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.AbstractBronVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronExamenresultaatVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronVakGegegevensVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.AbstractBronVoTerugkoppelMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.BronVoBatchgegevens;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.BronVoExamenTerugkoppelMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.BronVoSignaal;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.BronVoTerugkoppelbestand;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.BronVoVakTerugkoppelMelding;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.onderwijs.duo.bron.BronException;
import nl.topicus.onderwijs.duo.bron.vo.batches.terugkoppeling.VOTerugkoppelingDecentraal;
import nl.topicus.onderwijs.duo.bron.vo.batches.terugkoppeling.VOTerugkoppelingExamenDecentraal;
import nl.topicus.onderwijs.duo.bron.vo.batches.terugkoppeling.VOTerugkoppelingInschrijvingDecentraal;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.StatusMelding;

import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;

class BronVoTerugkoppelbestandProcessor extends BronTerugkoppelbestandProcessor
{
	private final BronTerugkoppelbestandInlezenJob job;

	private BronTerugkoppelbestandInlezenJobRun run;

	private boolean hasError = false;

	BronVoTerugkoppelbestandProcessor(
			BronTerugkoppelbestandInlezenJob bronTerugkoppelbestandInlezenJob,
			BronTerugkoppelbestandInlezenJobRun run)
	{
		job = bronTerugkoppelbestandInlezenJob;
		this.run = run;
	}

	@Override
	public boolean processBestand(byte[] contents) throws InterruptedException, BronException
	{
		BronParseResult< ? extends VOTerugkoppelingDecentraal> result =
			parseTerugkoppelbestand(contents, getVoorlooprecordSoort(contents));

		if (result.hasError())
		{
			job.failJob(run, result.getException());
			return false;
		}

		VOTerugkoppelingDecentraal voorlooprecord = result.getResult();
		job.setStatus("Verwerken van VO terugkoppelbestand");
		run.info("Verwerken van VO terugkoppelbestand " + job.getFilename());

		((BronVoTerugkoppelbestand) voorlooprecord).saveOrUpdate();

		int aantalTerugkoppelmeldingen = getTotaalAantalTerugkoppelmeldingen(voorlooprecord);
		int verwerkteTerugkoppelmeldingen = 0;

		Integer aanleverpuntnummer =
			((BronVoTerugkoppelbestand) voorlooprecord).getAanleverpuntNummer();

		BronAanleverpuntDataAccessHelper aanleverpunten =
			DataAccessRegistry.getHelper(BronAanleverpuntDataAccessHelper.class);
		BronAanleverpunt aanleverpunt = aanleverpunten.getAanleverpunt(aanleverpuntnummer);
		BronVoTerugkoppelbestand terugkoppeling = (BronVoTerugkoppelbestand) voorlooprecord;
		terugkoppeling.setBestandsnaam(run.getFilename());
		BronDataAccessHelper bron = DataAccessRegistry.getHelper(BronDataAccessHelper.class);

		List<BronVoBatchgegevens> batchgegevens = terugkoppeling.getBatches();
		for (BronVoBatchgegevens batchgegeven : batchgegevens)
		{
			Integer batchnummer = batchgegeven.getBatchNummer();
			AbstractBronBatchVO< ? > batch = bron.findVoBatch(aanleverpunt, batchnummer);
			if (batch == null)
			{
				// er kon geen batch gevonden worden bij het batchnummer, aanleverpunt
				throw new BronException(String.format(
					"Kon batch batch %03d voor %s bij aanleverpunt %s niet vinden", batchnummer,
					terugkoppeling.getBronOnderwijssoort(), aanleverpuntnummer));
			}
			run
				.info(String.format(
					"Verwerken terugkoppelmeldingen voor batch %03d voor %s bij aanleverpunt %s",
					batchnummer, terugkoppeling.getBronOnderwijssoort().toString(),
					aanleverpuntnummer));
			AbstractBronVoTerugkoppelMelding laatste =
				batchgegeven.getTerugkoppelingen().get(
					batchgegeven.getTerugkoppelingen().size() - 1);

			if (laatste.getMeldingNummer() > batch.getMeldingen().size())
			{
				// aantal meldingen voor batchgegevens komt niet overeen met het
				// aantal meldingen in de batch

				run
					.info(String
						.format(
							"Batch %03d voor %s bij aanleverpunt %s heeft minder meldingen (totaal %d) als het terugkoppelbestand verwacht (%d)",
							batchnummer, terugkoppeling.getBronOnderwijssoort(),
							aanleverpuntnummer, batch.getMeldingen().size(), laatste
								.getMeldingNummer()));
			}
			batchgegeven.setTerugkoppelbestand(terugkoppeling);
			batchgegeven.setBatch(batch);
			batchgegeven.saveOrUpdate();

			List<AbstractBronVoTerugkoppelMelding> meldingen = batchgegeven.getTerugkoppelingen();
			for (AbstractBronVoTerugkoppelMelding melding : meldingen)
			{
				job.setStatus("Verwerken melding van deelnemer "
					+ melding.getLeerlingNummerInstelling());

				Comparator<AbstractBronVOMelding> c = new Comparator<AbstractBronVOMelding>()
				{
					public int compare(AbstractBronVOMelding o1, AbstractBronVOMelding o2)
					{
						return o1.getMeldingnummer().compareTo(o2.getMeldingnummer());
					}
				};
				BronInschrijvingsgegevensVOMelding dummyMelding =
					new BronInschrijvingsgegevensVOMelding();
				dummyMelding.setMeldingNummer(melding.getMeldingNummer());
				int index = Collections.binarySearch(batch.getMeldingen(), dummyMelding, c);
				if (index < 0)
				{
					run
						.error(String
							.format(
								"Er is geen aanlevermelding gevonden voor terugkoppelmelding %d in batch %03d",
								melding.getMeldingNummer(), batchgegeven.getBatchNummer()));
					continue;
				}
				AbstractBronVOMelding aanlevermelding = batch.getMeldingen().get(index);

				if (StringUtil.isEmpty(aanlevermelding.getLeerlingNummerInstelling())
					|| StringUtil.isEmpty(melding.getLeerlingNummerInstelling())
					|| !Integer.valueOf(aanlevermelding.getLeerlingNummerInstelling()).equals(
						Integer.valueOf(melding.getLeerlingNummerInstelling())))
				{
					// zijn niet dezelfde deelnemers!!!
					run
						.error(String
							.format(
								"Terugkoppeling voor melding %d betreft niet dezelfde deelnemer (%s) als aanlevermelding (%s)",
								aanlevermelding.getMeldingnummer(), melding
									.getLeerlingNummerInstelling(), aanlevermelding
									.getLeerlingNummerInstelling()));
				}
				else
				{
					checkAndSetOnderwijsSofiNummer(aanlevermelding, melding);
				}
				melding.setAanlevermelding(aanlevermelding);
				aanlevermelding.setTerugkoppelmelding(melding);
				aanlevermelding.setBronMeldingStatus(BronMeldingStatus.valueOf(melding
					.getStatusMelding().getIdentifier()));
				for (BronVoSignaal signaal : melding.getSignalen())
				{
					signaal.setMelding(melding);
					signaal.setGeaccordeerd(false);
					signaal.saveOrUpdate();
				}
				melding.setBatchgegevens(batchgegeven);
				melding.update();
				aanlevermelding.update();

				boolean goedgekeurd = melding.getStatusMelding() == StatusMelding.GOEDGEKEURD;

				if (melding instanceof BronVoExamenTerugkoppelMelding
					&& aanlevermelding instanceof BronExamenresultaatVOMelding)
				{
					verwerkExamenmelding(melding, aanlevermelding);
				}
				BronUtils.updateStatussenNaTerugkoppeling(aanlevermelding, goedgekeurd);
				if (!goedgekeurd)
				{
					melding.getAanlevermelding().voegOpnieuwToeAanWachtrij();
				}
			}
			batch.berekenControleTotalen();
			batch.saveOrUpdate();
			job.setProgress(++verwerkteTerugkoppelmeldingen, aantalTerugkoppelmeldingen);
		}
		job.setStatus("Laatste wijzigingen opslaan");
		terugkoppeling.berekenControleGetallen();

		run.info(String.format("%d terugkoppelmeldingen verwerkt", aantalTerugkoppelmeldingen));
		terugkoppeling.save();
		return !hasError;
	}

	private void verwerkExamenmelding(AbstractBronVoTerugkoppelMelding melding,
			AbstractBronVOMelding aanlevermelding)
	{
		BronVoExamenTerugkoppelMelding examenMelding = (BronVoExamenTerugkoppelMelding) melding;
		BronExamenresultaatVOMelding aanlExamenMelding =
			(BronExamenresultaatVOMelding) aanlevermelding;
		for (BronVoVakTerugkoppelMelding vakMelding : examenMelding.getVakMeldingen())
		{
			vakMelding.setExamenMelding(examenMelding);
			vakMelding.saveOrUpdate();
			BronVakGegegevensVOMelding aanlVakMelding =
				aanlExamenMelding.getExamenvak(vakMelding.getExamenVak());
			if (aanlVakMelding != null)
			{
				vakMelding.setVakgegevensMelding(aanlVakMelding);
				vakMelding.saveOrUpdate();
				aanlVakMelding.setTerugkoppelmelding(vakMelding);
				aanlVakMelding.saveOrUpdate();
			}
			if (vakMelding.getSignalen() != null)
				for (BronVoSignaal signaal : vakMelding.getSignalen())
				{
					signaal.setMelding(melding);
					signaal.setVakMelding(vakMelding);
					signaal.setGeaccordeerd(false);
					signaal.saveOrUpdate();
				}
		}
	}

	private void checkAndSetOnderwijsSofiNummer(AbstractBronVOMelding aanlevermelding,
			AbstractBronVoTerugkoppelMelding melding)
	{
		if (!aanlevermelding.getSoortMutatie().equals(SoortMutatie.Toevoeging))
			return;
		if (aanlevermelding.getLeerlingNummerInstelling() == null
			|| melding.getLeerlingNummerInstelling() == null)
			return;
		Integer aanleverNr = Integer.valueOf(aanlevermelding.getLeerlingNummerInstelling());
		Integer terugkNr = Integer.valueOf(melding.getLeerlingNummerInstelling());
		if (aanleverNr == null || terugkNr == null || !aanleverNr.equals(terugkNr))
			return;
		DeelnemerDataAccessHelper helper =
			DataAccessRegistry.getHelper(DeelnemerDataAccessHelper.class);
		Deelnemer deelnemer = helper.getByDeelnemernummer(aanleverNr);
		if (deelnemer == null)
			return;
		if (deelnemer.getPersoon().getBsn() == null)
		{
			ElfProef elfProef = new ElfProef();
			if (melding.getSofiNummer() != null)
			{
				if (elfProef.isGeldigSofiNummer(melding.getSofiNummer().longValue()))
				{
					deelnemer.getPersoon().setBsn(melding.getSofiNummer().longValue());
					setBsnOpMeldingenInWachtrij(deelnemer, melding.getSofiNummer());
				}
			}
			else if (melding.getOnderwijsNummer() != null)
			{
				if (elfProef.isGeldigOnderwijsNummer(melding.getOnderwijsNummer().longValue()))
				{
					deelnemer.setOnderwijsnummer(melding.getOnderwijsNummer().longValue());
					setOnderwijsnummerOpMeldingInWachrij(deelnemer, melding.getOnderwijsNummer());
				}

			}
		}
	}

	private void setOnderwijsnummerOpMeldingInWachrij(Deelnemer deelnemer, Integer onderwijsNummer)
	{
		for (IBronMelding melding : getMeldingenInWachrij(deelnemer))
		{
			if (melding instanceof AbstractBronVOMelding)
			{
				AbstractBronVOMelding voMelding = (AbstractBronVOMelding) melding;
				voMelding.setOnderwijsNummer(onderwijsNummer);
			}
			if (melding instanceof BronAanleverMelding)
			{
				BronAanleverMelding bveMelding = (BronAanleverMelding) melding;
				bveMelding.setOnderwijsnummer(String.valueOf(onderwijsNummer));
			}
		}
	}

	private void setBsnOpMeldingenInWachtrij(Deelnemer deelnemer, Integer sofiNummer)
	{
		for (IBronMelding melding : getMeldingenInWachrij(deelnemer))
		{
			if (melding instanceof AbstractBronVOMelding)
			{
				AbstractBronVOMelding voMelding = (AbstractBronVOMelding) melding;
				voMelding.setSofiNummer(sofiNummer);
			}
			if (melding instanceof BronAanleverMelding)
			{
				BronAanleverMelding bveMelding = (BronAanleverMelding) melding;
				bveMelding.setSofinummer(String.valueOf(sofiNummer));
			}
		}
	}

	private List<IBronMelding> getMeldingenInWachrij(Deelnemer deelnemer)
	{
		BronMeldingZoekFilter filter = new BronMeldingZoekFilter();
		filter.setDeelnemer(deelnemer);
		filter.setMeldingStatus(BronMeldingStatus.WACHTRIJ);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			new AlwaysGrantedSecurityCheck()));
		BronDataAccessHelper helper = DataAccessRegistry.getHelper(BronDataAccessHelper.class);
		return helper.getBronMeldingen(filter);
	}

	private int getTotaalAantalTerugkoppelmeldingen(VOTerugkoppelingDecentraal voorlooprecord)
	{
		int totaalAantalMeldingen = 0;
		BronVoTerugkoppelbestand terugkoppeling = (BronVoTerugkoppelbestand) voorlooprecord;
		List<BronVoBatchgegevens> batchgegevens = terugkoppeling.getBatches();
		for (BronVoBatchgegevens batchgegeven : batchgegevens)
		{
			totaalAantalMeldingen += batchgegeven.getTerugkoppelingen().size();
		}
		return totaalAantalMeldingen;
	}

	public Class< ? extends VOTerugkoppelingDecentraal> getVoorlooprecordSoort(byte[] contents)
	{
		String recordtype = new String(contents, 13, Math.min(15, contents.length));
		if ("Examenresultaat".equals(recordtype.trim()))
		{
			return VOTerugkoppelingExamenDecentraal.class;
		}
		else
		{
			return VOTerugkoppelingInschrijvingDecentraal.class;
		}

	}
}