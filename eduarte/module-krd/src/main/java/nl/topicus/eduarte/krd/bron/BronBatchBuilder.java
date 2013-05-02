package nl.topicus.eduarte.krd.bron;

import static nl.topicus.cobra.util.StringUtil.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.AdministratiePakket;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.BronMeldingOnderdeel;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.krd.dao.helpers.BronAanleverpuntDataAccessHelper;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.dao.helpers.BronSchooljaarStatusDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.BronExamenverzameling;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.BronSchooljaarStatus;
import nl.topicus.eduarte.krd.entities.bron.BronStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchBVE;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchVOExamengegevens;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchVOInschrijvingen;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronExamenresultaatVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronSchooljaarStatusZoekFilter;
import nl.topicus.onderwijs.duo.bron.BatchBuilder;
import nl.topicus.onderwijs.duo.bron.BronException;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.IBronBatch;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Sectordeel;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.VerantwoordelijkeAanleverbestand;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;
import nl.topicus.onderwijs.duo.bron.vo.batches.VOVoorloopRecordDecentraal;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMelding;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.VerantwoordelijkeAanlevering;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BronBatchBuilder
{
	private static final Logger log = LoggerFactory.getLogger(BronBatchBuilder.class);

	private BronMeldingZoekFilter filter;

	private List<IBronBatch> batches = new ArrayList<IBronBatch>();

	private String errorMelding = "";

	private BronExamenverzameling examenverzameling;

	/**
	 * Deze constructor alleen gebruiken voor het maken van een examenbatch voor vo of
	 * vavo
	 */
	public BronBatchBuilder(BronExamenverzameling examenverzameling)
	{
		this.examenverzameling = examenverzameling;
	}

	/**
	 * Create de batches voor de meegegeven onderwijssoort. Het Gebruik van deze class is
	 * als volgt;
	 * 
	 * 
	 * BronBatchBuilder builder = new
	 * BronBatchBuilder(BronOnderwijssoort.BEROEPSONDERWIJS, false);<br>
	 * if(!builder.createBatches(VerantwoordelijkeAanlevering.Instelling))<br>
	 * error(builder.getErrorMelding()); <br>
	 * else <br>
	 * List<IBronBatch batches = builder.getBatches();
	 * 
	 */
	public BronBatchBuilder(BronMeldingZoekFilter filter)
	{
		Asserts.assertNotNull("filter", filter);
		Asserts.assertNotNull("filter.aanleverpunt", filter.getAanleverpunt());
		Asserts.assertNotNull("filter.schooljaar", filter.getSchooljaar());
		this.filter = filter;
	}

	public boolean createBatches(VerantwoordelijkeAanlevering verantwoordelijke)
	{
		if (filter.getBronOnderwijssoort() == BronOnderwijssoort.VOORTGEZETONDERWIJS)
			return createVOBatches(filter.getAanleverpunt(), verantwoordelijke);
		return createBVEBatches(filter.getAanleverpunt(), verantwoordelijke);
	}

	public boolean createBVEBatches(BronAanleverpunt aanleverpunt,
			VerantwoordelijkeAanlevering verantwoordelijke)
	{
		BronBatchBVE batch = null;
		switch (filter.getBronOnderwijssoort())
		{
			case BEROEPSONDERWIJS:
				batch =
					createBVEBatch(Sectordeel.Beroepsonderwijs, aanleverpunt, verantwoordelijke,
						filter.getSchooljaar());
				break;
			case EDUCATIE:
				batch =
					createBVEBatch(Sectordeel.Basiseducatie, aanleverpunt, verantwoordelijke,
						filter.getSchooljaar());
				break;
			case VAVO:
				batch =
					createBVEBatch(Sectordeel.VAVO, aanleverpunt, verantwoordelijke, filter
						.getSchooljaar());
			default:
				break;
		}
		if (batch == null)
		{
			errorMelding = "Kan geen batch maken";
			return false;
		}
		List<IBronMelding> meldingen = getMeldingen();
		for (IBronMelding iMelding : meldingen)
		{
			BronAanleverMelding melding = (BronAanleverMelding) iMelding;
			melding.setBatch(batch);

			// Laatste kans om meldingen met *en* een sofinummer *en* een onderwijsnummer
			// aan te passen zodat we altijd enkel met het een of het ander communiceren.

			if (isNotEmpty(melding.getSofinummer()) && isNotEmpty(melding.getOnderwijsnummer()))
			{
				log
					.error(
						"Melding {} voor organisatie {} heeft *en* sofinummer {} *en* onderwijsnummer {} gevuld. Dit is gerepareerd.",
						new Object[] {melding.getMeldingnummer(),
							melding.getOrganisatie().getNaam(), melding.getSofinummer(),
							melding.getOnderwijsnummer()});
				melding.setOnderwijsnummer(null);
			}
			batch.addMelding(melding);
		}
		batch.berekenControleTotalen();
		if (batch.getMeldingen().isEmpty())
		{
			errorMelding = "Er staan geen meldingen in de wachtrij";
			return false;
		}
		batches.add(batch);
		return true;
	}

	public boolean createVOBatches(BronAanleverpunt aanleverpunt,
			VerantwoordelijkeAanlevering verantwoordelijke)
	{
		boolean batchAangemaakt = false;
		filter.setBronMeldingOnderdeel(BronMeldingOnderdeel.VOInschrijving);
		List<IBronMelding> meldingen = getMeldingen();
		if (!meldingen.isEmpty())
		{
			BronBatchVOInschrijvingen batchInschr =
				createInschrijvingBatchVO(aanleverpunt, verantwoordelijke);
			if (batchInschr == null)
			{
				errorMelding = "Kan geen inschrijving batch maken";
				return false;
			}

			for (IBronMelding iMelding : meldingen)
			{
				BronInschrijvingsgegevensVOMelding melding =
					(BronInschrijvingsgegevensVOMelding) iMelding;
				melding.setBatch(batchInschr);
				batchInschr.addMelding(melding);
			}
			batches.add(batchInschr);
			batchAangemaakt = true;
			batchInschr.berekenControleTotalen();

		}
		if (!batchAangemaakt)
		{
			errorMelding = "Er staan geen meldingen in de wachtrij";
			return false;
		}
		return true;
	}

	private List<IBronMelding> getMeldingen()
	{
		BronDataAccessHelper helper = DataAccessRegistry.getHelper(BronDataAccessHelper.class);

		// *altijd* op createdAt sorteren, om de juiste volgorde van de meldingen te
		// kunnen garanderen.
		filter.setOrderByList(Arrays.asList("createdAt"));
		return helper.getBronMeldingen(filter);
	}

	private BronBatchBVE createBVEBatch(Sectordeel sector, BronAanleverpunt aanleverpunt,
			VerantwoordelijkeAanlevering verantwoordelijke, Schooljaar schooljaar)
	{
		BronBatchBVE batch = new BronBatchBVE();
		BronStatus status = getBronStatus(sector, schooljaar, aanleverpunt);

		// zet het batchnummer met een voorlopige waarde (best guess). Hierdoor zal in de
		// user interface een batchnummer getoond worden, die mogelijk nog wijzigt als er
		// meerdere gebruikers te gelijkertijd een batch aanmaken voor dezelfde
		// onderwijssoort.

		switch (sector)
		{
			case Beroepsonderwijs:
				batch.setBatchNummer(aanleverpunt.incBatchNrBO());
				break;
			case Basiseducatie:
				batch.setBatchNummer(aanleverpunt.incBatchNrED());
				break;
			case VAVO:
				batch.setBatchNummer(aanleverpunt.incBatchNrVAVO());
				break;
		}
		batch.setAanmaakdatumAanleverbestand(TimeUtil.getInstance().currentDate());
		batch.setBestandSoort(BestandSoort.AANLEVERING);
		batch.setSectordeel(sector);
		batch.setLaatsteAanlevering(status.isLaatsteAanleveringIndicatie());
		batch.setVerantwoordelijkeAanlevering(getBVEVerantwoordelijke(verantwoordelijke));
		batch.setAanleverpunt(aanleverpunt);
		batch.setSchooljaar(schooljaar);
		return batch;
	}

	private BronStatus getBronStatus(Sectordeel sector, Schooljaar schooljaar,
			BronAanleverpunt aanleverpunt)
	{
		BronSchooljaarStatusDataAccessHelper helper =
			DataAccessRegistry.getHelper(BronSchooljaarStatusDataAccessHelper.class);
		BronSchooljaarStatusZoekFilter schooljaarStatusFilter =
			new BronSchooljaarStatusZoekFilter();
		schooljaarStatusFilter.setAanleverpunt(aanleverpunt);
		schooljaarStatusFilter.setSchooljaren(Arrays.asList(schooljaar));
		BronSchooljaarStatus bronSchooljaarStatus = helper.list(schooljaarStatusFilter).get(0);
		switch (sector)
		{
			case Basiseducatie:
				return bronSchooljaarStatus.getStatusED();
			case Beroepsonderwijs:
				return bronSchooljaarStatus.getStatusBO();
			case VAVO:
				return bronSchooljaarStatus.getStatusVAVO();
			default:
				return null;
		}
	}

	private VerantwoordelijkeAanleverbestand getBVEVerantwoordelijke(
			VerantwoordelijkeAanlevering verantwoordelijke)
	{
		switch (verantwoordelijke)
		{
			case Instelling:
				return VerantwoordelijkeAanleverbestand.Instelling;
			case Accountant:
				return VerantwoordelijkeAanleverbestand.Accountant;
			case Cfi:
				return VerantwoordelijkeAanleverbestand.Cfi;
			default:
				return null;
		}
	}

	private BronBatchVOExamengegevens createExamenBatchVO(BronAanleverpunt aanleverpunt,
			VerantwoordelijkeAanlevering verantwoordelijke)
	{
		BronBatchVOExamengegevens batch = new BronBatchVOExamengegevens();

		// zet het batchnummer met een voorlopige waarde (best guess). Hierdoor zal in de
		// user interface een batchnummer getoond worden, die mogelijk nog wijzigt als er
		// meerdere gebruikers te gelijkertijd een batch aanmaken voor dezelfde
		// onderwijssoort.
		batch.setBatchNummer(aanleverpunt.incBatchNrVO());

		batch.setDatumVerzending(TimeUtil.getInstance().currentDate());
		batch.setBestandSoort(BestandSoort.AANLEVERING);
		// onderstaande wordt niet voor de examenbatches gebruikt
		batch.setLaatsteAanlevering(null);
		batch.setVerantwoordelijkeAanlevering(verantwoordelijke);
		batch.setNaamSchoolAdministratiePakket(AdministratiePakket.getPakket().getNaam());
		batch.setVersieSchoolAdministratiePakket(EduArteApp.get().getVersion());
		batch.setSoortMelding(SoortMelding.Examenresultaat);
		batch.setBRINNummer(EduArteContext.get().getInstelling().getBrincode().getCode());
		batch.setAanleverpunt(aanleverpunt);
		batch.setSchooljaar(examenverzameling.getSchooljaar());
		return batch;
	}

	private BronBatchVOInschrijvingen createInschrijvingBatchVO(BronAanleverpunt aanleverpunt,
			VerantwoordelijkeAanlevering verantwoordelijke)
	{
		BronBatchVOInschrijvingen batch = new BronBatchVOInschrijvingen();

		// zet het batchnummer met een voorlopige waarde (best guess). Hierdoor zal in de
		// user interface een batchnummer getoond worden, die mogelijk nog wijzigt als er
		// meerdere gebruikers te gelijkertijd een batch aanmaken voor dezelfde
		// onderwijssoort.
		batch.setBatchNummer(aanleverpunt.incBatchNrVO());

		batch.setDatumVerzending(TimeUtil.getInstance().currentDate());
		batch.setBestandSoort(BestandSoort.AANLEVERING);
		batch.setLaatsteAanlevering(filter.getBronSchooljaarStatus().getStatusVO()
			.isLaatsteAanleveringIndicatie());
		batch.setVerantwoordelijkeAanlevering(verantwoordelijke);
		batch.setNaamSchoolAdministratiePakket(AdministratiePakket.getPakket().getNaam());
		batch.setVersieSchoolAdministratiePakket(EduArteApp.get().getVersion());
		switch (verantwoordelijke)
		{
			case Instelling:
				batch.setSoortMelding(SoortMelding.Inschrijving);
				break;
			case Accountant:
				batch.setSoortMelding(SoortMelding.AccountantsMutatie);
				break;
			default:
				batch.setSoortMelding(SoortMelding.Inschrijving);
				break;
		}
		batch.setBRINNummer(EduArteContext.get().getInstelling().getBrincode().getCode());
		batch.setAanleverpunt(aanleverpunt);
		batch.setSchooljaar(filter.getBronSchooljaarStatus().getSchooljaar());
		return batch;
	}

	private BronAanleverpunt getBronAanleverpunt()
	{
		List<BronAanleverpunt> aanleverpunten =
			DataAccessRegistry.getHelper(BronAanleverpuntDataAccessHelper.class)
				.getBronAanleverpunten();
		if (aanleverpunten.size() > 0)
			return aanleverpunten.get(0);
		return null;
	}

	public List<IBronBatch> getBatches()
	{
		return batches;
	}

	public String getErrorMelding()
	{
		return errorMelding;
	}

	public static byte[] writeBronBatch(IBronBatch batch)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			BatchBuilder builder = new BatchBuilder(batch);
			builder.setCheckRequired(false);
			builder.writeBatchToStream(baos);
		}
		catch (BronException bronException)
		{
			throw new RuntimeException(bronException);
		}
		catch (IOException ioException)
		{
			throw new RuntimeException(ioException);
		}
		return baos.toByteArray();
	}

	public static String getFilename(IBronBatch batch)
	{
		if (batch instanceof BronBatchBVE)
		{
			BronBatchBVE batchBVE = (BronBatchBVE) batch;
			String sector = "B";
			switch (batchBVE.getSectordeel())
			{
				case Beroepsonderwijs:
					sector = "B";
					break;
				case Basiseducatie:
					sector = "E";
					break;
				case VAVO:
					sector = "A";
					break;
			}
			return String.format("%-4s%03dA.%1s%02d", EduArteContext.get().getInstelling()
				.getBrincode().getCode(), batch.getBatchNummer(), sector, batch
				.getAanleverPuntNummer());
		}
		return String.format("%-4s%03dA.V%02d", ((VOVoorloopRecordDecentraal) batch)
			.getBRINNummer(), batch.getBatchNummer(), batch.getAanleverPuntNummer());
	}

	/**
	 * Methode kan alleen aangeroepen worden als de constructor met de parameter
	 * examenverzameling gebruikt is
	 */
	public IBronBatch createExamenverzamelingBatch()
	{
		if (examenverzameling == null)
			throw new RuntimeException("Examenverzameling is null");
		if (examenverzameling.getBronOnderwijssoort()
			.equals(BronOnderwijssoort.VOORTGEZETONDERWIJS))
		{
			BronBatchVOExamengegevens examenbatch =
				createExamenBatchVO(getBronAanleverpunt(), VerantwoordelijkeAanlevering.Instelling);

			for (BronExamenresultaatVOMelding examenMelding : examenverzameling
				.getVoExamenMeldingen())
			{
				examenMelding.setBronMeldingStatus(BronMeldingStatus.IN_BEHANDELING);
				examenMelding.setBatch(examenbatch);
				examenbatch.addMelding(examenMelding);
				examenMelding.saveOrUpdate();
			}
			examenbatch.berekenControleTotalen();
			examenbatch.setBestand(writeBronBatch(examenbatch));
			examenbatch.setBestandsnaam(getFilename(examenbatch));
			examenbatch.saveOrUpdate();
			examenbatch.getAanleverpunt().saveOrUpdate();
			examenverzameling.setVoBatch(examenbatch);
			examenverzameling.saveOrUpdate();
			return examenbatch;
		}
		if (examenverzameling.getBronOnderwijssoort().equals(BronOnderwijssoort.VAVO))
		{
			BronBatchBVE examenbatch =
				createBVEBatch(Sectordeel.VAVO, getBronAanleverpunt(),
					VerantwoordelijkeAanlevering.Instelling, examenverzameling.getSchooljaar());
			for (BronAanleverMelding examenMelding : examenverzameling.getVavoExamenMeldingen())
			{
				examenMelding.setBronMeldingStatus(BronMeldingStatus.IN_BEHANDELING);
				examenMelding.setBatch(examenbatch);
				examenbatch.addMelding(examenMelding);
				examenMelding.saveOrUpdate();
			}
			examenbatch.berekenControleTotalen();
			examenbatch.setBestand(writeBronBatch(examenbatch));
			examenbatch.setBestandsnaam(getFilename(examenbatch));
			examenbatch.saveOrUpdate();
			examenbatch.getAanleverpunt().saveOrUpdate();
			examenverzameling.setBveBatch(examenbatch);
			examenverzameling.saveOrUpdate();
			return examenbatch;
		}
		return null;

	}
}