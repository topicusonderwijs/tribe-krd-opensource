/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel.columns;

import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.panels.TypedPanel;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

/**
 * Kolom die een property van het object uit het model toont.
 * 
 * @author marrink
 */
public class ClickableCustomPropertyColumn<T> extends CustomPropertyColumn<T>
{
	private static final long serialVersionUID = 1L;

	public ClickableCustomPropertyColumn(String id, String header, String propertyExpression)
	{
		super(id, header, propertyExpression);
	}

	public ClickableCustomPropertyColumn(String id, String header, String propertyExpression,
			boolean repeatWhenEqualToPrevRow)
	{
		super(id, header, propertyExpression, repeatWhenEqualToPrevRow);
	}

	public ClickableCustomPropertyColumn(String id, String header, String sortProperty,
			String propertyExpression)
	{
		super(id, header, sortProperty, propertyExpression);
	}

	public ClickableCustomPropertyColumn(String id, String header, String sortProperty,
			String propertyExpression, boolean repeatWhenEqualToPrevRow)
	{
		super(id, header, sortProperty, propertyExpression, repeatWhenEqualToPrevRow);
	}

	/**
	 * Methode die aangeroepen wordt bij het klikken op de link.
	 */
	@SuppressWarnings("unused")
	protected void onClick(IModel<T> model)
	{
	}

	/**
	 * @see org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn#populateItem(org.apache.wicket.markup.repeater.Item,
	 *      java.lang.String, org.apache.wicket.model.IModel)
	 */
	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<T> rowModel, int span)
	{
		cell.add(new LinkPanel(componentId, rowModel));
	}

	private final class LinkPanel extends TypedPanel<T>
	{
		private static final long serialVersionUID = 1L;

		public LinkPanel(String id, IModel<T> rowModel)
		{
			super(id, rowModel);
			setRenderBodyOnly(true);
			Link<T> link = new Link<T>("link", rowModel)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isEnabled()
				{
					return super.isEnabled()
						&& ClickableCustomPropertyColumn.this.isContentsEnabled(getModel());
				}

				@Override
				public boolean isVisible()
				{
					return super.isVisible()
						&& ClickableCustomPropertyColumn.this.isContentsVisible(getModel());
				}

				@Override
				public void onClick()
				{
					ClickableCustomPropertyColumn.this.onClick(getModel());
				}
			};
			link.add(ComponentFactory.getDataLabel("label", createLabelModel(rowModel)));
			add(link);
		}
	}
}
