package nl.topicus.cobra.web.components.link;

import nl.topicus.cobra.web.components.form.IFormErrorProcessingSubmittingComponent;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.model.IModel;

public class ErrorProcessingSubmitLink extends SubmitLink implements
		IFormErrorProcessingSubmittingComponent
{
	private static final long serialVersionUID = 1L;

	public ErrorProcessingSubmitLink(String id)
	{
		super(id);
	}

	public ErrorProcessingSubmitLink(String id, Form< ? > form)
	{
		super(id, form);
	}

	public ErrorProcessingSubmitLink(String id, IModel< ? > model)
	{
		super(id, model);
	}

	public ErrorProcessingSubmitLink(String id, IModel< ? > model, Form< ? > form)
	{
		super(id, model, form);
	}

	@Override
	public void onError()
	{
	}
}
