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
@DiscriminatorValue("AbsMeldingenImpJobRunDetail")
public class AbsentiemeldingenImporterenJobRunDetail extends JobRunDetail
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	private Long absentiemeldingId;

	public AbsentiemeldingenImporterenJobRunDetail()
	{
	}

	public AbsentiemeldingenImporterenJobRunDetail(JobRun run)
	{
		super(run);
	}

	public Long getAbsentiemeldingId()
	{
		return absentiemeldingId;
	}

	public void setAbsentiemeldingId(Long absentiemeldingId)
	{
		this.absentiemeldingId = absentiemeldingId;
	}

}
