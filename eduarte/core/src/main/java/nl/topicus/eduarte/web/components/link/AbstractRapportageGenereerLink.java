package nl.topicus.eduarte.web.components.link;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import nl.topicus.cobra.templates.TemplateManager;
import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.templates.exceptions.TemplateException;
import nl.topicus.cobra.templates.resolvers.FieldResolver;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.beanpropertyresolvers.EduArteBeanPropertyResolver;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.BijlageEntiteit;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.bijlage.TypeBijlage;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.rapportage.converter.RapportageConverter;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.resource.ByteArrayResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRapportageGenereerLink extends Link<DocumentTemplate>
{
	private static final long serialVersionUID = 1L;

	protected static Logger log = LoggerFactory.getLogger(AbstractRapportageGenereerLink.class);

	public AbstractRapportageGenereerLink(String id, IModel<DocumentTemplate> templateModel)
	{
		super(id, templateModel);
	}

	@Override
	public void onClick()
	{
		DocumentTemplate template = getModelObject();

		Map<String, Object> context = new HashMap<String, Object>();

		try
		{
			ByteArrayOutputStream outputStream =
				new ByteArrayOutputStream(template.getZzzBestand().length);
			nl.topicus.cobra.templates.documents.DocumentTemplate document;
			document =
				TemplateManager.getInstance().createDocumentTemplateByMime(
					template.getType().getMimeType(),
					new ByteArrayInputStream(template.getZzzBestand()));
			document.setOutputStream(outputStream);
			document.setKeepMergeFields(false);
			context = updateContext(context, document);

			if (template.getExtraParameters() != null)
				for (Map.Entry<String, Object> entry : template.getExtraParameters().entrySet())
					context.put(entry.getKey(), entry.getValue());

			FieldResolver resolver = new EduArteBeanPropertyResolver(context);
			document.mergeDocumentHeader(resolver);
			document.mergeDocumentFooter(resolver);

			writeSections(document, resolver);
			if (template.getForceerType() == DocumentTemplateType.PDF)
			{
				// Converteer naar PDF.
				RapportageConverter converter = new RapportageConverter();
				converter.convertDocument(document, template.getType(), template.getForceerType(),
					template.getBestandsnaam(), false, EduArteApp.get());
			}

			byte[] data = ((ByteArrayOutputStream) document.getOutputStream()).toByteArray();
			String mimeType =
				template.getForceerType() == null ? template.getType().getMimeType() : template
					.getForceerType().getMimeType();
			String fileExtension =
				template.getForceerType() == null ? template.getType().getFileExtension()
					: template.getForceerType().getFileExtension();
			String bestandsnaam = template.getBestandsnaamZonderExtensie() + "." + fileExtension;

			// Maak eventueel een bijlage bij het contextobject.
			if (template.isKopieBijContext())
			{
				String name =
					(String) context
						.get(nl.topicus.cobra.templates.documents.DocumentTemplate.CONTEXT_OBJECT_REF_NAME);
				createBijlage((IBijlageKoppelEntiteit< ? >) context.get(name), data, bestandsnaam);
			}

			ByteArrayResource resource = new ByteArrayResource(mimeType, data, bestandsnaam);
			WebResponse response = (WebResponse) getRequestCycle().getResponse();
			response.setAttachmentHeader(bestandsnaam);
			response.setContentType(mimeType);
			response.setContentLength(outputStream.size());
			getRequestCycle().setRequestTarget(
				new ResourceStreamRequestTarget(resource.getResourceStream()));
			getRequestCycle().setRedirect(false);
		}
		catch (TemplateException e)
		{
			log.error(e.toString(), e);
		}
	}

	private void createBijlage(IBijlageKoppelEntiteit< ? > contextObject, byte[] documentData,
			String bestandsnaam)
	{
		Bijlage bijlage = new Bijlage();
		bijlage.setBestand(documentData);
		StringBuilder uniekebestandsnaam = new StringBuilder();
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
		bijlage.setDocumentType(getModelObject().getDocumentType());
		bijlage.setOntvangstdatum(TimeUtil.getInstance().currentDateTime());
		bijlage.setOmschrijving(getModelObject().getOmschrijving());
		bijlage.setTypeBijlage(TypeBijlage.Bestand);
		bijlage.saveOrUpdate();

		BijlageEntiteit koppeling = contextObject.addBijlage(bijlage);
		koppeling.saveOrUpdate();
		koppeling.commit();
	}

	/**
	 * Functie om de context, welke de context, voor zowel de mergeHeader/mergeFooter als
	 * de secties, update. Vervangt bijvoorbeeld de gemapte deelnemer met de volgende in
	 * de lijst.
	 */
	protected abstract Map<String, Object> updateContext(Map<String, Object> context,
			nl.topicus.cobra.templates.documents.DocumentTemplate document);

	/**
	 * Functie welke het schrijven van de pagina's afhandelt. Deze functie kan
	 * bijvoorbeeld achtereenvolgens de header, de sectie en de footer weg schrijven.
	 * 
	 * @param document
	 * @param resolver
	 * @throws TemplateException
	 */
	protected abstract void writeSections(
			nl.topicus.cobra.templates.documents.DocumentTemplate document, FieldResolver resolver)
			throws TemplateException;

}