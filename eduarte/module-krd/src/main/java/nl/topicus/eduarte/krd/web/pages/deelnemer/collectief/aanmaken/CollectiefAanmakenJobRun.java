package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.aanmaken;

import javax.persistence.Entity;

import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.jobs.EduArteJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class CollectiefAanmakenJobRun extends JobRun
{
	private static final long serialVersionUID = 1L;

	public CollectiefAanmakenJobRun()
	{
	}

	@Override
	public Class< ? extends EduArteJob> getJobClass()
	{
		return CollectiefAanmakenJob.class;
	}

}