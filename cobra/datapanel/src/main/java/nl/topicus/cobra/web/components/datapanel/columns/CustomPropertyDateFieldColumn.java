/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel.columns;

import java.util.Date;

import nl.topicus.cobra.web.components.text.DatumField;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * CustomPropertyColumn die de waarde niet in een label toont, maar in een datumfield
 * zodat de gebruiker deze waarde kan aanpassen. Opslaan dient nog door de pagina geregeld
 * te worden.
 * 
 * @author vandekamp
 */
public class CustomPropertyDateFieldColumn<T> extends CustomPropertyColumn<T>
{
	private static final long serialVersionUID = 1L;

	private boolean required = false;

	/**
	 * @param id
	 * @param header
	 * @param sortProperty
	 * @param propertyExpression
	 */
	public CustomPropertyDateFieldColumn(String id, String header, String sortProperty,
			String propertyExpression)
	{
		super(id, header, sortProperty, propertyExpression);
	}

	/**
	 * @param id
	 * @param header
	 * @param propertyExpression
	 */
	public CustomPropertyDateFieldColumn(String id, String header, String propertyExpression)
	{
		super(id, header, propertyExpression);
	}

	/**
	 * @see nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn#populateItem(org.apache.wicket.markup.repeater.Item,
	 *      java.lang.String, org.apache.wicket.model.IModel)
	 */
	@Override
	public void populateItem(WebMarkupContainer cell, String componentId,
			final WebMarkupContainer row, final IModel<T> rowModel, int span)
	{
		DatumPanel panel = new DatumPanel(componentId);
		panel.add(new DatumField("datumField", createLabelModel(rowModel))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target, Date newValue)
			{
				CustomPropertyDateFieldColumn.this.onUpdate(row, target, newValue);
			}

			@Override
			public boolean isEnabled()
			{
				return super.isEnabled()
					&& CustomPropertyDateFieldColumn.this.isContentsEnabled(rowModel);
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible()
					&& CustomPropertyDateFieldColumn.this.isContentsVisible(rowModel);
			}

		}.setRequired(required));
		cell.add(panel);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IModel<Date> createLabelModel(IModel<T> rowModel)
	{
		return (IModel<Date>) super.createLabelModel(rowModel);
	}

	@SuppressWarnings("unused")
	protected void onUpdate(WebMarkupContainer row, AjaxRequestTarget target, Date newValue)
	{
	}

	private class DatumPanel extends Panel
	{
		private static final long serialVersionUID = 1L;

		private DatumPanel(String id)
		{
			super(id);
			setRenderBodyOnly(true);
		}
	}

	public boolean isRequired()
	{
		return required;
	}

	public CustomPropertyDateFieldColumn<T> setRequired(boolean required)
	{
		this.required = required;
		return this;
	}

}
