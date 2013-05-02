/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.personen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import nl.topicus.cobra.comparators.MultiFieldComparator;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.FieldPersistance;
import nl.topicus.cobra.entities.FieldPersistenceMode;
import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.cobra.web.components.labels.PasswordLabel;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.components.text.ReadonlyTextField;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.NummerGeneratorDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.entities.AutomatischeIncasso;
import nl.topicus.eduarte.entities.Betaalwijze;
import nl.topicus.eduarte.entities.Contacteerbaar;
import nl.topicus.eduarte.entities.ContacteerbaarUtil;
import nl.topicus.eduarte.entities.Debiteur;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.adres.Adresseerbaar;
import nl.topicus.eduarte.entities.adres.AdresseerbaarUtil;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.hogeronderwijs.Correspondentietaal;
import nl.topicus.eduarte.entities.landelijk.Gemeente;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.landelijk.Nationaliteit;
import nl.topicus.eduarte.entities.landelijk.Plaats;
import nl.topicus.eduarte.entities.landelijk.Verblijfsvergunning;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.settings.DebiteurNummerSetting;
import nl.topicus.eduarte.entities.vrijevelden.PersoonVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.providers.EmailProvider;
import nl.topicus.eduarte.providers.PersoonProvider;
import nl.topicus.eduarte.web.components.quicksearch.gemeente.GemeenteSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.plaats.PlaatsSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.voorvoegsel.VoorvoegselSearchEditor;
import nl.topicus.eduarte.web.components.text.GeboortedatumField;
import nl.topicus.eduarte.web.components.text.GeboortedatumLabel;
import nl.topicus.onderwijs.duo.bron.Bron;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.CumiCategorie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.CumiRatio;
import nl.topicus.onderwijs.duo.criho.annot.Criho;

import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Een persoon kan een medewerker of een deelnemer zijn.
 * <p>
 * <h2>Namen</h2>
 * <p>
 * Het is een enorm gezeik met de naam. Hier is de officiele regeling met betrekking tot
 * namen: Het veld officiele naam is ook bekend als de geboortenaam. Dit is de naam die
 * een persoon had voor een huwelijk de achternaam heeft doen veranderen. Voorheen was dit
 * ook bekend als meisjesnaam, maar omdat we kennelijk in een homodictatuur (aldus de
 * Telegraaf) leven, dekt *meisjes*naam de lading niet.
 * <p>
 * De naamvelden zonder prefix officieel zijn de aanspreeknamen. Deze worden in het
 * dagelijks leven gebruikt en ook getoond in de applicatie. Je zal dus normaal gesproken
 * het veld achternaam moeten tonen ipv officieleAchternaam. Bij twijfel: achternaam ipv
 * officieleAchternaam.
 * <p>
 * Bij de intake wizard wordt de officieleAchternaam velden ingevuld, en de waarden ook in
 * het veld achternaam gekopieerd. Als de aanspreeknaam anders is, dan kan de gebruiker
 * deze apart invullen.
 * <p>
 * Dit is ook <a href="http://taalunieversum.org/taalunie/hoe_noem_je_de_familienaam_die_iemand_gebruikte_vr_het_huwelijk/"
 * >gedocumenteerd door de Nederlandse Taalunie</a>.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Exportable
@BatchSize(size = 20)
@IsViewWhenOnNoise
public class Persoon extends InstellingEntiteit implements Comparable<Persoon>,
		Adresseerbaar<PersoonAdres>, Contacteerbaar<PersoonContactgegeven>,
		VrijVeldable<PersoonVrijVeld>, IBijlageKoppelEntiteit<PersoonBijlage>, Debiteur,
		PersoonProvider, EmailProvider
{
	private static final long serialVersionUID = 1L;

	@Column(length = 24, nullable = true)
	@AutoForm(label = "Voorletter(s)", htmlClasses = "unit_max")
	@Criho
	private String voorletters;

	@Column(length = 80, nullable = true)
	@AutoForm(label = "Voorna(a)m(en)", htmlClasses = "unit_max")
	@Bron
	@Criho
	private String voornamen;

	@Column(length = 40, nullable = true)
	@AutoForm(htmlClasses = "unit_max")
	private String roepnaam;

	@Column(name = "voorvoegsel", nullable = true)
	@AutoForm(label = "Voorvoegsel(s)", editorClass = VoorvoegselSearchEditor.class, htmlClasses = "unit_max")
	private String voorvoegsel;

	@Column(length = 80, nullable = false)
	@Index(name = "idx_Persoon_achternaam")
	@AutoForm(htmlClasses = "unit_max", label = "Aanspreekachternaam", description = "De achternaam zoals deze normaliter gehanteerd wordt in het systeem, correspondentie en overzichten. Standaard is deze naam dezelfde als de geboortenaam.")
	private String achternaam;

	@Column(length = 80, nullable = false)
	@Index(name = "idx_Persoon_lowerachter")
	private String lowercaseAchternaam;

	@Column(length = 165, nullable = false)
	@Index(name = "idx_Persoon_berekendenaam")
	private String berekendeZoeknaam;

	@Bron
	@Criho
	@Column(length = 20, nullable = true)
	@AutoForm(label = "Geboortevoorvoegsel(s)", editorClass = VoorvoegselSearchEditor.class, htmlClasses = "unit_max")
	private String officieleVoorvoegsel;

	@Bron
	@Criho
	@Column(length = 80, nullable = false)
	@Index(name = "idx_Persoon_offAchternaam")
	@AutoForm(htmlClasses = "unit_max", label = "Geboortenaam", description = "De offici&euml;le achternaam van deze persoon volgens de GBA. Bij getrouwde vrouwen is dit de meisjesnaam. Deze naam wordt gebruikt binnen offici&euml;le documenten zoals diploma's en communicatie met de overheid.")
	private String officieleAchternaam;

	@XmlType(name = "ToepassingGeboortedatum")
	@XmlEnum
	public static enum ToepassingGeboortedatum
	{
		/**
		 * Maand en geboortejaar gebruiken. 01-01-2009 wordt 00-10-2009.
		 */
		@XmlEnumValue("00-MM-JJJJ")
		GeboortemaandEnJaar
		{
			@Override
			public String toString()
			{
				return "Geboortemaand en -jaar";
			}
		},
		/**
		 * Alleen geboortejaar gebruiken. 01-01-2009 wordt 00-00-2009.
		 */
		@XmlEnumValue("00-00-JJJJ")
		Geboortejaar;
	}

	/**
	 * nullable is true omdat de persoon opgeslagen moet kunnen worden, zonder dat de
	 * verbintenis definitief is. Dit veld is pas verplicht als de verbintenis definitief
	 * wordt.
	 * 
	 * @mantis 37579
	 */
	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	@Index(name = "idx_Persoon_geboortedatum")
	@AutoForm(displayClass = GeboortedatumLabel.class, editorClass = GeboortedatumField.class)
	@Bron(sleutel = true, verplicht = true)
	@Criho
	private Date geboortedatum;

	@Bron(sleutel = true)
	@Criho
	@Enumerated(EnumType.STRING)
	@Column(nullable = true)
	// @AutoForm(label = "Toepassing geb. datum", description =
	// "Geeft aan welk deel van de geboortedatum gebruikt moet worden in communicatie met BRON."
	// )
	private ToepassingGeboortedatum toepassingGeboortedatum;

	/**
	 * nullable is true omdat de persoon opgeslagen moet kunnen worden, zonder dat de
	 * verbintenis definitief is. Dit veld is pas verplicht als de verbintenis definitief
	 * wordt.
	 * 
	 * @mantis 37579
	 */
	@Column(nullable = true)
	@Index(name = "idx_Persoon_geslacht")
	@Enumerated(value = EnumType.STRING)
	@AutoForm(htmlClasses = "unit_100")
	@Bron(sleutel = true, verplicht = true)
	@Criho
	private Geslacht geslacht;

	@Column(nullable = true)
	@Index(name = "idx_Persoon_bsn")
	@AutoForm(label = "BSN", editorClass = TextField.class, htmlClasses = "unit_max")
	@Bron(sleutel = true)
	private Long bsn;

	@Column(nullable = true)
	@AutoForm(editorClass = ReadonlyTextField.class, htmlClasses = "unit_max", description = "Het unieke debiteurennummer van deze persoon")
	private Long debiteurennummer;

	@Column(nullable = true)
	private Date laatsteExportDatum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nationaliteit1", nullable = true)
	@Index(name = "idx_Persoon_nationaliteit1")
	@AutoForm(label = "1e Nationaliteit", htmlClasses = "unit_max")
	private Nationaliteit nationaliteit1;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nationaliteit2", nullable = true)
	@Index(name = "idx_Persoon_nationaliteit2")
	@AutoForm(label = "2e Nationaliteit", htmlClasses = "unit_max")
	private Nationaliteit nationaliteit2;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	@AutoForm(editorClass = DatumField.class, htmlClasses = "unit_80", label = "Datum in Nederland")
	private Date datumInNederland;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geboorteGemeente", nullable = true)
	@Index(name = "idx_Persoon_geboorteGemeente")
	@AutoForm(editorClass = GemeenteSearchEditor.class, label = "Geboortegemeente", description = "Gemeente waar de persoon geboren is. Deze gemeente wordt onder meer afgedrukt op diploma's.", htmlClasses = "unit_max")
	private Gemeente geboorteGemeente;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geboorteland", nullable = true)
	@Index(name = "idx_Persoon_geboorteland")
	@AutoForm(htmlClasses = "unit_max")
	private Land geboorteland;

	/**
	 * Voor bijvoorbeeld personen die in het buitenland zijn geboren.
	 */
	@Column(nullable = true)
	@Index(name = "idx_Persoon_geboorteplaats")
	@AutoForm(label = "Geboren te", htmlClasses = "unit_max", description = "Plaats of gemeente waar de persoon geboren is. Deze plaats wordt onder meer afgedrukt op diploma's.", editorClass = PlaatsSearchEditor.class)
	private String geboorteplaats;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	@AutoForm(editorClass = DatumField.class)
	private Date datumOverlijden;

	@BatchSize(size = 20)
	@Bron
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "persoon")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@OrderBy(value = "begindatum DESC")
	private List<PersoonAdres> adressen = new ArrayList<PersoonAdres>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "persoon")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@OrderBy(value = "volgorde")
	@BatchSize(size = 20)
	private List<PersoonContactgegeven> contactgegevens = new ArrayList<PersoonContactgegeven>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "deelnemer")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	private List<AbstractRelatie> relaties = new ArrayList<AbstractRelatie>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "afbeelding", nullable = true)
	@Index(name = "idx_Persoon_afbeelding")
	private PersoonBijlage afbeelding;

	@Column
	@Enumerated(value = EnumType.STRING)
	@AutoForm(htmlClasses = "unit_max")
	private BurgerlijkeStaat burgerlijkeStaat;

	@Column(length = 11, nullable = true)
	@AutoForm(label = "Rekeningnummer", description = "Rekeningnummer tbv automatische incasso of voordrukken op Acceptgiro")
	private String bankrekeningnummer;

	@Column(length = 60, nullable = true)
	@AutoForm(htmlClasses = "unit_max", label = "Bankrek. tenaamstelling", description = "Tenaamstelling van de bankrekening tbv automatische incasso, indien afwijkend van naam debiteur")
	private String bankrekeningTenaamstelling;

	/**
	 * Unordered voor joins
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "persoon")
	@FieldPersistance(FieldPersistenceMode.SKIP)
	private List<PersoonAdres> persoonAdressenUnordered = new ArrayList<PersoonAdres>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "persoon")
	@FieldPersistance(FieldPersistenceMode.SKIP)
	private List<PersoonContactgegeven> persoonTelefoonsUnordered =
		new ArrayList<PersoonContactgegeven>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "persoon")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	private List<PersoonVrijVeld> vrijVelden = new ArrayList<PersoonVrijVeld>();

	/**
	 * Mapping ipv keiharde query, aangezien we dit ook in unit tests willen kunnen
	 * blijven gebruiken. Een <tt>@OneToMany</tt> levert niet die vervelende extra query
	 * op die Olav heeft weggewerkt, zoals bij de <tt>OneToOne</tt> mapping gebeurt. In
	 * afwachting van de oplossing voor <a
	 * href="http://opensource.atlassian.com/projects/hibernate/browse/HHH-3930"
	 * >HHH-3930</a>, de bug die we aangemeld hebben voor dit euvel.
	 */
	// FIXME aanpassen wanneer HHH-3930 opgelost is.
	@OneToMany(mappedBy = "persoon", fetch = FetchType.LAZY)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	@RestrictedAccess(hasGetter = false, hasSetter = false)
	@FieldPersistance(FieldPersistenceMode.SKIP)
	private List<Deelnemer> deelnemers = new ArrayList<Deelnemer>();

	@Bron
	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	@AutoForm(readOnly = true)
	private CumiRatio cumiRatio;

	@Bron
	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	@AutoForm(readOnly = true)
	private CumiCategorie cumiCategorie;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "persoon")
	private List<PersoonBijlage> bijlagen = new ArrayList<PersoonBijlage>();

	/**
	 * De indiciatie die aangeeft of de deelnemer door de gemeente als inburgeraar is
	 * aangemeld. Nodig bij BRON communicatie voor VAVO en ED.
	 */
	@Column(nullable = false)
	@Bron
	@AutoForm(description = "Indicatie die aangeeft of de deelnemer door de gemeente als inburgeraar is aangemeld.", label = "Inburgeraar")
	private boolean nieuwkomer;

	@Column(nullable = false)
	@AutoForm(description = "Indicatie die aangeeft of informatie over deze deelnemer verstrekt mag worden aan derden", label = "Informatie niet verstrekken aan derden")
	private boolean nietVerstrekkenAanDerden;

	@Enumerated(EnumType.STRING)
	@Column(length = 20, nullable = false)
	private AutomatischeIncasso automatischeIncasso = AutomatischeIncasso.Geen;

	@Column(nullable = true)
	@AutoForm(description = "Einddatum van de machtiging. Na deze datum zullen geen bedragen automatisch meer worden geïncasseerd.", label = "Aut. incasso einddatum")
	private Date automatischeIncassoEinddatum;

	@Column(nullable = true)
	@AutoForm(description = "Specifieke betalingstermijn voor deze debiteur (in dagen).", label = "Betalingstermijn")
	private Integer betalingstermijn;

	/**
	 * Dit is niet een account wachtwoord, maar een (tijdelijk) wachtwoord dat door
	 * webservices wordt aangeboden voor communicatie met de deelnemer/medewerker. Plain
	 * tekst, heeft geen verdere betekenis binnen KRD.
	 */
	@AutoForm(description = "Extern wachtwoord dat niet gekoppeld is aan het account van deze persoon.", editorClass = PasswordTextField.class, displayClass = PasswordLabel.class)
	@Column(length = 128, nullable = true, name = "wachtwoord")
	private String wachtwoord;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geboortelandOuder1", nullable = true)
	@Index(name = "idx_Persoon_geboorteOuder1")
	private Land geboortelandOuder1;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geboortelandOuder2", nullable = true)
	@Index(name = "idx_Persoon_geboorteOuder2")
	private Land geboortelandOuder2;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "verblijfsvergunning", nullable = true)
	@Index(name = "idx_Persoon_verblijfsverg")
	private Verblijfsvergunning verblijfsvergunning;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@Index(name = "idx_Persoon_correspondentie")
	private Correspondentietaal correspondentietaal;

	@Column(length = 34, nullable = true)
	@AutoForm(label = "Buitenlands rekeningnummer")
	private String buitenlandsBankrekeningnummer;

	@Column(length = 50, nullable = true)
	private String buitenlandseBanknaam;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "landBank", nullable = true)
	@Index(name = "idx_Persoon_landBank")
	private Land landBank;

	@Enumerated(EnumType.STRING)
	@Column(length = 30, nullable = false)
	private Betaalwijze factuurBetaalwijze = Betaalwijze.OVERIG;

	public Persoon()
	{
	}

	@Override
	public String toString()
	{
		return getVolledigeNaam();
	}

	@Exportable
	public String getVoorletters()
	{
		return voorletters;
	}

	@Exportable
	public String getEersteVoorletter()
	{
		return voorletters.substring(0, 1);
	}

	public void setVoorletters(String voorletters)
	{
		this.voorletters = voorletters;
	}

	@Exportable
	public String getRoepnaam()
	{
		return roepnaam;
	}

	public void setRoepnaam(String roepnaam)
	{
		this.roepnaam = roepnaam;
		updateBerekendeZoeknaam();
	}

	@Exportable
	public String getVoorvoegsel()
	{
		return voorvoegsel;
	}

	public void setVoorvoegsel(String voorvoegsel)
	{
		this.voorvoegsel = voorvoegsel;
		updateBerekendeZoeknaam();
	}

	@Exportable
	public String getAchternaam()
	{
		return achternaam;
	}

	public void setAchternaam(String achternaam)
	{
		this.achternaam = achternaam;

		if (StringUtil.isEmpty(officieleAchternaam))
			officieleAchternaam = achternaam;
		updateBerekendeZoeknaam();
		setLowercaseAchternaam(achternaam == null ? null : achternaam.toLowerCase());
	}

	public String getLowercaseAchternaam()
	{
		return lowercaseAchternaam;
	}

	public void setLowercaseAchternaam(String lowercaseAchternaam)
	{
		this.lowercaseAchternaam = lowercaseAchternaam;
	}

	@Exportable
	public Date getGeboortedatum()
	{
		return geboortedatum;
	}

	@Exportable
	public String getGeboortedatumOfficieel()
	{
		if (getGeboortedatum() != null)
		{
			return new SimpleDateFormat("dd MMMMM yyyy", new Locale("nl", "NL"))
				.format(getGeboortedatum());
		}
		return "";
	}

	@Exportable
	public String getGeboortedatumOfficieelZonderVoorloopNul()
	{
		if (getGeboortedatum() != null)
		{
			return new SimpleDateFormat("d MMMMM yyyy", new Locale("nl", "NL"))
				.format(getGeboortedatum());
		}
		return "";
	}

	public void setGeboortedatum(Date geboortedatum)
	{
		this.geboortedatum = geboortedatum;
	}

	public void setToepassingGeboortedatum(ToepassingGeboortedatum toepassingGeboortedatum)
	{
		this.toepassingGeboortedatum = toepassingGeboortedatum;
	}

	public ToepassingGeboortedatum getToepassingGeboortedatum()
	{
		return toepassingGeboortedatum;
	}

	@Exportable
	public boolean isMeerderjarig()
	{
		Date huidigeDatum = TimeUtil.getInstance().currentDate();
		Date geboorteDatum = getGeboortedatum();

		int leeftijd = 0;

		if (geboorteDatum != null)
			leeftijd = TimeUtil.getInstance().getDifferenceInYears(huidigeDatum, geboorteDatum);

		return (leeftijd >= 18);
	}

	@Exportable
	public Nationaliteit getNationaliteit1()
	{
		return nationaliteit1;
	}

	public void setNationaliteit1(Nationaliteit nationaliteit1)
	{
		this.nationaliteit1 = nationaliteit1;
		// als nationaliteit1 veranderd, moet de cumi opnieuw berekend worden
		setCumi();
	}

	@Exportable
	public Nationaliteit getNationaliteit2()
	{
		return nationaliteit2;
	}

	public void setNationaliteit2(Nationaliteit nationaliteit2)
	{
		this.nationaliteit2 = nationaliteit2;
		// als nationaliteit2 veranderd, moet de cumi opnieuw berekend worden
		setCumi();
	}

	@Exportable
	public Date getDatumInNederland()
	{
		return datumInNederland;
	}

	public void setDatumInNederland(Date datumInNederland)
	{
		this.datumInNederland = datumInNederland;
		// als de datum in nederland veranderd, moet de cumi opnieuw berekend worden
		setCumi();
	}

	@Exportable
	public Gemeente getGeboorteGemeente()
	{
		if (geboorteGemeente != null)
			return geboorteGemeente;
		if (geboorteplaats != null && Land.getNederland().equals(geboorteland))
		{
			Plaats plaats = Plaats.get(geboorteplaats, null, null);
			if (plaats != null)
				return plaats.getGemeente();
		}
		return null;
	}

	public void setGeboorteGemeente(Gemeente geboorteGemeente)
	{
		this.geboorteGemeente = geboorteGemeente;
		if (geboorteGemeente != null && Land.getNederland().equals(geboorteland))
			this.geboorteplaats = geboorteGemeente.getNaam();
	}

	@Exportable
	@AutoForm(label = "Datum van overlijden")
	public Date getDatumOverlijden()
	{
		return datumOverlijden;
	}

	public void setDatumOverlijden(Date datumOverlijden)
	{
		this.datumOverlijden = datumOverlijden;
	}

	/**
	 * @return Het eerste PersoonAdres op de peildatum
	 */
	public PersoonAdres getEerstePersoonAdresOpPeildatum()
	{
		List<PersoonAdres> persoonadressenList = getFysiekAdressenOpPeildatum();
		if (!persoonadressenList.isEmpty())
		{
			return persoonadressenList.get(0);
		}
		else
		{
			return null;
		}
	}

	/**
	 * @return Volledige naam van deze persoon, dwz roepnaam + voorvoegsel + achternaam.
	 */
	@Exportable
	public String getVolledigeNaam()
	{
		StringBuilder res = new StringBuilder(50);
		if (StringUtil.isNotEmpty(getRoepnaam()))
		{
			res.append(getRoepnaam()).append(" ");
		}
		else if (StringUtil.isNotEmpty(getVoornamen()))
		{
			res.append(getVoornamen()).append(" ");
		}
		else if (StringUtil.isNotEmpty(getVoorletters()))
		{
			res.append(getVoorletters()).append(" ");
		}
		if (StringUtil.isNotEmpty(getVoorvoegsel()))
		{
			res.append(getVoorvoegsel()).append(" ");
		}
		res.append(getAchternaam());

		return res.toString();
	}

	/**
	 * 
	 * @return De volledige officiele naam van deze deelnemer voor bijvoorbeeld diploma's.
	 *         Hierbij wordt gebruik gemaakt van de officiele tussenvoegsel en achternaam
	 *         ipv de aanspreeknamen en van de volledige voornamen ipv de roepnaam.
	 */
	@Exportable
	public String getVolledigeNaamOfficieel()
	{
		StringBuilder res = new StringBuilder(50);
		if (StringUtil.isNotEmpty(getVoornamen()))
		{
			res.append(getVoornamen()).append(" ");
		}
		if (StringUtil.isNotEmpty(getOfficieleVoorvoegsel()))
		{
			res.append(getOfficieleVoorvoegsel()).append(" ");
		}
		res.append(getOfficieleAchternaam());

		return res.toString();
	}

	/**
	 * @return Formele naam van deze persoon, dwz voorletters + voorvoegsel + achternaam.
	 */
	@Exportable
	public String getFormeleNaam()
	{
		StringBuilder res = new StringBuilder(50);
		if (StringUtil.isNotEmpty(getVoorletters()))
		{
			res.append(getVoorletters()).append(" ");
		}
		if (StringUtil.isNotEmpty(getVoorvoegsel()))
		{
			res.append(getVoorvoegsel()).append(" ");
		}
		res.append(getAchternaam());

		return res.toString();
	}

	/**
	 * @return voorvoegsel + achternaam van deze persoon
	 */
	@AutoForm(label = "Achternaam")
	@Exportable
	public String getVoorvoegselAchternaam()
	{
		StringBuilder res = new StringBuilder(50);
		if (StringUtil.isNotEmpty(getVoorvoegsel()))
		{
			res.append(getVoorvoegsel()).append(" ");
		}
		res.append(getAchternaam());

		return res.toString();
	}

	@Exportable
	public String getAanhefNaam()
	{
		StringBuilder res = new StringBuilder(50);
		if (StringUtil.isNotEmpty(getVoorvoegsel()))
		{
			res.append(StringUtil.onlyFirstCharUppercase(getVoorvoegsel())).append(" ");
		}
		res.append(StringUtil.onlyFirstCharUppercase(getAchternaam()));

		return res.toString();
	}

	/**
	 * @return heer Van Dommelen
	 */
	@Exportable
	public String getAanhef()
	{
		StringBuilder res = new StringBuilder(50);

		if (geslacht != null)
			res.append(geslacht.getAanhef());
		else
			res.append(Geslacht.Onbekend.getAanhef());

		res.append(' ');
		res.append(getAanhefNaam());

		return res.toString();
	}

	/**
	 * @return Vermeer, J.P. of Dommelen, van
	 */
	@Exportable
	public String getAchternaamVoorletters()
	{
		StringBuilder res = new StringBuilder(50);
		res.append(achternaam);
		if (StringUtil.isNotEmpty(voorvoegsel) || StringUtil.isNotEmpty(voorletters))
		{
			res.append(", ");
			if (StringUtil.isNotEmpty(voorletters))
				res.append(voorletters).append(' ');
			if (StringUtil.isNotEmpty(voorvoegsel))
				res.append(voorvoegsel);
		}
		return res.toString();
	}

	/**
	 * @return De heer J.P. Vermeer of Mevrouw Van Dommelen
	 */
	@Exportable()
	public String getAdresseringNaam()
	{
		StringBuilder res = new StringBuilder(50);
		if (geslacht != null)
			res.append(geslacht.getAdressering());
		else
			res.append(Geslacht.Onbekend.getAdressering());

		res.append(' ');
		if (!StringUtil.isEmpty(voorletters))
		{
			res.append(voorletters);
			res.append(' ');
			res.append(getVoorvoegselAchternaam());
		}
		else
		{
			res.append(getAanhefNaam());
		}

		return res.toString();
	}

	public String getAchternaamKommaRoepnaam()
	{
		StringBuilder res = new StringBuilder();

		res.append(getAchternaam());
		res.append(", ");
		res.append(getRoepnaam());

		return res.toString();
	}

	public List<PersoonAdres> getPersoonAdressenUnordered()
	{
		return persoonAdressenUnordered;
	}

	public void setPersoonAdressenUnordered(List<PersoonAdres> persoonAdressenUnordered)
	{
		this.persoonAdressenUnordered = persoonAdressenUnordered;
	}

	public List<PersoonContactgegeven> getPersoonTelefoonsUnordered()
	{
		return persoonTelefoonsUnordered;
	}

	public void setPersoonTelefoonsUnordered(List<PersoonContactgegeven> persoonTelefoonsUnordered)
	{
		this.persoonTelefoonsUnordered = persoonTelefoonsUnordered;
	}

	@Override
	public int compareTo(Persoon o)
	{
		return new MultiFieldComparator("achternaam", "roepnaam", "geboortedatum", "id", "saved")
			.compare(this, o);
	}

	@Exportable
	public String getVoornamen()
	{
		return voornamen;
	}

	public void setVoornamen(String voornamen)
	{
		this.voornamen = voornamen;
	}

	public String getBerekendeZoeknaam()
	{
		return berekendeZoeknaam;
	}

	public void setBerekendeZoeknaam(String berekendeZoeknaam)
	{
		this.berekendeZoeknaam = berekendeZoeknaam;
	}

	private void updateBerekendeZoeknaam()
	{
		// lower(replace(roepnaam || ' ' || voorvoegsel || ' ' || achternaam, ' ', ' '))
		StringBuilder sb = new StringBuilder();
		if (!StringUtil.isEmpty(getRoepnaam()))
		{
			sb.append(getRoepnaam());
			sb.append(' ');
		}
		if (!StringUtil.isEmpty(getVoorvoegsel()))
		{
			sb.append(getVoorvoegsel());
			sb.append(' ');
		}
		if (!StringUtil.isEmpty(getAchternaam()))
			sb.append(getAchternaam());
		setBerekendeZoeknaam(sb.toString().toLowerCase());
	}

	@Exportable
	public String getOfficieleVoorvoegsel()
	{
		return officieleVoorvoegsel;
	}

	public void setOfficieleVoorvoegsel(String officieleVoorvoegsel)
	{
		this.officieleVoorvoegsel = officieleVoorvoegsel;
	}

	@Exportable
	public String getOfficieleAchternaam()
	{
		return officieleAchternaam;
	}

	public void setOfficieleAchternaam(String officieleAchternaam)
	{
		this.officieleAchternaam = officieleAchternaam;
	}

	/**
	 * In de applicatie wordt overal de aanspreeknaam (veld: achternaam) getoond. Met deze
	 * functie kun je bekijken of deze persoon ook een afwijkende geboortenaam heeft.
	 * Handig voor personaliaoverzicht en -bewerk pagina's.
	 */
	@Exportable
	public boolean isGeboortenaamWijktAf()
	{
		boolean voorvoegselIsGelijk =
			(getVoorvoegsel() != null && getOfficieleVoorvoegsel() != null && getVoorvoegsel()
				.equals(getOfficieleVoorvoegsel()))
				|| (getVoorvoegsel() == null && getOfficieleVoorvoegsel() == null);
		boolean achternaamIsGelijk =
			getAchternaam() != null && getOfficieleAchternaam() != null
				&& getAchternaam().equals(getOfficieleAchternaam())
				|| (getAchternaam() == null && getOfficieleAchternaam() == null);
		return !voorvoegselIsGelijk || !achternaamIsGelijk;
	}

	@Exportable
	public Geslacht getGeslacht()
	{
		return geslacht;
	}

	public void setGeslacht(Geslacht geslacht)
	{
		this.geslacht = geslacht;
	}

	@Exportable
	public String getGeboorteplaats()
	{
		return geboorteplaats;
	}

	public void setGeboorteplaats(String geboorteplaats)
	{
		this.geboorteplaats = geboorteplaats;
		if (geboorteplaats != null)
		{
			Plaats plaats = Plaats.get(geboorteplaats, null, null);
			if (plaats != null)
				this.geboorteGemeente = plaats.getGemeente();
		}
	}

	public PersoonBijlage getAfbeelding()
	{
		return afbeelding;
	}

	public void setAfbeelding(PersoonBijlage afbeelding)
	{
		this.afbeelding = afbeelding;
	}

	public byte[] getAfbeeldingBytes()
	{
		if (getAfbeelding() == null)
			return null;
		if (getAfbeelding().getBijlage() == null)
			return null;

		return getAfbeelding().getBijlage().getBestand();
	}

	@Exportable
	public BurgerlijkeStaat getBurgerlijkeStaat()
	{
		return burgerlijkeStaat;
	}

	public void setBurgerlijkeStaat(BurgerlijkeStaat burgerlijkeStaat)
	{
		this.burgerlijkeStaat = burgerlijkeStaat;
	}

	@Exportable
	public Land getGeboorteland()
	{
		return geboorteland;
	}

	public void setGeboorteland(Land geboorteland)
	{
		this.geboorteland = geboorteland;
	}

	@Exportable
	public Long getBsn()
	{
		return bsn;
	}

	public void setBsn(Long bsn)
	{
		this.bsn = bsn;
	}

	@Exportable
	public Long getDebiteurennummer()
	{
		return debiteurennummer;
	}

	public void setDebiteurennummer(Long debiteurennummer)
	{
		this.debiteurennummer = debiteurennummer;
	}

	public void setBankrekeningnummer(String bankrekeningnummer)
	{
		this.bankrekeningnummer = bankrekeningnummer;
	}

	@Exportable
	public String getBankrekeningnummer()
	{
		return bankrekeningnummer;
	}

	public void setRelaties(List<AbstractRelatie> relaties)
	{
		this.relaties = relaties;
	}

	@Exportable
	public List<AbstractRelatie> getRelaties()
	{
		return relaties;
	}

	public List<Relatie> getRelatiesRelatie()
	{
		List<Relatie> list = new ArrayList<Relatie>();

		for (AbstractRelatie relatie : getRelaties())
		{
			if (relatie instanceof Relatie)
			{
				list.add((Relatie) relatie);
			}
		}
		return list;
	}

	@Exportable
	public List<PersoonExterneOrganisatie> getRelatiesPersoonExterneOrganistie()
	{
		List<PersoonExterneOrganisatie> list = new ArrayList<PersoonExterneOrganisatie>();

		for (AbstractRelatie relatie : getRelaties())
		{
			if (relatie instanceof PersoonExterneOrganisatie)
			{
				list.add((PersoonExterneOrganisatie) relatie);
			}
		}
		return list;
	}

	@Override
	@Exportable
	public List<PersoonAdres> getAdressen()
	{
		return adressen;
	}

	@Override
	public void setAdressen(List<PersoonAdres> adressen)
	{
		this.adressen = adressen;
	}

	@Override
	@Exportable
	public List<PersoonAdres> getAdressenOpPeildatum()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil.createPeildatumFilter());
	}

	@Override
	public List<PersoonAdres> getFysiekAdressen()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil.createFysiekadresFilter());
	}

	public PersoonAdres getLaatsteAdres()
	{
		List<PersoonAdres> adressen1 = getFysiekAdressen();
		if (adressen1.size() > 0)
		{
			adressen1 = AdresseerbaarUtil.sortAdressenOpBegindatum(adressen1);
			return adressen1.get(adressen1.size() - 1);
		}
		return null;
	}

	@Override
	public List<PersoonAdres> getFysiekAdressenOpPeildatum()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil
			.createFysiekadresOpPeildatumFilter());
	}

	@Override
	public List<PersoonAdres> getPostAdressen()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil.createPostadresFilter());
	}

	@Override
	public List<PersoonAdres> getPostAdressenOpPeildatum()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil
			.createPostadresOpPeildatumFilter());
	}

	@Override
	public List<PersoonAdres> getFactuurAdressen()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil.createFactuuradresFilter());
	}

	@Override
	public List<PersoonAdres> getFactuurAdressenOpPeildatum()
	{
		return AdresseerbaarUtil.getAdressen(this, AdresseerbaarUtil
			.createFactuuradresOpPeildatumFilter());
	}

	@Override
	@Exportable
	public PersoonAdres getFysiekAdres()
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createFysiekadresOpPeildatumFilter());
	}

	@Override
	public PersoonAdres getFysiekAdres(Date peildatum)
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createFysiekadresOpPeildatumFilter(peildatum));
	}

	@Override
	@Exportable
	public PersoonAdres getPostAdres()
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createPostadresOpPeildatumFilter());
	}

	@Override
	public PersoonAdres getPostAdres(Date peildatum)
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createPostadresOpPeildatumFilter(peildatum));
	}

	@Exportable
	@Override
	public PersoonAdres getFactuurAdres()
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createFactuuradresOpPeildatumFilter());
	}

	@Override
	public PersoonAdres getFactuurAdres(Date peildatum)
	{
		return AdresseerbaarUtil.getEersteAdres(this, AdresseerbaarUtil
			.createFactuuradresOpPeildatumFilter(peildatum));
	}

	/**
	 * Maakt een nieuw {@link PersoonAdres} aan, met nieuw {@link Adres} en voegt deze toe
	 * aan een van de lijsten. Ook voegt hij {@link PersoonAdres} toe aan de lijst in
	 * {@link Adres}.
	 */
	@Override
	public PersoonAdres newAdres()
	{
		PersoonAdres nieuwAdres = new PersoonAdres();
		nieuwAdres.setBegindatum(TimeUtil.getInstance().currentDate());
		nieuwAdres.setPersoon(this);
		nieuwAdres.getAdres().getPersoonAdressen().add(nieuwAdres);
		return nieuwAdres;
	}

	@Override
	@Exportable
	public String getFysiekAdresOmschrijving()
	{
		return "Woonadres";
	}

	@Override
	@Exportable
	public String getPostAdresOmschrijving()
	{
		return "Postadres";
	}

	@Override
	@Exportable
	public String getFactuurAdresOmschrijving()
	{
		return "Factuuradres";
	}

	@Override
	@Exportable
	public List<PersoonContactgegeven> getContactgegevens()
	{
		return contactgegevens;
	}

	@Override
	public void setContactgegevens(List<PersoonContactgegeven> contactgegevens)
	{
		this.contactgegevens = contactgegevens;
	}

	@Override
	public PersoonContactgegeven newContactgegeven()
	{
		PersoonContactgegeven gegeven = new PersoonContactgegeven();
		gegeven.setPersoon(this);
		gegeven.setVolgorde(getContactgegevens().size());

		return gegeven;
	}

	public List<PersoonContactgegeven> getContactgegevens(SoortContactgegeven soort)
	{
		return ContacteerbaarUtil.getContactgegevens(this, soort);
	}

	@AutoFormEmbedded
	@Exportable
	public PersoonContactgegeven getEersteEmailAdres()
	{
		return ContacteerbaarUtil.getEersteEmailAdres(this);
	}

	@AutoFormEmbedded
	@Exportable
	public PersoonContactgegeven getEersteHomepage()
	{
		return ContacteerbaarUtil.getEersteHomepage(this);
	}

	@AutoFormEmbedded
	@Exportable
	public PersoonContactgegeven getEersteMobieltelefoon()
	{
		return ContacteerbaarUtil.getEersteMobieltelefoon(this);
	}

	@AutoFormEmbedded
	public PersoonContactgegeven getEersteOverig()
	{
		return ContacteerbaarUtil.getEersteOverig(this);
	}

	@AutoFormEmbedded
	@Exportable
	public PersoonContactgegeven getEersteTelefoon()
	{
		return ContacteerbaarUtil.getEersteTelefoon(this);
	}

	@AutoFormEmbedded
	@Exportable
	public PersoonContactgegeven getContactGegevenMetCode(String code)
	{
		return ContacteerbaarUtil.getContactGegevenMetCode(this, code);
	}

	@Override
	public List<PersoonVrijVeld> getVrijVelden()
	{
		return vrijVelden;
	}

	@Override
	public List<PersoonVrijVeld> getVrijVelden(VrijVeldCategorie categorie)
	{
		List<PersoonVrijVeld> res = new ArrayList<PersoonVrijVeld>();
		for (PersoonVrijVeld pvv : getVrijVelden())
		{
			if (pvv.getVrijVeld().getCategorie().equals(categorie))
			{
				res.add(pvv);
			}
		}
		return res;
	}

	@Exportable
	public String getVrijVeldWaarde(String naamVrijVeld)
	{
		for (PersoonVrijVeld vrijVeld : vrijVelden)
		{
			if (vrijVeld.getVrijVeld().getNaam().equals(naamVrijVeld))
				return vrijVeld.getOmschrijving();
		}
		return null;
	}

	@Override
	public PersoonVrijVeld newVrijVeld()
	{
		PersoonVrijVeld pvv = new PersoonVrijVeld();
		pvv.setPersoon(this);

		return pvv;
	}

	@Override
	public void setVrijVelden(List<PersoonVrijVeld> vrijvelden)
	{
		this.vrijVelden = vrijvelden;
	}

	/**
	 * Protected om op deze manier Hibernate wel de mogelijkheid te geven dit te
	 * proxy-overschrijven, en tegelijkertijd te voorkomen dat mensen dit direct
	 * aanroepen.
	 * 
	 * @see #deelnemers
	 */
	protected List<Deelnemer> getDeelnemers()
	{
		return deelnemers;
	}

	protected void setDeelnemers(List<Deelnemer> deelnemers)
	{
		this.deelnemers = deelnemers;
	}

	/**
	 * Implementatie is complex om een bug in Hibernate te vermijden: een
	 * <tt>@OneToOne</tt> mapping levert 1 extra onnodige query op, maar een
	 * <tt>@OneToMany</tt> niet.
	 * 
	 * @return De deelnemer die gekoppeld is aan deze persoon.
	 */
	@Exportable
	public Deelnemer getDeelnemer()
	{
		// FIXME aanpassen wanneer HHH-3930 opgelost is.
		List<Deelnemer> list = getDeelnemers();
		if (list == null || list.isEmpty())
		{
			return null;
		}
		return list.get(0);
	}

	/**
	 * Implementatie is complex om een bug in Hibernate te vermijden: een
	 * <tt>@OneToOne</tt> mapping levert 1 extra onnodige query op, maar een
	 * <tt>@OneToMany</tt> niet.
	 * 
	 * @FIXME aanpassen wanneer HHH-3930 opgelost is.
	 */
	public void setDeelnemer(Deelnemer deelnemer)
	{
		// FIXME aanpassen wanneer HHH-3930 opgelost is.
		getDeelnemers().clear();
		getDeelnemers().add(deelnemer);
	}

	public void setCumi()
	{
		Cumi cumi = Cumi.getCumi(this, EduArteContext.get().getPeildatumOfVandaag());
		if (cumi != null)
		{
			setCumiRatio(cumi.getRatio());
			setCumiCategorie(cumi.getCategorie());
		}
		else
		{
			setCumiRatio(null);
			setCumiCategorie(null);
		}
	}

	public CumiCategorie getCumiCategorie()
	{
		return cumiCategorie;
	}

	public CumiRatio getCumiRatio()
	{
		return cumiRatio;

	}

	protected void setCumiRatio(CumiRatio cumiRatio)
	{
		this.cumiRatio = cumiRatio;
	}

	protected void setCumiCategorie(CumiCategorie cumiCategorie)
	{
		this.cumiCategorie = cumiCategorie;
	}

	public void setNieuwkomer(boolean nieuwkomer)
	{
		this.nieuwkomer = nieuwkomer;
	}

	@Exportable
	public boolean isNieuwkomer()
	{
		return nieuwkomer;
	}

	@AutoForm(label = "Nieuwkomer")
	public String getNieuwkomerOmschrijving()
	{
		return isNieuwkomer() ? "Ja" : "Nee";
	}

	/**
	 * @return De wettelijke vertegenwoordigers van de deelnemer, voor zover dat een
	 *         personen zijn. Een lege lijst indien de deelnemer meerderjarig is, geen
	 *         wettelijk vertegenwoordigers gevonden kunnen worden of als de wettelijk
	 *         vertegenwoordiger een organisatie is.
	 */
	@Exportable(omschrijving = "Wettelijk vertegenwoordigers van de deelnemer voor zover dat personen zijn. Geen indien meerdejarig.")
	public List<Persoon> getWettelijkVertegenwoordigendePersonen()
	{
		if (isMeerderjarig())
			return Collections.emptyList();

		List<Persoon> vertegenwoordigers = new ArrayList<Persoon>();

		for (AbstractRelatie relatie : getWettelijkVertegenwoordigers())
		{
			if (relatie instanceof Relatie)
				vertegenwoordigers.add(((Relatie) relatie).getRelatie());
		}

		return vertegenwoordigers;
	}

	/**
	 * Wettelijk vertegenwoordigers van de deelnemer voor zover dat personen zijn en de
	 * deelnemer minderjarig is, of anders de deelnemer zelf.
	 */
	@Exportable(omschrijving = "Wettelijk vertegenwoordigers van de deelnemer voor zover dat personen zijn en de deelnemer minderjarig is, of anders de deelnemer zelf.")
	public List<Persoon> getWettelijkVertegenwoordigendePersonenOfDeelnemer()
	{
		List<Persoon> result = getWettelijkVertegenwoordigendePersonen();
		if (result.isEmpty())
			return Collections.singletonList(this);

		return result;
	}

	/**
	 * @return De relaties die de wettelijk vertegenwoordigers van de deelnemer zijn, mits
	 *         de deelnemer minderjarig is. Lege lijst indien niet een dergelijke relatie
	 *         bestaat of de deelnemer meerderjarig is.
	 */
	public List<AbstractRelatie> getWettelijkVertegenwoordigers()
	{
		if (isMeerderjarig())
			return Collections.emptyList();

		List<AbstractRelatie> vertegenwoordigers = new ArrayList<AbstractRelatie>();
		for (AbstractRelatie relatie : relaties)
		{
			if (relatie.isWettelijkeVertegenwoordiger())
				vertegenwoordigers.add(relatie);
		}

		return vertegenwoordigers;
	}

	/**
	 * @return De betalingsplichtigevan de deelnemer. Indien geen betalingsplichtige
	 *         relatie is geregistreerd, is dat de deelnemer zelf.
	 */
	@Exportable
	public Debiteur getBetalingsplichtige()
	{
		AbstractRelatie relatie = getBetalingsplichtigeRelatie();

		if (relatie instanceof Relatie)
			return ((Relatie) relatie).getRelatie();
		else if (relatie instanceof PersoonExterneOrganisatie)
			return ((PersoonExterneOrganisatie) relatie).getRelatie();

		return this;
	}

	/**
	 * @return De relatie die de betalingsplichtige is van de deelnemer. Null indien een
	 *         dergelijke relatie niet bestaat.
	 */
	public AbstractRelatie getBetalingsplichtigeRelatie()
	{
		for (AbstractRelatie relatie : relaties)
		{
			if (relatie.isBetalingsplichtige())
				return relatie;
		}

		return null;
	}

	/**
	 * @return true indien de deelnemer is overleden
	 */
	@Exportable
	public boolean isOverleden()
	{
		return datumOverlijden != null;
	}

	public int getLeeftijdOpDatum(Date datum)
	{
		if (getGeboortedatum() == null)
			return 0;
		return TimeUtil.getInstance().getDifferenceInYears(datum, getGeboortedatum());
	}

	@Exportable
	public int getLeeftijd()
	{
		return getLeeftijdOpDatum(TimeUtil.getInstance().currentDate());
	}

	@AutoForm(label = "Betalingsplichtige", description = "Deelnemer zelf registreren als betalingsplichtige. Uit te schakelen door een bijbehorende relatie als betalingsplichtige aan te merken. Het kan zijn dat een BPV-bedrijf de betalingsplicht gedurende een bepaalde tijd overneemt.")
	public boolean isPersoonBetalingsplichtige()
	{
		return this.equals(getBetalingsplichtige());
	}

	/*
	 * Let op: Zelf zorgen voor het opslaan van de relatie entiteiten, bijvoorbeeld via
	 * ChangeRecordingModel.
	 */
	public void setPersoonBetalingsplichtige(boolean persoonBetalingsplichtige)
	{
		if (persoonBetalingsplichtige)
		{
			if (!isPersoonBetalingsplichtige())
			{
				for (AbstractRelatie relatie : getRelaties())
				{
					relatie.setBetalingsplichtige(false);
				}
			}
		}
		else if (!isMeerderjarig())
			throw new UnsupportedOperationException(
				"Niet mogelijk om een persoon handmatig niet-betalingsplichtig te maken. Maak hiervoor een van de gekoppelde relaties betalingsplichtig.");
	}

	public List<PersoonBijlage> getBijlagen()
	{
		return bijlagen;
	}

	public void setBijlagen(List<PersoonBijlage> bijlagen)
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
		PersoonBijlage teVerwijderen = null;
		for (PersoonBijlage persoonBijlage : getBijlagen())
		{
			if (persoonBijlage.getBijlage().equals(bijlage))
			{
				teVerwijderen = persoonBijlage;
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
		for (PersoonBijlage persoonBijlage : getBijlagen())
		{
			if (persoonBijlage.getBijlage().equals(bijlage))
				return true;
		}
		return false;
	}

	public PersoonBijlage addBijlage(Bijlage bijlage)
	{
		PersoonBijlage newBijlage = new PersoonBijlage();
		newBijlage.setBijlage(bijlage);
		newBijlage.setPersoon(this);

		getBijlagen().add(newBijlage);

		return newBijlage;
	}

	public Date getLaatsteExportDatum()
	{
		return laatsteExportDatum;
	}

	public void setLaatsteExportDatum(Date laatsteExportDatum)
	{
		this.laatsteExportDatum = laatsteExportDatum;
	}

	@Exportable(omschrijving = "M, V of O")
	public char getGeslachtEersteLetter()
	{
		if (geslacht == null)
			return 'O';
		return geslacht.getEersteLetter().charAt(0);
	}

	public void setNietVerstrekkenAanDerden(boolean nietVerstrekkenAanDerden)
	{
		this.nietVerstrekkenAanDerden = nietVerstrekkenAanDerden;
	}

	public boolean isNietVerstrekkenAanDerden()
	{
		return nietVerstrekkenAanDerden;
	}

	public void setAutomatischeIncasso(AutomatischeIncasso automatischeIncasso)
	{
		this.automatischeIncasso = automatischeIncasso;
		if (getAutomatischeIncasso() != AutomatischeIncasso.Geen
			&& getAutomatischeIncasso() != null)
			factuurBetaalwijze = Betaalwijze.AUTOMATISCHE_INCASSO;
	}

	@Override
	public AutomatischeIncasso getAutomatischeIncasso()
	{
		return automatischeIncasso;
	}

	@Exportable
	@Override
	public String getBankrekeningTenaamstelling()
	{
		if (bankrekeningTenaamstelling != null)
			return bankrekeningTenaamstelling;

		if (StringUtil.isNotEmpty(bankrekeningnummer))
			return getFormeleNaam();

		return null;
	}

	@Override
	public Date getAutomatischeIncassoEinddatum()
	{
		return automatischeIncassoEinddatum;
	}

	public void setAutomatischeIncassoEinddatum(Date automatischeIncassoEinddatum)
	{
		this.automatischeIncassoEinddatum = automatischeIncassoEinddatum;
	}

	public void setBankrekeningTenaamstelling(String bankrekeningTenaamstelling)
	{
		if (StringUtil.isNotEmpty(bankrekeningTenaamstelling)
			&& !bankrekeningTenaamstelling.equals(getFormeleNaam()))
			this.bankrekeningTenaamstelling = bankrekeningTenaamstelling;
		else
			this.bankrekeningTenaamstelling = null;
	}

	@Override
	@Exportable
	public String getContactpersoon()
	{
		return getFormeleNaam();
	}

	@Override
	@Exportable
	public char getBetaalwijze()
	{
		// op dit punt weet je niet of er wettelijk cursusgeld of andere dingen
		// gefactureerd gaan worden.
		if (automatischeIncasso == AutomatischeIncasso.Alles
			|| automatischeIncasso == AutomatischeIncasso.AlleenCursusgeld)
		{
			if (automatischeIncassoEinddatum == null
				|| !TimeUtil.getInstance().currentDate().after(automatischeIncassoEinddatum))
				return 'I';
		}
		return 'A';
	}

	@Override
	public Persoon getPersoon()
	{
		return this;
	}

	@Exportable
	public String getWachtwoord()
	{
		return wachtwoord;
	}

	public void setWachtwoord(String wachtwoord)
	{
		this.wachtwoord = wachtwoord;
	}

	public Land getGeboortelandOuder1()
	{
		return geboortelandOuder1;
	}

	public void setGeboortelandOuder1(Land geboortelandOuder1)
	{
		this.geboortelandOuder1 = geboortelandOuder1;
	}

	public Land getGeboortelandOuder2()
	{
		return geboortelandOuder2;
	}

	public void setGeboortelandOuder2(Land geboortelandOuder2)
	{
		this.geboortelandOuder2 = geboortelandOuder2;
	}

	public Verblijfsvergunning getVerblijfsvergunning()
	{
		return verblijfsvergunning;
	}

	public void setVerblijfsvergunning(Verblijfsvergunning verblijfsvergunning)
	{
		this.verblijfsvergunning = verblijfsvergunning;
	}

	public Correspondentietaal getCorrespondentietaal()
	{
		return correspondentietaal;
	}

	public void setCorrespondentietaal(Correspondentietaal correspondentietaal)
	{
		this.correspondentietaal = correspondentietaal;
	}

	public String getBuitenlandsBankrekeningnummer()
	{
		return buitenlandsBankrekeningnummer;
	}

	public void setBuitenlandsBankrekeningnummer(String buitenlandsBankrekeningnummer)
	{
		this.buitenlandsBankrekeningnummer = buitenlandsBankrekeningnummer;
	}

	public String getBuitenlandseBanknaam()
	{
		return buitenlandseBanknaam;
	}

	public void setBuitenlandseBanknaam(String buitenlandseBanknaam)
	{
		this.buitenlandseBanknaam = buitenlandseBanknaam;
	}

	public Land getLandBank()
	{
		return landBank;
	}

	public void setLandBank(Land landBank)
	{
		this.landBank = landBank;
	}

	@Override
	public boolean isVerzamelfacturen()
	{
		return true;
	}

	@Override
	@Exportable
	public Integer getBetalingstermijn()
	{
		return betalingstermijn;
	}

	@Override
	public void setBetalingstermijn(Integer betalingstermijn)
	{
		this.betalingstermijn = betalingstermijn;
	}

	/**
	 * Controleer of deze persoon (Deelnemer of Relatie) een debiteurennummer heeft die
	 * null is Dit kan bijvoorbeeld gebeuren na een Noise import. Wanneer deze null is dan
	 * controleren we de settings en genereren we alsnog een debiteurnummer volgens
	 * protocol
	 */
	public void checkAndCreateDebiteurNummer()
	{
		if (debiteurennummer == null)
		{
			Deelnemer deelnemer = getDeelnemer();
			NummerGeneratorDataAccessHelper generator =
				DataAccessRegistry.getHelper(NummerGeneratorDataAccessHelper.class);
			SettingsDataAccessHelper settingsHelper =
				DataAccessRegistry.getHelper(SettingsDataAccessHelper.class);
			DebiteurNummerSetting setting = settingsHelper.getSetting(DebiteurNummerSetting.class);
			if ((setting != null && setting.getValue() != null && (!setting.getValue()
				.isDeelnemernummerIsDebiteurnummer()))
				|| deelnemer == null)
			{
				setDebiteurennummer(generator.newDebiteurnummer());
			}
			else
			{
				setDebiteurennummer(Long.valueOf(deelnemer.getDeelnemernummer()));
			}
		}
	}

	@Override
	public String getEmailAdres()
	{
		PersoonContactgegeven email = getEersteEmailAdres();
		if (email != null)
			return email.getContactgegeven();
		else
			return null;
	}

	@Override
	public String getNaam()
	{
		return getFormeleNaam();
	}

	/**
	 * Dummy methode om te voldoen aan de Debiteur interface. Het is weinig zinvol om dit
	 * met subclasses of interfaces op te lossen.
	 */
	@Override
	public ExterneOrganisatieContactPersoon getContactpersoonMetRol(String rol)
	{
		return null;
	}

	/**
	 * @return een bankrekeningnummer, niet zijnde een max-7-cijferig
	 *         ING-bankrekeningnummer
	 */
	@Override
	@Exportable
	public String getBankrekeningBank()
	{
		if (bankrekeningnummer == null || bankrekeningnummer.length() <= 7)
			return null;

		return bankrekeningnummer;
	}

	/**
	 * @return een max-7-cijferig ING-bankrekeningnummer, voorheen bekend als Girorekening
	 */
	@Override
	@Exportable
	public String getBankrekeningGiro()
	{
		if (bankrekeningnummer == null || bankrekeningnummer.length() > 7)
			return null;

		return bankrekeningnummer;
	}

	public void setFactuurBetaalwijze(Betaalwijze factuurBetaalwijze)
	{
		this.factuurBetaalwijze = factuurBetaalwijze;
		if (factuurBetaalwijze == Betaalwijze.AUTOMATISCHE_INCASSO
			&& getAutomatischeIncasso() == AutomatischeIncasso.Geen)
			automatischeIncasso = AutomatischeIncasso.Alles;
	}

	@Override
	public Betaalwijze getFactuurBetaalwijze()
	{
		return factuurBetaalwijze;
	}

}