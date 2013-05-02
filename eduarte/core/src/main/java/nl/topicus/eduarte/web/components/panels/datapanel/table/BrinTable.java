package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.organisatie.Brin;

public class BrinTable extends CustomDataPanelContentDescription<Brin>
{
	private static final long serialVersionUID = 1L;

	public BrinTable()
	{
		super("Brincodes");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<Brin>("Brincode", "Brincode", "code"));
		addColumn(new CustomPropertyColumn<Brin>("Organisatie", "Organisatie", "naam"));
		addColumn(new CustomPropertyColumn<Brin>("Straat", "Straat",
			"fysiekAdres.adres.straatHuisnummer"));
		addColumn(new CustomPropertyColumn<Brin>("Plaats", "Plaats", "fysiekAdres.adres.plaats"));
		addColumn(new CustomPropertyColumn<Brin>("Onderwijssector", "Onderwijssector",
			"onderwijssector"));

	}
}