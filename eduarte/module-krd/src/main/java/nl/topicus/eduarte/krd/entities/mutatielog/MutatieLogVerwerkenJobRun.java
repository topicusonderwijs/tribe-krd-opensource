package nl.topicus.eduarte.krd.entities.mutatielog;

import javax.persistence.Entity;

import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krd.jobs.MutatieLogVerwerkenJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Jobrun voor een mutatielogverwerker.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class MutatieLogVerwerkenJobRun extends JobRun
{
	private static final long serialVersionUID = 1L;

	public MutatieLogVerwerkenJobRun()
	{
	}

	@Override
	public Class< ? extends EduArteJob> getJobClass()
	{
		return MutatieLogVerwerkenJob.class;
	}

}
