package nl.topicus.eduarte.entities.jobs.logging;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;

import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.jobs.EduArteJob;
import nl.topicus.eduarte.jobs.rapportage.RapportageJob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class RapportageJobRun extends JobRun
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	@Basic(optional = false)
	private String bestandsNaam;

	@Lob()
	@Basic(fetch = FetchType.LAZY)
	private byte[] resultaat;

	@Column(nullable = true)
	@RestrictedAccess(hasSetter = false)
	private Long resultaatSize;

	@Column(nullable = true)
	@Basic(optional = false)
	@Enumerated(EnumType.STRING)
	@Index(name = "idx_RJR_DTT")
	@AutoForm(label = "Type")
	private DocumentTemplateType documentType;

	public RapportageJobRun()
	{
		super();
	}

	@Override
	public Class< ? extends EduArteJob> getJobClass()
	{
		return RapportageJob.class;
	}

	public byte[] getResultaat()
	{
		return resultaat;
	}

	/**
	 * Stelt het resultaat in en de size van de aantal bytes van het resultaat.
	 * 
	 * @param resultaat
	 */
	public void setResultaat(byte[] resultaat)
	{
		this.resultaat = resultaat;

		if (resultaat == null)
			resultaatSize = null;
		else
			resultaatSize = new Long(resultaat.length);
	}

	/**
	 * @return de lengte van het resultaat, dit wordt ingesteld wanneer het resultaat
	 *         wordt ingesteld.
	 */
	public Long getResultaatSize()
	{
		return resultaatSize;
	}

	public String getResultaatSizeFormatted()
	{
		Long sizeKiB = 1024L; // KibiByte
		Long sizeMiB = 1024 * sizeKiB; // MebiByte
		Long sizeGiB = 1024 * sizeMiB; // GibiByte

		if (resultaatSize == null)
			return "0 KiB";
		else if (resultaatSize >= sizeGiB)
			return (resultaatSize / sizeGiB) + " GiB";
		else if (resultaatSize >= sizeMiB)
			return (resultaatSize / sizeMiB) + " MiB";
		else if (resultaatSize >= sizeKiB)
			return (resultaatSize / sizeKiB) + " KiB";

		return resultaatSize + " B";
	}

	/**
	 * @return true wanneer de size van het resultaat > 0 is.
	 */
	public boolean heeftResultaat()
	{
		return getResultaatSize() != null && getResultaatSize() > 0;
	}

	public String getBestandsNaam()
	{
		return bestandsNaam;
	}

	public void setBestandsNaam(String bestandsNaam)
	{
		this.bestandsNaam = bestandsNaam;
	}

	public DocumentTemplateType getDocumentType()
	{
		return documentType;
	}

	public void setDocumentType(DocumentTemplateType documentType)
	{
		this.documentType = documentType;
	}
}
