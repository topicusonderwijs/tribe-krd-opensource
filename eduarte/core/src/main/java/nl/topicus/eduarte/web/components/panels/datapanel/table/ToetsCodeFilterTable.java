package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CollectionPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.resultaatstructuur.ToetsCodeFilter;

public class ToetsCodeFilterTable extends CustomDataPanelContentDescription<ToetsCodeFilter>
{
	private static final long serialVersionUID = 1L;

	public ToetsCodeFilterTable()
	{
		super("Toetsfilters");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<ToetsCodeFilter>("Naam", "Naam", "naam", "naam"));
		addColumn(new CollectionPropertyColumn<ToetsCodeFilter>("Codes", "Codes",
			"toetsCodesAsSet", 5));
	}
}
