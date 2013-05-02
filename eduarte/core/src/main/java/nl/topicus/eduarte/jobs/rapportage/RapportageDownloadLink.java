package nl.topicus.eduarte.jobs.rapportage;

import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.entities.jobs.logging.RapportageJobRun;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.resource.ByteArrayResource;

public class RapportageDownloadLink extends Link<RapportageJobRun>
{
	private static final long serialVersionUID = 1L;

	public RapportageDownloadLink(String id, IModel<RapportageJobRun> model)
	{
		super(id, model);
	}

	@Override
	public void onClick()
	{
		RapportageJobRun _jobrun = getModelObject();

		ByteArrayResource resource =
			new ByteArrayResource(_jobrun.getDocumentType().getMimeType(), _jobrun.getResultaat(),
				_jobrun.getBestandsNaam());

		EduArteRequestCycle cycle = EduArteRequestCycle.get();
		WebResponse response = (WebResponse) cycle.getResponse();

		response.setAttachmentHeader(_jobrun.getBestandsNaam());
		response.setContentType(_jobrun.getDocumentType().getMimeType());
		response.setContentLength(_jobrun.getResultaatSize());
		response.setCharacterEncoding("UTF-8");

		cycle.setRequestTarget(new ResourceStreamRequestTarget(resource.getResourceStream()));
		cycle.setRedirect(false);
	}
}
