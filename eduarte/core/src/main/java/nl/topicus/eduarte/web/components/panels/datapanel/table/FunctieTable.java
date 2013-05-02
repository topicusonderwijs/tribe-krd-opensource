package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.personen.Functie;

public class FunctieTable extends CustomDataPanelContentDescription<Functie>
{
	private static final long serialVersionUID = 1L;

	public FunctieTable()
	{
		super("Functies");
		addColumn(new CustomPropertyColumn<Functie>("naam", "Naam", "naam", "naam"));
	}
}
