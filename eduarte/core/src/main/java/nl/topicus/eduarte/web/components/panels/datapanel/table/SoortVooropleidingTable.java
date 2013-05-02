package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding;

public class SoortVooropleidingTable extends CodeNaamActiefTable<SoortVooropleiding>
{
	private static final long serialVersionUID = 1L;

	public SoortVooropleidingTable()
	{
		super("Soort vooropleiding");
		addColumn(new CustomPropertyColumn<SoortVooropleiding>("Soort onderwijs met diploma",
			"Soort onderwijs met diploma", "soortOnderwijsMetDiploma"));
		addColumn(new CustomPropertyColumn<SoortVooropleiding>("Soort onderwijs zonder diploma",
			"Soort onderwijs zonder diploma", "soortOnderwijsZonderDiploma"));
	}
}
