/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.text;

import nl.topicus.cobra.entities.Time;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.AppendingStringBuffer;

/**
 * Class voor het invoeren van een tijd in een textfield. Gebaseerd op Vocusvariant.
 * 
 * @author maatman
 */
public class TijdField extends TextField<Time>
{
	public class TijdFieldAjaxHandler extends AjaxEventBehavior
	{
		private static final long serialVersionUID = 1L;

		public TijdFieldAjaxHandler()
		{
			super("onchange");
		}

		@Override
		protected CharSequence getEventHandler()
		{
			return generateCallbackScript(new AppendingStringBuffer("wicketAjaxPost('").append(
				getCallbackUrl()).append("', wicketSerialize(this)"));
		}

		@Override
		protected void onEvent(AjaxRequestTarget target)
		{
			String datum = TijdField.this.getInput();
			Time time = null;

			if (StringUtil.isNotEmpty(datum))
			{
				time = TimeUtil.getInstance().parseTimeString(datum);
				if (time == null)
				{
					TijdField.this.setModelObject(null);
				}
				else
				{
					TijdField.this.setModelObject(time);
				}
			}
			else
			{
				TijdField.this.setModelObject(null);
			}

			target.addComponent(TijdField.this);
			onUpdate(target, getModelObject());
		}
	}

	@SuppressWarnings("unused")
	protected void onUpdate(AjaxRequestTarget target, Time newValue)
	{
	}

	private static final long serialVersionUID = 6883784609679074032L;

	public TijdField(String id)
	{
		this(id, null);
	}

	public TijdField(String id, IModel<Time> model)
	{
		super(id, model, Time.class);
		setOutputMarkupId(true);
		add(new TijdFieldAjaxHandler());
	}
}
