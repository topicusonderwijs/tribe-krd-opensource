package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronExamenMelding;

/**
 * Definieert de content voor het tonen van de BRON {@link IBronExamenMelding} in een
 * {@link CustomDataPanel} .
 */
public class BronExamenmeldingTable extends CustomDataPanelContentDescription<IBronExamenMelding>
{
	private static final long serialVersionUID = 1L;

	public BronExamenmeldingTable()
	{
		super("Examenmeldingen");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<IBronExamenMelding>("Nr", "Nr",
			"deelnemer.deelnemernummer"));
		addColumn(new CustomPropertyColumn<IBronExamenMelding>("Naam", "Naam",
			"deelnemer.persoon.volledigeNaam"));
		addColumn(new CustomPropertyColumn<IBronExamenMelding>("OplCode", "Opl.Code", "examenCode"));
		addColumn(new CustomPropertyColumn<IBronExamenMelding>("DatumUitslagExamen",
			"Datum uitslag examen", "datumUitslagExamen"));
		addColumn(new CustomPropertyColumn<IBronExamenMelding>("UitslagExamen", "Uitslag examen",
			"uitslagExamen"));
		addColumn(new CustomPropertyColumn<IBronExamenMelding>("Vakken", "Vakken", "aantalVakken"));
	}
}
