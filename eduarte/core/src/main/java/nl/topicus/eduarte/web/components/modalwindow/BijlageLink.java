package nl.topicus.eduarte.web.components.modalwindow;

import nl.topicus.cobra.web.components.link.SecureLink;
import nl.topicus.eduarte.entities.bijlage.IDownloadable;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.resource.ByteArrayResource;

/**
 * Een download link voor {@link IDownloadable}
 * 
 * @author loite
 */
public class BijlageLink<T extends IDownloadable> extends SecureLink<T>
{
	private static final long serialVersionUID = 1L;

	public BijlageLink(String id, IModel<T> bijlageModel)
	{
		super(id, bijlageModel);
		add(new Label("label", new PropertyModel<T>(bijlageModel, "bestandsnaam")));
	}

	@Override
	public void onClick()
	{
		T bijlage = getModelObject();
		if (bijlage != null && bijlage.getBestand() != null)
		{
			ByteArrayResource resource =
				new ByteArrayResource(null, bijlage.getBestand(), bijlage.getBestandsnaam());

			((WebResponse) getRequestCycle().getResponse()).setHeader("Content-Disposition",
				"attachment; filename=\"" + bijlage.getBestandsnaam() + "\"");
			getRequestCycle().setRequestTarget(
				new ResourceStreamRequestTarget(resource.getResourceStream()));
			getRequestCycle().setRedirect(false);
		}
		else
		{
			error("De bijlage bevat geen bestand. U kunt het niet opvragen.");
		}
	}
}
