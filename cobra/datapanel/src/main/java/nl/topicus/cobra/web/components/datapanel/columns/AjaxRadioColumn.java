/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel.columns;

import nl.topicus.cobra.web.components.panels.TypedPanel;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.model.IModel;

/**
 * Column die default een (niet-aangevinkte) ajax checkbox toont.
 * 
 * LET OP: De waarde van de checkbox is gelijk aan het bij de row behorende object!
 * 
 * @author idserda
 */
public class AjaxRadioColumn<T> extends AbstractCustomColumn<T>
{
	private static final long serialVersionUID = 1L;

	public AjaxRadioColumn(String id, String header)
	{
		super(id, header);
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<T> rowModel, int span)
	{
		cell.add(new AjaxRadioPanel(componentId, "radio", rowModel));
	}

	private final class AjaxRadioPanel extends TypedPanel<T>
	{
		private static final long serialVersionUID = 1L;

		public AjaxRadioPanel(String id, final String radioId, final IModel<T> rowModel)
		{
			super(id, rowModel);
			Radio<T> radio = new Radio<T>(radioId, rowModel)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isEnabled()
				{
					return super.isEnabled() && AjaxRadioColumn.this.isContentsEnabled(getModel());
				}

				@Override
				public boolean isVisible()
				{
					return super.isVisible() && AjaxRadioColumn.this.isContentsVisible(getModel());
				}
			};

			radio.add(new AjaxEventBehavior("onchange")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onEvent(AjaxRequestTarget target)
				{
					onUpdate(target, rowModel);
				}
			});

			add(radio);
		}
	}

	@SuppressWarnings("unused")
	protected void onUpdate(AjaxRequestTarget target, IModel<T> selectedModel)
	{
	}
}
