/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.jobs.logging;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Generieke detail info bij een logregel van een {@link JobRun}.
 * 
 * @author marrink
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class JobRunDetail extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 2000)
	private String uitkomst;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "JobRun")
	@Index(name = "idx_JobDetail_JobRun")
	private JobRun jobRun;

	public JobRunDetail()
	{
	}

	public JobRunDetail(JobRun run)
	{
		setJobRun(run);
	}

	public String getUitkomst()
	{
		return uitkomst;
	}

	public void setUitkomst(String uitkomst)
	{
		this.uitkomst = StringUtil.truncate(uitkomst, 2000, "...");
	}

	public JobRun getJobRun()
	{
		return jobRun;
	}

	public void setJobRun(JobRun jobRun)
	{
		this.jobRun = jobRun;
	}
}
