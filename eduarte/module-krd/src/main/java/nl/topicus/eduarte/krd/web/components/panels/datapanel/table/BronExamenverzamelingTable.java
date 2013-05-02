package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.krd.entities.bron.BronExamenverzameling;

/**
 * Definieert de content voor het tonen van de BRON Examenverzamelingen in een
 * {@link CustomDataPanel} .
 */
public class BronExamenverzamelingTable extends
		CustomDataPanelContentDescription<BronExamenverzameling>
{
	private static final long serialVersionUID = 1L;

	public BronExamenverzamelingTable()
	{
		super("Examenverzamelingen");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<BronExamenverzameling>("Aanleverpunt", "Aanleverpunt",
			"aanleverpunt.nummer"));
		addColumn(new CustomPropertyColumn<BronExamenverzameling>("TypeOnd", "TypeOnd",
			"bronOnderwijssoort"));
		addColumn(new CustomPropertyColumn<BronExamenverzameling>("Datum", "Datum", "createdAt"));
		addColumn(new CustomPropertyColumn<BronExamenverzameling>("Onderwijssoort",
			"Onderwijssoort", "soortOnderwijs"));
		addColumn(new CustomPropertyColumn<BronExamenverzameling>("Aantal", "Aantal",
			"aantalMeldingen"));
		addColumn(new CustomPropertyColumn<BronExamenverzameling>("Batch", "Batch", "batchOmschr"));
	}
}
