package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.aanmaken;

import javax.persistence.Entity;

import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class CollectiefAanmakenJobRunDetail extends JobRunDetail
{
	private static final long serialVersionUID = 1L;

	public CollectiefAanmakenJobRunDetail()
	{
	}

	public CollectiefAanmakenJobRunDetail(JobRun run)
	{
		super(run);
	}
}
