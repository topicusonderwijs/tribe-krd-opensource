package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.inschrijving.UitkomstIntakegesprek;

public class UitkomstIntakegesprekTable extends
		CustomDataPanelContentDescription<UitkomstIntakegesprek>
{
	private static final long serialVersionUID = 1L;

	public UitkomstIntakegesprekTable()
	{
		super("Uitkomst intakegesprek");
		addColumn(new CustomPropertyColumn<UitkomstIntakegesprek>("code", "Code", "code", "code"));
		addColumn(new CustomPropertyColumn<UitkomstIntakegesprek>("naam", "Naam", "naam", "naam"));
		addColumn(new BooleanPropertyColumn<UitkomstIntakegesprek>("succesvol", "Succesvol",
			"succesvol", "succesvol"));
		addColumn(new BooleanPropertyColumn<UitkomstIntakegesprek>("actief", "Actief", "actief",
			"actief"));
	}
}
