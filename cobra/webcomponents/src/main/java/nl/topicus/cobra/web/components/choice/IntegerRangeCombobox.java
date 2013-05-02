/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.choice;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

/**
 * Combobox welke een lijst integers toont.
 * 
 * @author bos
 */
public class IntegerRangeCombobox extends DropDownChoice<Integer>
{
	private static final long serialVersionUID = 1L;

	public IntegerRangeCombobox(String id, List<Integer> choices)
	{
		super(id, choices);
	}

	public IntegerRangeCombobox(String id, int van, int tot)
	{
		super(id, toList(van, tot));
	}

	public IntegerRangeCombobox(String id, IModel<Integer> model, int van, int tot)
	{
		super(id, model, toList(van, tot));
	}

	public IntegerRangeCombobox(String id, IModel<Integer> model, List<Integer> choices)
	{
		super(id, model, choices);
	}

	private static List<Integer> toList(int van, int tot)
	{
		if (van > tot)
			throw new IllegalStateException(van + " is groter dan " + tot);
		List<Integer> list = new ArrayList<Integer>(tot - van + 1);
		int i = van;
		while (i <= tot)
		{
			list.add(new Integer(i));
			i++;
		}
		return list;
	}

}
