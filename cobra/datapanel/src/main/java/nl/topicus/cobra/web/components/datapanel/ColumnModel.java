/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.datapanel.CustomColumn.Positioning;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;

public class ColumnModel<T> extends AbstractReadOnlyModel<List<CustomColumn<T>>>
{
	protected final class CustomizableColumnModel<Y extends CustomColumn<T>> extends ListModel<Y>
	{
		private static final long serialVersionUID = 1L;

		// we need lazy init, because panelid might not be available before render
		private boolean initialized = false;

		@SuppressWarnings("unchecked")
		@Override
		public List<Y> getObject()
		{
			if (!initialized)
				super.setObject((List<Y>) new ArrayList<CustomColumn<T>>(getInitialColumns()));
			initialized = true;
			return super.getObject();
		}

		@Override
		public void setObject(List<Y> object)
		{
			super.setObject(object);
			saveColumns();
		}

		public void reset()
		{
			initialized = false;
		}
	}

	private static final long serialVersionUID = 1L;

	private CustomDataPanelIdSource panelIdSource;

	private CustomDataPanelContentDescription<T> content;

	private CustomDataPanelService<T> service;

	protected CustomizableColumnModel<CustomColumn<T>> columnModel;

	public ColumnModel(CustomDataPanel<T> panel, CustomDataPanelService<T> service)
	{
		this(panel, panel.getContentDescription(), service);
	}

	public ColumnModel(CustomDataPanelIdSource panelIdSource,
			CustomDataPanelContentDescription<T> content, CustomDataPanelService<T> service)
	{
		super();
		this.panelIdSource = panelIdSource;
		this.content = content;
		this.service = service;
		columnModel = new CustomizableColumnModel<CustomColumn<T>>();
	}

	public void resetToDefaults()
	{
		resetColumns();
		columnModel.reset();
	}

	protected CustomDataPanelService<T> getService()
	{
		return service;
	}

	private CustomDataPanelId getPanelId()
	{
		return panelIdSource.getPanelId();
	}

	protected CustomDataPanelContentDescription<T> getContent()
	{
		return content;
	}

	@Override
	public List<CustomColumn<T>> getObject()
	{
		List<CustomColumn<T>> ret = new ArrayList<CustomColumn<T>>();
		ret.addAll(getContent().getColumnsByPositioning(Positioning.FIXED_LEFT));
		for (CustomColumn<T> curColumn : columnModel.getObject())
			if (curColumn.isVisible())
				ret.add(curColumn);
		ret.addAll(getContent().getColumnsByPositioning(Positioning.FIXED_RIGHT));
		return ret;
	}

	public IModel<List<CustomColumn<T>>> getCustomizableColumnsModel()
	{
		return columnModel;
	}

	public List<CustomColumn<T>> getInitialColumns()
	{
		return getService().getColumns(getPanelId(), getContent());
	}

	public void saveColumns()
	{
		if (getPanelId() != null)
			getService().saveColumns(getPanelId(), getContent(), columnModel.getObject());
	}

	public void resetColumns()
	{
		if (getPanelId() != null)
			getService().resetColumns(getPanelId(), getContent());
	}
}