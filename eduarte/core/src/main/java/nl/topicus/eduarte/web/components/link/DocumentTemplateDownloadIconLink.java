/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.link;

import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.resource.ByteArrayResource;

/**
 * @author hop
 */
public class DocumentTemplateDownloadIconLink extends Link<DocumentTemplate>
{
	private static final long serialVersionUID = 1L;

	public DocumentTemplateDownloadIconLink(String id, IModel<DocumentTemplate> templateModel)
	{
		super(id, templateModel);

		// alleen wanneer de template ook echt een document gelinked heeft.
		DocumentTemplate template = templateModel.getObject();
		if (template != null && template.getZzzBestand() != null
			&& template.getZzzBestand().length > 0)
		{
			Image image = new Image("image");

			if (template.getType() == DocumentTemplateType.DOCX)
				image.add(new SimpleAttributeModifier("src", getRequest()
					.getRelativePathPrefixToContextRoot()
					+ "assets/img/icons/word2.png"));
			else if (template.getType() == DocumentTemplateType.JRXML)
				image.add(new SimpleAttributeModifier("src", getRequest()
					.getRelativePathPrefixToContextRoot()
					+ "assets/img/icons/printPDF.png"));
			else if (template.getType() == DocumentTemplateType.PDF)
				image.add(new SimpleAttributeModifier("src", getRequest()
					.getRelativePathPrefixToContextRoot()
					+ "assets/img/icons/printPDF.png"));
			else if (template.getType() == DocumentTemplateType.RTF)
				image.add(new SimpleAttributeModifier("src", getRequest()
					.getRelativePathPrefixToContextRoot()
					+ "assets/img/icons/word2.png"));
			else if (template.getType() == DocumentTemplateType.XLS)
				image.add(new SimpleAttributeModifier("src", getRequest()
					.getRelativePathPrefixToContextRoot()
					+ "assets/img/icons/excel.png"));
			else if (template.getType() == DocumentTemplateType.XLSX)
				image.add(new SimpleAttributeModifier("src", getRequest()
					.getRelativePathPrefixToContextRoot()
					+ "assets/img/icons/excel.png"));
		}
		else
		{
			add(new Image("image"));
			setEnabled(false);
		}
	}

	@Override
	public void onClick()
	{
		DocumentTemplate template = getModelObject();
		if (template != null && template.getZzzBestand() != null
			&& template.getZzzBestand().length > 0)
		{
			ByteArrayResource resource =
				new ByteArrayResource(null, template.getZzzBestand(), template.getBestandsnaam());

			WebResponse response = (WebResponse) getRequestCycle().getResponse();

			response.setAttachmentHeader(template.getBestandsnaam());
			response.setContentType(template.getType().getMimeType());
			response.setContentLength(template.getZzzBestand().length);

			getRequestCycle().setRequestTarget(
				new ResourceStreamRequestTarget(resource.getResourceStream()));

			getRequestCycle().setRedirect(false);
		}
	}
}
