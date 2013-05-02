package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CollectionPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.resultaatstructuur.StandaardToetsCodeFilter;

public class StandaardToetsCodeFilterTable extends
		CustomDataPanelContentDescription<StandaardToetsCodeFilter>
{
	private static final long serialVersionUID = 1L;

	public StandaardToetsCodeFilterTable()
	{
		super("Toetsfilters)");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<StandaardToetsCodeFilter>("Cohort", "Cohort", "cohort",
			"cohort"));
		addColumn(new CustomPropertyColumn<StandaardToetsCodeFilter>("Naam", "Naam",
			"toetsCodeFilter.naam", "toetsCodeFilter.naam"));
		addColumn(new CollectionPropertyColumn<StandaardToetsCodeFilter>("Codes", "Codes",
			"toetsCodeFilter.toetsCodesAsSet", 5));
	}
}
