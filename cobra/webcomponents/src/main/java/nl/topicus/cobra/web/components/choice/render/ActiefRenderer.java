/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.choice.render;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * Renderer voor het actief veld van de verschillende db entiteiten.
 * 
 * @author marrink
 */
public final class ActiefRenderer implements IChoiceRenderer<Boolean>
{
	private static final long serialVersionUID = 1L;

	@Override
	public Object getDisplayValue(Boolean object)
	{
		if (object == null)
			return "Beide";
		if (object.getClass() == Boolean.class && (Boolean) object)
			return "Actief";
		if (object.getClass() == Boolean.class)
			return "Niet actief";
		return String.valueOf(object);
	}

	@Override
	public String getIdValue(Boolean object, int index)
	{
		return String.valueOf(index);
	}
}
