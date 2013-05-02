package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;

public class SoortContactgegevenTable extends
		CustomDataPanelContentDescription<SoortContactgegeven>
{
	private static final long serialVersionUID = 1L;

	public SoortContactgegevenTable()
	{
		super("Soort contactgegevens");
		addColumn(new CustomPropertyColumn<SoortContactgegeven>("Code", "Code", "code", "code"));
		addColumn(new CustomPropertyColumn<SoortContactgegeven>("Naam", "Naam", "naam", "naam"));
		addColumn(new CustomPropertyColumn<SoortContactgegeven>("Type contactgegeven", "Type",
			"typeContactgegeven", "typeContactgegeven"));
		addColumn(new BooleanPropertyColumn<SoortContactgegeven>("Actief", "Actief", "actief",
			"actief"));
		addColumn(new CustomPropertyColumn<SoortContactgegeven>("Standaard tonen",
			"Standaard tonen", "standaardContactgegeven", "standaardContactgegeven"));
	}
}
