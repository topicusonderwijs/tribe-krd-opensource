/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel.columns;

import java.util.Collections;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Kolom die een property van het object uit het model toont.
 * 
 * @author marrink
 */
public class CustomPropertyColumn<T> extends PropertyColumn<T> implements IFilteredColumn<T>
{
	private static final long serialVersionUID = 1L;

	private String id;

	private boolean defaultVisible = true;

	private boolean visible = true;

	private boolean escapeModelStrings = true;

	private final boolean repeatWhenEqualToPrevRow;

	private Positioning positioning = Positioning.CUSTOMIZABLE;

	private List<String> filterProperties = Collections.emptyList();

	public CustomPropertyColumn(String id, String header, String propertyExpression)
	{
		this(id, header, propertyExpression, true);
	}

	public static <T> CustomPropertyColumn<T> create(String id, String header,
			String propertyExpression)
	{
		return new CustomPropertyColumn<T>(id, header, propertyExpression);
	}

	public CustomPropertyColumn(String id, String header, String propertyExpression,
			boolean repeatWhenEqualToPrevRow)
	{
		this(id, header, null, propertyExpression, repeatWhenEqualToPrevRow);
	}

	public static <T> CustomPropertyColumn<T> create(String id, String header,
			String propertyExpression, boolean repeatWhenEqualToPrevRow)
	{
		return new CustomPropertyColumn<T>(id, header, propertyExpression, repeatWhenEqualToPrevRow);
	}

	public CustomPropertyColumn(String id, String header, String sortProperty,
			String propertyExpression)
	{
		this(id, header, sortProperty, propertyExpression, true);
	}

	public static <T> CustomPropertyColumn<T> create(String id, String header, String sortProperty,
			String propertyExpression)
	{
		return new CustomPropertyColumn<T>(id, header, sortProperty, propertyExpression);
	}

	public CustomPropertyColumn(String id, String header, String sortProperty,
			String propertyExpression, boolean repeatWhenEqualToPrevRow)
	{
		super(new Model<String>(header), sortProperty, propertyExpression);
		this.id = id;
		this.repeatWhenEqualToPrevRow = repeatWhenEqualToPrevRow;
	}

	public static <T> CustomPropertyColumn<T> create(String id, String header, String sortProperty,
			String propertyExpression, boolean repeatWhenEqualToPrevRow)
	{
		return new CustomPropertyColumn<T>(id, header, sortProperty, propertyExpression,
			repeatWhenEqualToPrevRow);
	}

	/**
	 * @see nl.topicus.cobra.web.components.datapanel.CustomColumn#getId()
	 */
	@Override
	public String getId()
	{
		return id;
	}

	/**
	 * @see nl.topicus.cobra.web.components.datapanel.CustomColumn#isDefaultVisible()
	 */
	@Override
	public boolean isDefaultVisible()
	{
		return defaultVisible;
	}

	/**
	 * @param visible
	 * @return this
	 * @see nl.topicus.cobra.web.components.datapanel.CustomColumn#isDefaultVisible()
	 */
	public CustomPropertyColumn<T> setDefaultVisible(boolean visible)
	{
		this.defaultVisible = visible;
		return this;
	}

	/**
	 * @see org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn#populateItem(org.apache.wicket.markup.repeater.Item,
	 *      java.lang.String, org.apache.wicket.model.IModel)
	 */
	@Override
	final public void populateItem(Item<ICellPopulator<T>> item, String componentId, IModel<T> model)
	{
		populateItem(item, componentId, null, model, 0);
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
				return super.isEnabled() && CustomPropertyColumn.this.isContentsEnabled(rowModel);
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && CustomPropertyColumn.this.isContentsVisible(rowModel);
			}
		}.setEscapeModelStrings(escapeModelStrings).setRenderBodyOnly(true));
	}

	@Override
	public Component getHeader(Component cell, String componentId)
	{
		return getHeader(componentId);
	}

	public boolean isRepeatWhenEqualToPrevRow()
	{
		return repeatWhenEqualToPrevRow;
	}

	@Override
	public boolean isVisible()
	{
		return visible;
	}

	/**
	 * Check functie voor de inhoud van deze Column. Bedoelt voor o.a. buttons en links
	 * welke niet getoond mogen worden op basis van de waarde van het model.
	 * 
	 * @param rowModel
	 * @return default {@link #isVisible()}.
	 */
	@Override
	public boolean isContentsVisible(IModel<T> rowModel)
	{
		return isVisible();
	}

	/**
	 * Check functie voor de inhoud van deze Column. Bedoelt voor o.a. checkboxes en
	 * selects welke disabled moeten zijn op basis van de waarde van het model.
	 * 
	 * @param rowModel
	 * @return default {@link #isVisible()}.
	 */
	@Override
	public boolean isContentsEnabled(IModel<T> rowModel)
	{
		return true;
	}

	public CustomPropertyColumn<T> setVisible(boolean visible)
	{
		this.visible = visible;
		return this;
	}

	public CustomPropertyColumn<T> setPositioning(Positioning positioning)
	{
		this.positioning = positioning;
		return this;
	}

	@Override
	public Positioning getPositioning()
	{
		return positioning;
	}

	@Override
	public List<String> getFilterProperties()
	{
		return filterProperties;
	}

	public CustomPropertyColumn<T> setFilterProperties(List<String> filterProperties)
	{
		this.filterProperties = filterProperties;
		return this;
	}

	public CustomPropertyColumn<T> setEscapeModelStrings(boolean escapeModelStrings)
	{
		this.escapeModelStrings = escapeModelStrings;
		return this;
	}
}
