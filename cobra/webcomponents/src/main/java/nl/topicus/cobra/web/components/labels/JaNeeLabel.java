/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.labels;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * Label dat de String Ja, Nee of - toont afhankelijk van de boolean waarde van het model.
 * Gebaseerd op het JaNeeLabel van iridium
 */
public final class JaNeeLabel extends Label
{
	private static final long serialVersionUID = 1L;

	public JaNeeLabel(String id)
	{
		super(id);
	}

	public JaNeeLabel(String id, IModel<Boolean> model)
	{
		super(id, model);
	}

	@Override
	protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag)
	{
		if (getDefaultModelObject() instanceof Boolean)
		{
			if (getModelObject())
				replaceComponentTagBody(markupStream, openTag, "Ja");
			else
				replaceComponentTagBody(markupStream, openTag, "Nee");
		}
		else if (getDefaultModelObject() == null)
		{
			replaceComponentTagBody(markupStream, openTag, "-");
		}
		else
		{
			throw new IllegalArgumentException("Invalid modelobject type: ");
		}
	}

	public Boolean getModelObject()
	{
		return (Boolean) getDefaultModelObject();
	}
}
