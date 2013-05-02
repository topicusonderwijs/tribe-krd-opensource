package nl.topicus.eduarte.krd.bron.jobs;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.io.StringPrintWriter;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.quartz.JobSegment;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krd.entities.bron.BronTerugkoppelbestandInlezenJobRun;
import nl.topicus.onderwijs.duo.bron.BronException;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@JobInfo(name = BronTerugkoppelbestandInlezenJob.JOB_NAME)
@JobRunClass(BronTerugkoppelbestandInlezenJobRun.class)
public class BronTerugkoppelbestandInlezenJob extends EduArteJob
{
	public static final String JOB_NAME = "Terugkoppelbestand inlezen";

	public static final Object KEY_CONTENTS = "contents";

	public static final String KEY_FILENAME = "filename";

	private String filename;

	@Override
	protected void executeJob(JobExecutionContext context) throws InterruptedException,
			JobExecutionException
	{
		BronTerugkoppelbestandInlezenJobRun run = startJob(context);
		try
		{
			boolean succesvol = processJob(run);
			String result = "";
			if (succesvol)
			{
				result =
					String
						.format(
							"Terugkoppelbestand %s is ingelezen en verwerkt, klik voor meer informatie",
							getFilename());
			}
			else
			{
				result =
					String
						.format(
							"Er zijn fouten opgetreden tijdens het verwerken van bestand %s, klik voor meer informatie",
							getFilename());
			}
			run.setSamenvatting(result);
		}
		catch (InterruptedException e)
		{
			String instelling = EduArteContext.get().getInstelling().getNaam();
			log.info(String.format("Job interrupted: %s/%s", instelling, filename), e);
			throw e;
		}
		catch (Exception e)
		{
			failJob(run, e);
			throw new JobExecutionException(e);
		}
		run.setRunEinde(TimeUtil.getInstance().currentDateTime());
		run.saveOrUpdate();
		run.commit();
		return;
	}

	private BronTerugkoppelbestandInlezenJobRun startJob(JobExecutionContext context)
	{
		byte[] contents = extractData(context);
		setStatus("Inlezen bestand " + filename);

		BronTerugkoppelbestandInlezenJobRun run = new BronTerugkoppelbestandInlezenJobRun();
		run.setGestartDoor(getMedewerker());
		run.setRunStart(TimeUtil.getInstance().currentDateTime());
		run.setSamenvatting("Verwerken van BRON terugkoppelbestand " + filename + ".");
		run.setFilename(filename);
		run.setContents(contents);
		run.saveOrUpdate();

		return run;
	}

	private byte[] extractData(JobExecutionContext context)
	{
		filename = (String) context.getMergedJobDataMap().get(KEY_FILENAME);
		byte[] contents = (byte[]) context.getMergedJobDataMap().get(KEY_CONTENTS);
		return contents;
	}

	private boolean processJob(BronTerugkoppelbestandInlezenJobRun run)
			throws InterruptedException, BronException
	{
		setStatus("Verwerken van bestand " + filename);
		byte[] contents = run.getContents();

		Onderwijssoort onderwijssoort = getOnderwijssoort(contents);
		BronTerugkoppelbestandProcessor processor = null;
		switch (onderwijssoort)
		{
			case VO:
				processor = new BronVoTerugkoppelbestandProcessor(this, run);
				break;
			case MBO:
				processor = new BronBveTerugkoppelbestandProcessor(this, run);
				break;
			default:
				throw new IllegalStateException("Onbekende onderwijssoort: "
					+ onderwijssoort.name());
		}
		return processor.processBestand(contents);
	}

	enum Onderwijssoort
	{
		VO,
		MBO;
	}

	private Onderwijssoort getOnderwijssoort(byte[] contents)
	{
		String recordtype = new String(contents, 0, Math.min(3, contents.length));
		if ("200".equals(recordtype))
			return Onderwijssoort.VO;
		if ("400".equals(recordtype))
			return Onderwijssoort.MBO;
		throw new IllegalStateException("Onbekend recordtype gevonden: " + recordtype);
	}

	void failJob(BronTerugkoppelbestandInlezenJobRun run, Exception e)
	{
		DataAccessRegistry.getHelper(SessionDataAccessHelper.class).clearSession();
		DataAccessRegistry.getHelper(SessionDataAccessHelper.class).batchRollback();

		BronTerugkoppelbestandInlezenJobRun errorRun = new BronTerugkoppelbestandInlezenJobRun();
		errorRun.setGestartDoor(getMedewerker());
		errorRun.setRunStart(run.getRunStart());
		errorRun.setRunEinde(TimeUtil.getInstance().currentDateTime());
		errorRun.setSamenvatting("Inlezen van terugkoppelbestand " + filename
			+ " is mislukt, klik voor meer informatie");
		errorRun.setFilename(filename);

		if (e instanceof BronException)
		{
			errorRun.error(e.getMessage());
		}
		else
		{
			StringPrintWriter spw = new StringPrintWriter();
			e.printStackTrace(spw);
			errorRun.error("Inlezen van terugkoppelbestand " + filename
				+ " is mislukt door een onverwachte fout:\n" + spw.getString());
		}

		errorRun.saveOrUpdate();
		errorRun.commit();

		String instelling = EduArteContext.get().getInstelling().getNaam();
		log.error("Inlezen terugkoppelbestand mislukt: " + instelling + "/" + filename, e);
		setStatus("Verwerken van bestand " + filename + " is mislukt");

	}

	/**
	 * Methode publiceren in deze package.
	 */
	@Override
	protected void setStatus(String status)
	{
		log.info(status);
		super.setStatus(status);
	}

	/**
	 * Methode publiceren in deze package.
	 */
	@Override
	protected void setProgress(int count, int total) throws InterruptedException
	{
		super.setProgress(count, total);
	}

	/**
	 * Methode publiceren in deze package.
	 */
	@Override
	protected void setProgress(int count, int total, JobSegment segment)
			throws InterruptedException
	{
		super.setProgress(count, total, segment);
	}

	/**
	 * Methode publiceren in deze package.
	 */
	@Override
	protected void setProgress(int progress) throws InterruptedException
	{
		super.setProgress(progress);
	}

	public String getFilename()
	{
		return filename;
	}
}
