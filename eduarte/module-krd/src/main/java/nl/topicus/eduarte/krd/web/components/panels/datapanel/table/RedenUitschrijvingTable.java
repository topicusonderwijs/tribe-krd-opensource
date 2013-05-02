package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CodeNaamActiefTable;

public class RedenUitschrijvingTable extends CodeNaamActiefTable<RedenUitschrijving>
{
	private static final long serialVersionUID = 1L;

	public RedenUitschrijvingTable()
	{
		super("Reden uitschrijving");
		addColumn(new CustomPropertyColumn<RedenUitschrijving>("redenUitval", "Reden uitval",
			"redenUitval.code"));
		addColumn(new CustomPropertyColumn<RedenUitschrijving>("uitstroomredenWI",
			"Uitstroomreden WI", "uitstroomredenWI.code"));
		addColumn(new BooleanPropertyColumn<RedenUitschrijving>("tonenBijVerbintenis",
			"Tonen bij Verbintenis", "tonenBijVerbintenis", "tonenBijVerbintenis"));
		addColumn(new BooleanPropertyColumn<RedenUitschrijving>("tonenBijBPV", "Tonen bij BPV",
			"tonenBijBPV", "tonenBijBPV"));
		addColumn(new BooleanPropertyColumn<RedenUitschrijving>("Overlijden", "Overlijden",
			"overlijden", "overlijden").setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<RedenUitschrijving>("Geslaagd", "Geslaagd", "geslaagd",
			"geslaagd").setDefaultVisible(false));
	}
}
