package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.DateTimeColumn;
import nl.topicus.eduarte.entities.examen.ExamenstatusOvergang;

public class ExamenstatusOvergangTable extends
		CustomDataPanelContentDescription<ExamenstatusOvergang>
{
	private static final long serialVersionUID = 1L;

	public ExamenstatusOvergangTable()
	{
		super("Statusovergangen");
		createColumns();
		createGroupProperties();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<ExamenstatusOvergang>("Van status", "Van status",
			"vanStatus.naam"));
		addColumn(new CustomPropertyColumn<ExamenstatusOvergang>("Naar status", "Naar status",
			"naarStatus.naam"));
		addColumn(new DateTimeColumn<ExamenstatusOvergang>("Datum/tijd", "Datum/tijd", "datumTijd"));
		addColumn(new CustomPropertyColumn<ExamenstatusOvergang>("Opmerkingen", "Opmerkingen",
			"opmerkingen"));
	}

	private void createGroupProperties()
	{
	}

}
