package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.krd.entities.bron.cfi.BronCfiTerugmelding;

/**
 * @author vandekamp
 */
public class BronCfiTerugmeldingTable extends
		CustomDataPanelContentDescription<BronCfiTerugmelding>
{
	private static final long serialVersionUID = 1L;

	public BronCfiTerugmeldingTable()
	{
		super("CFI-terugmeldingen");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<BronCfiTerugmelding>("Bestandsnaam", "Bestandsnaam",
			"bestandsnaam", "bestandsnaam"));
		addColumn(new CustomPropertyColumn<BronCfiTerugmelding>("Status", "Status", "status",
			"status"));
		addColumn(new CustomPropertyColumn<BronCfiTerugmelding>("Sector", "Sector", "sector",
			"sector"));
		addColumn(new CustomPropertyColumn<BronCfiTerugmelding>("Peildatum", "Peildatum",
			"peildatum", "peildatum"));
		addColumn(new CustomPropertyColumn<BronCfiTerugmelding>("Aanmaakdatum", "Aanmaakdatum",
			"aanmaakdatum", "aanmaakdatum"));
		addColumn(new CustomPropertyColumn<BronCfiTerugmelding>("Inleesdatum", "Inleesdatum",
			"inleesdatum", "inleesdatum"));
		addColumn(new CustomPropertyColumn<BronCfiTerugmelding>("Ingelezen door", "Ingelezen door",
			"persoon.achternaam", "ingelezenDoor.persoon.volledigeNaam"));
		addColumn(new CustomPropertyColumn<BronCfiTerugmelding>("Aantal signalen",
			"Aantal signalen", "aantalSignalen", "aantalSignalen"));
		addColumn(new CustomPropertyColumn<BronCfiTerugmelding>("Aantal conflicten",
			"Aantal conflicten", "aantalConflicten", "aantalConflicten"));
		addColumn(new CustomPropertyColumn<BronCfiTerugmelding>("Controletotaal", "Controletotaal",
			"ControleTotaal", "ControleTotaal"));
	}

}
