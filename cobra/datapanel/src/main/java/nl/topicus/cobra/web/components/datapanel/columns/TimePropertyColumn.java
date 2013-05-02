/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel.columns;

import java.util.Date;

import nl.topicus.cobra.entities.Time;

import org.apache.wicket.Application;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

/**
 * CustomPropertyColumn specialized in displaying {@link Date} fields as {@link Time}.
 * 
 * @author marrink
 */
public class TimePropertyColumn<T> extends CustomPropertyColumn<T>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TimePropertyColumn(String id, String header, String propertyExpression)
	{
		super(id, header, propertyExpression);
	}

	public TimePropertyColumn(String id, String header, String sortProperty,
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
				return super.isEnabled() && TimePropertyColumn.this.isContentsEnabled(rowModel);
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && TimePropertyColumn.this.isContentsVisible(rowModel);
			}

			@Override
			@SuppressWarnings("unchecked")
			public IConverter getConverter(Class type)
			{
				return Application.get().getConverterLocator().getConverter(Time.class);
			}
		}.setRenderBodyOnly(true));
	}
}
