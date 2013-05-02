package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.bpv;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue("BPVCollectiefEditJobRunDetail")
public class BPVInschrijvingCollectiefEditJobRunDetail extends JobRunDetail
{
	private static final long serialVersionUID = 1L;

	public BPVInschrijvingCollectiefEditJobRunDetail()
	{
	}

	public BPVInschrijvingCollectiefEditJobRunDetail(JobRun run)
	{
		super(run);
	}
}
