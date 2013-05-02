package nl.topicus.cobra.web.components.form;

import org.apache.wicket.markup.html.form.IFormSubmittingComponent;

public interface IFormErrorProcessingSubmittingComponent extends IFormSubmittingComponent
{
	public void onError();
}
