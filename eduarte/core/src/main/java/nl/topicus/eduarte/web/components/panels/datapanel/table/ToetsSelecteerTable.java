package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.AjaxRadioColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

public class ToetsSelecteerTable extends CustomDataPanelContentDescription<Toets>
{
	private static final long serialVersionUID = 1L;

	public ToetsSelecteerTable()
	{
		super("Toetsen");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new AjaxRadioColumn<Toets>("select", ""));
		addColumn(new CustomPropertyColumn<Toets>("Naam", "Naam", "naam", "naam"));
	}
}
