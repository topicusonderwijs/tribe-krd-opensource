package nl.topicus.cobra.quartz;

import java.io.Serializable;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;

public interface JobDescriptionFilter extends Serializable
{
	public boolean matches(CobraJob job, JobExecutionContext context);

	public boolean matches(Trigger trigger);
}
