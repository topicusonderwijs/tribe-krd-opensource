package nl.topicus.eduarte.krd.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krd.jobs.StagemarktOrganisatieControleJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue("SmrktControleJobRun")
public class StagemarktOrganisatieControleJobRun extends JobRun
{
	private static final long serialVersionUID = 1L;

	public StagemarktOrganisatieControleJobRun()
	{
	}

	@Override
	public Class< ? extends EduArteJob> getJobClass()
	{
		return StagemarktOrganisatieControleJob.class;
	}
}
