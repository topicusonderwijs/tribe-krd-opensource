package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;

public class JobRunDetailTable extends CustomDataPanelContentDescription<JobRunDetail>
{
	private static final long serialVersionUID = 1L;

	public JobRunDetailTable()
	{
		super("Details");
		addColumn(new CustomPropertyColumn<JobRunDetail>("Datum/tijd", "Datum/tijd",
			"lastModifiedAt", "lastModifiedAtFormatted"));
		addColumn(new CustomPropertyColumn<JobRunDetail>("Uitkomst", "Uitkomst", "uitkomst",
			"uitkomst").setEscapeModelStrings(false));
	}
}
