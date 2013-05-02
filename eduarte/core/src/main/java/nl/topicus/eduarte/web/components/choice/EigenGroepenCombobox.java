/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.choice;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.ResourcePostfixProvider;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;

/**
 * @author loite
 */
public class EigenGroepenCombobox extends AbstractAjaxDropDownChoice<Boolean> implements
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

	public EigenGroepenCombobox(String id, IModel<Boolean> model)
	{
		super(id, model, valuesModel, new EigenGroepenRenderer(), false);
	}

	private static final class EigenGroepenRenderer implements IChoiceRenderer<Boolean>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Object getDisplayValue(Boolean object)
		{
			if (object.booleanValue())
				return "Alle groepen";
			return "Eigen groepen";
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
