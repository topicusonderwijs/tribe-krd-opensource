package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.IBronTerugkoppeling;

/**
 * Definieert de content voor het tonen van de BRON Terugkoppelingen in een
 * {@link CustomDataPanel} .
 */
public class BronTerugkoppelingTable extends CustomDataPanelContentDescription<IBronTerugkoppeling>
{
	private static final long serialVersionUID = 1L;

	public BronTerugkoppelingTable()
	{
		super("Terugkoppelingen");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<IBronTerugkoppeling>("Aanleverpunt", "Aanleverpunt",
			"aanleverpuntNummer", "aanleverpuntNummer"));
		addColumn(new CustomPropertyColumn<IBronTerugkoppeling>("Onderwijssoort", "Onderwijssoort",
			"bronOnderwijssoort", "bronOnderwijssoort"));
		addColumn(new CustomPropertyColumn<IBronTerugkoppeling>("Nummer", "Nummer",
			"bRONBatchNummerAsString", "bRONBatchNummerAsString"));
		addColumn(new CustomPropertyColumn<IBronTerugkoppeling>("DatumTerugkoppeling",
			"Datum terugkoppeling", "datumTerugkoppeling", "datumTerugkoppeling"));
		addColumn(new CustomPropertyColumn<IBronTerugkoppeling>("DatumInlezen", "Datum inlezen",
			"createdAt", "createdAt"));
		addColumn(new CustomPropertyColumn<IBronTerugkoppeling>("BatchesInBestand",
			"Batches in bestand", "batchesInBestand", "batchesInBestand"));
		addColumn(new CustomPropertyColumn<IBronTerugkoppeling>("AantalTeruggekoppeldeMeldingen",
			"Aantal teruggekoppelde meldingen", "aantalMeldingen", "aantalMeldingen"));
		addColumn(new CustomPropertyColumn<IBronTerugkoppeling>("AantalSignalen",
			"Aantal signalen", "aantalSignalen", "aantalSignalen"));
	}
}
