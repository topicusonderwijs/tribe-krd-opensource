/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.choice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.web.components.choice.render.ActiefRenderer;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

/**
 * @author marrink
 */
public class ActiefCombobox extends DropDownChoice<Boolean>
{
	private static final long serialVersionUID = 1L;

	public ActiefCombobox(String id)
	{
		this(id, false);
	}

	public ActiefCombobox(String id, boolean required)
	{
		super(id, new BooleanModel(), new ActiefRenderer());
		setNullValid(!required);
		setRequired(required);
	}

	public ActiefCombobox(String id, IModel<Boolean> model)
	{
		this(id, model, false);
	}

	public ActiefCombobox(String id, IModel<Boolean> model, boolean required)
	{
		super(id, model, new BooleanModel(), new ActiefRenderer());
		setNullValid(!required);
		setRequired(required);
	}

	/**
	 * Model dat 2 of 3 waardes opleverd: null, true en false. null wordt alleen
	 * opgeleverd als dit mag.
	 * 
	 * @author marrink
	 */
	public static final class BooleanModel implements IModel<List<Boolean>>
	{
		private static final long serialVersionUID = 1L;

		private final List<Boolean> values;

		public BooleanModel()
		{
			List<Boolean> temp = new ArrayList<Boolean>(3);
			temp.add(true);
			temp.add(false);
			values = Collections.unmodifiableList(temp);
		}

		@Override
		public List<Boolean> getObject()
		{
			return values;
		}

		@Override
		public void setObject(List<Boolean> object)
		{
			// noop
		}

		@Override
		public void detach()
		{
			// noop
		}

	}
}
