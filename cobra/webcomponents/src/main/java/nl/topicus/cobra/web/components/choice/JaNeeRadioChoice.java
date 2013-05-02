/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.choice;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;

/**
 * Een component dat de keuze voor Ja/Nee toont met behulp van radio buttons in plaats van
 * een dropdown box.
 * <p>
 * <h3>Voorbeeld</h3>
 * <p>
 * Naam is officieel: <label><input type="radio" name="janee" value="ja"
 * checked="checked"> Ja</label> <label><input type="radio" name="janee" value="nee">
 * Nee</label>
 * 
 * @author Martijn Dashorst
 */
public class JaNeeRadioChoice extends RadioChoice<Boolean>
{
	private static final long serialVersionUID = 1L;

	protected static final ArrayList<Boolean> values = new ArrayList<Boolean>(2);

	protected static final IModel<List<Boolean>> valuesModel = new ListModel<Boolean>(values);
	static
	{
		values.add(Boolean.TRUE);
		values.add(Boolean.FALSE);
	}

	public JaNeeRadioChoice(String id)
	{
		this(id, null);
	}

	public JaNeeRadioChoice(String id, IModel<Boolean> model)
	{
		super(id, model, valuesModel, new JaNeeRenderer());
		setPrefix("");
		setSuffix("&nbsp;");
		setOutputMarkupId(true);
	}

	protected JaNeeRadioChoice(String id, IModel<Boolean> model,
			IModel< ? extends List<Boolean>> choices, IChoiceRenderer<Boolean> renderer)
	{
		super(id, model, choices, renderer);
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
}
