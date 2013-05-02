package nl.topicus.eduarte.bron;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.ResourceUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.InstellingDataAccessHelper;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.bron.BronChanges;
import nl.topicus.eduarte.krd.bron.BronController;
import nl.topicus.eduarte.krd.bron.BronControllerVo;
import nl.topicus.eduarte.krd.bron.BronDeelnemerChanges;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchVOExamengegevens;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronExamenresultaatVOMelding;
import nl.topicus.onderwijs.duo.bron.BatchBuilder;
import nl.topicus.onderwijs.duo.bron.BronException;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMelding;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.VerantwoordelijkeAanlevering;

import org.apache.wicket.model.Model;
import org.junit.Before;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = "file:src/test/java/nl/topicus/eduarte/bron/spring-test.xml")
public abstract class BronSchakelTestCaseDB extends AbstractJUnit4SpringContextTests
{
	protected TimeUtil timeUtil;

	protected BronControllerVo controllerVO;

	protected BronController controller;

	protected BronBatchVOExamengegevens examenBatchVO;

	private Instelling instelling;

	@Before
	public void setupInstelling()
	{
		instelling = getInstelling();
		timeUtil = TimeUtil.getInstance();
		controller = new BronController();
		controllerVO = new BronControllerVo();
		EduArteContext.get().setPeildatumModel(new Model<Date>(timeUtil.currentDate()));
		EduArteContext.get().setOrganisatie(instelling);
	}

	private Instelling getInstelling()
	{
		InstellingDataAccessHelper instellingDAH =
			DataAccessRegistry.getHelper(InstellingDataAccessHelper.class);
		return instellingDAH.get("Educuscollege");
	}

	protected Date asDate(Integer i)
	{
		Date ret = timeUtil.parseDateString(i.toString());
		if (ret == null)
			throw new IllegalArgumentException(i + " is geen geldige waarde voor een Datum");
		return ret;
	}

	protected void addChange(Entiteit entiteit, String property, Object oldValue, Object newValue)
			throws Exception
	{
		String[] propertyNames = {property};
		Object[] previousState = {oldValue};
		Object[] currentState = {newValue};

		controller.controleerOpWijzigingenOpBronVelden(entiteit, currentState, previousState,
			propertyNames);
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
			BatchBuilder builder = new BatchBuilder(examenBatchVO);
			os = new ByteArrayOutputStream();
			builder.writeBatchToStream(os);
		}
		finally
		{
			ResourceUtil.flush(os);
			ResourceUtil.closeQuietly(os);
		}
	}

	protected void writeBronBatch(BronBatchVOExamengegevens batch) throws BronException,
			IOException, FileNotFoundException
	{
		BatchBuilder builder = new BatchBuilder(batch);
		builder.writeBatchToStream(System.err);
		FileOutputStream fos = null;
		try
		{
			String filename = getFilename(batch);
			fos = new FileOutputStream(filename);
			builder.writeBatchToStream(fos);
		}
		finally
		{
			ResourceUtil.flush(fos);
			ResourceUtil.closeQuietly(fos);
		}
		debugFile(new FileInputStream(getFilename(batch)));
	}

	private String getFilename(BronBatchVOExamengegevens batch)
	{
		return String.format("%-4s%03dA.V%02d", batch.getBRINNummer(), batch.getBatchNummer(),
			batch.getAanleverPuntNummer());
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

	public BronBatchVOExamengegevens maakBronExamenVOBatch()
	{
		BronBatchVOExamengegevens batch = createExamenBatchVO();

		BronChanges recordedChanges = controller.getRecordedChanges();
		for (Deelnemer deelnemer : recordedChanges.getGewijzigdeDeelnemers())
		{
			BronDeelnemerChanges changes = recordedChanges.getWijzigingen(deelnemer);
			List<Verbintenis> gewijzigdeVerbintenissen = new ArrayList<Verbintenis>();
			for (Verbintenis verbintenis : deelnemer.getVOVerbintenissen())
			{
				if (changes.heeftVOExamenresultaatWijzigingenVoor(verbintenis))
					gewijzigdeVerbintenissen.add(verbintenis);
			}
			for (Verbintenis verbintenis : gewijzigdeVerbintenissen)
			{
				BronExamenresultaatVOMelding melding =
					controllerVO.createExamenMeldingen(verbintenis.getLaatsteExamendeelname());
				if (melding != null)
					batch.addMelding(melding);
			}
		}
		return batch;
	}

	private BronBatchVOExamengegevens createExamenBatchVO()
	{
		BronAanleverpunt aanleverpunt = new BronAanleverpunt();
		aanleverpunt.setLaatsteBatchNrVO(-1);
		aanleverpunt.setLaatsteBatchNrBO(99);
		aanleverpunt.setLaatsteBatchNrED(199);
		aanleverpunt.setLaatsteBatchNrVAVO(299);
		aanleverpunt.setNummer(0);

		BronBatchVOExamengegevens batch = new BronBatchVOExamengegevens();
		aanleverpunt.setLaatsteBatchNrVO(aanleverpunt.getLaatsteBatchNrVO() + 1);
		batch.setBatchNummer(aanleverpunt.getLaatsteBatchNrVO());
		batch.setDatumVerzending(TimeUtil.getInstance().currentDate());
		batch.setBestandSoort(BestandSoort.AANLEVERING);
		batch.setLaatsteAanlevering(null);
		batch.setVerantwoordelijkeAanlevering(VerantwoordelijkeAanlevering.Instelling);
		batch.setNaamSchoolAdministratiePakket("EduArte");
		batch.setVersieSchoolAdministratiePakket("1.0");
		batch.setSoortMelding(SoortMelding.Examenresultaat);
		batch.setBRINNummer("00DI");
		batch.setAanleverpunt(aanleverpunt);

		return batch;
	}
}
