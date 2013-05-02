package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.contract.SoortContract;

public class SoortContractTable extends CustomDataPanelContentDescription<SoortContract>
{
	private static final long serialVersionUID = 1L;

	public SoortContractTable()
	{
		super("Soort contracten");
		addColumn(new CustomPropertyColumn<SoortContract>("code", "Code", "code", "code"));
		addColumn(new CustomPropertyColumn<SoortContract>("naam", "Naam", "naam", "naam"));
		addColumn(new BooleanPropertyColumn<SoortContract>("inburgering", "Inburgering",
			"inburgering", "inburgering"));
		addColumn(new BooleanPropertyColumn<SoortContract>("actief", "Actief", "actief", "actief"));
	}
}
