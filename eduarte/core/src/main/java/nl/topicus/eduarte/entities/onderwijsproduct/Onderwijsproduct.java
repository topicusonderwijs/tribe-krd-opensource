package nl.topicus.eduarte.entities.onderwijsproduct;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.HibernateObjectCopyManager;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.bpv.BPVCriteriaOnderwijsproduct;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelbaarEntiteit;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.entities.taxonomie.mbo.Deelkwalificatie;
import nl.topicus.eduarte.entities.vrijevelden.OnderwijsproductVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;

import org.apache.wicket.markup.html.form.TextArea;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * Onderwijsproduct is een vak, project, lessenreeks, les, stage enz. Let op: Dit is iets
 * anders dan een opleiding waarop een deelnemer ingeschreven kan worden.
 * 
 * @author loite
 */
@Entity()
@Exportable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code",
	"organisatie"})})
@Table(appliesTo = "Onderwijsproduct", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
public class Onderwijsproduct extends BeginEinddatumInstellingEntiteit implements
		IContextInfoObject, Cloneable, Comparable<Onderwijsproduct>,
		IOrganisatieEenheidLocatieKoppelbaarEntiteit<OnderwijsproductAanbod>,
		VrijVeldable<OnderwijsproductVrijVeld>, IBijlageKoppelEntiteit<OnderwijsproductBijlage>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(htmlClasses = "unit_max")
	@Column(nullable = false, length = 20)
	@Index(name = "idx_Onderwijsproduct_code")
	private String code;

	@AutoForm(htmlClasses = "unit_max")
	@Column(nullable = false, length = 100)
	@Index(name = "idx_Onderwijsproduct_titel")
	private String titel;

	@Column(nullable = true, length = 100)
	private String internationaleTitel;

	@Lob()
	@AutoForm(htmlClasses = "unit_440")
	private String omschrijving;

	@Lob()
	private String internationaleOmschrijving;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "soortProduct")
	@Index(name = "idx_Onderwijsproduct_soort")
	@AutoForm(htmlClasses = "unit_max")
	private SoortOnderwijsproduct soortProduct;

	/**
	 * Zoektermen van dit onderwijsproduct.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "onderwijsproduct")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
	private List<OnderwijsproductZoekterm> zoektermen;

	@Column(nullable = false)
	@Index(name = "idx_Onderwijsproduct_status")
	@Enumerated(value = EnumType.STRING)
	@AutoForm(htmlClasses = "unit_max")
	private OnderwijsproductStatus status;

	/**
	 * Omschrijving van wanneer in het jaar het onderwijsproduct beschikbaar is.
	 */
	@Column(nullable = true, length = 2000)
	private String kalender;

	/**
	 * De (gewenste) uitvoeringsfrequentie van het onderwijsproduct, bijvoorbeeld 3 uur
	 * per week, 2 keer in de maand.
	 */
	@Column(nullable = true, length = 2000)
	private String uitvoeringsfrequentie;

	/**
	 * De omvang van het onderwijsproduct in aantal begeleide klokuren.
	 */
	@Column(nullable = true, scale = 10, precision = 20)
	private BigDecimal omvang;

	/**
	 * De belasting van het onderwijsproduct. De meeteenheid is door de
	 * onderwijsinstelling te bepalen (bijvoorbeeld studielasturen of studiepunten).
	 */
	@Column(nullable = true, scale = 10, precision = 20)
	private BigDecimal belasting;

	/**
	 * Aggregatieniveau van het onderwijsproduct geeft aan waar in de hierarchie dat een
	 * onderwijsproduct geplaatst mag worden.
	 */
	@AutoForm(htmlClasses = "unit_max")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "aggregatieniveau")
	@Index(name = "idx_Onderwijsproduct_aggr")
	private Aggregatieniveau aggregatieniveau;

	/**
	 * Geeft aan of dit onderwijsproduct geschikt is als een startonderwijsproduct. Als
	 * dit property op true gezet wordt, mogen geen voorwaardelijke onderwijsproducten
	 * aangegeven worden.
	 */
	@Column(nullable = false)
	@Index(name = "idx_Onderwijsproduct_start")
	private boolean startonderwijsproduct;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "leerstijl")
	@Index(name = "idx_Onderwijsproduct_leer")
	private Leerstijl leerstijl;

	/**
	 * Omschrijving van de toegankelijkheid van het onderwijsproduct.
	 */
	@Lob()
	private String toegankelijkheid;

	@Column(nullable = true)
	private Integer minimumAantalDeelnemers;

	@Column(nullable = true)
	private Integer maximumAantalDeelnemers;

	@Column(nullable = true, length = 2000)
	@AutoForm(editorClass = TextArea.class, htmlClasses = "unit_440", label = "Juridische eigenaar/docent")
	private String juridischEigenaar;

	@Column(nullable = true, length = 2000)
	private String gebruiksrecht;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "soortPraktijklokaal")
	@Index(name = "idx_OndProd_soortPrakt")
	private SoortPraktijklokaal soortPraktijklokaal;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "typeToets")
	@Index(name = "idx_Onderwijsproduct_typeToets")
	private TypeToets typeToets;

	/**
	 * Vereiste competenties van het personeel.
	 */
	@Column(nullable = true, length = 2000)
	private String personeelCompetenties;

	@Column(nullable = true, length = 2000)
	private String personeelKennisgebiedEnNiveau;

	@Column(nullable = true, length = 2000)
	private String personeelWettelijkeVereisten;

	@Column(nullable = true, length = 2000)
	private String personeelBevoegdheid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "typeLocatie")
	@Index(name = "idx_Onderwijsproduct_typeLoc")
	private TypeLocatie typeLocatie;

	/**
	 * Geeft aan of er voor een deelnemer die dit onderwijsproduct afneemt een
	 * werkstuktitel ingevoerd kan worden. Dit property zou op true gezet moeten worden
	 * voor het sectorwerkstuk en profielwerkstuk.
	 */
	@Column(nullable = false)
	private boolean heeftWerkstuktitel;

	/**
	 * Geeft aan of dit onderwijsproduct alleen door externe organisatie aangeboden wordt,
	 * en dat deze instelling het onderwijsproduct alleen als een EVC erkend.
	 */
	@Column(nullable = false)
	private boolean alleenExtern;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "onderwijsproduct")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
	private List<OnderwijsproductGebruiksmiddel> onderwijsproductGebruiksmiddelen;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "onderwijsproduct")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
	private List<OnderwijsproductVerbruiksmiddel> onderwijsproductVerbruiksmiddelen;

	@Column(nullable = true, scale = 2, precision = 19)
	private BigDecimal kostprijs;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "onderwijsproduct")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
	private List<OnderwijsproductAanbod> onderwijsproductAanbodList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "onderwijsproduct")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
	private List<OnderwijsproductTaxonomie> onderwijsproductTaxonomieList;

	/**
	 * De onderwijsproducten die onderdeel uit maken van dit onderwijsproduct.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
	private List<OnderwijsproductSamenstelling> onderdelen;

	/**
	 * De onderwijsproducten waarvan dit onderwijsproduct een onderdeel van is.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "child")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
	private List<OnderwijsproductSamenstelling> onderdeelVan;

	/**
	 * De onderwijsproducten die een voorwaarde vormen voor dit product.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "voorwaardeVoor")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
	private List<OnderwijsproductVoorwaarde> voorwaarden;

	/**
	 * De onderwijsproducten die een voorwaarde vormen voor dit product.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "voorwaardelijkProduct")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
	private List<OnderwijsproductVoorwaarde> voorwaardelijkVoor;

	/**
	 * De onderwijsproducten die de opvolgers zijn van dit product.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "oudProduct")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
	private List<OnderwijsproductOpvolger> opvolgers;

	/**
	 * De onderwijsproducten waarvoor dit onderwijsproduct een opvolger is.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "nieuwProduct")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
	private List<OnderwijsproductOpvolger> opvolgerVan;

	/**
	 * De bijlages van dit onderwijsproduct
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "onderwijsproduct")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
	private List<OnderwijsproductBijlage> bijlagen;

	/**
	 * De afnames van dit onderwijsproduct
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "onderwijsproduct")
	private List<OnderwijsproductAfname> afnames;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "onderwijsproduct")
	private List<Resultaatstructuur> resultaatstructuren;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "onderwijsproduct")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	private List<OnderwijsproductVrijVeld> vrijVelden;

	/**
	 * Geeft aan of dit onderwijsproduct ook afgenomen mag worden bij een Verbintenis met
	 * status Intake.
	 */
	@Column(nullable = false)
	private boolean bijIntake;

	/**
	 * Uitbreiding onderwijscatalogus voor Amarantis
	 */

	@Column(nullable = true)
	@AutoForm(editorClass = JaNeeCombobox.class)
	private Boolean individueel;

	@Column(nullable = true)
	@AutoForm(editorClass = JaNeeCombobox.class)
	private Boolean onafhankelijk;

	@Column(nullable = true)
	@AutoForm(editorClass = JaNeeCombobox.class)
	private Boolean begeleid;

	@Lob()
	private String vereisteVoorkennis;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "niveauaanduiding")
	@Index(name = "idx_Onderwijsproduct_niva")
	@AutoForm(label = "Niveauaanduiding")
	private OnderwijsproductNiveauaanduiding niveauaanduiding;

	@AutoForm(label = "Belasting EC")
	@Column(nullable = true, scale = 10, precision = 20)
	private BigDecimal belastingEC;

	@Column(nullable = true, scale = 10, precision = 20)
	private BigDecimal belastingOverig;

	@AutoForm(htmlClasses = "unit_max")
	@Column(nullable = true, length = 500)
	private String leerstofdrager;

	@Lob()
	private String literatuur;

	@Lob()
	private String hulpmiddelen;

	@Lob()
	private String docentactiviteiten;

	@Column(nullable = true)
	private Integer frequentiePerWeek;

	@Column(nullable = true, scale = 10, precision = 20)
	private BigDecimal tijdPerEenheid;

	@Column(nullable = true)
	private Integer aantalWeken;

	@Column(nullable = true)
	private Integer credits;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "onderwijsproduct")
	private List<BPVCriteriaOnderwijsproduct> bpvCriteria =
		new ArrayList<BPVCriteriaOnderwijsproduct>();

	public Onderwijsproduct()
	{
	}

	@Exportable
	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	@Exportable
	public String getTitel()
	{
		return titel;
	}

	public void setTitel(String titel)
	{
		this.titel = titel;
	}

	@Exportable
	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public List<OnderwijsproductZoekterm> getZoektermen()
	{
		if (zoektermen == null)
			zoektermen = new ArrayList<OnderwijsproductZoekterm>();
		return zoektermen;
	}

	public void setZoektermen(List<OnderwijsproductZoekterm> zoektermen)
	{
		this.zoektermen = zoektermen;
	}

	public OnderwijsproductStatus getStatus()
	{
		return status;
	}

	public void setStatus(OnderwijsproductStatus status)
	{
		this.status = status;
	}

	public String getKalender()
	{
		return kalender;
	}

	public void setKalender(String kalender)
	{
		this.kalender = kalender;
	}

	public String getUitvoeringsfrequentie()
	{
		return uitvoeringsfrequentie;
	}

	public void setUitvoeringsfrequentie(String uitvoeringsfrequentie)
	{
		this.uitvoeringsfrequentie = uitvoeringsfrequentie;
	}

	public BigDecimal getOmvang()
	{
		return omvang;
	}

	public void setOmvang(BigDecimal omvang)
	{
		this.omvang = omvang;
	}

	public BigDecimal getBelasting()
	{
		return belasting;
	}

	public void setBelasting(BigDecimal belasting)
	{
		this.belasting = belasting;
	}

	public Aggregatieniveau getAggregatieniveau()
	{
		return aggregatieniveau;
	}

	public void setAggregatieniveau(Aggregatieniveau aggregatieniveau)
	{
		this.aggregatieniveau = aggregatieniveau;
	}

	public boolean isStartonderwijsproduct()
	{
		return startonderwijsproduct;
	}

	public void setStartonderwijsproduct(boolean startonderwijsproduct)
	{
		this.startonderwijsproduct = startonderwijsproduct;
	}

	/**
	 * @return Ja indien het een startonderwijsproduct is, en anders Nee
	 */
	public String getStartonderwijsproductOmschrijving()
	{
		return isStartonderwijsproduct() ? "Ja" : "Nee";
	}

	/**
	 * @return Ja indien onderwijsproduct gekoppeld mag worden met intake verbintenis,
	 *         anders Nee.
	 */
	public String getBijIntakeOmschrijving()
	{
		return isBijIntake() ? "Ja" : "Nee";
	}

	public Leerstijl getLeerstijl()
	{
		return leerstijl;
	}

	public void setLeerstijl(Leerstijl leerstijl)
	{
		this.leerstijl = leerstijl;
	}

	public String getToegankelijkheid()
	{
		return toegankelijkheid;
	}

	public void setToegankelijkheid(String toegankelijkheid)
	{
		this.toegankelijkheid = toegankelijkheid;
	}

	public Integer getMinimumAantalDeelnemers()
	{
		return minimumAantalDeelnemers;
	}

	public void setMinimumAantalDeelnemers(Integer minimumAantalDeelnemers)
	{
		this.minimumAantalDeelnemers = minimumAantalDeelnemers;
	}

	public Integer getMaximumAantalDeelnemers()
	{
		return maximumAantalDeelnemers;
	}

	public void setMaximumAantalDeelnemers(Integer maximumAantalDeelnemers)
	{
		this.maximumAantalDeelnemers = maximumAantalDeelnemers;
	}

	public String getJuridischEigenaar()
	{
		return juridischEigenaar;
	}

	public void setJuridischEigenaar(String juridischEigenaar)
	{
		this.juridischEigenaar = juridischEigenaar;
	}

	public String getGebruiksrecht()
	{
		return gebruiksrecht;
	}

	public void setGebruiksrecht(String gebruiksrecht)
	{
		this.gebruiksrecht = gebruiksrecht;
	}

	public SoortPraktijklokaal getSoortPraktijklokaal()
	{
		return soortPraktijklokaal;
	}

	public void setSoortPraktijklokaal(SoortPraktijklokaal soortPraktijklokaal)
	{
		this.soortPraktijklokaal = soortPraktijklokaal;
	}

	public TypeToets getTypeToets()
	{
		return typeToets;
	}

	public void setTypeToets(TypeToets typeToets)
	{
		this.typeToets = typeToets;
	}

	public String getPersoneelCompetenties()
	{
		return personeelCompetenties;
	}

	public void setPersoneelCompetenties(String personeelCompetenties)
	{
		this.personeelCompetenties = personeelCompetenties;
	}

	public String getPersoneelKennisgebiedEnNiveau()
	{
		return personeelKennisgebiedEnNiveau;
	}

	public void setPersoneelKennisgebiedEnNiveau(String personeelKennisgebiedEnNiveau)
	{
		this.personeelKennisgebiedEnNiveau = personeelKennisgebiedEnNiveau;
	}

	public String getPersoneelWettelijkeVereisten()
	{
		return personeelWettelijkeVereisten;
	}

	public void setPersoneelWettelijkeVereisten(String personeelWettelijkeVereisten)
	{
		this.personeelWettelijkeVereisten = personeelWettelijkeVereisten;
	}

	public String getPersoneelBevoegdheid()
	{
		return personeelBevoegdheid;
	}

	public void setPersoneelBevoegdheid(String personeelBevoegdheid)
	{
		this.personeelBevoegdheid = personeelBevoegdheid;
	}

	public TypeLocatie getTypeLocatie()
	{
		return typeLocatie;
	}

	public void setTypeLocatie(TypeLocatie typeLocatie)
	{
		this.typeLocatie = typeLocatie;
	}

	public List<OnderwijsproductGebruiksmiddel> getOnderwijsproductGebruiksmiddelen()
	{
		if (onderwijsproductGebruiksmiddelen == null)
			onderwijsproductGebruiksmiddelen = new ArrayList<OnderwijsproductGebruiksmiddel>();
		return onderwijsproductGebruiksmiddelen;
	}

	public void setOnderwijsproductGebruiksmiddelen(
			List<OnderwijsproductGebruiksmiddel> onderwijsproductGebruiksmiddelen)
	{
		this.onderwijsproductGebruiksmiddelen = onderwijsproductGebruiksmiddelen;
	}

	/**
	 * @return De gebruiksmiddelen van dit onderwijsproduct als een comma-separated
	 *         string.
	 */
	public String getGebruiksmiddelenAlsString()
	{
		return StringUtil.toString(getOnderwijsproductGebruiksmiddelen(), "",
			new StringUtil.StringConverter<OnderwijsproductGebruiksmiddel>()
			{

				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(OnderwijsproductGebruiksmiddel object, int listIndex)
				{
					return object.getGebruiksmiddel().getNaam();
				}

			});
	}

	public List<OnderwijsproductVerbruiksmiddel> getOnderwijsproductVerbruiksmiddelen()
	{
		if (onderwijsproductVerbruiksmiddelen == null)
			onderwijsproductVerbruiksmiddelen = new ArrayList<OnderwijsproductVerbruiksmiddel>();
		return onderwijsproductVerbruiksmiddelen;
	}

	public void setOnderwijsproductVerbruiksmiddelen(
			List<OnderwijsproductVerbruiksmiddel> onderwijsproductVerbruiksmiddelen)
	{
		this.onderwijsproductVerbruiksmiddelen = onderwijsproductVerbruiksmiddelen;
	}

	/**
	 * @return De verbruiksmiddelen van dit onderwijsproduct als een comma-separated
	 *         string.
	 */
	public String getVerbruiksmiddelenAlsString()
	{
		return StringUtil.toString(getOnderwijsproductVerbruiksmiddelen(), "",
			new StringUtil.StringConverter<OnderwijsproductVerbruiksmiddel>()
			{

				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(OnderwijsproductVerbruiksmiddel object, int listIndex)
				{
					return object.getVerbruiksmiddel().getNaam();
				}

			});
	}

	public BigDecimal getKostprijs()
	{
		return kostprijs;
	}

	public void setKostprijs(BigDecimal kostprijs)
	{
		this.kostprijs = kostprijs;
	}

	public SoortOnderwijsproduct getSoortProduct()
	{
		return soortProduct;
	}

	public void setSoortProduct(SoortOnderwijsproduct soortProduct)
	{
		this.soortProduct = soortProduct;
	}

	public List<OnderwijsproductAanbod> getOnderwijsproductAanbodList()
	{
		if (onderwijsproductAanbodList == null)
			onderwijsproductAanbodList = new ArrayList<OnderwijsproductAanbod>();
		return onderwijsproductAanbodList;
	}

	public void setOnderwijsproductAanbodList(
			List<OnderwijsproductAanbod> onderwijsproductAanbodList)
	{
		this.onderwijsproductAanbodList = onderwijsproductAanbodList;
	}

	public List<OnderwijsproductTaxonomie> getOnderwijsproductTaxonomieList()
	{
		if (onderwijsproductTaxonomieList == null)
			onderwijsproductTaxonomieList = new ArrayList<OnderwijsproductTaxonomie>();
		return onderwijsproductTaxonomieList;
	}

	public void setOnderwijsproductTaxonomieList(
			List<OnderwijsproductTaxonomie> onderwijsproductTaxonomieList)
	{
		this.onderwijsproductTaxonomieList = onderwijsproductTaxonomieList;
	}

	public List<OnderwijsproductSamenstelling> getOnderdelen()
	{
		if (onderdelen == null)
			onderdelen = new ArrayList<OnderwijsproductSamenstelling>();
		return onderdelen;
	}

	public void setOnderdelen(List<OnderwijsproductSamenstelling> onderdelen)
	{
		this.onderdelen = onderdelen;
	}

	public List<OnderwijsproductSamenstelling> getOnderdeelVan()
	{
		if (onderdeelVan == null)
			onderdeelVan = new ArrayList<OnderwijsproductSamenstelling>();
		return onderdeelVan;
	}

	public void setOnderdeelVan(List<OnderwijsproductSamenstelling> onderdeelVan)
	{
		this.onderdeelVan = onderdeelVan;
	}

	public List<OnderwijsproductVoorwaarde> getVoorwaarden()
	{
		if (voorwaarden == null)
			return new ArrayList<OnderwijsproductVoorwaarde>();
		return voorwaarden;
	}

	public void setVoorwaarden(List<OnderwijsproductVoorwaarde> voorwaarden)
	{
		this.voorwaarden = voorwaarden;
	}

	public List<OnderwijsproductVoorwaarde> getVoorwaardelijkVoor()
	{
		if (voorwaardelijkVoor == null)
			return new ArrayList<OnderwijsproductVoorwaarde>();
		return voorwaardelijkVoor;
	}

	public void setVoorwaardelijkVoor(List<OnderwijsproductVoorwaarde> voorwaardelijkVoor)
	{
		this.voorwaardelijkVoor = voorwaardelijkVoor;
	}

	/**
	 * Uitbreiding onderwijscatalogus voor Amarantis
	 */
	public Boolean isIndividueel()
	{
		return individueel;
	}

	public Boolean isOnafhankelijk()
	{
		return onafhankelijk;
	}

	public Boolean isBegeleid()
	{
		return begeleid;
	}

	public String getVereisteVoorkennis()
	{
		return vereisteVoorkennis;
	}

	public BigDecimal getBelastingEC()
	{
		return belastingEC;
	}

	public BigDecimal getBelastingOverig()
	{
		return belastingOverig;
	}

	public String getLeerstofdrager()
	{
		return leerstofdrager;
	}

	public String getLiteratuur()
	{
		return literatuur;
	}

	public String getHulpmiddelen()
	{
		return hulpmiddelen;
	}

	public String getDocentactiviteiten()
	{
		return docentactiviteiten;
	}

	public Integer getFrequentiePerWeek()
	{
		return frequentiePerWeek;
	}

	public BigDecimal getTijdPerEenheid()
	{
		return tijdPerEenheid;
	}

	public Integer getAantalWeken()
	{
		return aantalWeken;
	}

	public void setIndividueel(Boolean individueel)
	{
		this.individueel = individueel;
	}

	public void setOnafhankelijk(Boolean onafhankelijk)
	{
		this.onafhankelijk = onafhankelijk;
	}

	public OnderwijsproductNiveauaanduiding getNiveauaanduiding()
	{
		return niveauaanduiding;
	}

	public void setNiveauaanduiding(OnderwijsproductNiveauaanduiding niveauaanduiding)
	{
		this.niveauaanduiding = niveauaanduiding;
	}

	public void setBegeleid(Boolean begeleid)
	{
		this.begeleid = begeleid;
	}

	public void setVereisteVoorkennis(String vereisteVoorkennis)
	{
		this.vereisteVoorkennis = vereisteVoorkennis;
	}

	public void setBelastingEC(BigDecimal belastingEC)
	{
		this.belastingEC = belastingEC;
	}

	public void setBelastingOverig(BigDecimal belastingOverig)
	{
		this.belastingOverig = belastingOverig;
	}

	public void setLeerstofdrager(String leerstofdrager)
	{
		this.leerstofdrager = leerstofdrager;
	}

	public void setLiteratuur(String literatuur)
	{
		this.literatuur = literatuur;
	}

	public void setHulpmiddelen(String hulpmiddelen)
	{
		this.hulpmiddelen = hulpmiddelen;
	}

	public void setDocentactiviteiten(String docentactiviteiten)
	{
		this.docentactiviteiten = docentactiviteiten;
	}

	public void setFrequentiePerWeek(Integer frequentiePerWeek)
	{
		this.frequentiePerWeek = frequentiePerWeek;
	}

	public void setTijdPerEenheid(BigDecimal tijdPerEenheid)
	{
		this.tijdPerEenheid = tijdPerEenheid;
	}

	public void setAantalWeken(Integer aantalWeken)
	{
		this.aantalWeken = aantalWeken;
	}

	public BigDecimal getOnderwijstijd()
	{
		if (getFrequentiePerWeek() != null && getTijdPerEenheid() != null
			&& getAantalWeken() != null)
		{
			BigDecimal totaal =
				getTijdPerEenheid().multiply(
					BigDecimal.valueOf(getFrequentiePerWeek()).multiply(
						BigDecimal.valueOf(getAantalWeken())));

			return totaal.setScale(2, RoundingMode.HALF_UP);
		}
		return null;
	}

	/**
	 * @return De taxonomiecodes waaraan dit onderwijsproduct is gekoppeld als een
	 *         komma-separated string.
	 */
	public String getTaxonomiecodes()
	{
		return StringUtil.toString(getOnderwijsproductTaxonomieList(), "",
			new StringUtil.StringConverter<OnderwijsproductTaxonomie>()
			{

				@Override
				public String getSeparator(int listIndex)
				{
					return ", ";
				}

				@Override
				public String toString(OnderwijsproductTaxonomie object, int listIndex)
				{
					return object.getTaxonomieElement().getTaxonomiecode();
				}

			});
	}

	/**
	 * @return Een string met elk onderwijsproductaanbod in de vorm van
	 *         'Locatieafkorting'-'Organisatie-eenheidafkorting', gescheiden door komma's.
	 */
	public String getAanbodAlsString()
	{
		return StringUtil.toString(getOnderwijsproductAanbodList(), "",
			new StringUtil.StringConverter<OnderwijsproductAanbod>()
			{

				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(OnderwijsproductAanbod object, int listIndex)
				{
					if (object.getLocatie() != null)
					{
						return object.getLocatie().getAfkorting() + "-"
							+ object.getOrganisatieEenheid().getAfkorting();
					}
					else
					{
						return object.getOrganisatieEenheid().getAfkorting();
					}
				}

			});
	}

	/**
	 * @return Een string met de codes van de onderdelen (paklijst) van dit
	 *         onderwijsproduct, gescheiden door komma's.
	 */
	public String getOnderdelenAlsString()
	{
		return StringUtil.toString(getOnderdelen(), "",
			new StringUtil.StringConverter<OnderwijsproductSamenstelling>()
			{

				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(OnderwijsproductSamenstelling object, int listIndex)
				{
					return object.getChild().getCode();
				}

			});
	}

	/**
	 * @return Een string met de codes van de onderwijsproducten waarvan dit
	 *         onderwijsproduct een onderdeel is, gescheiden door komma's.
	 */
	public String getOnderdeelVanAlsString()
	{
		return StringUtil.toString(getOnderdeelVan(), "",
			new StringUtil.StringConverter<OnderwijsproductSamenstelling>()
			{

				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(OnderwijsproductSamenstelling object, int listIndex)
				{
					return object.getParent().getCode();
				}

			});
	}

	/**
	 * @return Een string met de codes van de onderwijsproducten die voorwaardelijke
	 *         onderwijsproducten zijn van dit onderwijsproduct, gescheiden door komma's.
	 */
	public String getVoorwaardenAlsString()
	{
		return StringUtil.toString(getVoorwaarden(), "",
			new StringUtil.StringConverter<OnderwijsproductVoorwaarde>()
			{

				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(OnderwijsproductVoorwaarde object, int listIndex)
				{
					return object.getVoorwaardelijkProduct().getCode();
				}

			});
	}

	/**
	 * @return Een string met de codes van de onderwijsproducten waarvan dit
	 *         onderwijsproduct een voorwaarde voor is, gescheiden door komma's.
	 */
	public String getVoorwaardelijkVoorAlsString()
	{
		return StringUtil.toString(getVoorwaardelijkVoor(), "",
			new StringUtil.StringConverter<OnderwijsproductVoorwaarde>()
			{

				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(OnderwijsproductVoorwaarde object, int listIndex)
				{
					return object.getVoorwaardeVoor().getCode();
				}

			});
	}

	/**
	 * @return Een string met de codes van de onderwijsproducten waarvan dit
	 *         onderwijsproduct een voorwaarde voor is, gescheiden door komma's.
	 */
	public String getZoektermenAlsString()
	{
		return StringUtil.toString(getZoektermen(), "",
			new StringUtil.StringConverter<OnderwijsproductZoekterm>()
			{

				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(OnderwijsproductZoekterm object, int listIndex)
				{
					return object.getZoekterm();
				}

			});
	}

	/**
	 * Van de zoektermenString die door ',' gescheiden moet zijn worden
	 * {@link OnderwijsproductZoekterm} gemaakt. Eventuele nieuwe zoektermen worden
	 * automatisch aangemaakt. De zoektermen worden niet opgeslagen, dit moet nog door de
	 * aanroeper gedaan worden.
	 */
	public void setZoektermenAlsString(String zoektermenString)
	{

		if (StringUtil.isNotEmpty(zoektermenString))
		{
			String zoektermenArray[] = zoektermenString.split(",", -1);
			for (String string : zoektermenArray)
			{
				if (StringUtil.isNotEmpty(string))
				{
					createZoektermAlsNietBestaat(string);
				}
			}
		}
	}

	private void createZoektermAlsNietBestaat(String string)
	{
		boolean bestaat = false;
		string.trim();
		for (OnderwijsproductZoekterm zoekterm : getZoektermen())
		{
			if (zoekterm.getZoekterm().equals(string))
			{
				bestaat = true;
				break;
			}
		}
		if (!bestaat)
		{
			OnderwijsproductZoekterm zoekterm = new OnderwijsproductZoekterm(this);
			zoekterm.setZoekterm(string);
			getZoektermen().add(zoekterm);
		}
	}

	@Override
	public String getContextInfoOmschrijving()
	{
		return getTitel();
	}

	/**
	 * @return true als dit element verwijderbaar is, en anders false. In dit geval wordt
	 *         true teruggegeven als het element niet in gebruik is.
	 */
	public boolean isVerwijderbaar()
	{
		if (!isSaved())
			return false;
		if (!getOnderwijsproductTaxonomieList().isEmpty())
		{
			return false;
		}
		if (!getOnderdelen().isEmpty())
		{
			return false;
		}
		if (!getOnderwijsproductVerbruiksmiddelen().isEmpty())
		{
			return false;
		}
		if (!getOnderwijsproductGebruiksmiddelen().isEmpty())
		{
			return false;
		}
		if (!getOnderwijsproductAanbodList().isEmpty())
		{
			return false;
		}
		if (!getOnderdeelVan().isEmpty())
		{
			return false;
		}
		if (!getVoorwaarden().isEmpty())
		{
			return false;
		}
		if (!getVoorwaardelijkVoor().isEmpty())
		{
			return false;
		}
		if (!getZoektermen().isEmpty())
		{
			return false;
		}
		// Zoek naar opleidingen e.d. die hiernaar verwijzen.
		return !DataAccessRegistry.getHelper(OnderwijsproductDataAccessHelper.class).isInGebruik(
			this);

	}

	/**
	 * Verwijdert het gegeven taxonomie-element van dit onderwijsproduct (indien het aan
	 * het onderwijsproduct gekoppeld is). Aanroeper is verantwoordelijk voor het
	 * committen van de transactie.
	 * 
	 * @param element
	 */
	public void deleteOnderwijsproductTaxonomie(TaxonomieElement element)
	{
		OnderwijsproductTaxonomie teVerwijderen = null;
		for (OnderwijsproductTaxonomie ondTax : getOnderwijsproductTaxonomieList())
		{
			if (ondTax.getTaxonomieElement().equals(element))
			{
				teVerwijderen = ondTax;
				break;
			}
		}
		if (teVerwijderen != null)
		{
			getOnderwijsproductTaxonomieList().remove(teVerwijderen);
			teVerwijderen.delete();
		}
	}

	/**
	 * Verwijdert de gegeven voorwaarde van dit onderwijsproduct. Aanroeper is
	 * verantwoordelijk voor het committen van de transactie.
	 * 
	 * @param voorwaarde
	 */
	public void deleteVoorwaarde(Onderwijsproduct voorwaarde)
	{
		OnderwijsproductVoorwaarde teVerwijderen = null;
		for (OnderwijsproductVoorwaarde onderwijsprVoorw : getVoorwaarden())
		{
			if (onderwijsprVoorw.getVoorwaardelijkProduct().equals(voorwaarde))
			{
				teVerwijderen = onderwijsprVoorw;
				break;
			}
		}
		if (teVerwijderen != null)
		{
			getVoorwaarden().remove(teVerwijderen);
			teVerwijderen.delete();
		}
	}

	/**
	 * Verwijdert de gegeven opvolger van dit onderwijsproduct. Aanroeper is
	 * verantwoordelijk voor het committen van de transactie.
	 * 
	 * @param opvolger
	 */
	public void deleteOpvolger(Onderwijsproduct opvolger)
	{
		OnderwijsproductOpvolger teVerwijderen = null;
		for (OnderwijsproductOpvolger onderwijsprOpvolger : getOpvolgers())
		{
			if (onderwijsprOpvolger.getNieuwProduct().equals(opvolger))
			{
				teVerwijderen = onderwijsprOpvolger;
				break;
			}
		}
		if (teVerwijderen != null)
		{
			getOpvolgers().remove(teVerwijderen);
			teVerwijderen.delete();
		}
	}

	/**
	 * Verwijdert de gegeven onderdeel van dit onderwijsproduct. Aanroeper is
	 * verantwoordelijk voor het committen van de transactie.
	 * 
	 * @param onderdeel
	 */
	public void deleteOnderdeel(Onderwijsproduct onderdeel)
	{
		OnderwijsproductSamenstelling teVerwijderen = null;
		for (OnderwijsproductSamenstelling samenstelling : getOnderdelen())
		{
			if (samenstelling.getChild().equals(onderdeel))
			{
				teVerwijderen = samenstelling;
				break;
			}
		}
		if (teVerwijderen != null)
		{
			getOnderdelen().remove(teVerwijderen);
			teVerwijderen.delete();
		}

	}

	public boolean bestaatTaxonomieElementKoppeling(TaxonomieElement taxonomieElement)
	{
		for (OnderwijsproductTaxonomie onderwijsprTax : getOnderwijsproductTaxonomieList())
		{
			if (onderwijsprTax.getTaxonomieElement().equals(taxonomieElement))
				return true;
		}
		return false;
	}

	public boolean isVoorwaarde(Onderwijsproduct voorwaarde)
	{
		OnderwijsproductDataAccessHelper helper =
			DataAccessRegistry.getHelper(OnderwijsproductDataAccessHelper.class);
		if (helper.getVoorwaarden(this).contains(voorwaarde))
			return true;
		return false;
	}

	public boolean isOnderdeel(Onderwijsproduct onderdeel)
	{
		OnderwijsproductDataAccessHelper helper =
			DataAccessRegistry.getHelper(OnderwijsproductDataAccessHelper.class);
		if (helper.getChildren(this).contains(onderdeel))
			return true;
		return false;
	}

	/**
	 * Verwijdert het gegeven gebruiksmiddel van dit onderwijsproduct. Aanroeper is
	 * verantwoordelijk voor het committen.
	 * 
	 * @param gebruiksmiddel
	 */
	public void deleteGebruiksmiddel(Gebruiksmiddel gebruiksmiddel)
	{
		OnderwijsproductGebruiksmiddel teVerwijderen = null;
		for (OnderwijsproductGebruiksmiddel middel : getOnderwijsproductGebruiksmiddelen())
		{
			if (middel.getGebruiksmiddel().equals(gebruiksmiddel))
			{
				teVerwijderen = middel;
				break;
			}
		}
		if (teVerwijderen != null)
		{
			getOnderwijsproductGebruiksmiddelen().remove(teVerwijderen);
			teVerwijderen.delete();
		}
	}

	/**
	 * @param gebruiksmiddel
	 * @return true als het gegeven gebruiksmiddel al aan dit onderwijsproduct is
	 *         gekoppeld.
	 */
	public boolean bestaatGebruiksmiddel(Gebruiksmiddel gebruiksmiddel)
	{
		for (OnderwijsproductGebruiksmiddel middel : getOnderwijsproductGebruiksmiddelen())
		{
			if (middel.getGebruiksmiddel().equals(gebruiksmiddel))
				return true;
		}
		return false;
	}

	/**
	 * Verwijdert het gegeven gebruiksmiddel van dit onderwijsproduct. Aanroeper is
	 * verantwoordelijk voor het committen van de transactie.
	 * 
	 * @param verbruiksmiddel
	 */
	public void deleteVerbruiksmiddel(Verbruiksmiddel verbruiksmiddel)
	{
		OnderwijsproductVerbruiksmiddel teVerwijderen = null;
		for (OnderwijsproductVerbruiksmiddel middel : getOnderwijsproductVerbruiksmiddelen())
		{
			if (middel.getVerbruiksmiddel().equals(verbruiksmiddel))
			{
				teVerwijderen = middel;
				break;
			}
		}
		if (teVerwijderen != null)
		{
			getOnderwijsproductVerbruiksmiddelen().remove(teVerwijderen);
			teVerwijderen.delete();

		}
	}

	/**
	 * @param verbruiksmiddel
	 * @return true als het gegeven verbruiksmiddel al gekoppeld is aan dit
	 *         onderwijsproduct.
	 */
	public boolean bestaatVerbruiksmiddel(Verbruiksmiddel verbruiksmiddel)
	{
		for (OnderwijsproductVerbruiksmiddel middel : getOnderwijsproductVerbruiksmiddelen())
		{
			if (middel.getVerbruiksmiddel().equals(verbruiksmiddel))
				return true;
		}
		return false;
	}

	public List<OnderwijsproductBijlage> getBijlagen()
	{
		if (bijlagen == null)
			bijlagen = new ArrayList<OnderwijsproductBijlage>();
		return bijlagen;
	}

	public void setBijlagen(List<OnderwijsproductBijlage> bijlagen)
	{
		this.bijlagen = bijlagen;
	}

	/**
	 * Verwijdert de gegeven bijlage van dit onderwijsproduct. Aanroeper is
	 * verantwoordelijk voor het committen.
	 * 
	 * @param bijlage
	 */
	public void deleteBijlage(Bijlage bijlage)
	{
		OnderwijsproductBijlage teVerwijderen = null;
		for (OnderwijsproductBijlage ondProdbijlage : getBijlagen())
		{
			if (ondProdbijlage.getBijlage().equals(bijlage))
			{
				teVerwijderen = ondProdbijlage;
				break;
			}
		}
		if (teVerwijderen != null)
		{
			getBijlagen().remove(teVerwijderen);
			teVerwijderen.delete();
		}
	}

	public boolean bestaatBijlage(Bijlage bijlage)
	{
		for (OnderwijsproductBijlage ondProdbijlage : getBijlagen())
		{
			if (ondProdbijlage.getBijlage().equals(bijlage))
				return true;
		}
		return false;
	}

	public OnderwijsproductBijlage addBijlage(Bijlage bijlage)
	{
		OnderwijsproductBijlage newBijlage = new OnderwijsproductBijlage();
		newBijlage.setBijlage(bijlage);
		newBijlage.setOnderwijsproduct(this);

		getBijlagen().add(newBijlage);

		return newBijlage;
	}

	/**
	 * @return code + titel
	 */
	@Override
	public String toString()
	{
		return getCode() + " - " + getTitel();
	}

	public String getCodeTitelEnExterneCode()
	{
		String res = getCode() + " - " + getTitel();
		if (!getOnderwijsproductTaxonomieList().isEmpty())
		{
			res = res + " (";
			boolean first = true;
			for (OnderwijsproductTaxonomie ot : getOnderwijsproductTaxonomieList())
			{
				if (ot.getTaxonomieElement().getExterneCode() != null)
				{
					if (!first)
						res = res + ", ";
					first = false;
					res = res + ot.getTaxonomieElement().getExterneCode();
				}
			}
			res = res + ")";
		}
		return res;
	}

	public List<OnderwijsproductAfname> getAfnames()
	{
		if (afnames == null)
			afnames = new ArrayList<OnderwijsproductAfname>();
		return afnames;
	}

	public void setAfnames(List<OnderwijsproductAfname> afnames)
	{
		this.afnames = afnames;
	}

	public Onderwijsproduct getCopy()
	{
		HibernateObjectCopyManager copyManager =
			new HibernateObjectCopyManager(Onderwijsproduct.class, OnderwijsproductAanbod.class,
				OnderwijsproductTaxonomie.class);
		return copyManager.copyObject(this);
	}

	/**
	 * 
	 * @return De externe code van het taxonomie-element dat aan dit onderwijsproduct
	 *         gekoppeld is. Deze methode geeft alleen een resultaat als er precies 1
	 *         taxonomie-element aan het onderwijsproduct gekoppeld is, of als er meerdere
	 *         taxonomie-elementen met precies dezelfde externe code aan het product
	 *         gekoppeld zijn (dus bijvoorbeeld 3 verschillende deelkwalificaties die
	 *         allemaal dezelfde crebo-code hebben).
	 */
	@Exportable
	public String getExterneCode()
	{
		if (getOnderwijsproductTaxonomieList().size() == 1)
		{
			return getOnderwijsproductTaxonomieList().get(0).getTaxonomieElement().getExterneCode();
		}
		else if (getOnderwijsproductTaxonomieList().size() > 1)
		{
			List<String> codes = new ArrayList<String>();
			for (OnderwijsproductTaxonomie onderwijsproductTaxonomie : getOnderwijsproductTaxonomieList())
			{
				String externeCode =
					onderwijsproductTaxonomie.getTaxonomieElement().getExterneCode();
				if (!codes.contains(externeCode))
				{
					codes.add(externeCode);
				}
			}
			if (codes.size() == 1)
				return codes.get(0);
		}
		return null;
	}

	/**
	 * 
	 * @return De taxonomiecode van het taxonomie-element dat aan dit onderwijsproduct
	 *         gekoppeld is. Deze methode geeft alleen een resultaat als er precies 1
	 *         taxonomie-element aan het onderwijsproduct gekoppeld is.
	 */
	public String getTaxonomiecode()
	{
		if (getOnderwijsproductTaxonomieList().size() == 1)
		{
			return getOnderwijsproductTaxonomieList().get(0).getTaxonomieElement()
				.getTaxonomiecode();
		}
		return null;
	}

	public boolean isHeeftWerkstuktitel()
	{
		return heeftWerkstuktitel;
	}

	public void setHeeftWerkstuktitel(boolean heeftWerkstuktitel)
	{
		this.heeftWerkstuktitel = heeftWerkstuktitel;
	}

	/**
	 * 
	 * @return Ja indien dit onderwijsproduct werkstuktitels heeft, en anders Nee.
	 */
	public String getHeeftWerkstuktitelOmschrijving()
	{
		return isHeeftWerkstuktitel() ? "Ja" : "Nee";
	}

	/**
	 * 
	 * @return De naam van het taxonomie-element dat aan dit onderwijsproduct is
	 *         gekoppeld. Geeft null terug als er geen of meer dan 1 taxonomie-elementen
	 *         met verschillende landelijke namen aan dit onderwijsproduct zijn gekoppeld.
	 */
	@Exportable
	public String getLandelijkeNaam()
	{
		if (getOnderwijsproductTaxonomieList().size() == 1)
		{
			return getOnderwijsproductTaxonomieList().get(0).getTaxonomieElement().getNaam();
		}
		else if (getOnderwijsproductTaxonomieList().size() > 1)
		{
			List<String> namen = new ArrayList<String>();
			for (OnderwijsproductTaxonomie onderwijsproductTaxonomie : getOnderwijsproductTaxonomieList())
			{
				String naam = onderwijsproductTaxonomie.getTaxonomieElement().getNaam();
				if (!namen.contains(naam))
				{
					namen.add(naam);
				}
			}
			if (namen.size() == 1)
				return namen.get(0);
		}
		return null;
	}

	@Override
	public int compareTo(Onderwijsproduct o)
	{
		if (o == null)
			return -1;
		return getCode().compareTo(o.getCode());
	}

	@Override
	public List<OnderwijsproductAanbod> getOrganisatieEenheidLocatieKoppelingen()
	{
		return getOnderwijsproductAanbodList();
	}

	public List<Resultaatstructuur> getResultaatstructuren()
	{
		return resultaatstructuren;
	}

	public void setResultaatstructuren(List<Resultaatstructuur> resultaatstructuren)
	{
		this.resultaatstructuren = resultaatstructuren;
	}

	public String getCodeAndTitle()
	{
		return getCode() + " " + getTitel();
	}

	/**
	 * @return een comma-separated string van alle schalen die aan de eindresultaten van
	 *         dit onderwijsproduct is gekoppeld.
	 */
	public String getSchaalNamen()
	{
		Set<Schaal> schalen = new HashSet<Schaal>();
		for (Resultaatstructuur structuur : getResultaatstructuren())
		{
			if (structuur.getEindresultaat() != null)
			{
				schalen.add(structuur.getEindresultaat().getSchaal());
			}
		}
		return StringUtil.toString(schalen, "", new PropertyStringConverter<Schaal>("naam"));
	}

	public boolean isGekoppeldAanCGOTaxonomie()
	{
		for (OnderwijsproductTaxonomie ot : getOnderwijsproductTaxonomieList())
		{
			if (ot.getTaxonomieElement().getTaxonomie().isCGO())
			{
				return true;
			}
		}
		return false;
	}

	public boolean isGekoppeldAanVOTaxonomie()
	{
		for (OnderwijsproductTaxonomie ot : getOnderwijsproductTaxonomieList())
		{
			if (ot.getTaxonomieElement().getTaxonomie().isVO())
			{
				return true;
			}
		}
		return false;
	}

	public boolean isGekoppeldAanEducatieTaxonomie()
	{
		for (OnderwijsproductTaxonomie ot : getOnderwijsproductTaxonomieList())
		{
			if (ot.getTaxonomieElement().getTaxonomie().isEducatie())
			{
				return true;
			}
		}
		return false;
	}

	public boolean isGekoppeldAanInburgeringTaxonomie()
	{
		for (OnderwijsproductTaxonomie ot : getOnderwijsproductTaxonomieList())
		{
			if (ot.getTaxonomieElement().getTaxonomie().isInburgering())
			{
				return true;
			}
		}
		return false;
	}

	public boolean isGekoppeldAanDeelKwalificatie()
	{
		for (OnderwijsproductTaxonomie ot : getOnderwijsproductTaxonomieList())
		{
			Entiteit taxElement = ot.getTaxonomieElement().doUnproxy();
			if (Deelkwalificatie.class.isAssignableFrom(taxElement.getClass()))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public List<OnderwijsproductVrijVeld> getVrijVelden()
	{
		if (vrijVelden == null)
			vrijVelden = new ArrayList<OnderwijsproductVrijVeld>();

		return vrijVelden;
	}

	@Override
	public List<OnderwijsproductVrijVeld> getVrijVelden(VrijVeldCategorie categorie)
	{
		List<OnderwijsproductVrijVeld> res = new ArrayList<OnderwijsproductVrijVeld>();
		for (OnderwijsproductVrijVeld pvv : getVrijVelden())
		{
			if (pvv.getVrijVeld().getCategorie().equals(categorie))
			{
				res.add(pvv);
			}
		}
		return res;
	}

	public boolean isBijIntake()
	{
		return bijIntake;
	}

	public void setBijIntake(boolean bijIntake)
	{
		this.bijIntake = bijIntake;
	}

	@Override
	public OnderwijsproductVrijVeld newVrijVeld()
	{
		OnderwijsproductVrijVeld pvv = new OnderwijsproductVrijVeld();
		pvv.setOnderwijsproduct(this);

		return pvv;
	}

	@Override
	public void setVrijVelden(List<OnderwijsproductVrijVeld> vrijvelden)
	{
		this.vrijVelden = vrijvelden;
	}

	@Exportable
	@Override
	public String getVrijVeldWaarde(String naam)
	{
		for (OnderwijsproductVrijVeld vrijVeld : vrijVelden)
			if (vrijVeld.getVrijVeld().getNaam().equals(naam))
				return vrijVeld.getOmschrijving();
		return null;
	}

	public boolean isAlleenExtern()
	{
		return alleenExtern;
	}

	public void setAlleenExtern(boolean alleenExtern)
	{
		this.alleenExtern = alleenExtern;
	}

	public String getAlleenExternOmschrijving()
	{
		return isAlleenExtern() ? "Ja" : "Nee";
	}

	public List<OnderwijsproductOpvolger> getOpvolgers()
	{
		if (opvolgers == null)
			opvolgers = new ArrayList<OnderwijsproductOpvolger>();
		return opvolgers;
	}

	public void setOpvolgers(List<OnderwijsproductOpvolger> opvolgers)
	{
		this.opvolgers = opvolgers;
	}

	public List<OnderwijsproductOpvolger> getOpvolgerVan()
	{
		if (opvolgerVan == null)
			opvolgerVan = new ArrayList<OnderwijsproductOpvolger>();
		return opvolgerVan;
	}

	public void setOpvolgerVan(List<OnderwijsproductOpvolger> opvolgerVan)
	{
		this.opvolgerVan = opvolgerVan;
	}

	public String getVOLeerweg()
	{
		String taxonomiecode = getTaxonomiecode();
		if (taxonomiecode != null)
		{
			if (taxonomiecode.startsWith("3.2.3"))
			{
				return "TL";
			}
			if (taxonomiecode.startsWith("3.2.4"))
			{
				return "KBL";
			}
			if (taxonomiecode.startsWith("3.2.5"))
			{
				return "BBL";
			}
		}
		return "";
	}

	public void setInternationaleTitel(String internationaleTitel)
	{
		this.internationaleTitel = internationaleTitel;
	}

	public String getInternationaleTitel()
	{
		if (StringUtil.isNotEmpty(internationaleTitel))
			return internationaleTitel;

		return titel;
	}

	public void setInternationaleOmschrijving(String internationaleOmschrijving)
	{
		this.internationaleOmschrijving = internationaleOmschrijving;
	}

	public String getInternationaleOmschrijving()
	{
		if (StringUtil.isNotEmpty(internationaleOmschrijving))
			return internationaleOmschrijving;

		return omschrijving;
	}

	public void setCredits(Integer credits)
	{
		this.credits = credits;
	}

	public Integer getCredits()
	{
		return credits;
	}

	public void setBpvCriteria(List<BPVCriteriaOnderwijsproduct> bpvCriteria)
	{
		this.bpvCriteria = bpvCriteria;
	}

	public List<BPVCriteriaOnderwijsproduct> getBpvCriteria()
	{
		return bpvCriteria;
	}

	public List<Onderwijsproduct> getAlleOnderdelen()
	{
		List<Onderwijsproduct> ret = new ArrayList<Onderwijsproduct>();
		addAlleOnderdelen(this, ret);
		return ret;
	}

	private void addAlleOnderdelen(Onderwijsproduct onderwijsproduct, List<Onderwijsproduct> list)
	{
		for (OnderwijsproductSamenstelling os : onderwijsproduct.getOnderdelen())
		{
			if (!list.contains(os.getChild()))
			{
				list.add(os.getChild());
				addAlleOnderdelen(os.getChild(), list);
			}
		}
	}

	public List<Onderwijsproduct> getAlleVoorwaarden()
	{
		List<Onderwijsproduct> ret = new ArrayList<Onderwijsproduct>();
		addAlleVoorwaarden(this, ret);
		return ret;
	}

	private void addAlleVoorwaarden(Onderwijsproduct onderwijsproduct, List<Onderwijsproduct> list)
	{
		for (OnderwijsproductVoorwaarde ov : onderwijsproduct.getVoorwaarden())
		{
			if (!list.contains(ov.getVoorwaardelijkProduct()))
			{
				list.add(ov.getVoorwaardelijkProduct());
				addAlleVoorwaarden(ov.getVoorwaardelijkProduct(), list);
			}
		}
	}
}
