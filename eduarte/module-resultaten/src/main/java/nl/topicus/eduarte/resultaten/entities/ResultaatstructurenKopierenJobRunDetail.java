package nl.topicus.eduarte.resultaten.entities;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue("ResstructsKopierenJobRunDetail")
public class ResultaatstructurenKopierenJobRunDetail extends JobRunDetail
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	private Long resultaatstructuurId;

	@Column(nullable = true)
	private Long toetsVerwijzingId;

	public ResultaatstructurenKopierenJobRunDetail()
	{
	}

	public ResultaatstructurenKopierenJobRunDetail(JobRun run)
	{
		super(run);
	}

	public void setResultaatstructuurId(Long resultaatstructuurId)
	{
		this.resultaatstructuurId = resultaatstructuurId;
	}

	public Long getResultaatstructuurId()
	{
		return resultaatstructuurId;
	}

	public void setToetsVerwijzingId(Long toetsVerwijzingId)
	{
		this.toetsVerwijzingId = toetsVerwijzingId;
	}

	public Long getToetsVerwijzingId()
	{
		return toetsVerwijzingId;
	}
}
