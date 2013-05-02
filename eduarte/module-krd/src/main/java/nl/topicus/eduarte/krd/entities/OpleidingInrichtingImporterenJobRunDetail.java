package nl.topicus.eduarte.krd.entities;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue("OplInrImportJobRunDetail")
public class OpleidingInrichtingImporterenJobRunDetail extends JobRunDetail
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	private Long criteriumId;

	@Column(nullable = true)
	private Long productregelId;

	public OpleidingInrichtingImporterenJobRunDetail()
	{
	}

	public OpleidingInrichtingImporterenJobRunDetail(JobRun run)
	{
		super(run);
	}

	public Long getCriteriumId()
	{
		return criteriumId;
	}

	public void setCriteriumId(Long criteriumId)
	{
		this.criteriumId = criteriumId;
	}

	public Long getProductregelId()
	{
		return productregelId;
	}

	public void setProductregelId(Long productregelId)
	{
		this.productregelId = productregelId;
	}
}
