package nl.topicus.eduarte.krdparticipatie.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.jobs.logging.TerugdraaibareJobRun;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krdparticipatie.jobs.AbsentiemeldingenImporterenJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue(value = "AbsentiemeldingenImpJobRun")
public class AbsentiemeldingenImporterenJobRun extends TerugdraaibareJobRun
{
	private static final long serialVersionUID = 1L;

	public AbsentiemeldingenImporterenJobRun()
	{
	}

	@Override
	public Class< ? extends EduArteJob> getJobClass()
	{
		return AbsentiemeldingenImporterenJob.class;
	}

}
