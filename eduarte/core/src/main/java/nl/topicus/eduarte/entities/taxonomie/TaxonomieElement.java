package nl.topicus.eduarte.entities.taxonomie;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.dao.helpers.TaxonomieElementDataAccessHelper;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumLandelijkOfInstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.providers.TaxonomieProvider;
import nl.topicus.eduarte.rapportage.entities.list.TaxonomieElementWrappedList;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * TaxonomieElementen kunnen verbintenisgebieden, deelgebieden of taxonomien zijn. Deze
 * kunnen zowel landelijk als instellingsspecifiek gedefinieerd zijn.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"taxonomiecode",
	"organisatie"})})
@Table(appliesTo = "TaxonomieElement", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
public abstract class TaxonomieElement extends BeginEinddatumLandelijkOfInstellingEntiteit
		implements Comparable<TaxonomieElement>, IContextInfoObject, TaxonomieProvider
{
	private static final long serialVersionUID = 1L;

	/**
	 * De 'lokale' code van het element
	 */
	@Column(nullable = false, length = 5)
	private String code;

	@Column(nullable = false, length = 50)
	private String afkorting;

	@Column(nullable = false, length = 200)
	@Index(name = "idx_TaxEl_naam")
	private String naam;

	@Column(nullable = true, length = 200)
	private String diplomanaam;

	@Column(nullable = true, length = 200)
	private String internationaleNaam;

	/**
	 * De externe code die gebruikt moet worden in communicatie met externen, zoals
	 * bijvoorbeeld de IB-Groep. Dit kan bijvoorbeeld de crebo-code of elementcode zijn.
	 */
	@Column(nullable = true, length = 20)
	@Index(name = "idx_TaxEl_externeCode")
	private String externeCode;

	/**
	 * De volledige taxonomiecode van het element
	 */
	@Column(nullable = false, length = 200)
	@Index(name = "idx_TaxEl_taxcode")
	private String taxonomiecode;

	@Column(nullable = false, length = 201)
	@Index(name = "idx_TaxEl_zoekParentCode")
	private String zoekParentCode;

	/**
	 * De volledige code van dit element maar dan in het formaat waarbij elk afzonderlijk
	 * deel van de taxonomiecode aangevuld is zodat deze allemaal uit 4 posities bestaan.
	 * 1.1.10.5 wordt dus bijvoorbeeld 0001.0001.0010.0005.
	 */
	@Column(nullable = false, length = 1000)
	@Index(name = "idx_TaxEl_sorteercode")
	private String sorteercode;

	/**
	 * Het element dat 1 niveau boven dit element staat. Mag null zijn indien het een
	 * topelement is.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "parent")
	@Index(name = "idx_TaxEl_parent")
	private TaxonomieElement parent;

	@Column(nullable = false)
	@Index(name = "idx_TaxEl_volgnummer")
	private int volgnummer;

	/**
	 * Nullable omdat het anders onmogelijk is om een insert te doen van een nieuwe
	 * taxonomie.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "taxonomie")
	@Index(name = "idx_TaxEl_taxonomie")
	private Taxonomie taxonomie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "taxonomieElementType")
	@Index(name = "idx_TaxEl_taxElType")
	private TaxonomieElementType taxonomieElementType;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<TaxonomieElement> children;

	/**
	 * een vrije matrix is een taxonomie element. Echter mag een vrije matrix nooit in de
	 * zoek resultaten van de taxonomieelementen voorkomen. Vrije matrix heeft een relatie
	 * met deelnemer. Dat heeft een taxonomieelement nooit. uitzonderlijk is dus false
	 * wanneer het geen vrije matrix is.
	 */
	@Column(nullable = false)
	private boolean uitzonderlijk = false;

	public TaxonomieElement()
	{
	}

	public TaxonomieElement(EntiteitContext context)
	{
		super(context);
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		if (code != null && code.length() > 5)
		{
			throw new IllegalArgumentException("Lengte van code mag maximaal 5 karakters zijn");
		}
		this.code = code;
		setTaxonomiecode(berekenTaxonomieCode());
	}

	@Exportable
	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	@Exportable
	public String getTaxonomiecode()
	{
		return taxonomiecode;
	}

	/**
	 * @return De taxonomiecode van de parent + een '.' of een '_' afhankelijk van of dit
	 *         een deelgebied of een verbintenisgebied is.
	 */
	public String getParentTaxonomiecodeMetScheidingsteken()
	{
		if (getParent() == null)
			return null;
		String res = getParent().getTaxonomiecode();
		if (getTaxonomieElementType().getSoort() == SoortTaxonomieElement.Deelgebied)
			res = res + "_";
		else
			res = res + ".";
		return res;
	}

	/**
	 * 
	 * @param typeNaam
	 * @return Het eerste parentelement van dit element dat gekoppeld is aan een
	 *         taxonomie-elementtype met de gegeven naam.
	 */
	@Exportable
	public TaxonomieElement getParentVanType(String typeNaam)
	{
		if (getTaxonomieElementType().getNaam().equals(typeNaam))
			return this;
		if (getParent() == null)
			return null;
		return getParent().getParentVanType(typeNaam);
	}

	/**
	 * 
	 * @param typeNamen
	 * @return Een lijst van alle children (recursief) die van een van de gegeven types
	 *         zijn. De typenamen moeten met komma's gescheiden zijn, dus bijvoorbeeld
	 *         'Kerntaak,Werkproces'
	 */
	@Exportable
	public TaxonomieElementWrappedList getChildrenVanTypes(String typeNamen)
	{
		List<TaxonomieElement> res = internalGetChildrenVanTypes(typeNamen);
		return new TaxonomieElementWrappedList(res);
	}

	private List<TaxonomieElement> internalGetChildrenVanTypes(String typeNamen)
	{
		if (StringUtil.isEmpty(typeNamen))
			return Collections.emptyList();
		String[] types = typeNamen.split(",", -1);
		List<TaxonomieElement> res = new ArrayList<TaxonomieElement>();
		for (TaxonomieElement child : getChildren())
		{
			for (String typeNaam : types)
			{
				if (child.getTaxonomieElementType().getNaam().equals(typeNaam))
				{
					res.add(child);
				}
				res.addAll(child.internalGetChildrenVanTypes(typeNaam));
			}
		}
		Collections.sort(res);
		return res;
	}

	public void setTaxonomiecode(String taxonomiecode)
	{
		this.taxonomiecode = taxonomiecode;
		setZoekParentCode(taxonomiecode + "%");
		berekenSorteercode();
	}

	@Exportable
	public TaxonomieElement getParent()
	{
		return parent;
	}

	public void setParent(TaxonomieElement parent)
	{
		this.parent = parent;
	}

	public int getVolgnummer()
	{
		return volgnummer;
	}

	public void setVolgnummer(int volgnummer)
	{
		this.volgnummer = volgnummer;
	}

	@Exportable
	public String getExterneCode()
	{
		return externeCode;
	}

	public void setExterneCode(String externeCode)
	{
		this.externeCode = externeCode;
	}

	/**
	 * Genereert de taxonomiecode van dit element op basis van de code van dit element en
	 * de codes van de bovenliggende elementen.
	 * 
	 * @return de taxonomiecode van dit element.
	 */
	public String berekenTaxonomieCode()
	{
		StringBuilder res = new StringBuilder();
		internalGenereerTaxonomieCode(res);

		return res.toString();
	}

	private void internalGenereerTaxonomieCode(StringBuilder currentCode)
	{
		if (getParent() != null)
		{
			getParent().internalGenereerTaxonomieCode(currentCode);
		}
		if (currentCode.length() > 0)
		{
			TaxonomieElement prnt = (TaxonomieElement) getParent().doUnproxy();
			if (Verbintenisgebied.class.isAssignableFrom(prnt.getClass())
				&& Deelgebied.class.isAssignableFrom(this.getClass()))
			{
				// Parent is verbintenisgebied en dit is deelgebied --> scheiding is een
				// underscore.
				currentCode.append("_");
			}
			else
			{
				currentCode.append(".");
			}
		}
		currentCode.append(getCode());
	}

	/**
	 * Berekent de soorteercode van dit element en roept de setter aan.
	 */
	private void berekenSorteercode()
	{
		StringBuilder res = new StringBuilder();
		internalBerekenSorteercode(res);
		setSorteercode(res.toString());
	}

	private void internalBerekenSorteercode(StringBuilder currentCode)
	{
		if (getParent() != null)
		{
			getParent().internalBerekenSorteercode(currentCode);
		}
		if (currentCode.length() > 0)
		{
			currentCode.append(".");
		}
		currentCode.append(StringUtil.voegVoorloopnullenToe(getCode(), 4));
	}

	public Taxonomie getTaxonomie()
	{
		return taxonomie;
	}

	public void setTaxonomie(Taxonomie taxonomie)
	{
		this.taxonomie = taxonomie;
	}

	public String getAfkorting()
	{
		return afkorting;
	}

	public void setAfkorting(String afkorting)
	{
		this.afkorting = afkorting;
	}

	public String getZoekParentCode()
	{
		return zoekParentCode;
	}

	public void setZoekParentCode(String zoekParentCode)
	{
		this.zoekParentCode = zoekParentCode;
	}

	public TaxonomieElementType getTaxonomieElementType()
	{
		return taxonomieElementType;
	}

	public void setTaxonomieElementType(TaxonomieElementType taxonomieElementType)
	{
		this.taxonomieElementType = taxonomieElementType;
	}

	@Override
	public String toString()
	{
		if (getTaxonomiecode() == null && getNaam() == null)
		{
			// Blijkbaar een nieuw element, dit zijn namelijk verplichte velden.
			return "Nieuw " + getTaxonomieElementType().getNaam();
		}
		return getTaxonomiecode() + " - " + getNaam();
	}

	public String getSorteercode()
	{
		return sorteercode;
	}

	public void setSorteercode(String sorteercode)
	{
		this.sorteercode = sorteercode;
	}

	@Override
	public int compareTo(TaxonomieElement o)
	{
		return this.getSorteercode().compareTo(o.getSorteercode());
	}

	@Override
	public String getContextInfoOmschrijving()
	{
		return toString();
	}

	/**
	 * @return true als dit element verwijderbaar is, en anders false. In dit geval wordt
	 *         true teruggegeven als het element niet in gebruik is.
	 */
	public boolean isVerwijderbaar()
	{
		if (!isSaved())
		{
			return false;
		}
		if (!getChildren().isEmpty())
		{
			return false;
		}
		// Zoek naar opleidingen e.d. die hiernaar verwijzen.
		return !DataAccessRegistry.getHelper(TaxonomieElementDataAccessHelper.class).isInGebruik(
			this);
	}

	public List<TaxonomieElement> getChildren()
	{
		if (children == null)
			children = new ArrayList<TaxonomieElement>();
		return children;
	}

	public List<Deelgebied> getDeelgebieden()
	{
		List<TaxonomieElement> taxonomieElementChildren = getChildren();
		ArrayList<Deelgebied> deelgebiedChildren = new ArrayList<Deelgebied>();

		for (TaxonomieElement element : taxonomieElementChildren)
		{
			if (Deelgebied.class.isAssignableFrom(element.getClass()))
				deelgebiedChildren.add((Deelgebied) element);
		}

		return deelgebiedChildren;
	}

	@Exportable
	public int getDeelgebiedenCount()
	{
		return getDeelgebieden().size();
	}

	public void setChildren(List<TaxonomieElement> children)
	{
		this.children = children;
	}

	@Exportable
	public String getDiplomanaam()
	{
		return diplomanaam;
	}

	public void setDiplomanaam(String diplomanaam)
	{
		this.diplomanaam = diplomanaam;
	}

	// Een aantal methodes die door subclasses overriden kunnen worden voor properties die
	// getoond kunnen worden in tabellen.
	@Exportable
	public String getLeerwegCodes()
	{
		return null;
	}

	@Exportable
	public String getSoortOpleidingNaam()
	{
		return null;
	}

	@Exportable
	public String getNiveauNaam()
	{
		return null;
	}

	@Exportable
	public String getNiveauNaamLC()
	{
		return null;
	}

	@Exportable
	public BigDecimal getPrijsfactor()
	{
		return null;
	}

	@Exportable
	public Integer getStudiebelastingsuren()
	{
		return null;
	}

	@Exportable
	public String getWettelijkeEisenOmschrijving()
	{
		return null;
	}

	@Exportable
	public String getBronWettelijkeEisen()
	{
		return null;
	}

	@Exportable
	public String getBrinKenniscentrum()
	{
		return null;
	}

	@Exportable
	public String getNaamKenniscentrum()
	{
		return null;
	}

	@Exportable
	public String getCodeCoordinatiepunt()
	{
		return null;
	}

	/**
	 * 
	 * @return De eerste parent van dit taxonomieelement dat een verbintenisgebied is.
	 */
	public Verbintenisgebied getEersteVerbintenisgebiedParent()
	{
		TaxonomieElement el = getParent();
		if (el == null)
		{
			return null;
		}
		el = (TaxonomieElement) el.doUnproxy();
		if (Verbintenisgebied.class.isAssignableFrom(el.getClass()))
		{
			return (Verbintenisgebied) el;
		}
		return getParent().getEersteVerbintenisgebiedParent();
	}

	/**
	 * 
	 * @return Taxonomiecode + Naam. Deze methode wordt overschreven door
	 *         {@link Deelgebied} zodat de parentcode niet meegenomen wordt.
	 */
	@Exportable
	public String getTaxonomiecodeNaam()
	{
		return getTaxonomiecode() + " " + getNaam();
	}

	@Exportable
	public String getProfiel1()
	{
		return null;
	}

	@Exportable
	public String getProfiel2()
	{
		return null;
	}

	@Exportable
	public String getSectornamen()
	{
		return null;
	}

	public boolean isUitzonderlijk()
	{
		return uitzonderlijk;
	}

	public void setUitzonderlijk(boolean uitzonderlijk)
	{
		this.uitzonderlijk = uitzonderlijk;
	}

	public void setCodeZonderTaxonomie(String code)
	{
		if (code != null && code.length() > 5)
		{
			throw new IllegalArgumentException("Lengte van code mag maximaal 5 karakters zijn");
		}
		this.code = code;
	}

	public void setInternationaleNaam(String internationaleNaam)
	{
		this.internationaleNaam = internationaleNaam;
	}

	/**
	 * @return de internationale (Engelse) naam van dit taxonomie-element; of
	 *         <code>naam</code> indien geen internationale naam bekend
	 */
	public String getInternationaleNaam()
	{
		if (StringUtil.isEmpty(internationaleNaam))
			return naam;

		return internationaleNaam;
	}
}
