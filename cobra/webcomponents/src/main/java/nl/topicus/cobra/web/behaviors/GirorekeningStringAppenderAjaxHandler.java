/*
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.behaviors;

import nl.topicus.cobra.util.StringUtil;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.TextField;

/**
 * Ajaxhandler voor tekstvelden welke een (giro)rekeningnummer moeten voorstellen.
 * 
 * @author hoeve
 */
public class GirorekeningStringAppenderAjaxHandler extends AjaxFormComponentUpdatingBehavior
{
	private static final long serialVersionUID = 1L;

	private final String valueToAdd;

	private final int maximumLength;

	private final int minimumLength;

	public GirorekeningStringAppenderAjaxHandler()
	{
		this("P", 1, 8);
	}

	public GirorekeningStringAppenderAjaxHandler(String valueToAdd)
	{
		this(valueToAdd, 1, 8);
	}

	public GirorekeningStringAppenderAjaxHandler(String valueToAdd, int minimumLength,
			int maximumLength)
	{
		super("onchange");
		this.valueToAdd = valueToAdd;
		this.minimumLength = minimumLength;
		this.maximumLength = maximumLength;
	}

	@Override
	protected void onUpdate(AjaxRequestTarget target)
	{
		String value = ((TextField< ? >) getComponent()).getInput();

		if (StringUtil.isNotEmpty(value))
			value = value.trim();

		if (minimumLength <= value.length() && value.length() <= maximumLength)
			value = valueToAdd + value;

		getComponent().setDefaultModelObject(value);
		target.addComponent(getComponent());
	}
}
