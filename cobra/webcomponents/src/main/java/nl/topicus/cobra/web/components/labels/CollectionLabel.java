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
 * Label dat de elementen van een Collection toont, gescheiden door komma's. Roept de
 * toString aan van alle elementen.
 */
public final class CollectionLabel extends Label
{
	private static final long serialVersionUID = 1L;

	public CollectionLabel(String id)
	{
		super(id);
	}

	public CollectionLabel(String id, IModel< ? > model)
	{
		super(id, model);
	}

	@Override
	protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag)
	{
		if (getDefaultModelObject() instanceof Iterable< ? >)
		{
			StringBuilder builder = new StringBuilder();
			Iterable< ? > iterable = (Iterable< ? >) getDefaultModelObject();
			for (Object o : iterable)
			{
				if (builder.length() > 0)
					builder.append(", ");
				builder.append(o.toString());
			}
			replaceComponentTagBody(markupStream, openTag, builder.toString());
		}
		else if (getDefaultModelObject() == null)
		{
			replaceComponentTagBody(markupStream, openTag, "");
		}
		else
		{
			throw new IllegalArgumentException("Invalid modelobject type: "
				+ getDefaultModelObject().getClass().getSimpleName());
		}
	}
}
