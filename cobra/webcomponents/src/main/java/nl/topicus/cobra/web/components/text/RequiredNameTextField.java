/*
 * $Id: RequiredNameTextField.java,v 1.1 2007-07-25 11:24:06 loite Exp $
 * $Revision: 1.1 $
 * $Date: 2007-07-25 11:24:06 $
 *
 * ====================================================================
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.text;

import org.apache.wicket.model.IModel;

/**
 * @author loite
 */
public class RequiredNameTextField extends NameTextField
{
	private static final long serialVersionUID = 1L;

	public RequiredNameTextField(String id)
	{
		super(id);
		setRequired(true);
	}

	public RequiredNameTextField(String id, IModel<String> model)
	{
		super(id, model);
		setRequired(true);
	}

}
