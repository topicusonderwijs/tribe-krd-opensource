/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.behaviors;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;

public class AjaxFormComponentSaveBehaviour extends AjaxFormComponentUpdatingBehavior
{
	private static final long serialVersionUID = 1L;

	public AjaxFormComponentSaveBehaviour()
	{
		super("onchange");
	}

	@Override
	protected void onUpdate(AjaxRequestTarget target)
	{
		// do nothing, the value is saved in the model
	}
}
