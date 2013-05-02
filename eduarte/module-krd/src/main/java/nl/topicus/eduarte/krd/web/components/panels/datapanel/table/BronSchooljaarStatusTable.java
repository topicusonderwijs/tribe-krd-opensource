package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.krd.entities.bron.BronSchooljaarStatus;

/**
 * Definieert de content voor het tonen van de BRON statussen van een instelling in een
 * {@link CustomDataPanel}.
 */
public class BronSchooljaarStatusTable extends
		CustomDataPanelContentDescription<BronSchooljaarStatus>
{
	private static final long serialVersionUID = 1L;

	public BronSchooljaarStatusTable()
	{
		super("BRON Status");

		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<BronSchooljaarStatus>("Aanleverpunt", "Aanleverpunt",
			"aanleverpunt.nummer"));
		addColumn(new CustomPropertyColumn<BronSchooljaarStatus>("Schooljaar", "Schooljaar",
			"schooljaar"));
		addColumn(new CustomPropertyColumn<BronSchooljaarStatus>("Statusbo", "Status BO",
			"statusBO"));
		addColumn(new CustomPropertyColumn<BronSchooljaarStatus>("Statused", "Status ED",
			"statusED"));
		addColumn(new CustomPropertyColumn<BronSchooljaarStatus>("Statusvavo", "Status VAVO",
			"statusVAVO"));
		addColumn(new CustomPropertyColumn<BronSchooljaarStatus>("Statusvo", "Status VO",
			"statusVO"));
	}
}
