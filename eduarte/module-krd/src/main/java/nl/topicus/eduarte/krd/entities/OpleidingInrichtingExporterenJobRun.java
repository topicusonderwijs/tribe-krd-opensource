package nl.topicus.eduarte.krd.entities;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.bijlage.IDownloadable;
import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.krd.jobs.OpleidingInrichtingExporterenJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue("OpleidingInrExportJobRun")
public class OpleidingInrichtingExporterenJobRun extends JobRun implements IDownloadable
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	@Lob
	private byte[] contents;

	public OpleidingInrichtingExporterenJobRun()
	{
	}

	@Override
	public Class< ? extends EduArteJob> getJobClass()
	{
		return OpleidingInrichtingExporterenJob.class;
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
		return "opleidingsinrichting_"
			+ TimeUtil.getInstance().formatDateTime(getCreatedAt(), "ddMMyyyy_HHmmss") + ".xml";
	}
}
