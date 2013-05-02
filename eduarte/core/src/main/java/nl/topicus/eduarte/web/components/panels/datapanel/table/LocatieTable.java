package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.DatePropertyColumn;
import nl.topicus.eduarte.entities.organisatie.Locatie;

public class LocatieTable extends CustomDataPanelContentDescription<Locatie>
{
	private static final long serialVersionUID = 1L;

	public LocatieTable()
	{
		super("Locaties");
		addColumn(new CustomPropertyColumn<Locatie>("Afkorting", "Afkorting", "afkorting",
			"afkorting"));
		addColumn(new CustomPropertyColumn<Locatie>("Naam", "Naam", "naam", "naam"));
		addColumn(new CustomPropertyColumn<Locatie>("Adres", "Adres",
			"fysiekAdres.adres.straatHuisnummer"));
		addColumn(new CustomPropertyColumn<Locatie>("Plaats", "Plaats", "fysiekAdres.adres.plaats"));

		addColumn(new DatePropertyColumn<Locatie>("Begindatum", "Begindatum", "begindatum",
			"begindatum").setDefaultVisible(false));
		addColumn(new DatePropertyColumn<Locatie>("Einddatum", "Einddatum", "einddatum",
			"einddatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Locatie>("Telefoonnummer", "Telefoonnummer",
			"eersteTelefoon.contactgegeven").setDefaultVisible(false));
	}
}
