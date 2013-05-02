package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;

public class NaamActiefTable<T> extends CustomDataPanelContentDescription<T>
{
	private static final long serialVersionUID = 1L;

	public NaamActiefTable(String title)
	{
		super(title);
		addColumn(new CustomPropertyColumn<T>("Naam", "Naam", "naam", "naam"));
		addColumn(new BooleanPropertyColumn<T>("Actief", "Actief", "actief", "actief"));
	}
}
