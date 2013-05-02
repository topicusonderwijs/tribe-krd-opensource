/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.labels;

import nl.topicus.cobra.converters.PasswordConverter;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

/**
 * Label dat een passwoord veilig kan weergeven. De text wordt nl vervangen door ***.
 * 
 * @author marrink
 */
public class PasswordLabel extends Label
{
	private static final long serialVersionUID = 1L;

	public PasswordLabel(String id)
	{
		super(id);
	}

	public PasswordLabel(String id, IModel< ? > model)
	{
		super(id, model);
	}

	@Override
	public IConverter getConverter(Class< ? > type)
	{
		return new PasswordConverter();
	}
}
