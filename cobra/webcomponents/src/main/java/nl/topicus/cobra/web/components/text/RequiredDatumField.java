/*
 * $Id: RequiredDatumField.java,v 1.1 2007-07-12 12:22:32 loite Exp $
 * $Revision: 1.1 $
 * $Date: 2007-07-12 12:22:32 $
 *
 * ====================================================================
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.text;

import java.util.Date;

import org.apache.wicket.model.IModel;

/**
 * @author loite
 */
public class RequiredDatumField extends DatumField
{
	private static final long serialVersionUID = 1L;

	public RequiredDatumField(String id)
	{
		this(id, null);
	}

	public RequiredDatumField(String id, IModel<Date> model)
	{
		super(id, model);
		setRequired(true);
	}

}
