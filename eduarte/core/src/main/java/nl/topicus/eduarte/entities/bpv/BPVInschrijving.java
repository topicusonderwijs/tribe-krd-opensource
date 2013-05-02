package nl.topicus.eduarte.entities.bpv;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.choice.EnumRadioChoice;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.eduarte.entities.BronEntiteitStatus;
import nl.topicus.eduarte.entities.IBronStatusEntiteit;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.inschrijving.BPVInschrijvingBijlage;
import nl.topicus.eduarte.entities.inschrijving.BronCommuniceerbaar;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.vrijevelden.BPVInschrijvingVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.components.panels.ExterneOrganisatieContactPersoonPanel;
import nl.topicus.eduarte.web.components.panels.ExterneOrganisatiePanel;
import nl.topicus.eduarte.web.components.quicksearch.bpvbedrijfsgegeven.BPVBedrijfsgegevenSearchEditor;
import nl.topicus.onderwijs.duo.bron.Bron;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * BPVInschrijving
 * 
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Table(appliesTo = "BPVInschrijving", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
@Exportable
@IsViewWhenOnNoise
public class BPVInschrijving extends BeginEinddatumInstellingEntiteit implements DeelnemerProvider,
		IBronStatusEntiteit, IBijlageKoppelEntiteit<BPVInschrijvingBijlage>,
		VrijVeldable<BPVInschrijvingVrijVeld>
{
	private static final long serialVersionUID = 1L;

	public static enum BPVStatus implements BronCommuniceerbaar, IActiefEntiteit
	{

		/**
		 * De BPV is nog niet officieel. Alle gegevens kunnen nog wijzigen. De BPV is nog
		 * niet aan BRON gemeld. De deelnemer is nog niet aan de slag gegaan bij het
		 * BPV-bedrijf.
		 */
		Voorlopig,

		/**
		 * De BPV is nog niet officieel (niet ondertekend), maar alle gegevens liggen in
		 * principe vast. De BPV wordt aan BRON gemeld. Er kan een BPVO worden afgedrukt.
		 * Bij het op volledig zetten wordt gecontroleerd of is voldaan aan alle eisen die
		 * BRON aan de gegevens stelt. Als dit niet het geval is, wordt dit via een
		 * melding weergegeven en blijft de status op voorlopig staan. Wanneer de status
		 * op “Volledig” gezet wordt, zijn ook praktijkopleider, praktijkbegeleider,
		 * kenniscentrum (bij BBL), verwachte einddatum, totale omvang en uren en dagen
		 * per week verplicht.
		 */
		Volledig,

		/**
		 * De BPVO is wel afgedrukt, maar nog niet ondertekend. De BPV is aan BRON gemeld
		 * (of de melding staat in de wachtrij). Gedurende deze status wordt gewacht op de
		 * retourontvangst van een ondertekende BPVO.
		 */
		OvereenkomstAfgedrukt
		{
			@Override
			public String toString()
			{
				return "Overeenkomst afgedrukt";
			}
		},

		/**
		 * De BPV is officieel en getekend. Alle gegevens liggen vast. De BPV is aan BRON
		 * gemeld (of melding staat in de wachtrij). De deelnemer kan aan de slag gaan bij
		 * het BPV-bedrijf.
		 */
		Definitief,

		/**
		 * De (werkelijke) einddatum van de BPV is verstreken. Deze status is in feite
		 * impliciet, want deze status wordt automatisch bereikt door het verstrijken van
		 * de einddatum, behalve wanneer de BPV afgemeld of afgewezen is.
		 */
		Beëindigd,

		/**
		 * De deelnemer heeft zich afgemeld voor de BPV. De BPV zal nooit worden
		 * geëffectueerd en de deelnemer zal niet aan de slag gaan bij het BPV-bedrijf. Er
		 * is geen melding naar BRON geweest, of, wanneer deze status via “definitief” was
		 * bereikt, de BPV-melding is uit BRON verwijderd. De verbintenis mag niet meer
		 * worden gewijzigd zodra deze status wordt bereikt.
		 */
		Afgemeld,

		/**
		 * De instelling, het BPV-bedrijf, het kenniscentrum of BRON heeft de BPV niet
		 * goedgekeurd. De BPV zal nooit worden geëffectueerd en de deelnemer zal niet aan
		 * de slag gaan bij het BPV-bedrijf. Er is geen melding naar BRON geweest, of,
		 * wanneer deze status via “volledig” was bereikt, de BPV-melding is uit BRON
		 * verwijderd. De BPV mag niet meer worden gewijzigd zodra deze status wordt
		 * bereikt.
		 */
		Afgewezen;

		/**
		 * @return <code>true</code> als <tt>status1 &lt;= this &lt;= status2</tt>
		 */
		public boolean tussen(BPVStatus status1, BPVStatus status2)
		{
			return (status1.ordinal() <= this.ordinal()) && this.ordinal() <= status2.ordinal();
		}

		/**
		 * @return <code>true</code> als
		 */
		@Override
		public boolean isBronCommuniceerbaar()
		{
			return tussen(BPVStatus.Volledig, BPVStatus.Beëindigd);
		}

		/**
		 * Deze methode geeft de mogelijke vervolgstatussen terug voor een normale
		 * gebruiker
		 * 
		 * @return de mogelijke vervolgstatussen van deze status.
		 */
		public BPVStatus[] getVervolgNormaal()
		{
			switch (this)
			{
				case Voorlopig:
					return new BPVStatus[] {this, Volledig, Afgewezen, Afgemeld};
				case Volledig:
					// KOL 20091110: Afgedrukt weggehaald als vervolgstatus omdat dit
					// alleen mag met de knop 'Afdrukken'
					return new BPVStatus[] {this};
				case OvereenkomstAfgedrukt:
					return new BPVStatus[] {this, Definitief};
				case Definitief:
					return new BPVStatus[] {this}; // Beeindigd moet je bereiken
					// via BPV beeindigen
				case Beëindigd:
					return new BPVStatus[] {this};
				case Afgemeld:
					return new BPVStatus[] {this, Voorlopig};
				case Afgewezen:
					return new BPVStatus[] {this, Voorlopig};
				default:
			}
			return new BPVStatus[] {this};
		}

		/**
		 * Deze methode geeft de mogelijke vervolgstatussen terug voor gebruikers die
		 * extra geautoriseerd zijn
		 * 
		 * @return De mogelijke vervolgstatussen bij extra autorisatie.
		 */
		public BPVStatus[] getVervolgUitgebreid()
		{
			switch (this)
			{
				case Voorlopig:
					return new BPVStatus[] {this, Volledig, Afgewezen, Afgemeld};
				case Volledig:
					// KOL 20091110: Afgedrukt weggehaald als vervolgstatus omdat dit
					// alleen mag met de knop 'Afdrukken'
					return new BPVStatus[] {this, Afgewezen, Afgemeld, Voorlopig};
				case OvereenkomstAfgedrukt:
					return new BPVStatus[] {this, Definitief, Afgewezen, Afgemeld, Volledig,
						Voorlopig};
				case Definitief:
					return new BPVStatus[] {this, Voorlopig}; // Beeindigd moet je
					// bereiken
				case Beëindigd:
					return new BPVStatus[] {this};
				case Afgemeld:
					return new BPVStatus[] {this, Voorlopig, Volledig, OvereenkomstAfgedrukt};
				case Afgewezen:
					return new BPVStatus[] {this, Voorlopig, Volledig, OvereenkomstAfgedrukt};
				default:
			}
			return new BPVStatus[] {this};
		}

		public static List<BPVStatus> getBronCommuniceerbareStatussen()
		{
			List<BPVStatus> result = new ArrayList<BPVStatus>();
			for (BPVStatus status : values())
			{
				if (status.isBronCommuniceerbaar())
				{
					result.add(status);
				}
			}
			return result;
		}

		public static List<BPVStatus> getNietBronCommuniceerbareStatussen()
		{
			List<BPVStatus> result = new ArrayList<BPVStatus>();
			for (BPVStatus status : values())
			{
				if (!status.isBronCommuniceerbaar())
				{
					result.add(status);
				}
			}
			return result;
		}

		@Override
		public boolean isActief()
		{
			return this != Afgemeld && this != Afgewezen;
		}
	}

	@SuppressWarnings("unchecked")
	@Bron
	@Transient
	private Enum handmatigVersturenNaarBronMutatie = null;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "verbintenis")
	@Index(name = "idx_bpvInsch_verbintenis")
	@AutoFormEmbedded
	private Verbintenis verbintenis;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bpvBedrijf", nullable = false)
	@Index(name = "idx_bpvInsch_bpvBedrijf")
	@AutoForm(label = "BPV-bedrijf (postadres)", displayClass = ExterneOrganisatiePanel.class, htmlClasses = "unit_max")
	private ExterneOrganisatie bpvBedrijf;

	/**
	 * Deze moet gekoppeld zijn aan de externe organisatie bpvBedrijf
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contactPersoonBPVBedrijf", nullable = true)
	@Index(name = "idx_bpvInsch_contPersBPV")
	@AutoForm(label = "Contactpersoon", displayClass = ExterneOrganisatieContactPersoonPanel.class, htmlClasses = "unit_max")
	private ExterneOrganisatieContactPersoon contactPersoonBPVBedrijf;

	/**
	 * Dit is de praktijkbegeleider van de instelling, verplicht bij status volledig.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "praktijkbegeleider", nullable = true)
	@Index(name = "idx_bpvInsch_medewerker")
	@AutoForm(htmlClasses = "unit_max", label = "Praktijkbegeleider (bij instelling)", description = "Medewerker van de onderwijsinstelling die de BPV begeleidt")
	private Medewerker praktijkbegeleider;

	/**
	 * De datum waarop de praktijkovereenkomst is opgesteld, zoals vastgelegd in de
	 * praktijkovereenkomst. Dit is niet per s&eacute; de datum waarop alle benodigde
	 * handtekeningen op de praktijkovereenkomst hoeven te staan.
	 */
	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	@Bron(verplicht = true)
	@AutoForm(description = "De datum waarop de praktijkovereenkomst is opgesteld, zoals vastgelegd in de praktijkovereenkomst. Dit is niet per s&eacute; de datum waarop alle benodigde handtekeningen op de praktijkovereenkomst hoeven te staan.")
	private Date afsluitdatum = new Date();

	@Column(nullable = false)
	@Bron(verplicht = true)
	private int volgnummer;

	/**
	 * Een gegenereerd overeenkomstnummer, vergelijkbaar met het deelnemernummer.
	 */
	@Column(nullable = false)
	private long overeenkomstnummer;

	/**
	 * De totale omvang van de BPV in klokuren, verplicht bij status volledig.
	 */
	@Bron
	@Column(nullable = true, length = 4)
	@AutoForm(description = "40/52 van het aantal uren per week over de periode tussen begin- en verwachte einddatum, met een maximum van 5120")
	private Integer totaleOmvang;

	@Column(nullable = true, length = 4)
	private Integer gerealiseerdeOmvang;

	/**
	 * Het aantal klokuren per week, verplicht bij status volledig.
	 */
	@Column(nullable = true, scale = 2, precision = 12)
	private BigDecimal urenPerWeek;

	/**
	 * Het aantal dagen per weerk, verplicht bij status volledig.
	 */
	@Column(nullable = true, length = 4)
	private Integer dagenPerWeek;

	/**
	 * De werkdagen waarop de deelnemer op BPV is.
	 */
	@Column(nullable = true, length = 30)
	private String werkdagen;

	/**
	 * De plek waar de POK zich fysiek bevindt (bijvoorbeeld bij deelnemer, in archief
	 * etc).
	 */
	@Column(nullable = true, length = 30)
	@AutoForm(label = "Locatie POK")
	private String locatiePOK;

	/**
	 * Het BPVBedrijfsgegeven legt het BPV-bedrijf, het kenniscentrum en de code
	 * leerbedrijf vast. Verplicht bij status volledig.
	 */
	@Bron
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bedrijfsgegeven", nullable = true)
	@Index(name = "idx_bpvInsch_bpvBedrGeg")
	@AutoForm(editorClass = BPVBedrijfsgegevenSearchEditor.class, htmlClasses = "unit_max", label = "BPV-bedrijf", required = true)
	private BPVBedrijfsgegeven bedrijfsgegeven = null;

	/**
	 * De contractpartner, in gevallen waarbij de BPV-overeenkomst wordt gesloten met een
	 * geaccrediteerd uitzendbureau/detacheringsorganisatie, maar de deelnemer
	 * werkzaamheden uitvoert bij een ander bedrijf.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contractpartner", nullable = true)
	@Index(name = "idx_bpvInsch_contractpartner")
	@AutoForm(displayClass = ExterneOrganisatiePanel.class, htmlClasses = "unit_max", description = "De contractpartner, in gevallen waarbij bij de BPV-overeenkomst niet alleen het geaccrediteerde BPV-bedrijf betrokken is, maar ook een contractpartner die optreedt als werkgever, holding of bijv. uitzendbureau.")
	private ExterneOrganisatie contractpartner;

	/**
	 * Deze moet gekoppeld zijn aan de externe organisatie contractpartner
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contactPersoonContractpartner", nullable = true)
	@Index(name = "idx_bpvInsch_contPersContr")
	@AutoForm(label = "Contactpersoon contractpartner", displayClass = ExterneOrganisatieContactPersoonPanel.class, htmlClasses = "unit_max")
	private ExterneOrganisatieContactPersoon contactPersoonContractpartner;

	/**
	 * Deze moet gekoppeld zijn aan de externe organisatie bpvBedrijf
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "praktijkopleiderBPVBedrijf", nullable = true)
	@Index(name = "idx_bpvInsch_praktijkopBPV")
	@AutoForm(label = "Praktijkopleider", displayClass = ExterneOrganisatieContactPersoonPanel.class, htmlClasses = "unit_max")
	private ExterneOrganisatieContactPersoon praktijkopleiderBPVBedrijf;

	@Column(nullable = true, length = 100)
	@AutoForm(label = "Praktijkopleider (naam)", htmlClasses = "unit_max")
	private String naamPraktijkopleiderBPVBedrijf;

	/**
	 * De reden van beinding van de bpv, als de verbintenis beeindigd wordt, wordt deze
	 * ook gevuld met dezelfde reden als bij de verbintenis
	 */
	@Bron
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "redenUitschrijving", nullable = true)
	@Index(name = "idx_bpvInsch_redenUit")
	@AutoForm(label = "Reden beëindiging")
	private RedenUitschrijving redenUitschrijving;

	@Lob
	@Column(nullable = true)
	@AutoForm(htmlClasses = "unit_max", label = "Toelichting beëindiging")
	private String toelichtingBeeindiging;

	/**
	 * De status van de BPV, hierboven staan de verschillende statussen beschreven
	 */
	@Bron
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@AutoForm(editorClass = EnumCombobox.class, htmlClasses = "unit_max")
	private BPVStatus status;

	@Column(nullable = false)
	@AutoForm(label = "Opnemen in BRON")
	@Bron
	private boolean opnemenInBron = true;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bpvInschrijving")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	private List<BPVInschrijvingVrijVeld> vrijVelden;

	/**
	 * De bijlages van deze inschrijving
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bpvInschrijving")
	private List<BPVInschrijvingBijlage> bijlagen;

	public static enum PraktijkbiedendeOrganisatie
	{
		BPVBEDRIJF
		{
			@Override
			public String toString()
			{
				return "BPV-bedrijf";
			}
		},

		CONTRACTPARTNER
		{
			@Override
			public String toString()
			{
				return "Contractpartner";
			}
		};
	}

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@AutoForm(editorClass = EnumRadioChoice.class)
	private PraktijkbiedendeOrganisatie praktijkbiedendeOrganisatie;

	/**
	 * De verwachte einddatum van de BPV, verplicht bij status volledig.
	 */
	@Bron
	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date verwachteEinddatum;

	@Lob
	@Column(nullable = true)
	@AutoForm(htmlClasses = "unit_max")
	private String opmerkingen;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@AutoForm(label = "BRON-status")
	private BronEntiteitStatus bronStatus = BronEntiteitStatus.Geen;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = true)
	@AutoForm(label = "BRON-datum")
	private Date bronDatum;

	@Column(nullable = true)
	@AutoForm(description = "Geeft aan dat het BPV-bedrijf de betalingsplicht van alle kosten overneemt.")
	private Boolean neemtBetalingsplichtOver = Boolean.FALSE;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bpvPlaats", nullable = true)
	@Index(name = "idx_bpvI_bpvPlaats")
	private BPVPlaats bpvPlaats;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bpvInschrijving")
	private List<OnderwijsproductAfname> onderwijsproductAfnames =
		new ArrayList<OnderwijsproductAfname>();

	public BPVInschrijving()
	{
	}

	public BPVInschrijving(Verbintenis verbintenis)
	{
		this.verbintenis = verbintenis;
		wijsNieuwVolgnummerToe();
	}

	@Exportable
	public Date getAfsluitdatum()
	{
		return afsluitdatum;
	}

	public void setAfsluitdatum(Date afsluitdatum)
	{
		this.afsluitdatum = afsluitdatum;
	}

	@Exportable
	public Verbintenis getVerbintenis()
	{
		return verbintenis;
	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = verbintenis;
	}

	public int getVolgnummer()
	{
		return volgnummer;
	}

	public void setVolgnummer(int volgnummer)
	{
		this.volgnummer = volgnummer;
	}

	public void wijsNieuwVolgnummerToe()
	{
		volgnummer = verbintenis.getVolgendeBPVInschrijvingVolgnummer();
	}

	@Exportable
	public ExterneOrganisatie getBpvBedrijf()
	{
		return bpvBedrijf;
	}

	public void setBpvBedrijf(ExterneOrganisatie bpvBedrijf)
	{
		this.bpvBedrijf = bpvBedrijf;
	}

	@Exportable
	public Medewerker getPraktijkbegeleider()
	{
		return praktijkbegeleider;
	}

	public void setPraktijkbegeleider(Medewerker praktijkbegeleider)
	{
		this.praktijkbegeleider = praktijkbegeleider;
	}

	@Exportable
	public Integer getTotaleOmvang()
	{
		return totaleOmvang;
	}

	public void setTotaleOmvang(Integer totaleOmvang)
	{
		this.totaleOmvang = totaleOmvang;
	}

	@Exportable
	public BigDecimal getUrenPerWeek()
	{
		return urenPerWeek;
	}

	public void setUrenPerWeek(BigDecimal urenPerWeek)
	{
		this.urenPerWeek = urenPerWeek;
	}

	@Exportable
	public Integer getDagenPerWeek()
	{
		return dagenPerWeek;
	}

	public void setDagenPerWeek(Integer dagenPerWeek)
	{
		this.dagenPerWeek = dagenPerWeek;
	}

	@Exportable
	public BPVBedrijfsgegeven getBedrijfsgegeven()
	{
		return bedrijfsgegeven;
	}

	public void setBedrijfsgegeven(BPVBedrijfsgegeven bedrijfsgegeven)
	{
		this.bedrijfsgegeven = bedrijfsgegeven;
		if (bedrijfsgegeven != null)
		{
			this.bpvBedrijf = bedrijfsgegeven.getExterneOrganisatie();
		}
	}

	@Exportable
	public ExterneOrganisatie getContractpartner()
	{
		return contractpartner;
	}

	public void setContractpartner(ExterneOrganisatie contractpartner)
	{
		this.contractpartner = contractpartner;
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

	@Exportable
	public BPVStatus getStatus()
	{
		return status;
	}

	public void setStatus(BPVStatus status)
	{
		this.status = status;
	}

	@Override
	@Exportable
	public Date getEinddatum()
	{
		return super.getEinddatum();
	}

	@Exportable
	public Date getVerwachteEinddatum()
	{
		return verwachteEinddatum;
	}

	public void setVerwachteEinddatum(Date verwachteEinddatum)
	{
		this.verwachteEinddatum = verwachteEinddatum;
	}

	public String getTot()
	{
		if (getEinddatum() != null)
			return TimeUtil.getInstance().formatDate(getEinddatum());
		if (getVerwachteEinddatum() != null)
			return "(verwacht " + TimeUtil.getInstance().formatDate(getVerwachteEinddatum()) + ")";
		return null;
	}

	@Exportable
	public String getOpmerkingen()
	{
		return opmerkingen;
	}

	public void setOpmerkingen(String opmerkingen)
	{
		this.opmerkingen = opmerkingen;
	}

	public void setContactPersoonContractpartner(
			ExterneOrganisatieContactPersoon contactPersoonContractpartner)
	{
		this.contactPersoonContractpartner = contactPersoonContractpartner;
	}

	@Exportable
	public ExterneOrganisatieContactPersoon getContactPersoonContractpartner()
	{
		return contactPersoonContractpartner;
	}

	public void setContactPersoonBPVBedrijf(
			ExterneOrganisatieContactPersoon contactPersoonBPVBedrijf)
	{
		this.contactPersoonBPVBedrijf = contactPersoonBPVBedrijf;
	}

	@Exportable
	public ExterneOrganisatieContactPersoon getContactPersoonBPVBedrijf()
	{
		return contactPersoonBPVBedrijf;
	}

	public void setOpnemenInBron(boolean opnemenInBron)
	{
		this.opnemenInBron = opnemenInBron;
	}

	public boolean isOpnemenInBron()
	{
		return opnemenInBron;
	}

	public void setPraktijkbiedendeOrganisatie(
			PraktijkbiedendeOrganisatie praktijkbiedendeOrganisatie)
	{
		this.praktijkbiedendeOrganisatie = praktijkbiedendeOrganisatie;
	}

	@Exportable
	public PraktijkbiedendeOrganisatie getPraktijkbiedendeOrganisatie()
	{
		return praktijkbiedendeOrganisatie;
	}

	public void setToelichtingBeeindiging(String toelichtingBeeindiging)
	{
		this.toelichtingBeeindiging = toelichtingBeeindiging;
	}

	@Exportable
	public String getToelichtingBeeindiging()
	{
		return toelichtingBeeindiging;
	}

	@Exportable
	@Override
	public Deelnemer getDeelnemer()
	{
		return verbintenis.getDeelnemer();
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

	@Override
	public void setBronStatus(BronEntiteitStatus bronStatus)
	{
		this.bronStatus = bronStatus;
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
	public String getCodeLeerbedrijf()
	{
		if (bedrijfsgegeven != null)
			return bedrijfsgegeven.getCodeLeerbedrijf();
		else
			return new String();
	}

	@Exportable
	public String getKenniscentrum()
	{
		if (bedrijfsgegeven != null)
			return bedrijfsgegeven.getKenniscentrum().getNaam();
		else
			return null;
	}

	@Override
	public BPVInschrijvingBijlage addBijlage(Bijlage bijlage)
	{
		BPVInschrijvingBijlage newBijlage = new BPVInschrijvingBijlage();
		newBijlage.setBijlage(bijlage);
		newBijlage.setBpvInschrijving(this);
		newBijlage.setDeelnemer(getDeelnemer());

		getBijlagen().add(newBijlage);

		return newBijlage;
	}

	@Override
	public boolean bestaatBijlage(Bijlage bijlage)
	{
		for (BPVInschrijvingBijlage deelbijlage : getBijlagen())
		{
			if (deelbijlage.getBijlage().equals(bijlage))
				return true;
		}
		return false;
	}

	@Override
	@Exportable
	public List<BPVInschrijvingBijlage> getBijlagen()
	{
		if (bijlagen == null)
			bijlagen = new ArrayList<BPVInschrijvingBijlage>();
		return bijlagen;
	}

	@Override
	public void setBijlagen(List<BPVInschrijvingBijlage> bijlagen)
	{
		this.bijlagen = bijlagen;
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

	@Override
	public String toString()
	{
		StringBuilder res = new StringBuilder();

		res.append(getBpvBedrijf().getNaam());
		res.append(" (").append(getId()).append(")");

		return res.toString();
	}

	@Exportable
	public String getPraktijkopleider()
	{
		if (getPraktijkopleiderBPVBedrijf() != null)
		{
			return getPraktijkopleiderBPVBedrijf().toString();
		}
		else
		{
			return getNaamPraktijkopleiderBPVBedrijf();
		}
	}

	@Exportable
	public ExterneOrganisatieContactPersoon getPraktijkopleiderBPVBedrijf()
	{
		return praktijkopleiderBPVBedrijf;
	}

	public void setPraktijkopleiderBPVBedrijf(
			ExterneOrganisatieContactPersoon praktijkopleiderBPVBedrijf)
	{
		this.praktijkopleiderBPVBedrijf = praktijkopleiderBPVBedrijf;
	}

	public void setGerealiseerdeOmvang(Integer gerealiseerdeOmvang)
	{
		this.gerealiseerdeOmvang = gerealiseerdeOmvang;
	}

	public Integer getGerealiseerdeOmvang()
	{
		return gerealiseerdeOmvang;
	}

	@Exportable
	@Override
	public Date getBegindatum()
	{
		return super.getBegindatum();
	}

	public void setNeemtBetalingsplichtOver(Boolean neemtBetalingsplichtOver)
	{
		this.neemtBetalingsplichtOver = neemtBetalingsplichtOver;
	}

	public Boolean getNeemtBetalingsplichtOver()
	{
		return neemtBetalingsplichtOver;
	}

	public String getNaamPraktijkopleiderBPVBedrijf()
	{
		return naamPraktijkopleiderBPVBedrijf;
	}

	public void setNaamPraktijkopleiderBPVBedrijf(String naamPraktijkopleiderBPVBedrijf)
	{
		this.naamPraktijkopleiderBPVBedrijf = naamPraktijkopleiderBPVBedrijf;
	}

	@Override
	public boolean isActief(Date peildatum)
	{
		return getStatus() != null && getStatus().isActief() && super.isActief(peildatum);
	}

	@Exportable
	public String getWerkdagen()
	{
		return werkdagen;
	}

	public void setWerkdagen(String werkdagen)
	{
		this.werkdagen = werkdagen;
	}

	@Exportable
	public String getLocatiePOK()
	{
		return locatiePOK;
	}

	public void setLocatiePOK(String locatiePOK)
	{
		this.locatiePOK = locatiePOK;
	}

	@Override
	public List<BPVInschrijvingVrijVeld> getVrijVelden()
	{
		if (vrijVelden == null)
			vrijVelden = new ArrayList<BPVInschrijvingVrijVeld>();

		return vrijVelden;
	}

	@Override
	public List<BPVInschrijvingVrijVeld> getVrijVelden(VrijVeldCategorie categorie)
	{
		List<BPVInschrijvingVrijVeld> res = new ArrayList<BPVInschrijvingVrijVeld>();
		for (BPVInschrijvingVrijVeld pvv : getVrijVelden())
		{
			if (pvv.getVrijVeld().getCategorie().equals(categorie))
			{
				res.add(pvv);
			}
		}
		return res;
	}

	@Override
	public BPVInschrijvingVrijVeld newVrijVeld()
	{
		BPVInschrijvingVrijVeld pvv = new BPVInschrijvingVrijVeld();
		pvv.setBpvInschrijving(this);
		return pvv;
	}

	@Override
	public void setVrijVelden(List<BPVInschrijvingVrijVeld> vrijvelden)
	{
		this.vrijVelden = vrijvelden;
	}

	@Override
	@Exportable
	public String getVrijVeldWaarde(String naamVrijVeld)
	{
		for (BPVInschrijvingVrijVeld vrijVeld : vrijVelden)
		{
			if (vrijVeld.getVrijVeld().getNaam().equals(naamVrijVeld))
				return vrijVeld.getOmschrijving();
		}
		return null;
	}

	public void setBpvPlaats(BPVPlaats bpvPlaats)
	{
		this.bpvPlaats = bpvPlaats;
	}

	public BPVPlaats getBpvPlaats()
	{
		return bpvPlaats;
	}

	public void setOnderwijsproductAfnames(List<OnderwijsproductAfname> onderwijsproductAfnames)
	{
		this.onderwijsproductAfnames = onderwijsproductAfnames;
	}

	public List<OnderwijsproductAfname> getOnderwijsproductAfnames()
	{
		return onderwijsproductAfnames;
	}
}