package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.landelijk.Verblijfsvergunning;

/**
 * Tabel met de mogelijke kolommen voor verblijfsvergunningen.
 * 
 * @author schimmel
 */
public class VerblijfsvergunningTable extends
		CustomDataPanelContentDescription<Verblijfsvergunning>
{
	private static final long serialVersionUID = 1L;

	public VerblijfsvergunningTable()
	{
		super("Verblijfsvergunningen");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<Verblijfsvergunning>("Code", "Code", "code", "code"));
		addColumn(new CustomPropertyColumn<Verblijfsvergunning>("Naam", "Naam", "naam", "naam"));
	}
}
