package nl.topicus.cobra.web.components.datapanel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.datapanel.CustomColumn.Positioning;
import nl.topicus.cobra.web.components.datapanel.GroupProperty.GroupPropertyList;

import org.apache.wicket.model.IDetachable;

public class CustomDataPanelContentDescription<T> implements Serializable, IDetachable
{
	private static final long serialVersionUID = 1L;

	private List<CustomColumn<T>> columns;

	private GroupPropertyList<T> groupProperties = new GroupPropertyList<T>(10);

	private GroupProperty<T> defaultGroupProperty;

	private String title;

	public CustomDataPanelContentDescription(String title)
	{
		columns = new ArrayList<CustomColumn<T>>();
		this.title = title;
	}

	public CustomDataPanelContentDescription(String title, GroupPropertyList<T> groupProperties,
			GroupProperty<T> defaultGroupProperty, List<CustomColumn<T>> columns)
	{
		this.groupProperties = groupProperties;
		this.defaultGroupProperty = defaultGroupProperty;
		this.columns = columns;
		this.title = title;
	}

	public CustomDataPanelContentDescription<T> addGroupProperty(GroupProperty<T> property)
	{
		groupProperties.add(property);
		return this;
	}

	public CustomDataPanelContentDescription<T> addColumn(CustomColumn<T> column)
	{
		columns.add(column);
		return this;
	}

	public CustomDataPanelContentDescription<T> setColumns(List<CustomColumn<T>> columns)
	{
		this.columns = columns;
		return this;
	}

	public List<CustomColumn<T>> getColumns()
	{
		return columns;
	}

	public CustomColumn<T> getColumn(String id)
	{
		for (CustomColumn<T> column : columns)
		{
			if (column.getId().equals(id))
			{
				return column;
			}
		}
		return null;
	}

	public List<CustomColumn<T>> getVisibleColumns()
	{
		List<CustomColumn<T>> ret = new ArrayList<CustomColumn<T>>();
		for (CustomColumn<T> curColumn : getColumns())
		{
			if (curColumn.isVisible())
				ret.add(curColumn);
		}
		return ret;
	}

	public List<CustomColumn<T>> getDefaultVisibleColumns()
	{
		List<CustomColumn<T>> ret = new ArrayList<CustomColumn<T>>();
		for (CustomColumn<T> curColumn : getColumns())
		{
			if (curColumn.isVisible() && curColumn.isDefaultVisible())
				ret.add(curColumn);
		}
		return ret;
	}

	public List<CustomColumn<T>> getCustomizableColumns()
	{
		return getColumnsByPositioning(Positioning.CUSTOMIZABLE);
	}

	public List<CustomColumn<T>> getColumnsByPositioning(Positioning positioning)
	{
		List<CustomColumn<T>> ret = new ArrayList<CustomColumn<T>>();
		for (CustomColumn<T> curColumn : getColumns())
		{
			if (curColumn.getPositioning().equals(positioning) && curColumn.isVisible())
				ret.add(curColumn);
		}
		return ret;
	}

	public List<CustomColumn<T>> getGroupColumnsByPositioning(Positioning positioning)
	{
		List<CustomColumn<T>> ret = new ArrayList<CustomColumn<T>>();
		for (CustomColumn<T> curColumn : getDefaultGroupProperty().getColumns())
		{
			if (curColumn.getPositioning().equals(positioning) && curColumn.isVisible())
				ret.add(curColumn);
		}
		return ret;
	}

	public List<CustomColumn<T>> getGroupColumns()
	{
		return getDefaultGroupProperty().getColumns();
	}

	public boolean isGroupable()
	{
		return getGroupProperties() != null && getGroupProperties().size() > 1;
	}

	public CustomDataPanelContentDescription<T> setGroupProperties(
			GroupPropertyList<T> groupProperties)
	{
		this.groupProperties = groupProperties;
		return this;
	}

	public GroupPropertyList<T> getGroupProperties()
	{
		return groupProperties;
	}

	public CustomDataPanelContentDescription<T> setDefaultGroupProperty(
			GroupProperty<T> defaultGroupProperty)
	{
		this.defaultGroupProperty = defaultGroupProperty;
		return this;
	}

	public GroupProperty<T> getDefaultGroupProperty()
	{
		if (defaultGroupProperty == null && groupProperties != null && !groupProperties.isEmpty())
		{
			return groupProperties.get((String) null);
		}
		return defaultGroupProperty;
	}

	public String getTitle()
	{
		return title;
	}

	public CustomDataPanelContentDescription<T> setTitle(String title)
	{
		this.title = title;
		return this;
	}

	@Override
	public void detach()
	{
		for (CustomColumn<T> column : columns)
			column.detach();
	}

	@SuppressWarnings("unchecked")
	public CustomColumn<T>[] getColumnsAsArray()
	{
		return columns.toArray(new CustomColumn[columns.size()]);
	}
}
