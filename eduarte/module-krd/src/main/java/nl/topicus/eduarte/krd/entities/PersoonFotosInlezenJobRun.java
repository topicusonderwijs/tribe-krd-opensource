package nl.topicus.eduarte.krd.entities;

import javax.persistence.Entity;

import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krd.jobs.PersoonFotosInlezenJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class PersoonFotosInlezenJobRun extends JobRun
{
	private static final long serialVersionUID = 1L;

	@Override
	public Class< ? extends EduArteJob> getJobClass()
	{
		return PersoonFotosInlezenJob.class;
	}
}
