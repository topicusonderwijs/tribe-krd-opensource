package nl.topicus.eduarte.resultaten.entities;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.bijlage.IDownloadable;
import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.resultaten.jobs.ResultaatstructurenExporterenJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue("ResstructurenExportJobRun")
public class ResultaatstructurenExporterenJobRun extends JobRun implements IDownloadable
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	@Lob
	private byte[] contents;

	public ResultaatstructurenExporterenJobRun()
	{
	}

	@Override
	public Class< ? extends EduArteJob> getJobClass()
	{
		return ResultaatstructurenExporterenJob.class;
	}

	public byte[] getContents()
	{
		return contents;
	}

	public void setContents(byte[] contents)
	{
		this.contents = contents;
	}

	@Override
	public byte[] getBestand()
	{
		return getContents();
	}

	@Override
	public String getBestandsnaam()
	{
		return "resultaatstructuren_"
			+ TimeUtil.getInstance().formatDateTime(getCreatedAt(), "ddMMyyyy_HHmmss") + ".xml";
	}
}
