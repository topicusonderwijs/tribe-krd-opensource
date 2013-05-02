/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.inschrijving;

import java.math.BigDecimal;
import java.util.*;

import javax.persistence.*;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.FieldPersistance;
import nl.topicus.cobra.entities.FieldPersistenceMode;
import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.cobra.web.components.search.browser.SearchResultsPanel.SearchBrowserEntiteit;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.beanpropertyresolvers.EduArtePrototypeBeanPropertyResolver;
import nl.topicus.eduarte.dao.helpers.BPVInschrijvingDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.MeeteenheidDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductAfnameDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.OpleidingDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ResultaatDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.LesweekindelingOrganisatieEenheidLocatieDataAccesHelper;
import nl.topicus.eduarte.entities.BronEntiteitStatus;
import nl.topicus.eduarte.entities.ContacteerbaarUtil;
import nl.topicus.eduarte.entities.IBronStatusEntiteit;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumUtil;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.contract.ContractOnderdeel;
import nl.topicus.eduarte.entities.contract.Contract.Onderaanneming;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.hogeronderwijs.Hoofdfase;
import nl.topicus.eduarte.entities.hogeronderwijs.Inschrijvingsverzoek;
import nl.topicus.eduarte.entities.hogeronderwijs.OpleidingFase;
import nl.topicus.eduarte.entities.hogeronderwijs.OpleidingsVorm;
import nl.topicus.eduarte.entities.ibgverzuimloket.IbgVerzuimmelding;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding.SoortOnderwijs;
import nl.topicus.eduarte.entities.inschrijving.Vervolgonderwijs.SoortVervolgonderwijs;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.opleiding.SoortOnderwijsTax;
import nl.topicus.eduarte.entities.opleiding.Team;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatieContactgegeven;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.entities.participatie.LesweekIndelingOrganisatieEenheidLocatie;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.VerbintenisBijlage;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.productregel.Productregel.TypeProductregel;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.SoortToets;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.entities.taxonomie.ho.CrohoOnderdeel;
import nl.topicus.eduarte.entities.taxonomie.ho.CrohoOpleiding;
import nl.topicus.eduarte.entities.taxonomie.ho.CrohoOpleidingAanbod;
import nl.topicus.eduarte.entities.taxonomie.mbo.Kwalificatie;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Meeteenheid;
import nl.topicus.eduarte.entities.taxonomie.vo.Elementcode;
import nl.topicus.eduarte.entities.vrijevelden.VerbintenisVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.providers.PersoonProvider;
import nl.topicus.eduarte.providers.VerbintenisProvider;
import nl.topicus.eduarte.rapportage.entities.OnderwijsproductAfnameContext_SE_CE_Eindresultaat;
import nl.topicus.eduarte.rapportage.entities.VerbintenisRapportage;
import nl.topicus.eduarte.util.criteriumbank.BereikbareDiplomasUtil;
import nl.topicus.eduarte.web.components.choice.CohortCombobox;
import nl.topicus.eduarte.web.components.choice.KenniscentrumCombobox;
import nl.topicus.eduarte.web.components.quicksearch.opleiding.OpleidingQuickSearchField;
import nl.topicus.eduarte.zoekfilters.BPVInschrijvingZoekFilter;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductAfnameZoekFilter;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.onderwijs.duo.bron.Bron;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;
import nl.topicus.onderwijs.duo.criho.annot.Criho;

import org.apache.wicket.model.Model;
import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;
import org.hibernate.Hibernate;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;
import org.hibernate.annotations.Type;

/**
 * Een inschrijving voor een opleiding.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@BatchSize(size = 20)
@Exportable
@Table(appliesTo = "Verbintenis", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
@IsViewWhenOnNoise
public class Verbintenis extends BeginEinddatumInstellingEntiteit implements DeelnemerProvider,
		PersoonProvider, VerbintenisProvider, IContextInfoObject,
		IOrganisatieEenheidLocatieKoppelEntiteit<Verbintenis>, VrijVeldable<VerbintenisVrijVeld>,
		SearchBrowserEntiteit, IBijlageKoppelEntiteit<VerbintenisBijlage>, IVooropleiding,
		IBronStatusEntiteit
{
	private static final long serialVersionUID = 1L;

	public static final String TABEL = "VERBINTENIS";

	@Transient
	private transient VerbintenisRapportage rapportage;

	/**
	 * De status van een inschrijving.
	 * <p>
	 * Direct wanneer de deelnemer een intakeprocedure start, wordt een verbintenis
	 * aangemaakt. Deze verbintenis aangemaakt, ook al is er nog geen positieve uitkomst
	 * van een intakegesprek. Deze "zeer voorlopige" verbintenis heeft de status "intake",
	 * hetgeen betekent dat deze nog niet wordt getoond in het tabblad Verbintenis van een
	 * deelnemer. Pas bij een positieve uitkomst van een intakegesprek, wanneer de
	 * administratief medewerker een (voorlopige) verbintenis "aanmaakt", verschijnt de
	 * verbintenis op het tabblad Verbintenis. Het bestaan van een verbintenis met status
	 * "intake" waarborgt echter dat de deelnemer in het scherm "Deelnemer zoeken" en
	 * "Uitgebreid zoeken" kan worden gevonden en zorgt ervoor dat de autorisatie goed
	 * werkt.
	 * <p>
	 * Zolang een verbintenis voorlopig is, kent zij geen verplichte velden en wordt de
	 * keuze voor een opleiding/organisatie-eenheid/locatie als voorlopig beschouwd. Er
	 * gaat pas een melding naar BRON wanneer de verbintenis volledig is.
	 * 
	 * @author loite
	 */
	public static enum VerbintenisStatus implements BronCommuniceerbaar
	{
		/**
		 * De deelnemer heeft zichzelf aangemeld maar de aanmelding is nog niet helemaal
		 * verwerkt. De status van de verwerking is te zien in de aanmelding De
		 * verbintenis is nog niet te vinden op het deelnemer zoeken scherm
		 */
		Aangemeld,
		/**
		 * De deelnemer bevindt zich nog in het intakeproces waar vanuit nog geen
		 * (voorlopige) verbintenis is gerealiseerd. In het tabblad Verbintenis is deze
		 * verbintenis nog niet zichtbaar.
		 */
		Intake,

		/**
		 * De verbintenis is nog niet officieel. Alle gegevens kunnen nog wijzigen. De
		 * deelnemer is nog niet aan BRON gemeld. De deelnemer is nog niet opgenomen in
		 * het onderwijslogistieke proces.
		 */
		Voorlopig,

		/**
		 * De verbintenis is nog niet officieel (niet ondertekend), maar alle gegevens
		 * liggen in principe vast. De deelnemer wordt al wel opgenomen in het
		 * onderwijslogistieke proces. De deelnemer is aan BRON gemeld (of de melding
		 * staat in de wachtrij). Er kan een overeenkomst worden afgedrukt. Bij het op
		 * volledig zetten wordt gecontroleerd of alle gegevens voor BRON aanwezig zijn,
		 * als dit niet het geval is, wordt dit via een melding weergegeven. De status
		 * wordt terug gezet of blijft op voorlopig staan.
		 */
		Volledig,

		/**
		 * De onderwijsovereenkomst is wel afgedrukt, maar nog niet ondertekend. De
		 * deelnemer is aan BRON gemeld (of de melding staat in de wachtrij). Gedurende
		 * deze status wordt gewacht op de retourontvangst van een ondertekende
		 * onderwijsovereenkomst.
		 */
		Afgedrukt,

		/**
		 * De verbintenis is officieel en getekend. Alle gegevens liggen vast. De
		 * deelnemer is aan BRON gemeld (of melding staat in de wachtrij). De deelnemer
		 * neemt deel aan het onderwijs.
		 */
		Definitief,

		/**
		 * De (werkelijke) einddatum van de verbintenis is verstreken. Deze status is in
		 * feite impliciet, want deze status wordt automatisch bereikt door het
		 * verstrijken van de einddatum, behalve wanneer de verbintenis afgemeld of
		 * afgewezen is.
		 */
		Beeindigd,

		/**
		 * De deelnemer heeft zich afgemeld. De verbintenis zal nooit worden geëffectueerd
		 * en de deelnemer neemt geen deel aan het onderwijslogistieke proces. Er is geen
		 * melding naar BRON geweest, of, wanneer deze status via Definitief was bereikt,
		 * de inschrijving is uit BRON verwijderd. De verbintenis mag niet meer gewijzigd
		 * worden zodra deze status bereikt wordt.
		 */
		Afgemeld,

		/**
		 * De instelling heeft de deelnemer afgewezen. De verbintenis zal nooit worden
		 * geëffectueerd en de deelnemer neemt geen deel aan het onderwijslogistieke
		 * proces. Er is geen melding naar BRON geweest, of, wanneer deze status via
		 * "definitief" was bereikt, de inschrijving is uit BRON verwijderd. De
		 * verbintenis mag niet meer gewijzigd worden zodra deze status bereikt wordt.
		 */
		Afgewezen;

		/**
		 * @see nl.topicus.eduarte.entities.inschrijving.BronCommuniceerbaar#isBronCommuniceerbaar()
		 */
		@Override
		public boolean isBronCommuniceerbaar()
		{
			return tussen(VerbintenisStatus.Volledig, VerbintenisStatus.Beeindigd);
		}

		/**
		 * @return <code>true</code> als <tt>status1 &lt;= this &lt;= status2</tt>
		 */
		public boolean tussen(VerbintenisStatus status1, VerbintenisStatus status2)
		{
			return (status1.ordinal() <= this.ordinal()) && this.ordinal() <= status2.ordinal();
		}

		/**
		 * @return De toegestane vervolgstatussen
		 */
		public VerbintenisStatus[] getVervolgEnHuidige(boolean authorized, boolean isVO)
		{
			VerbintenisStatus[] overgangen = getVervolg(authorized, isVO);
			VerbintenisStatus[] ret = new VerbintenisStatus[1 + overgangen.length];

			System.arraycopy(new VerbintenisStatus[] {this}, 0, ret, 0, 1);
			System.arraycopy(overgangen, 0, ret, 1, overgangen.length);

			return ret;
		}

		public VerbintenisStatus[] getVervolg(boolean authorized)
		{
			return getVervolg(authorized, false);
		}

		/**
		 * @return De toegestane vervolgstatussen
		 */
		public VerbintenisStatus[] getVervolg(boolean authorized, boolean isVO)
		{
			switch (this)
			{
				case Aangemeld:
					return new VerbintenisStatus[] {Intake};
				case Intake:
					return new VerbintenisStatus[] {Voorlopig};
				case Voorlopig:
					return new VerbintenisStatus[] {Volledig, Afgewezen, Afgemeld};
				case Volledig:
					// KOL 20091110: Afgedrukt weggehaald als vervolgstatus omdat dit
					// alleen mag met de knop 'Afdrukken'
					if (authorized && isVO)
						return new VerbintenisStatus[] {Definitief, Afgewezen, Afgemeld};
					else if (authorized)
						return new VerbintenisStatus[] {Afgewezen, Afgemeld};
					else if (isVO)
						return new VerbintenisStatus[] {Definitief};
					else
						return new VerbintenisStatus[] {};
				case Afgedrukt:
					if (authorized)
						return new VerbintenisStatus[] {Definitief, Afgewezen, Afgemeld};
					else
						return new VerbintenisStatus[] {Definitief};
				case Definitief:
					return new VerbintenisStatus[] {}; // Beeindigd moet je bereiken
					// via Verbintenis beeindigen
				case Beeindigd:
					return new VerbintenisStatus[] {};
				case Afgemeld:
					return new VerbintenisStatus[] {Voorlopig};
				case Afgewezen:
					return new VerbintenisStatus[] {Voorlopig};
				default:
			}
			return new VerbintenisStatus[] {};
		}

		/**
		 * @return true indien deze verbintenis door iedereen gemuteerd kan worden (voor
		 *         wat betreft de velden die in de onderwijsovereenkomst en BRON
		 *         vastliggen)
		 */
		public boolean isMuteerbaar()
		{
			return equals(Aangemeld) || equals(Intake) || equals(Voorlopig);
		}

		public boolean isAfgesloten()
		{
			return equals(Afgemeld) || equals(Afgewezen) || equals(Beeindigd);
		}

		public boolean isVerwijderd()
		{
			return equals(Afgemeld) || equals(Afgewezen);
		}

		/**
		 * @return true indien de deelnemer, gezien zijn status, actief is (afgezien van
		 *         begin- en einddatum). Dit zijn de statussen voorlopig t/m definitief.
		 */
		public boolean isActief()
		{
			return equals(Voorlopig) || equals(Volledig) || equals(Afgedrukt) || equals(Definitief);
		}

		public static List<VerbintenisStatus> getBronCommuniceerbareStatussen()
		{
			List<VerbintenisStatus> result = new ArrayList<VerbintenisStatus>();
			for (VerbintenisStatus status : values())
			{
				if (status.isBronCommuniceerbaar())
				{
					result.add(status);
				}
			}
			return result;
		}

		public static List<VerbintenisStatus> getNietBronCommuniceerbareStatussen()
		{
			List<VerbintenisStatus> result = new ArrayList<VerbintenisStatus>();
			for (VerbintenisStatus status : values())
			{
				if (!status.isBronCommuniceerbaar())
				{
					result.add(status);
				}
			}
			return result;
		}
	}

	public static enum Vertrekstatus
	{
		Vertrokken,
		BevorderdVMBOBL("Bevorderd naar VMBO BL"),
		BevorderdVMBOKL("Bevorderd naar VMBO KL"),
		BevorderdVMBOGL("Bevorderd naar VMBO GL"),
		BevorderdVMBOTL("Bevorderd naar VMBO TL"),
		BevorderdHAVO("Bevorderd naar HAVO"),
		BevorderdVWO("Bevorderd naar VWO"),
		NietBevorderd,
		AndereOpleiding,
		Geslaagd,
		Afgewezen;

		private String omschrijving;

		private Vertrekstatus()
		{
			this(null);
		}

		private Vertrekstatus(String omschrijving)
		{
			this.omschrijving = omschrijving;
		}

		@Override
		public String toString()
		{
			return omschrijving != null ? omschrijving : StringUtil.convertCamelCase(name());
		}
	}

	@SuppressWarnings("unchecked")
	@Bron
	@Transient
	private Enum handmatigVersturenNaarBronMutatie = null;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	@Bron(verplicht = true)
	private Date geplandeEinddatum;

	@Bron
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "redenUitschrijving", nullable = true)
	@Index(name = "idx_Verbintenis_redenUit")
	@AutoForm(htmlClasses = "unit_max")
	private RedenUitschrijving redenUitschrijving;

	@Formula(value = "case when einddatum is null then 0 else 1 end")
	private boolean beeindigd;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@Bron
	@Criho
	private VerbintenisStatus status;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date datumDefinitief;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date datumGeplaatst;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date datumVoorlopig;

	@Column(nullable = false)
	@Bron(verplicht = true)
	@RestrictedAccess(hasSetter = false)
	private int volgnummer;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date datumOvereenkomstOndertekend;

	/**
	 * Een gegenereerd overeenkomstnummer, vergelijkbaar met het deelnemernummer.
	 */
	@Column(nullable = false)
	private long overeenkomstnummer;

	/**
	 * Het volgnummer van deze entiteit in het oude pakket (bijvoorbeeld nOISe). Indien
	 * gevuld wordt dit volgnummer gebruikt in communicatie met BRON.
	 */
	@Column(nullable = true)
	@AutoForm(include = false)
	private String volgnummerInOudPakket;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = false)
	@Index(name = "idx_Verbintenis_deelnemer")
	@BatchSize(size = 20)
	@RestrictedAccess(hasSetter = false)
	private Deelnemer deelnemer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatieEenheid", nullable = false)
	@Index(name = "idx_Verbintenis_orgEhd")
	@AutoForm(label = "Organisatie-eenheid", htmlClasses = "unit_max")
	private OrganisatieEenheid organisatieEenheid;

	@Bron(verplicht = true)
	@Criho
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "opleiding", nullable = true)
	@Index(name = "idx_Verbintenis_opleiding")
	@AutoFormEmbedded
	@BatchSize(size = 20)
	private Opleiding opleiding;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true)
	@Index(name = "idx_Verbintenis_brin")
	@AutoForm(editorClass = KenniscentrumCombobox.class, htmlClasses = "unit_max")
	private Brin brin;

	/**
	 * De hoofdlocatie van de inschrijving
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "locatie", nullable = true)
	@Index(name = "idx_Verbintenis_locatie")
	@AutoForm(htmlClasses = "unit_max")
	@Bron
	private Locatie locatie;

	@Bron
	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "verbintenis")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<OnderwijsproductAfnameContext> afnameContexten =
		new ArrayList<OnderwijsproductAfnameContext>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cohort", nullable = true)
	@Index(name = "idx_Verbintenis_cohort")
	@AutoForm(editorClass = CohortCombobox.class, htmlClasses = "unit_120")
	private Cohort cohort;

	@Bron
	@Criho
	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "verbintenis")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@OrderBy("begindatum DESC, einddatum DESC")
	private List<Plaatsing> plaatsingen = new ArrayList<Plaatsing>();

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "verbintenis")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@OrderBy("begindatum DESC, einddatum DESC")
	private List<BPVInschrijving> bpvInschrijvingen = new ArrayList<BPVInschrijving>();

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "verbintenis")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@OrderBy("datumTijd DESC")
	private List<Intakegesprek> intakegesprekken = new ArrayList<Intakegesprek>();

	public static enum Bekostigd
	{
		/**
		 * Ja
		 */
		Ja,
		/**
		 * Nee
		 */
		Nee,
		/**
		 * Gedeeltelijk. In 'bekostigingsperiodes' wordt de periode waarin de verbintenis
		 * wel/niet bekostigd is opgeslagen.
		 */
		Gedeeltelijk;
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@Bron
	@AutoForm(htmlClasses = "unit_120")
	private Bekostigd bekostigd = Bekostigd.Nee;

	@Column(nullable = true)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "verbintenis")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@OrderBy("begindatum ASC")
	@Bron
	private List<Bekostigingsperiode> bekostigingsperiodes;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "verbintenis")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	private List<VerbintenisVrijVeld> vrijVelden;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "verbintenis")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	private List<VerbintenisContract> contracten;

	@Lob
	@Column(nullable = true)
	@AutoForm(htmlClasses = "unit_max")
	private String toelichting;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@Bron(verplicht = true)
	private Intensiteit intensiteit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "relevanteVooropleiding", nullable = true)
	@Index(name = "idx_Verbintenis_relVooropl")
	@Bron(verplicht = true)
	private Vooropleiding relevanteVooropleidingVooropleiding;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "relevanteVerbintenis", nullable = true)
	@Index(name = "idx_Verbintenis_relVerb")
	@Bron(verplicht = true)
	@FieldPersistance(FieldPersistenceMode.SAVE)
	private Verbintenis relevanteVerbintenis;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@AutoForm(htmlClasses = "unit_max")
	private Vertrekstatus vertrekstatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vervolgonderwijs", nullable = true)
	@Index(name = "idx_Verbintenis_vervolgonder")
	@AutoFormEmbedded
	private Vervolgonderwijs vervolgonderwijs;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "verbintenis")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	private List<Examendeelname> examendeelnames = new ArrayList<Examendeelname>();

	@Bron
	@Column(nullable = true, scale = 10, precision = 20)
	@AutoForm(label = "Contacturen per week (klokuren)")
	private BigDecimal contacturenPerWeek;

	/**
	 * De bijlages van deze verbintenis
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "verbintenis")
	private List<VerbintenisBijlage> bijlagen;

	/**
	 * Bij inburgeraars moet voor rapportage aan opdrachtgevers worden aangegeven of de
	 * deelnemers al of niet verplicht moeten inburgeren. Veld is niet meer nodig voor het
	 * keurmerk inburgering, maar opdrachtgevers willen het wel weten. Verplicht veld voor
	 * inburgeringsverbintenissen.
	 */
	public static enum RedenInburgering
	{
		Inburgeringsplichtig,
		Inburgeringsbehoeftig;
	}

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private RedenInburgering redenInburgering;

	/**
	 * Bij inburgering en staatsexamen dient een examendatum aangegeven te worden
	 */
	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date examenDatum;

	/**
	 * Soort staatsexamen dat een deelnemer doet
	 */
	public static enum StaatsExamenType
	{
		StaatsExamen1,
		StaatsExamen2;
	}

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private StaatsExamenType staatsExamenType;

	/**
	 * Soort inburgering die de inburgeraar volgt. Afhankelijk van het profiel zal men
	 * bepaalde onderwijsproducten willen aanbieden.
	 */
	public static enum ProfielInburgering
	{
		/**
		 * Opvoeding, Gezondheidszorg en Onderwijs
		 */
		OGO,
		Werk,
		Ondernemerschap,
		MaatschappelijkeParticipatie;
	}

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private ProfielInburgering profielInburgering;

	/**
	 * Voor inburgering. Geeft min of meer het niveau van de inburgeraar aan. Nodig voor
	 * rapportage aan het keurmerk inburgering en waarschijnlijk nuttig om mensen van
	 * hetzelfde niveau bij elkaar in de klas te kunnen zetten.
	 */
	public static enum Leerprofiel
	{
		P1a,
		P1b,
		P1c,
		P2,
		P3,
		P4;

		@Override
		public String toString()
		{
			return name().substring(1);
		}
	}

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private Leerprofiel leerprofiel;

	/**
	 * Voor inburgering. Wordt gebruikt voor rapportage aan het keurmerk inburgering.
	 */
	@Column(nullable = true)
	private Boolean deelcursus;

	/**
	 * Voor inburgering. De wijze waarop het praktijkexamen zal worden afgenomen. Dient te
	 * worden opgenomen in de onderwijsovereenkomst.
	 */
	public static enum SoortPraktijkexamen
	{
		Portfolio,
		Assessment,
		Combinatie,
	}

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private SoortPraktijkexamen soortPraktijkexamen;

	/**
	 * Voor inburgering. Als verbintenis onder een inburgeringscontract valt, is dit de
	 * datum waarop de opdrachtgever de cursist heeft aangemeld. Nodig voor rapportage aan
	 * het keurmerk inburgering.
	 */
	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	@AutoForm(label = "Datum aanmelden/beschikking", description = "Als verbintenis onder een inburgeringscontract valt, is dit de datum waarop de opdrachtgever de cursist heeft aangemeld. Nodig voor rapportage aan het keurmerk inburgering.")
	private Date datumAanmelden;

	/**
	 * Voor inburgering. Als verbintenis NIET onder een inburgeringscontract valt, is dit
	 * de datum waarop de cursist de onderwijsovereenkomst heeft ondertekend. Nodig voor
	 * rapportage aan het keurmerk inburgering.
	 */
	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	@AutoForm(description = "Als verbintenis NIET onder een inburgeringscontract valt, is dit de datum waarop de cursist de onderwijsovereenkomst heeft getekend. Nodig voor rapportage aan het keurmerk inburgering.")
	private Date datumAkkoord;

	/**
	 * Voor inburgering. Wordt gebruikt voor berekening doorlooptijd voor start van de
	 * cursus (prestatie-indicator 1 van het keurmerk). De verwachte startdatum die in
	 * deze indicator wordt gevraagd, is de begindatum van de verbintenis.
	 */
	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date datumEersteActiviteit;

	@Column(nullable = true, name = "beginNivSchrVaardigheden")
	@Enumerated(EnumType.STRING)
	private NT2Niveau beginNiveauSchriftelijkeVaardigheden;

	@Column(nullable = true, name = "eindNivSchrVaardigheden")
	@Enumerated(EnumType.STRING)
	private NT2Niveau eindNiveauSchriftelijkeVaardigheden;

	/**
	 * Geeft aan of de deelnemer extra financiering met zich meebrengt als gevolg van een
	 * handicap indicatie.
	 */
	@Column(nullable = false)
	@Bron
	private boolean indicatieGehandicapt;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@AutoForm(label = "BRON-status")
	private BronEntiteitStatus bronStatus = BronEntiteitStatus.Geen;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = true)
	@AutoForm(label = "BRON-datum")
	private Date bronDatum;

	/**
	 * Sommige bronsystemen ondersteunen een afwijkende externe code voor een verbintenis.
	 * Dat wil zeggen dat de deelnemer ingeschreven is op een opleiding met externe code
	 * X, maar voor deze deelnemer geldt alsnog externe code Y. Dit veld wordt gebruikt om
	 * deze gegevens mee te kunnen krijgen vanuit deze bronsystemen, maar is niet in te
	 * vullen in de applicatie. De methode 'getExterneCode' houdt rekening met dit
	 * property.
	 */
	@Column(nullable = true, length = 20)
	@AutoForm(include = false)
	private String afwijkendeExterneCode;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "verbintenis")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	private List<IbgVerzuimmelding> verzuimmeldingen;

	@AutoForm(description = "Zorgt dat de deelnemer op basis van deze verbintenis geen facturen meer krijgt, totdat dit veld is uitgeschakeld.")
	private boolean uitsluitenVanFacturatie = false;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "verbintenis")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	private List<Inschrijvingsverzoek> inschrijvingsverzoeken =
		new ArrayList<Inschrijvingsverzoek>();

	@Column(nullable = true)
	@Type(type = "nl.topicus.eduarte.entities.inschrijving.CreditsPerFaseUserType")
	private Map<Hoofdfase, Integer> creditsPerFase;

	@AutoForm(description = "Breng voor deze verbintenis alleen wettelijk collegegeld in rekening, ook als de student niet aan de voorwaarden m.b.t. nationaliteit, woonadres of behaalde graad voldoet.")
	@Column(name = "negeerWettCollGeldVoorwaarden")
	private Boolean negeerWettelijkCollegegeldVoorwaarden = false;

	/**
	 * Default constructor voor Hibernate.
	 * 
	 * @deprecated Deze is deprecated omdat het niet mogelijk is om hem protected te maken
	 *             en hij nodig is voor {@link EduArtePrototypeBeanPropertyResolver}.
	 *             Iedereen die deze constructor toch gebruikt en daarmee een bug
	 *             veroorzaakt trakteert op taart!
	 */
	@Deprecated
	public Verbintenis()
	{
	}

	/**
	 * Constructor op basis van deelnemer. Voegt de verbintenis toe aan de lijst van
	 * verbintenissen van de deelnemer. Instelling wordt tevens automatisch geset.
	 * 
	 * @param deelnemer
	 */
	public Verbintenis(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
		this.volgnummer = deelnemer.getVolgendeVerbintenisVolgnummer();
	}

	@Exportable
	public Date getGeplandeEinddatum()
	{
		return geplandeEinddatum;
	}

	public void setGeplandeEinddatum(Date geplandeEinddatum)
	{
		this.geplandeEinddatum = geplandeEinddatum;
	}

	/**
	 * Berekent de geplande einddatum adhv opleiding-duur en begindatum. Set de geplande
	 * einddatum nog niet.
	 * 
	 * @return geplande einddatum, null indien 1 van de benodigde velden null
	 */
	public Date berekenGeplandeEinddatum()
	{
		Date result = null;
		if (getBegindatum() != null && opleiding != null && opleiding.getDuurInMaanden() != null)
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(getBegindatum());
			cal.add(Calendar.MONTH, opleiding.getDuurInMaanden());
			// Mantis #45435: 1 dag eerder
			cal.add(Calendar.DATE, -1);
			result = cal.getTime();
		}
		return result;
	}

	@Exportable
	public RedenUitschrijving getRedenUitschrijving()
	{
		return redenUitschrijving;
	}

	public void setRedenUitschrijving(RedenUitschrijving redenUitschrijving)
	{
		this.redenUitschrijving = redenUitschrijving;
	}

	/**
	 * @return De status op de huidige datum. Houdt daarbij rekening met de einddatum van
	 *         de verbintenis en retourneert als die in het verleden ligt Beeindigd.
	 */
	@Exportable
	public VerbintenisStatus getStatus()
	{
		return getStatus(TimeUtil.getInstance().currentDate());
	}

	/**
	 * @return De status op de gegeven peildatum. Houdt rekening met de einddatum van de
	 *         verbintenis en retourneert als die voor de peildatum ligt Beeindigd.
	 */
	public VerbintenisStatus getStatus(Date peildatum)
	{
		if (status != null && !status.isAfgesloten() && peildatum != null
			&& peildatum.after(getEinddatumNotNull()))
			return VerbintenisStatus.Beeindigd;
		return status;
	}

	/**
	 * Nodig voor het aanpassen van bv. 'Afgemeld' naar 'Voorlopig' op de
	 * EditVerbintenisPage
	 */
	public VerbintenisStatus getStatusZonderBeeindigdCheck()
	{
		return status;
	}

	public void setStatus(VerbintenisStatus status)
	{
		this.status = status;
	}

	public Date getDatumDefinitief()
	{
		return datumDefinitief;
	}

	public void setDatumDefinitief(Date datumDefinitief)
	{
		this.datumDefinitief = datumDefinitief;
	}

	public Date getDatumGeplaatst()
	{
		return datumGeplaatst;
	}

	public void setDatumGeplaatst(Date datumGeplaatst)
	{
		this.datumGeplaatst = datumGeplaatst;
	}

	@Exportable
	public Date getDatumVoorlopig()
	{
		return datumVoorlopig;
	}

	public void setDatumVoorlopig(Date datumVoorlopig)
	{
		this.datumVoorlopig = datumVoorlopig;
	}

	@Exportable
	public int getVolgnummer()
	{
		return volgnummer;
	}

	/**
	 * In sommige gevallen (bijvoorbeeld bij kopieeren van verbintenis) is het nodig om de
	 * verbintenis een nieuw volgnummer toe te geven.
	 */
	public void wijsNieuwVolgnummerToe()
	{
		volgnummer = getDeelnemer().getVolgendeVerbintenisVolgnummer();
	}

	public void setVolgnummerInOudPakket(String volgnummerInOudPakket)
	{
		this.volgnummerInOudPakket = volgnummerInOudPakket;
	}

	public String getVolgnummerInOudPakket()
	{
		return volgnummerInOudPakket;
	}

	@Override
	@Exportable
	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	@Override
	@Exportable
	public Persoon getPersoon()
	{
		return getDeelnemer().getPersoon();
	}

	@Override
	@Exportable
	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	@Override
	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;
	}

	@AutoForm(label = "Verbintenis")
	@Override
	public String getContextInfoOmschrijving()
	{
		return getOmschrijving(null);
	}

	/**
	 * Geeft de omschrijving van deze verbintenis als een vooropleiding.
	 * 
	 * @see IVooropleiding#getOmschrijving()
	 */
	@Override
	public String getOmschrijving()
	{
		SoortOnderwijs soort = getSoortOnderwijs();
		if (soort != null)
		{
			return soort.getCode() + " - " + getContextInfoOmschrijving();
		}
		return getContextInfoOmschrijving();
	}

	/**
	 * @param voortgang
	 *            De voortgang van deze verbintenis. Wordt toegevoegd aan de omschrijving
	 *            als deze niet null is
	 * @return Een omschrijving van deze verbintenis.
	 */
	public String getOmschrijving(String voortgang)
	{
		StringBuilder titel = new StringBuilder();
		if (getOpleiding() != null)
		{
			titel.append(getOpleiding().getCode()).append(" - ");
			titel.append(getOpleiding().getNaam());
			if (voortgang != null)
			{
				titel.append(" - ").append(voortgang);
			}
		}
		else
		{
			OrganisatieEenheid oe = getOrganisatieEenheid();
			if (oe != null)
			{
				titel.append(oe.getNaam());
			}
		}

		titel.append(" (").append(status).append(")");

		// if (!isActief())
		// {
		// titel = titel + " (Inactief)";
		// }
		return titel.toString();
	}

	/**
	 * @return Alle verbintenisgebieden die onder deze inschrijving vallen. Dwz het
	 *         verbintenisgebied waarop de deelnemer is ingeschreven, alsmede alle
	 *         verbintenisgebieden die onderdeel uitmaken van dit verbintenisgebied.
	 */
	public Set<Verbintenisgebied> getAlleVerbintenisgebieden()
	{
		Set<Verbintenisgebied> res = new HashSet<Verbintenisgebied>(2);
		if (getOpleiding() != null)
			res.add(getOpleiding().getVerbintenisgebied());
		return res;
	}

	@Override
	@Exportable
	public Locatie getLocatie()
	{
		return locatie;
	}

	@Override
	public void setLocatie(Locatie locatie)
	{
		this.locatie = locatie;
	}

	@Exportable
	public Opleiding getOpleiding()
	{
		return opleiding;
	}

	@AutoForm(label = "Opleiding", editorClass = OpleidingQuickSearchField.class)
	public Opleiding getNonembeddedOpleiding()
	{
		return opleiding;
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = opleiding;
	}

	public String getBpvString()
	{
		StringBuilder res = new StringBuilder();
		if (getDeelnemer() != null)
		{
			res.append(getDeelnemer().getDeelnemernummer());
		}
		if (getDeelnemer() != null && getDeelnemer().getPersoon() != null)
		{
			Persoon persoon = getDeelnemer().getPersoon();
			res.append(" ").append(persoon.getVolledigeNaam());
			if (persoon.getFysiekAdres() != null)
				res.append(" ").append(persoon.getFysiekAdres().getAdres().getPlaats());
		}
		return res.toString();
	}

	@Override
	public String toString()
	{
		StringBuilder res = new StringBuilder();
		if (getDeelnemer() != null)
		{
			res.append(getDeelnemer().getDeelnemernummer());
		}
		if (getDeelnemer() != null && getDeelnemer().getPersoon() != null)
		{
			res.append(" ").append(getDeelnemer().getPersoon().getVolledigeNaam());
		}
		if (getOpleiding() != null)
			res.append(" - ").append(getOpleiding().getNaam());
		return res.toString();
	}

	@Exportable
	public List<OnderwijsproductAfnameContext> getAfnameContexten()
	{
		return afnameContexten;
	}

	public void setAfnameContexten(List<OnderwijsproductAfnameContext> afnameContexten)
	{
		this.afnameContexten = afnameContexten;
	}

	@Exportable
	public Cohort getCohort()
	{
		return cohort;
	}

	public void setCohort(Cohort cohort)
	{
		this.cohort = cohort;
	}

	@Exportable
	public List<Plaatsing> getPlaatsingen()
	{
		if (plaatsingen == null)
			plaatsingen = new ArrayList<Plaatsing>();

		return plaatsingen;
	}

	public void setPlaatsingen(List<Plaatsing> plaatsingen)
	{
		this.plaatsingen = plaatsingen;
	}

	public Plaatsing getPlaatsingOpDatum(Date datum)
	{
		return BeginEinddatumUtil.getElementOpPeildatum(getPlaatsingen(), datum);
	}

	@Exportable
	public Plaatsing getPlaatsingOpPeildatum()
	{
		return BeginEinddatumUtil.getElementOpPeildatum(getPlaatsingen(), EduArteContext.get()
			.getPeildatumOfVandaag());
	}

	@Exportable
	public Plaatsing getLaatstePlaatsing()
	{
		Plaatsing ret = null;
		for (Plaatsing plaatsing : getPlaatsingen())
		{
			if (ret == null)
				ret = plaatsing;
			else if (plaatsing.getBegindatum().after(ret.getBegindatum()))
				ret = plaatsing;
		}
		return ret;
	}

	public void setBekostigd(Bekostigd bekostigd)
	{
		this.bekostigd = bekostigd;
	}

	@Exportable
	public Bekostigd getBekostigd()
	{
		return bekostigd;
	}

	public void setBekostigingsperiodes(List<Bekostigingsperiode> bekostiging)
	{
		this.bekostigingsperiodes = bekostiging;
	}

	public List<Bekostigingsperiode> getBekostigingsperiodes()
	{
		if (bekostigingsperiodes == null)
		{
			bekostigingsperiodes = new ArrayList<Bekostigingsperiode>();
		}

		return bekostigingsperiodes;
	}

	/**
	 * Bepaalt aan de hand van de oude waardes voor bekostiging en intensiteit of de
	 * verbintenis bekostigd was op de datum.
	 */
	public boolean wasBekostigdOpDatum(Bekostigd oudeBekostigd, Intensiteit oudeIntensiteit,
			Date datum)
	{
		if (isVOVerbintenis() && oudeIntensiteit != Intensiteit.Examendeelnemer)
			return true;
		else if (!isBOVerbintenis() && !isHOVerbintenis())
			return false;
		if (datum.before(getBegindatum()))
			return false;
		if (oudeBekostigd == Bekostigd.Ja)
			return true;
		if (oudeBekostigd == Bekostigd.Nee)
			return false;
		if (oudeBekostigd == Bekostigd.Gedeeltelijk)
		{
			for (Bekostigingsperiode periode : getBekostigingsperiodes())
			{
				if (periode.isActief(datum))
				{
					return periode.isBekostigd();
				}
			}
		}

		return false;
	}

	/**
	 * Bepaalt aan de hand van actuele waardes van bekostigd en intensiteit of de
	 * verbintenis bekostigd was op de datum.
	 */
	public boolean isBekostigdOpDatum(Date datum)
	{
		return wasBekostigdOpDatum(getBekostigd(), getIntensiteit(), datum);
	}

	public boolean isBekostigdOpPeildatum()
	{
		return isBekostigdOpDatum(EduArteContext.get().getPeildatumOfVandaag());
	}

	@Override
	public List<VerbintenisVrijVeld> getVrijVelden()
	{
		if (vrijVelden == null)
			vrijVelden = new ArrayList<VerbintenisVrijVeld>();

		return vrijVelden;
	}

	@Override
	public List<VerbintenisVrijVeld> getVrijVelden(VrijVeldCategorie categorie)
	{
		List<VerbintenisVrijVeld> res = new ArrayList<VerbintenisVrijVeld>();
		for (VerbintenisVrijVeld pvv : getVrijVelden())
		{
			if (pvv.getVrijVeld().getCategorie().equals(categorie))
			{
				res.add(pvv);
			}
		}
		return res;
	}

	@Override
	public VerbintenisVrijVeld newVrijVeld()
	{
		VerbintenisVrijVeld pvv = new VerbintenisVrijVeld();
		pvv.setVerbintenis(this);

		return pvv;
	}

	@Override
	public void setVrijVelden(List<VerbintenisVrijVeld> vrijvelden)
	{
		this.vrijVelden = vrijvelden;
	}

	public void setToelichting(String toelichting)
	{
		this.toelichting = toelichting;
	}

	@Exportable
	public String getToelichting()
	{
		return toelichting;
	}

	private static final String[] SEARCH_BROWSER_PROPERTIES =
		{"deelnemer.deelnemernummer", "deelnemer.persoon.volledigeNaam", "opleiding.naam"};

	@Override
	public String[] getSearchBrowserProperties()
	{
		return SEARCH_BROWSER_PROPERTIES;
	}

	@Exportable
	public List<Intakegesprek> getIntakegesprekken()
	{
		if (intakegesprekken == null)
			intakegesprekken = new ArrayList<Intakegesprek>();
		return intakegesprekken;
	}

	/**
	 * @return het eerste intakegesprek na nu, anders het meest recente. null indien geen
	 *         intakegesprekken. Intakegesprekken zonder datum/tijd vallen buiten de
	 *         beoordeling.
	 */
	@Exportable
	public Intakegesprek getEerstVolgendeIntakegesprek()
	{
		Intakegesprek eerstvolgende = null;
		Intakegesprek laatste = null;
		for (Intakegesprek gesprek : getIntakegesprekken())
		{
			if (eerstvolgende == null
				|| (gesprek.getDatumTijd() != null && eerstvolgende.getDatumTijd() != null
					&& gesprek.getDatumTijd().after(TimeUtil.getInstance().currentDateTime()) && gesprek
					.getDatumTijd().before(eerstvolgende.getDatumTijd())))
				eerstvolgende = gesprek;
			if (laatste == null
				|| (gesprek.getDatumTijd() != null && laatste.getDatumTijd() != null && gesprek
					.getDatumTijd().after(laatste.getDatumTijd())))
				laatste = gesprek;
		}
		if (eerstvolgende != null)
			return eerstvolgende;
		return laatste;
	}

	public void setIntakegesprekken(List<Intakegesprek> intakegesprekken)
	{
		this.intakegesprekken = intakegesprekken;
	}

	@Exportable
	public String getIntakegesprekStatussen()
	{
		return StringUtil.toString(getIntakegesprekken(), "",
			new PropertyStringConverter<Intakegesprek>("status"));
	}

	@Exportable
	public String getIntakegesprekkenDatumTijd()
	{
		return StringUtil.toString(getIntakegesprekken(), "",
			new PropertyStringConverter<Intakegesprek>("datumTijdFormatted"));
	}

	@Exportable
	public String getIntakegesprekkenIntaker()
	{
		return StringUtil.toString(getIntakegesprekken(), "",
			new PropertyStringConverter<Intakegesprek>("intaker.persoon.volledigeNaam"));
	}

	@Exportable
	public String getIntakegesprekkenGewensteOpleiding()
	{
		return StringUtil.toString(getIntakegesprekken(), "",
			new PropertyStringConverter<Intakegesprek>("gewensteOpleiding.naam"));
	}

	@Exportable
	public String getIntakegesprekkenGewensteBpv()
	{
		return StringUtil.toString(getIntakegesprekken(), "",
			new PropertyStringConverter<Intakegesprek>("gewensteBPV.naam"));
	}

	@Exportable
	public String getIntakegesprekkenGewensteLocatie()
	{
		return StringUtil.toString(getIntakegesprekken(), "",
			new PropertyStringConverter<Intakegesprek>("gewensteLocatie.naam"));
	}

	@Exportable
	public String getIntakegesprekkenGewensteBegindatum()
	{
		return StringUtil.toString(getIntakegesprekken(), "",
			new PropertyStringConverter<Intakegesprek>("gewensteBegindatumNL"));
	}

	@Exportable
	public String getIntakegesprekkenGewensteEinddatum()
	{
		return StringUtil.toString(getIntakegesprekken(), "",
			new PropertyStringConverter<Intakegesprek>("gewensteEinddatum"));
	}

	@Exportable
	public String getIntakegesprekkenOrganisatieEenheid()
	{
		return StringUtil.toString(getIntakegesprekken(), "",
			new PropertyStringConverter<Intakegesprek>("organisatieEenheid.naam"));
	}

	@Exportable
	public String getIntakegesprekkenOpmerking()
	{
		return StringUtil.toString(getIntakegesprekken(), "",
			new PropertyStringConverter<Intakegesprek>("opmerking"));
	}

	@Exportable
	public String getContractOmschrijvingOpPeildatum()
	{
		StringBuilder res = new StringBuilder();
		Set<Contract> contractenSet = new HashSet<Contract>();
		for (VerbintenisContract contract : getContractenOpPeildatum())
		{
			contractenSet.add(contract.getContract());
		}
		int cnt = 0;
		for (Contract contract : contractenSet)
		{
			if (cnt > 0)
			{
				res.append("<br/><br/>");
			}
			res.append(contract.getNaam());
			int counter = 0;
			for (VerbintenisContract vc : getContractenOpPeildatum())
			{
				if (vc.getContract().equals(contract))
				{
					if (vc.getOnderdeel() != null)
					{
						res.append("<br/>");
						res.append(StringUtil.repeatString("&nbsp;", 15));
						res.append(" ").append(vc.getOnderdeel().getNaam());
						res.append(" ").append(vc.getGeldigVanTotBeschrijving());
					}
					else
					{
						res.append(" ").append(vc.getGeldigVanTotBeschrijving());
					}
					counter++;
				}
			}
			cnt++;
		}

		return res.toString();
	}

	@Exportable
	public List<VerbintenisContract> getContracten()
	{
		if (contracten == null)
			contracten = new ArrayList<VerbintenisContract>();
		return contracten;
	}

	public void setContracten(List<VerbintenisContract> contracten)
	{
		this.contracten = contracten;
	}

	@Exportable
	public List<VerbintenisContract> getContractenOpPeildatum()
	{
		return BeginEinddatumUtil.getElementenOpPeildatum(getContracten(), EduArteContext.get()
			.getPeildatum());
	}

	public List<ContractOnderdeel> getContractonderdelenOpPeildatum()
	{
		List<VerbintenisContract> list =
			BeginEinddatumUtil.getElementenOpPeildatum(getContracten(), EduArteContext.get()
				.getPeildatum());
		List<ContractOnderdeel> onderdelen = new ArrayList<ContractOnderdeel>(list.size());
		for (VerbintenisContract vc : list)
		{
			if (vc.getOnderdeel() != null)
			{
				onderdelen.add(vc.getOnderdeel());
			}
		}
		return onderdelen;
	}

	public String getContractcodesOpPeildatum()
	{
		return StringUtil.toString(getContractenOpPeildatum(), "",
			new PropertyStringConverter<VerbintenisContract>("contract.code"));
	}

	public String getContractnamenOpPeildatum()
	{
		return StringUtil.toString(getContractenOpPeildatum(), "",
			new PropertyStringConverter<VerbintenisContract>("contract.naam"));
	}

	public String getContractOnderdeelNamenOpPeildatum()
	{
		return StringUtil.toString(getContractonderdelenOpPeildatum(), "",
			new PropertyStringConverter<ContractOnderdeel>("naam"));
	}

	public String getContractBegindataOpPeildatum()
	{
		return StringUtil.toString(getContractenOpPeildatum(), "",
			new PropertyStringConverter<VerbintenisContract>("begindatumFormatted"));
	}

	public String getContractEinddataOpPeildatum()
	{
		return StringUtil.toString(getContractenOpPeildatum(), "",
			new PropertyStringConverter<VerbintenisContract>("einddatumFormatted"));
	}

	public String getContractExterneOrganisatieNamenOpPeildatum()
	{
		return StringUtil.toString(getContractenOpPeildatum(), "",
			new PropertyStringConverter<VerbintenisContract>("contract.externeOrganisatie.naam"));
	}

	public String getContractExterneOrganisatieDebiteurennummersOpPeildatum()
	{
		return StringUtil.toString(getContractenOpPeildatum(), "",
			new PropertyStringConverter<VerbintenisContract>(
				"contract.externeOrganisatie.debiteurennummer"));
	}

	/**
	 * @return <code>true</code> wanneer de verbintenis een status heeft waar deze met
	 *         BRON gecommuniceerd moet worden.
	 */
	public boolean isBronCommuniceerbaar()
	{
		return getStatus().isBronCommuniceerbaar()
			&& (getOpleiding() == null || getOpleiding().isCommunicerenMetDUO());
	}

	public boolean isOpleidingBronCommuniceerbaar()
	{
		return getOpleiding() != null && getOpleiding().isCommunicerenMetDUO();
	}

	/**
	 * @return De externe code van het verbintenisgebied waarop de deelnemer is
	 *         ingeschreven. Hierbij wordt rekening gehouden met een eventuele
	 *         lwoo-beschikking voor vmbo-deelnemers. De code die deze methode teruggeeft
	 *         is dus ook wat naar BRON gestuurd zou moeten worden.
	 */
	@Exportable
	public String getExterneCode()
	{
		if (getAfwijkendeExterneCode() != null)
			return getAfwijkendeExterneCode();
		if (getOpleiding() != null)
		{
			Verbintenisgebied verbintenisgebied = getVerbintenisgebied();
			return verbintenisgebied.getExterneCode();
		}
		return null;
	}

	private Verbintenisgebied getVerbintenisgebied()
	{
		if (getOpleiding() != null)
		{
			Verbintenisgebied verbintenisgebied =
				(Verbintenisgebied) getOpleiding().getVerbintenisgebied().doUnproxy();
			Plaatsing plaatsing = getPlaatsingOpPeildatum();
			if (plaatsing == null)
				plaatsing = getLaatstePlaatsing();
			if (plaatsing != null && plaatsing.isLwoo())
			{
				// De deelnemer zou ingeschreven moeten zijn op een elementcode.
				if (Elementcode.class.isAssignableFrom(verbintenisgebied.getClass()))
				{
					Elementcode elementcode = (Elementcode) verbintenisgebied;
					if (elementcode.getLwooTaxonomieElement() != null)
					{
						return (Verbintenisgebied) elementcode.getLwooTaxonomieElement()
							.doUnproxy();
					}
				}
			}
			return verbintenisgebied;
		}
		return null;
	}

	/**
	 * @return Het label van de externe code: "Crebo-code" voor MBO en CGO, "Elementcode"
	 *         voor VO, "Externe code" voor overig
	 */
	@Exportable
	public String getExterneCodeLabel()
	{
		String code = "Externe code";
		if (getOpleiding() != null && getOpleiding().getVerbintenisgebied() != null
			&& getTaxonomie() != null)
		{
			Taxonomie taxonomie = getTaxonomie();

			if (taxonomie.isVO())
				code = "Elementcode";
			else if (taxonomie.isBO())
			{
				code = "Crebo-code";
			}
		}
		return code;
	}

	@Exportable
	public List<BPVInschrijving> getBpvInschrijvingen()
	{
		return bpvInschrijvingen;
	}

	public void setBpvInschrijvingen(List<BPVInschrijving> bpvInschrijvingen)
	{
		this.bpvInschrijvingen = bpvInschrijvingen;
	}

	public List<BPVInschrijving> getActieveBpvInschrijvingen()
	{
		BPVInschrijvingZoekFilter filter = new BPVInschrijvingZoekFilter();
		filter.setVerbintenis(this);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			new AlwaysGrantedSecurityCheck()));

		List<BPVStatus> statusOngelijkAan = new ArrayList<BPVStatus>();
		statusOngelijkAan.add(BPVStatus.Afgemeld);
		statusOngelijkAan.add(BPVStatus.Afgewezen);
		filter.setBpvStatusOngelijkAanList(statusOngelijkAan);
		filter.addOrderByProperty("begindatum");
		filter.setAscending(false);

		return DataAccessRegistry.getHelper(BPVInschrijvingDataAccessHelper.class).list(filter);
	}

	/**
	 * Eerste BPV-inschrijving op de peildatum
	 */
	@Exportable
	public BPVInschrijving getBPVInschrijvingOpPeildatum()
	{
		for (BPVInschrijving currentBPVInschrijving : getBpvInschrijvingen())
		{
			if (currentBPVInschrijving.isActief(EduArteContext.get().getPeildatum()))
				return currentBPVInschrijving;
		}
		return null;
	}

	public List<BPVInschrijving> getBPVInschrijvingenOpPeildatum()
	{
		List<BPVInschrijving> res = new ArrayList<BPVInschrijving>();
		for (BPVInschrijving currentBPVInschrijving : getBpvInschrijvingen())
		{
			if (currentBPVInschrijving.isActief(EduArteContext.get().getPeildatum()))
				res.add(currentBPVInschrijving);
		}
		return res;
	}

	public String getBPVTelefoonnummersOpPeildatum()
	{
		return StringUtil.toString(getBPVInschrijvingenOpPeildatum(), "",
			new StringUtil.StringConverter<BPVInschrijving>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ", ";
				}

				@Override
				public String toString(BPVInschrijving object, int listIndex)
				{
					ExterneOrganisatieContactgegeven ret =
						ContacteerbaarUtil.getEersteTelefoon(object.getBpvBedrijf());
					return ret == null ? null : ret.getFormattedContactgegeven();
				}
			});
	}

	private List<Adres> getBPVBezoekAdressenOpPeildatum()
	{
		List<Adres> ret = new ArrayList<Adres>();
		for (BPVInschrijving curInschrijving : getBPVInschrijvingenOpPeildatum())
		{
			ret.add(curInschrijving.getBpvBedrijf().getFysiekAdres().getAdres());
		}
		return ret;
	}

	public String getBPVBedrijvenBezoekadresOpPeildatumFormatted()
	{
		return StringUtil.toString(getBPVBezoekAdressenOpPeildatum(), "",
			new StringUtil.StringConverter<Adres>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ", ";
				}

				@Override
				public String toString(Adres object, int listIndex)
				{
					return object.getVolledigAdresOp1Regel();
				}
			});
	}

	public String getBPVBedrijvenBezoekStraatHuisnummerOpPeildatumFormatted()
	{
		return StringUtil.toString(getBPVBezoekAdressenOpPeildatum(), "",
			new StringUtil.StringConverter<Adres>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ", ";
				}

				@Override
				public String toString(Adres object, int listIndex)
				{
					return object.getStraatHuisnummerFormatted();
				}
			});
	}

	public String getBPVBedrijvenBezoekPostcodePlaatsOpPeildatumFormatted()
	{
		return StringUtil.toString(getBPVBezoekAdressenOpPeildatum(), "",
			new StringUtil.StringConverter<Adres>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ", ";
				}

				@Override
				public String toString(Adres object, int listIndex)
				{
					return object.getPostcodePlaatsFormatted();
				}
			});
	}

	public String getBPVBedrijvenBezoekGemeenteOpPeildatumFormatted()
	{
		return StringUtil.toString(getBPVBezoekAdressenOpPeildatum(), "",
			new StringUtil.StringConverter<Adres>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ", ";
				}

				@Override
				public String toString(Adres object, int listIndex)
				{
					return object.getGemeenteFormatted();
				}
			});
	}

	public String getBPVBedrijvenBezoekProvincieOpPeildatumFormatted()
	{
		return StringUtil.toString(getBPVBezoekAdressenOpPeildatum(), "",
			new StringUtil.StringConverter<Adres>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ", ";
				}

				@Override
				public String toString(Adres object, int listIndex)
				{
					String ret = object.getProvincieFormatted();
					return ret == null ? "" : ret;
				}
			});
	}

	private List<Adres> getBPVPostAdressenOpPeildatum()
	{
		List<Adres> ret = new ArrayList<Adres>();
		for (BPVInschrijving curInschrijving : getBPVInschrijvingenOpPeildatum())
		{
			ret.add(curInschrijving.getBpvBedrijf().getPostAdres().getAdres());
		}
		return ret;
	}

	public String getBPVBedrijvenPostadresOpPeildatumFormatted()
	{
		return StringUtil.toString(getBPVPostAdressenOpPeildatum(), "",
			new StringUtil.StringConverter<Adres>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ", ";
				}

				@Override
				public String toString(Adres object, int listIndex)
				{
					return object.getVolledigAdresOp1Regel();
				}
			});
	}

	public String getBPVBedrijvenPostStraatHuisnummerOpPeildatumFormatted()
	{
		return StringUtil.toString(getBPVPostAdressenOpPeildatum(), "",
			new StringUtil.StringConverter<Adres>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ", ";
				}

				@Override
				public String toString(Adres object, int listIndex)
				{
					return object.getStraatHuisnummerFormatted();
				}
			});
	}

	public String getBPVBedrijvenPostPostcodePlaatsOpPeildatumFormatted()
	{
		return StringUtil.toString(getBPVPostAdressenOpPeildatum(), "",
			new StringUtil.StringConverter<Adres>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ", ";
				}

				@Override
				public String toString(Adres object, int listIndex)
				{
					return object.getPostcodePlaatsFormatted();
				}
			});
	}

	public String getBPVBedrijvenPostGemeenteOpPeildatumFormatted()
	{
		return StringUtil.toString(getBPVPostAdressenOpPeildatum(), "",
			new StringUtil.StringConverter<Adres>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ", ";
				}

				@Override
				public String toString(Adres object, int listIndex)
				{
					return object.getGemeenteFormatted();
				}
			});
	}

	public String getBPVBedrijvenPostProvincieOpPeildatumFormatted()
	{
		return StringUtil.toString(getBPVPostAdressenOpPeildatum(), "",
			new StringUtil.StringConverter<Adres>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ", ";
				}

				@Override
				public String toString(Adres object, int listIndex)
				{
					String ret = object.getProvincieFormatted();
					return ret == null ? "" : ret;
				}
			});
	}

	public String getBPVBedrijvenOpPeildatum()
	{
		return StringUtil.toString(getBPVInschrijvingenOpPeildatum(), "",
			new StringUtil.StringConverter<BPVInschrijving>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ", ";
				}

				@Override
				public String toString(BPVInschrijving object, int listIndex)
				{
					return object.getBpvBedrijf().getNaam();
				}
			});
	}

	public String getBetalingsplichtigBPVBedrijf()
	{
		StringBuilder builder = new StringBuilder();
		for (BPVInschrijving bpv : getBpvInschrijvingen())
		{
			if (Boolean.TRUE.equals(bpv.getNeemtBetalingsplichtOver()))
			{
				if (builder.length() > 0)
					builder.append(", ");
				builder.append(bpv.getBpvBedrijf().getNaam());
			}
		}
		return builder.toString();
	}

	public String getBPVAfsluitdataOpPeildatum()
	{
		return StringUtil.toString(getBPVInschrijvingenOpPeildatum(), "",
			new StringUtil.StringConverter<BPVInschrijving>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(BPVInschrijving object, int listIndex)
				{
					return TimeUtil.getInstance().formatDate(object.getAfsluitdatum());
				}
			});
	}

	public String getBPVVerwachteEinddataOpPeildatum()
	{
		return StringUtil.toString(getBPVInschrijvingenOpPeildatum(), "",
			new StringUtil.StringConverter<BPVInschrijving>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ", ";
				}

				@Override
				public String toString(BPVInschrijving object, int listIndex)
				{
					String res = TimeUtil.getInstance().formatDate(object.getVerwachteEinddatum());
					return (res == null) ? "onbekend" : res;
				}
			});
	}

	public String getBPVEinddataOpPeildatum()
	{
		return StringUtil.toString(getBPVInschrijvingenOpPeildatum(), "",
			new StringUtil.StringConverter<BPVInschrijving>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ", ";
				}

				@Override
				public String toString(BPVInschrijving object, int listIndex)
				{
					String res = TimeUtil.getInstance().formatDate(object.getEinddatum());
					return (res == null) ? "onbekend" : res;
				}
			});
	}

	public String getBPVBegindataOpPeildatum()
	{
		return StringUtil.toString(getBPVInschrijvingenOpPeildatum(), "",
			new StringUtil.StringConverter<BPVInschrijving>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(BPVInschrijving object, int listIndex)
				{
					return TimeUtil.getInstance().formatDate(object.getBegindatum());
				}
			});
	}

	public String getBPVCodesLeerbedrijfOpPeildatum()
	{
		return StringUtil.toString(getBPVInschrijvingenOpPeildatum(), "",
			new StringUtil.StringConverter<BPVInschrijving>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(BPVInschrijving object, int listIndex)
				{
					return object.getBedrijfsgegeven() == null ? "" : object.getBedrijfsgegeven()
						.getCodeLeerbedrijf();
				}
			});
	}

	public String getBPVContractpartnersOpPeildatum()
	{
		return StringUtil.toString(getBPVInschrijvingenOpPeildatum(), "",
			new StringUtil.StringConverter<BPVInschrijving>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(BPVInschrijving object, int listIndex)
				{
					return object.getContractpartner() == null ? "" : object.getContractpartner()
						.getNaam();
				}
			});
	}

	public String getBPVContactpersonenContractpartnerOpPeildatum()
	{
		return StringUtil.toString(getBPVInschrijvingenOpPeildatum(), "",
			new StringUtil.StringConverter<BPVInschrijving>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(BPVInschrijving object, int listIndex)
				{
					return object.getContactPersoonContractpartner() == null ? "" : object
						.getContactPersoonContractpartner().getNaam();
				}
			});
	}

	public String getBPVPraktijkopleidersBPVBedrijfOpPeildatum()
	{
		return StringUtil.toString(getBPVInschrijvingenOpPeildatum(), "",
			new StringUtil.StringConverter<BPVInschrijving>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(BPVInschrijving object, int listIndex)
				{
					return object.getPraktijkopleiderBPVBedrijf() == null ? "" : object
						.getPraktijkopleiderBPVBedrijf().getNaam();
				}
			});
	}

	public String getBPVStatussenOpPeildatum()
	{
		return StringUtil.toString(getBPVInschrijvingenOpPeildatum(), "",
			new StringUtil.StringConverter<BPVInschrijving>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(BPVInschrijving object, int listIndex)
				{
					return object.getStatus().toString();
				}
			});
	}

	public String getBPVOpnemenInBronOpPeildatum()
	{
		return StringUtil.toString(getBPVInschrijvingenOpPeildatum(), "",
			new StringUtil.StringConverter<BPVInschrijving>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(BPVInschrijving object, int listIndex)
				{
					return object.isOpnemenInBron() ? "Ja" : "Nee";
				}
			});
	}

	public String getBPVPraktijkbiedendeOrganisatieOpPeildatum()
	{
		return StringUtil.toString(getBPVInschrijvingenOpPeildatum(), "",
			new StringUtil.StringConverter<BPVInschrijving>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(BPVInschrijving object, int listIndex)
				{
					return object.getPraktijkbiedendeOrganisatie() == null ? "" : object
						.getPraktijkbiedendeOrganisatie().toString();
				}
			});
	}

	@Exportable
	public Intensiteit getIntensiteit()
	{
		return intensiteit;
	}

	public void setIntensiteit(Intensiteit intensiteit)
	{
		this.intensiteit = intensiteit;
	}

	@Exportable
	public Vooropleiding getRelevanteVooropleidingVooropleiding()
	{
		return relevanteVooropleidingVooropleiding;
	}

	public void setRelevanteVooropleidingVooropleiding(Vooropleiding relevanteVooropleiding)
	{
		this.relevanteVooropleidingVooropleiding = relevanteVooropleiding;
	}

	public void setVertrekstatus(Vertrekstatus vertrekstatus)
	{
		this.vertrekstatus = vertrekstatus;
	}

	@Exportable
	public Vertrekstatus getVertrekstatus()
	{
		return vertrekstatus;
	}

	public void setVervolgonderwijs(Vervolgonderwijs vervolgonderwijs)
	{
		this.vervolgonderwijs = vervolgonderwijs;
	}

	@Exportable
	public Vervolgonderwijs getVervolgonderwijs()
	{
		return vervolgonderwijs;
	}

	@Exportable
	public List<Examendeelname> getExamendeelnames()
	{
		return examendeelnames;
	}

	public void setExamendeelnames(List<Examendeelname> examendeelnames)
	{
		this.examendeelnames = examendeelnames;
	}

	public Examendeelname getExamendeelname()
	{
		return getExamendeelnames().size() == 0 ? null : getExamendeelnames().get(0);
	}

	public List<IbgVerzuimmelding> getVerzuimmeldingen()
	{
		return verzuimmeldingen;
	}

	public void setVerzuimmeldingen(List<IbgVerzuimmelding> meldingen)
	{
		verzuimmeldingen = meldingen;
	}

	@Exportable
	@AutoFormEmbedded(includeProperties = {"datumUitslag", "bekostigd"})
	public Examendeelname getLaatsteExamendeelname()
	{
		List<Examendeelname> res = new ArrayList<Examendeelname>(getExamendeelnames());
		Collections.sort(res);
		if (!res.isEmpty())
		{
			return res.get(res.size() - 1);
		}
		return null;
	}

	@Override
	@Exportable
	public boolean isDiplomaBehaald()
	{
		// Vraag de laatste examendeelname op en controleer of het diploma behaald is.
		Examendeelname deelname = getLaatsteExamendeelname();
		if (deelname != null)
		{
			if (deelname.getExamenstatus().isGeslaagd())
			{
				return true;
			}
		}
		return false;
	}

	public String getDiplomaBehaaldOmschrijving()
	{
		return isDiplomaBehaald() ? "Ja" : "Nee";
	}

	@Exportable
	public Date getDatumExamenuitslag()
	{
		// Vraag de laatste examendeelname op en controleer of het diploma behaald is.
		Examendeelname deelname = getLaatsteExamendeelname();
		if (deelname != null)
		{
			return deelname.getDatumUitslag();
		}
		return null;
	}

	@Exportable
	public BigDecimal getContacturenPerWeek()
	{
		return contacturenPerWeek;
	}

	public void setContacturenPerWeek(BigDecimal contacturenPerWeek)
	{
		this.contacturenPerWeek = contacturenPerWeek;
	}

	/**
	 * 
	 * @return true indien er bij deze verbintenis een examendeelname is die nog actief
	 *         is.
	 */
	public boolean heeftActieveExamendeelname()
	{
		for (Examendeelname examendeelname : getExamendeelnames())
		{
			if (!examendeelname.getExamenstatus().isEindstatus())
			{
				return true;
			}
		}
		return false;
	}

	public boolean heeftBekostigdeExamendeelnameInKalenderjaar(int kalenderjaar)
	{
		for (Examendeelname examendeelname : getExamendeelnames())
		{
			if (examendeelname.isBekostigd())
			{
				if (examendeelname.getExamenjaar() != null
					&& examendeelname.getExamenjaar().intValue() == kalenderjaar)
				{
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public VerbintenisBijlage addBijlage(Bijlage bijlage)
	{
		VerbintenisBijlage newBijlage = new VerbintenisBijlage();
		newBijlage.setBijlage(bijlage);
		newBijlage.setVerbintenis(this);
		newBijlage.setDeelnemer(getDeelnemer());

		getBijlagen().add(newBijlage);

		return newBijlage;
	}

	@Override
	public boolean bestaatBijlage(Bijlage bijlage)
	{
		for (VerbintenisBijlage deelbijlage : getBijlagen())
		{
			if (deelbijlage.getBijlage().equals(bijlage))
				return true;
		}
		return false;
	}

	@Override
	@Exportable
	public List<VerbintenisBijlage> getBijlagen()
	{
		if (bijlagen == null)
			bijlagen = new ArrayList<VerbintenisBijlage>();
		return bijlagen;
	}

	@Override
	public void setBijlagen(List<VerbintenisBijlage> bijlagen)
	{
		this.bijlagen = bijlagen;
	}

	/**
	 * Maakt een lijst van *NIEUWE* onderwijsproductafnamecontexten voor de verplichte
	 * productregels met maar 1 keuze voor deze verbintenis. De aanroeper moet vervolgens
	 * deze objecten opslaan. Deze methode geeft een lege lijst indien de verbintenis nog
	 * niet aan een opleiding en cohort gekoppeld is.
	 * 
	 * @return Een lijst met nieuwe onderwijsproductafnamecontexten voor de verplichte
	 *         productregels van deze verbintenis.
	 */
	private List<OnderwijsproductAfnameContext> createDefaultOnderwijsproductAfnameContexten()
	{
		OnderwijsproductAfnameZoekFilter filter =
			new OnderwijsproductAfnameZoekFilter(getDeelnemer());
		filter.setCohort(getCohort());
		filter.setCustomPeildatumModel(new Model<Date>());
		List<OnderwijsproductAfname> huidigeAfnames =
			DataAccessRegistry.getHelper(OnderwijsproductAfnameDataAccessHelper.class).list(filter);
		List<OnderwijsproductAfnameContext> res = new ArrayList<OnderwijsproductAfnameContext>();
		if (getOpleiding() != null && getCohort() != null)
		{
			List<Productregel> productregels =
				getOpleiding().getLandelijkeEnLokaleProductregels(getCohort());
			for (Productregel regel : productregels)
			{
				if (regel.isVerplicht())
				{
					Set<Onderwijsproduct> producten =
						regel.getOnderwijsproducten(getOpleiding(), false, false);
					if (producten.size() == 1)
					{
						// Haal al bestaande afname op.
						OnderwijsproductAfname afname = null;
						for (OnderwijsproductAfname oa : huidigeAfnames)
						{
							if (oa.getOnderwijsproduct().equals(producten.iterator().next()))
							{
								afname = oa;
								break;
							}
						}
						if (afname == null)
						{
							afname = new OnderwijsproductAfname();
							afname.setBegindatum(getBegindatum());
							afname.setCohort(getCohort());
							afname.setDeelnemer(getDeelnemer());
							afname.setOnderwijsproduct(producten.iterator().next());
						}
						OnderwijsproductAfnameContext context = new OnderwijsproductAfnameContext();
						context.setOnderwijsproductAfname(afname);
						context.setProductregel(regel);
						context.setVerbintenis(this);
						res.add(context);
						afname.getAfnameContexten().add(context);
					}
				}
			}
		}
		return res;
	}

	public boolean heeftKeuzeVoorProductregel(Productregel productregel)
	{
		for (OnderwijsproductAfnameContext context : getAfnameContexten())
		{
			if (context.getProductregel().equals(productregel))
			{
				return true;
			}
		}
		return false;
	}

	@Exportable
	public List<OnderwijsproductAfname> getOnderwijsproductAfnames()
	{
		List<OnderwijsproductAfname> afnames = new ArrayList<OnderwijsproductAfname>();
		for (OnderwijsproductAfnameContext context : getAfnameContexten())
		{
			afnames.add(context.getOnderwijsproductAfname());
		}
		return afnames;
	}

	/**
	 * @return De onderwijsproduct afnamecontext die gekoppeld is aan het onderwijsproduct
	 *         met de gegeven zoekcode. Wordt gebruikt voor rapportage.
	 */
	@Exportable
	public OnderwijsproductAfnameContext getAfnameContext(String onderwijsproductZoekcode)
	{
		for (OnderwijsproductAfnameContext context : getAfnameContexten())
		{
			if (context.getOnderwijsproductAfname().getOnderwijsproduct().getCode().equals(
				onderwijsproductZoekcode))
			{
				return context;
			}
		}
		return null;
	}

	public OnderwijsproductAfnameContext getAfnameContext(Resultaatstructuur structuur)
	{
		for (OnderwijsproductAfnameContext context : getAfnameContexten())
		{
			OnderwijsproductAfname curAfname = context.getOnderwijsproductAfname();
			if (curAfname.getCohort().equals(structuur.getCohort())
				&& curAfname.getOnderwijsproduct().equals(structuur.getOnderwijsproduct()))
				return context;
		}
		return null;
	}

	/**
	 * 
	 * @param afname
	 * @return De onderwijsproductafnamecontext voor de gegeven onderwijsproductafname, of
	 *         null indien er geen afnamecontext bestaat die naar deze
	 *         onderwijsproductafname verwijst.
	 */
	public OnderwijsproductAfnameContext getAfnameContext(OnderwijsproductAfname afname)
	{
		if (afname == null || afname.getOnderwijsproduct() == null)
			return null;
		for (OnderwijsproductAfnameContext context : getAfnameContexten())
		{
			if (context.getOnderwijsproductAfname().equals(afname))
			{
				return context;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param onderwijsproduct
	 * @return true als het gegeven onderwijsproduct al gebruikt is bij een
	 *         onderwijsproductafnamecontext binnen dit verbintenis, en anders false.
	 */
	public boolean heeftOnderwijsproductGekozenBinnenVerbintenis(Onderwijsproduct onderwijsproduct)
	{
		for (OnderwijsproductAfnameContext context : getAfnameContexten())
		{
			if (context.getOnderwijsproductAfname().getOnderwijsproduct().equals(onderwijsproduct))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param afname
	 * @return true als de gegeven onderwijsproductafname een onderdeel is van het
	 *         combinatievak bij VO/VAVO
	 */
	public boolean isOnderdeelVanCombinatievak(OnderwijsproductAfname afname)
	{
		if (getOpleiding() == null)
			return false;
		if (!getTaxonomie().isVO())
			return false;
		List<OnderwijsproductAfnameContext> keuzes = getAfnameContexten();
		for (Productregel productregel : getOpleiding().getLandelijkeEnLokaleProductregels(
			getCohort()))
		{
			if (productregel.getTypeProductregel() == TypeProductregel.AfgeleideProductregel)
			{
				// TOOD: Eigen productregelsoort maken voor combinatievak.
				if (productregel.isLandelijk())
				{
					return productregel.isOnderdeelVanAfgeleideProductregel(getOpleiding(), afname,
						keuzes);
				}
			}
		}

		return false;
	}

	/**
	 * Maakt default productregelkeuzes aan voor deze verbintenis en slaat deze op in de
	 * database (batch). Eventuele oude niet meer geldige productregelkeuzes worden
	 * verwijderd.
	 */
	public void maakDefaultProductregelKeuzes()
	{
		boolean heeftOngeldigeKeuze = false;
		for (OnderwijsproductAfnameContext context : getAfnameContexten())
		{
			if (getOpleiding() == null
				|| (context.getProductregel().getOpleiding() != null && !context.getProductregel()
					.getOpleiding().equals(getOpleiding()))
				|| (context.getProductregel().getOpleiding() == null && !context.getProductregel()
					.getVerbintenisgebied().equals(getOpleiding().getVerbintenisgebied())))
			{
				heeftOngeldigeKeuze = true;
				break;
			}
		}
		if (heeftOngeldigeKeuze)
		{
			// Verwijder alle huidige keuzes.
			for (OnderwijsproductAfnameContext context : getAfnameContexten())
			{
				context.delete();
			}
			// flush nodig om de deletes voor de inserts te krijgen.
			flush();
			getAfnameContexten().clear();
		}
		if (getOpleiding() != null && getAfnameContexten().isEmpty())
		{
			// Maak deafult onderwijsproductafnames aan.
			List<OnderwijsproductAfnameContext> contexten =
				createDefaultOnderwijsproductAfnameContexten();
			for (OnderwijsproductAfnameContext context : contexten)
			{
				context.getOnderwijsproductAfname().saveOrUpdate();
				context.save();
				getAfnameContexten().add(context);
			}
		}
	}

	/**
	 * Bepaalt of de verbintenis betrekking heeft op een VAVO opleiding.
	 */
	public boolean isVAVOVerbintenis()
	{
		// VO opleidingen (ook VAVO) hebben een verbintenisgebied dat een Elementcode is.
		Elementcode elementcode = getElementcode();
		if (elementcode == null)
			return false;
		return elementcode.isVAVO();
	}

	/**
	 * Bepaalt of de verbintenis betrekking heeft op een BVE-verbintenis oftewel BO, VAVO
	 * of Educatie. Alleen VO-verbintenissen en contractonderwijs vallen hier niet onder.
	 */
	public boolean isBVEVerbintenis()
	{
		return isBOVerbintenis() || isVAVOVerbintenis() || isEducatieVerbintenis();
	}

	public boolean isLNV()
	{
		if (getKwalificatie() != null)
			return getKwalificatie().isLnv();
		return false;
	}

	public boolean isLwoo()
	{
		return getPlaatsingOpPeildatum() != null ? getPlaatsingOpPeildatum().isLwoo() : false;
	}

	public Kwalificatie getKwalificatie()
	{
		if (getOpleiding() == null)
			return null;
		Verbintenisgebied verbintenisgebied = getVerbintenisgebied();
		if (verbintenisgebied == null)
			return null;
		if (Kwalificatie.class.isAssignableFrom(Hibernate.getClass(verbintenisgebied)))
		{
			Kwalificatie kwalificatie = (Kwalificatie) verbintenisgebied.doUnproxy();
			return kwalificatie;
		}
		return null;
	}

	/**
	 * @return de elementcode van de gevolgde opleiding, of <code>null</code> als de
	 *         opleiding geen elementcode als verbintenisgebied heeft (of zelf
	 *         <code>null</code> is)
	 */
	private Elementcode getElementcode()
	{
		if (getOpleiding() == null)
			return null;
		Verbintenisgebied verbintenisgebied = getVerbintenisgebied();
		if (verbintenisgebied == null)
			return null;
		if (Elementcode.class.isAssignableFrom(Hibernate.getClass(verbintenisgebied)))
		{
			Elementcode elementcode = (Elementcode) verbintenisgebied.doUnproxy();
			return elementcode;
		}
		return null;
	}

	/**
	 * Bepaalt of de verbintenis betrekking hefet op een VO opleiding, exclusief VAVO
	 * opleidingen.
	 */
	public boolean isVOVerbintenis()
	{
		Elementcode elementcode = getElementcode();
		if (elementcode == null)
			return false;
		return !elementcode.isVAVO();
	}

	/**
	 * @return true als die een VO of VAVO verbintenis is, gekoppeld aan een Havo of Vwo
	 *         bovenbouw opleiding.
	 */
	public boolean isHavoVwoVerbintenis()
	{
		if (isVOVerbintenis() || isVAVOVerbintenis())
		{
			String taxonomiecode = getElementcode().getTaxonomiecode();
			if (taxonomiecode.startsWith("3.2.1") || taxonomiecode.startsWith("3.2.2"))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Bepaalt of de verbintenis betrekking heeft op een een BO opleiding (CGO of
	 * kwalificatie).
	 */
	public boolean isBOVerbintenis()
	{
		Taxonomie taxonomie = getTaxonomie();
		if (taxonomie == null)
			return false;
		return taxonomie.isBO();
	}

	public boolean isHOVerbintenis()
	{
		Taxonomie taxonomie = getTaxonomie();
		if (taxonomie == null)
			return false;
		return taxonomie.isHO();
	}

	public BronOnderwijssoort getBronOnderwijssoort()
	{
		if (isBOVerbintenis())
			return BronOnderwijssoort.BEROEPSONDERWIJS;
		else if (isEducatieVerbintenis())
			return BronOnderwijssoort.EDUCATIE;
		else if (isVAVOVerbintenis())
			return BronOnderwijssoort.VAVO;
		else if (isVOVerbintenis())
			return BronOnderwijssoort.VOORTGEZETONDERWIJS;
		else
			return null;
	}

	/**
	 * @return de taxonomie van de opleiding, of <code>null</code> als deze niet bepaald
	 *         kon worden
	 */
	public Taxonomie getTaxonomie()
	{
		if (getOpleiding() != null && getOpleiding().getVerbintenisgebied() != null)
			return getOpleiding().getVerbintenisgebied().getTaxonomie();
		else
			return null;
	}

	/**
	 * Bepaalt of de verbintenis betrekking heeft op een Eduatie opleiding (ED).
	 */
	public boolean isEducatieVerbintenis()
	{
		if (getTaxonomie() == null)
			return false;
		return getTaxonomie().isEducatie();
	}

	@Exportable
	public OnderwijsproductWrappedList getAlleActieveOnderwijsproducten(String arg0)
	{
		return new OnderwijsproductWrappedList(this, arg0);
	}

	@Override
	@Exportable
	public String getVrijVeldWaarde(String naamVrijVeld)
	{
		for (VerbintenisVrijVeld vrijVeld : vrijVelden)
		{
			if (vrijVeld.getVrijVeld().getNaam().equals(naamVrijVeld))
				return vrijVeld.getOmschrijving();
		}
		return null;
	}

	@Exportable
	public VerbintenisRapportage getRapportage()
	{
		if (rapportage == null)
			rapportage = new VerbintenisRapportage(this);
		return rapportage;
	}

	public void setHandmatigVersturenNaarBron(Enum< ? > soortMutatie)
	{
		this.handmatigVersturenNaarBronMutatie = soortMutatie;
	}

	public nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie getHandmatigeBronBveSoortMutatie()
	{
		if (handmatigVersturenNaarBronMutatie instanceof nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie)
		{
			return (nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie) handmatigVersturenNaarBronMutatie;
		}
		return null;
	}

	public nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie getHandmatigeBronVoSoortMutatie()
	{
		if (handmatigVersturenNaarBronMutatie instanceof nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie)
		{
			return (nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie) handmatigVersturenNaarBronMutatie;
		}
		return null;
	}

	public boolean isHandmatigVersturenNaarBron()
	{
		return handmatigVersturenNaarBronMutatie != null;
	}

	@Exportable
	public String getAantalGekozenOnderwijsproductenInWoorden()
	{
		return getRapportage().getAantalGekozenOnderwijsproductenInWoorden();
	}

	@Exportable
	public List<TaxonomieElement> getTaxonomieElementenVanProductregelkeuzes()
	{
		return getRapportage().getTaxonomieElementenVanProductregelkeuzes();
	}

	@Exportable
	public List<Resultaat> getEindresultatenCijferlijst()
	{
		return getRapportage().getEindresultatenCijferlijst();
	}

	@Exportable
	public Resultaat getEindresultaatCombinatievak()
	{
		return getRapportage().getEindresultaatCombinatievak();
	}

	@Exportable
	public List<Resultaat> getEindresultatenCijferlijstOpSoort(String soortOnderwijsproduct)
	{
		return getRapportage().getEindresultatenCijferlijstOpSoort(soortOnderwijsproduct);
	}

	@Exportable
	public List<Resultaat> getEindresultatenCijferlijstZonderTalen()
	{
		return getRapportage().getEindresultatenCijferlijstZonderTalen();
	}

	@Exportable
	public List<Resultaat> getEindresultatenCijferlijstAlleenTalen()
	{
		return getRapportage().getEindresultatenCijferlijstAlleenTalen();
	}

	@Exportable
	public List<Resultaat> getSchoolexamenResultatenCijferlijst()
	{
		return getRapportage().getSchoolexamenResultatenCijferlijst();
	}

	@Exportable
	public List<Resultaat> getCentraalExamenResultatenCijferlijst()
	{
		return getRapportage().getCentraalExamenResultatenCijferlijst();
	}

	@Exportable
	public List<OnderwijsproductAfnameContext_SE_CE_Eindresultaat> getSchoolexamen_CentraalExamen_EindresultatenCijferlijst()
	{
		return getRapportage().getSE_CE_EindresultatenCijferlijst(false);
	}

	@Exportable
	public List<OnderwijsproductAfnameContext_SE_CE_Eindresultaat> getSchoolexamen_CentraalExamen_EindresultatenCijferlijstPerDeel(
			String soortProductregelDiplomanaam)
	{
		return getRapportage().getSE_CE_EindresultatenCijferlijstPerDeel(
			soortProductregelDiplomanaam);
	}

	@Exportable
	public List<OnderwijsproductAfnameContext_SE_CE_Eindresultaat> getSchoolexamen_CentraalExamen_EindresultatenCijferlijstBehaald()
	{
		return getRapportage().getSE_CE_EindresultatenCijferlijstBehaald();
	}

	@Exportable
	public List<OnderwijsproductAfnameContext_SE_CE_Eindresultaat> getVergaderlijstCijfers()
	{
		return getRapportage().getSE_CE_EindresultatenCijferlijst(true);
	}

	@Exportable
	public List<OnderwijsproductAfnameContext_SE_CE_Eindresultaat> getSchoolexamen_CentraalExamen_EindresultatenCombinatievak()
	{
		return getRapportage().getSE_CE_EindresultatenCombinatievak();
	}

	/**
	 * @return de titel van het werkstuk dat aan een onderwijsproductafnamecontext van het
	 *         verbintenis gekoppeld is. De methode gaat ervan uit dat het verbintenis
	 *         maar 1 onderwijsproductafnamecontext heeft met een werktstuktitel.
	 */
	@Exportable()
	public String getWerkstukTitel()
	{
		return getRapportage().getWerkstukTitel();
	}

	/**
	 * @return de beoordeling van het werkstuk dat aan een onderwijsproductafnamecontext
	 *         van het verbintenis gekoppeld is. De methode gaat ervan uit dat het
	 *         verbintenis maar 1 onderwijsproductafnamecontext heeft met een
	 *         werktstuktitel.
	 */
	@Exportable()
	public Resultaat getBeoordelingWerkstuk()
	{
		return getRapportage().getBeoordelingWerkstuk();
	}

	@Exportable()
	public List<OnderwijsproductAfnameContext> getOnderwijsproductAfnameContextenMetBetrekkingOpWerkstuk()
	{
		return getRapportage().getOnderwijsproductAfnameContextenMetBetrekkingOpWerkstuk();
	}

	@Exportable()
	public String getLandelijkeNamenOnderwijsproductenMetBetrekkingOpWerkstuk()
	{
		return getRapportage().getLandelijkeNamenOnderwijsproductenMetBetrekkingOpWerkstuk();
	}

	/**
	 * Maakt een nieuwe plaatsing aan voor deze verbintenis. Indien de verbintenis nog
	 * geen plaatsingen had, krijgt deze de begindatum van de verbintenis mee. Als er al
	 * wel plaatsingen waren, zal de nieuwe plaatsing aansluiten op de einddatum van de
	 * laatste plaatsing. Als die laatste plaatsing geen einddatum had, wordt een nieuwe
	 * plaatsing gemaakt met een begindatum van vandaag. De aanroeper moet dan bij het
	 * opslaan zorgen dat de laatste plaatsing de juiste einddatum krijgt.
	 * 
	 * <p>
	 * De nieuwe plaatsing wordt toegevoegd aan de plaatsingen-property van deze
	 * verbintenis.
	 * 
	 * @return de nieuwe plaatsing
	 */
	public Plaatsing nieuwePlaatsing()
	{
		Plaatsing plaatsing = new Plaatsing();

		Date begindatum = getBegindatum();

		if (!getPlaatsingen().isEmpty())
		{
			Date einddatum = getPlaatsingen().get(getPlaatsingen().size() - 1).getEinddatum();
			if (einddatum != null)
				begindatum = TimeUtil.getInstance().addDays(einddatum, 1);
			else
				begindatum = TimeUtil.getInstance().currentDate();
		}

		plaatsing.setBegindatum(begindatum);
		plaatsing.setVerbintenis(this);
		plaatsing.setDeelnemer(getDeelnemer());
		plaatsing.setLwoo(getDeelnemer().getLWOOLaatstePlaatsing());

		getPlaatsingen().add(plaatsing);

		return plaatsing;

	}

	/**
	 * @see nl.topicus.eduarte.entities.inschrijving.IVooropleiding#getSoortOnderwijs()
	 *      Retourneert het soort onderwijs van deze verbintenis, mits het diploma is
	 *      behaald. Is geen diploma behaald voor deze verbintenis maar is wel een
	 *      relevante vooropleiding geregistreerd, dan wordt het soort onderwijs van de
	 *      vooropleiding teruggegeven.
	 */
	@Override
	public SoortOnderwijs getSoortOnderwijs()
	{
		if (getOpleiding() == null)
			return null;

		SoortOnderwijs soortOnderwijs = getOpleiding().getVerbintenisgebied().getSoortOnderwijs();
		if (isDiplomaBehaald())
		{
			return soortOnderwijs;
		}
		else if (getRelevanteVooropleidingVooropleiding() != null)
		{
			return getRelevanteVooropleiding().getSoortOnderwijs();
		}
		else if (getRelevanteVerbintenis() != null && getRelevanteVerbintenis().isDiplomaBehaald())
		{
			return getRelevanteVooropleiding().getSoortOnderwijs();
		}
		else
		{
			// best guess
			Taxonomie taxonomie = getTaxonomie();
			if (taxonomie.isMBO() || taxonomie.isCGO())
				return SoortOnderwijs.VMBO;
			if (SoortOnderwijs.Basisvorming.equals(soortOnderwijs))
				return SoortOnderwijs.Basisonderwijs;
			if (taxonomie.isVO())
				return SoortOnderwijs.Basisvorming;
		}
		return null;
	}

	public String getSoortVooropleiding()
	{
		return getOpleiding().getNaam();
	}

	@Override
	public Verbintenis getVerbintenis()
	{
		return this;
	}

	/**
	 * @return true indien actief, dwz de begindatum ligt voor de peildatum en de
	 *         verbintenis heeft (op die peildatum) geen afgesloten status (Beeindigd,
	 *         Afgewezen, Afgemeld).
	 */
	@Override
	public boolean isActief(Date peildatum)
	{
		VerbintenisStatus st = getStatus(peildatum);
		return super.isActief(peildatum) && (st == null || !st.isAfgesloten());
	}

	@Override
	public Deelnemer getEntiteit()
	{
		return getDeelnemer();
	}

	public void setRedenInburgering(RedenInburgering redenInburgering)
	{
		this.redenInburgering = redenInburgering;
	}

	@Exportable
	public RedenInburgering getRedenInburgering()
	{
		return redenInburgering;
	}

	public void setProfielInburgering(ProfielInburgering profielInburgering)
	{
		this.profielInburgering = profielInburgering;
	}

	@Exportable
	public ProfielInburgering getProfielInburgering()
	{
		return profielInburgering;
	}

	public void setLeerprofiel(Leerprofiel leerprofiel)
	{
		this.leerprofiel = leerprofiel;
	}

	@Exportable
	public Leerprofiel getLeerprofiel()
	{
		return leerprofiel;
	}

	public void setDeelcursus(Boolean deelcursus)
	{
		this.deelcursus = deelcursus;
	}

	@Exportable
	public Boolean getDeelcursus()
	{
		return deelcursus;
	}

	public void setDatumAanmelden(Date datumAanmelden)
	{
		this.datumAanmelden = datumAanmelden;
	}

	/**
	 * Voor inburgering. Als verbintenis onder inburgeringscontract valt, is dit de datum
	 * waarop de opdrachtgever de cursist heeft aangemeld. Nodig voor rapportage aan het
	 * keurmerk inburgering.
	 */
	@Exportable
	public Date getDatumAanmelden()
	{
		return datumAanmelden;
	}

	public void setDatumAkkoord(Date datumAkkoord)
	{
		this.datumAkkoord = datumAkkoord;
	}

	/**
	 * Voor inburgering. Als verbintenis NIET onder een inburgeringscontract valt, is dit
	 * de datum waarop de cursist de onderwijsovereenkomst heeft ondertekend. Nodig voor
	 * rapportage aan het keurmerk inburgering.
	 */
	@Exportable
	public Date getDatumAkkoord()
	{
		return datumAkkoord;
	}

	public void setDatumEersteActiviteit(Date datumEersteActiviteit)
	{
		this.datumEersteActiviteit = datumEersteActiviteit;
	}

	@Exportable
	public Date getDatumEersteActiviteit()
	{
		return datumEersteActiviteit;
	}

	public void setBeginNiveauSchriftelijkeVaardigheden(
			NT2Niveau beginNiveauSchriftelijkeVaardigheden)
	{
		this.beginNiveauSchriftelijkeVaardigheden = beginNiveauSchriftelijkeVaardigheden;
	}

	@Exportable
	public NT2Niveau getBeginNiveauSchriftelijkeVaardigheden()
	{
		return beginNiveauSchriftelijkeVaardigheden;
	}

	public void setEindNiveauSchriftelijkeVaardigheden(NT2Niveau eindNiveauSchriftelijkeVaardigheden)
	{
		this.eindNiveauSchriftelijkeVaardigheden = eindNiveauSchriftelijkeVaardigheden;
	}

	@Exportable
	public NT2Niveau getEindNiveauSchriftelijkeVaardigheden()
	{
		return eindNiveauSchriftelijkeVaardigheden;
	}

	public void setSoortPraktijkexamen(SoortPraktijkexamen soortPraktijkexamen)
	{
		this.soortPraktijkexamen = soortPraktijkexamen;
	}

	@Exportable
	public SoortPraktijkexamen getSoortPraktijkexamen()
	{
		return soortPraktijkexamen;
	}

	/**
	 * Geeft het hoogste volgnummer van de bpvinschrijvingen <tt>+ 1</tt> terug.
	 */
	public int getVolgendeBPVInschrijvingVolgnummer()
	{
		int max = 0;
		for (BPVInschrijving bpvInschrijving : getBpvInschrijvingen())
		{
			max = Math.max(bpvInschrijving.getVolgnummer(), max);
		}
		return max + 1;
	}

	public void setIndicatieGehandicapt(boolean indicatieGehandicapt)
	{
		this.indicatieGehandicapt = indicatieGehandicapt;
	}

	/*
	 * isIndicatieGehandicapt() klinkt dusdanig verkeerd dat een get-methode beter is
	 * (voldoet nog steeds aan de javabeans standaard).
	 */
	@Exportable
	public boolean getIndicatieGehandicapt()
	{
		return indicatieGehandicapt;
	}

	@AutoForm(label = "Indicatie gehandicapt")
	@Exportable
	public String getIndicatieGehandicaptOmschrijving()
	{
		return getIndicatieGehandicapt() ? "Ja" : "Nee";
	}

	@Override
	public void setBronStatus(BronEntiteitStatus bronStatus)
	{
		if (bronStatus == null)
		{
			this.bronStatus = BronEntiteitStatus.Geen;
		}
		else
		{
			this.bronStatus = bronStatus;
		}
	}

	@Override
	public BronEntiteitStatus getBronStatus()
	{
		if (bronStatus != null)
			return bronStatus;
		return BronEntiteitStatus.Geen;
	}

	@Override
	public void setBronDatum(Date bronDatum)
	{
		this.bronDatum = bronDatum;
	}

	@Override
	public Date getBronDatum()
	{
		return bronDatum;
	}

	@Exportable
	public Resultaat getBehaaldNiveauNT2Gesprekken()
	{
		return getBehaaldNiveauNT2Vaardigheid(SoortToets.Gesprekken);
	}

	@Exportable
	public Resultaat getBehaaldNiveauNT2Lezen()
	{
		return getBehaaldNiveauNT2Vaardigheid(SoortToets.Lezen);
	}

	@Exportable
	public Resultaat getBehaaldNiveauNT2Luisteren()
	{
		return getBehaaldNiveauNT2Vaardigheid(SoortToets.Luisteren);
	}

	@Exportable
	public Resultaat getBehaaldNiveauNT2Schrijven()
	{
		return getBehaaldNiveauNT2Vaardigheid(SoortToets.Schrijven);
	}

	@Exportable
	public Resultaat getBehaaldNiveauNT2Spreken()
	{
		return getBehaaldNiveauNT2Vaardigheid(SoortToets.Spreken);
	}

	@Exportable
	public Resultaat getInstroomniveauNT2Gesprekken()
	{
		return getInstroomniveauNT2Vaardigheid(SoortToets.Gesprekken);
	}

	@Exportable
	public Resultaat getInstroomniveauNT2Lezen()
	{
		return getInstroomniveauNT2Vaardigheid(SoortToets.Lezen);
	}

	@Exportable
	public Resultaat getInstroomniveauNT2Luisteren()
	{
		return getInstroomniveauNT2Vaardigheid(SoortToets.Luisteren);
	}

	@Exportable
	public Resultaat getInstroomniveauNT2Schrijven()
	{
		return getInstroomniveauNT2Vaardigheid(SoortToets.Schrijven);
	}

	@Exportable
	public Resultaat getInstroomniveauNT2Spreken()
	{
		return getInstroomniveauNT2Vaardigheid(SoortToets.Spreken);
	}

	/**
	 * @return Het geldende resultaat voor het behaalde niveau van de gegeven NT2
	 *         vaardigheid.
	 */
	public Resultaat getBehaaldNiveauNT2Vaardigheid(SoortToets vaardigheid)
	{
		return getRapportage().getNiveauNT2Vaardigheid(vaardigheid, SoortToets.BehaaldNiveau);
	}

	/**
	 * @return Het geldende resultaat voor het instroomniveau van de gegeven NT2
	 *         vaardigheid.
	 */
	public Resultaat getInstroomniveauNT2Vaardigheid(SoortToets vaardigheid)
	{
		return getRapportage().getNiveauNT2Vaardigheid(vaardigheid, SoortToets.Instroomniveau);
	}

	@Override
	public boolean isBeeindigd()
	{
		return beeindigd;
	}

	public void setBeeindigd(boolean beeindigd)
	{
		this.beeindigd = beeindigd;
	}

	/**
	 * Geeft de lesweekindeling terug van de OrganisatieEenheidLocatie
	 */
	public LesweekIndeling getLesweekIndeling()
	{
		LesweekIndelingOrganisatieEenheidLocatie defaultLesweekOrgLoc =
			DataAccessRegistry.getHelper(
				LesweekindelingOrganisatieEenheidLocatieDataAccesHelper.class)
				.getOrganisatieEenheidLocatie(getOrganisatieEenheid(), getLocatie());

		LesweekIndeling defaultLesweekIndeling = null;
		if (defaultLesweekOrgLoc != null)
		{
			defaultLesweekIndeling = defaultLesweekOrgLoc.getLesweekIndeling();
		}
		else
		{
			// defaultLesweekIndeling = getOrganisatieEenheid().getLesweekindeling();
			return null;
		}

		return defaultLesweekIndeling;
	}

	@Override
	public String getGeldigVanTotBeschrijving()
	{
		String geldigVanTotBeschrijving = null;

		if (getBegindatum() != null)
		{
			geldigVanTotBeschrijving = TimeUtil.getInstance().formatDate(getBegindatum()) + " t/m ";
			if (getEinddatum() != null)
			{
				geldigVanTotBeschrijving += TimeUtil.getInstance().formatDate(getEinddatum());
			}
			if (getGeplandeEinddatum() != null)
			{
				geldigVanTotBeschrijving +=
					TimeUtil.getInstance().formatDate(getGeplandeEinddatum()) + " (verwacht)";
			}
			else
			{
				geldigVanTotBeschrijving += "...";
			}
		}
		else
		{
			geldigVanTotBeschrijving = "Onbekend";
		}

		return geldigVanTotBeschrijving;
	}

	/**
	 * @return true als dit een inburgeringsdeelnemer is (opleiding uit taxonomie
	 *         Inburgering)
	 */
	public boolean isInburgeringVerbintenis()
	{
		return opleiding != null && opleiding.isInburgering();
	}

	/**
	 * @return true als dit een inburgeringsdeelnemer is die onder een
	 *         inburgeringscontract valt
	 */
	public boolean isInburgeringContractVerbintenis()
	{
		if (!isInburgeringVerbintenis() || contracten == null)
			return false;

		boolean inburgeringscontract = false;
		for (VerbintenisContract contract : contracten)
			if (contract.getContract().isInburgering())
				inburgeringscontract = true;

		return inburgeringscontract;
	}

	/**
	 * @return de {@link Onderaanneming} die is ingesteld bij het (eerste)
	 *         inburgeringscontract waaronder deze verbintenis valt. Null indien geen
	 *         inburgering of geen inburgeringscontract.
	 */
	public Onderaanneming getInburgeringContractOnderaanneming()
	{
		if (!isInburgeringVerbintenis() || contracten == null)
			return null;

		for (VerbintenisContract contract : getContractenOpPeildatum())
			if (contract.getContract().isInburgering())
				return contract.getContract().getOnderaanneming();

		return null;
	}

	public void setRelevanteVerbintenis(Verbintenis relevanteVerbintenis)
	{
		if (equals(relevanteVerbintenis))
			throw new IllegalArgumentException(
				"De relevante vooropleiding van deze verbintenis kan niet verwijzen naar zichzelf.");
		this.relevanteVerbintenis = relevanteVerbintenis;
	}

	public Verbintenis getRelevanteVerbintenis()
	{
		return relevanteVerbintenis;
	}

	public void setRelevanteVooropleiding(IVooropleiding relevanteVooropleiding)
	{
		if (relevanteVooropleiding == null)
		{
			setRelevanteVerbintenis(null);
			setRelevanteVooropleidingVooropleiding(null);
		}// instanceof werkt niet met Hibernate ivm proxies
		else if (Vooropleiding.class.isAssignableFrom(Hibernate.getClass(relevanteVooropleiding)))
		{
			setRelevanteVooropleidingVooropleiding((Vooropleiding) relevanteVooropleiding);
			setRelevanteVerbintenis(null);
		}// instanceof werkt niet met Hibernate ivm proxies
		else if (Verbintenis.class.isAssignableFrom(Hibernate.getClass(relevanteVooropleiding)))
		{
			setRelevanteVerbintenis((Verbintenis) relevanteVooropleiding);
			setRelevanteVooropleidingVooropleiding(null);
		}
		else
			throw new IllegalArgumentException("Unsupported IVooropleiding");
	}

	@AutoForm(include = true)
	@AutoFormEmbedded
	public IVooropleiding getRelevanteVooropleiding()
	{
		if (getRelevanteVerbintenis() != null)
			return getRelevanteVerbintenis();
		else if (getRelevanteVooropleidingVooropleiding() != null)
			return getRelevanteVooropleidingVooropleiding();
		return null;
	}

	public List<IVooropleiding> getMogelijkeVooropleidingen()
	{
		List<IVooropleiding> vooropleidingen = new ArrayList<IVooropleiding>();
		Date begindatum = getBegindatum();

		if (begindatum != null)
		{
			for (Vooropleiding vooropleiding : getDeelnemer().getVooropleidingen())
			{
				// Begindatum is of null, of ligt voor begindatum van Verbintenis EN
				// Einddatum is of null, of ligt voor begindatum van Verbintenis
				if ((vooropleiding.getBegindatum() == null || (vooropleiding.getBegindatum() != null && vooropleiding
					.getBegindatum().before(begindatum)))
					&& (vooropleiding.getEinddatum() == null || (vooropleiding.getEinddatum() != null && vooropleiding
						.getEinddatum().before(begindatum))))
					vooropleidingen.add(vooropleiding);
			}
		}
		else
		{
			vooropleidingen.addAll(getDeelnemer().getVooropleidingen());
		}

		for (Verbintenis verbintenis : getDeelnemer().getVerbintenissen())
		{
			if (!verbintenis.equals(this))
			{
				// als de begindatum van de huidige verbintenis is ingevuld, moeten de
				// begindatum en de einddatum (voor zover ingevuld) voor de begindatum van
				// de huidige verbintenis liggen
				if (begindatum == null
					|| (verbintenis.getBegindatum().before(begindatum) && (verbintenis
						.getEinddatum() == null || verbintenis.getEinddatum().before(begindatum))))
					vooropleidingen.add(verbintenis);
			}
		}

		return vooropleidingen;
	}

	/**
	 * @return De lijst met deelkwalificaties die behaald zijn bij deze verbintenis. Kan
	 *         alleen aangeroepen worden voor MBO-oud verbintenissen.
	 */
	public List<OnderwijsproductAfnameContext> getBehaaldeDeelkwalificaties()
	{
		List<OnderwijsproductAfnameContext> res = new ArrayList<OnderwijsproductAfnameContext>();
		if (getTaxonomie().isMBO())
		{
			List<OnderwijsproductAfname> afnames = getOnderwijsproductAfnames();
			ResultaatDataAccessHelper helper =
				DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class);
			Map<OnderwijsproductAfname, Resultaat> eindResultaten =
				helper.getEindresultaten(getDeelnemer(), afnames);
			for (OnderwijsproductAfnameContext context : getAfnameContexten())
			{
				OnderwijsproductAfname afname = context.getOnderwijsproductAfname();
				Onderwijsproduct product = afname.getOnderwijsproduct();
				if (product.isGekoppeldAanDeelKwalificatie() && product.getExterneCode() != null
					&& eindResultaten.containsKey(afname))
				{
					Resultaat resultaat = eindResultaten.get(afname);
					if (resultaat.isBehaald())
					{
						res.add(context);
					}
				}
			}
		}
		return res;
	}

	public boolean heeftBehaaldeDeelkwalificaties()
	{
		return !getBehaaldeDeelkwalificaties().isEmpty();
	}

	@Exportable
	public long getOvereenkomstnummer()
	{
		return overeenkomstnummer;
	}

	public void setOvereenkomstnummer(long overeenkomstnummer)
	{
		this.overeenkomstnummer = overeenkomstnummer;
	}

	@Exportable
	public String getContextOnderwijsproductCodes()
	{
		return StringUtil.toString(getAfnameContexten(), "",
			new PropertyStringConverter<OnderwijsproductAfnameContext>(
				"onderwijsproductAfname.onderwijsproduct.code"));
	}

	public String getSoortVoOnderwijs()
	{
		for (SoortOnderwijsTax soort : SoortOnderwijsTax.values())
		{
			if (getTaxonomie().getCode().startsWith(soort.getTaxCode()))
				return soort.toString();
		}
		return "";
	}

	@Exportable
	@Override
	public Date getBegindatum()
	{
		return super.getBegindatum();
	}

	@Exportable
	@Override
	public Date getEinddatum()
	{
		return super.getEinddatum();
	}

	public String getExamennummerOfDeelnemernummer()
	{
		if (getLaatsteExamendeelname() != null
			&& !getLaatsteExamendeelname().getExamennummerMetPrefix().isEmpty())
			return getLaatsteExamendeelname().getExamennummerMetPrefix();
		return "" + getDeelnemer().getDeelnemernummer();

	}

	@Override
	public String getBrincode()
	{
		return getOrganisatie().getBrincode().getCode();
	}

	@Override
	public String getNaamVooropleiding()
	{
		return getOpleiding() == null ? null : getOpleiding().getNaam();
	}

	@Override
	public Integer getCitoscore()
	{
		// Verbintenis heeft geen citoscore
		return null;
	}

	@Override
	public Schooladvies getSchooladvies()
	{
		// Verbintenis heeft geen schooladvies
		return null;
	}

	public void setUitsluitenVanFacturatie(boolean uitsluitenVanFacturatie)
	{
		this.uitsluitenVanFacturatie = uitsluitenVanFacturatie;
	}

	public boolean isUitsluitenVanFacturatie()
	{
		return uitsluitenVanFacturatie;
	}

	public Aanmelding getAanmelding()
	{
		return DataAccessRegistry.getHelper(VerbintenisDataAccessHelper.class).getAanmelding(this);
	}

	public boolean heeftTaalMeeteenheid()
	{
		Meeteenheid meeteenheid =
			DataAccessRegistry.getHelper(MeeteenheidDataAccessHelper.class).getTaalMeeteenheid(
				this, getOpleiding(), getCohort());
		return meeteenheid != null;
	}

	@Exportable
	public String getAlleSectorNamenOpTweeRegels()
	{
		return StringUtil.join(getAlleSectorNamen(), "\n");
	}

	@Exportable
	public List<String> getAlleSectorNamen()
	{
		List<String> ret = new ArrayList<String>();
		if (getOpleiding() != null
			&& getOpleiding().getVerbintenisgebied().getTaxonomiecode().startsWith("3.2.3"))
		{
			OpleidingZoekFilter filter = new OpleidingZoekFilter();
			filter.setTaxonomiecode(getOpleiding() == null ? null : getOpleiding()
				.getVerbintenisgebied().getParent().getTaxonomiecode());
			filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
				new AlwaysGrantedSecurityCheck()));

			List<Opleiding> res =
				DataAccessRegistry.getHelper(OpleidingDataAccessHelper.class).list(filter);
			BereikbareDiplomasUtil util =
				new BereikbareDiplomasUtil(getDeelnemer(), res, getCohort());
			util.berekenOpleidingenWaarvoorDeDeelnemerIsGeslaagd();

			for (Opleiding opl : util.getGeslaagdVoorOpleidingen())
			{
				Verbintenisgebied verGebied = opl.getVerbintenisgebied();
				if (!StringUtil.isEmpty(verGebied.getSectornamen()))
					ret.add(verGebied.getSectornamen());
			}
		}
		if (ret.isEmpty())
			return null;
		return ret;

	}

	@Exportable(omschrijving = "Aantal hele maanden tussen begin- en geplande einddatum")
	public Integer getGeplandeDuurInMaanden()
	{
		if (geplandeEinddatum == null)
			return null;

		// + 1 dag omdat de einddatum een tot-en-met-datum is.
		Date einddatum = TimeUtil.getInstance().addDays(geplandeEinddatum, 1);
		return TimeUtil.getInstance().getDifferenceInMonths(getBegindatum(), einddatum);
	}

	public void setExamenDatum(Date examenDatum)
	{
		this.examenDatum = examenDatum;
	}

	public Date getExamenDatum()
	{
		return examenDatum;
	}

	public void setStaatsExamenType(StaatsExamenType staatsExamenType)
	{
		this.staatsExamenType = staatsExamenType;
	}

	public StaatsExamenType getStaatsExamenType()
	{
		return staatsExamenType;
	}

	public BigDecimal getWettelijkCursusgeldBedragVoorSchooljaar(Schooljaar schooljaar)
	{
		if (!EduArteApp.get().isModuleActive(EduArteModuleKey.FINANCIEEL))
			return null;

		BigDecimal opleidingBedrag = WettelijkCursusgeld.getOpleidingBedrag(this, schooljaar);
		if (opleidingBedrag != null)
			return WettelijkCursusgeld.verrekenGemisteMaanden(schooljaar, this, opleidingBedrag);
		return null;
	}

	@Exportable
	public BigDecimal getWettelijkCursusgeldBedragVoorSchooljaar(int schooljaar)
	{
		return getWettelijkCursusgeldBedragVoorSchooljaar(Schooljaar.valueOf(schooljaar));
	}

	@Exportable
	public BigDecimal getWettelijkCursusgeldBedrag()
	{
		return getWettelijkCursusgeldBedragVoorSchooljaar(Schooljaar.huidigSchooljaar());
	}

	public List<Inschrijvingsverzoek> getInschrijvingsverzoeken()
	{
		return inschrijvingsverzoeken;
	}

	public void setInschrijvingsverzoeken(List<Inschrijvingsverzoek> inschrijvingsverzoeken)
	{
		this.inschrijvingsverzoeken = inschrijvingsverzoeken;
	}

	public SoortVervolgonderwijs getVervolgonderwijsSoort()
	{
		return vervolgonderwijs.getSoortVervolgonderwijs();
	}

	public String getVervolgonderwijsNaam()
	{
		return vervolgonderwijs.getNaam() + ", "
			+ StringUtil.firstCharUppercase(vervolgonderwijs.getPlaats());
	}

	public void setDatumOvereenkomstOndertekend(Date datumOvereenkomstOndertekend)
	{
		this.datumOvereenkomstOndertekend = datumOvereenkomstOndertekend;
	}

	@Exportable
	public Date getDatumOvereenkomstOndertekend()
	{
		return datumOvereenkomstOndertekend;
	}

	public void setBrin(Brin brin)
	{
		this.brin = brin;
	}

	public Brin getBrin()
	{
		return brin;
	}

	public String getBrinnaam()
	{
		return getBrin() != null ? getBrin().getNaam() : "";
	}

	@Exportable
	public String getKenniscentrum()
	{
		if (getBrin() != null)
			return getBrin().getNaam();
		if (getOpleiding().getVerbintenisgebied().getTaxonomie().getNaamKenniscentrum() != null)
			return getOpleiding().getVerbintenisgebied().getTaxonomie().getNaamKenniscentrum();
		return "";
	}

	public Map<Hoofdfase, Integer> getDefaultCreditsPerFase()
	{
		OpleidingsVorm vorm = null;
		Plaatsing plaatsing = getPlaatsingOpPeildatum();
		if (plaatsing != null)
			vorm = plaatsing.getOpleidingsVorm();

		Map<Hoofdfase, Integer> result = new HashMap<Hoofdfase, Integer>();
		for (OpleidingFase fase : getOpleiding().getFases())
			if (vorm == null || fase.getOpleidingsvorm().equals(vorm))
				result.put(fase.getHoofdfase(), fase.getCredits());

		return result;
	}

	public Set<Hoofdfase> getFases()
	{
		return getDefaultCreditsPerFase().keySet();
	}

	public Integer getCredits(Hoofdfase hoofdfase)
	{
		if (creditsPerFase != null)
		{
			Integer credits = creditsPerFase.get(hoofdfase);
			if (credits != null)
				return credits;
		}

		return getDefaultCreditsPerFase().get(hoofdfase);
	}

	public void setCredits(Hoofdfase hoofdfase, Integer credits)
	{
		Map<Hoofdfase, Integer> defaultCreditsPerFase = getDefaultCreditsPerFase();
		Integer defaultCredits = defaultCreditsPerFase.get(hoofdfase);

		if (creditsPerFase == null)
			creditsPerFase = new HashMap<Hoofdfase, Integer>();

		if (credits == null || credits.equals(defaultCredits))
			creditsPerFase.remove(hoofdfase);
		else
			creditsPerFase.put(hoofdfase, credits);
	}

	@Exportable
	public Team getTeam()
	{
		return getOpleiding() != null ? getOpleiding().selecteerTeam(getOrganisatieEenheid(),
			getLocatie()) : null;
	}

	/**
	 * @return true als de student in aanmerking komt voor wettelijk collegegeld. Dit
	 *         gesubsidieerde collegegeld is alleen van toepassing voor specifieke
	 *         nationaliteiten, woonadressen in Benelux+grensstreek Duitsland en alleen
	 *         als de student nog geen Bachelor resp. Mastergraad heeft.
	 */
	public boolean isHOBekostigbaar()
	{
		if (Boolean.TRUE.equals(getNegeerWettelijkCollegegeldVoorwaarden()))
			return true;

		boolean nationaliteitBekostigbaar = getDeelnemer().isNationaliteitHOBekostigbaar();
		boolean woonlandBekostigbaar = getDeelnemer().isWoonlandHOBekostigbaar();

		return nationaliteitBekostigbaar && woonlandBekostigbaar && !isGraadBehaald();
	}

	/**
	 * @return true indien dit een Bachelor-/Masteropleiding is en de student heeft al een
	 *         Bachelor resp. Mastergraad. Voor Onderwijs en Gezondheidszorg geldt een
	 *         uitzondering en wordt altijd false geretourneerd.
	 */
	public boolean isGraadBehaald()
	{
		boolean heeftGraad =
			getDeelnemer().getHeeftBachelorgraad() || getDeelnemer().getHeeftMastergraad();
		boolean onderwijsOfGezondheidszorg = false;
		if (getOpleiding() != null && getOpleiding().isHogerOnderwijs())
		{
			// Bij een Bachelorgraad wordt een Masteropleiding nog wel bekostigd (of
			// andersom voor zover dat voorkomt).
			// Of de opleiding een Bachelor of Master is, zit versleuteld in de naam van
			// het verbintenisgebied. Is dit niet het geval (bijv. bij historische
			// opleidingen) dan wordt zowel een bachelor als een mastergraad beschouwd als
			// graad.
			Verbintenisgebied verbintenisgebied = getOpleiding().getVerbintenisgebied().doUnproxy();
			CrohoOpleiding croho = (CrohoOpleiding) verbintenisgebied;
			List<CrohoOpleidingAanbod> aanbod =
				croho.getAanbod(getOrganisatieEenheid().getBrincode());
			for (CrohoOpleidingAanbod coa : aanbod)
				if (coa.getOnderdeel() == CrohoOnderdeel.Onderwijs
					|| coa.getOnderdeel() == CrohoOnderdeel.Gezondheidszorg)
					onderwijsOfGezondheidszorg = true;

			if (croho.getNaam().startsWith("B "))
				heeftGraad = getDeelnemer().getHeeftBachelorgraad();
			else if (croho.getNaam().startsWith("M "))
				heeftGraad = getDeelnemer().getHeeftMastergraad();
		}

		return !onderwijsOfGezondheidszorg && heeftGraad;
	}

	public void setNegeerWettelijkCollegegeldVoorwaarden(
			Boolean negeerWettelijkCollegegeldVoorwaarden)
	{
		this.negeerWettelijkCollegegeldVoorwaarden = negeerWettelijkCollegegeldVoorwaarden;
	}

	public Boolean getNegeerWettelijkCollegegeldVoorwaarden()
	{
		return negeerWettelijkCollegegeldVoorwaarden;
	}

	public String getAfwijkendeExterneCode()
	{
		return afwijkendeExterneCode;
	}

	public void setAfwijkendeExterneCode(String afwijkendeExterneCode)
	{
		this.afwijkendeExterneCode = afwijkendeExterneCode;
	}

	public void setCreditsPerFase(Map<Hoofdfase, Integer> creditsPerFase)
	{
		this.creditsPerFase = creditsPerFase;
	}

	public Map<Hoofdfase, Integer> getCreditsPerFase()
	{
		return creditsPerFase;
	}

	@Exportable
	public Integer getStudiebelastingsurenGeplandeDuur()
	{
		final int studiebelatingsuurPerMaand = 160;

		Integer totaalAantalMaanden = getGeplandeDuurInStudiemaanden();
		if (totaalAantalMaanden != null)
			return studiebelatingsuurPerMaand * totaalAantalMaanden;

		return null;
	}

	/**
	 * @return Het aantal werkelijke studiemaanden (dus met uitzondering van juli &
	 *         augustus)
	 */
	public Integer getGeplandeDuurInStudiemaanden()
	{
		if (getGeplandeEinddatum() == null)
			return null;

		// Einddatum is tot en met
		Date einddatum = TimeUtil.getInstance().addDays(getGeplandeEinddatum(), 1);

		Integer difference =
			TimeUtil.getInstance().getDifferenceInMonths(getBegindatum(), einddatum);

		Calendar begin = Calendar.getInstance();
		begin.setTime(getBegindatum());
		Calendar eind = Calendar.getInstance();
		eind.setTime(einddatum);

		// Maanden juli & augustus tellen niet mee voor SBU's

		// Voor een volledig jaar 2 maanden aftrekken
		difference -= (2 * (eind.get(Calendar.YEAR) - begin.get(Calendar.YEAR) - 1));

		// Voor niet-volledige jaren, begindatum
		Date datumNa = TimeUtil.getInstance().asDate(begin.get(Calendar.YEAR), 8, 1);
		if (getBegindatum().before(datumNa))
		{
			difference -=
				Math.min(2, TimeUtil.getInstance().getDifferenceInMonths(getBegindatum(), datumNa));
		}

		// Voor niet-volledige jaren, einddatum
		Date datumVoor = TimeUtil.getInstance().asDate(eind.get(Calendar.YEAR), 5, 30);
		if (einddatum.after(datumVoor))
		{
			difference -=
				Math.min(2, TimeUtil.getInstance().getDifferenceInMonths(einddatum, datumVoor));
		}

		return difference;
	}

	/**
	 * @see IVooropleiding#getOrganisatieOmschrijving()
	 */
	@Override
	public String getOrganisatieOmschrijving()
	{
		return getOrganisatie().getNaam();
	}
}
