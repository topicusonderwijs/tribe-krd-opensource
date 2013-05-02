package nl.topicus.eduarte.entities.bijlage;

import java.util.Date;

import javax.persistence.*;

import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Algemene bijlage entiteit, Als er een bijlage gekoppeld moet worden aan bijvoorbeeld
 * een deelnemer, maak dan een Koppeltabel DeelnemerBijlage
 * 
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class Bijlage extends InstellingEntiteit implements IDownloadable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Wel nullable voor technische bijlage's, zoals bijvoorbeeld foto's.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "documentType")
	@AutoForm(label = "Documenttype", required = true)
	@Index(name = "idx_bijlage_documenttype")
	private DocumentType documentType;

	@Column(nullable = true, length = 200)
	private String omschrijving;

	@Column(length = 200, nullable = true)
	private String documentnummer;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date ontvangstdatum;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date geldigTot;

	@Column(length = 200, nullable = true)
	private String locatie;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	@Index(name = "idx_Bijlage_TypeBijlage")
	private TypeBijlage typeBijlage;

	@Column(length = 200, nullable = true)
	private String bestandsnaam;

	@Lob()
	private byte[] bestand;

	@Column(nullable = true)
	@RestrictedAccess(hasSetter = false)
	private Long bestandSize;

	@Column(length = 1000, nullable = true)
	private String link;

	public Bijlage()
	{
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public TypeBijlage getTypeBijlage()
	{
		return typeBijlage;
	}

	public void setTypeBijlage(TypeBijlage typeBijlage)
	{
		this.typeBijlage = typeBijlage;
	}

	public String getBestandsnaam()
	{
		return bestandsnaam;
	}

	public void setBestandsnaam(String bestandsnaam)
	{
		this.bestandsnaam = bestandsnaam;
	}

	public byte[] getBestand()
	{
		return bestand;
	}

	/**
	 * Stelt het bestand in en de size van de aantal bytes van het bestand.
	 * 
	 * @param bestand
	 *            The bestand to set.
	 */
	public void setBestand(byte[] bestand)
	{
		this.bestand = bestand;

		if (bestand == null)
			bestandSize = null;
		else
			bestandSize = new Long(bestand.length);
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		if (link != null && !link.contains("://"))
			this.link = "http://" + link;
		else
			this.link = link;
	}

	public Long getBestandSize()
	{
		return bestandSize;
	}

	public String getResultaatSizeFormatted()
	{
		Long sizeKiB = 1024L; // KibiByte
		Long sizeMiB = 1024 * sizeKiB; // MebiByte
		Long sizeGiB = 1024 * sizeMiB; // GibiByte

		if (bestandSize == null)
			return "";
		else if (bestandSize >= sizeGiB)
			return (bestandSize / sizeGiB) + " GiB";
		else if (bestandSize >= sizeMiB)
			return (bestandSize / sizeMiB) + " MiB";
		else if (bestandSize >= sizeKiB)
			return (bestandSize / sizeKiB) + " KiB";

		return bestandSize + " B";
	}

	public DocumentType getDocumentType()
	{
		return documentType;
	}

	public void setDocumentType(DocumentType documentType)
	{
		this.documentType = documentType;
	}

	public String getDocumentnummer()
	{
		return documentnummer;
	}

	public void setDocumentnummer(String documentnummer)
	{
		this.documentnummer = documentnummer;
	}

	public Date getOntvangstdatum()
	{
		return ontvangstdatum;
	}

	public void setOntvangstdatum(Date ontvangstdatum)
	{
		this.ontvangstdatum = ontvangstdatum;
	}

	public Date getGeldigTot()
	{
		return geldigTot;
	}

	public void setGeldigTot(Date geldigTot)
	{
		this.geldigTot = geldigTot;
	}

	public String getLocatie()
	{
		return locatie;
	}

	public void setLocatie(String locatie)
	{
		this.locatie = locatie;
	}
}
