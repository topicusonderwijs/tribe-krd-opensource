package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;

/**
 * Definieert de content voor het tonen van de BRON Batches in een {@link CustomDataPanel}
 * .
 */
public class BronMeldingTable extends CustomDataPanelContentDescription<IBronMelding>
{
	private static final long serialVersionUID = 1L;

	public BronMeldingTable()
	{
		super("Meldingen");

		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<IBronMelding>("Batch", "Batch",
			"batch.batchNummerAsString", "batch.batchNummerAsString"));
		addColumn(new CustomPropertyColumn<IBronMelding>("Aanlpt", "Aanlpt",
			"batch.aanleverPuntNummer", "batch.aanleverPuntNummer"));
		addColumn(new CustomPropertyColumn<IBronMelding>("Onderwijs", "Onderwijs",
			"onderwijssoort", "onderwijssoort"));
		// addColumn(new CustomPropertyColumn("Terugkoppel", "Terugkoppel",
		// "batch.terugkoppel"));
		addColumn(new CustomPropertyColumn<IBronMelding>("DatumAanmaken", "Datum aanmaken",
			"createdAt", "createdAt"));
		addColumn(new CustomPropertyColumn<IBronMelding>("SoortMutatie", "Mutatie",
			"soortMutatiesOmsch", "soortMutatiesOmsch"));
		addColumn(new CustomPropertyColumn<IBronMelding>("StatusMelding", "Status",
			"bronMeldingStatus", "bronMeldingStatus"));
		addColumn(new CustomPropertyColumn<IBronMelding>("Naam", "Naam",
			"deelnemer.persoon.volledigeNaam", "deelnemer.persoon.volledigeNaam"));
		addColumn(new CustomPropertyColumn<IBronMelding>("Onderdeel", "Onderdeel(Soort)",
			"bronMeldingOnderdelenOmsch", "bronMeldingOnderdelenOmsch"));
		addColumn(new BooleanPropertyColumn<IBronMelding>("Geblokkeerd", "Geblokkeerd",
			"geblokkeerd", "geblokkeerd"));
		addColumn(new CustomPropertyColumn<IBronMelding>("Organisatie", "Organisatie",
			"verbintenis.organisatieEenheid", "verbintenis.organisatieEenheid")
			.setDefaultVisible(false));

		// addColumn(new CustomPropertyColumn("Ingangsdatum", "Ingangsdatum",
		// "createdAt"));
		// addColumn(new CustomPropertyColumn("Deelnemer",
		// "Deelnemer","deelnemer.deelnemernummer"));
		// addColumn(new CustomPropertyColumn("Naam", "Naam",
		// "deelnemer.persoon.volledigeNaam"));

	}
}
