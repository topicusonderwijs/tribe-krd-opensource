/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.behaviors;

import nl.topicus.cobra.web.pages.FeedbackComponent;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;

/**
 * Behavior dat een form component via Ajax valideert. Ververst het feedbackpanel van de
 * pagina en het formcomponent.
 * <p>
 * NB DIT BEHAVIOR WERKT NIET VOOR CHOICE EN/OF GROUP COMPONENTEN.
 * <p>
 * NB DIT BEHAVIOR MAG ALLEEN AAN COMPONENTEN VERBONDEN WORDEN DIE AAN EEN FEEDBACKPAGE
 * GEKOPPELD ZIJN.
 */
public class AjaxFormComponentValidatingBehavior extends AjaxFormComponentUpdatingBehavior
{
	private static final long serialVersionUID = 1L;

	/**
	 * Bind het behavior aan het client side event. Voor text velden is 'onchange' het
	 * beste event.
	 */
	public AjaxFormComponentValidatingBehavior(String event)
	{
		super(event);
	}

	@Override
	protected void onBind()
	{
		super.onBind();
		if (!(getComponent().getPage() instanceof FeedbackComponent))
		{
			throw new WicketRuntimeException(
				"The page of this component doesn't implement FeedbackPage which is required for this validator to function");
		}
	}

	@Override
	protected void onUpdate(AjaxRequestTarget target)
	{
		// ververs de componenten ook als de invoer correct is, dan worden namelijk de
		// foutmeldingen in het feedback panel en de error-state van de input verwijderd.
		refreshInputAndFeedback(target);
	}

	private void refreshInputAndFeedback(AjaxRequestTarget target)
	{
		Component component = getComponent();
		FeedbackComponent feedbackPage = (FeedbackComponent) component.getPage();

		target.addComponent(component);
		feedbackPage.refreshFeedback(target);
	}

	@Override
	protected void onError(AjaxRequestTarget target, RuntimeException e)
	{
		refreshInputAndFeedback(target);
	}
}
