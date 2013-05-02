/*
 * $Id EnumRequiredCombobox.java,v 1.0 2006/01/31 10:49:29 bos Exp  $
 * $Revision 1.0 $
 * $Date 2006/01/31 10:49:29 $
 *
 * ====================================================================
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.choice;

import org.apache.wicket.model.IModel;

/**
 * @author bos
 * 
 */
public class EnumRequiredCombobox<T extends Enum<T>> extends EnumCombobox<T>
{
	private static final long serialVersionUID = 7001101203206098609L;

	public EnumRequiredCombobox(String id, T... choices)
	{
		super(id, choices);
		setRequired(true);
	}

	public EnumRequiredCombobox(String id, IModel<T> model, T... choices)
	{
		super(id, model, choices);
		setRequired(true);
	}
}
