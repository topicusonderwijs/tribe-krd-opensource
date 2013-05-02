/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel.columns;

import java.util.Locale;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converters.BooleanConverter;

/**
 * CustomPropertyColumn specialized in displaying {@link Boolean} fields.
 * 
 * @author hoeve
 */
public class BooleanPropertyColumn<T> extends CustomPropertyColumn<T>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Boolean converter die beide kanten op convert.
	 * 
	 * @author hoeve
	 */
	private final class DuplexBooleanConverter extends BooleanConverter
	{
		@Override
		public String convertToString(Object value, Locale locale)
		{
			Boolean bool = (Boolean) value;
			if (bool == null)
			{
				return null;
			}
			else if (bool.booleanValue())
				return "Ja";
			else
				return "Nee";
		}

		private static final long serialVersionUID = 1L;

	}

	private BooleanConverter converter = new DuplexBooleanConverter();

	public BooleanPropertyColumn(String id, String header, String propertyExpression)
	{
		super(id, header, propertyExpression);
	}

	public BooleanPropertyColumn(String id, String header, String sortProperty,
			String propertyExpression)
	{
		super(id, header, sortProperty, propertyExpression);
	}

	public BooleanPropertyColumn(String id, String header, String sortProperty,
			String propertyExpression, boolean repeatWhenEqualToPrevRow)
	{
		super(id, header, sortProperty, propertyExpression, repeatWhenEqualToPrevRow);
	}

	/**
	 * @see nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn#populateItem(org.apache.wicket.markup.repeater.Item,
	 *      java.lang.String, org.apache.wicket.model.IModel)
	 */
	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			final IModel<T> rowModel, int span)
	{
		cell.add(new Label(componentId, createLabelModel(rowModel))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return super.isEnabled() && BooleanPropertyColumn.this.isContentsEnabled(rowModel);
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && BooleanPropertyColumn.this.isContentsVisible(rowModel);
			}

			@Override
			public IConverter getConverter(Class< ? > type)
			{
				return converter;
			}
		}.setRenderBodyOnly(true));
	}
}
