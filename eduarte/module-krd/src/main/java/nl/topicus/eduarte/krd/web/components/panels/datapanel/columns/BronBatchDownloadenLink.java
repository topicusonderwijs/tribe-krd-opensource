package nl.topicus.eduarte.krd.web.components.panels.datapanel.columns;

import nl.topicus.onderwijs.duo.bron.IBronBatch;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.resource.ByteArrayResource;

/**
 * Een download link voor bijlages. Kan overweg met {@link IBronBatch}
 * 
 * @author vandekamp
 */
public class BronBatchDownloadenLink extends Link<Void>
{
	private static final long serialVersionUID = 1L;

	private final IModel<IBronBatch> bronBatchModel;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            wicket-id
	 * @param bronBatchModel
	 *            Model met daarin een bijlage
	 */
	public BronBatchDownloadenLink(String id, IModel<IBronBatch> bronBatchModel)
	{
		super(id);
		this.bronBatchModel = bronBatchModel;
		IBronBatch batch = bronBatchModel.getObject();
		add(new Label("label", batch.getBestandsnaam()));

	}

	@Override
	public void onClick()
	{
		IBronBatch batch = bronBatchModel.getObject();
		if (batch != null && batch.getBestand() != null)
		{
			ByteArrayResource resource =
				new ByteArrayResource("application/octet-stream", batch.getBestand(), batch
					.getBestandsnaam());

			ResourceStreamRequestTarget target =
				new ResourceStreamRequestTarget(resource.getResourceStream());
			target.setFileName(batch.getBestandsnaam());
			getRequestCycle().setRequestTarget(target);

			getRequestCycle().setRedirect(false);
		}
		else
		{
			error("De bijlage bevat geen bestand. U kunt het niet opvragen.");
		}
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		bronBatchModel.detach();
	}

}
