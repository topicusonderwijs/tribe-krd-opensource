/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel.columns;

import nl.topicus.cobra.web.components.datapanel.CustomColumn;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Kolom waarvoor de gebruiker kan bepalen of deze getoond wordt of niet en waar. Hiervoor
 * wordt er een id toegekend aan de kolom, dit is niet het wicket component id en dient
 * uniek te zijn binnen de table waar hij gebruikt wordt.
 * 
 * @author marrink
 */
public abstract class AbstractCustomColumn<T> extends AbstractColumn<T> implements CustomColumn<T>
{

	private static final long serialVersionUID = 1L;

	private boolean visible = true;

	private boolean defaultVisible = true;

	private final boolean repeatWhenEqualToPrevRow;

	private Positioning positioning = Positioning.CUSTOMIZABLE;

	private String id;

	public AbstractCustomColumn(String id, String header, String sortProperty)
	{
		this(id, header, sortProperty, true);
	}

	public AbstractCustomColumn(String id, String header)
	{
		this(id, header, true);
	}

	public AbstractCustomColumn(String id, String header, boolean repeatWhenEqualToPrevRow)
	{
		this(id, header, null, repeatWhenEqualToPrevRow);
	}

	public AbstractCustomColumn(String id, String header, String sortProperty,
			boolean repeatWhenEqualToPrevRow)
	{
		super(new Model<String>(header), sortProperty);
		this.id = id;
		this.repeatWhenEqualToPrevRow = repeatWhenEqualToPrevRow;
	}

	@Override
	public boolean isDefaultVisible()
	{
		return defaultVisible;
	}

	public AbstractCustomColumn<T> setDefaultVisible(boolean visible)
	{
		this.defaultVisible = visible;
		return this;
	}

	@Override
	public String getId()
	{
		return id;
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
	public boolean isContentsVisible(IModel<T> rowModel)
	{
		return isVisible();
	}

	/**
	 * Check functie voor de inhoud van deze Column. Bedoelt voor o.a. checkboxes en
	 * selects welke disabled moeten zijn op basis van de waarde van het model.
	 * 
	 * @param rowModel
	 * @return default true.
	 */
	public boolean isContentsEnabled(IModel<T> rowModel)
	{
		return true;
	}

	public AbstractCustomColumn<T> setVisible(boolean visible)
	{
		this.visible = visible;
		return this;
	}

	public AbstractCustomColumn<T> setPositioning(Positioning positioning)
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
	public Component getHeader(Component cell, String componentId)
	{
		return getHeader(componentId);
	}

	@Override
	final public void populateItem(final Item<ICellPopulator<T>> cellItem,
			final String componentId, final IModel<T> rowModel)
	{
		populateItem(cellItem, componentId, null, rowModel, 0);
	}
}
