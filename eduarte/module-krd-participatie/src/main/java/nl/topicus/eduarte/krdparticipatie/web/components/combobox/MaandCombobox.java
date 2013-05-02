/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krdparticipatie.web.components.combobox;

import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.participatie.zoekfilters.AanwezigheidMaandFilter;
import nl.topicus.eduarte.participatie.zoekfilters.AanwezigheidMaandFilter.Maand;

import org.apache.wicket.model.IModel;

/**
 * @author loite
 */
public class MaandCombobox extends AbstractAjaxDropDownChoice<Maand>
{
	private static final long serialVersionUID = 1L;

	public MaandCombobox(String id)
	{
		this(id, null);
	}

	public MaandCombobox(String id, IModel<Maand> model)
	{
		super(id, model, AanwezigheidMaandFilter.Maand.MAANDEN, new MaandRenderer());
	}

}
