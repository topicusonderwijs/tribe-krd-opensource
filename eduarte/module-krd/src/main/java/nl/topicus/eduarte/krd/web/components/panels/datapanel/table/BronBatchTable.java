package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.onderwijs.duo.bron.IBronBatch;

/**
 * Definieert de content voor het tonen van de BRON Batches in een {@link CustomDataPanel}
 * .
 */
public class BronBatchTable extends CustomDataPanelContentDescription<IBronBatch>
{
	private static final long serialVersionUID = 1L;

	public BronBatchTable()
	{
		super("Batches");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<IBronBatch>("Aanleverpunt", "Aanleverpunt",
			"aanleverPuntNummer"));
		addColumn(new CustomPropertyColumn<IBronBatch>("Onderwijssoort", "Onderwijssoort",
			"onderwijssoort"));
		addColumn(new CustomPropertyColumn<IBronBatch>("Nummer", "Nummer", "batchNummerAsString"));
		addColumn(new CustomPropertyColumn<IBronBatch>("DatumAanmaken", "Datum aanmaken",
			"createdAt"));
		// addColumn(new CustomPropertyColumn("Peildatum", "Peildatum",
		// "aantalUitschrijvingen"));
		addColumn(new CustomPropertyColumn<IBronBatch>("AantalMeldingen", "Meldingen",
			"aantalMeldingen"));
		addColumn(new CustomPropertyColumn<IBronBatch>("AantalMeldingenInBehandeling",
			"In behandeling", "aantalMeldingenInBehandeling"));

		addColumn(new CustomPropertyColumn<IBronBatch>("verantwoordelijkeAanlevering",
			"Verantwoordelijke", "verantwoordelijkeAanlevering"));

		addColumn(new CustomPropertyColumn<IBronBatch>("AantalTerugMeldingen", "Terug",
			"aantalTerugkoppelMeldingen").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<IBronBatch>("AantalToevoegingen", "Toevoegingen",
			"aantalToevoegingen").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<IBronBatch>("AantalAanpassingen", "Aanpassingen",
			"aantalAanpassingen").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<IBronBatch>("AantalVerwijderingen", "Verwijderingen",
			"aantalVerwijderingen").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<IBronBatch>("AantalUitschrijvingen", "Uitschrijvingen",
			"aantalUitschrijvingen").setDefaultVisible(false));
		// linkcolumn
		// addColumn(new CustomPropertyColumn("Downloaden", "Downloaden","Downloaden"));
		// addColumn(new CustomPropertyColumn("Details", "Details", "Details"));
	}
}
