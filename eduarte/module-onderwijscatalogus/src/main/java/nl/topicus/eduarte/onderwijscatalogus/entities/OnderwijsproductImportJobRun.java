package nl.topicus.eduarte.onderwijscatalogus.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.onderwijscatalogus.jobs.OnderwijsproductImportJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue("OnderwijsproductImportJobRun")
public class OnderwijsproductImportJobRun extends JobRun
{
	private static final long serialVersionUID = 1L;

	public OnderwijsproductImportJobRun()
	{
	}

	@Override
	public Class< ? extends EduArteJob> getJobClass()
	{
		return OnderwijsproductImportJob.class;
	}

}
