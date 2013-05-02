package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.DateTimeColumn;
import nl.topicus.eduarte.entities.examen.Examendeelname;

public class ExamendeelnameTable extends CustomDataPanelContentDescription<Examendeelname>
{
	private static final long serialVersionUID = 1L;

	public ExamendeelnameTable()
	{
		super("Examendeelnames");
		createColumns();
		createGroupProperties();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<Examendeelname>("Opleiding", "Opleiding", "verbintenis",
			"verbintenis.opleiding.naam"));
		addColumn(new CustomPropertyColumn<Examendeelname>("Status", "Status",
			"examenstatus.volgnummer", "examenstatus.naam"));
		addColumn(new DateTimeColumn<Examendeelname>("Datum/tijd laatste statusovergang",
			"Datum/tijd", "datumLaatsteStatusovergang", "datumLaatsteStatusovergang"));
		addColumn(new CustomPropertyColumn<Examendeelname>("Examennummer", "Examennummer",
			"examennummer", "examennummerMetPrefix"));
		addColumn(new CustomPropertyColumn<Examendeelname>("Tijdvak", "Tijdvak", "tijdvak",
			"tijdvak"));
		addColumn(new CustomPropertyColumn<Examendeelname>("Examenjaar", "Examenjaar",
			"examenjaar", "examenjaar"));
	}

	private void createGroupProperties()
	{
	}

}
