package nl.topicus.eduarte.krd.entities.mutatielog;

import javax.persistence.Entity;

import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krd.jobs.MutatieLogVerwerkenJobsStarterJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * JobRun voor de Job die de mutatielogverwerker jobs start.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class MutatieLogVerwerkenJobsStarterJobRun extends JobRun
{
	private static final long serialVersionUID = 1L;

	public MutatieLogVerwerkenJobsStarterJobRun()
	{
	}

	@Override
	public Class< ? extends EduArteJob> getJobClass()
	{
		return MutatieLogVerwerkenJobsStarterJob.class;
	}
}
