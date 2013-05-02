package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;

import org.apache.wicket.model.IModel;

public class JobRunDetailZoekFilter<T extends JobRunDetail> extends AbstractZoekFilter<T>
{
	private static final long serialVersionUID = 1L;

	private IModel<JobRun> jobRun;

	private String uitkomst;

	public JobRunDetailZoekFilter(JobRun jobRun)
	{
		setJobRun(jobRun);
	}

	public JobRun getJobRun()
	{
		return getModelObject(jobRun);
	}

	public void setJobRun(JobRun jobRun)
	{
		this.jobRun = makeModelFor(jobRun);
	}

	public String getUitkomst()
	{
		return uitkomst;
	}

	public void setUitkomst(String uitkomst)
	{
		this.uitkomst = uitkomst;
	}
}
