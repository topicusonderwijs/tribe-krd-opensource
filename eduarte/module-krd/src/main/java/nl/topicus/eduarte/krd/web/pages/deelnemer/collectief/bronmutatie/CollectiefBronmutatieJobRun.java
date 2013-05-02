package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.bronmutatie;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.jobs.EduArteJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue("CollectiefBronmutatieJobRun")
public class CollectiefBronmutatieJobRun extends JobRun
{
	private static final long serialVersionUID = 1L;

	public CollectiefBronmutatieJobRun()
	{
	}

	@Override
	public Class< ? extends EduArteJob> getJobClass()
	{
		return CollectiefBronmutatieJob.class;
	}

}