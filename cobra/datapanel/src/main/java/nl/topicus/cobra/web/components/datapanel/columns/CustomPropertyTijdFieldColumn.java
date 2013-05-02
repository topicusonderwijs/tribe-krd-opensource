/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel.columns;

import nl.topicus.cobra.entities.Time;
import nl.topicus.cobra.web.components.text.TijdField;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class CustomPropertyTijdFieldColumn<T> extends CustomPropertyColumn<T>
{
	private static final long serialVersionUID = 1L;

	public CustomPropertyTijdFieldColumn(String id, String header, String sortProperty,
			String propertyExpression)
	{
		super(id, header, sortProperty, propertyExpression);
	}

	public CustomPropertyTijdFieldColumn(String id, String header, String propertyExpression)
	{
		super(id, header, propertyExpression);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			final IModel<T> rowModel, int span)
	{
		TijdPanel panel = new TijdPanel(componentId);
		panel.add(new TijdField("tijdField", (IModel<Time>) createLabelModel(rowModel))
			.setRequired(false));
		cell.add(panel);
	}

	private class TijdPanel extends Panel
	{
		private static final long serialVersionUID = 1L;

		private TijdPanel(String id)
		{
			super(id);
			setRenderBodyOnly(true);
		}
	}
}
