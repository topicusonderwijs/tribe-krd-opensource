package nl.topicus.cobra.quartz;

import nl.topicus.cobra.entities.IOrganisatie;

import org.quartz.Trigger;

public interface JobSchedule
{
	public Class< ? extends CobraJob> getJobClass();

	public IOrganisatie getOrganisatie();

	public Trigger createTrigger(String jobName, String groupName);

	public boolean isEnabled();
}
