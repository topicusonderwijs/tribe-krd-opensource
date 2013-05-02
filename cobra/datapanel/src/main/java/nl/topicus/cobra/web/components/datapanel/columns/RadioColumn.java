/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel.columns;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsScopeContext;

/**
 * Column die default een (niet-aangevinkte) checkbox toont.
 * 
 * @author bos, ambrosius
 */
public class RadioColumn<T, E> extends AbstractCustomColumn<T>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Value van de radio.
	 */
	private IModel<E> radioValue;

	private boolean ajax;

	public RadioColumn(String id, String header, IModel<E> radioValue)
	{
		super(id, header);
		this.radioValue = radioValue;
	}

	public RadioColumn<T, E> setAjax(boolean ajax)
	{
		this.ajax = ajax;
		return this;
	}

	/**
	 * @see org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator#populateItem(org.apache.wicket.markup.repeater.Item,
	 *      java.lang.String, org.apache.wicket.model.IModel)
	 */
	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<T> rowModel, int span)
	{
		cell.add(new RadioPanel(componentId, "radio", rowModel));
	}

	/**
	 * Panel als een wrapper om de checkbox.
	 * 
	 * @author bos
	 */
	private final class RadioPanel extends Panel
	{
		private static final long serialVersionUID = 1L;

		public RadioPanel(String id, final String radioId, final IModel<T> rowModel)
		{
			super(id);

			final Radio<E> radio = new Radio<E>(radioId, getRadioValue(rowModel))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isEnabled()
				{
					return super.isEnabled() && RadioColumn.this.isContentsEnabled(rowModel);
				}

				@Override
				public boolean isVisible()
				{
					return super.isVisible() && RadioColumn.this.isContentsVisible(rowModel);
				}
			};
			if (ajax)
			{
				radio.add(new AjaxEventBehavior("onclick")
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onEvent(AjaxRequestTarget target)
					{
						update(radio, target);
					}

					@Override
					protected final CharSequence getEventHandler()
					{
						return generateCallbackScript(new AppendingStringBuffer("wicketAjaxPost('")
							.append(getCallbackUrl(false)).append(
								"', wicketSerialize(Wicket.$('" + getComponent().getMarkupId()
									+ "'))"));
					}
				});
			}
			radio.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public JsScope callback()
				{
					return new JsScope("event")
					{
						private static final long serialVersionUID = 1L;

						@Override
						protected void execute(JsScopeContext scopeContext)
						{
							scopeContext.append("event.stopPropagation()");
						}
					};
				}
			}));
			add(radio);
		}
	}

	@SuppressWarnings("unchecked")
	private void update(Radio<E> radio, AjaxRequestTarget target)
	{
		RadioGroup<E> formComponent = radio.findParent(RadioGroup.class);
		try
		{
			formComponent.inputChanged();
			formComponent.validate();
			if (formComponent.hasErrorMessage())
			{
				formComponent.invalid();
				onError(target, null);
			}
			else
			{
				formComponent.valid();
				formComponent.updateModel();
				onUpdate(target);
			}
		}
		catch (RuntimeException e)
		{
			onError(target, e);
		}
	}

	@SuppressWarnings("unused")
	protected void onUpdate(AjaxRequestTarget target)
	{
	}

	@SuppressWarnings("unused")
	protected void onError(AjaxRequestTarget target, RuntimeException e)
	{
		if (e != null)
		{
			throw e;
		}
	}

	@SuppressWarnings("unused")
	public IModel<E> getRadioValue(IModel<T> rowModel)
	{
		return radioValue;
	}
}
