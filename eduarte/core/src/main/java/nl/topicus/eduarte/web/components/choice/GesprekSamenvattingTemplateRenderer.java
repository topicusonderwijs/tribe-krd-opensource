package nl.topicus.eduarte.web.components.choice;

/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */

import nl.topicus.eduarte.entities.dbs.trajecten.GesprekSamenvattingTemplate;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * @author N. Henzen
 */
public class GesprekSamenvattingTemplateRenderer implements
		IChoiceRenderer<GesprekSamenvattingTemplate>
{
	private static final long serialVersionUID = 1L;

	@Override
	public Object getDisplayValue(GesprekSamenvattingTemplate object)
	{
		return object.getNaam();
	}

	@Override
	public String getIdValue(GesprekSamenvattingTemplate object, int index)
	{
		return object.getId().toString();
	}

}
