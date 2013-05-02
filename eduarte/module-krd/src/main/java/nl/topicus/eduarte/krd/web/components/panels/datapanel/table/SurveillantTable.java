package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.krd.web.pages.deelnemer.examens.procesverbaal.Surveillant;

public class SurveillantTable extends CustomDataPanelContentDescription<Surveillant>
{
	private static final long serialVersionUID = 1L;

	public SurveillantTable()
	{
		super("Surveillanten");
		addColumn(new CustomPropertyColumn<Surveillant>("Achternaam", "Achternaam", "achternaam"));
		addColumn(new CustomPropertyColumn<Surveillant>("Voorletter(s)", "Voorletter(s)",
			"voorletters"));
	}
}
