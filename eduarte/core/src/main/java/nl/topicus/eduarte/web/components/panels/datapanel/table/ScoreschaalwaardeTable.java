package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.resultaatstructuur.Scoreschaalwaarde;

public class ScoreschaalwaardeTable extends CustomDataPanelContentDescription<Scoreschaalwaarde>
{
	private static final long serialVersionUID = 1L;

	public ScoreschaalwaardeTable()
	{
		super("Score tabel");
		addColumn(new CustomPropertyColumn<Scoreschaalwaarde>("Schaalwaarde", "Schaalwaarde",
			"waarde.naam", "waarde.naam"));
		addColumn(new CustomPropertyColumn<Scoreschaalwaarde>("Vanaf score", "Vanaf score",
			"vanafScore", "vanafScore"));
		addColumn(new CustomPropertyColumn<Scoreschaalwaarde>("Tot score", "Tot score", "totScore",
			"totScore"));
	}
}
