package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.organisatie.SoortOrganisatieEenheid;

public class SoortOrganisatieEenheidTable extends
		CustomDataPanelContentDescription<SoortOrganisatieEenheid>
{
	private static final long serialVersionUID = 1L;

	public SoortOrganisatieEenheidTable()
	{
		super("Soort Organisatie-eenheden");
		addColumn(new CustomPropertyColumn<SoortOrganisatieEenheid>("code", "Code", "code", "code"));
		addColumn(new CustomPropertyColumn<SoortOrganisatieEenheid>("naam", "Naam", "naam", "naam"));
		addColumn(new BooleanPropertyColumn<SoortOrganisatieEenheid>("actief", "Actief", "actief",
			"actief"));
	}
}
