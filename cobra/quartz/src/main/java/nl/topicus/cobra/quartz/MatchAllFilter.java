package nl.topicus.cobra.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;

public class MatchAllFilter implements JobDescriptionFilter
{
	private static final long serialVersionUID = 1L;

	@Override
	public boolean matches(CobraJob job, JobExecutionContext context)
	{
		return true;
	}

	@Override
	public boolean matches(Trigger trigger)
	{
		return true;
	}
}
