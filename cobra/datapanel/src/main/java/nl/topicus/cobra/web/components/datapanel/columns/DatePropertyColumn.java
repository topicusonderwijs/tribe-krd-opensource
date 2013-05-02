/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel.columns;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converters.DateConverter;

/**
 * CustomPropertyColumn specialized in displaying {@link Date} fields with a custom
 * pattern.
 * 
 * @author marrink
 */
public class DatePropertyColumn<T> extends CustomPropertyColumn<T>
{
	/**
	 * Date converter die gebruik maakt van het pattern.
	 * 
	 * @author marrink
	 */
	private final class PatternDateConverter extends DateConverter
	{
		private static final long serialVersionUID = 1L;

		/**
		 * @see org.apache.wicket.util.convert.converters.DateConverter#getDateFormat(java.util.Locale)
		 */
		@Override
		public DateFormat getDateFormat(Locale locale)
		{
			return new SimpleDateFormat(getDatePattern(), locale == null ? Locale.getDefault()
				: locale);
		}
	}

	private static final long serialVersionUID = 1L;

	private String datePattern = "dd-MM-yyyy";

	private DateConverter converter = new PatternDateConverter();

	public DatePropertyColumn(String id, String header, String propertyExpression)
	{
		super(id, header, propertyExpression);
	}

	public DatePropertyColumn(String id, String header, String sortProperty,
			String propertyExpression)
	{
		super(id, header, sortProperty, propertyExpression);
	}

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
				return super.isEnabled() && DatePropertyColumn.this.isContentsEnabled(rowModel);
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && DatePropertyColumn.this.isContentsVisible(rowModel);
			}

			@Override
			@SuppressWarnings("unchecked")
			public IConverter getConverter(Class type)
			{
				// if (Date.class.isAssignableFrom(type))
				return converter;
				// getModelObjectAsString gebruikt de String class waardoor alle datums
				// dus als tijd getoond worden, niet de bedoeling.
				// return
				// Application.get().getConverterLocator().getConverter(Time.class);
			}
		}.setRenderBodyOnly(true));
	}

	public final String getDatePattern()
	{
		return datePattern;
	}

	public final DatePropertyColumn<T> setDatePattern(String datePattern)
	{
		this.datePattern = datePattern;
		return this;
	}
}
