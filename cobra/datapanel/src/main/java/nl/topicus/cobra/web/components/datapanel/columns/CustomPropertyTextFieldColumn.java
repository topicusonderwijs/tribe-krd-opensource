/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel.columns;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

/**
 * CustomPropertyColumn die de waarde niet in een label toont, maar in een tekstveld zodat
 * de gebruiker deze waarde kan aanpassen. Opslaan dient nog door de pagina geregeld te
 * worden.
 * 
 * @author bos
 */
public class CustomPropertyTextFieldColumn<T> extends CustomPropertyColumn<T>
{
	private static final long serialVersionUID = 1L;

	public CustomPropertyTextFieldColumn(String id, String header, String sortProperty,
			String propertyExpression)
	{
		super(id, header, sortProperty, propertyExpression);
	}

	/**
	 * @param id
	 * @param header
	 * @param propertyExpression
	 */
	public CustomPropertyTextFieldColumn(String id, String header, String propertyExpression)
	{
		super(id, header, propertyExpression);
	}

	/**
	 * @see nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn#populateItem(org.apache.wicket.markup.repeater.Item,
	 *      java.lang.String, org.apache.wicket.model.IModel)
	 */
	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<T> rowModel, int span)
	{
		CustomPropertyTextFieldColumnPanel panel =
			new CustomPropertyTextFieldColumnPanel(componentId);
		panel.setVisible(isContentsVisible(rowModel));
		panel.add(createTextField("textfield", createLabelModel(rowModel), rowModel));
		cell.add(panel);
	}

	@SuppressWarnings("unchecked")
	protected TextField< ? > createTextField(String id, IModel< ? > propertyModel,
			final IModel<T> rowModel)
	{
		TextField ret = new TextField(id, propertyModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return super.isEnabled()
					&& CustomPropertyTextFieldColumn.this.isContentsEnabled(rowModel);
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible()
					&& CustomPropertyTextFieldColumn.this.isContentsVisible(rowModel);
			}
		};
		ret.setRequired(false);
		return ret;
	}
}
