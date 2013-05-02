/*
 * Copyright (c) 2010, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.participatie.MaatregelToekenning;

public class MaatregelTable extends CustomDataPanelContentDescription<MaatregelToekenning>
{
	private static final long serialVersionUID = 1L;

	public MaatregelTable()
	{
		super("Maatregelen");
		createColumns();
		createGroupProperties();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<MaatregelToekenning>("Maatregel", "Maatregel",
			"maatregel.omschrijving", "maatregel.omschrijving"));
		addColumn(new CustomPropertyColumn<MaatregelToekenning>("Datum", "Datum", "maatregelDatum",
			"maatregelDatum"));
		addColumn(new CustomPropertyColumn<MaatregelToekenning>("Nagekomen", "Nagekomen",
			"nagekomen", "nagekomenOmschrijving"));
		addColumn(new CustomPropertyColumn<MaatregelToekenning>("Opmerkingen", "Opmerkingen",
			"opmerkingen", "opmerkingen"));
		addColumn(new CustomPropertyColumn<MaatregelToekenning>("Automatisch", "Automatisch",
			"automatischToegekend", "automatischToegekendOmschrijving").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<MaatregelToekenning>("Veroorzaakt door",
			"Veroorzaakt door", "veroorzaaktDoor.absentieReden.omschrijving")
			.setDefaultVisible(false));
	}

	private void createGroupProperties()
	{
		addGroupProperty(new GroupProperty<MaatregelToekenning>("maatregel.omschrijving",
			"Maatregel", "maatregel.omschrijving"));
		addGroupProperty(new GroupProperty<MaatregelToekenning>("groepeerDatumOmschrijving",
			"Datum", "maatregelDatum"));
		addGroupProperty(new GroupProperty<MaatregelToekenning>("nagekomenOmschrijving",
			"Nagekomen", "nagekomen"));
	}
}
