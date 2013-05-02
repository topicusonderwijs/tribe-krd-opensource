/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.choice;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.ResourcePostfixProvider;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;

/**
 * @author loite
 */
public class JaNeeCombobox extends AbstractAjaxDropDownChoice<Boolean> implements
		ResourcePostfixProvider
{
	private static final long serialVersionUID = 1L;

	private static final ArrayList<Boolean> values = new ArrayList<Boolean>(2);

	private static final IModel<List<Boolean>> valuesModel = new ListModel<Boolean>(values);
	static
	{
		values.add(Boolean.TRUE);
		values.add(Boolean.FALSE);
	}

	public JaNeeCombobox(String id)
	{
		this(id, null);
	}

	public JaNeeCombobox(String id, IModel<Boolean> model)
	{
		this(id, model, false);
	}

	public JaNeeCombobox(String id, IModel<Boolean> model, boolean addAjaxHandler)
	{
		this(id, model, new JaNeeRenderer(), addAjaxHandler);
	}

	public JaNeeCombobox(String id, IModel<Boolean> model, IChoiceRenderer<Boolean> renderer,
			boolean addAjaxHandler)
	{
		super(id, model, valuesModel, renderer, addAjaxHandler);
	}

	public JaNeeCombobox(String id, IModel<Boolean> model, IChoiceRenderer<Boolean> renderer,
			IModel<List<Boolean>> valuesModel, boolean addAjaxHandler)
	{
		super(id, model, valuesModel, renderer, addAjaxHandler);
	}

	private static final class JaNeeRenderer implements IChoiceRenderer<Boolean>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Object getDisplayValue(Boolean object)
		{
			if (object.booleanValue())
				return "Ja";
			return "Nee";
		}

		@Override
		public String getIdValue(Boolean object, int index)
		{
			return object.toString();
		}
	}

	@Override
	public String getResourcePostfix()
	{
		IModel<String> label = getLabel();
		return label == null ? null : label.getObject().toString().replaceAll("\\s", "");
	}
}
