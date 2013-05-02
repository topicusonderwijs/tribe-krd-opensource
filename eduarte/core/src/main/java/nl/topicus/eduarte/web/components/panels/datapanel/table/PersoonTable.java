package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.personen.Persoon;

/**
 * Tabel met de mogelijke kolommen voor personen.
 * 
 * @author vandekamp
 */
public class PersoonTable extends CustomDataPanelContentDescription<Persoon>
{
	private static final long serialVersionUID = 1L;

	public PersoonTable()
	{
		super("Personen");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<Persoon>("Roepnaam", "Roepnaam", "roepnaam", "roepnaam"));
		addColumn(new CustomPropertyColumn<Persoon>("Voornamen", "Voornamen", "voornamen",
			"voornamen"));
		addColumn(new CustomPropertyColumn<Persoon>("Voorvoegsel", "Voorvoegsel", "voorvoegsel",
			"voorvoegsel"));
		addColumn(new CustomPropertyColumn<Persoon>("Achternaam", "Achternaam", "achternaam",
			"achternaam"));
	}
}
