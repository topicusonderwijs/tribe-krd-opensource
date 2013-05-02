package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.personen.RedenUitDienst;

public class RedenUitDienstTable extends CustomDataPanelContentDescription<RedenUitDienst>
{
	private static final long serialVersionUID = 1L;

	public RedenUitDienstTable()
	{
		super("Reden uit dienst");
		addColumn(new CustomPropertyColumn<RedenUitDienst>("code", "Code", "code", "code"));
		addColumn(new CustomPropertyColumn<RedenUitDienst>("naam", "Naam", "naam", "naam"));
	}
}
