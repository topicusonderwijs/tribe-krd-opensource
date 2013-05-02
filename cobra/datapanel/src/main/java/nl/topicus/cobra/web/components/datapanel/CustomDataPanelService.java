package nl.topicus.cobra.web.components.datapanel;

import java.io.Serializable;
import java.util.List;

public interface CustomDataPanelService<T> extends Serializable
{
	public GroupProperty<T> getGroupProperty(CustomDataPanelId id,
			CustomDataPanelContentDescription<T> content);

	public void saveGroupProperty(CustomDataPanelId panelId, GroupProperty<T> property);

	public List<CustomColumn<T>> getColumns(CustomDataPanelId id,
			CustomDataPanelContentDescription<T> content);

	public void saveColumns(CustomDataPanelId id, CustomDataPanelContentDescription<T> content,
			List<CustomColumn<T>> columns);

	public void resetColumns(CustomDataPanelId id, CustomDataPanelContentDescription<T> content);
}
