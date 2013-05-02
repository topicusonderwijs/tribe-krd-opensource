package nl.topicus.eduarte.entities.participatie;

import java.util.Date;

import javax.persistence.*;

import nl.topicus.cobra.entities.Time;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.participatie.enums.IParticipatieBlokObject;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingSoort;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.providers.DeelnemerProvider;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Een waarneming geeft aan of een deelnemer op een gegeven moment wel/niet aanwezig is
 * geweest. Hierbij wordt geen reden opgegeven, alleen een indicatie dat de deelnemer
 * aan-/afwezig was.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class Waarneming extends InstellingEntiteit implements IParticipatieBlokObject,
		DeelnemerProvider
{
	private static final long serialVersionUID = 1L;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date beginDatumTijd;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = false)
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

	/**
	 * Geeft aan of de deelnemer aanwezig of afwezig was. Kan ook de speciale Nvt
	 * waarneming zijn. Dit betekent dat de deelnemer weliswaar was ingepland, maar
	 * hij/zij hoefde uiteindelijk toch niet op te komen dagen.
	 */
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private WaarnemingSoort waarnemingSoort;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = false)
	@Index(name = "idx_Waarneming_deelnemer")
	private Deelnemer deelnemer;

	/**
	 * Als een absentiewaarneming gekoppeld is aan een absentiemelding, is de waarneming
	 * per definitie afgehandeld. De afhandeling van de waarneming loopt dan namelijk via
	 * de absentiemelding.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "absentieMelding", nullable = true)
	@Index(name = "idx_Waarneming_absentieMelding")
	private AbsentieMelding absentieMelding;

	/**
	 * Een waarneming kan gekoppeld zijn aan een afspraak. Hiermee wordt aangegeven voor
	 * welke afspraak de waarneming geldt. De begin- en eindtijd van de waarneming moet
	 * dan binnen de begin- en eindtijd van de afspraak liggen (of gelijk zijn aan...)
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "afspraak", nullable = true)
	@Index(name = "idx_Waarneming_afspraak")
	private Afspraak afspraak;

	/**
	 * Het onderwijsproduct waarvoor de waarneming is. Dit zou overeen moeten komen met
	 * het onderwijsproduct van de afspraak, mits er een afspraak is.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "onderwijsproduct", nullable = true)
	@Index(name = "idx_Waarneming_product")
	private Onderwijsproduct onderwijsproduct;

	/**
	 * Een absentiewaarneming wordt standaard op afgehandeld=false gezet. Een
	 * presentiewaarneming of een nvt-waarneming gaat standaard op afgehandeld=true. Een
	 * absentiewaarneming die gekoppeld is aan een absentiemelding wordt daarmee standaard
	 * afgehandeld. Dit property blijft dan op afgehandeld=false staan, zodat als de
	 * absentiemelding aangepast wordt, de waarneming weer terugkomt in het bakje met
	 * openstaande waarnemingen.
	 */
	@Column(nullable = false)
	private boolean afgehandeld;

	/**
	 * Default constructor voor Hibernate.
	 */
	public Waarneming()
	{
	}

	/**
	 * @param waarneming
	 */
	public Waarneming(Waarneming waarneming)
	{
		setBeginDatumTijd(waarneming.getBeginDatumTijd());
		setEindDatumTijd(waarneming.getEindDatumTijd());
		setBeginLesuur(waarneming.getBeginLesuur());
		setEindLesuur(waarneming.getEindLesuur());
		setWaarnemingSoort(waarneming.getWaarnemingSoort());
		setDeelnemer(waarneming.getDeelnemer());
		setAbsentieMelding(waarneming.getAbsentieMelding());
		setAfspraak(waarneming.getAfspraak());
		setAfgehandeld(waarneming.isAfgehandeld());
	}

	/**
	 * @return begin en eindtijd
	 */
	public String getBeginEnEindTijd()
	{
		StringBuilder res = new StringBuilder();
		Time beginTijd = new Time(getBeginDatumTijd().getTime());
		Time eindTijd = new Time(getEindDatumTijd().getTime());
		if (beginTijd.before(eindTijd))
		{
			res.append(" ").append(beginTijd).append(" - ").append(eindTijd);
		}
		return res.toString();

	}

	/**
	 * @return String met de begin/einddatum dan wel datum en begin/eindlesuur.
	 */
	public String getBeginEind()
	{
		return getBeginEind(true);
	}

	/**
	 * @return String met de begin/eindtijd dan wel datum en begin/eindlesuur.
	 */
	public String getBeginEindZonderDatum()
	{
		return getBeginEind(false);
	}

	/**
	 * @return String met de begin/einddatum dan wel datum en begin/eindlesuur.
	 */
	private String getBeginEind(boolean metDatum)
	{
		StringBuilder res = new StringBuilder();
		if (getBeginLesuur() != null && getBeginLesuur() != 0)
		{
			if (metDatum)
			{
				res.append(TimeUtil.getInstance().formatDate(getBeginDatumTijd())).append(" ");
			}
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
				if (metDatum)
				{
					res.append(TimeUtil.getInstance().formatDate(getBeginDatumTijd()));
				}
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
	 * @return Ja indien afgehandeld, en anders Nee.
	 */
	public String getAfgehandeldOmschrijving()
	{
		return isAfgehandeld() ? "Ja" : "Nee";
	}

	/**
	 * @return Toekomstig, Vandaag, Gisteren, Deze maand, Dit schooljaar, 2006/2007 etc.
	 */
	public String getGroepeerDatumOmschrijving()
	{
		return TimeUtil.getInstance().getDateGroup(getBeginDatumTijd());
	}

	/**
	 * @return Returns the beginDatumTijd.
	 */
	public Date getBeginDatumTijd()
	{
		return beginDatumTijd;
	}

	/**
	 * @param beginDatumTijd
	 *            The beginDatumTijd to set.
	 */
	public void setBeginDatumTijd(Date beginDatumTijd)
	{
		this.beginDatumTijd = beginDatumTijd;
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
	 * @return Returns the beginLesuur.
	 */
	public Integer getBeginLesuur()
	{
		return beginLesuur;
	}

	/**
	 * @param beginLesuur
	 *            The beginLesuur to set.
	 */
	public void setBeginLesuur(Integer beginLesuur)
	{
		this.beginLesuur = beginLesuur;
	}

	/**
	 * @return Returns the eindLesuur.
	 */
	public Integer getEindLesuur()
	{
		return eindLesuur;
	}

	/**
	 * @param eindLesuur
	 *            The eindLesuur to set.
	 */
	public void setEindLesuur(Integer eindLesuur)
	{
		this.eindLesuur = eindLesuur;
	}

	/**
	 * @return Returns the waarnemingSoort.
	 */
	public WaarnemingSoort getWaarnemingSoort()
	{
		return waarnemingSoort;
	}

	/**
	 * @param waarnemingSoort
	 *            The waarnemingSoort to set.
	 */
	public void setWaarnemingSoort(WaarnemingSoort waarnemingSoort)
	{
		this.waarnemingSoort = waarnemingSoort;
	}

	/**
	 * @return Returns the deelnemer.
	 */
	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	/**
	 * @param deelnemer
	 *            The deelnemer to set.
	 */
	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	/**
	 * @return Returns the absentieMelding.
	 */
	public AbsentieMelding getAbsentieMelding()
	{
		return absentieMelding;
	}

	/**
	 * @param absentieMelding
	 *            The absentieMelding to set.
	 */
	public void setAbsentieMelding(AbsentieMelding absentieMelding)
	{
		this.absentieMelding = absentieMelding;
	}

	/**
	 * @return Returns the afspraak.
	 */
	public Afspraak getAfspraak()
	{
		return afspraak;
	}

	/**
	 * @param afspraak
	 *            The afspraak to set.
	 */
	public void setAfspraak(Afspraak afspraak)
	{
		this.afspraak = afspraak;
	}

	/**
	 * @return Returns the afgehandeld.
	 */
	public boolean isAfgehandeld()
	{
		return afgehandeld;
	}

	/**
	 * @param afgehandeld
	 *            The afgehandeld to set.
	 */
	public void setAfgehandeld(boolean afgehandeld)
	{
		this.afgehandeld = afgehandeld;
	}

	@Override
	public String getCssClass()
	{
		String cssClass = "green-cell";
		if (this.getAfspraak() == null)
			cssClass = "light_green_cell";
		if (getWaarnemingSoort().equals(WaarnemingSoort.Afwezig))
		{
			cssClass = "red-cell";
			if (getAbsentieMelding() != null
				&& getAbsentieMelding().getAbsentieReden().isGeoorloofd())
				cssClass = "orange-cell";
		}
		return cssClass;
	}

	@Override
	public String getTitle()
	{
		String title = getWaarnemingSoort().toString();
		title += " " + getBeginEind();
		return title;
	}

	@Override
	public boolean isActiefOpDatum(Date datum)
	{
		return (!getBeginDatumTijd().after(datum)) && getEindDatumTijd().after(datum);
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
	 * @return aantal lesuren dat de waarneming duurt
	 */
	public int getDuurInLesuren()
	{
		if (getBeginLesuur() != null && getEindLesuur() != null)
			return (getEindLesuur() - getBeginLesuur()) + 1;
		return 0;

	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return onderwijsproduct;
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = onderwijsproduct;
	}
}
