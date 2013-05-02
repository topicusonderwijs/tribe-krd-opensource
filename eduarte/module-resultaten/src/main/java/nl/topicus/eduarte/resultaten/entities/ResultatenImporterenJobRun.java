package nl.topicus.eduarte.resultaten.entities;

import javax.persistence.Entity;

import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.resultaten.jobs.SeResultatenInlezenJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class ResultatenImporterenJobRun extends JobRun
{
	private static final long serialVersionUID = 1L;

	public ResultatenImporterenJobRun()
	{
	}

	@Override
	public Class< ? extends EduArteJob> getJobClass()
	{
		return SeResultatenInlezenJob.class;
	}

}
