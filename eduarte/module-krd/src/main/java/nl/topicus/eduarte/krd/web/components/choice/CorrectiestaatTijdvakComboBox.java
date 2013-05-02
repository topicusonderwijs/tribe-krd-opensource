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
public class CorrectiestaatTijdvakComboBox extends AbstractAjaxDropDownChoice<Integer>
{
	private static final long serialVersionUID = 1L;

	public CorrectiestaatTijdvakComboBox(String id)
	{
		super(id, getChoiceList(), new TijdvakRenderer());
	}

	public CorrectiestaatTijdvakComboBox(String id, IModel<Integer> model)
	{
		super(id, model, getChoiceList(), new TijdvakRenderer());
	}

	private static List<Integer> getChoiceList()
	{
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		return list;
	}
}
