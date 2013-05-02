package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.bpv;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.jobs.EduArteJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue("BPVCollectiefEditJobRun")
public class BPVInschrijvingCollectiefEditJobRun extends JobRun
{
	private static final long serialVersionUID = 1L;

	public BPVInschrijvingCollectiefEditJobRun()
	{
	}

	@Override
	public Class< ? extends EduArteJob> getJobClass()
	{
		return BPVInschrijvingCollectiefEditJob.class;
	}

}
