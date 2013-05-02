package nl.topicus.eduarte.jobs;

import java.util.Date;

import nl.topicus.cobra.quartz.CobraScheduler;
import nl.topicus.cobra.quartz.JobDescription;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.apache.wicket.model.IModel;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;

public class EduArteJobDescription extends JobDescription
{
	private static final long serialVersionUID = 1L;

	private final Date peildatum;

	private final Long medewerkerId;

	public EduArteJobDescription(EduArteJob job, JobExecutionContext context)
	{
		super(job, context);

		medewerkerId = job.getMedewerkerId();
		peildatum = job.getPeildatum();
	}

	public EduArteJobDescription(Trigger trigger)
	{
		super(trigger);
		Medewerker medewerker =
			(Medewerker) getFromJobDataMap(trigger, CobraScheduler.MEDEWERKER_KEY);
		medewerkerId = medewerker == null ? null : medewerker.getId();
		peildatum = (Date) getFromJobDataMap(trigger, CobraScheduler.PEILDATUM_KEY);
	}

	@Override
	public Object getFromJobDataMap(Trigger trigger, String key)
	{
		Object ret = super.getFromJobDataMap(trigger, key);
		return ret instanceof IModel< ? > ? ((IModel< ? >) ret).getObject() : ret;
	}

	public Long getMedewerkerId()
	{
		return medewerkerId;
	}

	public Date getPeildatum()
	{
		return peildatum;
	}
}
