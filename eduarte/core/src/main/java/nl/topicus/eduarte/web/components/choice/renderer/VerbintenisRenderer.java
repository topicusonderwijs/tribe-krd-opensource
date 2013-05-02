/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.choice.renderer;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * Renderer voor {@link Verbintenis}.
 * 
 * @author marrink
 */
public class VerbintenisRenderer implements IChoiceRenderer<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	@Override
	public Object getDisplayValue(Verbintenis verbintenis)
	{
		if (verbintenis == null)
			return null;
		return verbintenis.getOmschrijving(null);
	}

	@Override
	public String getIdValue(Verbintenis verbintenis, int index)
	{
		if (verbintenis == null)
			return null;
		if (verbintenis.getId() == null)
			return null;
		return verbintenis.getId().toString();
	}
}
