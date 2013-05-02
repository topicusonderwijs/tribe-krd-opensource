package nl.topicus.cobra.web.components.form;

import org.apache.wicket.markup.html.form.IFormSubmittingComponent;
import org.apache.wicket.model.IModel;

public class SavableForm<T> extends VersionedForm<T>
{
	private static final long serialVersionUID = 1L;

	private boolean updateModelsOnError = false;

	public SavableForm(String id, IModel<T> model)
	{
		super(id, model);
	}

	public boolean isUpdateModelsOnError()
	{
		return updateModelsOnError;
	}

	public void setUpdateModelsOnError(boolean updateModelsOnError)
	{
		this.updateModelsOnError = updateModelsOnError;
	}

	public void updateModelsOnComponents()
	{
		updateFormComponentModels();
	}

	@Override
	protected void onError()
	{
		super.onError();
		if (isUpdateModelsOnError())
			updateModelsOnComponents();
		IFormSubmittingComponent submitComponent = findSubmittingButton();
		if (submitComponent instanceof IFormErrorProcessingSubmittingComponent)
			((IFormErrorProcessingSubmittingComponent) submitComponent).onError();
	}
}
