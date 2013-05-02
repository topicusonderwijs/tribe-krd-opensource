package nl.topicus.eduarte.jobs;

import nl.topicus.cobra.quartz.CobraJob;
import nl.topicus.cobra.quartz.CobraScheduler;
import nl.topicus.cobra.quartz.JobDescriptionFilter;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.apache.wicket.model.IModel;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;

public class MatchMedewerkerFilter implements JobDescriptionFilter
{
	private static final long serialVersionUID = 1L;

	private Long medewerkerId;

	public MatchMedewerkerFilter(Medewerker medewerker)
	{
		this.medewerkerId = medewerker.getId();
	}

	@Override
	public boolean matches(CobraJob job, JobExecutionContext context)
	{
		return medewerkerId.equals(((EduArteJob) job).getMedewerkerId());
	}

	@Override
	public boolean matches(Trigger trigger)
	{
		Medewerker triggerMedewerker =
			(Medewerker) getFromJobDataMap(trigger, CobraScheduler.MEDEWERKER_KEY);
		return triggerMedewerker == null ? false : medewerkerId.equals(triggerMedewerker);
	}

	private Object getFromJobDataMap(Trigger trigger, String key)
	{
		Object ret = trigger.getJobDataMap().get(key);
		return ret instanceof IModel< ? > ? ((IModel< ? >) ret).getObject() : ret;
	}
}
