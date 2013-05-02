package nl.topicus.eduarte.krd.bron.jobs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.io.StringPrintWriter;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krd.entities.BronFotobestandInlezenJobRun;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestand;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestand.BronFotoStatus;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestand.BronFotoVerwerkingsstatus;
import nl.topicus.eduarte.krd.entities.bron.foto.bve.*;
import nl.topicus.onderwijs.duo.bron.BronException;
import nl.topicus.onderwijs.duo.bron.bve.foto.pve_9_9.BronFotoRecordType;
import nl.topicus.onderwijs.duo.bron.bve.foto.pve_9_9.BronFotoType;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.FotoOnderwijssector;

import org.hibernate.HibernateException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@JobInfo(name = BronFotobestandInlezenJob.JOB_NAME)
@JobRunClass(BronFotobestandInlezenJobRun.class)
public class BronFotobestandInlezenJob extends EduArteJob
{
	public static final String JOB_NAME = "BRON Fotobestand inlezen";

	private BronFotoBOInschrijvingRecord currentBOInschrijvingRecord;

	private BronFotoVEInschrijvingRecord currentVEInschrijvingRecord;

	private BronFotoVEVAVOExamenRecord currentExamenRecord;

	private BronFotoVEVakBasiseducatieRecord currentVakRecord;

	private BronFotoVOInschrijvingRecord currentVOInschrijvingRecord;

	private BronFotobestandInlezenJobRun run;

	private String filename;

	@Override
	protected void executeJob(JobExecutionContext context) throws JobExecutionException,
			InterruptedException
	{
		run = createJobRun();
		try
		{
			File file = (File) context.getMergedJobDataMap().get("bestand");
			if (file == null)
			{
				failJob("Upload van het bestand is mislukt, geen data ontvangen");
				run.commit();
				return;
			}
			filename = file.getName();
			if (file.length() == 0)
			{
				failJob("Bestand " + filename + " is leeg, geen data ontvangen");
				run.commit();
				return;
			}
			setStatus("Leest bestand " + filename);

			List<String> lines = leesBestand(file);

			if (!valideerBestand(lines))
				return;

			BronFotobestand fotobestand = importeerBestand(lines);

			int aantalVerschillen = vergelijkBestandMetDatabase(fotobestand);

			if (aantalVerschillen == 1)
			{
				run.info("Er is 1 verschil geconstateerd tussen BRON en de database");
			}
			else
			{
				run.info("Er zijn "
					+ (aantalVerschillen == 0 ? "geen" : String.valueOf(aantalVerschillen))
					+ " verschillen geconstateerd tussen BRON en de database");
			}
			run.info("Fotobestand " + filename + " is geimporteerd en geanalyseerd");
			run.setSamenvatting("Fotobestand " + filename + " geimporteerd en geanalyseerd : "
				+ (aantalVerschillen == 0 ? "geen" : String.valueOf(aantalVerschillen)) + " "
				+ (aantalVerschillen == 1 ? "verschil" : "verschillen"));
			run.setRunEinde(TimeUtil.getInstance().currentDateTime());
			run.update();
			run.commit();

			flushAndClearHibernate();

			fotobestand.setAantalVerschillen(aantalVerschillen);
			fotobestand.setVerwerkingsstatus(BronFotoVerwerkingsstatus.Verwerkt);
			fotobestand.update();
			fotobestand.commit();
		}
		catch (InterruptedException e)
		{
			log.error("Job was interrupted: " + e.getMessage(), e);

			// Rethrow.
			throw e;
		}
		catch (HibernateException e)
		{
			failJobAndRollback(e);
		}
		catch (Exception e)
		{
			failJob(e);
			run.commit();
			throw new JobExecutionException(e);
		}
	}

	private BronFotobestandInlezenJobRun createJobRun()
	{
		BronFotobestandInlezenJobRun jobrun = new BronFotobestandInlezenJobRun();
		jobrun.setGestartDoor(getMedewerker());
		jobrun.setRunStart(TimeUtil.getInstance().currentDateTime());
		jobrun.setSamenvatting("Foto importeren gestart");
		jobrun.save();
		return jobrun;
	}

	/**
	 * Voert een snelle syntax check en inhoudelijke controle uit of het bestand wel een
	 * foto bestand is, en of de foto wel voor de juiste instelling ingelezen wordt.
	 */
	private boolean valideerBestand(List<String> lines)
	{
		if (lines.isEmpty())
		{
			failJob("Bestand " + filename + " is leeg");
			return false;
		}

		String eersteRegel = lines.get(0);
		String[] eersteVelden = eersteRegel.split("~", -1);
		if (eersteVelden == null || eersteVelden.length < 2)
		{
			failJob("Bestand " + filename + " is geen geldig fotobestand");
			return false;
		}
		if (BronFotoRecordType.FTO != BronFotoRecordType.parse(eersteVelden[0]))
		{
			failJob("Bestand " + filename
				+ " is geen geldig fotobestand, eerste record is geen FTO");
			return false;
		}
		boolean bestandIsGeldigFotoBestand = true;
		for (int i = 0; i < lines.size(); i++)
		{
			String line = lines.get(i);
			String[] velden = line.split("~", -1);
			if (velden == null)
			{
				run.error(String.format("Regel %d: bevat geen velden", i + 1));
				bestandIsGeldigFotoBestand = false;
				continue;
			}
			else if (velden.length < 2)
			{
				run.error(String.format("Regel %d: bevat niet minstens 2 velden", i + 1));
				bestandIsGeldigFotoBestand = false;
				continue;
			}
			else if (BronFotoRecordType.parse(velden[0]) == null)
			{
				run.error(String.format("Regel %d: %s is een onbekend record type", i + 1,
					velden[0]));
				bestandIsGeldigFotoBestand = false;
				continue;
			}
			else if (FotoOnderwijssector.parse(velden[1]) == null)
			{
				run.error(String.format("Regel %d: %s is een onbekende onderwijssoort", i + 1,
					velden[1]));
				bestandIsGeldigFotoBestand = false;
				continue;
			}

			BronFotoRecordType type = BronFotoRecordType.valueOf(velden[0]);
			FotoOnderwijssector sector = FotoOnderwijssector.valueOf(velden[1]);

			if (type == BronFotoRecordType.IFO)
			{
				String fotoBrin = null;
				switch (sector)
				{
					case BO:
						fotoBrin = velden[8];
						break;
					case VE:
						fotoBrin = velden[11];
						break;
					case VO:
						fotoBrin = velden[11];
						break;
				}
				String instellingBrin =
					EduArteContext.get().getInstelling().getBrincode().getCode();
				if (!instellingBrin.equals(fotoBrin))
				{
					failJob(String
						.format(
							"Regel %d: BRIN %s in de foto komt niet overeen met de BRIN van de instelling (%s)",
							i + 1, fotoBrin, instellingBrin));
					return false;
				}
			}
		}
		if (!bestandIsGeldigFotoBestand)
		{
			failJob("Bestand " + filename
				+ " bevat ongeldige records. Klik hier voor meer informatie");
		}
		return bestandIsGeldigFotoBestand;
	}

	private BronFotobestand importeerBestand(List<String> lines) throws InterruptedException
	{
		setStatus("Importeert bestand " + filename);
		BronFotobestand fotobestand = new BronFotobestand();
		fotobestand.setBestandsnaam(filename);
		fotobestand.setIngelezenDoor(getMedewerker());
		fotobestand.setVerwerkingsstatus(BronFotoVerwerkingsstatus.InBehandeling);
		fotobestand.setInleesdatum(getDatumTijdOpgestart());
		fotobestand.save();
		int counter = 0;
		for (String line : lines)
		{
			BronFotoRecord record = parseLine(line, fotobestand);
			if (record != null)
			{
				record.save();
			}
			counter++;
			setProgress(counter, lines.size() * 2);
			if (counter % 100 == 0)
			{
				flushAndClearHibernate();
			}
		}
		flushAndClearHibernate();
		return fotobestand;
	}

	private int vergelijkBestandMetDatabase(BronFotobestand fotobestand)
			throws InterruptedException
	{
		setStatus("Vergelijkt foto " + filename + " met database");
		int aantalVerschillen = 0;
		if (fotobestand.getType() == BronFotoType.BO)
		{
			BronFotoVergelijker<BronFotoBOInschrijvingRecord> vergelijker =
				new BronFotoVergelijker<BronFotoBOInschrijvingRecord>();
			aantalVerschillen =
				vergelijker.vergelijkFotoMetDatabase(this, fotobestand, run,
					BronFotoBOInschrijvingRecord.class);
		}
		else if (fotobestand.getType() == BronFotoType.ED_VAVO)
		{
			BronFotoVergelijker<BronFotoVEInschrijvingRecord> vergelijker =
				new BronFotoVergelijker<BronFotoVEInschrijvingRecord>();
			aantalVerschillen =
				vergelijker.vergelijkFotoMetDatabase(this, fotobestand, run,
					BronFotoVEInschrijvingRecord.class);
		}
		else if (fotobestand.getType() == BronFotoType.VO)
		{
			BronFotoVergelijker<BronFotoVOInschrijvingRecord> vergelijker =
				new BronFotoVergelijker<BronFotoVOInschrijvingRecord>();
			aantalVerschillen =
				vergelijker.vergelijkFotoMetDatabase(this, fotobestand, run,
					BronFotoVOInschrijvingRecord.class);
		}
		return aantalVerschillen;
	}

	private BronFotoRecord parseLine(String line, BronFotobestand fotobestand)
	{
		BronFotoRecord record = null;
		String[] velden = line.split("~", -1);
		if (velden != null && velden.length >= 2)
		{
			BronFotoRecordType recordtype = BronFotoRecordType.valueOf(velden[0]);
			FotoOnderwijssector sector = FotoOnderwijssector.valueOf(velden[1]);
			if (sector == FotoOnderwijssector.BO)
			{
				if (recordtype == BronFotoRecordType.FTO)
				{
					record = new BronFotoBORecord(velden);
					fotobestand.setType(BronFotoType.BO);
					fotobestand.setPeildatum(record.getTeldatum());
					fotobestand.setStatus(BronFotoStatus.parse(record.getStatus()));
					fotobestand.update();
				}
				else if (recordtype == BronFotoRecordType.IFO)
				{
					record = new BronFotoBOInstellingRecord(velden);
					fotobestand.setAanmaakdatum(((BronFotoBOInstellingRecord) record)
						.getAanmaakdatum());
					fotobestand.setControletotaalAccountant(((BronFotoBOInstellingRecord) record)
						.getControletotaalAccountant());
					fotobestand.update();
				}
				else if (recordtype == BronFotoRecordType.ISG)
				{
					currentBOInschrijvingRecord = new BronFotoBOInschrijvingRecord(velden);
					record = currentBOInschrijvingRecord;
				}
				else if (recordtype == BronFotoRecordType.EXN)
				{
					record =
						new BronFotoBODiplomaKwalificatieRecord(velden, currentBOInschrijvingRecord);
				}
				else if (recordtype == BronFotoRecordType.BPV)
				{
					record = new BronFotoBOBPVRecord(velden, currentBOInschrijvingRecord);
				}
				else if (recordtype == BronFotoRecordType.CTR)
				{
					record = new BronFotoBOControleRecordInstelling(velden);
				}
				else if (recordtype == BronFotoRecordType.OOE)
				{
					record = new BronFotoBOOnderwijsontvangendeRecord(velden);
				}
				else if (recordtype == BronFotoRecordType.GTR)
				{
					// ignore, deze hoeven we niet te parsen
				}
			}
			if (sector == FotoOnderwijssector.VE)
			{
				if (recordtype == BronFotoRecordType.FTO)
				{
					record = new BronFotoVERecord(velden);
					fotobestand.setType(BronFotoType.ED_VAVO);
					fotobestand.setPeildatum(record.getTeldatum());
					fotobestand.setStatus(BronFotoStatus.parse(record.getStatus()));
					fotobestand.update();
				}
				else if (recordtype == BronFotoRecordType.IFO)
				{
					record = new BronFotoVEInstellingRecord(velden);
					fotobestand.setAanmaakdatum(((BronFotoVEInstellingRecord) record)
						.getAanmaakdatum());
					fotobestand.update();
				}
				else if (recordtype == BronFotoRecordType.ISG)
				{
					currentVEInschrijvingRecord = new BronFotoVEInschrijvingRecord(velden);
					record = currentVEInschrijvingRecord;
				}
				else if (recordtype == BronFotoRecordType.VED)
				{
					currentVakRecord =
						new BronFotoVEVakBasiseducatieRecord(velden, currentVEInschrijvingRecord);
					record = currentVakRecord;
				}
				else if (recordtype == BronFotoRecordType.VRD)
				{
					record = new BronFotoVEVaardigheidNT2Record(velden, currentVakRecord);
				}
				else if (recordtype == BronFotoRecordType.EVA)
				{
					currentExamenRecord =
						new BronFotoVEVAVOExamenRecord(velden, currentVEInschrijvingRecord);
					record = currentExamenRecord;
				}
				else if (recordtype == BronFotoRecordType.VVA)
				{
					record = new BronFotoVEVAVOExamenvakRecord(velden, currentExamenRecord);
				}
				else if (recordtype == BronFotoRecordType.CTR)
				{
					record = new BronFotoVEControleRecordInschrijving(velden);
				}
				else if (recordtype == BronFotoRecordType.OOE)
				{
					record = new BronFotoVEOnderwijsontvangendeRecord(velden);
				}
				else if (recordtype == BronFotoRecordType.GTR)
				{
					// ignore, deze hoeven we niet te parsen
				}
			}
			if (sector == FotoOnderwijssector.VO)
			{
				if (recordtype == BronFotoRecordType.FTO)
				{
					record = new BronFotoVORecord(velden);
					fotobestand.setType(BronFotoType.VO);
					fotobestand.setPeildatum(record.getTeldatum());
					fotobestand.setStatus(BronFotoStatus.parse(record.getStatus()));
					fotobestand.update();
				}
				else if (recordtype == BronFotoRecordType.IFO)
				{
					record = new BronFotoVOInstellingRecord(velden);
					fotobestand.setAanmaakdatum(((BronFotoVOInstellingRecord) record)
						.getAanmaakdatum());
					fotobestand.setControletotaalAccountant(((BronFotoVOInstellingRecord) record)
						.getControletotaalAccountant());
					fotobestand.update();
				}
				else if (recordtype == BronFotoRecordType.ISG)
				{
					currentVOInschrijvingRecord = new BronFotoVOInschrijvingRecord(velden);
					record = currentVOInschrijvingRecord;
				}
				else if (recordtype == BronFotoRecordType.OLG)
				{
					record = new BronFotoVOOpleidingRecord(velden, currentVOInschrijvingRecord);
				}
				else if (recordtype == BronFotoRecordType.OOE)
				{
					record = new BronFotoVOOnderwijsontvangendeRecord(velden);
				}
				else if (recordtype == BronFotoRecordType.GTR)
				{
					// ignore, deze hoeven we niet te parsen
				}
			}
		}
		if (record != null)
		{
			record.setBestand(fotobestand);
		}
		return record;
	}

	private List<String> leesBestand(File file) throws IOException
	{
		List<String> res = new ArrayList<String>();

		InputStream stream = new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "Cp819"));
		String line = reader.readLine();
		while (StringUtil.isNotEmpty(line))
		{
			res.add(line);
			line = reader.readLine();
		}
		reader.close();
		return res;
	}

	@Override
	protected void setProgress(int count, int total) throws InterruptedException
	{
		super.setProgress(count, total);
	}

	@Override
	protected void flushAndClearHibernate()
	{
		super.flushAndClearHibernate();
	}

	void failJob(String message)
	{
		setStatus("Verwerken van bestand is mislukt");

		run.setSamenvatting("Inlezen fotobestand mislukt: " + message);
		run.error(message);
		run.setRunEinde(TimeUtil.getInstance().currentDateTime());
		run.saveOrUpdate();
		run.commit();
	}

	void failJob(Exception e)
	{
		String instelling = EduArteContext.get().getInstelling().getNaam();
		log.error("Inlezen fotobestand mislukt: " + instelling + "/" + filename, e);

		run.setSamenvatting("Inlezen van het fotobestand is mislukt, klik voor meer informatie");
		run.setRunEinde(TimeUtil.getInstance().currentDateTime());

		setStatus("Verwerken van bestand is mislukt");

		if (e instanceof BronException)
		{
			run.error(e.getMessage());
		}
		else
		{
			StringPrintWriter spw = new StringPrintWriter();
			e.printStackTrace(spw);
			run.error("Inlezen van het fotobestand is mislukt door een onverwachte fout:\n"
				+ spw.getString());
		}
		run.saveOrUpdate();
		run.commit();
	}

	void failJobAndRollback(HibernateException e) throws JobExecutionException
	{
		// Handel alle andere excepties wel af
		// Rollback en clear.
		BatchDataAccessHelper< ? > batchHelper =
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class);
		batchHelper.batchRollback();

		SessionDataAccessHelper sessionHelper =
			DataAccessRegistry.getHelper(SessionDataAccessHelper.class);
		sessionHelper.clearSession();

		BronFotobestandInlezenJobRun jobrun = createJobRun();

		jobrun.setRunEinde(TimeUtil.getInstance().currentDateTime());
		jobrun
			.setSamenvatting("Er is een fout opgetreden tijdens het inlezen van het fotobestand: "
				+ e.getLocalizedMessage());
		jobrun.save();
		jobrun.commit();
		throw new JobExecutionException(e);
	}
}
