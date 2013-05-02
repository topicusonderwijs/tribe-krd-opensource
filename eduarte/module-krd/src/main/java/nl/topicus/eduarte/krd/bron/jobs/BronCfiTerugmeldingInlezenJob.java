package nl.topicus.eduarte.krd.bron.jobs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
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
import nl.topicus.eduarte.krd.entities.BronCfiTerugmInlezenJobRun;
import nl.topicus.eduarte.krd.entities.bron.cfi.*;
import nl.topicus.eduarte.krd.entities.bron.cfi.BronCfiTerugmelding.BronCFIStatus;
import nl.topicus.onderwijs.duo.bron.BronException;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.hibernate.HibernateException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@JobInfo(name = BronCfiTerugmeldingInlezenJob.JOB_NAME)
@JobRunClass(BronCfiTerugmInlezenJobRun.class)
public class BronCfiTerugmeldingInlezenJob extends EduArteJob
{
	public static final String JOB_NAME = "CFI-terugmelding inlezen";

	private BronCfiTerugmInlezenJobRun run;

	private String filename;

	@Override
	protected void executeJob(JobExecutionContext context) throws JobExecutionException,
			InterruptedException
	{
		run = createJobRun();
		try
		{
			FileUpload file = (FileUpload) context.getMergedJobDataMap().get("bestand");
			if (file == null)
			{
				failJob("Upload van het bestand is mislukt, geen data ontvangen");
				run.commit();
				return;
			}
			filename = file.getClientFileName();

			if (!valideerFilename())
				return;
			if (file.getSize() == 0)
			{
				failJob("Bestand " + filename + " is leeg, geen data ontvangen");
				run.commit();
				return;
			}
			setStatus("Leest bestand " + filename);

			List<String> lines = leesBestand(file);

			if (!valideerBestand(lines))
				return;

			BronCfiTerugmelding terugmelding = createTerugmelding(lines.get(0));
			importeerBestand(lines, terugmelding);

			if (!valideerControlegetallen(terugmelding))
			{
				failJob("De controlegetallen aan het einde van de file kloppen niet");
				return;
			}

			run.setRunEinde(TimeUtil.getInstance().currentDateTime());
			run.setSamenvatting("CFI-terugmelding inlezen voltooid");
			run.update();
			run.commit();

			flushAndClearHibernate();

			terugmelding.setStatus(BronCFIStatus.Verwerkt);
			terugmelding.update();
			terugmelding.commit();
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

	private BronCfiTerugmelding createTerugmelding(String eersteRegel)
	{
		BronCfiTerugmelding terugmelding = new BronCfiTerugmelding();
		terugmelding.setBestandsnaam(filename);
		terugmelding.setIngelezenDoor(getMedewerker());
		terugmelding.setStatus(BronCFIStatus.InBehandeling);
		terugmelding.setInleesdatum(getDatumTijdOpgestart());
		String[] filenameVelden = filename.split("_", -1);
		terugmelding.setSector(filenameVelden[2]);
		terugmelding.setPeildatum(TimeUtil.getInstance().parseDateString(
			filenameVelden[3].substring(0, 8), "ddmmyyyy"));
		String[] eersteVelden = eersteRegel.split(";", -1);
		terugmelding.setControleTotaal(Integer.valueOf(eersteVelden[7]));
		terugmelding.save();
		return terugmelding;
	}

	private boolean valideerFilename()
	{
		String[] filenameVelden = filename.split("_", -1);
		if (filenameVelden.length != 4)
		{
			failJob("De bestandsnaam voldoet niet aan de specificaties");
			return false;
		}
		if (!filenameVelden[0].equals("TGM"))
		{
			failJob("De bestandsnaam begint niet met TGM");
			return false;
		}
		String brincode = EduArteContext.get().getInstelling().getBrincode().getCode();
		if (!filenameVelden[1].equals(brincode))
		{
			failJob("De brincode in de bestandsnaam(" + filenameVelden[1]
				+ ") is niet gelijk aan die van de instelling(" + brincode + ")");
			return false;
		}
		if (!filenameVelden[1].equals(brincode))
		{
			failJob("De brincode in de bestandsnaam(" + filenameVelden[1]
				+ ") is niet gelijk aan die van de instelling(" + brincode + ")");
			return false;
		}
		Date peildatum =
			TimeUtil.getInstance().parseDateString(filenameVelden[3].substring(0, 8), "ddmmyyyy");
		if (peildatum == null)
		{
			failJob("Kan de peildatum niet uit de bestandsnaam halen");
			return false;
		}
		return true;

	}

	private boolean valideerControlegetallen(BronCfiTerugmelding terugmelding)
	{
		int aantalBEK = 0;
		int aantalEXP = 0;
		int aantalSAG = 0;
		int aantalSIN = 0;
		int aantalSBH = 0;
		int aantalSBL = 0;
		for (BronCfiTerugmeldingRegel regel : terugmelding.getRegels())
		{
			switch (regel.getRegelType())
			{
				case BEK:
					aantalBEK++;
					break;
				case EXP:
					aantalEXP++;
					break;
				case SAG:
					aantalSAG++;
					break;
				case SIN:
					aantalSIN++;
					break;
				case SBH:
					aantalSBH++;
					break;
				case SBL:
					aantalSBL++;
					break;
				default:
					break;
			}
		}
		if (aantalBEK == terugmelding.getAantalBEK() && aantalSAG == terugmelding.getAantalSAG()
			&& aantalSIN == terugmelding.getAantalSIN() && aantalSBH == terugmelding.getAantalSBH()
			&& aantalSBL == terugmelding.getAantalSBL())
			return true;
		return false;
	}

	private BronCfiTerugmInlezenJobRun createJobRun()
	{
		BronCfiTerugmInlezenJobRun jobrun = new BronCfiTerugmInlezenJobRun();
		jobrun.setGestartDoor(getMedewerker());
		jobrun.setRunStart(TimeUtil.getInstance().currentDateTime());
		jobrun.setSamenvatting("CFI-terugmelding inlezen gestart");
		jobrun.save();
		return jobrun;
	}

	/**
	 * Voert een snelle syntax check en inhoudelijke controle uit of het bestand wel een
	 * cfi-terugmelding bestand is, en of de terugmelding wel voor de juiste instelling
	 * ingelezen wordt.
	 */
	private boolean valideerBestand(List<String> lines)
	{
		if (lines.isEmpty())
		{
			failJob("Bestand " + filename + " is leeg");
			return false;
		}

		String eersteRegel = lines.get(0);
		String[] eersteVelden = eersteRegel.split(";", -1);
		if (eersteVelden == null || eersteVelden.length < 8)
		{
			failJob("Bestand " + filename + " is geen geldig CFI-terugmelding");
			return false;
		}
		if (BronCfiRegelType.BRI != BronCfiRegelType.parse(eersteVelden[0]))
		{
			failJob("Bestand " + filename
				+ " is geen geldig terugmelding, eerste record is geen BRI");
			return false;
		}

		String instellingBrin = EduArteContext.get().getInstelling().getBrincode().getCode();
		if (!instellingBrin.equals(eersteVelden[2]))
		{
			failJob(String.format(
				"BRIN %s in de terugmelding komt niet overeen met de BRIN van de instelling (%s)",
				eersteVelden[2], instellingBrin));
			return false;
		}

		boolean cfiTerugmeldingIsGeldig = true;
		for (int i = 0; i < lines.size(); i++)
		{
			String line = lines.get(i);
			String[] velden = line.split(";", -1);
			if (velden == null)
			{
				run.error(String.format("Regel %d: bevat geen velden", i + 1));
				cfiTerugmeldingIsGeldig = false;
				continue;
			}
			else if (velden.length < 6)
			{
				run.error(String.format("Regel %d: bevat niet minstens 6 velden", i + 1));
				cfiTerugmeldingIsGeldig = false;
				continue;
			}
			else if (BronCfiRegelType.parse(velden[0]) == null)
			{
				run.error(String.format("Regel %d: %s is een onbekend record type", i + 1,
					velden[0]));
				cfiTerugmeldingIsGeldig = false;
				continue;
			}
		}

		if (!cfiTerugmeldingIsGeldig)
		{
			failJob("Bestand " + filename
				+ " bevat ongeldige records. Klik hier voor meer informatie");
		}
		return cfiTerugmeldingIsGeldig;
	}

	private BronCfiTerugmelding importeerBestand(List<String> lines,
			BronCfiTerugmelding terugmelding) throws InterruptedException
	{
		setStatus("Importeert bestand " + filename);
		int counter = 0;
		for (String line : lines)
		{
			BronCfiTerugmeldingRegel regel = parseLine(line, terugmelding);
			if (regel != null)
			{
				terugmelding.getRegels().add(regel);
				regel.save();
			}
			counter++;
			setProgress(counter, lines.size() * 2);
			if (counter % 100 == 0)
			{
				flushAndClearHibernate();
			}
		}
		flushAndClearHibernate();
		return terugmelding;
	}

	private BronCfiTerugmeldingRegel parseLine(String line, BronCfiTerugmelding terugmelding)
	{
		BronCfiTerugmeldingRegel regel = null;
		String[] velden = line.split(";", -1);
		if (velden != null && velden.length >= 4)
		{
			BronCfiRegelType regeltype = BronCfiRegelType.valueOf(velden[0]);
			switch (regeltype)
			{
				case BEK:
					regel = new BronCfiTerugmeldingBEKRegel(velden);
					break;
				case EXP:
					regel = new BronCfiTerugmeldingEXPRegel(velden);
					break;
				case SAG:
					regel = new BronCfiTerugmeldingSAGRegel(velden);
					break;
				case SIN:
					regel = new BronCfiTerugmeldingSINRegel(velden);
					break;
				case SBH:
					regel = new BronCfiTerugmeldingSBHRegel(velden);
					break;
				case SBL:
					regel = new BronCfiTerugmeldingSBLRegel(velden);
					break;
				case SLR:
					setControleGetallen(terugmelding, velden);
					break;
				default:
					break;
			}
		}
		if (regel != null)
		{
			regel.setCfiTerugmelding(terugmelding);
		}
		return regel;
	}

	private void setControleGetallen(BronCfiTerugmelding terugmelding, String[] velden)
	{
		terugmelding.setAantalBEK(Integer.valueOf(velden[2]));
		terugmelding.setAantalEXP(Integer.valueOf(velden[3]));
		terugmelding.setAantalSAG(Integer.valueOf(velden[4]));
		terugmelding.setAantalSIN(Integer.valueOf(velden[5]));
		terugmelding.setAantalSBH(0);
		terugmelding.setAantalSBL(0);
		if (!velden[5].isEmpty())
			terugmelding.setAantalSBH(Integer.valueOf(velden[6]));
		if (!velden[6].isEmpty())
			terugmelding.setAantalSBL(Integer.valueOf(velden[7]));
		terugmelding.setAanmaakdatum(TimeUtil.getInstance().parseDateString(velden[9], "yyyymmdd"));
		terugmelding.saveOrUpdate();

	}

	private List<String> leesBestand(FileUpload file) throws IOException
	{
		List<String> res = new ArrayList<String>();
		InputStream stream = file.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "ISO-8859-1"));
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
		setStatus("Verwerken van CFI-terugmelding is mislukt");
		run.setSamenvatting("Inlezen CFI-terugmelding mislukt: " + message);
		run.error(message);
		run.setRunEinde(TimeUtil.getInstance().currentDateTime());
		run.saveOrUpdate();
		run.commit();
	}

	void failJob(Exception e)
	{
		String instelling = EduArteContext.get().getInstelling().getNaam();
		log.error("Inlezen CFI-terugmelding mislukt: " + instelling + "/" + filename, e);

		run
			.setSamenvatting("Inlezen van het CFI-terugmelding is mislukt, klik voor meer informatie");
		run.setRunEinde(TimeUtil.getInstance().currentDateTime());

		setStatus("Verwerken van CFI-terugmelding is mislukt");

		if (e instanceof BronException)
		{
			run.error(e.getMessage());
		}
		else
		{
			StringPrintWriter spw = new StringPrintWriter();
			e.printStackTrace(spw);
			run.error("Inlezen van het CFI-terugmelding is mislukt door een onverwachte fout:\n"
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

		BronCfiTerugmInlezenJobRun jobrun = createJobRun();

		jobrun.setRunEinde(TimeUtil.getInstance().currentDateTime());
		jobrun
			.setSamenvatting("Er is een fout opgetreden tijdens het inlezen van het CFI-terugmelding: "
				+ e.getLocalizedMessage());
		jobrun.save();
		jobrun.commit();
		throw new JobExecutionException(e);
	}
}
