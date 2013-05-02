/*
 * $Id: NameTextField.java,v 1.1 2007-07-25 11:24:06 loite Exp $
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

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

/**
 * @author loite
 */
public class NameTextField extends TextField<String>
{
	private static final long serialVersionUID = 1L;

	public NameTextField(String id)
	{
		super(id, String.class);
		add(new HoofdletterAjaxHandler(HoofdletterMode.ElkWoord));
	}

	public NameTextField(String id, IModel<String> object)
	{
		super(id, object, String.class);
		add(new HoofdletterAjaxHandler(HoofdletterMode.ElkWoord));
	}
}
