package nl.topicus.eduarte.krd.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.jobs.logging.TerugdraaibareJobRun;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krd.jobs.OpleidingInrichtingImporterenJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue("OpleidingInrImportJobRun")
public class OpleidingInrichtingImporterenJobRun extends TerugdraaibareJobRun
{
	private static final long serialVersionUID = 1L;

	public OpleidingInrichtingImporterenJobRun()
	{
	}

	@Override
	public Class< ? extends EduArteJob> getJobClass()
	{
		return OpleidingInrichtingImporterenJob.class;
	}

}
