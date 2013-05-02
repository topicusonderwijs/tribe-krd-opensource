package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.columns.AjaxLinkColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.jobs.logging.RapportageJobRun;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.RapportageDownloadLinkColumn;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

public class RapportageJobRunTable extends JobRunTable<RapportageJobRun>
{
	private static final long serialVersionUID = 1L;

	public RapportageJobRunTable()
	{
		super(RapportageJobRun.class);
		setTitle("Afgeronde rapportages");
		addColumn(new CustomPropertyColumn<RapportageJobRun>("Grootte", "Grootte", "resultaatSize"));
		addColumn(new RapportageDownloadLinkColumn("Download", "Download"));
		addColumn(new AjaxLinkColumn<RapportageJobRun>("Verwijderen", "Verwijderen", "Verwijderen",
			"Rapportage verwijderen", "")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(IModel<RapportageJobRun> rowModel, AjaxRequestTarget target)
			{
				RapportageJobRun run = rowModel.getObject();
				run.delete();
				run.commit();

				updateDataPanel(target);
			}
		});
	}

	@SuppressWarnings("unused")
	public void updateDataPanel(AjaxRequestTarget target)
	{

	}
}
