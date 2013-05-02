/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.link;

import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.resource.ByteArrayResource;

/**
 * @author hop
 */
public class DocumentTemplateDownloadLink<T extends DocumentTemplate> extends Link<T>
{
	private static final long serialVersionUID = 1L;

	public DocumentTemplateDownloadLink(String id, IModel<T> templateModel)
	{
		super(id, templateModel);

		PropertyModel<String> bestandsNaamModel =
			new PropertyModel<String>(templateModel, "bestandsnaam");
		String bestandsnaam = "";
		if (bestandsNaamModel.getObject() != null)
			bestandsnaam = bestandsNaamModel.getObject().toString();
		add(ComponentFactory.getDataLabel("label", bestandsnaam));

		// alleen wanneer de template ook echt een document gelinked heeft.
		DocumentTemplate template = templateModel.getObject();
		if (template == null || template.getZzzBestand() == null
			|| template.getZzzBestand().length == 0)
			setEnabled(false);
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
