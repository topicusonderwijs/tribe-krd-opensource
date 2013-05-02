package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.landelijk.Plaats;

public class PlaatsTable extends CustomDataPanelContentDescription<Plaats>
{
	private static final long serialVersionUID = 1L;

	public PlaatsTable()
	{
		super("Plaatsnamen");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<Plaats>("Naam", "Naam", "naam", "naam"));
		addColumn(new CustomPropertyColumn<Plaats>("Gemeente", "Gemeente", "gemeente.naam",
			"gemeente.naam"));
		addColumn(new CustomPropertyColumn<Plaats>("Provincie", "Provincie", "provincie.naam",
			"provincie.naam"));
	}
}