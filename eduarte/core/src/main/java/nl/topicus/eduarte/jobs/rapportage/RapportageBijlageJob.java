package nl.topicus.eduarte.jobs.rapportage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.templates.TemplateManager;
import nl.topicus.cobra.templates.documents.DocumentTemplate;
import nl.topicus.cobra.templates.documents.zip.ZipFileDocument;
import nl.topicus.cobra.templates.exceptions.TemplateCreationException;
import nl.topicus.cobra.templates.exceptions.TemplateException;
import nl.topicus.cobra.templates.monitors.DocumentTemplateProgressMonitor;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.beanpropertyresolvers.EduArteBackgroundBeanPropertyResolver;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.BijlageEntiteit;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.bijlage.TypeBijlage;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.jobs.JobRunClass;
import nl.topicus.eduarte.entities.jobs.logging.RapportageJobRun;
import nl.topicus.eduarte.rapportage.converter.RapportageConverter;

import org.quartz.JobExecutionException;

/**
 * Subclass van RapportageJob welke meerdere documenten creert; voor elk context object 1.
 * en voor de gebruiker een totaal document. Deze worden in een zip file gestopt en evt
 * doorgestuurd naar een converter. De enkele documenten worden aan de context objecten
 * gekoppeld.
 * 
 * @author hoeve
 */
@JobInfo(name = RapportageBijlageJob.JOB_NAME)
@JobRunClass(RapportageJobRun.class)
public class RapportageBijlageJob extends RapportageJob
{
	@SuppressWarnings("hiding")
	public static final String JOB_NAME = "Rapportage ";

	private Map<String, DocumentTemplate> documents;

	private DocumentTemplate totaleDocument;

	/**
	 * Pakt de documenten uit de map en stopt ze in de zipfile.
	 * 
	 * @throws TemplateException
	 * @see nl.topicus.eduarte.jobs.rapportage.RapportageJob#saveDocuments()
	 */
	@Override
	protected void saveDocuments() throws IOException, TemplateException
	{
		if (!documents.isEmpty())
		{
			convertDocuments();
			linkDocuments();
		}

		RapportageConverter converter = new RapportageConverter();
		converter.convertDocument(totaleDocument, getStartType(), getEindType(), documentTemplate
			.getBestandsnaam(), false, getEduarteApp());
		// convertDocument(totaleDocument, documentTemplate.getBestandsnaam(), false);

		// @return stellen we currentDocument = totaleDocument zodat de base class de boel
		// correct kan afhandelen. nml: de gebruiker krijgt het totale document.
		currentDocument = totaleDocument;
	}

	private void convertDocuments() throws IOException, TemplateException
	{
		RapportageConverter converter = new RapportageConverter();
		// stel een byte array in voor het document, leg daar een zip stream over heen
		// zodat we een zipfile kunnen maken.
		ByteArrayOutputStream innerOutputStream = new ByteArrayOutputStream();
		ZipOutputStream outputStream = new ZipOutputStream(innerOutputStream);
		currentDocument = new ZipFileDocument();
		currentDocument.setOutputStream(innerOutputStream);

		for (Object key : documents.keySet())
		{
			nl.topicus.cobra.templates.documents.DocumentTemplate document = documents.get(key);
			ZipEntry zipEntry = new ZipEntry(key.toString() + "." + getFilenameExtensie());
			outputStream.putNextEntry(zipEntry);

			if (document.getOutputStream() instanceof ByteArrayOutputStream)
			{
				byte[] buffer = ((ByteArrayOutputStream) document.getOutputStream()).toByteArray();
				outputStream.write(buffer, 0, buffer.length);
				zipEntry.setSize(buffer.length);
			}

			outputStream.closeEntry();
			outputStream.flush();
		}

		outputStream.close();

		// in dit geval hebben we nu tijdelijk maar 1 document, een zipfile document.
		converter.convertDocument(currentDocument, getStartType(), getEindType(), getZipFilename(),
			true, getEduarteApp());
	}

	/**
	 * Voert het eigenlijke genereren uit. Dit merged de datamap met de template voor 1
	 * object en voegt deze toe aan de Map. Ook merged hij de datamap met het totale
	 * document.
	 * 
	 * @throws TemplateException
	 *             wanneer er een fout in de template of datamap zit.
	 */
	@Override
	protected <T extends IBijlageKoppelEntiteit< ? extends BijlageEntiteit>> void generateDocuments(
			Class<T> contextClass) throws TemplateException
	{
		initTemplateObjects();

		List<T> selection = getSelection(contextClass);
		totalProgress = selection.size();

		if (!currentDocument.hasSections())
			documentDataMap.put(documentTemplate.getContext().getModelName(), selection);

		totaleDocument.mergeDocumentHeader(resolver);
		totaleDocument.mergeDocumentFooter(resolver);
		totaleDocument.writeDocumentHeader();

		for (T contextObject : selection)
		{
			Serializable id =
				getJobDataMap(contextClass).getBijlageContextIDProvider().getBijlageContextID(
					contextObject);
			documentDataMap.put(documentTemplate.getContext().getModelName(), contextObject);
			totaleDocument.writeSection(resolver);

			if (id != null)
			{
				initTemplateObjects();

				currentDocument.mergeDocumentHeader(resolver);
				currentDocument.mergeDocumentFooter(resolver);
				currentDocument.writeDocumentHeader();

				currentDocument.writeSection(resolver);

				currentDocument.writeDocumentFooter();

				try
				{
					currentDocument.getOutputStream().flush();
					currentDocument.getOutputStream().close();
				}
				catch (IOException e)
				{
					throw new TemplateCreationException(
						"Fout tijdens het genereren van het document.", new JobExecutionException(
							"Kon de data niet opslaan.", e));
				}

				documents.put(id.toString(), currentDocument);
			}

			if (isInterrupted())
				break;
		}

		totaleDocument.writeDocumentFooter();

		try
		{
			totaleDocument.getOutputStream().flush();
			totaleDocument.getOutputStream().close();
		}
		catch (IOException e)
		{
			throw new TemplateCreationException("Fout tijdens het genereren van het document.",
				new JobExecutionException("Kon de data niet opslaan.", e));
		}
	}

	/**
	 * Maakt steeds een nieuw "currentDocument". Controleert ook of andere variabelen niet
	 * null zijn.
	 */
	@Override
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

		if (documentDataMap == null)
			documentDataMap = new HashMap<String, Object>();
		if (documentTemplate.getExtraParameters() != null)
			for (Map.Entry<String, Object> entry : documentTemplate.getExtraParameters().entrySet())
				documentDataMap.put(entry.getKey(), entry.getValue());
		if (resolver == null)
			resolver = new EduArteBackgroundBeanPropertyResolver(documentDataMap, this);
		if (documents == null)
			documents =
				new HashMap<String, nl.topicus.cobra.templates.documents.DocumentTemplate>();

		documentDataMap.put(
			nl.topicus.cobra.templates.documents.DocumentTemplate.CONTEXT_OBJECT_REF_NAME,
			documentTemplate.getContext().getModelName());

		if (totaleDocument == null)
		{
			ByteArrayOutputStream totaleOutputStream =
				new ByteArrayOutputStream(documentTemplate.getZzzBestand().length);

			totaleDocument =
				TemplateManager.getInstance().createDocumentTemplateByMime(
					documentTemplate.getType().getMimeType(),
					new ByteArrayInputStream(documentTemplate.getZzzBestand()));
			totaleDocument.setOutputStream(totaleOutputStream);
			totaleDocument.setKeepMergeFields(false);

			DocumentTemplateProgressMonitor monitor = new DocumentTemplateProgressMonitor();
			monitor.registerMonitorListener(this);
			totaleDocument.setProgressMonitor(monitor);
		}
	}

	/**
	 * Functie welke de documenten in de zip file koppelt aan hun bijbehorende context
	 * object.
	 * 
	 * @throws IOException
	 */
	private void linkDocuments() throws IOException
	{
		if (currentDocument.getOutputStream() instanceof ByteArrayOutputStream)
		{
			ByteArrayOutputStream outputStream =
				(ByteArrayOutputStream) currentDocument.getOutputStream();
			ZipInputStream inStream =
				new ZipInputStream(new ByteArrayInputStream(outputStream.toByteArray()));

			ZipEntry entry = inStream.getNextEntry();
			while (entry != null && inStream.available() > 0)
			{
				// vervang van alle documenten de outputstream.
				DocumentTemplate document =
					documents.get(getFilenameZonderExtensie(entry.getName()));
				ByteArrayOutputStream documentStream = new ByteArrayOutputStream();
				document.setOutputStream(documentStream);

				byte[] documentData = new byte[1024];
				int count;

				while ((count = inStream.read(documentData, 0, 1024)) > 0)
					documentStream.write(documentData, 0, count);

				documentStream.flush();

				// maak een bijlage object voor de context.
				createBijlage(new Long(getFilenameZonderExtensie(entry.getName())), documentStream
					.toByteArray(), getContext());

				entry = inStream.getNextEntry();
			}
		}
	}

	/**
	 * Maakt voor de het object met {@link id} als ID een bijlage document met
	 * {@link documentData} als data.
	 * 
	 * @param id
	 * @param documentData
	 */
	private <T extends IBijlageKoppelEntiteit< ? extends BijlageEntiteit>> void createBijlage(
			Long id, byte[] documentData, Class<T> contextClass)
	{
		@SuppressWarnings("unchecked")
		BatchDataAccessHelper<T> helper = DataAccessRegistry.getHelper(BatchDataAccessHelper.class);
		T contextObject = helper.get(contextClass, id);

		nl.topicus.eduarte.entities.rapportage.DocumentTemplate template = documentTemplate;
		Bijlage bijlage = new Bijlage();
		bijlage.setBestand(documentData);
		StringBuilder uniekebestandsnaam = new StringBuilder();
		String bestandsnaam = getEindFilename();
		int dot = bestandsnaam.lastIndexOf('.');
		if (dot >= 0)
			uniekebestandsnaam.append(bestandsnaam.substring(0, dot));
		else
			uniekebestandsnaam.append(bestandsnaam);
		if (contextObject instanceof Verbintenis)
		{
			uniekebestandsnaam.append('-');
			Verbintenis verbintenis = (Verbintenis) contextObject;
			uniekebestandsnaam.append(verbintenis.getOvereenkomstnummer());
			bijlage.setDocumentnummer(Long.toString(verbintenis.getOvereenkomstnummer()));
		}
		else if (contextObject instanceof BPVInschrijving)
		{
			uniekebestandsnaam.append('-');
			BPVInschrijving bpv = (BPVInschrijving) contextObject;
			uniekebestandsnaam.append(bpv.getOvereenkomstnummer());
			bijlage.setDocumentnummer(Long.toString(bpv.getOvereenkomstnummer()));
		}
		if (dot >= 0)
			uniekebestandsnaam.append(bestandsnaam.substring(dot));
		bijlage.setBestandsnaam(uniekebestandsnaam.toString());
		bijlage.setDocumentType(template == null ? null : template.getDocumentType());
		bijlage.setOmschrijving(documentTemplate.getOmschrijving());
		bijlage.setOntvangstdatum(TimeUtil.getInstance().currentDateTime());
		bijlage.setTypeBijlage(TypeBijlage.Bestand);
		bijlage.saveOrUpdate();

		BijlageEntiteit koppeling = contextObject.addBijlage(bijlage);
		koppeling.saveOrUpdate();
	}
}
