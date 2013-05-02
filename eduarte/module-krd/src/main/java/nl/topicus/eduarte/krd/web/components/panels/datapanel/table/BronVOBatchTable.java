package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.onderwijs.duo.bron.IBronBatch;

/**
 * Definieert de content voor het tonen van de BRON VO-batches in een
 * {@link CustomDataPanel}.
 */
public class BronVOBatchTable extends CustomDataPanelContentDescription<IBronBatch>
{
	private static final long serialVersionUID = 1L;

	public BronVOBatchTable()
	{
		super("Vo-batches");

		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<IBronBatch>("BatchNummer", "BatchNummer", "batchNummer"));
		addColumn(new CustomPropertyColumn<IBronBatch>("Aanleverpunt", "Aanleverpunt",
			"aanleverpunt"));
		addColumn(new CustomPropertyColumn<IBronBatch>("Toevoegingen", "Toevoegingen",
			"aantalToevoegingen"));
		addColumn(new CustomPropertyColumn<IBronBatch>("Aanpassingen", "Aanpassingen",
			"aantalAanpassingen"));
		addColumn(new CustomPropertyColumn<IBronBatch>("Uitschrijvingen", "Uitschrijvingen",
			"aantalUitschrijvingen"));
		addColumn(new CustomPropertyColumn<IBronBatch>("Verwijderingen", "Verwijderingen",
			"aantalVerwijderingen"));

		// linkcolumn
		// addColumn(new CustomPropertyColumn("Details", "Details", "Details"));
	}
}
