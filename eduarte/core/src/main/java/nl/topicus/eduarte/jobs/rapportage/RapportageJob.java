package nl.topicus.eduarte.jobs.rapportage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.quartz.JobSegment;
import nl.topicus.cobra.quartz.SingleSegment;
import nl.topicus.cobra.templates.TemplateManager;
import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.templates.exceptions.TemplateCreationException;
import nl.topicus.cobra.templates.exceptions.TemplateException;
import nl.topicus.cobra.templates.monitors.DocumentTemplateProgressMonitor;
import nl.topicus.cobra.templates.monitors.MonitorListener;
import nl.topicus.cobra.templates.resolvers.FieldResolver;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.beanpropertyresolvers.EduArteBackgroundBeanPropertyResolver;
import nl.topicus.eduarte.entities.bijlage.BijlageEntiteit;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.entities.jobs.logging.RapportageJobRun;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.rapportage.converter.RapportageConverter;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratieFactory;

import org.hibernate.FlushMode;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Taak om van een bepaalde template een document te genereren. Hierbij is het idee dat de
 * taak niet hoeft te weten van welk type de template is. De template kan ook worden
 * geconverteerd naar een ander type (wanneer de gebruiker hier om vraagt).
 * 
 * @author hoeve
 */
@JobInfo(name = RapportageJob.JOB_NAME)
@JobRunClass(RapportageJobRun.class)
public class RapportageJob extends EduArteJob implements MonitorListener
{
	public static final String JOB_NAME = "Rapportage";

	protected JobSegment currentJobSegment = SingleSegment.ENTIRE_JOB;

	protected DocumentTemplate documentTemplate;

	protected nl.topicus.cobra.templates.documents.DocumentTemplate currentDocument;

	protected int totalProgress = 1;

	protected int internalProgress = 0;

	private RapportageJobRun run;

	private RapportageJobDataMap< ? extends IBijlageKoppelEntiteit< ? extends BijlageEntiteit>, IdObject> jobDataMap;

	protected HashMap<String, Object> documentDataMap;

	protected FieldResolver resolver;

	@SuppressWarnings("unchecked")
	@Override
	protected void executeJob(JobExecutionContext context)
	{
		try
		{
			extractData(context);
			prepareJob();

			if (documentTemplate == null || jobDataMap == null
				|| getSelection(IBijlageKoppelEntiteit.class) == null)
				throw new TemplateCreationException("Er is niet voldoende informatie opgegeven");

			setStatus("Taak uitvoeren");

			generateDocuments(getContext());
			setProgress(totalProgress);
			saveDocuments();
		}
		catch (Exception e)
		{
			rollbackJob(e);
			log.error(e.getMessage(), e);
		}

		setStatus("Taak afgerond");
		finishJob();
	}

	/**
	 * Voert een batchRollback uit en voegt een detail item toe aan de JobRun om meer
	 * informatie te bieden aan de gebruiker.
	 * 
	 * @param e
	 */
	protected void rollbackJob(Exception e)
	{
		JobRunDetail failDetail = new JobRunDetail(run);
		failDetail.setUitkomst("Er is een fout op getreden: " + e.getMessage());
		failDetail.setJobRun(run);
		run.getDetails().add(failDetail);

		run.rollback();
	}

	/**
	 * Stelt het resultaat in en commit alles.
	 */
	private void finishJob()
	{
		if (currentDocument != null && currentDocument.getOutputStream() != null)
			run.setResultaat(((ByteArrayOutputStream) currentDocument.getOutputStream())
				.toByteArray());

		run.setBestandsNaam(getEindFilename());
		run.setDocumentType(jobDataMap.getEindType());
		run.setRunEinde(TimeUtil.getInstance().currentDateTime());

		run.saveOrUpdate();
		run.commit();

		DataAccessRegistry.getHelper(SessionDataAccessHelper.class).getHibernateSessionProvider()
			.getSession().setFlushMode(FlushMode.AUTO);
	}

	/**
	 * Wrapper functie welke het voorbereiden en afhandelen voor en na het genereren
	 * aanroept. Vooral bedoelt voor subclasses.
	 * 
	 * @throws TemplateException
	 */
	protected <T extends IBijlageKoppelEntiteit< ? extends BijlageEntiteit>> void generateDocuments(
			Class<T> contextClass) throws TemplateException
	{
		initTemplateObjects();
		generateDocument(contextClass);
		RapportageConverter converter = new RapportageConverter();
		converter.convertDocument(currentDocument, jobDataMap.getStartType(), jobDataMap
			.getEindType(), documentTemplate.getBestandsnaam(), false, getEduarteApp());
		// convertDocument(currentDocument, documentTemplate.getBestandsnaam(), false);
	}

	/**
	 * Wrapper functie welke het opslaan na het genereren aanroept. Vooral bedoelt voor
	 * subclasses.
	 * 
	 * @throws TemplateException
	 * @throws IOException
	 */
	protected void saveDocuments() throws TemplateException, IOException
	{
	}

	/**
	 * Voert het eigenlijke genereren uit. Dit merged de datamap met de template.
	 * 
	 * @throws TemplateException
	 *             wanneer er een fout in de template of datamap zit.
	 * @throws IOException
	 *             wanneer de outputstream niet goed werkt.
	 */
	private <T extends IBijlageKoppelEntiteit< ? extends BijlageEntiteit>> void generateDocument(
			Class<T> contextClass) throws TemplateException
	{
		List<T> selection = getSelection(contextClass);
		totalProgress = selection.size();

		RapportageConfiguratieFactory<T> configuratie =
			getJobDataMap(contextClass).getConfiguration();

		if (!currentDocument.hasSections())
			documentDataMap.put(documentTemplate.getContext().getModelName(), selection);

		currentDocument.mergeDocumentHeader(resolver);
		currentDocument.mergeDocumentFooter(resolver);
		currentDocument.writeDocumentHeader();

		if (currentDocument.hasSections())
		{
			if (documentTemplate.isSectiePerElement())
			{
				for (T contextObject : selection)
				{
					documentDataMap
						.put(documentTemplate.getContext().getModelName(), contextObject);
					if (configuratie != null)
					{
						documentDataMap.put(documentTemplate.getConfiguratieRegistratie().naam(),
							configuratie.createConfiguratie(contextObject));
						documentDataMap.put("instellingen", configuratie);
					}
					currentDocument.writeSection(resolver);

					if (isInterrupted())
						break;
				}
			}
			else
			{
				documentDataMap.put(documentTemplate.getContext().getModelName(), selection);
				if (configuratie != null)
				{
					List<Object> configuraties = new ArrayList<Object>();
					for (T contextObject : selection)
						configuraties.add(configuratie.createConfiguratie(contextObject));
					documentDataMap.put(documentTemplate.getConfiguratieRegistratie().naam(),
						configuraties);
					documentDataMap.put("instellingen", configuratie);
				}
				currentDocument.writeSection(resolver);
			}
		}

		currentDocument.writeDocumentFooter();

		try
		{
			currentDocument.getOutputStream().flush();
		}
		catch (IOException e)
		{
			throw new TemplateCreationException("Fout tijdens het genereren van het document.",
				new JobExecutionException("Kon de data niet opslaan.", e));
		}
	}

	@SuppressWarnings( {"unchecked", "unused"})
	protected <T extends IBijlageKoppelEntiteit< ? extends BijlageEntiteit>> RapportageJobDataMap<T, ? > getJobDataMap(
			Class<T> contextClass)
	{
		return (RapportageJobDataMap<T, ? >) jobDataMap;
	}

	protected <T extends IBijlageKoppelEntiteit< ? extends BijlageEntiteit>> List<T> getSelection(
			Class<T> contextClass)
	{
		return getJobDataMap(contextClass).getSelection();
	}

	protected Class< ? extends IBijlageKoppelEntiteit< ? extends BijlageEntiteit>> getContext()
	{
		return documentTemplate.getContext().getContextClass();
	}

	protected DocumentTemplateType getStartType()
	{
		return getJobDataMap(getContext()).getStartType();
	}

	protected DocumentTemplateType getEindType()
	{
		return getJobDataMap(getContext()).getEindType();
	}

	/**
	 * @return de filename zoals deze zal zijn nadat hij door de converter is gekomen.
	 */
	protected String getEindFilename()
	{
		return getEindFilename(documentTemplate.getBestandsnaam());
	}

	/**
	 * @return de filename zoals deze zal zijn nadat hij door de converter is gekomen.
	 */
	protected String getEindFilename(String filename)
	{
		return getFilenameZonderExtensie(filename) + "."
			+ jobDataMap.getEindType().getFileExtension();
	}

	protected String getFilenameExtensie()
	{
		return getFilenameExtensie(documentTemplate.getBestandsnaam());
	}

	protected String getFilenameExtensie(String filename)
	{
		return filename.substring(filename.lastIndexOf('.') + 1);
	}

	protected String getFilenameZonderExtensie()
	{
		return getFilenameZonderExtensie(documentTemplate.getBestandsnaam());
	}

	protected String getFilenameZonderExtensie(String filename)
	{
		return filename.substring(0, filename.lastIndexOf('.'));
	}

	/**
	 * @return de filename met als extension .zip
	 */
	protected String getZipFilename()
	{
		return getFilenameZonderExtensie(documentTemplate.getBestandsnaam()) + ".zip";
	}

	/**
	 * Stelt een aantal objecten in zodat de generatie van 1 document mogelijk is.
	 * 
	 * @throws TemplateException
	 */
	protected void initTemplateObjects() throws TemplateException
	{
		ByteArrayOutputStream outputStream =
			new ByteArrayOutputStream(documentTemplate.getZzzBestand().length);

		currentDocument =
			TemplateManager.getInstance().createDocumentTemplateByMime(
				documentTemplate.getType().getMimeType(),
				new ByteArrayInputStream(documentTemplate.getZzzBestand()));
		currentDocument.setOutputStream(outputStream);
		currentDocument.setKeepMergeFields(false);

		DocumentTemplateProgressMonitor monitor = new DocumentTemplateProgressMonitor();
		monitor.registerMonitorListener(this);
		currentDocument.setProgressMonitor(monitor);

		if (documentDataMap == null)
			documentDataMap = new HashMap<String, Object>();
		if (documentTemplate.getExtraParameters() != null)
			for (Map.Entry<String, Object> entry : documentTemplate.getExtraParameters().entrySet())
				documentDataMap.put(entry.getKey(), entry.getValue());
		if (resolver == null)
			resolver = new EduArteBackgroundBeanPropertyResolver(documentDataMap, this);

		documentDataMap.put(
			nl.topicus.cobra.templates.documents.DocumentTemplate.CONTEXT_OBJECT_REF_NAME,
			documentTemplate.getContext().getModelName());
	}

	/**
	 * Bereid de job voor op het genereren, maakt bv de JobRun en stelt de FlushMode in op
	 * MANUAL.
	 */
	protected void prepareJob()
	{
		setStatus("Taak voorbereiden");
		run = new RapportageJobRun();
		run.setRunStart(TimeUtil.getInstance().currentDateTime());
		run.setGestartDoor(getMedewerker());
		run.setGestartDoorAccount(getAccount());

		String samenvatting = "Genereren onbekende rapportage";
		if (documentTemplate != null)
			samenvatting = documentTemplate.getOmschrijving();
		run.setSamenvatting(samenvatting);

		if (jobDataMap.getSelection() == null)
		{
			JobRunDetail failDetail = new JobRunDetail(run);
			failDetail.setUitkomst("Er is geen zoekfilter gevonden.");
			failDetail.setJobRun(run);
			run.getDetails().add(failDetail);
		}
		if (documentTemplate == null)
		{
			JobRunDetail failDetail = new JobRunDetail(run);
			failDetail.setUitkomst("Er is geen samenvoegdocument gevonden.");
			failDetail.setJobRun(run);
			run.getDetails().add(failDetail);
		}

		DataAccessRegistry.getHelper(SessionDataAccessHelper.class).getHibernateSessionProvider()
			.getSession().setFlushMode(FlushMode.COMMIT);
	}

	/**
	 * Haalt de benodigde data uit de datamap van de job.
	 * 
	 * @param context
	 * @throws TemplateCreationException
	 */
	@SuppressWarnings("unchecked")
	private void extractData(JobExecutionContext context) throws TemplateCreationException
	{
		setStatus("Data vergaren");

		jobDataMap = new RapportageJobDataMap(context.getMergedJobDataMap());

		if (jobDataMap == null || jobDataMap.getDocumentTemplate() == null
			|| jobDataMap.getSelection() == null)
			throw new TemplateCreationException(
				"Niet genoeg informatie om het document te genereren.");

		documentTemplate = jobDataMap.getDocumentTemplate();
	}

	/**
	 * Voert de progress op.
	 * 
	 * @see nl.topicus.cobra.quartz.CobraJob#setProgress(int)
	 */
	protected void increaseProgress()
	{
		internalProgress++;

		try
		{
			super.setProgress(internalProgress, totalProgress);
		}
		catch (InterruptedException e)
		{
			// ignore this exception, because we cannot throw it due to sectionWritten
			// (part of an interface). The job will check if it is interrupted or not
		}
	}

	/**
	 * @see nl.topicus.cobra.templates.monitors.MonitorListener#sectionWritten()
	 */
	@Override
	public void sectionWritten()
	{
		increaseProgress();
	}
}
