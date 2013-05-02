package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.participatie.DeelnemerMedewerkerGroep;

/**
 * Tabel met de mogelijke kolommen voor medewerkers.
 * 
 * @author loite
 */
public class DeelnemerMedewerkerGroepTable extends
		CustomDataPanelContentDescription<DeelnemerMedewerkerGroep>
{
	private static final long serialVersionUID = 1L;

	public DeelnemerMedewerkerGroepTable()
	{
		super("Deelnemer/Groep/Medewerker");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<DeelnemerMedewerkerGroep>("Code", "Code", "code", "code"));
		addColumn(new CustomPropertyColumn<DeelnemerMedewerkerGroep>("Omschrijving",
			"Omschrijving", "omschrijving", "omschrijving"));
		addColumn(new CustomPropertyColumn<DeelnemerMedewerkerGroep>("Type", "Type", "type", "type"));

	}
}
