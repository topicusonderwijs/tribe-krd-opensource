package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;

public class CodeNaamTable<T> extends CustomDataPanelContentDescription<T>
{
	private static final long serialVersionUID = 1L;

	public CodeNaamTable(String title)
	{
		super(title);
		addColumn(new CustomPropertyColumn<T>("Code", "Code", "code", "code"));
		addColumn(new CustomPropertyColumn<T>("Naam", "Naam", "naam", "naam"));
	}
}
