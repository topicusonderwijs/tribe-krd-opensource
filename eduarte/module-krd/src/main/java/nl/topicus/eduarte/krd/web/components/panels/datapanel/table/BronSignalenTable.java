package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.IBronSignaal;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.columns.DeelnemernummerClickableColumn;

/**
 * Definieert de content voor het tonen van de BRON Batches in een {@link CustomDataPanel}
 */
public class BronSignalenTable extends CustomDataPanelContentDescription<IBronSignaal>
{
	private static final long serialVersionUID = 1L;

	public BronSignalenTable()
	{
		super("Signalen");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<IBronSignaal>("Ondsoort", "Ondsoort",
			"aanleverMelding.bronMeldingOnderdelenOmsch"));
		addColumn(new CustomPropertyColumn<IBronSignaal>("Aanlpt", "Aanlpt",
			"aanleverMelding.batch.aanleverPuntNummer"));
		addColumn(new CustomPropertyColumn<IBronSignaal>("Batch", "Batch",
			"aanleverMelding.batch.batchNummer"));
		addColumn(new CustomPropertyColumn<IBronSignaal>("Terugkoppelnr", "Terugkoppelnr",
			"terugkMelding.terugkoppelNummer"));
		addColumn(new DeelnemernummerClickableColumn("Deelnemernr", "Deelnemernr"));
		addColumn(new CustomPropertyColumn<IBronSignaal>("Naam", "Naam",
			"aanleverMelding.deelnemer.persoon.volledigeNaam"));
		addColumn(new CustomPropertyColumn<IBronSignaal>("Code", "Code", "signaalcode"));
		addColumn(new CustomPropertyColumn<IBronSignaal>("Omschrijving", "Omschrijving",
			"omschrijvingSignaal"));
		addColumn(new CustomPropertyColumn<IBronSignaal>("Soort", "Soort", "Ernst"));
		addColumn(new CustomPropertyColumn<IBronSignaal>("Organisatie", "Organisatie",
			"melding.aanlevermelding.verbintenis.organisatieEenheid").setDefaultVisible(false));
	}
}
