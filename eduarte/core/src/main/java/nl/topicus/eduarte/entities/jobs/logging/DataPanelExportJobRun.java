package nl.topicus.eduarte.entities.jobs.logging;

import javax.persistence.Entity;

import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.jobs.rapportage.DataPanelExportJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Job run voor het exporteren van datapanels.
 * 
 * @author loite
 * 
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class DataPanelExportJobRun extends RapportageJobRun
{
	private static final long serialVersionUID = 1L;

	@Override
	public Class< ? extends EduArteJob> getJobClass()
	{
		return DataPanelExportJob.class;
	}

}
