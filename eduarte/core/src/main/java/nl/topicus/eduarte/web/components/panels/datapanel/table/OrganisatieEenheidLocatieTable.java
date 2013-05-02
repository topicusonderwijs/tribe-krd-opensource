package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.DatePropertyColumn;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheidLocatie;

/**
 * Tabel met de mogelijke kolommen voor een OrganisatieEenheidLocatie.
 * 
 * @author hoeve
 */
public class OrganisatieEenheidLocatieTable extends
		CustomDataPanelContentDescription<OrganisatieEenheidLocatie>
{
	private static final long serialVersionUID = 1L;

	public OrganisatieEenheidLocatieTable()
	{
		super("Locaties");

		addColumn(new CustomPropertyColumn<OrganisatieEenheidLocatie>("Afkorting", "Afkorting",
			"locatie.afkorting", "locatie.afkorting"));
		addColumn(new CustomPropertyColumn<OrganisatieEenheidLocatie>("Naam", "Naam",
			"locatie.naam"));
		addColumn(new DatePropertyColumn<OrganisatieEenheidLocatie>("Begindatum", "Begindatum",
			"begindatum", "begindatum"));
		addColumn(new DatePropertyColumn<OrganisatieEenheidLocatie>("Einddatum", "Einddatum",
			"einddatum", "einddatum"));
	}

}
