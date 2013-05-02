package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.entities.jobs.logging.TerugdraaibareJobRun;

public class JobRunTable<T extends JobRun> extends CustomDataPanelContentDescription<T>
{
	private static final long serialVersionUID = 1L;

	public JobRunTable(Class< ? extends JobRun> jobRunClass)
	{
		super("Uitgevoerde taken");
		createColumns(jobRunClass);
	}

	private void createColumns(Class< ? extends JobRun> jobRunClass)
	{
		addColumn(new CustomPropertyColumn<T>("Datum/tijd start", "Starttijd", "runStart",
			"runStartFormatted"));
		addColumn(new CustomPropertyColumn<T>("Datum/tijd einde", "Eindtijd", "runEinde",
			"runEindeFormatted"));
		addColumn(new CustomPropertyColumn<T>("Medewerker", "Medewerker", "gestartDoor",
			"gestartDoor.persoon.volledigeNaam"));
		if (TerugdraaibareJobRun.class.isAssignableFrom(jobRunClass))
			addColumn(new CustomPropertyColumn<T>("Teruggedraaid", "Teruggedraaid",
				"teruggedraaid", "teruggedraaidOmschrijving"));
		addColumn(new CustomPropertyColumn<T>("Samenvatting", "Samenvatting", "samenvatting",
			"samenvatting"));
	}
}
