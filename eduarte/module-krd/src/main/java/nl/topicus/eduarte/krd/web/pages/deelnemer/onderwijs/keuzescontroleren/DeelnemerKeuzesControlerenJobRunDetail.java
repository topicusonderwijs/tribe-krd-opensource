package nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs.keuzescontroleren;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue("DeelnemerKeuzesCtrlJobRunDetail")
public class DeelnemerKeuzesControlerenJobRunDetail extends JobRunDetail
{
	private static final long serialVersionUID = 1L;

	public DeelnemerKeuzesControlerenJobRunDetail()
	{
	}

	public DeelnemerKeuzesControlerenJobRunDetail(JobRun run)
	{
		super(run);
	}
}
