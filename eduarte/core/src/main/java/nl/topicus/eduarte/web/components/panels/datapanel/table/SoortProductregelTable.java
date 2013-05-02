package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.productregel.SoortProductregel;

/**
 * @author vandekamp
 */
public class SoortProductregelTable extends CustomDataPanelContentDescription<SoortProductregel>
{
	private static final long serialVersionUID = 1L;

	public SoortProductregelTable()
	{
		super("Soort productregels");
		addColumn(new CustomPropertyColumn<SoortProductregel>("Volgnummer", "Volgnummer",
			"volgnummer", "volgnummer"));
		addColumn(new CustomPropertyColumn<SoortProductregel>("Naam", "Naam", "naam", "naam"));
		addColumn(new CustomPropertyColumn<SoortProductregel>("Diplomanaam", "Diplomanaam",
			"diplomanaam", "diplomanaam"));
		addColumn(new BooleanPropertyColumn<SoortProductregel>("actief", "Actief", "actief",
			"actief"));
		addColumn(new CustomPropertyColumn<SoortProductregel>("Taxonomie", "Taxonomie",
			"taxonomie.naam", "taxonomie.naam"));
		addColumn(new BooleanPropertyColumn<SoortProductregel>("Landelijk", "Landelijk",
			"landelijk").setDefaultVisible(false));
	}
}
