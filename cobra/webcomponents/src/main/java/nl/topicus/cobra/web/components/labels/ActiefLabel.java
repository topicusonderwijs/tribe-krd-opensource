/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.labels;

import nl.topicus.cobra.converters.ActiefConverter;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

/**
 * Label dat de String Actief of Niet actief toont afhankelijk van de boolean waarde van
 * het model.
 * 
 * @author marrink
 */
public class ActiefLabel extends Label
{
	private static final long serialVersionUID = 1L;

	public ActiefLabel(String id)
	{
		super(id);
	}

	public ActiefLabel(String id, IModel<Boolean> model)
	{
		super(id, model);
	}

	@Override
	public IConverter getConverter(Class< ? > type)
	{
		return new ActiefConverter();
	}
}
