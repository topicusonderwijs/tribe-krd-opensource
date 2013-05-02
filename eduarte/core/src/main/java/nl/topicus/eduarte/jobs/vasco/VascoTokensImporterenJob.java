package nl.topicus.eduarte.jobs.vasco;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.vasco.VascoDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.vasco.VascoDataAccessHelper.VascoImportException;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.vasco.VascoTokensImporterenJobRun;
import nl.topicus.eduarte.jobs.EduArteJob;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Achtergrond taak voor het importeren van een vasco licentie bestand waarmee de Vasco
 * tokens in de database worden geladen.
 */
@JobInfo(name = VascoTokensImporterenJob.JOB_NAME)
@JobRunClass(VascoTokensImporterenJobRun.class)
public class VascoTokensImporterenJob extends EduArteJob
{
	public static final String JOB_NAME = "Vasco tokens inlezen";

	@Override
	protected void executeJob(JobExecutionContext context) throws JobExecutionException
	{
		VascoTokensImporterenJobRun run = new VascoTokensImporterenJobRun();
		run.setGestartDoor(getMedewerker());
		run.setRunStart(getDatumTijdOpgestart());
		run.setSamenvatting("Importeren vasco tokens gestart");
		run.saveOrUpdate();

		String license = (String) context.getMergedJobDataMap().get("dpxkey");
		FileUpload bestand = (FileUpload) context.getMergedJobDataMap().get("bestand");

		byte[] contents = bestand.getBytes();

		if (contents != null && contents.length > 0)
		{
			importTokens(license, contents);
			run.setSamenvatting("Bestand " + bestand.getClientFileName()
				+ " is succesvol geimporteerd");
		}
		else
		{
			run.setSamenvatting("Verwerken van het Vasco token bestand is mislukt. "
				+ "Het ingelezen bestand was niet goed geupload of leeg");
		}
		run.setRunEinde(TimeUtil.getInstance().currentDateTime());
		run.saveOrUpdate();
		run.commit();
	}

	private void importTokens(String license, byte[] contents) throws JobExecutionException
	{
		try
		{
			VascoDataAccessHelper vascoHelper =
				DataAccessRegistry.getHelper(VascoDataAccessHelper.class);
			vascoHelper.importFile(license, contents);
		}
		catch (VascoImportException e)
		{
			failJobAndRollback(e);
		}
	}

	void failJobAndRollback(Exception e) throws JobExecutionException
	{
		BatchDataAccessHelper< ? > batchHelper =
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class);
		batchHelper.batchRollback();

		SessionDataAccessHelper sessionHelper =
			DataAccessRegistry.getHelper(SessionDataAccessHelper.class);
		sessionHelper.clearSession();

		VascoTokensImporterenJobRun run = new VascoTokensImporterenJobRun();
		run.setGestartDoor(getMedewerker());
		run.setRunStart(getDatumTijdOpgestart());
		run.setRunEinde(TimeUtil.getInstance().currentDateTime());
		run
			.setSamenvatting("Er is een fout opgetreden tijdens het inlezen van het fotobestand. Klik voor meer informatie");
		run.error(e.getLocalizedMessage());
		run.save();
		run.commit();
		throw new JobExecutionException(e);
	}
}
