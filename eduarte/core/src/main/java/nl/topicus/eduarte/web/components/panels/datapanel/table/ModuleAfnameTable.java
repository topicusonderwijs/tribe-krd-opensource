package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.organisatie.ModuleAfname;

public class ModuleAfnameTable extends CustomDataPanelContentDescription<ModuleAfname>
{
	private static final long serialVersionUID = 1L;

	public ModuleAfnameTable()
	{
		super("Afgenomen modules");

		addColumn(new CustomPropertyColumn<ModuleAfname>("Module", "Module", "moduleName",
			"moduleName"));
		addColumn(new CustomPropertyColumn<ModuleAfname>("Instelling", "Instelling",
			"organisatie.naam", "organisatie.naam"));
		addColumn(new BooleanPropertyColumn<ModuleAfname>("Actief", "Actief", "actief", "actief"));
		addColumn(new CustomPropertyColumn<ModuleAfname>("Checksum", "Checksum", "checksum",
			"checksum"));
		addColumn(new BooleanPropertyColumn<ModuleAfname>("Geldig", "Geldig", "isChecksumValid"));

		GroupProperty<ModuleAfname> grouping =
			new GroupProperty<ModuleAfname>("organisatie.naam", "Instelling", "organisatie.naam");
		addGroupProperty(grouping);
		setDefaultGroupProperty(grouping);
	}
}
