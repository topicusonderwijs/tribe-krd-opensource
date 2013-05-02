package nl.topicus.eduarte.krd.entities.bron;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krd.bron.jobs.BronExamenverzamelingenAanmakenJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue("BronExamenverzAanmJobRun")
public class BronExamenverzamelingenAanmakenJobRun extends JobRun
{
	private static final long serialVersionUID = 1L;

	public BronExamenverzamelingenAanmakenJobRun()
	{
	}

	@Override
	public Class< ? extends EduArteJob> getJobClass()
	{
		return BronExamenverzamelingenAanmakenJob.class;
	}

	@Override
	public void info(String message)
	{
		JobRunDetail detail = new JobRunDetail(this);
		detail.setUitkomst(message.replaceAll("\n", "<br/>"));
		getDetails().add(detail);
	}

	@Override
	public void error(String message)
	{
		JobRunDetail detail = new JobRunDetail(this);
		detail.setUitkomst(message.replaceAll("\n", "<br/>"));
		getDetails().add(detail);
	}
}
