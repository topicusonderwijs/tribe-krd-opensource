/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.choice;

import nl.topicus.eduarte.entities.dbs.trajecten.GesprekSamenvattingZin;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * @author N Henzen
 */
public class GesprekSamenvattingZinRenderer implements IChoiceRenderer<GesprekSamenvattingZin>
{
	private static final long serialVersionUID = 1L;

	@Override
	public Object getDisplayValue(GesprekSamenvattingZin object)
	{
		return object.getZin();
	}

	@Override
	public String getIdValue(GesprekSamenvattingZin object, int index)
	{
		return object.getId().toString();
	}
}
