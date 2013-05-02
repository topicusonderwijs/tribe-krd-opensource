package nl.topicus.cobra.web.components.choice;

import nl.topicus.cobra.util.StringUtil;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.util.string.AppendingStringBuffer;

/**
 * Gebaseerd op {@link AjaxFormComponentUpdatingBehavior}. Het verschil zit in de onEvent
 * (die daar final is en dus niet overschreven kon worden) waar een lege waarde op een
 * required veld dat nullValid is, niet gezien wordt als validatie fout.
 * 
 * @author papegaaij
 */
public abstract class AjaxDropDownUpdatingBehaviour extends AjaxEventBehavior
{
	private static final long serialVersionUID = 1L;

	public AjaxDropDownUpdatingBehaviour()
	{
		super("onchange");
	}

	@Override
	protected final CharSequence getEventHandler()
	{
		return generateCallbackScript(new AppendingStringBuffer("wicketAjaxPost('").append(
			getCallbackUrl(false)).append(
			"', wicketSerialize(Wicket.$('" + getComponent().getMarkupId() + "'))"));
	}

	@Override
	protected void onEvent(final AjaxRequestTarget target)
	{
		final AbstractAjaxDropDownChoice< ? > dropdown =
			(AbstractAjaxDropDownChoice< ? >) getComponent();

		try
		{
			dropdown.inputChanged();
			dropdown.validate();
			if ((dropdown.isRequired() && dropdown.isNullValid() && StringUtil.isEmpty(dropdown
				.getInput()))
				|| !dropdown.hasErrorMessage())
			{
				dropdown.valid();
				dropdown.updateModel();

				onUpdate(target);
			}
			else
			{
				dropdown.invalid();

				onError(target, null);
			}
		}
		catch (RuntimeException e)
		{
			onError(target, e);

		}
	}

	/**
	 * Listener invoked on the ajax request. This listener is invoked after the
	 * component's model has been updated.
	 * 
	 * @param target
	 */
	protected abstract void onUpdate(AjaxRequestTarget target);

	/**
	 * Called to handle any error resulting from updating form component. Errors thrown
	 * from {@link #onUpdate(AjaxRequestTarget)} will not be caught here.
	 * 
	 * The RuntimeException will be null if it was just a validation or conversion error
	 * of the FormComponent
	 * 
	 * @param target
	 * @param e
	 */
	protected void onError(AjaxRequestTarget target, RuntimeException e)
	{
		if (e != null)
		{
			throw e;
		}
	}
}
