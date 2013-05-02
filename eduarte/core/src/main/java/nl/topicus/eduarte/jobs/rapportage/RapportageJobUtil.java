package nl.topicus.eduarte.jobs.rapportage;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteScheduler;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;

import org.quartz.SchedulerException;

public class RapportageJobUtil
{
	public static void startRapportageJob(DocumentTemplate template,
			RapportageJobDataMap< ? extends IdObject, ? extends IdObject> datamap)
			throws SchedulerException
	{
		EduArteScheduler scheduler = EduArteApp.get().getEduarteScheduler();
		scheduler.triggerJob(template.getJobClass(), datamap);
	}
}