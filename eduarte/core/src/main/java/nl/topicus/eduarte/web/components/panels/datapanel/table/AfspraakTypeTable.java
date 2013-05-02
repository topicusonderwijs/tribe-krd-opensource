package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.participatie.AfspraakType;
import nl.topicus.eduarte.providers.OrganisatieEenheidProvider;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.OrganisatieEenheidInheritanceColumn;

public class AfspraakTypeTable extends CustomDataPanelContentDescription<AfspraakType>
{
	private static final long serialVersionUID = 1L;

	public AfspraakTypeTable(OrganisatieEenheidProvider orgEhdProvider)
	{
		super("Afspraaktypes");

		addColumn(new OrganisatieEenheidInheritanceColumn<AfspraakType>("image", orgEhdProvider));
		addColumn(new CustomPropertyColumn<AfspraakType>("Naam", "Naam", "naam", "naam"));
		addColumn(new CustomPropertyColumn<AfspraakType>("Omschrijving", "Omschrijving",
			"omschrijving", "omschrijving"));
		addColumn(new CustomPropertyColumn<AfspraakType>("Organisatie-eenheid",
			"Organisatie-eenheid", "organisatieEenheid", "organisatieEenheid.naam"));
		addColumn(new CustomPropertyColumn<AfspraakType>("Locatie", "Locatie", "locatie.naam",
			"locatie.naam"));
		addColumn(new BooleanPropertyColumn<AfspraakType>("Actief", "Actief", "actief", "actief"));
	}
}
