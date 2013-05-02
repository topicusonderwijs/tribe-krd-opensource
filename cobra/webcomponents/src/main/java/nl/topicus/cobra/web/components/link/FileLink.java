/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.link;

import nl.topicus.cobra.util.ComponentUtil;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.resource.ByteArrayResource;

/**
 * Een download link voor bestanden.
 * 
 * @author loite
 * @author marrink
 * @author papegaaij
 */
public class FileLink extends Link<Void>
{
	private static final long serialVersionUID = 1L;

	private final IModel<byte[]> fileModel;

	private final IModel<String> filenameModel;

	public FileLink(String id, IModel<byte[]> fileModel, IModel<String> filenameModel)
	{
		super(id);
		this.fileModel = fileModel;
		this.filenameModel = filenameModel;
	}

	@Override
	public void onClick()
	{
		byte[] file = fileModel.getObject();
		String filename = filenameModel.getObject();
		ByteArrayResource resource = new ByteArrayResource(null, file, filename);
		((WebResponse) getRequestCycle().getResponse()).setHeader("Content-Disposition",
			"attachment; filename=\"" + filename + "\"");
		getRequestCycle().setRequestTarget(
			new ResourceStreamRequestTarget(resource.getResourceStream()));
		getRequestCycle().setRedirect(false);
	}

	@Override
	public void detachModels()
	{
		super.detachModels();

		ComponentUtil.detachQuietly(fileModel);
		ComponentUtil.detachQuietly(filenameModel);
	}
}
