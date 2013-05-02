package nl.topicus.eduarte.krd.bron.schakeltest;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.ResourceUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.begineinddatum.IBeginEinddatumEntiteit;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.examen.Examenstatus;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.Brin.Onderwijssector;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.eduarte.krd.bron.BronChanges;
import nl.topicus.eduarte.krd.bron.BronController;
import nl.topicus.eduarte.krd.bron.BronControllerVo;
import nl.topicus.eduarte.krd.bron.MockBronHibernateDataAccessHelper;
import nl.topicus.eduarte.krd.bron.MockBronSchooljaarStatusDataAccessHelper;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.dao.helpers.BronSchooljaarStatusDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpuntLocatie;
import nl.topicus.eduarte.krd.entities.bron.BronSchooljaarStatus;
import nl.topicus.eduarte.krd.entities.bron.BronStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.AbstractBronBatchVO;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchBVE;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchVOExamengegevens;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchVOInschrijvingen;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronExamenresultaatVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.eduarte.krd.zoekfilters.BronSchooljaarStatusZoekFilter;
import nl.topicus.eduarte.tester.EduArteTestCase;
import nl.topicus.onderwijs.duo.bron.BatchBuilder;
import nl.topicus.onderwijs.duo.bron.BronException;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Sectordeel;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.VerantwoordelijkeAanleverbestand;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMelding;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.VerantwoordelijkeAanlevering;

import org.junit.Before;

public abstract class BronSchakelTestCase extends EduArteTestCase
{
	protected final Schooljaar schooljaar0809;

	protected final Schooljaar schooljaar0910;

	protected final Schooljaar schooljaar1011;

	protected final Schooljaar huidigSchooljaar;

	protected final Schooljaar vorigSchooljaar;

	protected final Schooljaar volgendSchooljaar;

	protected TimeUtil timeUtil;

	protected BronController controller;

	protected BronBatchBVE batchBVE;

	protected BronBatchVOInschrijvingen inschrijvingBatchVO;

	protected BronBatchVOExamengegevens examenBatchVO;

	protected Deelnemer deelnemer;

	protected Verbintenis verbintenis;

	protected Plaatsing plaatsing;

	protected BPVInschrijving bpvInschrijving;

	protected BronBuilder builder;

	private BronAanleverpunt aanleverpunt;

	protected Examendeelname deelname1;

	private Examenstatus exGeslaagd;

	private Examenstatus exVerwijderd;

	private Examenstatus exAfgewezen;

	private Examenstatus exCertificaten;

	protected Locatie locatie;

	protected Persoon persoon;

	protected BronSchakelTestCase()
	{
		huidigSchooljaar = Schooljaar.huidigSchooljaar();
		vorigSchooljaar = huidigSchooljaar.getVorigSchooljaar();
		volgendSchooljaar = huidigSchooljaar.getVolgendSchooljaar();
		schooljaar0809 = Schooljaar.valueOf(2008);
		schooljaar0910 = Schooljaar.valueOf(2009);
		schooljaar1011 = Schooljaar.valueOf(2010);
	}

	@Before
	public void setupInstelling()
	{
		registerQuietly(BronDataAccessHelper.class, new MockBronHibernateDataAccessHelper(tester));
		registerQuietly(BronSchooljaarStatusDataAccessHelper.class,
			new MockBronSchooljaarStatusDataAccessHelper(tester));

		tester.getInstelling().setBrincode(new Brin("01AA"));
		timeUtil = TimeUtil.getInstance();
		controller = new BronController();
		deelnemer = null;
		verbintenis = null;

		locatie = new Locatie();
		locatie.setNaam("BRON Schakeltest locatie");
		locatie.saveOrUpdate();
		aanleverpunt = new BronAanleverpunt();
		aanleverpunt.saveOrUpdate();

		BronAanleverpuntLocatie aanleverpuntLocatie = new BronAanleverpuntLocatie();
		aanleverpuntLocatie.setAanleverpunt(aanleverpunt);
		aanleverpuntLocatie.setLocatie(locatie);
		aanleverpuntLocatie.saveOrUpdate();
		aanleverpunt.getLocaties().add(aanleverpuntLocatie);

		tester.clearTransactionLog();
	}

	private <T extends DataAccessHelper< ? >> void registerQuietly(Class<T> interfaceClass, T helper)
	{
		try
		{
			tester.register(interfaceClass, helper);
		}
		catch (Exception e)
		{
		}
	}

	protected final BronBatchBVE maakBronBOBatch()
	{
		return maakBronBatch(Sectordeel.Beroepsonderwijs);
	}

	protected final BronBatchBVE maakBronEDBatch()
	{
		return maakBronBatch(Sectordeel.Basiseducatie);
	}

	protected final BronBatchBVE maakBronVAVOBatch()
	{
		return maakBronBatch(Sectordeel.VAVO);
	}

	private BronBatchBVE maakBronBatch(Sectordeel sector)
	{
		BronBatchBVE batch = createBatch(sector);
		try
		{
			controller.save();
		}
		catch (BronException e)
		{
			Asserts.fail(e.toString());
		}
		addAanleverMeldingen(batch);
		batch.berekenControleTotalen();
		return batch;
	}

	private void addAanleverMeldingen(BronBatchBVE batch)
	{
		List<IdObject> objectsFromTransaction = tester.getObjectsFromTransaction();
		for (IdObject object : objectsFromTransaction)
		{
			if (object instanceof BronAanleverMelding)
			{
				batch.addMelding(BronAanleverMelding.class.cast(object));
			}
		}
	}

	private BronBatchBVE createBatch(Sectordeel sector)
	{
		BronAanleverpunt newAanleverpunt = new BronAanleverpunt();
		newAanleverpunt.setLaatsteBatchNrVO(-1);
		newAanleverpunt.setLaatsteBatchNrBO(99);
		newAanleverpunt.setLaatsteBatchNrED(199);
		newAanleverpunt.setLaatsteBatchNrVAVO(299);
		newAanleverpunt.setNummer(0);

		BronBatchBVE batch = new BronBatchBVE();
		switch (sector)
		{
			case Beroepsonderwijs:
				newAanleverpunt.setLaatsteBatchNrBO(newAanleverpunt.getLaatsteBatchNrBO() + 1);
				batch.setBatchNummer(newAanleverpunt.getLaatsteBatchNrBO());
				break;
			case Basiseducatie:
				newAanleverpunt.setLaatsteBatchNrED(newAanleverpunt.getLaatsteBatchNrED() + 1);
				batch.setBatchNummer(newAanleverpunt.getLaatsteBatchNrED());

				break;
			case VAVO:
				newAanleverpunt.setLaatsteBatchNrVAVO(newAanleverpunt.getLaatsteBatchNrVAVO() + 1);
				batch.setBatchNummer(newAanleverpunt.getLaatsteBatchNrVAVO());
				break;

			default:
				break;
		}
		batch.setAanmaakdatumAanleverbestand(TimeUtil.getInstance().currentDate());
		batch.setBestandSoort(BestandSoort.AANLEVERING);
		batch.setSectordeel(sector);
		batch.setLaatsteAanlevering(false);
		batch.setVerantwoordelijkeAanlevering(VerantwoordelijkeAanleverbestand.Instelling);
		batch.setAanleverpunt(newAanleverpunt);
		return batch;
	}

	protected final BronBatchVOInschrijvingen maakBronInschrijvingVOBatch()
	{
		BronBatchVOInschrijvingen batch = createInschrijvingBatchVO();

		try
		{
			controller.save();
		}
		catch (BronException e)
		{
			Asserts.fail(e.toString());
		}

		for (IdObject object : tester.getObjectsFromTransaction())
		{
			if (object instanceof BronInschrijvingsgegevensVOMelding)
			{
				batch.addMelding(BronInschrijvingsgegevensVOMelding.class.cast(object));
			}
		}
		batch.berekenControleTotalen();
		return batch;
	}

	private BronBatchVOInschrijvingen createInschrijvingBatchVO()
	{
		BronAanleverpunt newAanleverpunt = new BronAanleverpunt();
		newAanleverpunt.setNummer(0);
		newAanleverpunt.setLaatsteBatchNrVO(-1);
		newAanleverpunt.setLaatsteBatchNrBO(99);
		newAanleverpunt.setLaatsteBatchNrED(199);
		newAanleverpunt.setLaatsteBatchNrVAVO(299);

		BronBatchVOInschrijvingen batch = new BronBatchVOInschrijvingen();
		newAanleverpunt.setLaatsteBatchNrVO(newAanleverpunt.getLaatsteBatchNrVO() + 1);
		batch.setBatchNummer(newAanleverpunt.getLaatsteBatchNrVO());
		batch.setDatumVerzending(TimeUtil.getInstance().currentDate());
		batch.setBestandSoort(BestandSoort.AANLEVERING);
		batch.setLaatsteAanlevering(false);
		batch.setVerantwoordelijkeAanlevering(VerantwoordelijkeAanlevering.Instelling);

		batch.setNaamSchoolAdministratiePakket("EduArte");
		batch.setVersieSchoolAdministratiePakket("1.0");
		batch.setSoortMelding(SoortMelding.Inschrijving);
		batch.setBRINNummer("01OE");
		batch.setAanleverpunt(newAanleverpunt);
		return batch;
	}

	// TODO deze via de ExamenverzamelingenAanmakenJob laten lopen...
	@SuppressWarnings("hiding")
	protected final BronBatchVOExamengegevens maakBronExamenVOBatch()
	{
		BronBatchVOExamengegevens batch = createExamenBatchVO();
		BronControllerVo controllerVo = new BronControllerVo();
		BronChanges recordedChanges = controller.getRecordedChanges();
		for (Verbintenis verbintenis : recordedChanges.getGewijzigdeVerbintenissen())
		{
			BronExamenresultaatVOMelding melding =
				controllerVo.createExamenMeldingen(verbintenis.getLaatsteExamendeelname());
			if (melding != null)
				batch.addMelding(melding);
		}
		batch.berekenControleTotalen();
		return batch;
	}

	private BronBatchVOExamengegevens createExamenBatchVO()
	{
		BronAanleverpunt newAanleverpunt = new BronAanleverpunt();
		newAanleverpunt.setLaatsteBatchNrVO(-1);
		newAanleverpunt.setLaatsteBatchNrBO(99);
		newAanleverpunt.setLaatsteBatchNrED(199);
		newAanleverpunt.setLaatsteBatchNrVAVO(299);
		newAanleverpunt.setNummer(0);

		BronBatchVOExamengegevens batch = new BronBatchVOExamengegevens();
		newAanleverpunt.setLaatsteBatchNrVO(newAanleverpunt.getLaatsteBatchNrVO() + 1);
		batch.setBatchNummer(newAanleverpunt.getLaatsteBatchNrVO());
		batch.setDatumVerzending(TimeUtil.getInstance().currentDate());
		batch.setBestandSoort(BestandSoort.AANLEVERING);
		batch.setLaatsteAanlevering(null);
		batch.setVerantwoordelijkeAanlevering(VerantwoordelijkeAanlevering.Instelling);
		batch.setNaamSchoolAdministratiePakket("EduArte");
		batch.setVersieSchoolAdministratiePakket("1.0");
		batch.setSoortMelding(SoortMelding.Examenresultaat);
		batch.setBRINNummer("00DI");
		batch.setAanleverpunt(newAanleverpunt);

		return batch;
	}

	protected Date asDate(Integer i)
	{
		Date ret = timeUtil.parseDateString(i.toString());
		if (ret == null)
			throw new IllegalArgumentException(i + " is geen geldige waarde voor een Datum");
		return ret;
	}

	protected List<Integer> getRecordTypesFromBatch()
	{
		List<Integer> result = new ArrayList<Integer>();

		for (BronAanleverMelding melding : batchBVE.getMeldingen())
		{
			result.addAll(getRecordTypes(melding));
		}
		return result;
	}

	protected List<Integer> getRecordTypes(BronAanleverMelding melding)
	{
		List<Integer> result = new ArrayList<Integer>();

		result.add(305);
		for (BronBveAanleverRecord record : melding.getMeldingen())
		{
			result.add(record.getRecordType());
		}
		return result;
	}

	protected List<SoortMutatie> getMutaties(BronAanleverMelding melding)
	{
		List<SoortMutatie> result = new ArrayList<SoortMutatie>();

		for (BronBveAanleverRecord record : melding.getMeldingen())
		{
			result.add(record.getSoortMutatie());
		}
		return result;
	}

	protected <T, V extends T> void addChange(Entiteit entiteit, String property, T oldValue,
			V newValue) throws BronException
	{
		Asserts.assertNotNull(entiteit.getClass().getSimpleName() + "." + property, ReflectionUtil
			.findProperty(entiteit.getClass(), property));

		String[] propertyNames = {property};
		Object[] previousState = {oldValue};
		Object[] currentState = {newValue};

		controller.controleerOpWijzigingenOpBronVelden(entiteit, currentState, previousState,
			propertyNames);
	}

	protected BronTestData add(int deelnemernummer, Integer bsn, Integer onderwijsnummer,
			Integer geboortedatum, String postcode, String land, Integer inschrijfdatum,
			Integer opleiding, MBOLeerweg leerweg, Bekostigd bekostiging)
	{
		builder = new BronBuilder();
		builder
			.buildDeelnemer(deelnemernummer, bsn, onderwijsnummer, geboortedatum, postcode, land);
		builder.addVerbintenisMBO(inschrijfdatum, opleiding, leerweg, bekostiging);

		BronTestData data = builder.build();
		deelnemer = data.getDeelnemer();
		persoon = data.getDeelnemer().getPersoon();
		verbintenis = data.getVerbintenis();

		verbintenis.setLocatie(locatie);

		tester.clearTransactionLog();

		return data;
	}

	protected BronTestData add(int deelnemernummer, Integer bsn, Integer onderwijsnummer,
			Integer geboortedatum, String postcode, String land, int inschrijfdatum, int opleiding,
			Geslacht geslacht)
	{
		builder = new BronBuilder();
		builder
			.buildDeelnemer(deelnemernummer, bsn, onderwijsnummer, geboortedatum, postcode, land);
		builder.addVerbintenisVO(inschrijfdatum, opleiding);

		BronTestData data = builder.build();
		deelnemer = data.getDeelnemer();
		deelnemer.getPersoon().setGeslacht(geslacht);
		verbintenis = data.getVerbintenis();
		return data;
	}

	protected final int getAantalInTransactie(Class< ? > clz)
	{
		int aantal = 0;

		List<IdObject> objectsFromTransaction = tester.getObjectsFromTransaction();
		for (IdObject object : objectsFromTransaction)
		{
			if (clz.isAssignableFrom(object.getClass()))
			{
				aantal++;
			}
		}
		return aantal;
	}

	/**
	 * Geeft de eerste aanlevermelding terug die in de huidige transactie is aangemaakt.
	 */
	protected final BronAanleverMelding getEersteMelding()
	{
		return getMeldingNummerN(BronAanleverMelding.class, 1);
	}

	/**
	 * Geeft de tweede aanlevermelding terug die in de huidige transactie is aangemaakt.
	 */
	protected final BronAanleverMelding getTweedeMelding()
	{
		return getMeldingNummerN(BronAanleverMelding.class, 2);
	}

	private <T> T getMeldingNummerN(Class<T> clz, int n)
	{
		List<IdObject> objectsFromTransaction = tester.getObjectsFromTransaction();
		int i = 0;
		for (IdObject object : objectsFromTransaction)
		{
			if (clz.isAssignableFrom(object.getClass()))
			{
				i++;
				if (i == n)
				{
					return clz.cast(object);
				}
			}
		}
		return null;
	}

	protected BronInschrijvingsgegevensVOMelding getEersteBronInschrijvingsgegevensVOMelding()
	{
		return getMeldingNummerN(BronInschrijvingsgegevensVOMelding.class, 1);
	}

	protected BronInschrijvingsgegevensVOMelding getTweedeBronInschrijvingsgegevensVOMelding()
	{
		return getMeldingNummerN(BronInschrijvingsgegevensVOMelding.class, 2);
	}

	protected BronInschrijvingsgegevensVOMelding getDerdeBronInschrijvingsgegevensVOMelding()
	{
		return getMeldingNummerN(BronInschrijvingsgegevensVOMelding.class, 3);
	}

	protected BronExamenresultaatVOMelding getBronExamenresultaatVOMelding(int i)
	{
		return examenBatchVO.getMeldingen().get(i);
	}

	protected void writeDummyExamenBatchVO() throws Exception
	{
		examenBatchVO = maakBronExamenVOBatch();
		examenBatchVO.setBatchNummer(1);
		OutputStream os = null;

		try
		{
			BatchBuilder bbuilder = new BatchBuilder(examenBatchVO);
			os = new ByteArrayOutputStream();
			bbuilder.writeBatchToStream(os);
		}
		finally
		{
			ResourceUtil.flush(os);
			ResourceUtil.closeQuietly(os);
		}
	}

	protected void writeDummyInschrijvingBatchVO() throws Exception
	{
		inschrijvingBatchVO = maakBronInschrijvingVOBatch();
		inschrijvingBatchVO.setBatchNummer(1);
		OutputStream os = null;

		try
		{
			BatchBuilder bbuilder = new BatchBuilder(inschrijvingBatchVO);
			os = new ByteArrayOutputStream();
			bbuilder.writeBatchToStream(os);
		}
		finally
		{
			ResourceUtil.flush(os);
			ResourceUtil.closeQuietly(os);
		}
	}

	protected void writeBronBatch(BronBatchBVE batch) throws BronException, IOException,
			FileNotFoundException
	{
		BatchBuilder bbuilder = new BatchBuilder(batch);
		bbuilder.writeBatchToStream(System.err);
		FileOutputStream fos = null;
		try
		{
			String filename = getFilename(batch);
			fos = new FileOutputStream(filename);
			bbuilder.writeBatchToStream(fos);
		}
		finally
		{
			ResourceUtil.flush(fos);
			ResourceUtil.closeQuietly(fos);
		}
		debugFile(new FileInputStream(getFilename(batch)));
	}

	protected void writeBronBatch(AbstractBronBatchVO< ? > batch) throws BronException,
			IOException, FileNotFoundException
	{
		BatchBuilder bbuilder = new BatchBuilder(batch);
		bbuilder.writeBatchToStream(System.err);
		FileOutputStream fos = null;
		try
		{
			String filename = getFilename(batch);
			fos = new FileOutputStream(filename);
			bbuilder.writeBatchToStream(fos);
		}
		finally
		{
			ResourceUtil.flush(fos);
			ResourceUtil.closeQuietly(fos);
		}
		debugFile(new FileInputStream(getFilename(batch)));
	}

	private String getFilename(AbstractBronBatchVO< ? > batch)
	{
		return String.format("target/%-4s%03dA.V%02d", batch.getBRINNummer(), batch
			.getBatchNummer(), batch.getAanleverPuntNummer());
	}

	private String getFilename(BronBatchBVE batch)
	{
		String sector = "B";
		switch (batch.getSectordeel())
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
		return String.format("target/%-4s%03dA.%1s%02d", tester.getInstelling().getBrincode()
			.getCode(), batch.getBatchNummer(), sector, batch.getAanleverPuntNummer());
	}

	private void debugFile(FileInputStream fis) throws IOException
	{
		System.err.println();
		int read = 0;
		int line = 1;
		int col = 0;
		System.err.print("     ");
		for (int i = 1; i < 60; i++)
			System.err.printf("%10d", i % 10);
		System.err.println();
		System.err.print("     ");
		for (int i = 1; i < 600; i++)
			System.err.print(i % 10);
		System.err.println();

		System.err.printf("%4d ", line);
		while (true)
		{
			read = fis.read();
			char ch = (char) read;
			if (read == -1)
			{
				System.err.printf("# %d (%d)\n", col - 2, col);
				break;
			}
			col++;
			if (read == 0x0d)
				System.err.print('%');
			else if (read == 0x0a)
			{
				System.err.printf("$ %d (%d)\n", col - 2, col);
				line++;
				col = 0;
				System.err.printf("%4d ", line);
			}
			else
				System.err.print(ch);
		}
		System.err.println();
	}

	protected Examendeelname addExamendeelname(Verbintenis kwalificatie, int datumBehaald)
	{
		deelname1 = new Examendeelname(kwalificatie);
		deelname1.setExamennummer(1);
		if (datumBehaald > 0)
			deelname1.setDatumUitslag(asDate(datumBehaald));
		deelname1.setExamenstatus(getGeslaagd());
		deelname1.setBekostigd(true);

		kwalificatie.getExamendeelnames().add(deelname1);
		return deelname1;
	}

	protected Examenstatus getGeslaagd()
	{
		if (exGeslaagd == null)
		{
			exGeslaagd = new Examenstatus(EntiteitContext.LANDELIJK);
			exGeslaagd.setNaam("Geslaagd");
			exGeslaagd.setGeslaagd(true);
			exGeslaagd.setBeginstatus(false);
			exGeslaagd.setEindstatus(true);
		}

		return exGeslaagd;
	}

	protected Examenstatus getVerwijderd()
	{
		if (exVerwijderd == null)
		{
			exVerwijderd = new Examenstatus(EntiteitContext.LANDELIJK);
			exVerwijderd.setNaam("Verwijderd");
			exVerwijderd.setGeslaagd(false);
			exVerwijderd.setBeginstatus(false);
			exVerwijderd.setEindstatus(true);
		}
		return exVerwijderd;
	}

	protected Examenstatus getAfgewezen()
	{
		if (exAfgewezen == null)
		{
			exAfgewezen = new Examenstatus(EntiteitContext.LANDELIJK);
			exAfgewezen.setNaam("Afgewezen");
			exAfgewezen.setGeslaagd(false);
			exAfgewezen.setBeginstatus(false);
			exAfgewezen.setEindstatus(true);
		}
		return exAfgewezen;
	}

	protected Examenstatus getCertificaten()
	{
		if (exCertificaten == null)
		{
			exCertificaten = new Examenstatus(EntiteitContext.LANDELIJK);
			exCertificaten.setNaam("Certificaten");
			exCertificaten.setGeslaagd(false);
			exCertificaten.setBeginstatus(false);
			exCertificaten.setEindstatus(true);
		}
		return exCertificaten;
	}

	protected Locatie getLocatie(String brinCode)
	{
		Brin brin = new Brin();
		brin.setCode(brinCode);
		brin.setOnderwijssector(Onderwijssector.VOS);

		@SuppressWarnings("hiding")
		Locatie locatie = new Locatie();
		locatie.setAfkorting("VOS");
		locatie.setNaam("VO-school");
		locatie.setBrincode(brin);
		return locatie;
	}

	protected Plaatsing getPlaatsing(int leerjaar, Verbintenis verb)
	{
		plaatsing = new Plaatsing();
		plaatsing.setVerbintenis(verb);
		plaatsing.setBegindatum(verb.getBegindatum());
		plaatsing.setLeerjaar(leerjaar);
		plaatsing.setLwoo(false);
		return plaatsing;
	}

	protected BPVInschrijving addBpvInschrijving(int afsluitdatum, int begindatum,
			int verwachteEinddatum, String leerbedrijf, int omvang)
	{
		bpvInschrijving = new BPVInschrijving(verbintenis);

		// voor bron schakeltest dienen alle BPV inschrijvingen gecommuniceerd te worden.
		bpvInschrijving.setOpnemenInBron(true);
		verbintenis.getBpvInschrijvingen().add(bpvInschrijving);
		bpvInschrijving.setVolgnummer(verbintenis.getBpvInschrijvingen().size());
		bpvInschrijving.setAfsluitdatum(asDate(afsluitdatum));
		bpvInschrijving.setBegindatum(asDate(begindatum));
		bpvInschrijving.setVerwachteEinddatum(asDate(verwachteEinddatum));
		bpvInschrijving.setTotaleOmvang(omvang);
		bpvInschrijving.setStatus(BPVStatus.Definitief);

		ExterneOrganisatie organisatie = new ExterneOrganisatie();
		organisatie.setBegindatum(IBeginEinddatumEntiteit.MIN_DATE);
		organisatie.setBpvBedrijf(true);
		bpvInschrijving.setBpvBedrijf(organisatie);
		organisatie.setId(10001L);

		BPVBedrijfsgegeven bpvBedrijf = new BPVBedrijfsgegeven(organisatie);
		bpvBedrijf.setCodeLeerbedrijf(leerbedrijf);
		bpvInschrijving.setBedrijfsgegeven(bpvBedrijf);
		bpvBedrijf.setId(10002L);

		return bpvInschrijving;
	}

	/**
	 * Zet de BRON schooljaarstatus voor het schooljaar waarin de verbintenis actief
	 * wordt.
	 */
	protected void zetSchooljaarStatus(BronStatus status)
	{
		zetSchooljaarStatus(Schooljaar.valueOf(verbintenis.getBegindatum()), status);
	}

	/**
	 * Zet de BRON schooljaarstatus voor het gegeven schooljaar.
	 */
	protected void zetSchooljaarStatus(Schooljaar schooljaar, BronStatus status)
	{
		BronSchooljaarStatusDataAccessHelper helper =
			DataAccessRegistry.getHelper(BronSchooljaarStatusDataAccessHelper.class);

		BronSchooljaarStatusZoekFilter filter = new BronSchooljaarStatusZoekFilter();
		filter.setSchooljaren(Arrays.asList(schooljaar));

		List<BronSchooljaarStatus> bronSchooljaarStatussen = helper.list(filter);
		BronSchooljaarStatus schooljaarStatus = null;
		for (BronSchooljaarStatus bronSchooljaarStatus : bronSchooljaarStatussen)
		{
			if (bronSchooljaarStatus.getSchooljaar() == schooljaar)
			{
				schooljaarStatus = bronSchooljaarStatus;
				break;
			}
		}
		if (schooljaarStatus == null)
		{
			schooljaarStatus = new BronSchooljaarStatus();
			schooljaarStatus.setSchooljaar(schooljaar);
			schooljaarStatus.setAanleverpunt(aanleverpunt);
			schooljaarStatus.saveOrUpdate();
		}
		schooljaarStatus.setStatusBO(status);
	}

	protected int aantalInTransactie(Class< ? extends IdObject> clzz)
	{
		int aantal = 0;
		List<IdObject> objectsFromTransaction1 = tester.getObjectsFromTransaction();
		for (IdObject idObject : objectsFromTransaction1)
		{
			if (clzz.isAssignableFrom(idObject.getClass()))
				aantal++;
		}
		return aantal;
	}
}
