package nl.topicus.eduarte.entities.inschrijving;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.entities.begineinddatum.IBeginEinddatumEntiteit;
import nl.topicus.eduarte.entities.hogeronderwijs.VooropleidingSignaalcode;
import nl.topicus.eduarte.entities.hogeronderwijs.VooropleidingVakResultaat;
import nl.topicus.eduarte.entities.hogeronderwijs.VooropleidingVerificatieStatus;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding.SoortOnderwijs;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.taxonomie.ho.CrohoOpleiding;
import nl.topicus.eduarte.entities.vrijevelden.VooropleidingVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.onderwijs.duo.bron.Bron;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Vooropleiding van een deelnemer.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Exportable
public class Vooropleiding extends InstellingEntiteit implements IBeginEinddatumEntiteit,
		IVooropleiding, VrijVeldable<VooropleidingVrijVeld>
{
	private static final long serialVersionUID = 1L;

	public enum SoortVooropleidingOrganisatie
	{
		ExterneOrganisatie
		{
			@Override
			public String toString()
			{
				return "Externe Organisatie";
			}
		},
		Buitenland,
		Overig;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = false)
	@Index(name = "idx_Vooropleiding_deelnemer")
	private Deelnemer deelnemer;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private SoortVooropleidingOrganisatie soortOrganisatie =
		SoortVooropleidingOrganisatie.ExterneOrganisatie;

	/**
	 * De externe organisatie (landelijk of instelling-specifiek) waaraan de vooropleiding
	 * is genoten.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "externeOrganisatie", nullable = true)
	@Index(name = "idx_Vooropleiding_extOrg")
	@Bron
	private ExterneOrganisatie externeOrganisatie;

	/**
	 * Naam van de organisatie waar de vooropleiding is genoten.
	 */
	@Column(length = 100, nullable = true)
	@AutoForm(label = "Naam onderwijsinstelling")
	private String naam;

	@Column(length = 100, nullable = true)
	private String plaats;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "soortVooropleiding", nullable = true)
	@Index(name = "idx_Vooropleiding_soort")
	@Bron
	private SoortVooropleiding soortVooropleiding;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "soortVooropleidingHO", nullable = true)
	@Index(name = "idx_Vooropleiding_soort_HO")
	private SoortVooropleidingHO soortVooropleidingHO;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "soortVooropleidingBuitenlands", nullable = true)
	@Index(name = "idx_Vooropl_SoortVooroplBuit")
	private SoortVooropleidingBuitenlands soortVooropleidingBuitenlands;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "soortVooropleidingCroho", nullable = true)
	@Index(name = "idx_Vooropl_SoortVooroplCroho")
	private CrohoOpleiding soortVooropleidingCroho;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "land", nullable = true)
	@Index(name = "idx_Vooropleiding_land")
	private Land land;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schooladvies", nullable = true)
	@Index(name = "idx_Vooropleiding_adv")
	private Schooladvies schooladvies;

	@Column(nullable = true)
	private Integer citoscore;

	@Column(nullable = false)
	@Bron
	private boolean diplomaBehaald;

	@Column(nullable = true)
	private Integer aantalJarenOnderwijs;

	@Column(nullable = false)
	private boolean aantalJarenZelfInvullen = false;

	/**
	 * De eerste dag waarop de vooropleiding geldig wordt.
	 */
	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date begindatum;

	/**
	 * De laatste dag waarop de vooropleiding geldig wordt.
	 */
	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date einddatum;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vooropleiding")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	private List<VooropleidingVrijVeld> vrijVelden;

	@Column(length = 100, nullable = true)
	private String vooropleidingNaam;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vooropleiding")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	private List<VooropleidingSignaalcode> vooropleidingSignaalcodes =
		new ArrayList<VooropleidingSignaalcode>();

	@Column(nullable = true)
	@Enumerated(value = EnumType.STRING)
	private VooropleidingVerificatieStatus verificatieStatus;

	@Column(nullable = true)
	private Date verificatieDatum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "verificatieBrin", nullable = true)
	@Index(name = "idx_Vooropl_Brin")
	private Brin verificatieDoor;

	@Column(length = 70, nullable = true)
	private String verificatieDoorInstelling;

	@Column(length = 35, nullable = true)
	private String verificatieDoorMedewerker;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vooropleiding")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	private List<VooropleidingVakResultaat> vooropleidingVakResultaten =
		new ArrayList<VooropleidingVakResultaat>();

	public Vooropleiding()
	{
		if (DataAccessRegistry.isInitialized())
		{
			// standaard als land = nederland instellen, maar alleen als de
			// DataAccessRegistry beschikbaar is, want anders krijgen we een exception
			// tijdens het opstarten
			land = Land.getNederland();
		}
		soortOrganisatie = SoortVooropleidingOrganisatie.ExterneOrganisatie;
	}

	public Vooropleiding(Deelnemer deelnemer)
	{
		this();
		setDeelnemer(deelnemer);
	}

	/**
	 * @return Returns the soortOrganisatie.
	 */
	@Exportable
	public SoortVooropleidingOrganisatie getSoortOrganisatie()
	{
		return soortOrganisatie;
	}

	/**
	 * @param deelnemer
	 *            The deelnemer to set.
	 */
	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
		if (deelnemer != null)
		{
			deelnemer.berekenEnSetStartkwalificatieplichtigTot(this);
		}
	}

	/**
	 * @return Returns the deelnemer.
	 */
	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	/**
	 * @param soortOrganisatie
	 *            The soortOrganisatie to set.
	 */
	public void setSoortOrganisatie(SoortVooropleidingOrganisatie soortOrganisatie)
	{
		this.soortOrganisatie = soortOrganisatie;
	}

	@Exportable
	public ExterneOrganisatie getExterneOrganisatie()
	{
		return externeOrganisatie;
	}

	public void setExterneOrganisatie(ExterneOrganisatie externeOrganisatie)
	{
		this.externeOrganisatie = externeOrganisatie;
	}

	/**
	 * @return Returns the naam.
	 */
	@Exportable
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
	 * @return Returns the plaats.
	 */
	@Exportable
	public String getPlaats()
	{
		return plaats;
	}

	/**
	 * @param plaats
	 *            The plaats to set.
	 */
	public void setPlaats(String plaats)
	{
		this.plaats = plaats;
	}

	/**
	 * @return Returns the land.
	 */
	public Land getLand()
	{
		return land;
	}

	/**
	 * @param land
	 *            The land to set.
	 */
	public void setLand(Land land)
	{
		this.land = land;
	}

	/**
	 * @return Returns the schooladvies.
	 */
	@Exportable
	public Schooladvies getSchooladvies()
	{
		return schooladvies;
	}

	/**
	 * @param schooladvies
	 *            The schooladvies to set.
	 */
	public void setSchooladvies(Schooladvies schooladvies)
	{
		this.schooladvies = schooladvies;
	}

	/**
	 * @return Returns the citoscore.
	 */
	public Integer getCitoscore()
	{
		return citoscore;
	}

	/**
	 * @param citoscore
	 *            The citoscore to set.
	 */
	public void setCitoscore(Integer citoscore)
	{
		this.citoscore = citoscore;
	}

	/**
	 * @return Returns the diplomaBehaald.
	 */
	@Exportable
	public boolean isDiplomaBehaald()
	{
		return diplomaBehaald;
	}

	/**
	 * @param diplomaBehaald
	 *            The diplomaBehaald to set.
	 */
	public void setDiplomaBehaald(boolean diplomaBehaald)
	{
		this.diplomaBehaald = diplomaBehaald;
		if (getDeelnemer() != null)
		{
			getDeelnemer().berekenEnSetStartkwalificatieplichtigTot(this);
		}
	}

	@Override
	public boolean isActief()
	{
		return true;
	}

	@Override
	public boolean isActief(Date peildatum)
	{
		return isActief();
	}

	public String getOrganisatieOmschrijving()
	{
		StringBuilder builder = new StringBuilder();
		if (naam != null)
		{
			builder.append(naam);
			if (plaats != null)
				builder.append(", ");
		}
		if (plaats != null)
			builder.append(plaats).append(' ');
		if (land != null && !Land.getNederland().equals(land))
			builder.append('(').append(land.getNaam()).append(')');
		return builder.toString();
	}

	public String getOmschrijving()
	{
		StringBuilder builder = new StringBuilder();
		if (getSoortVooropleiding() != null)
		{
			builder.append(getSoortOnderwijs().getCode()).append(" - ").append(
				getSoortVooropleiding().getNaam()).append(" ");
		}
		builder.append(getOrganisatieOmschrijving());

		return builder.toString();
	}

	@Override
	public String toString()
	{
		return getOmschrijving();
	}

	@Override
	@Exportable
	public Date getBegindatum()
	{
		return begindatum;
	}

	@Override
	@Exportable
	public Date getEinddatum()
	{
		return einddatum;
	}

	@Override
	public void setBegindatum(Date begindatum)
	{
		this.begindatum = begindatum;
	}

	@Override
	public void setEinddatum(Date einddatum)
	{
		this.einddatum = einddatum;
		if (getDeelnemer() != null)
		{
			getDeelnemer().berekenEnSetStartkwalificatieplichtigTot(this);
		}
	}

	@Override
	public Date getEinddatumNotNull()
	{
		return einddatum != null ? einddatum : MAX_DATE;
	}

	public void setAantalJarenOnderwijs(Integer aantalJarenOnderwijs)
	{
		this.aantalJarenOnderwijs = aantalJarenOnderwijs;
	}

	@Exportable
	public Integer getAantalJarenOnderwijs()
	{
		return aantalJarenOnderwijs;
	}

	/**
	 * Berekent het aantal jaren onderwijs. Set niets.
	 * 
	 * @return Het aantal jaren onderwijs adhv begin- en einddatum (afgerond naar het
	 *         dichtstbijzijnde hele getal). Null indien begin- of einddatum ontbreekt of
	 *         de begindatum na de einddatum ligt.
	 */
	public Integer berekenAantalJarenOnderwijs()
	{
		if (begindatum != null && einddatum != null && !begindatum.after(einddatum))
		{
			// tel bij de einddatum een half jaar op om af te ronden naar het
			// dichtstbijzijnde aantal hele jaren
			Date halfJaarLater = TimeUtil.getInstance().addMonths(einddatum, 6);
			return TimeUtil.getInstance().getDifferenceInYears(halfJaarLater, begindatum);
		}
		return null;
	}

	public void setSoortVooropleiding(SoortVooropleiding soortVooropleiding)
	{
		this.soortVooropleiding = soortVooropleiding;
		if (getDeelnemer() != null)
		{
			getDeelnemer().berekenEnSetStartkwalificatieplichtigTot(this);
		}
	}

	@Exportable
	public SoortVooropleiding getSoortVooropleiding()
	{
		return soortVooropleiding;
	}

	public SoortVooropleidingBuitenlands getSoortVooropleidingBuitenlands()
	{
		return soortVooropleidingBuitenlands;
	}

	public void setSoortVooropleidingBuitenlands(
			SoortVooropleidingBuitenlands soortVooropleidingBuitenlands)
	{
		this.soortVooropleidingBuitenlands = soortVooropleidingBuitenlands;
	}

	public CrohoOpleiding getSoortVooropleidingCroho()
	{
		return soortVooropleidingCroho;
	}

	public void setSoortVooropleidingCroho(CrohoOpleiding soortVooropleidingCroho)
	{
		this.soortVooropleidingCroho = soortVooropleidingCroho;
	}

	@Override
	@Exportable
	public SoortOnderwijs getSoortOnderwijs()
	{
		if (getSoortVooropleiding() != null)
			return isDiplomaBehaald() ? getSoortVooropleiding().getSoortOnderwijsMetDiploma()
				: getSoortVooropleiding().getSoortOnderwijsZonderDiploma();
		return null;
	}

	public Boolean hasBronCommuniceerbareVerbintenissen()
	{
		int count = 0;
		for (Verbintenis verbintenis : getDeelnemer().getVerbintenissen())
		{
			if (verbintenis.getRelevanteVooropleiding() != null
				&& verbintenis.getRelevanteVooropleiding().equals(this))
			{
				count++;
			}
		}
		if (count == 0)
			return false;

		List<VerbintenisStatus> arr =
			java.util.Arrays.asList(VerbintenisStatus.Voorlopig, VerbintenisStatus.Intake,
				VerbintenisStatus.Afgemeld, VerbintenisStatus.Afgewezen);
		count = 0;
		for (Verbintenis verbintenis : getDeelnemer().getVerbintenissen())
		{
			if (verbintenis.getRelevanteVooropleiding() != null
				&& verbintenis.getRelevanteVooropleiding().equals(this))
			{
				if (arr.contains(verbintenis.getStatus()))
					count++;
			}
		}
		return count == 0;
	}

	@Override
	public List<VooropleidingVrijVeld> getVrijVelden()
	{
		if (vrijVelden == null)
			vrijVelden = new ArrayList<VooropleidingVrijVeld>();

		return vrijVelden;
	}

	@Override
	public List<VooropleidingVrijVeld> getVrijVelden(VrijVeldCategorie categorie)
	{
		List<VooropleidingVrijVeld> res = new ArrayList<VooropleidingVrijVeld>();
		for (VooropleidingVrijVeld pvv : getVrijVelden())
		{
			if (pvv.getVrijVeld().getCategorie().equals(categorie))
			{
				res.add(pvv);
			}
		}
		return res;
	}

	@Override
	public VooropleidingVrijVeld newVrijVeld()
	{
		VooropleidingVrijVeld pvv = new VooropleidingVrijVeld();
		pvv.setVooropleiding(this);

		return pvv;
	}

	@Override
	public void setVrijVelden(List<VooropleidingVrijVeld> vrijvelden)
	{
		this.vrijVelden = vrijvelden;
	}

	public String getVooropleidingNaam()
	{
		return vooropleidingNaam;
	}

	public void setVooropleidingNaam(String vooropleidingNaam)
	{
		this.vooropleidingNaam = vooropleidingNaam;
	}

	public List<VooropleidingSignaalcode> getVooropleidingSignaalcodes()
	{
		return vooropleidingSignaalcodes;
	}

	public void setVooropleidingSignaalcodes(
			List<VooropleidingSignaalcode> vooropleidingSignaalcodes)
	{
		this.vooropleidingSignaalcodes = vooropleidingSignaalcodes;
	}

	public VooropleidingVerificatieStatus getVerificatieStatus()
	{
		return verificatieStatus;
	}

	public void setVerificatieStatus(VooropleidingVerificatieStatus verificatieStatus)
	{
		this.verificatieStatus = verificatieStatus;
	}

	public Date getVerificatieDatum()
	{
		return verificatieDatum;
	}

	public void setVerificatieDatum(Date verificatieDatum)
	{
		this.verificatieDatum = verificatieDatum;
	}

	public Brin getVerificatieDoor()
	{
		return verificatieDoor;
	}

	public void setVerificatieDoor(Brin verificatieDoor)
	{
		this.verificatieDoor = verificatieDoor;
	}

	public String getVerificatieDoorInstelling()
	{
		return verificatieDoorInstelling;
	}

	public void setVerificatieDoorInstelling(String verificatieDoorInstelling)
	{
		this.verificatieDoorInstelling = verificatieDoorInstelling;
	}

	public String getVerificatieDoorMedewerker()
	{
		return verificatieDoorMedewerker;
	}

	public void setVerificatieDoorMedewerker(String verificatieDoorMedewerker)
	{
		this.verificatieDoorMedewerker = verificatieDoorMedewerker;
	}

	public List<VooropleidingVakResultaat> getVooropleidingVakResultaten()
	{
		return vooropleidingVakResultaten;
	}

	public void setVooropleidingVakResultaten(
			List<VooropleidingVakResultaat> vooropleidingVakResultaten)
	{
		this.vooropleidingVakResultaten = vooropleidingVakResultaten;
	}

	@Override
	public String getVrijVeldWaarde(String vrijVeldNaam)
	{
		for (VooropleidingVrijVeld vrijVeld : vrijVelden)
			if (vrijVeld.getVrijVeld().getNaam().equals(vrijVeldNaam))
				return vrijVeld.getOmschrijving();
		return null;
	}

	@Override
	@Exportable
	public String getBrincode()
	{
		ExterneOrganisatie org =
			getExterneOrganisatie() == null ? null : (ExterneOrganisatie) getExterneOrganisatie()
				.doUnproxy();
		if (org == null || !(org instanceof Brin))
			return null;
		return ((Brin) org).getCode();
	}

	@Override
	public String getNaamVooropleiding()
	{
		return getOmschrijving();
	}

	public boolean isAantalJarenZelfInvullen()
	{
		return aantalJarenZelfInvullen;
	}

	public void setAantalJarenZelfInvullen(boolean aantalJarenZelfInvullen)
	{
		this.aantalJarenZelfInvullen = aantalJarenZelfInvullen;
	}

	public void setSoortVooropleidingHO(SoortVooropleidingHO soortVooropleidingHO)
	{
		this.soortVooropleidingHO = soortVooropleidingHO;
	}

	public SoortVooropleidingHO getSoortVooropleidingHO()
	{
		return soortVooropleidingHO;
	}

	/**
	 * @return true als deze vooropleiding voldoet aan de startkwalificatieeisen.
	 */
	public boolean isStartkwalificatie()
	{
		SoortOnderwijs soort = getSoortOnderwijs();
		if (soort == null)
			return false;
		return soort.isStartkwalificatie();
	}

}