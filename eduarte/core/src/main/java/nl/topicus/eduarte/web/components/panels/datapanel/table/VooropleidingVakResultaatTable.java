package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.hogeronderwijs.VooropleidingVakResultaat;

public class VooropleidingVakResultaatTable extends
		AbstractVrijVeldableTable<VooropleidingVakResultaat>
{

	private static final long serialVersionUID = 1L;

	public VooropleidingVakResultaatTable()
	{
		super("Vakresultaten");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<VooropleidingVakResultaat>("Naam", "Naam", "vak.naam"));
		addColumn(new CustomPropertyColumn<VooropleidingVakResultaat>("Cijfer", "Cijfer", "score"));
		addColumn(new CustomPropertyColumn<VooropleidingVakResultaat>("IB-verificatie",
			"IB-verificatie", "status"));
		addColumn(new CustomPropertyColumn<VooropleidingVakResultaat>("Vakcode", "Vakcode",
			"vak.code"));
	}
}