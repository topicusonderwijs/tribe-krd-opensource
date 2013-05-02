package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.contract.TypeFinanciering;

public class TypeFinancieringTable extends CustomDataPanelContentDescription<TypeFinanciering>
{
	private static final long serialVersionUID = 1L;

	public TypeFinancieringTable()
	{
		super("Soort financieringen");
		addColumn(new CustomPropertyColumn<TypeFinanciering>("code", "Code", "code", "code"));
		addColumn(new CustomPropertyColumn<TypeFinanciering>("naam", "Naam", "naam", "naam"));
		addColumn(new BooleanPropertyColumn<TypeFinanciering>("actief", "Actief", "actief",
			"actief"));
	}
}
