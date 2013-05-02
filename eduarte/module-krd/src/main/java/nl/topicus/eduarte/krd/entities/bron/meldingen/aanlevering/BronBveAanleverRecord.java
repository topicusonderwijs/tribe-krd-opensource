package nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering;

import static nl.topicus.onderwijs.duo.bron.BRONConstants.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.JavaUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.eduarte.entities.BronMeldingOnderdeel;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.bron.BronEduArteModel;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchBVE;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.BronBveTerugkoppelRecord;
import nl.topicus.onderwijs.duo.bron.BronRecordTypeDiscriminator;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.PersoonsgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.WijzigingSleutelgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.BpvGegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.ExamengegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.InschrijvingsgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.PeriodegegevensInschrijvingRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.NT2Vaardigheden;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.ResultaatgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.vavo.VakgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.*;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.HuisnummerAanduiding;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.BeoordelingSchoolExamen;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.BeoordelingWerkstuk;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.HogerNiveau;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.ToepassingResultaat;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "BRON_BVE_AANLEVERRECORDS")
public class BronBveAanleverRecord extends InstellingEntiteit
		implements
		BronRecordTypeDiscriminator,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.PersoonsgegevensRecord,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.WijzigingSleutelgegevensRecord,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.BpvGegevensRecord,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.ExamengegevensRecord,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.InschrijvingsgegevensRecord,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.PeriodegegevensInschrijvingRecord,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.InschrijvingsgegevensRecord,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.NT2Vaardigheden,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.ResultaatgegevensRecord,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.VakgegevensRecord,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.vavo.InschrijvingsgegevensRecord,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.vavo.VakgegevensRecord,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.vavo.ExamengegevensRecord,
		IVakMelding
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "melding", nullable = false)
	@Index(name = "IDX_BRON_BVE_AAN_MELDING")
	@AutoFormEmbedded
	private BronAanleverMelding melding;

	@Column(nullable = false, insertable = true, updatable = false)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	@AutoForm(include = false)
	private int recordType;

	/**
	 * Index kolom voor het in de juiste volgorde inlezen van de records die aan een
	 * melding hangen.
	 */
	@SuppressWarnings("unused")
	@Column(name = "recordNummer")
	@RestrictedAccess(hasGetter = false, hasSetter = true)
	@AutoForm(include = false)
	private int recordNummer;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private SoortMutatie soortMutatie;

	@Column(nullable = true)
	@RestrictedAccess(hasSetter = false)
	@AutoForm(include = false)
	@Lob
	private String reden;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "terugkoppelrecord")
	@Index(name = "idx_BRON_BVE_AAN_terugkoppelr")
	private BronBveTerugkoppelRecord terugkoppelrecord;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "verbintenis")
	@Index(name = "idx_BRON_BVE_AAN_verbintenis")
	@RestrictedAccess(hasSetter = false)
	private Verbintenis verbintenis;

	/*
	 * WIJZIGING SLEUTELGEGEVENS
	 */
	@Column(length = 9, name = "sofinummerGewijzigd")
	private String sofinummerAchterhaald;

	@Column(length = 6, name = "postcodeGewijzigd")
	private String postcodeVolgensInstellingGewijzigd;

	@Column(length = 4)
	private String landGewijzigd;

	@Enumerated(EnumType.STRING)
	private Geslacht geslachtGewijzigd;

	@Column()
	@Type(type = "nl.topicus.eduarte.krd.hibernate.usertypes.DatumUsertype")
	private Datum geboortedatumGewijzigd;

	@Column()
	private Date datumIngangAdreswijziging;

	/*
	 * BO INSCHRIJFGEGEVENS
	 */
	private Date werkelijkeDatumUitschrijving;

	private Integer locatie;

	@Enumerated(EnumType.STRING)
	private Leerweg leerweg;

	@Column(length = 5)
	private String laatsteVooropleiding;

	@Enumerated(EnumType.STRING)
	private Intensiteit intensiteit;

	@Column(name = "risicodeelnemer")
	private Boolean indicatieRisicodeelnemer;

	@Column(name = "gehandicapt")
	private Boolean indicatieGehandicapt;

	@Enumerated(EnumType.STRING)
	private HoogsteVooropleiding hoogsteVooropleiding;

	@Column(length = 5)
	private String gevolgdeOpleiding;

	private Date geplandeDatumUitschrijving;

	private Date datumInschrijving;

	@Enumerated(EnumType.STRING)
	private RedenUitval redenUitstroom;

	/*
	 * PERIODEGEGEVENS BO INSCHRIJVING
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bekostingsperiode")
	@Index(name = "idx_bron_bve_aan_bekostig")
	@RestrictedAccess(hasSetter = false)
	private Bekostigingsperiode bekostigingsperiode;

	@Column(name = "datumIngangPeriodegegevens")
	private Date datumIngangPeriodegegevensInschrijving;

	@Column(name = "bekostigingInschrijving")
	private Boolean indicatieBekostigingInschrijving;

	@Column(length = 1, name = "lesgeld")
	private String indicatieLesgeld;

	/*
	 * BPV INSCHRIJFGEGEVENS
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bpvInschrijving")
	@Index(name = "idx_BRON_BVE_AAN_bpvInschrijv")
	@RestrictedAccess(hasSetter = false)
	private BPVInschrijving bpvInschrijving;

	private Date afsluitdatumBpv;

	@Column(length = 4)
	private String brinCodeKbb;

	@Column(length = 5)
	private String creboCodeBpv;

	private Date datumBeginBpv;

	private Date geplandeDatumEindeBpv;

	private Integer huisnummerLeerbedrijf;

	@Column(length = 40)
	private String leerbedrijf;

	private Integer omvangBpv;

	@Column(length = 80)
	private String naamLeerbedrijf;

	@Column(length = 6)
	private String postcodeLeerbedrijf;

	private Date werkelijkeDatumEindeBpv;

	/*
	 * EXAMENGEGEVENS BO
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "examendeelname")
	@Index(name = "idx_BRON_BVE_AAN_examen")
	@RestrictedAccess(hasSetter = false)
	private Examendeelname examenDeelname;

	@Column(name = "indBekostigingDiploma")
	private Boolean indicatieBekostigingDiploma;

	@Column
	private Date datumBehaald;

	@Column(length = 5)
	private String behaaldeDeelKwalificatie;

	/*
	 * EXAMENGEGEVENS VAVO
	 */
	@Enumerated(EnumType.STRING)
	private BeoordelingWerkstuk beoordelingWerkstuk;

	@Column(length = 3)
	private String cijferWerkstuk;

	@Temporal(TemporalType.DATE)
	private Date datumUitslagExamen;

	@Column(length = 4)
	private String examen;

	@Column()
	private Integer examenjaar;

	@Column(length = 150)
	private String titelThemaWerkstuk;

	@Enumerated(EnumType.STRING)
	private ToepassingBeoordelingWerkstuk toepassingBeoordelingWerkstuk;

	@Enumerated(EnumType.STRING)
	private UitslagExamen uitslagExamen;

	/*
	 * INSCHRIJFGEGEVENS VAVO
	 */

	private Integer contacturenPerWeek;

	@Column(name = "nieuwkomer")
	private Boolean indicatieNieuwkomer;

	/*
	 * PERSOONSGEGEVENS
	 */
	@Column(length = 200)
	private String achternaam;

	@Column(length = 10)
	private String voorvoegsel;

	@Column(length = 200)
	private String alleVoornamen;

	@Column(length = 24)
	private String straatnaam;

	private Integer huisnummer;

	@Column(length = 5)
	private String huisnummerToevoeging;

	@Column(length = 35)
	private String locatieOmschrijving;

	@Enumerated(EnumType.STRING)
	private HuisnummerAanduiding huisnummerAanduiding;

	@Column(length = 24)
	private String plaatsnaam;

	@Column(length = 35)
	private String adresregelBuitenland1;

	@Column(length = 35)
	private String adresregelBuitenland2;

	@Column(length = 35)
	private String adresregelBuitenland3;

	@Column(length = 4)
	private String nationaliteit1;

	@Column(length = 4)
	private String nationaliteit2;

	/*
	 * VAKGEGEVENS VAVO
	 */
	@Column(name = "combinatiecijfer")
	private Boolean indicatieCombinatieCijfer;

	@Enumerated(EnumType.STRING)
	private BeoordelingSchoolExamen beoordelingSchoolExamen;

	private Integer cijferCE1;

	private Integer cijferCE2;

	private Integer cijferCE3;

	private Integer cijferCijferlijst;

	private Integer cijferSchoolExamen;

	private Integer derdeEindcijfer;

	private Integer eersteEindcijfer;

	private String examenvak;

	@Enumerated(EnumType.STRING)
	private HogerNiveau hogerNiveau;

	@Column(name = "certificaatBehaald")
	private Boolean certificaat;

	@Column(name = "diplomavak")
	private Boolean indicatieDiplomavak;

	@Column(name = "naarVolgendTijdvak")
	private Boolean verwezenNaarVolgendeTijdvak;

	@Column(name = "werkstuk")
	private Boolean indicatieWerkstuk;

	@Enumerated(EnumType.STRING)
	private ToepassingResultaatExamenvak toepassingResultaatExamenvak;

	private Integer tweedeEindcijfer;

	private Integer vakCodeHogerNiveau;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "afnameContext")
	@Index(name = "idx_BRON_BVE_AAN_ondafctx")
	// @RestrictedAccess(hasSetter = false)
	private OnderwijsproductAfnameContext afnameContext;

	/*
	 * EDUCATIE NT2VAARDIGHEDEN
	 */

	@Enumerated(EnumType.STRING)
	private NT2Niveau behaaldNiveau;

	@Enumerated(EnumType.STRING)
	private NT2Vaardigheid nT2Vaardigheid;

	@Enumerated(EnumType.STRING)
	private NT2Niveau startniveau;

	private Integer vakvolgnummer;

	/*
	 * EDUCATIE RESULTAATGEGEVENS
	 */

	private Date datumVoltooid;

	@Column(length = 5)
	private String voltooideOpleiding;

	/*
	 * EDUCATIE VAKGEGEVENS
	 */

	@Column(length = 4)
	private String vak;

	@RestrictedAccess(hasGetter = true, hasSetter = false)
	@Transient
	private transient String identifier;

	/**
	 * NIET AANROEPEN! Enkel voor gebruik door Hibernate en de HibernateObjectCopyManager.
	 * Zie de static factory methods voor het construeren van deze records.
	 */
	public BronBveAanleverRecord()
	{
		// Enkel voor Hibernate
	}

	private BronBveAanleverRecord(int recordType, BronAanleverMelding aanleverMelding)
	{
		this.melding = aanleverMelding;
		this.recordType = recordType;

		melding.getMeldingen().add(this);
		recordNummer = melding.getMeldingen().size();

		// standaard bij het aanmaken de status op wachtrij zetten
		aanleverMelding.setBronMeldingStatus(BronMeldingStatus.WACHTRIJ);
	}

	private BronBveAanleverRecord(int recordType, BronAanleverMelding aanleverMelding,
			Verbintenis verbintenis)
	{
		this(recordType, aanleverMelding);
		this.verbintenis = verbintenis;
		melding.setVerbintenis(verbintenis);
	}

	private BronBveAanleverRecord(int recordType, BronAanleverMelding aanleverMelding,
			BPVInschrijving inschrijving)
	{
		this(recordType, aanleverMelding, inschrijving.getVerbintenis());
		this.bpvInschrijving = inschrijving;
	}

	private BronBveAanleverRecord(int recordType, BronAanleverMelding aanlevermelding,
			Verbintenis verbintenis, Examendeelname deelname)
	{
		this(recordType, aanlevermelding, verbintenis);
		this.examenDeelname = deelname;
	}

	private BronBveAanleverRecord(int recordType, BronAanleverMelding aanlevermelding,
			Verbintenis verbintenis, Examendeelname deelname,
			OnderwijsproductAfnameContext afnameContext)
	{
		this(recordType, aanlevermelding, verbintenis);
		this.examenDeelname = deelname;
		this.afnameContext = afnameContext;
	}

	private BronBveAanleverRecord(int recordType, BronAanleverMelding melding,
			Verbintenis verbintenis, Bekostigingsperiode periode)
	{
		this(recordType, melding, verbintenis);
		this.bekostigingsperiode = periode;
	}

	private BronBveAanleverRecord(int recordType, BronAanleverMelding melding,
			Verbintenis verbintenis, OnderwijsproductAfnameContext context)
	{
		this(recordType, melding, verbintenis);
		this.afnameContext = context;
	}

	public void ververs(boolean voegNieuweMeldingenToe)
	{
		switch (recordType)
		{
			case BVE_AANLEVERING_WIJZIGING_SLEUTELGEGEVENS:
				return;
			case BVE_AANLEVERING_PERSOONSGEGEVENS:
				vulPersoonsgegevensRecord();
				return;
			case BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS:
				vulBoInschrijfgegevens();
				return;
			case BVE_AANLEVERING_BO_PERIODEGEGEVENS:
				vulBoPeriodegegevens();
				return;
			case BVE_AANLEVERING_BO_BPVGEGEVENS:
				vulBoBpvgegevensInschrijving();
				return;
			case BVE_AANLEVERING_BO_EXAMENGEGEVENS:
				vulBoExamengegevens(voegNieuweMeldingenToe);
				return;
			case BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS:
				vulEdInschrijvingsgegevensRecord();
				return;
			case BVE_AANLEVERING_ED_NT2VAARDIGHEDEN:
				return;
			case BVE_AANLEVERING_ED_RESULTAATGEGEVENS:
				vulEdResultaatgegevensRecord();
				return;
			case BVE_AANLEVERING_ED_VAKGEGEVENS:
				vulEdVakgegevensRecord();
				return;
			case BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS:
				vulVavoInschrijvingsgegevensRecord();
				return;
			case BVE_AANLEVERING_VAVO_VAKGEGEVENS:
				return;
			case BVE_AANLEVERING_VAVO_EXAMENGEGEVENS:
				return;
		}
		throw new IllegalStateException(recordType + " is een onbekend BRON recordtype");
	}

	public int getRecordType()
	{
		return recordType;
	}

	public void setTerugkoppelrecord(BronBveTerugkoppelRecord terugkoppelrecord)
	{
		this.terugkoppelrecord = terugkoppelrecord;
	}

	public BronBveTerugkoppelRecord getTerugkoppelrecord()
	{
		return terugkoppelrecord;
	}

	@Override
	public Class< ? > getActualRecordType()
	{
		switch (recordType)
		{
			case BVE_AANLEVERING_WIJZIGING_SLEUTELGEGEVENS:
				return nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.WijzigingSleutelgegevensRecord.class;

			case BVE_AANLEVERING_PERSOONSGEGEVENS:
				return nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.PersoonsgegevensRecord.class;

			case BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS:
				return nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.InschrijvingsgegevensRecord.class;
			case BVE_AANLEVERING_BO_PERIODEGEGEVENS:
				return nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.PeriodegegevensInschrijvingRecord.class;
			case BVE_AANLEVERING_BO_BPVGEGEVENS:
				return nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.BpvGegevensRecord.class;
			case BVE_AANLEVERING_BO_EXAMENGEGEVENS:
				return nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.ExamengegevensRecord.class;

			case BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS:
				return nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.InschrijvingsgegevensRecord.class;
			case BVE_AANLEVERING_ED_NT2VAARDIGHEDEN:
				return nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.NT2Vaardigheden.class;
			case BVE_AANLEVERING_ED_RESULTAATGEGEVENS:
				return nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.ResultaatgegevensRecord.class;
			case BVE_AANLEVERING_ED_VAKGEGEVENS:
				return nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.VakgegevensRecord.class;

			case BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS:
				return nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.vavo.InschrijvingsgegevensRecord.class;
			case BVE_AANLEVERING_VAVO_VAKGEGEVENS:
				return nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.vavo.VakgegevensRecord.class;
			case BVE_AANLEVERING_VAVO_EXAMENGEGEVENS:
				return nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.vavo.ExamengegevensRecord.class;
		}
		throw new IllegalStateException(recordType + " is een onbekend BRON recordtype");
	}

	public BronMeldingOnderdeel getBronMeldingOnderdeel()
	{
		BronMeldingOnderdeel[] onderdelen = BronMeldingOnderdeel.values();
		for (BronMeldingOnderdeel onderdeel : onderdelen)
		{
			if (onderdeel.getRecordTypes().contains(recordType))
				return onderdeel;
		}
		return null;
	}

	public BronAanleverMelding getMelding()
	{
		return melding;
	}

	public void setMelding(BronAanleverMelding melding)
	{
		this.melding = melding;
	}

	public final Integer getMeldingnummer()
	{
		return getMelding().getMeldingnummer();
	}

	/**
	 * LET OP: Deze methode geeft de soortmutatie terug van de VO waardelijst
	 */
	public nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie getMutatieSoort()
	{
		if (soortMutatie == null)
			return null;
		switch (soortMutatie)
		{
			case Toevoeging:
				return nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie.Toevoeging;
			case Aanpassing:
				return nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie.Aanpassing;
			case Verwijdering:
				return nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie.Verwijdering;
			default:
				return null;
		}
	}

	public SoortMutatie getSoortMutatie()
	{
		return soortMutatie;
	}

	public Verbintenis getVerbintenis()
	{
		return verbintenis;
	}

	public Deelnemer getDeelnemer()
	{
		return melding.getDeelnemer();
	}

	public BronBatchBVE getBatch()
	{
		return melding.getBatch();
	}

	public void setSoortMutatie(SoortMutatie soortMutatie)
	{
		this.soortMutatie = soortMutatie;
	}

	public String getReden()
	{
		return reden;
	}

	public void addReden(String wijziging)
	{
		if (StringUtil.isEmpty(wijziging))
			return;
		if (StringUtil.isEmpty(reden))
		{
			reden = wijziging.substring(0, Math.min(4000, wijziging.length()));
		}
		else
		{
			int length = reden.length() + 1 + wijziging.length();
			if (length <= 4000)
			{
				reden = reden + "\n" + wijziging;
			}
		}
	}

	public final String getOnderwijssoort()
	{
		if (recordType < 300)
		{
			return "Voortgezet Onderwijs";
		}
		if (recordType >= 320 && recordType <= 323)
		{
			return "Beroepsonderwijs";
		}
		if (recordType >= 325 && recordType <= 329)
		{
			return "Educatie";
		}
		if (recordType >= 330 && recordType <= 339)
		{
			return "VAVO";
		}
		return "Niet bekend";
	}

	@Override
	public String getInschrijvingsvolgnummer()
	{
		if (verbintenis != null)
		{
			if (StringUtil.isNotEmpty(verbintenis.getVolgnummerInOudPakket()))
				return verbintenis.getVolgnummerInOudPakket();
			else
				return String.valueOf(verbintenis.getVolgnummer());
		}
		return null;
	}

	/*
	 * WIJZIGING SLEUTELGEGEVENS
	 */

	@Override
	public Date getDatumIngangAdreswijziging()
	{
		return datumIngangAdreswijziging;
	}

	public void setDatumIngangAdreswijziging(Date datumIngangAdreswijziging)
	{
		this.datumIngangAdreswijziging = datumIngangAdreswijziging;
	}

	@Override
	public Datum getGeboortedatumGewijzigd()
	{
		return geboortedatumGewijzigd;
	}

	public void setGeboortedatumGewijzigd(Datum geboortedatumGewijzigd)
	{
		this.geboortedatumGewijzigd = geboortedatumGewijzigd;
	}

	@Override
	public Geslacht getGeslachtGewijzigd()
	{
		return geslachtGewijzigd;
	}

	public void setGeslachtGewijzigd(Geslacht geslachtGewijzigd)
	{
		this.geslachtGewijzigd = geslachtGewijzigd;
	}

	@Override
	public String getLandGewijzigd()
	{
		return landGewijzigd;
	}

	public void setLandGewijzigd(String landGewijzigd)
	{
		this.landGewijzigd = landGewijzigd;
	}

	@Override
	public String getPostcodeVolgensInstellingGewijzigd()
	{
		return postcodeVolgensInstellingGewijzigd;
	}

	public void setPostcodeVolgensInstellingGewijzigd(String postcode)
	{
		String postcodeZonderSpaties = (postcode != null) ? postcode.replaceAll(" ", "") : postcode;
		Asserts.assertMaxLength("postcode", postcodeZonderSpaties, 6);
		this.postcodeVolgensInstellingGewijzigd = postcodeZonderSpaties;
	}

	@Override
	public String getSofinummerAchterhaald()
	{
		return sofinummerAchterhaald;
	}

	public void setSofinummerAchterhaald(String sofinummerAchterhaald)
	{
		this.sofinummerAchterhaald = sofinummerAchterhaald;
	}

	/*
	 * BO INSCHRIJFGEGEVENS
	 */
	@Override
	public Date getDatumInschrijving()
	{
		return datumInschrijving;
	}

	@Override
	public Date getGeplandeDatumUitschrijving()
	{
		return geplandeDatumUitschrijving;
	}

	@Override
	public String getGevolgdeOpleiding()
	{
		return gevolgdeOpleiding;
	}

	@Override
	public HoogsteVooropleiding getHoogsteVooropleiding()
	{
		return hoogsteVooropleiding;
	}

	@Override
	public Boolean getIndicatieGehandicapt()
	{
		return indicatieGehandicapt;
	}

	@Override
	public Boolean getIndicatieRisicodeelnemer()
	{
		return indicatieRisicodeelnemer;
	}

	@Override
	public Intensiteit getIntensiteit()
	{
		return intensiteit;
	}

	@Override
	public String getLaatsteVooropleiding()
	{
		return laatsteVooropleiding;
	}

	@Override
	public Leerweg getLeerweg()
	{
		return leerweg;
	}

	@Override
	public Integer getLocatie()
	{
		return locatie;
	}

	@Override
	public Date getWerkelijkeDatumUitschrijving()
	{
		return werkelijkeDatumUitschrijving;
	}

	@Override
	public RedenUitval getRedenUitstroom()
	{
		return redenUitstroom;
	}

	@Override
	public void setWerkelijkeDatumUitschrijving(Date werkelijkeDatumUitschrijving)
	{
		this.werkelijkeDatumUitschrijving = werkelijkeDatumUitschrijving;
	}

	@Override
	public void setLocatie(Integer locatie)
	{
		this.locatie = locatie;
	}

	@Override
	public void setLeerweg(Leerweg leerweg)
	{
		this.leerweg = leerweg;
	}

	@Override
	public void setLaatsteVooropleiding(String laatsteVooropleiding)
	{
		Asserts.assertMaxLength("laatsteVooropleiding", laatsteVooropleiding, 5);
		this.laatsteVooropleiding = laatsteVooropleiding;
	}

	@Override
	public void setIntensiteit(Intensiteit intensiteit)
	{
		this.intensiteit = intensiteit;
	}

	@Override
	public void setIndicatieRisicodeelnemer(Boolean indicatieRisicodeelnemer)
	{
		this.indicatieRisicodeelnemer = indicatieRisicodeelnemer;
	}

	@Override
	public void setIndicatieGehandicapt(Boolean indicatieGehandicapt)
	{
		this.indicatieGehandicapt = indicatieGehandicapt;
	}

	@Override
	public void setHoogsteVooropleiding(HoogsteVooropleiding hoogsteVooropleiding)
	{
		if (!SoortMutatie.Verwijdering.equals(getSoortMutatie()))
			Asserts.assertNotNull("hoogste vooropleiding", hoogsteVooropleiding);
		this.hoogsteVooropleiding = hoogsteVooropleiding;
	}

	@Override
	public void setGevolgdeOpleiding(String gevolgdeOpleiding)
	{
		this.gevolgdeOpleiding = gevolgdeOpleiding;
	}

	@Override
	public void setGeplandeDatumUitschrijving(Date geplandeDatumUitschrijving)
	{
		this.geplandeDatumUitschrijving = geplandeDatumUitschrijving;
	}

	@Override
	public void setDatumInschrijving(Date datumInschrijving)
	{
		this.datumInschrijving = datumInschrijving;
	}

	@Override
	public void setRedenUitstroom(RedenUitval redenUitstroom)
	{
		this.redenUitstroom = redenUitstroom;
	}

	/*
	 * PERIODEGEGEGEVENS INSCHRIJVING BO
	 */
	public Bekostigingsperiode getBekostigingsperiode()
	{
		return bekostigingsperiode;
	}

	@Override
	public Date getDatumIngangPeriodegegevensInschrijving()
	{
		return datumIngangPeriodegegevensInschrijving;
	}

	@Override
	public Boolean getIndicatieBekostigingInschrijving()
	{
		return indicatieBekostigingInschrijving;
	}

	@Override
	public String getIndicatieLesgeld()
	{
		return indicatieLesgeld;
	}

	public void setDatumIngangPeriodegegevensInschrijving(
			Date datumIngangPeriodegegevensInschrijving)
	{
		this.datumIngangPeriodegegevensInschrijving = datumIngangPeriodegegevensInschrijving;
	}

	public void setIndicatieBekostigingInschrijving(Boolean indicatieBekostigingInschrijving)
	{
		this.indicatieBekostigingInschrijving = indicatieBekostigingInschrijving;
	}

	public void setIndicatieLesgeld(String indicatieLesgeld)
	{
		this.indicatieLesgeld = indicatieLesgeld;
	}

	/*
	 * INSCHRIJFGEGEVENS BPV
	 */
	public BPVInschrijving getBpvInschrijving()
	{
		return bpvInschrijving;
	}

	@Override
	public Date getAfsluitdatumBpv()
	{
		return afsluitdatumBpv;
	}

	@Override
	public Integer getBpvVolgnummer()
	{
		return getBpvInschrijving().getVolgnummer();
	}

	@Override
	public String getBrinCodeKbb()
	{
		return brinCodeKbb;
	}

	@Override
	public String getCreboCodeBpv()
	{
		return creboCodeBpv;
	}

	@Override
	public Date getDatumBeginBpv()
	{
		return datumBeginBpv;
	}

	@Override
	public Date getGeplandeDatumEindeBpv()
	{
		return geplandeDatumEindeBpv;
	}

	@Override
	public Integer getHuisnummerLeerbedrijf()
	{
		return huisnummerLeerbedrijf;
	}

	@Override
	public String getLeerbedrijf()
	{
		return leerbedrijf;
	}

	@Override
	public String getNaamLeerbedrijf()
	{
		return naamLeerbedrijf;
	}

	@Override
	public Integer getOmvangBpv()
	{
		return omvangBpv;
	}

	@Override
	public String getPostcodeLeerbedrijf()
	{
		return postcodeLeerbedrijf;
	}

	@Override
	public Date getWerkelijkeDatumEindeBpv()
	{
		return werkelijkeDatumEindeBpv;
	}

	public void setAfsluitdatumBpv(Date afsluitdatumBpv)
	{
		this.afsluitdatumBpv = afsluitdatumBpv;
	}

	public void setBrinCodeKbb(String brinCodeKbb)
	{
		Asserts.assertMaxLength("brinCodeKbb", brinCodeKbb, 4);
		this.brinCodeKbb = brinCodeKbb;
	}

	public void setCreboCodeBpv(String creboCodeBpv)
	{
		Asserts.assertMaxLength("creboCodeBpv", creboCodeBpv, 5);
		this.creboCodeBpv = creboCodeBpv;
	}

	public void setDatumBeginBpv(Date datumBeginBpv)
	{
		this.datumBeginBpv = datumBeginBpv;
	}

	public void setGeplandeDatumEindeBpv(Date geplandeDatumEindeBpv)
	{
		this.geplandeDatumEindeBpv = geplandeDatumEindeBpv;
	}

	public void setHuisnummerLeerbedrijf(Integer huisnummerLeerbedrijf)
	{
		this.huisnummerLeerbedrijf = huisnummerLeerbedrijf;
	}

	public void setLeerbedrijf(String leerbedrijf)
	{
		Asserts.assertMaxLength("leerbedrijf", leerbedrijf, 40);
		this.leerbedrijf = leerbedrijf;
	}

	public void setOmvangBpv(Integer omvangBpv)
	{
		this.omvangBpv = omvangBpv;
	}

	public void setNaamLeerbedrijf(String naamLeerbedrijf)
	{
		Asserts.assertMaxLength("naamLeerbedrijf", naamLeerbedrijf, 80);
		this.naamLeerbedrijf = naamLeerbedrijf;
	}

	public void setPostcodeLeerbedrijf(String postcode)
	{
		String postcodeZonderSpaties = (postcode != null) ? postcode.replaceAll(" ", "") : postcode;
		Asserts.assertMaxLength("postcode", postcodeZonderSpaties, 6);
		this.postcodeLeerbedrijf = postcodeZonderSpaties;
	}

	public void setWerkelijkeDatumEindeBpv(Date werkelijkeDatumEindeBpv)
	{
		this.werkelijkeDatumEindeBpv = werkelijkeDatumEindeBpv;
	}

	@Override
	public String getBehaaldeDeelKwalificatie()
	{
		return behaaldeDeelKwalificatie;
	}

	@Override
	public Date getDatumBehaald()
	{
		return datumBehaald;
	}

	@Override
	public Boolean getIndicatieBekostigingDiploma()
	{
		return indicatieBekostigingDiploma;
	}

	public void setIndicatieBekostigingDiploma(Boolean indicatieBekostigingDiploma)
	{
		this.indicatieBekostigingDiploma = indicatieBekostigingDiploma;
	}

	public Examendeelname getExamenDeelname()
	{
		return examenDeelname;
	}

	public void setDatumBehaald(Date datumBehaald)
	{
		this.datumBehaald = datumBehaald;
	}

	public void setBehaaldeDeelKwalificatie(String behaaldeDeelKwalificatie)
	{
		this.behaaldeDeelKwalificatie = behaaldeDeelKwalificatie;
	}

	/*
	 * EXAMENGEGEVENS VAVO
	 */
	public BeoordelingWerkstuk getBeoordelingWerkstuk()
	{
		return beoordelingWerkstuk;
	}

	public void setBeoordelingWerkstuk(BeoordelingWerkstuk beoordelingWerkstuk)
	{
		this.beoordelingWerkstuk = beoordelingWerkstuk;
	}

	public String getCijferWerkstuk()
	{
		return cijferWerkstuk;
	}

	public void setCijferWerkstuk(String cijferWerkstuk)
	{
		this.cijferWerkstuk = cijferWerkstuk;
	}

	public Date getDatumUitslagExamen()
	{
		return datumUitslagExamen;
	}

	public void setDatumUitslagExamen(Date datumUitslagExamen)
	{
		this.datumUitslagExamen = datumUitslagExamen;
	}

	public String getExamen()
	{
		return examen;
	}

	public void setExamen(String examen)
	{
		this.examen = examen;
	}

	public Integer getExamenjaar()
	{
		return examenjaar;
	}

	public void setExamenjaar(Integer examenjaar)
	{
		this.examenjaar = examenjaar;
	}

	public String getTitelThemaWerkstuk()
	{
		return titelThemaWerkstuk;
	}

	public void setTitelThemaWerkstuk(String titelThemaWerkstuk)
	{
		this.titelThemaWerkstuk = titelThemaWerkstuk;
	}

	public ToepassingBeoordelingWerkstuk getToepassingBeoordelingWerkstuk()
	{
		return toepassingBeoordelingWerkstuk;
	}

	public void setToepassingBeoordelingWerkstuk(
			ToepassingBeoordelingWerkstuk toepassingBeoordelingWerkstuk)
	{
		this.toepassingBeoordelingWerkstuk = toepassingBeoordelingWerkstuk;
	}

	public UitslagExamen getUitslagExamen()
	{
		return uitslagExamen;
	}

	public void setUitslagExamen(UitslagExamen uitslagExamen)
	{
		this.uitslagExamen = uitslagExamen;
	}

	public Integer getContacturenPerWeek()
	{
		return contacturenPerWeek;
	}

	public void setContacturenPerWeek(Integer contacturenPerWeek)
	{
		this.contacturenPerWeek = contacturenPerWeek;
	}

	public Boolean getIndicatieNieuwkomer()
	{
		return indicatieNieuwkomer;
	}

	public void setIndicatieNieuwkomer(Boolean indicatieNieuwkomer)
	{
		this.indicatieNieuwkomer = indicatieNieuwkomer;
	}

	/*
	 * PERSOONSGEGEVENS
	 */
	@Override
	public String getAchternaam()
	{
		return achternaam;
	}

	@Override
	public String getAdresregelBuitenland1()
	{
		return adresregelBuitenland1;
	}

	@Override
	public String getAdresregelBuitenland2()
	{
		return adresregelBuitenland2;
	}

	@Override
	public String getAdresregelBuitenland3()
	{
		return adresregelBuitenland3;
	}

	@Override
	public String getAlleVoornamen()
	{
		return alleVoornamen;
	}

	@Override
	public Integer getHuisnummer()
	{
		return huisnummer;
	}

	@Override
	public HuisnummerAanduiding getHuisnummerAanduiding()
	{
		return huisnummerAanduiding;
	}

	@Override
	public String getHuisnummerToevoeging()
	{
		return huisnummerToevoeging;
	}

	@Override
	public String getLocatieOmschrijving()
	{
		return locatieOmschrijving;
	}

	@Override
	public String getNationaliteit1()
	{
		return nationaliteit1;
	}

	@Override
	public String getNationaliteit2()
	{
		return nationaliteit2;
	}

	@Override
	public String getPlaatsnaam()
	{
		return plaatsnaam;
	}

	@Override
	public String getStraatnaam()
	{
		return straatnaam;
	}

	@Override
	public String getVoorvoegsel()
	{
		return voorvoegsel;
	}

	public void setAchternaam(String achternaam)
	{
		this.achternaam = StringUtil.truncate(achternaam, 200, "");
	}

	public void setVoorvoegsel(String voorvoegsel)
	{
		this.voorvoegsel = StringUtil.truncate(voorvoegsel, 10, "");
	}

	public void setAlleVoornamen(String alleVoornamen)
	{
		this.alleVoornamen = StringUtil.truncate(alleVoornamen, 200, "");
	}

	public void setStraatnaam(String straatnaam)
	{
		this.straatnaam = StringUtil.truncate(straatnaam, 24, "");
	}

	public void setHuisnummer(Integer huisnummer)
	{
		this.huisnummer = huisnummer;
	}

	public void setHuisnummerToevoeging(String huisnummerToevoeging)
	{
		this.huisnummerToevoeging = StringUtil.truncate(huisnummerToevoeging, 5, "");
	}

	public void setLocatieOmschrijving(String locatieOmschrijving)
	{
		this.locatieOmschrijving = StringUtil.truncate(locatieOmschrijving, 35, "");
	}

	public void setHuisnummerAanduiding(HuisnummerAanduiding huisnummerAanduiding)
	{
		this.huisnummerAanduiding = huisnummerAanduiding;
	}

	public void setPlaatsnaam(String plaatsnaam)
	{
		this.plaatsnaam = StringUtil.truncate(plaatsnaam, 24, "");
	}

	public void setAdresregelBuitenland1(String adresregelBuitenland1)
	{
		this.adresregelBuitenland1 = StringUtil.truncate(adresregelBuitenland1, 35, "");
	}

	public void setAdresregelBuitenland2(String adresregelBuitenland2)
	{
		this.adresregelBuitenland2 = StringUtil.truncate(adresregelBuitenland2, 35, "");
	}

	public void setAdresregelBuitenland3(String adresregelBuitenland3)
	{
		this.adresregelBuitenland3 = StringUtil.truncate(adresregelBuitenland3, 35, "");
	}

	public void setNationaliteit1(String nationaliteit1)
	{
		this.nationaliteit1 = nationaliteit1;
	}

	public void setNationaliteit2(String nationaliteit2)
	{
		this.nationaliteit2 = nationaliteit2;
	}

	/*
	 * VAKGEGEVENS VAVO
	 */

	public Boolean getIndicatieCombinatieCijfer()
	{
		return indicatieCombinatieCijfer;
	}

	public void setIndicatieCombinatieCijfer(Boolean indicatieCombinatieCijfer)
	{
		this.indicatieCombinatieCijfer = indicatieCombinatieCijfer;
	}

	public BeoordelingSchoolExamen getBeoordelingSchoolExamen()
	{
		return beoordelingSchoolExamen;
	}

	public void setBeoordelingSchoolExamen(BeoordelingSchoolExamen beoordelingSchoolExamen)
	{
		this.beoordelingSchoolExamen = beoordelingSchoolExamen;
	}

	public Boolean getCertificaat()
	{
		return certificaat;
	}

	public void setCertificaat(Boolean certificaat)
	{
		this.certificaat = certificaat;
	}

	public Boolean getVerwezenNaarVolgendeTijdvak()
	{
		return verwezenNaarVolgendeTijdvak;
	}

	public void setVerwezenNaarVolgendeTijdvak(Boolean verwezenNaarVolgendeTijdvak)
	{
		this.verwezenNaarVolgendeTijdvak = verwezenNaarVolgendeTijdvak;
	}

	public Integer getCijferSchoolExamen()
	{
		return cijferSchoolExamen;
	}

	public void setCijferSchoolExamen(Integer cijferSchoolExamen)
	{
		this.cijferSchoolExamen = cijferSchoolExamen;
	}

	public Integer getVakCodeHogerNiveau()
	{
		return vakCodeHogerNiveau;
	}

	public void setVakCodeHogerNiveau(Integer vakCodeHogerNiveau)
	{
		this.vakCodeHogerNiveau = vakCodeHogerNiveau;
	}

	public OnderwijsproductAfnameContext getAfnameContext()
	{
		return afnameContext;
	}

	public void setAfnameContext(OnderwijsproductAfnameContext context)
	{
		this.afnameContext = context;
	}

	public Integer getCijferCE1()
	{
		return cijferCE1;
	}

	public void setCijferCE1(Integer cijferCE1)
	{
		this.cijferCE1 = cijferCE1;
	}

	public Integer getCijferCE2()
	{
		return cijferCE2;
	}

	public void setCijferCE2(Integer cijferCE2)
	{
		this.cijferCE2 = cijferCE2;
	}

	public Integer getCijferCE3()
	{
		return cijferCE3;
	}

	public void setCijferCE3(Integer cijferCE3)
	{
		this.cijferCE3 = cijferCE3;
	}

	public Integer getCijferCijferlijst()
	{
		return cijferCijferlijst;
	}

	public void setCijferCijferlijst(Integer cijferCijferlijst)
	{
		this.cijferCijferlijst = cijferCijferlijst;
	}

	public Integer getDerdeEindcijfer()
	{
		return derdeEindcijfer;
	}

	public void setDerdeEindcijfer(Integer derdeEindcijfer)
	{
		this.derdeEindcijfer = derdeEindcijfer;
	}

	public Integer getEersteEindcijfer()
	{
		return eersteEindcijfer;
	}

	public void setEersteEindcijfer(Integer eersteEindcijfer)
	{
		this.eersteEindcijfer = eersteEindcijfer;
	}

	public String getExamenvak()
	{
		return examenvak;
	}

	public void setExamenvak(String examenvak)
	{
		this.examenvak = examenvak;
	}

	public HogerNiveau getHogerNiveau()
	{
		return hogerNiveau;
	}

	public void setHogerNiveau(HogerNiveau hogerNiveau)
	{
		this.hogerNiveau = hogerNiveau;
	}

	public Boolean getIndicatieDiplomavak()
	{
		return indicatieDiplomavak;
	}

	public void setIndicatieDiplomavak(Boolean indicatieDiplomavak)
	{
		this.indicatieDiplomavak = indicatieDiplomavak;
	}

	public Boolean getIndicatieWerkstuk()
	{
		return indicatieWerkstuk;
	}

	public void setIndicatieWerkstuk(Boolean indicatieWerkstuk)
	{
		this.indicatieWerkstuk = indicatieWerkstuk;
	}

	public ToepassingResultaatExamenvak getToepassingResultaatExamenvak()
	{
		return toepassingResultaatExamenvak;
	}

	public void setToepassingResultaatExamenvak(
			ToepassingResultaatExamenvak toepassingResultaatExamenvak)
	{
		this.toepassingResultaatExamenvak = toepassingResultaatExamenvak;
	}

	public Integer getTweedeEindcijfer()
	{
		return tweedeEindcijfer;
	}

	public void setTweedeEindcijfer(Integer tweedeEindcijfer)
	{
		this.tweedeEindcijfer = tweedeEindcijfer;
	}

	/*
	 * EDUCATIE NT2VAARDIGHEDEN
	 */
	public NT2Niveau getBehaaldNiveau()
	{
		return behaaldNiveau;
	}

	public void setBehaaldNiveau(NT2Niveau behaaldNiveau)
	{
		this.behaaldNiveau = behaaldNiveau;
	}

	public NT2Vaardigheid getNT2Vaardigheid()
	{
		return nT2Vaardigheid;
	}

	public void setNT2Vaardigheid(NT2Vaardigheid vaardigheid)
	{
		nT2Vaardigheid = vaardigheid;
	}

	public NT2Niveau getStartniveau()
	{
		return startniveau;
	}

	public void setStartniveau(NT2Niveau startniveau)
	{
		this.startniveau = startniveau;
	}

	public Integer getVakvolgnummer()
	{
		return vakvolgnummer;
	}

	public void setVakvolgnummer(Integer vakvolgnummer)
	{
		this.vakvolgnummer = vakvolgnummer;
	}

	/*
	 * EDUCATIE RESULTAATGEGEVENS
	 */
	@Override
	public Date getDatumVoltooid()
	{
		return datumVoltooid;
	}

	public void setDatumVoltooid(Date datumVoltooid)
	{
		this.datumVoltooid = datumVoltooid;
	}

	@Override
	public String getVoltooideOpleiding()
	{
		return voltooideOpleiding;
	}

	public void setVoltooideOpleiding(String voltooideOpleiding)
	{
		this.voltooideOpleiding = voltooideOpleiding;
	}

	/*
	 * EDUCATIE VAKGEGEVENS
	 */
	@Override
	public String getVak()
	{
		return vak;
	}

	public void setVak(String vak)
	{
		this.vak = vak;
	}

	public static PersoonsgegevensRecord newPersoonsgegevensRecord(BronAanleverMelding melding)
	{
		return new BronBveAanleverRecord(BVE_AANLEVERING_PERSOONSGEGEVENS, melding);
	}

	public void vulPersoonsgegevensRecord()
	{
		BronEduArteModel model = new BronEduArteModel();
		Deelnemer deelnemer = getDeelnemer();
		setAchternaam(model.getAchternaam(deelnemer));
		setAlleVoornamen(model.getAlleVoornamen(deelnemer));
		setNationaliteit1(model.getNationaliteit1(deelnemer));
		setNationaliteit2(model.getNationaliteit2(deelnemer));
		setVoorvoegsel(model.getVoorvoegsel(deelnemer));
		if (model.heeftWoonadres(deelnemer))
		{
			setAdresregelBuitenland1(model.getAdresregelBuitenland1(deelnemer));
			setAdresregelBuitenland2(model.getAdresregelBuitenland2(deelnemer));
			setAdresregelBuitenland3(model.getAdresregelBuitenland3(deelnemer));
			setHuisnummer(model.getHuisnummer(deelnemer));
			setHuisnummerAanduiding(model.getHuisnummerAanduiding(deelnemer));
			setHuisnummerToevoeging(model.getHuisnummerToevoeging(deelnemer));
			setLocatieOmschrijving(model.getLocatieOmschrijving(deelnemer));
			setPlaatsnaam(model.getPlaatsnaam(deelnemer));
			setStraatnaam(model.getStraatnaam(deelnemer));
		}
	}

	public static InschrijvingsgegevensRecord newBoInschrijfgegevens(BronAanleverMelding melding,
			Verbintenis verbintenis)
	{
		return new BronBveAanleverRecord(BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS, melding, verbintenis);
	}

	public void vulBoInschrijfgegevens()
	{
		BronEduArteModel model = new BronEduArteModel();

		setGevolgdeOpleiding(verbintenis.getExterneCode());
		setIntensiteit(model.getIntensiteit(verbintenis));

		// Leerweg moet leeg zijn als Intensiteit = “EX”
		if (getIntensiteit() == Intensiteit.Examendeelnemer)
			setLeerweg(null);
		else
			setLeerweg(model.getLeerweg(verbintenis));

		setDatumInschrijving(verbintenis.getBegindatum());
		setGeplandeDatumUitschrijving(verbintenis.getGeplandeEinddatum());
		setWerkelijkeDatumUitschrijving(verbintenis.getEinddatum());

		setHoogsteVooropleiding(model.getHoogsteVooropleiding(verbintenis));
		setIndicatieRisicodeelnemer(null);
		setIndicatieGehandicapt(verbintenis.getIndicatieGehandicapt());

		// Uit het PvE 9.3: 'dit veld blijft voorlopig leeg, vooruitlopend op een
		// wetswijziging.'
		// melding.setLaatsteVooropleiding(model.getLaatsteVooropleiding(deelnemer));

		setLocatie(model.getLocatie(verbintenis));

		// doet *nog* niets, aangezien de benodigde MBO tabel nog niet gevonden is. Zie
		// ook protocol versie 9.9.9 van BRON waarin staat:
		/*
		 * Het 320-record kan tijdelijk in twee formaten worden aangeleverd, _een_met
		 * reden uitstroom en _een_ zonder. De code van de reden uitstroom conform de
		 * tabel zoals bepaald door de MBO raad.
		 */
		// ZIE OOK mantis 44715
		// melding.setRedenUitstroom(model.getRedenUitstroom(verbintenis));
	}

	public static BpvGegevensRecord newBoBpvgegevensInschrijving(
			BronAanleverMelding aanleverMelding, BPVInschrijving bpvinschrijving)
	{
		return new BronBveAanleverRecord(BVE_AANLEVERING_BO_BPVGEGEVENS, aanleverMelding,
			bpvinschrijving);
	}

	public void vulBoBpvgegevensInschrijving()
	{
		BPVInschrijving bpv = getBpvInschrijving();
		BronEduArteModel model = new BronEduArteModel();

		setAfsluitdatumBpv(bpv.getAfsluitdatum());
		setDatumBeginBpv(bpv.getBegindatum());

		String ocwCode = model.getLeerbedrijf(bpv);
		setLeerbedrijf(ocwCode);
		if (StringUtil.isEmpty(ocwCode))
		{
			ExterneOrganisatie bpvBedrijf = bpv.getBpvBedrijf();
			setNaamLeerbedrijf(bpvBedrijf.getNaam());
			if (bpvBedrijf.getFysiekAdres() != null)
			{
				Adres adres = bpvBedrijf.getFysiekAdres().getAdres();
				setPostcodeLeerbedrijf(model.getPostcode(adres));
				if (model.getHuisnummer(adres) != null)
					setHuisnummerLeerbedrijf(model.getHuisnummer(adres));
			}
			setBrinCodeKbb(model.getBrinCodeKbb(bpv));
			setCreboCodeBpv(bpv.getVerbintenis().getExterneCode());
		}
		setOmvangBpv(bpv.getTotaleOmvang());
		setGeplandeDatumEindeBpv(bpv.getVerwachteEinddatum());
		setWerkelijkeDatumEindeBpv(bpv.getEinddatum());
	}

	public static ExamengegevensRecord newBoExamengegevens(BronAanleverMelding aanlevermelding,
			Verbintenis verbintenis, Examendeelname deelname)
	{
		return new BronBveAanleverRecord(BVE_AANLEVERING_BO_EXAMENGEGEVENS, aanlevermelding,
			verbintenis, deelname);
	}

	/**
	 * Deelkwalificatie, is gekoppeld aan {@link OnderwijsproductAfnameContext}
	 */
	public static ExamengegevensRecord newBoExamengegevensDK(BronAanleverMelding aanlevermelding,
			Verbintenis verbintenis, Examendeelname deelname,
			OnderwijsproductAfnameContext afnameContext)
	{
		return new BronBveAanleverRecord(BVE_AANLEVERING_BO_EXAMENGEGEVENS, aanlevermelding,
			verbintenis, deelname, afnameContext);
	}

	public void vulBoExamengegevens(boolean voegNieuweMeldingenToe)
	{
		Examendeelname deelname = getExamenDeelname();
		setDatumBehaald(deelname.getDatumUitslag());
		setBehaaldeDeelKwalificatie(verbintenis.getExterneCode());
		setIndicatieBekostigingDiploma(deelname.isBekostigd());

		/**
		 * Als LNV -> Deelkwalificaties naar BRON.
		 */
		if (voegNieuweMeldingenToe && verbintenis.isLNV())
		{
			vulBehaaldeDeelkwalificaties(getMelding(), deelname);
		}
	}

	public static void vulBehaaldeDeelkwalificaties(BronAanleverMelding melding,
			Examendeelname deelname)
	{
		List<OnderwijsproductAfnameContext> behaaldeDeelkwalificaties =
			deelname.getVerbintenis().getBehaaldeDeelkwalificaties();
		for (OnderwijsproductAfnameContext context : behaaldeDeelkwalificaties)
		{
			ExamengegevensRecord dkRecord =
				findOrNewExamengegevensRecord(melding, deelname, context
					.getOnderwijsproductAfname().getOnderwijsproduct().getExterneCode(), context);
			if (dkRecord.getSoortMutatie() == null)
			{
				if (context.getBronStatus() != null && context.getBronStatus().isGemeldAanBron())
					dkRecord.setSoortMutatie(SoortMutatie.Aanpassing);
				else
					dkRecord.setSoortMutatie(SoortMutatie.Toevoeging);
			}
			dkRecord.setDatumBehaald(deelname.getDatumUitslag());
			dkRecord.setBehaaldeDeelKwalificatie(context.getOnderwijsproductAfname()
				.getOnderwijsproduct().getExterneCode());
			dkRecord.setIndicatieBekostigingDiploma(false);
		}
	}

	private static ExamengegevensRecord findOrNewExamengegevensRecord(BronAanleverMelding melding,
			Examendeelname examendeelname, String behaaldeKwalificatie,
			OnderwijsproductAfnameContext context)
	{
		ExamengegevensRecord record = melding.getExamengegevensRecord(behaaldeKwalificatie);
		if (record != null)
			return record;
		return BronBveAanleverRecord.newBoExamengegevensDK(melding,
			examendeelname.getVerbintenis(), examendeelname, context);
	}

	public static PeriodegegevensInschrijvingRecord newBoPeriodegegevens(
			BronAanleverMelding aanlevermelding, Verbintenis verbintenis,
			Bekostigingsperiode periode)
	{
		return new BronBveAanleverRecord(BVE_AANLEVERING_BO_PERIODEGEGEVENS, aanlevermelding,
			verbintenis, periode);
	}

	public void vulBoPeriodegegevens()
	{
		if (getBekostigingsperiode() == null)
		{
			// probeer de bekostigingsperiode te vinden op basis van de aangegeven datum,
			// aangzien dit mogelijk niet goed geconverteerd wordt.
			for (Bekostigingsperiode periode : verbintenis.getBekostigingsperiodes())
			{
				if (periode.getBegindatum().equals(getDatumIngangPeriodegegevensInschrijving()))
				{
					bekostigingsperiode = periode;
					break;
				}
			}
		}
		if (getBekostigingsperiode() == null)
		{
			setDatumIngangPeriodegegevensInschrijving(verbintenis.getBegindatum());

			// Exameninschrijvingen komen niet voor bekostiging in aanmerking [ 839 ]
			if (verbintenis.getIntensiteit() == Intensiteit.Examendeelnemer)
				setIndicatieBekostigingInschrijving(false);
			else
				setIndicatieBekostigingInschrijving(verbintenis.getBekostigd() == Bekostigd.Ja);
		}
		else
		{
			setDatumIngangPeriodegegevensInschrijving(getBekostigingsperiode().getBegindatum());
			setIndicatieBekostigingInschrijving(getBekostigingsperiode().isBekostigd());
		}
		setIndicatieLesgeld(null);
	}

	public static nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.vavo.ExamengegevensRecord newVavoExamengegevensRecord(
			BronAanleverMelding melding, Examendeelname deelname)
	{
		return new BronBveAanleverRecord(BVE_AANLEVERING_VAVO_EXAMENGEGEVENS, melding, deelname
			.getVerbintenis(), deelname);
	}

	public static VakgegevensRecord newVavoVakgegevensRecord(BronAanleverMelding melding,
			Verbintenis verbintenis, Examendeelname deelname, OnderwijsproductAfnameContext context)
	{
		return new BronBveAanleverRecord(BVE_AANLEVERING_VAVO_VAKGEGEVENS, melding, verbintenis,
			deelname, context);
	}

	public static nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.vavo.InschrijvingsgegevensRecord newVavoInschrijvingsgegevensRecord(
			BronAanleverMelding melding, Verbintenis verbintenis)
	{
		return new BronBveAanleverRecord(BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS, melding,
			verbintenis);
	}

	@SuppressWarnings("hiding")
	public void vulVavoInschrijvingsgegevensRecord()
	{
		Verbintenis verbintenis = getVerbintenis();
		Deelnemer deelnemer = getDeelnemer();
		BronEduArteModel model = new BronEduArteModel();

		if (verbintenis.getContacturenPerWeek() != null)
			setContacturenPerWeek(verbintenis.getContacturenPerWeek().setScale(0,
				BigDecimal.ROUND_HALF_UP).intValue());
		setDatumInschrijving(verbintenis.getBegindatum());
		setWerkelijkeDatumUitschrijving(verbintenis.getEinddatum());
		setGeplandeDatumUitschrijving(verbintenis.getGeplandeEinddatum());
		setGevolgdeOpleiding(model.getOpleidingscode(verbintenis));
		setHoogsteVooropleiding(model.getHoogsteVooropleiding(verbintenis));
		setIndicatieNieuwkomer(model.getIndicatieNieuwkomer(deelnemer));
	}

	public static nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.InschrijvingsgegevensRecord newEdInschrijvingsgegevensRecord(
			BronAanleverMelding aanleverMelding, Verbintenis verbintenis)
	{
		return new BronBveAanleverRecord(BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS, aanleverMelding,
			verbintenis);
	}

	@SuppressWarnings("hiding")
	public void vulEdInschrijvingsgegevensRecord()
	{
		Verbintenis verbintenis = getVerbintenis();
		Deelnemer deelnemer = getDeelnemer();
		BronEduArteModel model = new BronEduArteModel();

		setGevolgdeOpleiding(verbintenis.getExterneCode());
		setDatumInschrijving(verbintenis.getBegindatum());
		setGeplandeDatumUitschrijving(verbintenis.getGeplandeEinddatum());
		setWerkelijkeDatumUitschrijving(verbintenis.getEinddatum());
		setHoogsteVooropleiding(model.getHoogsteVooropleiding(verbintenis));
		setIndicatieNieuwkomer(deelnemer.getPersoon().isNieuwkomer());
		setContacturenPerWeek(verbintenis.getContacturenPerWeek().setScale(0, RoundingMode.HALF_UP)
			.intValue());
	}

	public static NT2Vaardigheden newEdNt2Vaardigheden(BronAanleverMelding aanlevermelding,
			Verbintenis verbintenis)
	{
		return new BronBveAanleverRecord(BVE_AANLEVERING_ED_NT2VAARDIGHEDEN, aanlevermelding,
			verbintenis);
	}

	public static ResultaatgegevensRecord newEdResultaatgegevensRecord(
			BronAanleverMelding aanlevermelding, Verbintenis verbintenis, Examendeelname deelname)
	{
		return new BronBveAanleverRecord(BVE_AANLEVERING_ED_RESULTAATGEGEVENS, aanlevermelding,
			verbintenis, deelname);
	}

	public void vulEdResultaatgegevensRecord()
	{
		setVoltooideOpleiding(getExamenDeelname().getVerbintenis().getExterneCode());
		setDatumVoltooid(getExamenDeelname().getDatumUitslag());
	}

	public static nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.VakgegevensRecord newEdVakgegevensRecord(
			BronAanleverMelding aanleverMelding, Verbintenis verbintenis,
			OnderwijsproductAfnameContext context)
	{
		return new BronBveAanleverRecord(BVE_AANLEVERING_ED_VAKGEGEVENS, aanleverMelding,
			verbintenis, context);
	}

	public void vulEdVakgegevensRecord()
	{
		OnderwijsproductAfnameContext context = getAfnameContext();
		if (context != null)
		{
			Onderwijsproduct onderwijsproduct =
				context.getOnderwijsproductAfname().getOnderwijsproduct();
			context.bepaalVolgnummer();
			setVak(onderwijsproduct.getExterneCode());
			setVakvolgnummer(context.getVolgnummer());
			if (context.getOnderwijsproductAfname().getEinddatum() != null)
				setDatumVoltooid(context.getOnderwijsproductAfname().getEinddatum());
		}
	}

	public static WijzigingSleutelgegevensRecord newWijzigingSleutelgegevensRecord(
			BronAanleverMelding aanleverMelding)
	{
		return new BronBveAanleverRecord(BVE_AANLEVERING_WIJZIGING_SLEUTELGEGEVENS, aanleverMelding);
	}

	@Override
	public ToepassingResultaat getToepassingResultaatOfBeoordelingExamenVak()
	{
		if (getToepassingResultaatExamenvak() != null)
		{
			switch (getToepassingResultaatExamenvak())
			{
				case Dispensatie:
					return ToepassingResultaat.Dispensatie;
				case GeexamineerdInJaarMelding:
					return ToepassingResultaat.GeexamineerdInJaarVanMelding;
				case InruilCertificaatVoorDiploma:
					return ToepassingResultaat.InruilCertificaatVoorDiploma;
				case Vrijstelling:
					return ToepassingResultaat.Vrijstelling;
			}
		}
		return null;
	}

	public void setRecordNummer(int i)
	{
		this.recordNummer = i;
	}

	public String getIdentifier()
	{
		if (identifier == null)
		{
			identifier = generateIdentifier();
		}
		return identifier;
	}

	private String generateIdentifier()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		switch (recordType)
		{
			case BVE_AANLEVERING_WIJZIGING_SLEUTELGEGEVENS:
				return String.valueOf(recordType);

			case BVE_AANLEVERING_PERSOONSGEGEVENS:
				return String.valueOf(recordType);

			case BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS:
				return String.valueOf(recordType) + getInschrijvingsvolgnummer();
			case BVE_AANLEVERING_BO_PERIODEGEGEVENS:
				return String.valueOf(recordType) + getInschrijvingsvolgnummer()
					+ sdf.format(getDatumIngangPeriodegegevensInschrijving());
			case BVE_AANLEVERING_BO_BPVGEGEVENS:
				return String.valueOf(recordType) + getInschrijvingsvolgnummer()
					+ getBpvVolgnummer();
			case BVE_AANLEVERING_BO_EXAMENGEGEVENS:
				return String.valueOf(recordType) + getInschrijvingsvolgnummer()
					+ getBehaaldeDeelKwalificatie();

			case BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS:
				return String.valueOf(recordType) + getInschrijvingsvolgnummer();
			case BVE_AANLEVERING_ED_RESULTAATGEGEVENS:
				return String.valueOf(recordType) + getInschrijvingsvolgnummer()
					+ getVoltooideOpleiding();
			case BVE_AANLEVERING_ED_VAKGEGEVENS:
				return String.valueOf(recordType) + getInschrijvingsvolgnummer()
					+ getVakvolgnummer();
			case BVE_AANLEVERING_ED_NT2VAARDIGHEDEN:
				return String.valueOf(recordType) + getInschrijvingsvolgnummer()
					+ getVakvolgnummer() + getNT2Vaardigheid();

			case BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS:
				return String.valueOf(recordType) + getInschrijvingsvolgnummer();
			case BVE_AANLEVERING_VAVO_VAKGEGEVENS:
				return String.valueOf(recordType) + getInschrijvingsvolgnummer() + getExamen()
					+ getExamenjaar();
			case BVE_AANLEVERING_VAVO_EXAMENGEGEVENS:
				return String.valueOf(recordType) + getInschrijvingsvolgnummer() + getExamen()
					+ getExamenvak();
		}
		throw new IllegalStateException(recordType + " is een onbekend BRON recordtype");
	}

	public boolean isSleutelwijzigingGevuld()
	{
		return JavaUtil.areNotNullOrEmpty(getSofinummerAchterhaald(), getGeboortedatumGewijzigd(),
			getGeslachtGewijzigd(), getPostcodeVolgensInstellingGewijzigd(), getLandGewijzigd());
	}

	public boolean isInschrijvingsrecord()
	{
		boolean isInschrijvingsrecord =
			recordType == BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS
				|| recordType == BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS
				|| recordType == BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS;
		return isInschrijvingsrecord;
	}

	@Override
	public String toString()
	{
		return String.format("%d%c %s (%s)", recordType, soortMutatie.getIdentifier(),
			getIdentifier(), super.toString());
	}
}
