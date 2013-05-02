package nl.topicus.eduarte.krd.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.jobs.logging.TerugdraaibareJobRun;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krd.jobs.ProductregelsKopierenJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue("ProdregelsKopierenJobRun")
public class ProductregelsKopierenJobRun extends TerugdraaibareJobRun
{
	private static final long serialVersionUID = 1L;

	public ProductregelsKopierenJobRun()
	{
	}

	@Override
	public Class< ? extends EduArteJob> getJobClass()
	{
		return ProductregelsKopierenJob.class;
	}

}
