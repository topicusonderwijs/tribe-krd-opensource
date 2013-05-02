package nl.topicus.eduarte.entities.jobs.logging;

import javax.persistence.Entity;

import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.jobs.rapportage.RapportageCleanJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class RapportageCleanJobRun extends JobRun
{
	private static final long serialVersionUID = 1L;

	public RapportageCleanJobRun()
	{
		super();
	}

	@Override
	public Class< ? extends EduArteJob> getJobClass()
	{
		return RapportageCleanJob.class;
	}
}
