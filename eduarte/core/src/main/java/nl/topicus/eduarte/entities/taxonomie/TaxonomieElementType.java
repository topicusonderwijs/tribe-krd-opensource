package nl.topicus.eduarte.entities.taxonomie;

import javax.persistence.*;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.TaxonomieElementTypeDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Typering van een taxonomie-element, bijvoorbeeld Kwalificatiedossier, Uitstroom,
 * Kerntaak, Onderwijssoort etc.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"taxonomie",
	"volgnummer"})})
public class TaxonomieElementType extends LandelijkOfInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 5)
	private String afkorting;

	@Column(nullable = false, length = 100)
	private String naam;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "parent")
	@Index(name = "idx_TaxElType_parent")
	private TaxonomieElementType parent;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private SoortTaxonomieElement soort;

	@Column(nullable = false)
	private boolean inschrijfbaar;

	@Column(nullable = false)
	private boolean diplomeerbaar;

	/**
	 * Nullable alleen voor het speciale type 'Taxonomie'.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "taxonomie")
	@Index(name = "idx_TaxElType_taxonomie")
	private Taxonomie taxonomie;

	/**
	 * Volgnummer voor het sorteren van lijsten van elementtypes.
	 */
	@Column(nullable = false)
	private int volgnummer;

	/**
	 * Default constructor
	 */
	protected TaxonomieElementType()
	{
	}

	public TaxonomieElementType(EntiteitContext context)
	{
		super(context);
	}

	/**
	 * @return Het speciale taxonomie-elementtype 'Taxonomie'.
	 */
	public static final TaxonomieElementType getTaxonomieType()
	{
		return DataAccessRegistry.getHelper(TaxonomieElementTypeDataAccessHelper.class)
			.getTaxonomieType();
	}

	/**
	 * @return Returns the naam.
	 */
	public String getNaam()
	{
		return naam;
	}

	/**
	 * @param naam
	 *            The naam to set.
	 */
	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	/**
	 * @return Returns the taxonomie.
	 */
	public Taxonomie getTaxonomie()
	{
		return taxonomie;
	}

	/**
	 * @param taxonomie
	 *            The taxonomie to set.
	 */
	public void setTaxonomie(Taxonomie taxonomie)
	{
		this.taxonomie = taxonomie;
	}

	/**
	 * @return Returns the soort.
	 */
	public SoortTaxonomieElement getSoort()
	{
		return soort;
	}

	/**
	 * @param soort
	 *            The soort to set.
	 */
	public void setSoort(SoortTaxonomieElement soort)
	{
		this.soort = soort;
	}

	/**
	 * @return Returns the parent.
	 */
	public TaxonomieElementType getParent()
	{
		return parent;
	}

	/**
	 * @param parent
	 *            The parent to set.
	 */
	public void setParent(TaxonomieElementType parent)
	{
		this.parent = parent;
	}

	/**
	 * @return Returns the inschrijfbaar.
	 */
	public boolean isInschrijfbaar()
	{
		return inschrijfbaar;
	}

	/**
	 * @param inschrijfbaar
	 *            The inschrijfbaar to set.
	 */
	public void setInschrijfbaar(boolean inschrijfbaar)
	{
		this.inschrijfbaar = inschrijfbaar;
	}

	/**
	 * @return Returns the diplomeerbaar.
	 */
	public boolean isDiplomeerbaar()
	{
		return diplomeerbaar;
	}

	/**
	 * @param diplomeerbaar
	 *            The diplomeerbaar to set.
	 */
	public void setDiplomeerbaar(boolean diplomeerbaar)
	{
		this.diplomeerbaar = diplomeerbaar;
	}

	/**
	 * @return Returns the volgnummer.
	 */
	public int getVolgnummer()
	{
		return volgnummer;
	}

	/**
	 * @param volgnummer
	 *            The volgnummer to set.
	 */
	public void setVolgnummer(int volgnummer)
	{
		this.volgnummer = volgnummer;
	}

	/**
	 * @see nl.topicus.eduarte.entities.Entiteit#toString()
	 */
	@Override
	public String toString()
	{
		return getNaam();
	}

	/**
	 * @return Ja indien diplomeerbaar, en anders Nee
	 */
	public String getDiplomeerbaarOmschrijving()
	{
		return isDiplomeerbaar() ? "Ja" : "Nee";
	}

	/**
	 * @return Ja indien inschrijfbaar, en anders Nee
	 */
	public String getInschrijfbaarOmschrijving()
	{
		return isInschrijfbaar() ? "Ja" : "Nee";
	}

	/**
	 * @return Het taxonomie-elementtype dat dit taxonomie-elementtype als parent heeft,
	 *         of null indien er geen taxonomnie-elementtypes binnen deze taxonomie zijn
	 *         met dit taxonomie-elementtype als parent.
	 * @throws IllegalStateException
	 *             indien deze methode aangeroepen wordt voor het speciale
	 *             taxonomie-elementtype 'Taxonomie'. Gebruik in dat geval de methode
	 *             {@link #getVerbintenisgebiedChild(Taxonomie)}
	 */
	public TaxonomieElementType getVerbintenisgebiedChild()
	{
		if (getSoort() == SoortTaxonomieElement.Taxonomie)
		{
			throw new IllegalStateException(
				"Deze methode mag niet aangeroepen worden voor het speciale TaxonomieElementType 'Taxonomie'");
		}
		return getVerbintenisgebiedChild(getTaxonomie());
	}

	/**
	 * @param zoekInTaxonomie
	 * @return Het taxonomie-elementtype van de gegeven taxonomie dat dit
	 *         taxonomie-elementtype als parent heeft, of null indien er geen
	 *         taxonomie-elementtypes binnen de gegeven taxonomie zijn met dit
	 *         taxonomie-elementtype als parent.
	 */
	public TaxonomieElementType getVerbintenisgebiedChild(Taxonomie zoekInTaxonomie)
	{
		for (TaxonomieElementType type : zoekInTaxonomie.getTaxonomieElementTypes())
		{
			if (type.getSoort() == SoortTaxonomieElement.Verbintenisgebied
				&& type.getParent().equals(this))
			{
				return type;
			}
		}
		return null;
	}

	/**
	 * @return Het taxonomie-elementtype van het soort Deelgebied dat dit
	 *         taxonomie-elementtype als parent heeft, of null indien er geen deelgebieden
	 *         binnen deze taxonomie zijn met dit taxonomie-elementtype als parent.
	 * @throws IllegalStateException
	 *             indien deze methode aangeroepen wordt voor het speciale
	 *             taxonomie-elementtype 'Taxonomie'. Gebruik in dat geval de methode
	 *             {@link #getDeelgebiedChild(Taxonomie)}
	 */
	public TaxonomieElementType getDeelgebiedChild()
	{
		if (getSoort() == SoortTaxonomieElement.Taxonomie)
		{
			throw new IllegalStateException(
				"Deze methode mag niet aangeroepen worden voor het speciale TaxonomieElementType 'Taxonomie'");
		}
		return getDeelgebiedChild(getTaxonomie());
	}

	/**
	 * @param zoekInTaxonomie
	 * @return Het taxonomie-elementtype van het soort Deelgebied van de gegeven taxonomie
	 *         dat dit taxonomie-elementtype als parent heeft, of null indien er geen
	 *         deelgebieden binnen de gegeven taxonomie zijn met dit taxonomie-elementtype
	 *         als parent.
	 */
	public TaxonomieElementType getDeelgebiedChild(Taxonomie zoekInTaxonomie)
	{
		for (TaxonomieElementType type : zoekInTaxonomie.getTaxonomieElementTypes())
		{
			if (type.getSoort() == SoortTaxonomieElement.Deelgebied
				&& type.getParent().equals(this))
			{
				return type;
			}
		}
		return null;
	}

	/**
	 * @return true indien dit element verwijderd kan worden. Het element kan verwijderd
	 *         worden als er geen taxonomieelementen van dit type aangemaakt zijn, en als
	 *         dit type geen children heeft.
	 */
	public boolean isVerwijderbaar()
	{
		if (!isSaved())
		{
			return false;
		}
		return !DataAccessRegistry.getHelper(TaxonomieElementTypeDataAccessHelper.class)
			.isInGebruik(this);
	}

	public String getAfkorting()
	{
		return afkorting;
	}

	public void setAfkorting(String afkorting)
	{
		this.afkorting = afkorting;
	}

}
