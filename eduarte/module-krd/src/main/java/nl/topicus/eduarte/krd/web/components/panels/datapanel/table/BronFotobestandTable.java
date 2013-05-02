package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestand;

/**
 * 
 * 
 * @author loite
 */
public class BronFotobestandTable extends CustomDataPanelContentDescription<BronFotobestand>
{
	private static final long serialVersionUID = 1L;

	public BronFotobestandTable()
	{
		super("Fotobestanden");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<BronFotobestand>("Bestandsnaam", "Bestandsnaam",
			"bestandsnaam", "bestandsnaam"));
		addColumn(new CustomPropertyColumn<BronFotobestand>("Status", "Status", "status", "status"));
		addColumn(new CustomPropertyColumn<BronFotobestand>("Verwerkingsstatus",
			"Verwerkingsstatus", "verwerkingsstatus", "verwerkingsstatus"));
		addColumn(new CustomPropertyColumn<BronFotobestand>("Type", "Type", "type", "type"));
		addColumn(new CustomPropertyColumn<BronFotobestand>("Peildatum", "Peildatum", "peildatum",
			"peildatum"));
		addColumn(new CustomPropertyColumn<BronFotobestand>("Aanmaakdatum", "Aanmaakdatum",
			"aanmaakdatum", "aanmaakdatum"));
		addColumn(new CustomPropertyColumn<BronFotobestand>("Inleesdatum", "Inleesdatum",
			"inleesdatum", "inleesdatum"));
		addColumn(new CustomPropertyColumn<BronFotobestand>("Ingelezen door", "Ingelezen door",
			"persoon.achternaam", "ingelezenDoor.persoon.volledigeNaam"));
		addColumn(new CustomPropertyColumn<BronFotobestand>("Aantal verschillen",
			"Aantal verschillen", "aantalVerschillen", "aantalVerschillen"));
		addColumn(new CustomPropertyColumn<BronFotobestand>("Controle-getal", "Controle-getal",
			"controletotaalAccountant", "controletotaalAccountant"));
	}
}
