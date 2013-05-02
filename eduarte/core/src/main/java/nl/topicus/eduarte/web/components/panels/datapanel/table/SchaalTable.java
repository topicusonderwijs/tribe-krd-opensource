package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal;

public class SchaalTable extends CustomDataPanelContentDescription<Schaal>
{
	private static final long serialVersionUID = 1L;

	public SchaalTable()
	{
		super("Schalen");
		createColumns();
		createGroupProperties();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<Schaal>("Naam", "Naam", "naam", "naam"));
		addColumn(new CustomPropertyColumn<Schaal>("Type", "Schaaltype", "schaaltype", "schaaltype"));
		addColumn(new CustomPropertyColumn<Schaal>("Minimum voor behaald", "Behaald bij",
			"minimumVoorBehaald", "minimumVoorBehaald").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Schaal>("Aantal decimalen", "Decimalen",
			"aantalDecimalen", "aantalDecimalen"));
		addColumn(new CustomPropertyColumn<Schaal>("Minimum", "Minimum", "minimum", "minimum"));
		addColumn(new CustomPropertyColumn<Schaal>("Maximum", "Maximum", "maximum", "maximum"));
		addColumn(new BooleanPropertyColumn<Schaal>("Actief", "Actief", "actief", "actief"));
	}

	private void createGroupProperties()
	{
		addGroupProperty(getDefaultGroupProperty());
	}

	@Override
	public GroupProperty<Schaal> getDefaultGroupProperty()
	{
		return new GroupProperty<Schaal>("schaaltype", "Schaaltype", "schaaltype");
	}
}
