package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.eduarte.entities.personen.RelatieSoort;

/**
 * 
 * 
 * @author vanharen
 */
public class RelatieSoortTable extends CodeNaamActiefTable<RelatieSoort>
{

	private static final long serialVersionUID = 1L;

	public RelatieSoortTable(String title)
	{
		super(title);
		addColumn(new BooleanPropertyColumn<RelatieSoort>("Persoonrelatie", "Persoonrelatie",
			"persoonOpname", "persoonOpname"));
		addColumn(new BooleanPropertyColumn<RelatieSoort>("Organisatierelatie",
			"Organisatierelatie", "organisatieOpname", "organisatieOpname"));
	}

}
