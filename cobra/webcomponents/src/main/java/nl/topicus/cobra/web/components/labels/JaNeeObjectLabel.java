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
 * Label dat de String Ja of Nee toont afhankelijk of het model wel of geen (null) object
 * heeft.
 */
public final class JaNeeObjectLabel extends Label
{
	private static final long serialVersionUID = 1L;

	public JaNeeObjectLabel(String id)
	{
		super(id);
	}

	public JaNeeObjectLabel(String id, IModel< ? > model)
	{
		super(id, model);
	}

	@Override
	protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag)
	{
		if (getDefaultModelObject() == null)
			replaceComponentTagBody(markupStream, openTag, "Nee");
		else
			replaceComponentTagBody(markupStream, openTag, "Ja");
	}
}
