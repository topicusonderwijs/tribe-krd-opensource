package nl.topicus.eduarte.krdparticipatie.entities;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue("KRDWaarnemingenImpJobRunDetail")
public class KRDWaarnemingenImporterenJobRunDetail extends JobRunDetail
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	private Long waarnemingId;

	public KRDWaarnemingenImporterenJobRunDetail()
	{
	}

	public KRDWaarnemingenImporterenJobRunDetail(JobRun run)
	{
		super(run);
	}

	public Long getWaarnemingId()
	{
		return waarnemingId;
	}

	public void setWaarnemingId(Long waarnemingId)
	{
		this.waarnemingId = waarnemingId;
	}

}
