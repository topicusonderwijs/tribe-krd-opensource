/*
 * $Id: RequiredVoorlettersTextField.java,v 1.1 2007-07-25 11:24:06 loite Exp $
 * $Revision: 1.1 $
 * $Date: 2007-07-25 11:24:06 $
 *
 * ====================================================================
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.text;

import nl.topicus.cobra.transformers.HoofdletterMode;
import nl.topicus.cobra.web.behaviors.HoofdletterAjaxHandler;

import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.IModel;

/**
 * @author dikkers
 */
public class RequiredVoorlettersTextField extends RequiredTextField<String>
{

	private static final long serialVersionUID = 1L;

	public RequiredVoorlettersTextField(String id)
	{
		super(id, String.class);
		add(new HoofdletterAjaxHandler(HoofdletterMode.PuntSeperated));
	}

	public RequiredVoorlettersTextField(String id, IModel<String> model)
	{
		super(id, model, String.class);
		add(new HoofdletterAjaxHandler(HoofdletterMode.PuntSeperated));
	}
}
