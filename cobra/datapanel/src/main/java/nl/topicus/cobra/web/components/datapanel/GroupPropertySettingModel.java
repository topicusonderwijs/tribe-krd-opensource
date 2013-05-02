package nl.topicus.cobra.web.components.datapanel;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.model.IModel;

/**
 * Model voor het vasthouden van de geselecteerde groepering.
 * 
 * @author loite
 */
public final class GroupPropertySettingModel<T> implements IModel<GroupProperty<T>>
{
	private static final long serialVersionUID = 1L;

	private final CustomDataPanel<T> panel;

	private final CustomDataPanelService<T> service;

	private GroupProperty<T> setting = null;

	public GroupPropertySettingModel(CustomDataPanel<T> panel, CustomDataPanelService<T> service)
	{
		super();
		this.panel = panel;
		this.service = service;
	}

	@Override
	public GroupProperty<T> getObject()
	{
		if (setting == null)
			setting = service.getGroupProperty(panel.getPanelId(), panel.getContentDescription());
		return setting;
	}

	/**
	 * Geeft aan dat de sortstate gewijzigd is.
	 * 
	 * @param setting
	 */
	@Override
	public void setObject(GroupProperty<T> setting)
	{
		GroupProperty<T> prop = setting;
		service.saveGroupProperty(panel.getPanelId(), prop);
		if (setting != null)
		{
			ISortStateLocator locator = panel.getSortStateLocator();
			if (prop.getSorteerProperty() != null && locator != null)
			{
				ISortState state = locator.getSortState();
				int volgorde = state.getPropertySortOrder(prop.getSorteerProperty());
				if (volgorde == ISortState.NONE)
					volgorde = prop.isAscending() ? ISortState.ASCENDING : ISortState.DESCENDING;
				state.setPropertySortOrder(prop.getSorteerProperty(), volgorde);
			}
		}
		this.setting = prop;
	}

	@Override
	public void detach()
	{
	}
}