package nl.topicus.eduarte.entities.vrijevelden;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Entiteit welke een enkel VrijVeld bevat.
 * 
 * @author hoeve
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public abstract class VrijVeldEntiteit extends LandelijkOfInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vrijVeld", nullable = false)
	@Index(name = "idx_PVV_vrijVeld")
	private VrijVeld vrijVeld;

	@Column(nullable = true)
	private Boolean checkWaarde;

	@Column(nullable = true)
	private Date dateWaarde;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entiteit")
	@BatchSize(size = 100)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
	private List<VrijVeldOptieKeuze> keuzes = new ArrayList<VrijVeldOptieKeuze>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "keuze", nullable = true)
	@Index(name = "idx_VVE_keuze")
	private VrijVeldKeuzeOptie keuze;

	@Lob
	@Basic(optional = true)
	private String longTextWaarde;

	@Column(nullable = true)
	private Long numberWaarde;

	@Column(nullable = true)
	private String textWaarde;

	public VrijVeldEntiteit()
	{
	}

	public VrijVeld getVrijVeld()
	{
		return vrijVeld;
	}

	public void setVrijVeld(VrijVeld veld)
	{
		vrijVeld = veld;
	}

	/**
	 * @return de string waarde van het vrijveld
	 */
	public String getOmschrijving()
	{
		if (getVrijVeld().getType().equals(VrijVeldType.AANKRUISVAK))
			return getCheckWaarde() == null ? null : getCheckWaarde() ? "Ja" : "Nee";
		else if (getVrijVeld().getType().equals(VrijVeldType.DATUM))
			return TimeUtil.getInstance().formatDate(getDateWaarde());
		else if (getVrijVeld().getType().equals(VrijVeldType.KEUZELIJST))
			return getKeuze() != null ? getKeuze().getNaam() : "";
		else if (getVrijVeld().getType().equals(VrijVeldType.LANGETEKST))
			return getLongTextWaarde();
		else if (getVrijVeld().getType().equals(VrijVeldType.MULTISELECTKEUZELIJST))
		{
			String omschrijving = "";

			for (VrijVeldOptieKeuze optie : getKeuzes())
			{
				omschrijving += optie.getOptie().getNaam() + ", ";
			}

			if (omschrijving.lastIndexOf(",") > 0)
				omschrijving = omschrijving.substring(0, omschrijving.lastIndexOf(","));

			if (omschrijving.length() > 40)
			{
				omschrijving = omschrijving.substring(0, 37);
				omschrijving += " ...";
			}

			return omschrijving;
		}
		else if (getVrijVeld().getType().equals(VrijVeldType.NUMERIEK) && getNumberWaarde() != null)
			return getNumberWaarde().toString();
		else if (getVrijVeld().getType().equals(VrijVeldType.TEKST))
			return getTextWaarde();

		return null;
	}

	public boolean isIngevuld()
	{
		return StringUtil.isNotEmpty(getOmschrijving());
	}

	public Boolean getCheckWaarde()
	{
		return checkWaarde;
	}

	public void setCheckWaarde(Boolean checkWaarde)
	{
		this.checkWaarde = checkWaarde;
	}

	public Date getDateWaarde()
	{
		return dateWaarde;
	}

	public void setDateWaarde(Date dateWaarde)
	{
		this.dateWaarde = dateWaarde;
	}

	public List<VrijVeldOptieKeuze> getKeuzes()
	{
		return keuzes;
	}

	public void setKeuzes(List<VrijVeldOptieKeuze> keuzes)
	{
		this.keuzes = keuzes;
	}

	public VrijVeldKeuzeOptie getKeuze()
	{
		return keuze;
	}

	public void setKeuze(VrijVeldKeuzeOptie keuze)
	{
		this.keuze = keuze;
	}

	public String getLongTextWaarde()
	{
		return longTextWaarde;
	}

	public void setLongTextWaarde(String longTextWaarde)
	{
		this.longTextWaarde = longTextWaarde;
	}

	public Long getNumberWaarde()
	{
		return numberWaarde;
	}

	public void setNumberWaarde(Long numberWaarde)
	{
		this.numberWaarde = numberWaarde;
	}

	public String getTextWaarde()
	{
		return textWaarde;
	}

	public void setTextWaarde(String textWaarde)
	{
		this.textWaarde = textWaarde;
	}

	/**
	 * @return indien van toepassing, het object waar deze implementatie aangekoppeld is
	 *         afgezien van {@link VrijVeld}.
	 */
	public abstract VrijVeldable< ? extends VrijVeldEntiteit> getEntiteit();

	/**
	 * Stelt, indien van toepassing, het object in waar deze implementatie aangekoppeld is
	 * afgezien van {@link VrijVeld}.
	 * 
	 * @param entiteit
	 */
	public abstract void setEntiteit(VrijVeldable< ? extends VrijVeldEntiteit> entiteit);
}
