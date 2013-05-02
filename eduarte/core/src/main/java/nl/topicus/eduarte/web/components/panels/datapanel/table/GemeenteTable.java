/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.landelijk.Gemeente;

public class GemeenteTable extends CustomDataPanelContentDescription<Gemeente>
{
	private static final long serialVersionUID = 1L;

	public GemeenteTable()
	{
		super("Gemeente");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<Gemeente>("Code", "Code", "code", "code"));
		addColumn(new CustomPropertyColumn<Gemeente>("Naam", "Naam", "naam", "naam"));
		addColumn(new CustomPropertyColumn<Gemeente>("Begindatum", "Begindatum", "begindatum",
			"begindatum"));
		addColumn(new CustomPropertyColumn<Gemeente>("Einddatum", "Einddatum", "einddatum",
			"einddatum"));
		addColumn(new CustomPropertyColumn<Gemeente>("Nieuwe gemeente", "Nieuwe gemeente",
			"nieuweGemeente.naam"));
	}
}
