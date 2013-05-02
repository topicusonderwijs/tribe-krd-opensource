package nl.topicus.eduarte.resultaten.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.jobs.logging.TerugdraaibareJobRun;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.resultaten.jobs.ResultaatstructurenKopierenJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue("ResstructurenKopierenJobRun")
public class ResultaatstructurenKopierenJobRun extends TerugdraaibareJobRun
{
	private static final long serialVersionUID = 1L;

	public ResultaatstructurenKopierenJobRun()
	{
	}

	@Override
	public Class< ? extends EduArteJob> getJobClass()
	{
		return ResultaatstructurenKopierenJob.class;
	}

}
