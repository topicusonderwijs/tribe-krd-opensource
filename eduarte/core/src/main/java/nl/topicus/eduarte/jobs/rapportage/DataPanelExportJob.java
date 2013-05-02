package nl.topicus.eduarte.jobs.rapportage;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.fileresources.CsvFileResourceStream;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.link.CustomDataPanelAfdrukkenLink;
import nl.topicus.cobra.web.components.link.CustomDataPanelExportLink;
import nl.topicus.cobra.web.components.link.IProgressCallback;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.jobs.logging.DataPanelExportJobRun;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.jobs.EduArteJob;

import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.hibernate.FlushMode;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@JobInfo(name = DataPanelExportJob.JOB_NAME)
@JobRunClass(DataPanelExportJobRun.class)
public class DataPanelExportJob extends EduArteJob implements IProgressCallback
{
	public static final String JOB_NAME = "Export";

	private Link< ? > link;

	private String omschrijving;

	private DataPanelExportJobRun run;

	private boolean pdf;

	@Override
	protected void executeJob(JobExecutionContext context) throws JobExecutionException,
			InterruptedException
	{
		try
		{
			link = (Link< ? >) context.getMergedJobDataMap().get("link");
			EduArteApp app =
				(EduArteApp) Application
					.get(context.getMergedJobDataMap().getString("application"));
			Application.set(app);
			Session session = (Session) context.getMergedJobDataMap().get("session");
			Session.set(session);
			omschrijving = context.getMergedJobDataMap().getString("omschrijving");
			pdf = (Boolean) context.getMergedJobDataMap().get("pdf");

			prepareJob();
			if (link == null)
				throw new JobExecutionException("Er is geen link opgegeven");
			setStatus("Taak uitvoeren");
			ByteArrayOutputStream stream = null;
			String bestandsnaam = "leeg";
			if (pdf && link instanceof CustomDataPanelAfdrukkenLink< ? >)
			{
				JasperPrint print = ((CustomDataPanelAfdrukkenLink< ? >) link).getJasperPrint(this);
				stream = new ByteArrayOutputStream();
				JasperExportManager.exportReportToPdfStream(print, stream);
				bestandsnaam = "lijst.pdf";
			}
			else if (!pdf && link instanceof CustomDataPanelExportLink< ? >)
			{
				CustomDataPanelExportLink< ? > exportLink = (CustomDataPanelExportLink< ? >) link;
				bestandsnaam = exportLink.getBestandsnaam();
				if (bestandsnaam == null)
					bestandsnaam = "export.csv";
				if (!bestandsnaam.endsWith(".csv"))
					bestandsnaam = bestandsnaam + ".csv";
				CsvFileResourceStream csvStream =
					exportLink.getRows().generateCsvFileFromView(exportLink.getBestandsnaam(),
						exportLink.getHeader().getObject(), true, this);
				byte[] res = new byte[(int) csvStream.length()];
				BufferedInputStream bis = new BufferedInputStream(csvStream.getInputStream());
				bis.read(res);
				bis.close();
				csvStream.close();
				stream = new ByteArrayOutputStream(res.length);
				stream.write(res);
			}

			setStatus("Taak afgerond");
			finishJob(stream, bestandsnaam);
		}
		catch (JRException e)
		{
			log.error(e.getLocalizedMessage(), e);
		}
		catch (ResourceStreamNotFoundException e)
		{
			log.error(e.getLocalizedMessage(), e);
		}
		catch (IOException e)
		{
			log.error(e.getLocalizedMessage(), e);
		}
		finally
		{
			Application.unset();
			Session.unset();
		}

	}

	/**
	 * Stelt het resultaat in en commit alles.
	 * 
	 */
	private void finishJob(ByteArrayOutputStream stream, String bestandsnaam)
	{
		run.setResultaat(stream == null ? null : stream.toByteArray());
		run.setBestandsNaam(bestandsnaam);
		if (pdf)
		{
			run.setDocumentType(DocumentTemplateType.PDF);
		}
		else
		{
			run.setDocumentType(DocumentTemplateType.CSV);
		}
		run.setRunEinde(TimeUtil.getInstance().currentDateTime());
		run.setRunEinde(TimeUtil.getInstance().currentDateTime());
		run.saveOrUpdate();
		run.commit();
		DataAccessRegistry.getHelper(SessionDataAccessHelper.class).getHibernateSessionProvider()
			.getSession().setFlushMode(FlushMode.AUTO);
	}

	/**
	 * Bereid de job voor op het genereren, maakt bv de JobRun en stelt de FlushMode in op
	 * MANUAL.
	 */
	protected void prepareJob()
	{
		setStatus("Taak voorbereiden");
		run = new DataPanelExportJobRun();
		run.setRunStart(TimeUtil.getInstance().currentDateTime());
		run.setGestartDoor(getMedewerker());
		run.setGestartDoorAccount(getAccount());

		String samenvatting = "Gegevens exporteren";
		if (omschrijving != null)
			samenvatting = omschrijving;
		run.setSamenvatting(samenvatting);

		if (link == null)
		{
			JobRunDetail failDetail = new JobRunDetail(run);
			failDetail.setUitkomst("Er is geen link gevonden.");
			failDetail.setJobRun(run);
			run.getDetails().add(failDetail);
		}
		DataAccessRegistry.getHelper(SessionDataAccessHelper.class).getHibernateSessionProvider()
			.getSession().setFlushMode(FlushMode.COMMIT);
	}

	@Override
	public void setProgress(int progress) throws InterruptedException
	{
		super.setProgress(progress);
	}

}
