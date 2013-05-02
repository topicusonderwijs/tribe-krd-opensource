package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeld;
import nl.topicus.eduarte.web.components.panels.datapanel.table.NaamActiefTable;

public class VrijVeldTable extends NaamActiefTable<VrijVeld>
{
	private static final long serialVersionUID = 1L;

	public VrijVeldTable()
	{
		super("Vrij veld");
		addColumn(new CustomPropertyColumn<VrijVeld>("categorie", "Categorie", "categorie",
			"categorie"));
		addColumn(new CustomPropertyColumn<VrijVeld>("type", "Type", "type", "type"));

		addColumn(new BooleanPropertyColumn<VrijVeld>("intakescherm", "Intakescherm",
			"intakescherm", "intakescherm"));
		addColumn(new BooleanPropertyColumn<VrijVeld>("dossierscherm", "Dossierscherm",
			"dossierscherm", "dossierscherm"));
		addColumn(new BooleanPropertyColumn<VrijVeld>("uitgebreidzoeken", "Uitgebreid zoeken",
			"uitgebreidzoeken", "uitgebreidzoeken"));
		addColumn(new CustomPropertyColumn<VrijVeld>("taxonomie", "Taxonomie", "taxonomie",
			"taxonomie"));
	}
}
