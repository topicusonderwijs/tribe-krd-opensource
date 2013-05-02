package nl.topicus.eduarte.krdparticipatie.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.jobs.logging.TerugdraaibareJobRun;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krdparticipatie.jobs.KRDWaarnemingenImporterenJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue(value = "KRDWaarnemingenImpJobRun")
public class KRDWaarnemingenImporterenJobRun extends TerugdraaibareJobRun
{
	private static final long serialVersionUID = 1L;

	public KRDWaarnemingenImporterenJobRun()
	{
	}

	@Override
	public Class< ? extends EduArteJob> getJobClass()
	{
		return KRDWaarnemingenImporterenJob.class;
	}

}
