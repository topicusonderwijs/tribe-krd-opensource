/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krdparticipatie.web.components.combobox;

import nl.topicus.eduarte.participatie.zoekfilters.AanwezigheidMaandFilter.Maand;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * @author loite
 */
public class MaandRenderer implements IChoiceRenderer<Maand>
{
	private static final long serialVersionUID = 1L;

	@Override
	public Object getDisplayValue(Maand object)
	{
		return object.getNaam();
	}

	@Override
	public String getIdValue(Maand object, int index)
	{
		return object.getNaam();
	}

}
