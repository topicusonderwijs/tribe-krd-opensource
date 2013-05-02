package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;

public class OrganisatieEenheidTable extends CustomDataPanelContentDescription<OrganisatieEenheid>
{
	private static final long serialVersionUID = 1L;

	public OrganisatieEenheidTable()
	{
		super("Organisatie-eenheden");
		addColumn(new CustomPropertyColumn<OrganisatieEenheid>("Afkorting", "Afkorting",
			"afkorting", "afkorting"));
		addColumn(new CustomPropertyColumn<OrganisatieEenheid>("Naam", "Naam", "naam", "naam"));
		addColumn(new CustomPropertyColumn<OrganisatieEenheid>("Officiele naam", "Officiele naam",
			"officieleNaam", "officieleNaam"));
		addColumn(new CustomPropertyColumn<OrganisatieEenheid>("Begindatum", "Begindatum",
			"begindatum", "begindatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OrganisatieEenheid>("Einddatum", "Einddatum",
			"einddatum", "einddatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OrganisatieEenheid>("Brincode", "Brincode",
			"organisatie.brincode", "organisatie.brincode").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OrganisatieEenheid>("Overkoepelende eenheid",
			"Overkoepelende eenheid", "parent", "parent"));
		addColumn(new CustomPropertyColumn<OrganisatieEenheid>("Soort organisatie-eenheid",
			"Soort", "soortOrganisatieEenheid", "soortOrganisatieEenheid.naam"));
		addColumn(new CustomPropertyColumn<OrganisatieEenheid>("Locaties", "Locaties",
			"locatieNamen"));

		addColumn(new CustomPropertyColumn<OrganisatieEenheid>("Straat en huisnummer", "Adres",
			"fysiekAdres.adres.straatHuisnummer").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OrganisatieEenheid>("Plaats", "Plaats",
			"fysiekAdres.adres.plaats").setDefaultVisible(false));
	}
}
