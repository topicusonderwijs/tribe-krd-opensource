package nl.topicus.eduarte.resultaten.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.resultaten.jobs.ResultatenHerberekenJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class ResultatenHerberekenenJobRun extends JobRun
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	private long resultaatstructuurId;

	public ResultatenHerberekenenJobRun()
	{
	}

	@Override
	public Class< ? extends EduArteJob> getJobClass()
	{
		return ResultatenHerberekenJob.class;
	}

	public void setResultaatstructuurId(long resultaatstructuurId)
	{
		this.resultaatstructuurId = resultaatstructuurId;
	}

	public long getResultaatstructuurId()
	{
		return resultaatstructuurId;
	}

}
