/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel.columns;

import java.util.Date;

import nl.topicus.cobra.util.TimeUtil;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * @author maatman
 */
public class DateTimeColumn<T> extends CustomPropertyColumn<T>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DateTimeColumn(String id, String header, String propertyExpression)
	{
		super(id, header, propertyExpression);
	}

	public DateTimeColumn(String id, String header, String sortProperty, String propertyExpression)
	{
		super(id, header, sortProperty, propertyExpression);
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			final IModel<T> rowModel, int span)
	{
		IModel<Date> mod = new PropertyModel<Date>(rowModel, getPropertyExpression());
		cell.add(new Label(componentId, new Model<String>(TimeUtil.getInstance()
			.formatDateTimeNoSeconds(mod.getObject())))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return super.isEnabled() && DateTimeColumn.this.isContentsEnabled(rowModel);
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && DateTimeColumn.this.isContentsVisible(rowModel);
			}
		});
	}
}
