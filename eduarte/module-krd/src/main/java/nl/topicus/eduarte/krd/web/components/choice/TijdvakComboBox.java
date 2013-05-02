/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.components.choice;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.krd.web.components.choice.renderer.TijdvakRenderer;

import org.apache.wicket.model.IModel;

/**
 * @author vandekamp Een combobox waarin in tijdvak gekozen kan worden tijdvak 1 - 3
 */
public class TijdvakComboBox extends AbstractAjaxDropDownChoice<Integer>
{
	private static final long serialVersionUID = 1L;

	public TijdvakComboBox(String id, IModel<Integer> model)
	{
		super(id, model, getChoiceList(), new TijdvakRenderer());
	}

	private static List<Integer> getChoiceList()
	{
		List<Integer> list = new ArrayList<Integer>();
		list.add(2);
		list.add(3);
		return list;
	}
}
