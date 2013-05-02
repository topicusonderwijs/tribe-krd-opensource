/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.personen;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.util.StringUtil.StringConverter;
import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.cobra.web.components.text.ReadonlyTextField;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.security.checks.DeelnemerSecurityCheck;
import nl.topicus.eduarte.core.principals.deelnemer.verbintenis.DeelnemerVerbintenisDocumentInzien;
import nl.topicus.eduarte.dao.helpers.AccountDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.DBSMedewerkerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.InschrijvingsverzoekDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.MedewerkerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.NummerGeneratorDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.dbs.IncidentDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.dbs.NotitieDataAccessHelper;
import nl.topicus.eduarte.entities.BronEntiteitStatus;
import nl.topicus.eduarte.entities.IBronStatusEntiteit;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.DocumentCategorie;
import nl.topicus.eduarte.entities.bijlage.DocumentType;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.dbs.gedrag.Incident;
import nl.topicus.eduarte.entities.dbs.gedrag.Notitie;
import nl.topicus.eduarte.entities.dbs.trajecten.Traject;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.groep.Groepstype;
import nl.topicus.eduarte.entities.hogeronderwijs.Inschrijvingsverzoek;
import nl.topicus.eduarte.entities.hogeronderwijs.StudielinkBericht;
import nl.topicus.eduarte.entities.inschrijving.IVooropleiding;
import nl.topicus.eduarte.entities.inschrijving.Intakegesprek;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding.SoortOnderwijs;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.kenmerk.DeelnemerKenmerk;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.landelijk.Nationaliteit;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelbaarEntiteit;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateRecht;
import nl.topicus.eduarte.entities.security.authentication.DeelnemerAccount;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.providers.PersoonProvider;
import nl.topicus.eduarte.zoekfilters.InschrijvingsverzoekZoekFilter;
import nl.topicus.eduarte.zoekfilters.dbs.IncidentZoekFilter;
import nl.topicus.eduarte.zoekfilters.dbs.NotitieZoekFilter;
import nl.topicus.onderwijs.duo.bron.Bron;
import nl.topicus.onderwijs.duo.criho.annot.Criho;

import org.apache.wicket.Application;
import org.apache.wicket.security.WaspApplication;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Deelnemer (leerling). Verwijst naar Persoon waar bijvoorbeeld naam en sofinummer is
 * opgeslagen.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"organisatie",
	"deelnemernummer"})})
@Exportable
@BatchSize(size = 20)
@IsViewWhenOnNoise
public class Deelnemer extends InstellingEntiteit implements DeelnemerProvider, PersoonProvider,
		Comparable<Deelnemer>, IContextInfoObject, IBijlageKoppelEntiteit<DeelnemerBijlage>,
		IOrganisatieEenheidLocatieKoppelbaarEntiteit<Verbintenis>, IBronStatusEntiteit
{
	private static final long serialVersionUID = 1L;

	@BatchSize(size = 20)
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "persoon", nullable = false)
	@Index(name = "idx_Deelnemer_persoon")
	@AutoFormEmbedded
	@Bron
	private Persoon persoon;

	@Column(nullable = true)
	private boolean allochtoon;

	/**
	 * Dit is het 'ov-nummer' of 'deelnemernummer' zoals dit bekend is bij medewerkers van
	 * scholen. Dit is dus waaraan een deelnemer uniek geidentificeerd wordt binnen een
	 * school.
	 */
	@Bron(sleutel = true)
	@Column(nullable = false)
	@Index(name = "idx_Deelnemer_deelnemernummer")
	@AutoForm(label = "Nummer", editorClass = ReadonlyTextField.class)
	private int deelnemernummer;

	@Column(nullable = true)
	@Index(name = "idx_Deelnemer_onderwijsnummer")
	@AutoForm(htmlClasses = "unit_max")
	@Bron(sleutel = true)
	private Long onderwijsnummer;

	/**
	 * LGF = Leerlinggebonden Financiering. Wordt niet aangevraagd via KRD / BRON, maar
	 * bij de CVI (indicatiestellingsorgaan). Scholen willen wel graag weten dat sprake is
	 * van LGF omdat ze dan geld hebben om de deelnemer extra te begeleiden.
	 */
	@Column(nullable = false)
	@AutoForm(label = "LGF", description = "Leerling Gebonden Financiering geeft aan of "
		+ "de Commissie voor Indicatiestelling extra middelen heeft toegekend voor "
		+ "ondersteuning en begeleiding van de deelnemer met een bepaalde handicap of "
		+ "beperking. Ook bekend als het \"rugzakje\".")
	private boolean lgf;

	/**
	 * Lijst met alle inschrijvingen voor deze deelnemer.
	 */
	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "deelnemer")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@OrderBy("begindatum DESC, volgnummer DESC, einddatum DESC, geplandeEinddatum DESC")
	@Bron
	private List<Verbintenis> verbintenissen = new ArrayList<Verbintenis>();

	/**
	 * Unordered, alleen gebruiken voor joins
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "deelnemer")
	private List<Verbintenis> verbintenissenUnordered;

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "deelnemer")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@OrderBy("begindatum DESC, einddatum DESC")
	private List<Groepsdeelname> groepsdeelnames;

	/**
	 * Unordered, alleen gebruiken voor joins
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "deelnemer")
	private List<Groepsdeelname> groepsdeelnamesUnordered;

	/**
	 * De bijlages van deze deelnemer
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "deelnemer")
	private List<DeelnemerBijlage> bijlagen;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "deelnemer")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@Bron
	private List<Vooropleiding> vooropleidingen = new ArrayList<Vooropleiding>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "deelnemer")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<OnderwijsproductAfname> onderwijsproductAfnames =
		new ArrayList<OnderwijsproductAfname>();

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@AutoForm(label = "BRON-status")
	private BronEntiteitStatus bronStatus = BronEntiteitStatus.Geen;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = true)
	@AutoForm(label = "BRON-datum")
	private Date bronDatum;

	/**
	 * Zet deze waarde op true om te voorkomen dat er een BRON sleutelwijziging melding
	 * aangemaakt wordt voor deze deelnemer.
	 */
	@Transient
	@AutoForm(editorClass = JaNeeCombobox.class)
	private boolean negeerSleutelWijzigingenBron = false;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "deelnemer")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<DeelnemerKenmerk> kenmerken = new ArrayList<DeelnemerKenmerk>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "deelnemer")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<Traject> trajecten = new ArrayList<Traject>();

	@Temporal(value = TemporalType.DATE)
	private Date registratieDatum;

	@Column(nullable = true)
	@Temporal(value = TemporalType.DATE)
	private Date startkwalificatieplichtigTot;

	@AutoForm(description = "Zorgt dat de deelnemer geen facturen meer krijgt, totdat dit veld is uitgeschakeld.")
	private boolean uitsluitenVanFacturatie = false;

	@Criho
	@Column(nullable = true)
	@Index(name = "idx_Deelnemer_Ocwnummer")
	private Long ocwnummer;

	@Column(nullable = true)
	@Index(name = "idx_Deelnemer_Studielinknummer")
	private Integer studielinknummer;

	@Column(nullable = true)
	private Boolean gbaRelatie;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "deelnemer")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	private List<StudielinkBericht> studielinkBerichten = new ArrayList<StudielinkBericht>();

	@Column(nullable = true)
	private Boolean daPersGegegensConflict;

	@Column(nullable = true)
	private Boolean heeftBachelorgraad;

	@Column(nullable = true)
	private Boolean heeftMastergraad;

	public Deelnemer()
	{
	}

	@Override
	@Exportable
	public Persoon getPersoon()
	{
		return persoon;
	}

	public void setPersoon(Persoon persoon)
	{
		this.persoon = persoon;
	}

	@Exportable
	public Long getOnderwijsnummer()
	{
		return onderwijsnummer;
	}

	public void setOnderwijsnummer(Long onderwijsnummer)
	{
		this.onderwijsnummer = onderwijsnummer;
	}

	public void setLgf(boolean lgf)
	{
		this.lgf = lgf;
	}

	@Exportable
	public boolean getLgf()
	{
		return lgf;
	}

	@AutoForm(label = "LGF")
	public String getLgfOmschrijving()
	{
		return getLgf() ? "Ja" : "Nee";
	}

	public void setAllochtoon(boolean allochtoon)
	{
		this.allochtoon = allochtoon;
	}

	@Exportable
	public boolean isAllochtoon()
	{
		return allochtoon;
	}

	/**
	 * @return Ja indien allochtoon, en anders Nee.
	 */
	@AutoForm(label = "Allochtoon", include = true)
	@Exportable
	public String getAllochtoonOmschrijving()
	{
		return isAllochtoon() ? "Ja" : "Nee";
	}

	/**
	 * Geeft de eerste inschrijving die actief is op de gezette peildatum.
	 * 
	 * @return De eerste actieve inschrijving op de gezette peildatum.
	 */
	public Verbintenis getEersteInschrijvingOpPeildatum()
	{
		return getEersteInschrijvingOpPeildatum(false);
	}

	/**
	 * @param geefOokInactieveInschrijvingen
	 * @return De eerste inschrijving die gevonden wordt die actief is op de gezetten
	 *         peildatum. Als geefOokInactieveInschrijvingen true is, en er geen actieve
	 *         inschrijving gevonden kan worden, wordt ook de eerstvolgende actieve
	 *         inschrijving, dan wel de laatst actieve inschrijving teruggegeven.
	 */
	public Verbintenis getEersteInschrijvingOpPeildatum(boolean geefOokInactieveInschrijvingen)
	{
		Date peildatum = EduArteContext.get().getPeildatumOfVandaag();
		for (Verbintenis inschrijving : getVerbintenissen())
		{
			if (inschrijving.isActief(peildatum))
			{
				return inschrijving;
			}
		}
		if (geefOokInactieveInschrijvingen)
		{
			// Zoek toekomstige inschrijvingen
			for (Verbintenis inschrijving : getVerbintenissen())
			{
				if (inschrijving.getBegindatum().after(peildatum))
				{
					return inschrijving;
				}
			}
			// Zoek meeste recente inschrijving uit het verleden
			Verbintenis meestRecenteInschrijving = null;
			for (Verbintenis inschrijving : getVerbintenissen())
			{
				if (!inschrijving.isActief(peildatum))
				{
					// check of deze inschrijving recenter is dan de vorige gevonden
					// inschrijving
					if (meestRecenteInschrijving == null
						|| inschrijving.getEinddatumNotNull().after(
							meestRecenteInschrijving.getEinddatumNotNull()))
						meestRecenteInschrijving = inschrijving;
				}
			}
			return meestRecenteInschrijving;
		}
		return null;
	}

	/**
	 * Geeft de datum waarop de kwalificatieplicht voor deze deelnemer eindigt.
	 * 
	 * @return De datum waarop de kwalificatieplicht eindigt. Dit is de datum waarop de
	 *         deelnemer 18 wordt. De regeling geldt niet voor deelnemers die voor 1
	 *         augustus 1990 geboren zijn (op 1 augustus 2007 17 jaar of ouder zijn).
	 */
	public Date getDatumEindeKwalificatieplicht()
	{
		Date gebDat = getPersoon().getGeboortedatum();
		Date aug_1_1990 = TimeUtil.getInstance().asDate(1990, 7, 1);
		if (gebDat == null || gebDat.before(aug_1_1990))
		{
			return null;
		}
		Date eind = TimeUtil.getInstance().addYears(gebDat, 18);
		return eind;
	}

	/**
	 * @return true als de wet op kwalificatieplicht van toepassing is op deze deelnemer.
	 *         Kwalificatieplicht is alleen van toepassing voor deelnemers geboren voor 1
	 *         aug 1990.
	 */
	public boolean isKwalificatieplichtVanToepassing()
	{
		Date gebDat = getPersoon().getGeboortedatum();
		Date aug_1_1990 = TimeUtil.getInstance().asDate(1990, 7, 1);
		return gebDat != null && !gebDat.before(aug_1_1990);
	}

	@Exportable
	public boolean isLgf()
	{
		return lgf;
	}

	/**
	 * Geeft de datum waarop het de leerplicht eindigt voor deze deelnemer. Eerst wordt
	 * gekeken naar wat in nOISe is vastgelegd. Als hier niets gevonden wordt, wordt een
	 * berekening gedaan. De datum einde leerplicht is aan het eind van het schooljaar
	 * waarin de deelnemer 16 wordt. Een deelnemer die in september 2008 16 wordt heeft
	 * dus een einde leerplicht datum van 31-7-2009.
	 * 
	 * @return De datum waarop de leerplicht eindigt voor deze deelnemer.
	 */
	public Date getDatumEindeLeerplicht()
	{
		Date gebDat = getPersoon().getGeboortedatum();
		if (gebDat == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(gebDat);
		cal.add(Calendar.YEAR, 16);
		cal.add(Calendar.MONTH, 5);
		cal.set(Calendar.MONTH, Calendar.JULY);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		return cal.getTime();
	}

	/**
	 * @return Een lijst met alle verzorgerrelaties van deze deelnemer.
	 */
	public List<Relatie> getVerzorgers()
	{
		return getPersoon().getRelatiesRelatie();
	}

	@Exportable
	public Relatie getEersteVerzorger()
	{
		List<Relatie> list = getVerzorgers();
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	@Exportable
	public Relatie getTweedeVerzorger()
	{
		List<Relatie> list = getVerzorgers();
		if (list.size() > 1)
			return list.get(1);
		return null;
	}

	public Relatie getEersteRelatie()
	{
		if (getPersoon().getRelatiesRelatie().size() > 0)
			return getPersoon().getRelatiesRelatie().get(0);
		return null;
	}

	/**
	 * @return De eerste verzorger die als wettelijke vertegenwoordiger is aangegeven.
	 */
	@Exportable(omschrijving = "De eerste verzorger die als wettelijke vertegenwoordiger is aangegeven")
	public Persoon getWettelijkeVertegenwoordiger()
	{
		if (getPersoon().isMeerderjarig())
			return getPersoon();
		for (AbstractRelatie relatie : getPersoon().getWettelijkVertegenwoordigers())
		{
			AbstractRelatie unproxy = (AbstractRelatie) relatie.doUnproxy();
			if (unproxy instanceof Relatie)
				return ((Relatie) unproxy).getRelatie();
		}
		return null;
	}

	@Override
	public Deelnemer getDeelnemer()
	{
		return this;
	}

	/**
	 * @return Het deelnemeraccount dat gekoppeld is aan deze deelnemer.
	 */
	public DeelnemerAccount getDeelnemerAccount()
	{
		return (DeelnemerAccount) DataAccessRegistry.getHelper(AccountDataAccessHelper.class).get(
			this);
	}

	@Override
	public int compareTo(Deelnemer o)
	{
		int res = getPersoon().compareTo(o.getPersoon());
		if (res == 0)
		{
			res = getDeelnemernummer() - o.getDeelnemernummer();
		}
		return res;
	}

	@Exportable
	public int getDeelnemernummer()
	{
		return deelnemernummer;
	}

	public void setDeelnemernummer(int deelnemernummer)
	{
		this.deelnemernummer = deelnemernummer;
	}

	@Exportable
	public List<Verbintenis> getVerbintenissen()
	{
		return verbintenissen;
	}

	public List<Verbintenis> getActieveVerbintenissenOpPeildatum()
	{
		List<Verbintenis> ret = new ArrayList<Verbintenis>();
		for (Verbintenis verbintenis : getVerbintenissen())
		{
			if (verbintenis.isActief())
				ret.add(verbintenis);
		}
		return ret;
	}

	public List<Verbintenis> getActieveNietAfgemeldOfAfgewezenVerbintenissen()
	{
		List<Verbintenis> ret = new ArrayList<Verbintenis>();
		for (Verbintenis verbintenis : getActieveVerbintenissenOpPeildatum())
		{
			if (!verbintenis.getStatus().equals(VerbintenisStatus.Afgemeld)
				&& !verbintenis.getStatus().equals(VerbintenisStatus.Afgewezen))
				ret.add(verbintenis);
		}
		return ret;
	}

	public List<Verbintenis> getActieveVerbintenissenOpPeildatumOfInDeToekomst()
	{
		List<Verbintenis> ret = new ArrayList<Verbintenis>();
		for (Verbintenis verbintenis : getVerbintenissen())
		{
			if (!verbintenis.isBeeindigd())
				ret.add(verbintenis);
		}
		return ret;
	}

	public List<BPVInschrijving> getActieveBPVInschrijvingenOpPeildatumOfInDeToekomst()
	{
		List<BPVInschrijving> ret = new ArrayList<BPVInschrijving>();
		for (Verbintenis verbintenis : getVerbintenissen())
		{
			for (BPVInschrijving bpv : verbintenis.getBpvInschrijvingen())
				if (!bpv.isBeeindigd() && !bpv.getStatus().equals(BPVStatus.Afgemeld)
					&& !bpv.getStatus().equals(BPVStatus.Afgewezen))
					ret.add(bpv);
		}
		return ret;
	}

	@Exportable
	public Verbintenis getVerbintenisOpPeildatum()
	{
		for (Verbintenis verbintenis : getVerbintenissen())
		{
			if (verbintenis.isActief(EduArteContext.get().getPeildatum()))
				return verbintenis;
		}

		return null;
	}

	@Exportable
	public Verbintenis getVerbintenisOpPeildatumOfToekomst()
	{
		Verbintenis res = getVerbintenisOpPeildatum();
		if (res == null)
		{
			// Verbintenissen staatn gesorteerd op begindatum, dus geeft onderstaande de
			// eerste die actief wordt in de toekomst.
			for (Verbintenis verbintenis : getVerbintenissen())
			{
				if (verbintenis.getBegindatum().after(EduArteContext.get().getPeildatumOfVandaag()))
				{
					res = verbintenis;
					break;
				}
			}
		}
		return res;
	}

	/**
	 * @return alle verbintenissen in status intake (omgekeerd chronologisch)
	 */
	public List<Verbintenis> getIntakes()
	{
		List<Verbintenis> intakes = new ArrayList<Verbintenis>();
		for (Verbintenis verbintenis : getVerbintenissen())
		{
			if (verbintenis.getStatus() == VerbintenisStatus.Intake)
				intakes.add(verbintenis);
		}
		return intakes;
	}

	public void setVerbintenissen(List<Verbintenis> verbintenissen)
	{
		this.verbintenissen = verbintenissen;
	}

	public List<Groepsdeelname> getGroepsdeelnamesUnordered()
	{
		return groepsdeelnamesUnordered;
	}

	public void setGroepsdeelnamesUnordered(List<Groepsdeelname> groepsdeelnamesUnordered)
	{
		this.groepsdeelnamesUnordered = groepsdeelnamesUnordered;
	}

	public List<Medewerker> getDocentenOpDatum(Date datum)
	{
		MedewerkerDataAccessHelper helper =
			DataAccessRegistry.getHelper(MedewerkerDataAccessHelper.class);
		return helper.getDocentenVan(this, datum);
	}

	public List<Medewerker> getBegeleidersOpDatum(Date datum)
	{
		MedewerkerDataAccessHelper helper =
			DataAccessRegistry.getHelper(MedewerkerDataAccessHelper.class);
		return helper.getBegeleidersVan(this, datum);
	}

	public List<Medewerker> getVerantwoordelijkenOpDatum(Date datum)
	{
		DBSMedewerkerDataAccessHelper helper =
			DataAccessRegistry.getHelper(DBSMedewerkerDataAccessHelper.class);
		return helper.getVerantwoordelijkenVan(this, datum);
	}

	public List<Medewerker> getUitvoerendenOpDatum(Date datum)
	{
		DBSMedewerkerDataAccessHelper helper =
			DataAccessRegistry.getHelper(DBSMedewerkerDataAccessHelper.class);
		return helper.getUitvoerendenVan(this, datum);
	}

	@Override
	public String getContextInfoOmschrijving()
	{
		return getPersoon().getVolledigeNaam();
	}

	public List<Verbintenis> getVerbintenissenUnordered()
	{
		return verbintenissenUnordered;
	}

	public void setVerbintenissenUnordered(List<Verbintenis> verbintenissenUnordered)
	{
		this.verbintenissenUnordered = verbintenissenUnordered;
	}

	public List<Groepsdeelname> getGroepsdeelnames()
	{
		return groepsdeelnames;
	}

	public void setGroepsdeelnames(List<Groepsdeelname> groepsdeelnames)
	{
		this.groepsdeelnames = groepsdeelnames;
	}

	public List<Groepsdeelname> getGroepsdeelnamesOpPeildatum()
	{
		List<Groepsdeelname> res = new ArrayList<Groepsdeelname>();
		for (Groepsdeelname deelname : getGroepsdeelnames())
		{
			if (deelname.isActief(EduArteContext.get().getPeildatum()))
			{
				res.add(deelname);
			}
		}
		return res;
	}

	public List<Groepsdeelname> getGroepsdeelnamesMetGekoppeldeGroepOpPeildatum()
	{
		List<Groepsdeelname> res = new ArrayList<Groepsdeelname>();
		for (Groepsdeelname deelname : getGroepsdeelnames())
		{
			if (deelname.getGroep() != null
				&& deelname.isActief(EduArteContext.get().getPeildatum()))
			{
				res.add(deelname);
			}
		}
		return res;
	}

	@Exportable
	public String getGroepscodesOpPeildatum()
	{
		return StringUtil.toString(getGroepsdeelnamesMetGekoppeldeGroepOpPeildatum(), "",
			new StringConverter<Groepsdeelname>()
			{

				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(Groepsdeelname object, int listIndex)
				{
					if (object.getGroep() != null)
						return object.getGroep().getCode();
					return "";
				}
			});
	}

	@Exportable
	public String getGroepsnamenOpPeildatum()
	{
		return StringUtil.toString(getGroepsdeelnamesMetGekoppeldeGroepOpPeildatum(), "",
			new StringConverter<Groepsdeelname>()
			{

				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(Groepsdeelname object, int listIndex)
				{
					if (object.getGroep() != null)
						return object.getGroep().getNaam();
					return "";
				}
			});
	}

	@Exportable
	public String getGroepstypesOpPeildatum()
	{
		Set<Groepstype> set = new HashSet<Groepstype>();
		for (Groepsdeelname deelname : getGroepsdeelnamesOpPeildatum())
		{
			if (deelname.getGroep() != null)
				set.add(deelname.getGroep().getGroepstype());
		}
		return StringUtil.toString(set, "", new StringConverter<Groepstype>()
		{

			@Override
			public String getSeparator(int listIndex)
			{
				return ",";
			}

			@Override
			public String toString(Groepstype object, int listIndex)
			{
				return object.getNaam();
			}
		});
	}

	@Override
	public DeelnemerBijlage addBijlage(Bijlage bijlage)
	{
		DeelnemerBijlage newBijlage = new DeelnemerBijlage();
		newBijlage.setBijlage(bijlage);
		newBijlage.setDeelnemer(this);

		getBijlagen().add(newBijlage);

		return newBijlage;
	}

	@Override
	public boolean bestaatBijlage(Bijlage bijlage)
	{
		for (DeelnemerBijlage deelbijlage : getBijlagen())
		{
			if (deelbijlage.getBijlage().equals(bijlage))
				return true;
		}
		return false;
	}

	@Override
	public List<DeelnemerBijlage> getBijlagen()
	{
		if (bijlagen == null)
			return new ArrayList<DeelnemerBijlage>();
		return bijlagen;
	}

	/**
	 * Geef alleen de bijlagen die de huidige gebruiker mag zien, op basis van de
	 * documentcategorie van de bijlage en de rechten en rollen van de gebruiker
	 */
	public List<DeelnemerBijlage> getFilteredBijlagen()
	{
		DeelnemerSecurityCheck check =
			new DeelnemerSecurityCheck(new DataSecurityCheck(
				DeelnemerVerbintenisDocumentInzien.DEELNEMER_VERBINTENIS_DOCUMENT_INZIEN), this);

		List<DeelnemerBijlage> bijlages = new ArrayList<DeelnemerBijlage>();
		Set<Rol> rollen = EduArteContext.get().getAccount().getRollenAsRol();
		for (DeelnemerBijlage bijlage : getBijlagen())
		{
			if (bijlage.getBijlage().getDocumentType() == null)
			{
				bijlages.add(bijlage);
			}
			else
			{
				DocumentCategorie cat = bijlage.getBijlage().getDocumentType().getCategorie();
				if (!cat.isBeperkAutorisatie())
					bijlages.add(bijlage);
				else
				{
					List<DocumentTemplateRecht> rechten = cat.getRechten();
					for (DocumentTemplateRecht recht : rechten)
					{
						if (rollen.contains(recht.getRol())
							&& check.isActionAuthorized(getAction(recht.getActionClass())))
						{
							bijlages.add(bijlage);
							break;
						}
					}
				}
			}
		}
		return bijlages;
	}

	protected final WaspAction getAction(Class< ? extends WaspAction> action)
	{
		return ((WaspApplication) Application.get()).getActionFactory().getAction(action);
	}

	public int getAantalBijlagenVanType(DocumentType documentType)
	{
		int aantal = 0;
		for (DeelnemerBijlage bijlage : getBijlagen())
		{
			if (documentType.equals(bijlage.getBijlage().getDocumentType()))
			{
				aantal++;
			}
		}
		return aantal;
	}

	public void setVooropleidingen(List<Vooropleiding> vooropleidingen)
	{
		this.vooropleidingen = vooropleidingen;
	}

	public List<Vooropleiding> getVooropleidingen()
	{
		return vooropleidingen;
	}

	@Override
	public void setBijlagen(List<DeelnemerBijlage> bijlagen)
	{
		this.bijlagen = bijlagen;
	}

	/**
	 * @return De actieve verbintenis
	 */
	public Verbintenis getVOVerbintenis(Date peildatum)
	{
		for (Verbintenis inschrijving : getVerbintenissen())
		{
			if (inschrijving.isActief(peildatum) && inschrijving.isVOVerbintenis())
				return inschrijving;
		}
		return null;
	}

	public List<Verbintenis> getVOVerbintenissen()
	{
		List<Verbintenis> ret = new ArrayList<Verbintenis>();
		for (Verbintenis verbintenis : getVerbintenissen())
		{
			if (verbintenis.isVOVerbintenis())
				ret.add(verbintenis);
		}
		return ret;
	}

	public boolean heeftPersoonsgebondennummer()
	{
		return getPersoon().getBsn() != null || getOnderwijsnummer() != null;
	}

	/**
	 * Retourneert een nieuwe verbintenis voor deze deelnemer. Indien verderMetIntake ==
	 * true en de deelnemer precies 1 intake heeft, wordt deze genomen en opgewaardeerd
	 * tot voorlopige verbintenis. Anders is dit een volledig nieuwe verbintenis met een
	 * begindatum van 01-08 van dit schooljaar en een plaatsing met diezelfde begindatum
	 * en het bijbehorende cohort. Een volledig nieuw aangemaakte verbintenis wordt nog
	 * niet toegevoegd aan deelnemer.verbintenisen.
	 * 
	 * @param verderMetIntake
	 *            indien true en de deelnemer precies 1 intake heeft, wordt deze
	 *            geretourneerd; anders altijd een nieuwe verbintenis
	 */
	public Verbintenis nieuweVerbintenis(boolean verderMetIntake)
	{
		List<Verbintenis> intakes = getIntakes();
		Verbintenis verbintenis;
		if (verderMetIntake && intakes.size() == 1)
		{
			verbintenis = intakes.get(0);
			if (verbintenis.getIntakegesprekken().size() == 1)
			{
				Intakegesprek gesprek = verbintenis.getIntakegesprekken().get(0);
				verbintenis.setOpleiding(gesprek.getGewensteOpleiding());
				verbintenis.setLocatie(gesprek.getGewensteLocatie());
				if (gesprek.getGewensteBegindatum() != null)
					verbintenis.setBegindatum(gesprek.getGewensteBegindatum());
				verbintenis.setGeplandeEinddatum(verbintenis.berekenGeplandeEinddatum());
				if (verbintenis.getGeplandeEinddatum() == null)
					verbintenis.setGeplandeEinddatum(gesprek.getGewensteEinddatum());
			}
		}
		else
		{
			verbintenis = new Verbintenis(this);
			verbintenis.setOvereenkomstnummer(DataAccessRegistry.getHelper(
				NummerGeneratorDataAccessHelper.class).newOvereenkomstnummer());
			verbintenis.setStatus(VerbintenisStatus.Voorlopig);
			Cohort cohort = Cohort.getHuidigCohort();
			verbintenis.setCohort(cohort);
			verbintenis.setIndicatieGehandicapt(false);
			verbintenis.setBegindatum(cohort.getBegindatum());
			verbintenis.setRelevanteVooropleiding(getHoogsteRelevanteVooropleiding());
			verbintenis.nieuwePlaatsing();
		}
		return verbintenis;
	}

	/**
	 * Maak nieuwe verbintenis met status Intake bij de gegeven organisatieEenheid. Begin-
	 * en einddatum zijn huidige cohort (schooljaar). De verbintenis krijgt een nieuwe
	 * plaatsing. De verbintenis wordt nog niet toegevoegd aan de lijst
	 * deelnemer.verbintenissen
	 * 
	 * @return de nieuwe verbintenis
	 */
	public Verbintenis nieuweVerbintenis(OrganisatieEenheid organisatieEenheid)
	{
		return nieuweVerbintenisMetCohort(organisatieEenheid, Cohort.getHuidigCohort()
			.getBegindatum());
	}

	/**
	 * Maak nieuwe verbintenis met status Intake bij de gegeven organisatieEenheid. Begin-
	 * en einddatum zijn het gegeven cohort (schooljaar). De verbintenis krijgt een nieuwe
	 * plaatsing. De verbintenis wordt nog niet toegevoegd aan de lijst
	 * deelnemer.verbintenissen
	 * 
	 * @return de nieuwe verbintenis
	 */
	public Verbintenis nieuweVerbintenisMetCohort(OrganisatieEenheid organisatieEenheid,
			Date begindatum)
	{
		Verbintenis verbintenis = new Verbintenis(this);
		verbintenis.setOvereenkomstnummer(DataAccessRegistry.getHelper(
			NummerGeneratorDataAccessHelper.class).newOvereenkomstnummer());
		verbintenis.setStatus(VerbintenisStatus.Intake);
		verbintenis.setIndicatieGehandicapt(false);
		verbintenis.setOrganisatieEenheid(organisatieEenheid);
		verbintenis.setCohort(Cohort.getCohort(begindatum));
		verbintenis.setBegindatum(begindatum);
		verbintenis.setRelevanteVooropleiding(getHoogsteRelevanteVooropleiding());
		verbintenis.nieuwePlaatsing();

		return verbintenis;
	}

	/**
	 * Convenience methode om bij een deelnemer de hoogste geregistreerde vooropleiding op
	 * te vragen. Aan BRON wordt niet deze vooropleiding doorgegeven, maar de
	 * relevanteVooropleiding van de verbintenis.
	 * 
	 * @return De hoogste vooropleiding die geregistreerd staat bij de vooropleidingen van
	 *         de deelnemer (de verbintenissen niet meegenomen). Kan null zijn.
	 */
	public Vooropleiding getHoogsteVooropleiding()
	{
		Vooropleiding hoogste = null;
		SoortOnderwijs hoogsteSoort = null;
		for (Vooropleiding vooropleiding : vooropleidingen)
		{
			SoortOnderwijs soort = vooropleiding.getSoortOnderwijs();
			if (soort != null)
			{
				if (hoogsteSoort == null || soort.compareTo(hoogsteSoort) > 0)
				{
					hoogste = vooropleiding;
					hoogsteSoort = soort;
				}
			}
		}
		return hoogste;
	}

	/**
	 * Methode voor het opvragen van de hoogst genoten vooropleiding van deze deelnemer
	 * die als relevante vooropleiding kan dienen voor nieuwe verbintenissen. Houdt
	 * rekening met zowel externe (Vooropleiding) als interne (Verbintenis)
	 * vooropleidingen.
	 */
	public IVooropleiding getHoogsteRelevanteVooropleiding()
	{
		IVooropleiding res = getHoogsteVooropleiding();
		for (Verbintenis verbintenis : getVerbintenissen())
		{
			if (verbintenis.getOpleiding() != null)
			{
				if (verbintenis.isDiplomaBehaald())
				{
					SoortOnderwijs soortOnderwijs =
						verbintenis.getOpleiding().getVerbintenisgebied().getSoortOnderwijs();
					if (res == null)
					{
						res = verbintenis;
					}
					else
					{
						if (soortOnderwijs != null && res.getSoortOnderwijs() != null
							&& soortOnderwijs.ordinal() > res.getSoortOnderwijs().ordinal())
						{
							res = verbintenis;
						}
					}
				}
			}
		}
		return res;
	}

	/**
	 * @return true indien deze deelnemer een actief verbintenis heeft op de gegeven
	 *         datum.
	 */
	public boolean isActief(Date peildatum)
	{
		for (Verbintenis verbintenis : getVerbintenissen())
		{
			if (verbintenis.isActief(peildatum))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Verbintenis> getOrganisatieEenheidLocatieKoppelingen()
	{
		return getVerbintenissen();
	}

	/**
	 * Geeft het hoogste volgnummer van de verbintenissen <tt>+ 1</tt> terug.
	 */
	@SuppressWarnings("hiding")
	public int getVolgendeVerbintenisVolgnummer()
	{
		int max = 0;
		List<Verbintenis> verbintenissen = getVerbintenissen();
		for (Verbintenis verbintenis : verbintenissen)
		{
			max = Math.max(verbintenis.getVolgnummer(), max);
		}
		return max + 1;
	}

	/**
	 * @return De namen van alle basisgroepen waarin de deelnemer is geplaatst
	 */
	public String getBasisgroepnamen()
	{
		StringBuilder res = new StringBuilder(30);

		List<Verbintenis> listVerbintenis = getVerbintenissen();
		int counter = 0;
		for (Verbintenis verbintenis : listVerbintenis)
		{
			List<Plaatsing> plaatsingen = verbintenis.getPlaatsingen();

			for (Plaatsing plaatsing : plaatsingen)
			{
				if (plaatsing.getGroep() != null)
				{
					if (counter > 0)
					{
						res.append(", ");
					}

					res.append(plaatsing.getGroep().getNaam());

					counter++;
				}
			}
		}
		return res.toString();
	}

	public void setOnderwijsproductAfnames(List<OnderwijsproductAfname> onderwijsproductAfnames)
	{
		this.onderwijsproductAfnames = onderwijsproductAfnames;
	}

	public List<OnderwijsproductAfname> getOnderwijsproductAfnames()
	{
		return onderwijsproductAfnames;
	}

	public OnderwijsproductAfname getOnderwijsproductAfname(Onderwijsproduct onderwijsproduct,
			Cohort cohort)
	{
		for (OnderwijsproductAfname afname : getOnderwijsproductAfnames())
		{
			if (afname.getOnderwijsproduct().equals(onderwijsproduct)
				&& afname.getCohort().equals(cohort))
			{
				return afname;
			}
		}
		return null;
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

	public void setNegeerSleutelWijzigingenBron(boolean negeerSleutelWijzigingenBron)
	{
		this.negeerSleutelWijzigingenBron = negeerSleutelWijzigingenBron;
	}

	public boolean isNegeerSleutelWijzigingenBron()
	{
		return negeerSleutelWijzigingenBron;
	}

	public List<DeelnemerKenmerk> getKenmerken()
	{
		return kenmerken;
	}

	public void setKenmerken(List<DeelnemerKenmerk> kenmerken)
	{
		this.kenmerken = kenmerken;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(deelnemernummer).append(' ');
		if (getPersoon() != null)
			builder.append(getPersoon().toString());
		return builder.toString();
	}

	public String getKenmerkNamen()
	{
		return StringUtil.toString(getKenmerken(), "", new StringConverter<DeelnemerKenmerk>()
		{

			@Override
			public String getSeparator(int listIndex)
			{
				return ",";
			}

			@Override
			public String toString(DeelnemerKenmerk object, int listIndex)
			{
				return object.getKenmerk().getNaam();
			}

		});
	}

	public String getKenmerkCategorien()
	{
		return StringUtil.toString(getKenmerken(), "", new StringConverter<DeelnemerKenmerk>()
		{

			@Override
			public String getSeparator(int listIndex)
			{
				return ",";
			}

			@Override
			public String toString(DeelnemerKenmerk object, int listIndex)
			{
				return object.getKenmerk().getCategorie().getNaam();
			}

		});
	}

	public String getKenmerkToelichtingen()
	{
		return StringUtil.toString(getKenmerken(), "", new StringConverter<DeelnemerKenmerk>()
		{

			@Override
			public String getSeparator(int listIndex)
			{
				return ",";
			}

			@Override
			public String toString(DeelnemerKenmerk object, int listIndex)
			{
				return object.getToelichting();
			}

		});
	}

	public String getBegindatumKenmerken()
	{
		return StringUtil.toString(getKenmerken(), "", new StringConverter<DeelnemerKenmerk>()
		{

			@Override
			public String getSeparator(int listIndex)
			{
				return ",";
			}

			@Override
			public String toString(DeelnemerKenmerk object, int listIndex)
			{
				return object.getBegindatumFormatted();
			}

		});
	}

	public String getEinddatumKenmerken()
	{
		return StringUtil.toString(getKenmerken(), "", new StringConverter<DeelnemerKenmerk>()
		{

			@Override
			public String getSeparator(int listIndex)
			{
				return ",";
			}

			@Override
			public String toString(DeelnemerKenmerk object, int listIndex)
			{
				String res = object.getEinddatumFormatted();
				return res == null ? "geen" : res;
			}

		});
	}

	public void setRegistratieDatum(Date registratieDatum)
	{
		this.registratieDatum = registratieDatum;
	}

	@Exportable
	public Date getRegistratieDatum()
	{
		return registratieDatum;
	}

	public Date getStartkwalificatieplichtigTot()
	{
		if (startkwalificatieplichtigTot == null)
		{
			startkwalificatieplichtigTot =
				berekenDatumStartkwalificatieplichtigTotOpBasisVanLeeftijd();
		}
		return startkwalificatieplichtigTot;
	}

	public void setStartkwalificatieplichtigTot(Date startkwalificatieplichtigTot)
	{
		this.startkwalificatieplichtigTot = startkwalificatieplichtigTot;
	}

	public Long getOcwnummer()
	{
		return ocwnummer;
	}

	public void setOcwnummer(Long ocwnummer)
	{
		this.ocwnummer = ocwnummer;
	}

	public Integer getStudielinknummer()
	{
		return studielinknummer;
	}

	public void setStudielinknummer(Integer studielinknummer)
	{
		this.studielinknummer = studielinknummer;
	}

	public Boolean getGbaRelatie()
	{
		return gbaRelatie;
	}

	public void setGbaRelatie(Boolean gbaRelatie)
	{
		this.gbaRelatie = gbaRelatie;
	}

	public List<StudielinkBericht> getStudielinkBerichten()
	{
		return studielinkBerichten;
	}

	public void setStudielinkBerichten(List<StudielinkBericht> studielinkBerichten)
	{
		this.studielinkBerichten = studielinkBerichten;
	}

	private Date berekenDatumStartkwalificatieplichtigTotOpBasisVanLeeftijd()
	{
		if (getPersoon() != null && getPersoon().getGeboortedatum() != null)
		{
			return TimeUtil.getInstance().addYears(getPersoon().getGeboortedatum(), 24);
		}
		return null;
	}

	public boolean heeftBijlageVanCategorie(DocumentCategorie categorie)
	{
		for (DeelnemerBijlage bijlage : getBijlagen())
		{
			if (bijlage.getBijlage().getDocumentType() != null
				&& bijlage.getBijlage().getDocumentType().getCategorie().equals(categorie))
			{
				return true;
			}
		}
		return false;
	}

	public boolean heeftBijlageVanType(DocumentType type)
	{
		for (DeelnemerBijlage bijlage : getBijlagen())
		{
			if (bijlage.getBijlage().getDocumentType() != null
				&& bijlage.getBijlage().getDocumentType().equals(type))
			{
				return true;
			}
		}
		return false;
	}

	public void setUitsluitenVanFacturatie(boolean uitsluitenVanFacturatie)
	{
		this.uitsluitenVanFacturatie = uitsluitenVanFacturatie;
	}

	public boolean isUitsluitenVanFacturatie()
	{
		return uitsluitenVanFacturatie;
	}

	public boolean heeftBetalingsplichtigBPVBedrijf()
	{
		for (Verbintenis verbintenis : getVerbintenissen())
		{
			for (BPVInschrijving bpv : verbintenis.getBpvInschrijvingen())
				if (Boolean.TRUE.equals(bpv.getNeemtBetalingsplichtOver()))
					return true;
		}
		return false;
	}

	public void setDaPersGegegensConflict(Boolean daPersGegegensConflict)
	{
		this.daPersGegegensConflict = daPersGegegensConflict;
	}

	public Boolean getDaPersGegegensConflict()
	{
		return daPersGegegensConflict;
	}

	@Exportable
	public List<Traject> getTrajecten()
	{
		return trajecten;
	}

	public void setTrajecten(List<Traject> trajectenLijst)
	{
		this.trajecten = trajectenLijst;
	}

	/**
	 * Notieties voor het genereren van een pdf
	 */
	public List<Notitie> getNotities()
	{
		NotitieZoekFilter filter = new NotitieZoekFilter();
		filter.setDeelnemer(this);
		filter.setVertrouwelijkAllowed(false);

		List<Notitie> notities =
			DataAccessRegistry.getHelper(NotitieDataAccessHelper.class).list(filter);

		return notities;
	}

	/**
	 * Incidenten voor het genereren van een pdf
	 */
	public List<Incident> getIncidenten()
	{
		IncidentZoekFilter filter = new IncidentZoekFilter();
		filter.setDeelnemer(this);
		filter.setVertrouwelijkAllowed(false);

		List<Incident> incidenten =
			DataAccessRegistry.getHelper(IncidentDataAccessHelper.class).list(filter);

		return incidenten;
	}

	public boolean getLWOOLaatstePlaatsing()
	{
		Plaatsing laatstePlaatsing = null;
		for (Verbintenis v : getVerbintenissen())
		{
			Plaatsing plaatsing = v.getLaatstePlaatsing();
			if ((laatstePlaatsing == null || laatstePlaatsing.getBegindatum() == null || (plaatsing != null
				&& plaatsing.getBegindatum() != null && plaatsing.getBegindatum().after(
				laatstePlaatsing.getBegindatum()))))
			{
				laatstePlaatsing = plaatsing;
			}
		}
		return laatstePlaatsing != null ? laatstePlaatsing.isLwoo() : false;
	}

	public List<Inschrijvingsverzoek> getInschrijfverzoeken()
	{
		InschrijvingsverzoekZoekFilter filter = new InschrijvingsverzoekZoekFilter();
		filter.setDeelnemer(this);

		List<Inschrijvingsverzoek> verzoeken =
			DataAccessRegistry.getHelper(InschrijvingsverzoekDataAccessHelper.class).list(filter);

		return verzoeken;
	}

	/**
	 * Berekent de datum waarop deze deelnemer een startkwalificatie gehaald heeft en set
	 * deze op het betreffende property. De berekende datum wordt alleen gebruikt als deze
	 * eerder is dan de al gezette datum.
	 */
	public void berekenEnSetStartkwalificatieplichtigTot(Vooropleiding gewijzigdeVooropleiding)
	{
		Date kwalificatieBehaald = getStartkwalificatieplichtigTot();
		if (kwalificatieBehaald == null)
			return;
		List<Vooropleiding> vooropleidingenList =
			new ArrayList<Vooropleiding>(getVooropleidingen() != null ? getVooropleidingen()
				: new ArrayList<Vooropleiding>());
		if (gewijzigdeVooropleiding != null
			&& !vooropleidingenList.contains(gewijzigdeVooropleiding))
			vooropleidingenList.add(gewijzigdeVooropleiding);
		for (Vooropleiding vooropleiding : vooropleidingenList)
		{
			if (vooropleiding.isStartkwalificatie())
			{
				if (vooropleiding.getEinddatum() != null
					&& vooropleiding.getEinddatum().before(kwalificatieBehaald))
				{
					kwalificatieBehaald = vooropleiding.getEinddatum();
				}
			}
		}
		for (Verbintenis verbintenis : getVerbintenissen())
		{
			if (verbintenis.isDiplomaBehaald())
			{
				SoortOnderwijs soort = verbintenis.getSoortOnderwijs();
				if (soort != null && soort.isStartkwalificatie())
				{
					Date datumUitslag = verbintenis.getDatumExamenuitslag();
					if (datumUitslag != null && datumUitslag.before(kwalificatieBehaald))
					{
						kwalificatieBehaald = datumUitslag;
					}
				}
			}
		}
		if (kwalificatieBehaald.before(getStartkwalificatieplichtigTot()))
			setStartkwalificatieplichtigTot(kwalificatieBehaald);
	}

	public void setHeeftBachelorgraad(Boolean heeftBachelorgraad)
	{
		this.heeftBachelorgraad = heeftBachelorgraad;
	}

	public Boolean getHeeftBachelorgraad()
	{
		return heeftBachelorgraad != null ? heeftBachelorgraad : Boolean.FALSE;
	}

	public void setHeeftMastergraad(Boolean heeftMastergraad)
	{
		this.heeftMastergraad = heeftMastergraad;
	}

	public Boolean getHeeftMastergraad()
	{
		return heeftMastergraad != null ? heeftMastergraad : Boolean.FALSE;
	}

	/**
	 * @return true voor een nationaliteit in EER, Zwitserland of Suriname
	 */
	public boolean isNationaliteitHOBekostigbaar()
	{
		Persoon p = getPersoon();
		Nationaliteit nationaliteit1 = p.getNationaliteit1();
		Nationaliteit nationaliteit2 = p.getNationaliteit2();
		return (nationaliteit1 != null && nationaliteit1.isHOBekostigbaar())
			|| (nationaliteit2 != null && nationaliteit2.isHOBekostigbaar());
	}

	/**
	 * @return true voor een woonadres in Benelux of grensstreek Duitsland
	 */
	public boolean isWoonlandHOBekostigbaar()
	{
		return persoon.getFysiekAdres() != null
			&& persoon.getFysiekAdres().getAdres().isHOBekostigbaar();
	}

}