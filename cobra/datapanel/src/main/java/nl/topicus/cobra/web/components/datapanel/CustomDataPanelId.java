package nl.topicus.cobra.web.components.datapanel;

import java.io.Serializable;

public interface CustomDataPanelId extends Serializable
{
	public String generateId();

	public boolean isAccountSpecific();
}
