package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.DateTimeColumn;
import nl.topicus.eduarte.entities.examen.Examendeelname;

public class DeelnemerExamendeelnameTable extends CustomDataPanelContentDescription<Examendeelname>
{
	private static final long serialVersionUID = 1L;

	public DeelnemerExamendeelnameTable()
	{
		super("Examendeelnames");
		createColumns();
		createGroupProperties();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<Examendeelname>("Nummer", "Nummer",
			"deelnemer.deelnemernummer", "verbintenis.deelnemer.deelnemernummer", false));
		addColumn(new CustomPropertyColumn<Examendeelname>("Roepnaam", "Roepnaam",
			"persoon.roepnaam", "verbintenis.deelnemer.persoon.roepnaam", false));
		addColumn(new CustomPropertyColumn<Examendeelname>("Voorletters", "Voorletters",
			"persoon.voorletters", "verbintenis.deelnemer.persoon.voorletters", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Examendeelname>("Voornamen", "Voornamen",
			"persoon.voornamen", "verbintenis.deelnemer.persoon.voornamen", false)
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Examendeelname>("Voorvoegsel", "Voorvoegsel",
			"persoon.voorvoegsel", "verbintenis.deelnemer.persoon.voorvoegsel", false));
		addColumn(new CustomPropertyColumn<Examendeelname>("Achternaam", "Achternaam",
			"persoon.achternaam", "verbintenis.deelnemer.persoon.achternaam", false));
		addColumn(new CustomPropertyColumn<Examendeelname>("Opleiding", "Opleiding",
			"opleiding.naam", "verbintenis.opleiding.naam"));
		addColumn(new CustomPropertyColumn<Examendeelname>("Status", "Status",
			"examenstatus.volgnummer", "examenstatus.naam"));
		addColumn(new DateTimeColumn<Examendeelname>("Datum/tijd laatste statusovergang",
			"Datum/tijd", "datumLaatsteStatusovergang", "datumLaatsteStatusovergang"));
		addColumn(new CustomPropertyColumn<Examendeelname>("Examennummer", "Examennummer",
			"examennummer", "examennummerMetPrefix"));
		addColumn(new CustomPropertyColumn<Examendeelname>("Tijdvak", "Tijdvak", "tijdvak",
			"tijdvak"));
	}

	private void createGroupProperties()
	{
	}

}
