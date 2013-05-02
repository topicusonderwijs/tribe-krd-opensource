package nl.topicus.eduarte.entities.jobs;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.jobs.account.AccountImportJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue("AccountsImportJobRun")
public class AccountsImportJobRun extends JobRun
{
	private static final long serialVersionUID = 1L;

	@Override
	public Class< ? extends EduArteJob> getJobClass()
	{
		return AccountImportJob.class;
	}
}
