package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.DatePropertyColumn;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;

public class OrganisatieLocatieTable<T extends IOrganisatieEenheidLocatieKoppelEntiteit< ? >>
		extends CustomDataPanelContentDescription<T>
{
	private static final long serialVersionUID = 1L;

	public OrganisatieLocatieTable(boolean beginEindDatumTonen)
	{
		this(beginEindDatumTonen, false);
	}

	public OrganisatieLocatieTable(boolean beginEindDatumTonen, boolean teamTonen)
	{
		super("Locaties");
		createColumns(beginEindDatumTonen, teamTonen);
	}

	private void createColumns(boolean beginEindDatumTonen, boolean teamTonen)
	{
		addColumn(new CustomPropertyColumn<T>("Organisatie-eenheid", "Organisatie-eenheid",
			"organisatieEenheid", "organisatieEenheid"));
		addColumn(new CustomPropertyColumn<T>("Locatie", "Locatie", "locatie", "locatie"));
		if (beginEindDatumTonen)
		{
			addColumn(new DatePropertyColumn<T>("Begindatum", "Begindatum", "begindatum"));
			addColumn(new DatePropertyColumn<T>("Einddatum", "Einddatum", "einddatum"));
		}
		if (teamTonen)
		{
			addColumn(new CustomPropertyColumn<T>("Team", "Team", "team.naam"));
		}
	}
}
