package nl.topicus.cobra.web.components.wiquery.auto;

import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.web.behaviors.ClientSideCallable;
import nl.topicus.cobra.web.behaviors.ServerCallAjaxBehaviour;
import nl.topicus.cobra.web.pages.FeedbackComponent;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AbstractBehavior;

public abstract class AjaxQuickSearchFieldUpdatingBehavior extends AbstractBehavior
{
	private static final long serialVersionUID = 1L;

	private QuickSearchField< ? > component;

	public AjaxQuickSearchFieldUpdatingBehavior()
	{
	}

	@Override
	public void bind(Component c)
	{
		if (!(c instanceof QuickSearchField< ? >))
		{
			throw new IllegalArgumentException("Can only bind to QuickSearchField");
		}
		super.bind(c);

		component = (QuickSearchField< ? >) c;
		component.add(new ServerCallAjaxBehaviour(this));
		component.getOptions().put(
			"callback",
			"function(input, inputVal, hiddenVal){"
				+ "input.serverCall('onCallBack', inputVal, hiddenVal);}");
	}

	@ClientSideCallable
	public void onCallBack(AjaxRequestTarget target, String inputVal, String hiddenVal)
	{
		// #46450
		if (!component.isVisibleInHierarchy())
			return;

		try
		{
			ReflectionUtil.findProperty(component.getClass(), "rawInput").setValue(component,
				inputVal);
			try
			{
				component.setHiddenValueOverride(hiddenVal);
				component.validate();
			}
			finally
			{
				component.setHiddenValueOverride(null);
			}
			if ((component.getConvertedInput() == null && component.isRequired())
				|| !component.hasErrorMessage())
			{
				component.valid();
				component.updateModel();
				onUpdate(target);
			}
			else
			{
				component.invalid();
				target.addComponent(component);
				FeedbackComponent feedback = component.findParent(FeedbackComponent.class);
				if (feedback != null)
					feedback.refreshFeedback(target);
				onError(target, null);
			}
		}
		catch (RuntimeException e)
		{
			onError(target, e);

		}
	}

	@SuppressWarnings("unused")
	protected void onError(AjaxRequestTarget target, RuntimeException e)
	{
		if (e != null)
		{
			throw e;
		}
	}

	public abstract void onUpdate(AjaxRequestTarget target);
}
