package nl.topicus.eduarte.entities.participatie;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.FieldPersistance;
import nl.topicus.cobra.entities.FieldPersistenceMode;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.cobra.entities.Time;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.FieldContainerType;
import nl.topicus.cobra.web.components.labels.DatumTijdLabel;
import nl.topicus.cobra.web.components.text.DatumTijdField;
import nl.topicus.eduarte.app.security.checks.DeelnemerSecurityCheck;
import nl.topicus.eduarte.app.security.checks.OrganisatieEenheidLocatieKoppelbaarSecurityCheck;
import nl.topicus.eduarte.dao.participatie.helpers.PersoonlijkeGroepDataAccessHelper;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakTypeCategory;
import nl.topicus.eduarte.entities.participatie.enums.IParticipatieBlokObject;
import nl.topicus.eduarte.entities.participatie.enums.UitnodigingStatus;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.participatie.web.components.input.AddRemoveInput;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;
import nl.topicus.eduarte.web.components.quicksearch.onderwijsproduct.OnderwijsproductSearchEditor;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * Een afspraak kan bijvoorbeeld een roosterelement, een individueel gesprek tussen een
 * begeleider en een deelnemer, een projectbijeenkomst of een stagebezoek zijn. Een
 * afspraak geeft aan dat een groep van deelnemers en eventueel een of meer begeleiders
 * bij elkaar komen voor een onderwijsactiviteit. Een afspraak kan voor een gedeelte (of
 * helemaal) meetellen voor In Instelling Verzorgd Onderwijs (IIVO). IIVO-uren zijn de
 * uren die meetellen voor de 850-/1040-urennorm.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Table(appliesTo = "Afspraak", indexes = {@Index(name = "idx_Afspraak_externId", columnNames = {
	"organisatie", "externId", "externSysteem"})})
@BatchSize(size = 1000)
public class Afspraak extends InstellingEntiteit implements IParticipatieBlokObject,
		IBijlageKoppelEntiteit<AfspraakBijlage>, OrganisatieEenheidLocatieProvider
{
	public static final String MEDEWERKER_WRITE = "AFSPRAAK_MEDEWERKER_WRITE";

	public static final String DEELNEMER_WRITE = "AFSPRAAK_DEELNEMER_WRITE";

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "organisatieEenheid")
	@Index(name = "idx_Afspraak_orgEhd")
	@AutoForm(htmlClasses = "unit_max")
	private OrganisatieEenheid organisatieEenheid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "locatie")
	@Index(name = "idx_Afspraak_locatie")
	@AutoForm(htmlClasses = "unit_max")
	private Locatie locatie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "afspraakType")
	@Index(name = "idx_Afspraak_type")
	@AutoForm(htmlClasses = "unit_max")
	private AfspraakType afspraakType;

	@Column(nullable = false, length = 255)
	@AutoForm(htmlClasses = "unit_max")
	private String titel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "onderwijsproduct")
	@Index(name = "idx_Afspraak_onderwijsproduct")
	@AutoForm(editorClass = OnderwijsproductSearchEditor.class, htmlClasses = "unit_max")
	private Onderwijsproduct onderwijsproduct;

	@Column(nullable = true, length = 100)
	@AutoForm(htmlClasses = "unit_max")
	private String afspraakLocatie;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true)
	@AutoForm(editorClass = DatumTijdField.class, displayClass = DatumTijdLabel.class, label = "Begin", order = 7, editorContainer = FieldContainerType.DIV)
	private Date beginDatumTijd;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true)
	@AutoForm(editorClass = DatumTijdField.class, displayClass = DatumTijdLabel.class, label = "Eind", order = 8, editorContainer = FieldContainerType.DIV)
	private Date eindDatumTijd;

	/**
	 * Het beginlesuur van de afspraak. Niet verplicht, wordt alleen gebruikt voor
	 * informatieve doeleinden.
	 */
	@Column(nullable = true)
	private Integer beginLesuur;

	/**
	 * Het eindlesuur van de afspraak. Niet verplicht, wordt alleen gebruikt voor
	 * informatieve doeleinden.
	 */
	@Column(nullable = true)
	private Integer eindLesuur;

	@Column(nullable = true)
	@Lob
	@AutoForm(htmlClasses = "unit_max", order = 6)
	private String omschrijving;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "auteur")
	@Index(name = "idx_Afspraak_auteur")
	private Persoon auteur;

	/**
	 * Het aantal minute dat deze afspraak meetelt voor IIVO. Het aantal minuten moet
	 * tussen 0 en het aantal minuten van de afspraak liggen (inclusief).
	 */
	@Column(nullable = false)
	@AutoForm(label = "Minuten IIVO")
	private int minutenIIVO;

	/**
	 * Moet presentie/absentie bijgehouden worden voor deze afspraak? Zo ja, moet het veld
	 * presentieRegistratieVerwerkt uiteindelijk op True gezet worden.
	 */
	@Column(nullable = false)
	@AutoForm(label = "Presentieregistratie")
	private boolean presentieRegistratieVerplicht;

	/**
	 * Is de presentie/absentieregistratie van deze afspraak uitgevoerd.
	 */
	@Column(nullable = false)
	private boolean presentieRegistratieVerwerkt;

	/**
	 * Mag de deelnemer zelf zijn presentie registratie doen
	 */
	@Column(nullable = false, name = "presentieDoorDeelnemer")
	@AutoForm(label = "... door deelnemer")
	private boolean presentieRegistratieDoorDeelnemer;

	/**
	 * Als een afspraak niet eenmalig is, maar elke X weken terugkeert wordt een
	 * HerhalendeAfspraak aangemaakt. Alle afspraken die het resultaat zijn van de
	 * herhalende afspraak verwijzen naar dezelfde herhalende afspraak. Op deze manier kan
	 * bij het wijzigen van een herhalende afspraak eventueel ook alle andere bijbehorende
	 * afspraken gewijzigd worden.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "herhalendeAfspraak", nullable = true)
	@Index(name = "idx_Afspraak_herhalendeAf")
	private HerhalendeAfspraak herhalendeAfspraak;

	/**
	 * Een afspraak kan gedefinieerd zijn binnen een basisrooster.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "basisrooster", nullable = true)
	@Index(name = "idx_Afspraak_basisrooster")
	private Basisrooster basisrooster;

	/**
	 * Als een afspraak afkomstig is uit een extern systeem, zoals bijvoorbeeld een
	 * roosterprogramma, wordt het id van het externe systeem bijgehouden om bij eventuele
	 * wijzigingen in het bronsysteem de afspraak terug te kunnen vinden in de
	 * participatiemodule. Als het externe roostersysteem geen id aanlevert, kan door de
	 * participatiemodule een id gegenereerd worden die bestaat uit de identificerende
	 * bestanddelen van de afspraak.
	 */
	@Column(length = 500, nullable = true)
	private String externId;

	/**
	 * Het externe systeem waar de afspraak van afkomstig is.
	 */
	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private ExternSysteem externSysteem;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "afspraak")
	private List<AfspraakParticipant> participanten = new ArrayList<AfspraakParticipant>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "afspraak")
	private List<AfspraakBijlage> bijlagen = new ArrayList<AfspraakBijlage>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "afspraak")
	private List<Waarneming> waarnemingen = new ArrayList<Waarneming>();

	@RestrictedAccess(hasGetter = true, hasSetter = false)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "afspraak")
	@FieldPersistance(FieldPersistenceMode.SKIP)
	private List<AfspraakDeelnemer> deelnemers = new ArrayList<AfspraakDeelnemer>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cacheRegion", nullable = true)
	@Index(name = "idx_Afspraak_cacheRegion")
	private CacheRegion cacheRegion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inloopCollege", nullable = true)
	@Index(name = "idx_Afspraak_inloopCollege")
	@AutoForm(editorClass = AddRemoveInput.class)
	private InloopCollege inloopCollege;

	/**
	 * Default constructor voor Hibernate.
	 */
	public Afspraak()
	{
	}

	public List<AfspraakBijlage> getBijlagen()
	{
		return bijlagen;
	}

	public void setBijlagen(List<AfspraakBijlage> bijlagen)
	{
		this.bijlagen = bijlagen;
	}

	/**
	 * Verwijdert de gegeven bijlage van dit onderwijsproduct. Aanroeper is
	 * verantwoordelijk voor het committen.
	 * 
	 * @param bijlage
	 */
	public AfspraakBijlage deleteBijlage(Bijlage bijlage)
	{
		AfspraakBijlage teVerwijderen = null;
		for (AfspraakBijlage afspraakBijlage : getBijlagen())
		{
			if (afspraakBijlage.getBijlage().equals(bijlage))
			{
				teVerwijderen = afspraakBijlage;
				break;
			}
		}
		if (teVerwijderen != null)
		{
			getBijlagen().remove(teVerwijderen);
		}
		return teVerwijderen;
	}

	public boolean bestaatBijlage(Bijlage bijlage)
	{
		for (AfspraakBijlage afspraakBijlage : getBijlagen())
		{
			if (afspraakBijlage.getBijlage().equals(bijlage))
				return true;
		}
		return false;
	}

	public AfspraakBijlage addBijlage(Bijlage bijlage)
	{
		AfspraakBijlage newBijlage = new AfspraakBijlage();
		newBijlage.setBijlage(bijlage);
		newBijlage.setAfspraak(this);

		getBijlagen().add(newBijlage);

		return newBijlage;
	}

	public Date getBeginDatumTijd()
	{
		return beginDatumTijd;
	}

	public void setBeginDatumTijd(Date beginDatumTijd)
	{
		this.beginDatumTijd = beginDatumTijd;
	}

	/**
	 * Geeft de begindatum terug. Dit is het deel van de begin datum en tijd dat alleen de
	 * datum bevat. De tijd wordt op 0:00 gezet.
	 * 
	 * @return de begindatum
	 */
	public Date getBeginDatum()
	{
		return TimeUtil.getInstance().asDate(getBeginDatumTijd());
	}

	/**
	 * Veranderd de begindatum. De tijd van de begin datum en tijd blijft ongewijzigd.
	 * 
	 * @param beginDatum
	 */
	public void setBeginDatum(Date beginDatum)
	{
		setBeginDatumTijd(TimeUtil.getInstance().setTimeOnDate(beginDatum, getBeginTijd()));
	}

	/**
	 * Geeft de begintijd terug. Dit is het deel van de begin datum en tijd dat alleen de
	 * tijd bevat. De datum wordt op de epoch gezet.
	 * 
	 * @return de begintijd
	 */
	public Time getBeginTijd()
	{
		if (getBeginDatumTijd() == null)
			return null;
		return new Time(TimeUtil.getInstance().asTime(getBeginDatumTijd()).getTime());
	}

	/**
	 * Veranderd de begintijd. De datum van de begin datum en tijd blijft ongewijzigd.
	 * 
	 * @param beginTijd
	 */
	public void setBeginTijd(Time beginTijd)
	{
		setBeginDatumTijd(TimeUtil.getInstance().setTimeOnDate(getBeginDatum(), beginTijd));
	}

	/**
	 * @return String met de begin/einddatum dan wel datum en begin/eindlesuur.
	 */
	public String getBeginEind()
	{
		StringBuilder res = new StringBuilder();
		if (getBeginLesuur() != null && getBeginLesuur() != 0)
		{
			res.append(TimeUtil.getInstance().formatDate(getBeginDatumTijd())).append(" ");
			if (getBeginLesuur().intValue() == getEindLesuur().intValue())
			{
				res.append(getBeginLesuur()).append("e uur ");
			}
			else
			{
				res.append(getBeginLesuur()).append("e t/m ").append(getEindLesuur()).append(
					"e uur");
			}
		}
		else
		{
			Date begindatum = TimeUtil.getInstance().asDate(getBeginDatumTijd());
			Date einddatum = TimeUtil.getInstance().asDate(getEindDatumTijd());
			if (begindatum.equals(einddatum))
			{
				res.append(TimeUtil.getInstance().formatDate(getBeginDatumTijd()));
				Time beginTijd = new Time(getBeginDatumTijd().getTime());
				Time eindTijd = new Time(getEindDatumTijd().getTime());
				if (beginTijd.before(eindTijd))
				{
					res.append(" ").append(beginTijd).append(" - ").append(eindTijd);
				}
			}
			else
			{
				res.append(TimeUtil.getInstance().formatDate(getBeginDatumTijd())).append(" t/m ")
					.append(TimeUtil.getInstance().formatDate(getEindDatumTijd()));
			}
		}
		return res.toString();
	}

	/**
	 * @return Returns the eindDatumTijd.
	 */
	public Date getEindDatumTijd()
	{
		return eindDatumTijd;
	}

	/**
	 * @param eindDatumTijd
	 *            The eindDatumTijd to set.
	 */
	public void setEindDatumTijd(Date eindDatumTijd)
	{
		this.eindDatumTijd = eindDatumTijd;
	}

	/**
	 * Geeft de einddatum terug. Dit is het deel van de eind datum en tijd dat alleen de
	 * datum bevat. De tijd wordt op 0:00 gezet.
	 * 
	 * @return de einddatum
	 */
	public Date getEindDatum()
	{
		return TimeUtil.getInstance().asDate(getEindDatumTijd());
	}

	/**
	 * Veranderd de einddatum. De tijd van de eind datum en tijd blijft ongewijzigd.
	 * 
	 * @param eindDatum
	 */
	public void setEindDatum(Date eindDatum)
	{
		setEindDatumTijd(TimeUtil.getInstance().setTimeOnDate(eindDatum, getEindTijd()));
	}

	/**
	 * Geeft de eindtijd terug. Dit is het deel van de eind datum en tijd dat alleen de
	 * tijd bevat. De datum wordt op de epoch gezet.
	 * 
	 * @return de eindtijd
	 */
	public Time getEindTijd()
	{
		if (getEindDatumTijd() == null)
			return null;
		return new Time(TimeUtil.getInstance().asTime(getEindDatumTijd()).getTime());
	}

	/**
	 * Veranderd de eindtijd. De datum van de eind datum en tijd blijft ongewijzigd.
	 * 
	 * @param eindTijd
	 */
	public void setEindTijd(Time eindTijd)
	{
		setEindDatumTijd(TimeUtil.getInstance().setTimeOnDate(getEindDatum(), eindTijd));
	}

	public Integer getBeginLesuur()
	{
		return beginLesuur;
	}

	public void setBeginLesuur(Integer beginLesuur)
	{
		this.beginLesuur = beginLesuur;
	}

	public Integer getEindLesuur()
	{
		return eindLesuur;
	}

	public void setEindLesuur(Integer eindLesuur)
	{
		this.eindLesuur = eindLesuur;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public int getMinutenIIVO()
	{
		return minutenIIVO;
	}

	public void setMinutenIIVO(int minutenIIVO)
	{
		this.minutenIIVO = minutenIIVO;
	}

	public boolean isPresentieRegistratieVerplicht()
	{
		return presentieRegistratieVerplicht;
	}

	public void setPresentieRegistratieVerplicht(boolean presentieRegistratieVerplicht)
	{
		this.presentieRegistratieVerplicht = presentieRegistratieVerplicht;
	}

	public boolean isPresentieRegistratieVerwerkt()
	{
		return presentieRegistratieVerwerkt;
	}

	public void setPresentieRegistratieVerwerkt(boolean presentieRegistratieVerwerkt)
	{
		this.presentieRegistratieVerwerkt = presentieRegistratieVerwerkt;
	}

	public void setPresentieRegistratieDoorDeelnemer(boolean presentieRegistratieDoorDeelnemer)
	{
		this.presentieRegistratieDoorDeelnemer = presentieRegistratieDoorDeelnemer;
	}

	public boolean isPresentieRegistratieDoorDeelnemer()
	{
		return presentieRegistratieDoorDeelnemer;
	}

	public String getAfspraakLocatie()
	{
		return afspraakLocatie;
	}

	public void setAfspraakLocatie(String afspraakLocatie)
	{
		this.afspraakLocatie = afspraakLocatie;
	}

	public String getExternId()
	{
		return externId;
	}

	public void setExternId(String externId)
	{
		this.externId = externId;
	}

	public ExternSysteem getExternSysteem()
	{
		return externSysteem;
	}

	public void setExternSysteem(ExternSysteem externSysteem)
	{
		this.externSysteem = externSysteem;
	}

	public HerhalendeAfspraak getHerhalendeAfspraak()
	{
		return herhalendeAfspraak;
	}

	public void setHerhalendeAfspraak(HerhalendeAfspraak herhalendeAfspraak)
	{
		this.herhalendeAfspraak = herhalendeAfspraak;
	}

	/**
	 * @return Een omschrijving van de eventuele herhaling van deze afspraak.
	 */
	public String getHerhalendeAfspraakStr()
	{
		if (getHerhalendeAfspraak() != null)
			return getHerhalendeAfspraak().toString();
		return "Geen herhaling";
	}

	public Basisrooster getBasisrooster()
	{
		return basisrooster;
	}

	public void setBasisrooster(Basisrooster basisrooster)
	{
		this.basisrooster = basisrooster;
	}

	public String getTitel()
	{
		return titel;
	}

	public void setTitel(String titel)
	{
		this.titel = titel;
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = onderwijsproduct;
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return onderwijsproduct;
	}

	public AfspraakType getAfspraakType()
	{
		return afspraakType;
	}

	public void setAfspraakType(AfspraakType afspraakType)
	{
		this.afspraakType = afspraakType;
	}

	public List<AfspraakParticipant> getParticipanten()
	{
		return participanten;
	}

	// Participanten As string voor Samenvoegvelden en pdf
	public String getParticipantenAsString()
	{
		StringBuilder participantenBuilder = new StringBuilder();
		for (AfspraakParticipant participant : getParticipanten())
		{
			participantenBuilder.append(participant.getPersoon().getVolledigeNaam());
			participantenBuilder.append(", ");
		}

		return participantenBuilder.toString();
	}

	public void setParticipanten(List<AfspraakParticipant> participanten)
	{
		this.participanten = participanten;
	}

	/**
	 * @return Toekomstig, Vandaag, Gisteren, Deze maand, Dit schooljaar, 2006/2007 etc.
	 */
	public String getGroepeerDatumOmschrijving()
	{
		return TimeUtil.getInstance().getDateGroup(getBeginDatumTijd());
	}

	/**
	 * Zoekt de AfspraakParticipant voor de gegeven participant (zoekt niet in groepen).
	 * 
	 * @param participant
	 * @return De AfspraakParticipant of null.
	 */
	public AfspraakParticipant getParticipant(IdObject participant)
	{
		for (AfspraakParticipant curParticipant : getParticipanten())
		{
			if (curParticipant.getParticipantEntiteit().equals(participant))
			{
				return curParticipant;
			}
		}
		return null;
	}

	/**
	 * Zoekt de AfspraakParticipant voor de gegeven participant (zoekt niet in groepen).
	 * 
	 * @param participant
	 * @return De AfspraakParticipant of null.
	 */
	public AfspraakParticipant getParticipantRecursive(IdObject participant)
	{
		for (AfspraakParticipant curParticipant : getParticipanten())
		{
			if (curParticipant.getParticipantEntiteit().equals(participant))
			{
				return curParticipant;
			}
			if (participant instanceof Deelnemer)
			{
				if (curParticipant.getGroep() != null)
				{
					curParticipant.getGroep();
					if (curParticipant.getGroep().getDeelnemersOpPeildatum().contains(participant))
						return curParticipant;
				}
				else if (curParticipant.getPersoonlijkeGroep() != null)
				{
					PersoonlijkeGroepDataAccessHelper helper =
						DataAccessRegistry.getHelper(PersoonlijkeGroepDataAccessHelper.class);
					if (helper.getDeelnemers(curParticipant.getPersoonlijkeGroep(),
						getBeginDatumTijd()).contains(participant))
						return curParticipant;
				}
			}
		}
		return null;
	}

	/**
	 * @return De afspraakparticipant die overeenkomt met de auteur van deze afspraak, of
	 *         null als de auteur niet aanwezig is bij de afspraak
	 */
	public AfspraakParticipant getAuteurParticipant()
	{
		for (AfspraakParticipant curParticipant : getParticipanten())
		{
			if (curParticipant.isAuteur())
				return curParticipant;
		}
		return null;
	}

	/**
	 * Berekent een extern id voor afspraken die uit gp-Untis zijn geimporteerd. Het id
	 * bestaat uit de identificerende elementen van de afspraak, te weten de datum/tijd en
	 * de medewerker (docent).
	 * 
	 * @return Het berekende id.
	 */
	public String berekenExternIdGpUntis()
	{
		TimeUtil tu = TimeUtil.getInstance();
		String begin = tu.formatDateTimeSystem(getBeginDatumTijd());
		String eind = tu.formatDateTimeSystem(getEindDatumTijd());
		StringBuilder id = new StringBuilder();
		for (AfspraakParticipant dln : getParticipanten())
		{
			if (dln.getMedewerker() != null)
			{
				id.append(dln.getMedewerker().getAfkorting()).append(";");
			}
		}
		if (getOnderwijsproduct() != null)
			id.append(getOnderwijsproduct().getCode()).append(";");
		if (StringUtil.isNotEmpty(getAfspraakLocatie()))
			id.append(getAfspraakLocatie()).append(";");
		return id.append(begin).append(eind).toString();
	}

	public Persoon getAuteur()
	{
		return auteur;
	}

	public void setAuteur(Persoon auteur)
	{
		this.auteur = auteur;
	}

	/**
	 * Voeg een bijlage toe aan deze afspraak
	 * 
	 * @param bijlage
	 */
	public void addBijlage(AfspraakBijlage bijlage)
	{
		getBijlagen().add(bijlage);
	}

	/**
	 * Verwijder de gegeven bijlage van deze afspraak
	 * 
	 * @param bijlage
	 */
	public void removeBijlage(AfspraakBijlage bijlage)
	{
		getBijlagen().remove(bijlage);
	}

	/**
	 * Voeg een participant toe aan deze afspraak
	 * 
	 * @param participant
	 */
	public void addParticipant(AfspraakParticipant participant)
	{
		getParticipanten().add(participant);
	}

	/**
	 * Geef een lijst met alle participanten bij deze afspraak. Dit is een lijst van
	 * deelnemers, groepen en medewerkers.
	 * 
	 * @return lijst met alle participanten
	 */
	public List<IdObject> getAlleParticipanten()
	{
		List<IdObject> ret = new ArrayList<IdObject>();
		for (AfspraakParticipant curParticipant : getParticipanten())
		{
			if (curParticipant.getGroep() != null)
			{
				curParticipant.getGroep();
				ret.addAll(curParticipant.getGroep().getDeelnemersOpPeildatumAsDeelnemer());
			}
			else if (curParticipant.getPersoonlijkeGroep() != null)
				ret.addAll(DataAccessRegistry.getHelper(PersoonlijkeGroepDataAccessHelper.class)
					.getDeelnemers(curParticipant.getPersoonlijkeGroep(), getBeginDatum()));
			else
				ret.add(curParticipant.getParticipantEntiteit());
		}
		return ret;
	}

	@Override
	public String getCssClass()
	{
		if (EnumSet.of(AfspraakTypeCategory.PRIVE, AfspraakTypeCategory.EXTERN,
			AfspraakTypeCategory.BESCHERMD).contains(getAfspraakType().getCategory()))
			return "purple-cell";
		if (!isPresentieRegistratieVerplicht())
			return "light_blue-cell";
		return "blue-cell";
	}

	@Override
	public String getTitle()
	{
		if (EnumSet.of(AfspraakTypeCategory.PRIVE, AfspraakTypeCategory.EXTERN,
			AfspraakTypeCategory.BESCHERMD).contains(getAfspraakType().getCategory()))
			return getAfspraakType().getCategory().toString();
		String title = getAfspraakType().getNaam();
		title += ", " + getTitel();
		title += " " + getBeginEind();
		if (getOmschrijving() != null)
			title += " " + getOmschrijving();
		return title;
	}

	@Override
	public boolean isActiefOpDatum(Date datum)
	{
		return (!getBeginDatumTijd().after(datum)) && getEindDatumTijd().after(datum);
	}

	/**
	 * Maak een afspraakparticipant die de auteur is van deze afspraak en voeg deze
	 * participant toe aan de afspraak.
	 * 
	 * @param editor
	 */
	public void createAuteurParticipant(IdObject editor)
	{
		AfspraakParticipant auteurParticipant = new AfspraakParticipant();
		auteurParticipant.setAfspraak(this);
		auteurParticipant.setUitnodigingStatus(UitnodigingStatus.DIRECTE_PLAATSING);
		auteurParticipant.setParticipantEntiteit(editor);
		this.getParticipanten().add(auteurParticipant);
	}

	/**
	 * Maak een afspraakparticipant voor het contextobject (de persoon waarvoor de agenda
	 * is geopend) en voeg deze participant toe aan de afspraak. De methode controleert of
	 * het contextobject gelijk is aan de editor. Indien dit het geval is, wordt geen
	 * nieuwe participant toegevoegd aan de afspraak.
	 * 
	 * @param editor
	 * @param contextParticipant
	 */
	public void createContextParticipant(IdObject editor, IdObject contextParticipant)
	{
		if (!contextParticipant.equals(editor))
		{
			AfspraakParticipant deelnemer = new AfspraakParticipant();
			deelnemer.setAfspraak(this);
			deelnemer.setParticipantEntiteit(contextParticipant);
			if (isDirectePlaatsingVerplicht(deelnemer, editor)
				|| isDirectePlaatsingAllowed(deelnemer))
				deelnemer.setUitnodigingStatus(UitnodigingStatus.DIRECTE_PLAATSING);
			else
				deelnemer.setUitnodigingStatus(UitnodigingStatus.UITGENODIGD);
			deelnemer.resetUitnodigingVerstuurd(true);
			this.getParticipanten().add(deelnemer);
		}
	}

	/**
	 * @param participant
	 * @return true indien directe plaatsing toegestaan is voor de gegeven participant.
	 */
	public static boolean isDirectePlaatsingAllowed(AfspraakParticipant participant)
	{
		if (participant.getMedewerker() != null)
			return new OrganisatieEenheidLocatieKoppelbaarSecurityCheck(new DataSecurityCheck(
				MEDEWERKER_WRITE), participant.getMedewerker()).isActionAuthorized(Enable.class);
		else if (participant.getDeelnemer() != null)
			return new DeelnemerSecurityCheck(new DataSecurityCheck(DEELNEMER_WRITE), participant
				.getDeelnemer()).isActionAuthorized(Enable.class);
		return participant.getExterne() != null;
	}

	/**
	 * @param participant
	 * @param editor
	 * @return true indien directe plaatsing verplicht is voor de gegeven participant
	 *         wanneer de gegeven editor de huidige editor is.
	 */
	public static boolean isDirectePlaatsingVerplicht(AfspraakParticipant participant,
			IdObject editor)
	{
		return participant.isAuteur() || participant.getGroep() != null
			|| participant.getExterne() != null || participant.getPersoonlijkeGroep() != null
			|| participant.getParticipantEntiteit().equals(editor);
	}

	/**
	 * @return beginLesuur t/m eindLesuur indien deze ingevuld zijn, en anders null.
	 */
	public String getLesurenOmschrijving()
	{
		if (getBeginLesuur() != null && getEindLesuur() != null)
		{
			if (getBeginLesuur().intValue() == getEindLesuur().intValue())
			{
				return getBeginLesuur().toString();
			}
			return getBeginLesuur() + " t/m " + getEindLesuur();
		}
		return null;
	}

	/**
	 * @param deelnemer
	 * @return De waarneming bij deze afspraak voor de gegeven deelnemer.
	 */
	public Waarneming getWaarneming(Deelnemer deelnemer)
	{
		for (Waarneming curWaarneming : getWaarnemingen())
		{
			if (curWaarneming.getDeelnemer().equals(deelnemer))
			{
				return curWaarneming;
			}
		}
		return null;
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;
	}

	public void setCacheRegion(CacheRegion cacheRegion)
	{
		this.cacheRegion = cacheRegion;
	}

	public CacheRegion getCacheRegion()
	{
		return cacheRegion;
	}

	public List<AfspraakDeelnemer> getDeelnemers()
	{
		return deelnemers;
	}

	/**
	 * @return Begin- en eindtijd in het formaat HH:mi t/m HH:mi
	 */
	public String getBeginEindTijdOmschrijving()
	{
		TimeUtil util = TimeUtil.getInstance();
		StringBuilder res = new StringBuilder();
		res.append(util.formatHourMinute(getBeginDatumTijd(), ":")).append(" t/m ").append(
			util.formatHourMinute(getEindDatumTijd(), ":"));
		return res.toString();
	}

	/**
	 * @return Begin- en eindtijd in het formaat HH:mi - HH:mi (n&gt;sup&lt;e&gt;/sup&lt -
	 *         ne lesuur)
	 */
	public String getBeginEindTijdEnLesuurOmschrijving()
	{
		return getBeginEindTijdEnLesuurOmschrijving(true);
	}

	/**
	 * @param showHtml
	 * @return Begin- en eindtijd in het formaat HH:mi - HH:mi (n&gt;sup&lt;e&gt;/sup&lt -
	 *         ne lesuur)
	 */
	public String getBeginEindTijdEnLesuurOmschrijving(boolean showHtml)
	{
		TimeUtil util = TimeUtil.getInstance();
		StringBuilder res = new StringBuilder();
		res.append(util.formatHourMinute(getBeginDatumTijd(), ":")).append(" - ").append(
			util.formatHourMinute(getEindDatumTijd(), ":"));
		if (beginLesuur != null && eindLesuur != null)
			if (showHtml)
				res.append(" (").append(beginLesuur).append("<sup>e</sup> - ").append(eindLesuur)
					.append("<sup>e</sup> lesuur)");
			else
				res.append(" (").append(beginLesuur).append("e - ").append(eindLesuur).append(
					"e lesuur)");
		return res.toString();
	}

	/**
	 * @return Een lijst met alle afspraakparticipanten die gekoppeld zijn aan een
	 *         medewerker.
	 */
	public List<AfspraakParticipant> getMedewerkerParticipanten()
	{
		List<AfspraakParticipant> res = new ArrayList<AfspraakParticipant>();
		for (AfspraakParticipant ap : getParticipanten())
		{
			if (ap.getMedewerker() != null)
			{
				res.add(ap);
			}
		}
		return res;
	}

	/**
	 * @return Een lijst met alle afspraakparticipanten die gekoppeld zijn aan een
	 *         medewerker.
	 */
	public List<AfspraakParticipant> getGroepParticipanten()
	{
		List<AfspraakParticipant> res = new ArrayList<AfspraakParticipant>();
		for (AfspraakParticipant ap : getParticipanten())
		{
			if (ap.getGroep() != null)
			{
				res.add(ap);
			}
		}
		return res;
	}

	/**
	 * @return Een comma-separated string met de namen van de docenten bij deze afspraak
	 */
	public String getGroepNamen()
	{
		List<AfspraakParticipant> groepen = getGroepParticipanten();
		StringBuilder res = new StringBuilder();
		for (AfspraakParticipant ap : groepen)
		{
			if (res.length() > 0)
				res.append(", ");
			res.append(ap.getGroep());
		}
		return res.toString();
	}

	/**
	 * @return Een comma-separated string met de namen van de docenten bij deze afspraak
	 */
	public String getDocentNamen()
	{
		List<AfspraakParticipant> docenten = getMedewerkerParticipanten();
		StringBuilder res = new StringBuilder();
		for (AfspraakParticipant ap : docenten)
		{
			if (res.length() > 0)
				res.append(", ");
			res.append(ap.getMedewerker().getPersoon().getVolledigeNaam());
		}
		return res.toString();
	}

	/**
	 * @return Een comma-separated string met de roostercodes van de docenten bij deze
	 *         afspraak
	 */
	public String getDocentRoostercodes()
	{
		List<AfspraakParticipant> docenten = getMedewerkerParticipanten();
		StringBuilder res = new StringBuilder();
		for (AfspraakParticipant ap : docenten)
		{
			if (ap.getMedewerker().getAfkorting() != null)
			{
				if (res.length() > 0)
					res.append(", ");
				res.append(ap.getMedewerker().getAfkorting());
			}
		}
		return res.toString();
	}

	/**
	 * @return de einddatum - de begindatum
	 */
	public long getDuurInSeconds()
	{
		if (getBeginDatumTijd() != null && getEindDatumTijd() != null)
		{
			long millis = getEindDatumTijd().getTime() - getBeginDatumTijd().getTime();
			return millis / 1000;
		}
		return 0;
	}

	/**
	 * @return aantal lesuren dat de afspraak duurt
	 */
	public int getDuurInLesuren()
	{
		if (getBeginLesuur() != null && getEindLesuur() != null)
			return (getEindLesuur() - getBeginLesuur()) + 1;
		return 0;

	}

	@Override
	public void delete()
	{
		// Verwijder alle afspraakparticipanten.
		for (AfspraakParticipant p : getParticipanten())
			p.delete();
		// Verwijder alle bijlagen
		for (AfspraakBijlage bijlage : getBijlagen())
		{
			bijlage.getBijlage().delete();
			bijlage.delete();
		}

		// dereference de waarnemingen
		for (Waarneming wng : getWaarnemingen())
		{
			wng.setAfspraak(null);
			wng.update();
		}
		// Verwijder de afspraak.
		super.delete();
	}

	public List<Waarneming> getWaarnemingen()
	{
		return waarnemingen;
	}

	public void setWaarnemingen(List<Waarneming> waarnemingen)
	{
		this.waarnemingen = waarnemingen;
	}

	public void setInloopCollege(InloopCollege inloopCollege)
	{
		this.inloopCollege = inloopCollege;
	}

	public InloopCollege getInloopCollege()
	{
		return inloopCollege;
	}

	public void setLocatie(Locatie locatie)
	{
		this.locatie = locatie;
	}

	public Locatie getLocatie()
	{
		return locatie;
	}

}
