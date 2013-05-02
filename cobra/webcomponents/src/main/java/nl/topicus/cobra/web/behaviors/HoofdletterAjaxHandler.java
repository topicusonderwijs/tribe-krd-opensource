/*
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.behaviors;

import nl.topicus.cobra.transformers.HoofdletterMode;
import nl.topicus.cobra.transformers.HoofdletterTransformer;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.TextField;

/**
 * Ajaxhandler voor tekstvelden om een deel van de waarde om te zetten in hoofdletters.
 * 
 * @author loite
 */
public class HoofdletterAjaxHandler extends AjaxFormComponentUpdatingBehavior
{
	private static final long serialVersionUID = 1L;

	private final HoofdletterTransformer transformatie;

	public HoofdletterAjaxHandler(HoofdletterMode mode)
	{
		super("onblur");
		this.transformatie = new HoofdletterTransformer(mode);
	}

	@Override
	protected void onUpdate(AjaxRequestTarget target)
	{
		String value = processString();
		getComponent().setDefaultModelObject(value);
		target.addComponent(getComponent());
	}

	protected String processString()
	{
		String value = ((TextField< ? >) getComponent()).getInput();
		return transformatie.transform(value);
	}
}
