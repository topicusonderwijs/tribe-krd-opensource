package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.quartz.JobDescription;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.AjaxButtonColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.ProgressbarColumn;
import nl.topicus.cobra.web.pages.FeedbackComponent;
import nl.topicus.eduarte.app.EduArteApp;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.quartz.SchedulerException;
import org.quartz.UnableToInterruptJobException;

public class JobDescriptionTable extends CustomDataPanelContentDescription<JobDescription>
{
	private static final long serialVersionUID = 1L;

	public JobDescriptionTable()
	{
		super("Lopende taken");
		addColumn(new CustomPropertyColumn<JobDescription>("Organisatie", "Organisatie",
			"organisatie", "organisatie"));
		addColumn(new CustomPropertyColumn<JobDescription>("Omschrijving", "Omschrijving",
			"omschrijving", "omschrijving"));
		addColumn(new CustomPropertyColumn<JobDescription>("Datum/tijd opgestart",
			"Datum/tijd opgestart", "datumTijdOpgestartFormatted", "datumTijdOpgestartFormatted"));
		addColumn(new CustomPropertyColumn<JobDescription>("Status", "Status", "status", "status"));
		addColumn(new ProgressbarColumn<JobDescription>("Voortgang", "Voortgang", "progress"));
		addColumn(new AjaxButtonColumn<JobDescription>("Taak stoppen", "Stoppen")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected String getCssDisabled()
			{
				return "ui-icon ui-icon-closethick";
			}

			@Override
			protected String getCssEnabled()
			{
				return "ui-icon ui-icon-closethick";
			}

			@Override
			public void onClick(WebMarkupContainer item, IModel<JobDescription> rowModel,
					AjaxRequestTarget target)
			{
				JobDescription description = rowModel.getObject();
				try
				{
					EduArteApp.get().getEduarteScheduler().interruptJob(description);
					item.info("De taak is verzocht te stoppen. "
						+ "Het kan even duren voordat deze daadwerkelijk stopt.");
				}
				catch (UnableToInterruptJobException e)
				{
					item.error("De taak kon niet gestopt worden: " + e.getMessage());
				}
				catch (SchedulerException e)
				{
					item.error("De taak kon niet gestopt worden: " + e.getMessage());
				}
				target.addComponent(item.findParent(CustomDataPanel.class));
				FeedbackComponent feedback = item.findParent(FeedbackComponent.class);
				if (feedback != null)
					feedback.refreshFeedback(target);
			}
		});
	}
}
