package nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling;

import java.util.Date;

import javax.persistence.*;

import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.*;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.HuisnummerAanduiding;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.BeoordelingSchoolExamen;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.BeoordelingWerkstuk;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.HogerNiveau;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.Ernst;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "BRON_BVE_TERUGKOPPELRECORDS")
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER)
public class BronBveTerugkoppelRecord extends InstellingEntiteit
		implements
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.TerugkoppelRecord,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.GbaGegevens,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.Persoonsgegevens,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.WijzigingSleutelgegevens,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.Signaal,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.bo.InschrijvingsgegevensRecord,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.bo.BpvGegevensRecord,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.bo.ExamengegevensRecord,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.bo.PeriodegegevensInschrijvingRecord,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.ed.InschrijvingsgegevensRecord,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.ed.NT2Vaardigheden,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.ed.ResultaatgegevensRecord,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.ed.VakgegevensRecord,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.vavo.InschrijvingsgegevensRecord,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.vavo.ExamengegevensRecord,
		nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.vavo.VakgegevensRecord,
		IBronSignaal
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "melding")
	@Index(name = "IDX_BRON_BVE_TER_MELDING")
	@AutoFormEmbedded
	private BronBveTerugkoppelMelding melding;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "aanleverrecord")
	@Index(name = "idx_BRON_BVE_TER_aanleverreco")
	private BronBveAanleverRecord aanleverrecord;

	@Column(nullable = false, insertable = true, updatable = false, name = "type")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	@AutoForm(include = false)
	private int recordType;

	@Column(name = "batchnummer", nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Integer batchnummerAanleverbestand;

	@Column(name = "meldingnummer", nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Integer meldingnummer;

	@Column(nullable = true)
	@Type(type = "nl.topicus.eduarte.krd.hibernate.usertypes.DatumUsertype")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Datum datumEindeVerblijfstitel;

	@Column(nullable = true)
	@Type(type = "nl.topicus.eduarte.krd.hibernate.usertypes.DatumUsertype")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Datum datumIngangAdres;

	@Column(nullable = true)
	@Type(type = "nl.topicus.eduarte.krd.hibernate.usertypes.DatumUsertype")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Datum datumIngangNationaliteit1;

	@Column(nullable = true)
	@Type(type = "nl.topicus.eduarte.krd.hibernate.usertypes.DatumUsertype")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Datum datumIngangNationaliteit2;

	@Column(nullable = true)
	@Type(type = "nl.topicus.eduarte.krd.hibernate.usertypes.DatumUsertype")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Datum datumIngangVerblijfstitel;

	@Column(nullable = true)
	@Type(type = "nl.topicus.eduarte.krd.hibernate.usertypes.DatumUsertype")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Datum datumOverlijden;

	@Column(nullable = true)
	@Type(type = "nl.topicus.eduarte.krd.hibernate.usertypes.DatumUsertype")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Datum datumVestigingInNederland;

	@Column(length = 4, nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String geboorteland;

	@Column(length = 4, nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String geboortelandOuder1;

	@Column(length = 4, nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String geboortelandOuder2;

	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Geslacht geslachtOuder1;

	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Geslacht geslachtOuder2;

	@Column(length = 4, nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String landWaarnaarVertrokken;

	@Column(length = 4, nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String nationaliteit1;

	@Column(length = 4, nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String nationaliteit2;

	@Column(length = 6, nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String postcodeVolgensGba;

	@Column(length = 2, nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String verblijfstitel;

	@Column(nullable = true)
	@Type(type = "nl.topicus.eduarte.krd.hibernate.usertypes.DatumUsertype")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Datum vertrekUitNederland;

	@Column(nullable = true, length = 200)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String achternaam;

	@Column(nullable = true, length = 35)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String adresregelBuitenland1;

	@Column(nullable = true, length = 35)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String adresregelBuitenland2;

	@Column(nullable = true, length = 35)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String adresregelBuitenland3;

	@Column(nullable = true, length = 200)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String alleVoornamen;

	@Column(nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Integer huisnummer;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private HuisnummerAanduiding huisnummerAanduiding;

	@Column(nullable = true, length = 5)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String huisnummerToevoeging;

	@Column(nullable = true, length = 35)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String locatieOmschrijving;

	@Column(nullable = true, length = 24)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String plaatsnaam;

	@Column(nullable = true, length = 24)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String straatnaam;

	@Column(nullable = true, length = 10)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String voorvoegsel;

	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Date datumIngangAdreswijziging;

	@Column(nullable = true)
	@Type(type = "nl.topicus.eduarte.krd.hibernate.usertypes.DatumUsertype")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Datum geboortedatumGewijzigd;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Geslacht geslachtGewijzigd;

	@Column(nullable = true, length = 4)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String landGewijzigd;

	@Column(nullable = true, length = 6, name = "postcodeGewijzigd")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String postcodeVolgensInstellingGewijzigd;

	@Column(nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Integer sofinummerAchterhaald;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Ernst ernst;

	@Column(nullable = true, length = 150)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String omschrijvingSignaal;

	@Column(nullable = true, length = 20)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String recordidentificatie;

	@Column(nullable = true, length = 3)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private int recordsoortTerugkoppeling;

	@Column(nullable = true, length = 3)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Integer signaalcode;

	@Temporal(TemporalType.DATE)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Date datumInschrijving;

	@Temporal(TemporalType.DATE)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Date geplandeDatumUitschrijving;

	@Column(nullable = true, length = 5)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String gevolgdeOpleiding;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private HoogsteVooropleiding hoogsteVooropleiding;

	@Column(nullable = true, name = "gehandicapt")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Boolean indicatieGehandicapt;

	@Column(nullable = true, name = "risicodeelnemer")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Boolean indicatieRisicodeelnemer;

	@Column(nullable = true, length = 3)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String inschrijvingsvolgnummer;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Intensiteit intensiteit;

	@Column(nullable = true, length = 5)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String laatsteVooropleiding;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Leerweg leerweg;

	@Column(nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Integer locatie;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private SoortMutatie soortMutatie;

	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Date werkelijkeDatumUitschrijving;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private RedenUitval redenUitstroom;

	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Date afsluitdatumBpv;

	@Column(nullable = true, length = 2)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Integer bpvVolgnummer;

	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Date datumBeginBpv;

	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Date geplandeDatumEindeBpv;

	@Column(nullable = true, length = 40)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String leerbedrijf;

	@Column(nullable = true, length = 4)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Integer omvangBpv;

	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Date werkelijkeDatumEindeBpv;

	@Column(nullable = true, length = 5)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String behaaldeDeelKwalificatie;

	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Date datumBehaald;

	@Column(nullable = true, name = "bekostigingDiploma")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Boolean indicatieBekostigingDiploma;

	@Temporal(TemporalType.DATE)
	@Column(nullable = true, name = "datumIngangPeriodegegevens")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Date datumIngangPeriodegegevensInschrijving;

	@Column(name = "bekostigingInschrijving")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Boolean indicatieBekostigingInschrijving;

	@Column(length = 1, name = "lesgeld")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String indicatieLesgeld;

	@Column(length = 2, nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Integer contacturenPerWeek;

	@Column(name = "nieuwkomer", nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Boolean indicatieNieuwkomer;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private NT2Niveau behaaldNiveau;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private NT2Vaardigheid nT2Vaardigheid;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private NT2Niveau startniveau;

	@Column(nullable = true, length = 2)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Integer vakvolgnummer;

	@Temporal(TemporalType.DATE)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Date datumVoltooid;

	@Column(nullable = true, length = 5)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String voltooideOpleiding;

	@Column(nullable = true, length = 4)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String vak;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private BeoordelingWerkstuk beoordelingWerkstuk;

	@Column(nullable = true, length = 3)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String cijferWerkstuk;

	@Temporal(TemporalType.DATE)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Date datumUitslagExamen;

	@Column(length = 4, nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String examen;

	@Column(length = 4, nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Integer examenjaar;

	@Column(length = 150, nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String titelThemaWerkstuk;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private ToepassingBeoordelingWerkstuk toepassingBeoordelingWerkstuk;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private UitslagExamen uitslagExamen;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private BeoordelingSchoolExamen beoordelingSchoolexamen;

	@Column(length = 3, nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Integer cijferCE1;

	@Column(length = 3, nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Integer cijferCE2;

	@Column(length = 3, nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Integer cijferCE3;

	@Column(length = 2, nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Integer cijferCijferlijst;

	@Column(length = 3, nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Integer cijferSchoolexamen;

	@Column(length = 2, nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Integer derdeEindcijfer;

	@Column(length = 2, nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Integer eersteEindcijfer;

	@Column(length = 4, nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String examenvak;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private HogerNiveau hogerNiveau;

	@Column(nullable = true, name = "certificaatBehaald")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Boolean indicatieCertificaatBehaald;

	@Column(nullable = true, name = "combinatiecijfer")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Boolean indicatieCombinatiecijfer;

	@Column(nullable = true, name = "diplomavak")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Boolean indicatieDiplomavak;

	@Column(nullable = true, name = "naarVolgendTijdvak")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Boolean indicatieVerwezenNaarVolgendTijdvak;

	@Column(nullable = true, name = "werkstuk")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Boolean indicatieWerkstuk;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private ToepassingResultaatExamenvak toepassingResultaatExamenvak;

	@Column(length = 2, nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Integer tweedeEindcijfer;

	@Column(length = 4, nullable = true)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Integer vakcodeHogerNiveau;

	@Column(nullable = true)
	private Boolean geaccordeerd;

	@Lob
	@Column(nullable = true)
	private String opmerking;

	private String postcodeLeerbedrijf;

	private String naamLeerbedrijf;

	private Integer huisnummerLeerbedrijf;

	private String creboCodeBpv;

	private String brinCodeKbb;

	public BronBveTerugkoppelMelding getMelding()
	{
		return melding;
	}

	public void setMelding(BronBveTerugkoppelMelding melding)
	{
		this.melding = melding;
	}

	@Override
	public Integer getBatchnummerAanleverbestand()
	{
		return batchnummerAanleverbestand;
	}

	@Override
	public int getRecordType()
	{
		return recordType;
	}

	public BronBveAanleverRecord getAanleverrecord()
	{
		return aanleverrecord;
	}

	public void setAanleverrecord(BronBveAanleverRecord aanleverrecord)
	{
		this.aanleverrecord = aanleverrecord;
	}

	@Override
	public Integer getMeldingnummer()
	{
		return meldingnummer;
	}

	@Override
	public Datum getDatumEindeVerblijfstitel()
	{
		return datumEindeVerblijfstitel;
	}

	@Override
	public Datum getDatumIngangAdres()
	{
		return datumIngangAdres;
	}

	@Override
	public Datum getDatumIngangNationaliteit1()
	{
		return datumIngangNationaliteit1;
	}

	@Override
	public Datum getDatumIngangNationaliteit2()
	{
		return datumIngangNationaliteit2;
	}

	@Override
	public Datum getDatumIngangVerblijfstitel()
	{
		return datumIngangVerblijfstitel;
	}

	@Override
	public Datum getDatumOverlijden()
	{
		return datumOverlijden;
	}

	@Override
	public Datum getDatumVestigingInNederland()
	{
		return datumVestigingInNederland;
	}

	@Override
	public String getGeboorteland()
	{
		return geboorteland;
	}

	@Override
	public String getGeboortelandOuder1()
	{
		return geboortelandOuder1;
	}

	@Override
	public String getGeboortelandOuder2()
	{
		return geboortelandOuder2;
	}

	@Override
	public Geslacht getGeslachtOuder1()
	{
		return geslachtOuder1;
	}

	@Override
	public Geslacht getGeslachtOuder2()
	{
		return geslachtOuder2;
	}

	@Override
	public String getLandWaarnaarVertrokken()
	{
		return landWaarnaarVertrokken;
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
	public String getPostcodeVolgensGba()
	{
		return postcodeVolgensGba;
	}

	@Override
	public String getVerblijfstitel()
	{
		return verblijfstitel;
	}

	@Override
	public Datum getVertrekUitNederland()
	{
		return vertrekUitNederland;
	}

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

	@Override
	public Date getDatumIngangAdreswijziging()
	{
		return datumIngangAdreswijziging;
	}

	@Override
	public Datum getGeboortedatumGewijzigd()
	{
		return geboortedatumGewijzigd;
	}

	@Override
	public Geslacht getGeslachtGewijzigd()
	{
		return geslachtGewijzigd;
	}

	@Override
	public String getLandGewijzigd()
	{
		return landGewijzigd;
	}

	@Override
	public String getPostcodeVolgensInstellingGewijzigd()
	{
		return postcodeVolgensInstellingGewijzigd;
	}

	@Override
	public Integer getSofinummerAchterhaald()
	{
		return sofinummerAchterhaald;
	}

	@Override
	public Ernst getErnst()
	{
		return ernst;
	}

	@Override
	public String getOmschrijvingSignaal()
	{
		return omschrijvingSignaal;
	}

	@Override
	public String getRecordidentificatie()
	{
		return recordidentificatie;
	}

	@Override
	public int getRecordsoortTerugkoppeling()
	{
		return recordsoortTerugkoppeling;
	}

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
	public String getInschrijvingsvolgnummer()
	{
		return inschrijvingsvolgnummer;
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
	public SoortMutatie getSoortMutatie()
	{
		return soortMutatie;
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
	public Date getAfsluitdatumBpv()
	{
		return afsluitdatumBpv;
	}

	@Override
	public Integer getBpvVolgnummer()
	{
		return bpvVolgnummer;
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
	public String getLeerbedrijf()
	{
		return leerbedrijf;
	}

	@Override
	public Integer getOmvangBpv()
	{
		return omvangBpv;
	}

	@Override
	public Date getWerkelijkeDatumEindeBpv()
	{
		return werkelijkeDatumEindeBpv;
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

	@Override
	public Integer getContacturenPerWeek()
	{
		return contacturenPerWeek;
	}

	@Override
	public Boolean getIndicatieNieuwkomer()
	{
		return indicatieNieuwkomer;
	}

	@Override
	public NT2Niveau getBehaaldNiveau()
	{
		return behaaldNiveau;
	}

	@Override
	public NT2Vaardigheid getNT2Vaardigheid()
	{
		return nT2Vaardigheid;
	}

	@Override
	public NT2Niveau getStartniveau()
	{
		return startniveau;
	}

	@Override
	public Integer getVakvolgnummer()
	{
		return vakvolgnummer;
	}

	@Override
	public Date getDatumVoltooid()
	{
		return datumVoltooid;
	}

	@Override
	public String getVoltooideOpleiding()
	{
		return voltooideOpleiding;
	}

	@Override
	public String getVak()
	{
		return vak;
	}

	@Override
	public BeoordelingWerkstuk getBeoordelingWerkstuk()
	{
		return beoordelingWerkstuk;
	}

	@Override
	public String getCijferWerkstuk()
	{
		return cijferWerkstuk;
	}

	@Override
	public Date getDatumUitslagExamen()
	{
		return datumUitslagExamen;
	}

	@Override
	public String getExamen()
	{
		return examen;
	}

	@Override
	public Integer getExamenjaar()
	{
		return examenjaar;
	}

	@Override
	public String getTitelThemaWerkstuk()
	{
		return titelThemaWerkstuk;
	}

	@Override
	public ToepassingBeoordelingWerkstuk getToepassingBeoordelingWerkstuk()
	{
		return toepassingBeoordelingWerkstuk;
	}

	@Override
	public UitslagExamen getUitslagExamen()
	{
		return uitslagExamen;
	}

	@Override
	public BeoordelingSchoolExamen getBeoordelingSchoolexamen()
	{
		return beoordelingSchoolexamen;
	}

	/**
	 * Methode die nodig is vanwege het elke keer verschillend schrijven van Schoolexamen.
	 */
	public BeoordelingSchoolExamen getBeoordelingSchoolExamen()
	{
		return getBeoordelingSchoolexamen();
	}

	@Override
	public Integer getCijferCE1()
	{
		return cijferCE1;
	}

	@Override
	public Integer getCijferCE2()
	{
		return cijferCE2;
	}

	@Override
	public Integer getCijferCE3()
	{
		return cijferCE3;
	}

	@Override
	public Integer getCijferCijferlijst()
	{
		return cijferCijferlijst;
	}

	@Override
	public Integer getCijferSchoolexamen()
	{
		return cijferSchoolexamen;
	}

	/**
	 * Methode die nodig is vanwege het elke keer verschillend schrijven van Schoolexamen.
	 */
	public Integer getCijferSchoolExamen()
	{
		return getCijferSchoolexamen();
	}

	@Override
	public Integer getDerdeEindcijfer()
	{
		return derdeEindcijfer;
	}

	@Override
	public Integer getEersteEindcijfer()
	{
		return eersteEindcijfer;
	}

	@Override
	public String getExamenvak()
	{
		return examenvak;
	}

	@Override
	public HogerNiveau getHogerNiveau()
	{
		return hogerNiveau;
	}

	@Override
	public Boolean getIndicatieCertificaatBehaald()
	{
		return indicatieCertificaatBehaald;
	}

	public Boolean getCertificaat()
	{
		return getIndicatieCertificaatBehaald();
	}

	@Override
	public Boolean getIndicatieCombinatiecijfer()
	{
		return indicatieCombinatiecijfer;
	}

	public Boolean getIndicatieCombinatieCijfer()
	{
		return getIndicatieCombinatiecijfer();
	}

	@Override
	public Boolean getIndicatieDiplomavak()
	{
		return indicatieDiplomavak;
	}

	@Override
	public Boolean getIndicatieVerwezenNaarVolgendTijdvak()
	{
		return indicatieVerwezenNaarVolgendTijdvak;
	}

	public Boolean getVerwezenNaarVolgendeTijdvak()
	{
		return getIndicatieVerwezenNaarVolgendTijdvak();
	}

	@Override
	public Boolean getIndicatieWerkstuk()
	{
		return indicatieWerkstuk;
	}

	@Override
	public ToepassingResultaatExamenvak getToepassingResultaatExamenvak()
	{
		return toepassingResultaatExamenvak;
	}

	@Override
	public Integer getTweedeEindcijfer()
	{
		return tweedeEindcijfer;
	}

	@Override
	public Integer getVakcodeHogerNiveau()
	{
		return vakcodeHogerNiveau;
	}

	public Integer getVakCodeHogerNiveau()
	{
		return getVakcodeHogerNiveau();
	}

	public Integer getSignaalcode()
	{
		return signaalcode;
	}

	public void setGeaccordeerd(Boolean geaccordeerd)
	{
		this.geaccordeerd = geaccordeerd;
	}

	public Boolean getGeaccordeerd()
	{
		return geaccordeerd;
	}

	public void setOpmerking(String opmerking)
	{
		this.opmerking = opmerking;
	}

	public String getOpmerking()
	{
		return opmerking;
	}

	@Override
	public IBronMelding getAanleverMelding()
	{
		BronBveTerugkoppelMelding terugkMelding = getMelding();
		if (terugkMelding == null)
			return null;
		return terugkMelding.getAanlevermelding();
	}

	@Override
	public IBronTerugkMelding getTerugkMelding()
	{
		return getMelding();
	}

	@Override
	public String getBrinCodeKbb()
	{
		return brinCodeKbb;
	}

	public void setBrinCodeKbb(String brinCodeKbb)
	{
		this.brinCodeKbb = brinCodeKbb;
	}

	@Override
	public String getCreboCodeBpv()
	{
		return creboCodeBpv;
	}

	public void setCreboCodeBpv(String creboCodeBpv)
	{
		this.creboCodeBpv = creboCodeBpv;
	}

	@Override
	public Integer getHuisnummerLeerbedrijf()
	{
		return huisnummerLeerbedrijf;
	}

	public void setHuisnummerLeerbedrijf(Integer huisnummerLeerbedrijf)
	{
		this.huisnummerLeerbedrijf = huisnummerLeerbedrijf;
	}

	@Override
	public String getNaamLeerbedrijf()
	{
		return naamLeerbedrijf;
	}

	public void setNaamLeerbedrijf(String naamLeerbedrijf)
	{
		this.naamLeerbedrijf = naamLeerbedrijf;
	}

	@Override
	public String getPostcodeLeerbedrijf()
	{
		return postcodeLeerbedrijf;
	}

	public void setPostcodeLeerbedrijf(String postcodeLeerbedrijf)
	{
		this.postcodeLeerbedrijf = postcodeLeerbedrijf;
	}
}
