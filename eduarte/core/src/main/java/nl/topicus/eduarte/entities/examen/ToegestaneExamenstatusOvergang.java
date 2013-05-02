package nl.topicus.eduarte.entities.examen;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Toegestane statusovergang tussen verschillende examenstatussen. Voor VO/VAVO en CGO/MBO
 * zijn deze landelijk gedefinieerd.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
@javax.persistence.Table(name = "ToegExamenstatusOvergang")
public class ToegestaneExamenstatusOvergang extends LandelijkOfInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	/**
	 * Volgnummer om de statusovergangen in een logische volgorde op het scherm te kunnen
	 * tonen.
	 */
	@Column(nullable = false)
	private int volgnummer;

	/**
	 * Omschrijving van de actie die uitgevoerd wordt bij de statusovergang, bijvoorbeeld
	 * 'Uitslag bepalen'.
	 */
	@Column(nullable = false, length = 100)
	private String actie;

	/**
	 * Omschrijving van de actie wanneer deze voor een individuele deelnemer wordt
	 * uitgevoerd.
	 */
	@Column(nullable = false, length = 100)
	private String actieIndividueel;

	/**
	 * Omschrijving van de actie wanneer deze voor een individuele deelnemer wordt
	 * uitgevoerd, en dan in het kort, zodat deze goed op de knoppen past
	 */
	@Column(nullable = true, length = 100)
	private String actieIndividueelKort;

	/**
	 * Boolean die aangeeft of bij deze statusovergang examennummers toegekend moeten
	 * worden. Dit is geen echte statusovergang omdat de status van de examendeelname niet
	 * wijzigt. Dit is ook de enige soort statusovergang waarbij de naarStatus niet
	 * verplicht is (mag niet ingevuld worden).
	 */
	@Column(nullable = false)
	private boolean examennummersToekennen;

	/**
	 * Boolean die aangeeft of bij deze statusovergang een tijdvak geselecteerd moet
	 * worden.
	 */
	@Column(nullable = false)
	private boolean tijdvakAangeven;

	/**
	 * Boolean die aangeeft of bij deze statusovergang opmerkingen handmatig ingevoerd
	 * moeten kunnen worden. Dit is vooral van belang van statusovergangen zoals Kandidaat
	 * -> Goedgekeurd door examencommisssie. Hierbij is het wenselijk om een opmerking in
	 * te kunnen voeren voor de deelnemers die uitvallen.
	 */
	@Column(nullable = false)
	private boolean handmatigOpmerkingenInvoeren;

	/**
	 * Boolean die aangeeft of deze statusovergang ook de statusovergang is die de
	 * standaard datum uitslag moet zetten.
	 */
	@Column(nullable = false)
	private boolean bepaaltDatumUitslag;

	/**
	 * De workflow waarvoor deze statusovergang geldt. Is in principe redundant omdat dit
	 * ook altijd te herleiden is uit de naarStatus.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "examenWorkflow")
	@Index(name = "idx_toegExOvergang_flow")
	private ExamenWorkflow examenWorkflow;

	/**
	 * De toegestane beginstatussen voor deze statusovergang.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "toegestaneExamenstatusOvergang")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
	private List<ToegestaneBeginstatus> toegestaneBeginstatussen;

	/**
	 * De examenstatus waarnaar de examendeelnames overgezet moeten worden bij het
	 * uitvoeren van deze statusovergang. Dit is de doelstatus als de overgang gelukt is.
	 * Als de statusovergang voor een examendeelname mislukt, wordt de afgewezenStatus op
	 * de examendeelname gezet. De naarStatus mag alleen null zijn als de boolean
	 * examennummersToekennen op true gezet is. In dat geval *MOET* de naarStatus null
	 * zijn.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "naarExamenstatus")
	@Index(name = "idx_toegExOvergang_naar")
	private Examenstatus naarExamenstatus;

	/**
	 * De status die gezet wordt op de examendeelname als de statusovergang mislukt. Het
	 * mislukken kan op twee manieren: Als bij de statusovergang een criteriumbankcontrole
	 * uitgevoerd moet worden, wordt de afgewezenstatus gezet op de examendeelnames die
	 * niet door de controle heen komen. Als het om een handmatige statusovergang gaat,
	 * kan de gebruiker zelf kiezen welke status de verschillende examendeelnames moeten
	 * krijgen.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "afgewezenExamenstatus")
	@Index(name = "idx_toegExOvergang_afg")
	private Examenstatus afgewezenExamenstatus;

	/**
	 * Default constructor voor Hibernate.
	 */
	public ToegestaneExamenstatusOvergang()
	{
	}

	public ToegestaneExamenstatusOvergang(EntiteitContext context)
	{
		super(context);
	}

	/**
	 * Constructor
	 * 
	 * @param examenWorkflow
	 */
	public ToegestaneExamenstatusOvergang(ExamenWorkflow examenWorkflow)
	{
		super(examenWorkflow.isLandelijk());
		setExamenWorkflow(examenWorkflow);
	}

	/**
	 * @return Returns the actie.
	 */
	public String getActie()
	{
		return actie;
	}

	/**
	 * @param actie
	 *            The actie to set.
	 */
	public void setActie(String actie)
	{
		this.actie = actie;
	}

	/**
	 * @return Returns the toegestaneBeginstatussen.
	 */
	public List<ToegestaneBeginstatus> getToegestaneBeginstatussen()
	{
		if (toegestaneBeginstatussen == null)
			toegestaneBeginstatussen = new ArrayList<ToegestaneBeginstatus>();
		return toegestaneBeginstatussen;
	}

	/**
	 * @param toegestaneBeginstatussen
	 *            The toegestaneBeginstatussen to set.
	 */
	public void setToegestaneBeginstatussen(List<ToegestaneBeginstatus> toegestaneBeginstatussen)
	{
		this.toegestaneBeginstatussen = toegestaneBeginstatussen;
	}

	/**
	 * @return Returns the naarExamenstatus.
	 */
	public Examenstatus getNaarExamenstatus()
	{
		return naarExamenstatus;
	}

	/**
	 * @param naarExamenstatus
	 *            The naarExamenstatus to set.
	 */
	public void setNaarExamenstatus(Examenstatus naarExamenstatus)
	{
		this.naarExamenstatus = naarExamenstatus;
	}

	/**
	 * @return Returns the afgewezenExamenstatus.
	 */
	public Examenstatus getAfgewezenExamenstatus()
	{
		return afgewezenExamenstatus;
	}

	/**
	 * @param afgewezenExamenstatus
	 *            The afgewezenExamenstatus to set.
	 */
	public void setAfgewezenExamenstatus(Examenstatus afgewezenExamenstatus)
	{
		this.afgewezenExamenstatus = afgewezenExamenstatus;
	}

	/**
	 * @return Returns the examenWorkflow.
	 */
	public ExamenWorkflow getExamenWorkflow()
	{
		return examenWorkflow;
	}

	/**
	 * @param examenWorkflow
	 *            The examenWorkflow to set.
	 */
	public void setExamenWorkflow(ExamenWorkflow examenWorkflow)
	{
		this.examenWorkflow = examenWorkflow;
	}

	/**
	 * @return Returns the volgnummer.
	 */
	public int getVolgnummer()
	{
		return volgnummer;
	}

	/**
	 * @param volgnummer
	 *            The volgnummer to set.
	 */
	public void setVolgnummer(int volgnummer)
	{
		this.volgnummer = volgnummer;
	}

	/**
	 * @return Returns the examennummersToekennen.
	 */
	public boolean isExamennummersToekennen()
	{
		return examennummersToekennen;
	}

	/**
	 * @param examennummersToekennen
	 *            The examennummersToekennen to set.
	 */
	public void setExamennummersToekennen(boolean examennummersToekennen)
	{
		this.examennummersToekennen = examennummersToekennen;
	}

	/**
	 * @return Returns the tijdvakAangeven.
	 */
	public boolean isTijdvakAangeven()
	{
		return tijdvakAangeven;
	}

	/**
	 * @param tijdvakAangeven
	 *            The tijdvakAangeven to set.
	 */
	public void setTijdvakAangeven(boolean tijdvakAangeven)
	{
		this.tijdvakAangeven = tijdvakAangeven;
	}

	/**
	 * @return Returns the handmatigOpmerkingenInvoeren.
	 */
	public boolean isHandmatigOpmerkingenInvoeren()
	{
		return handmatigOpmerkingenInvoeren;
	}

	/**
	 * @param handmatigOpmerkingenInvoeren
	 *            The handmatigOpmerkingenInvoeren to set.
	 */
	public void setHandmatigOpmerkingenInvoeren(boolean handmatigOpmerkingenInvoeren)
	{
		this.handmatigOpmerkingenInvoeren = handmatigOpmerkingenInvoeren;
	}

	public String getActieIndividueel()
	{
		return actieIndividueel;
	}

	public void setActieIndividueel(String actieIndividueel)
	{
		this.actieIndividueel = actieIndividueel;
	}

	/**
	 * 
	 * @param mogelijkeBeginstatus
	 * @return true als deze examenstatusovergang toegestaan is met de gegeven
	 *         examenstatus als beginstatus.
	 */
	public boolean isToegestaanVanafExamenstatus(Examenstatus mogelijkeBeginstatus)
	{
		if (mogelijkeBeginstatus == null)
			return false;
		for (ToegestaneBeginstatus beginstatus : getToegestaneBeginstatussen())
		{
			if (mogelijkeBeginstatus.equals(beginstatus.getExamenstatus()))
			{
				return true;
			}
		}
		return false;
	}

	public boolean isBepaaltDatumUitslag()
	{
		return bepaaltDatumUitslag;
	}

	public void setBepaaltDatumUitslag(boolean bepaaltDatumUitslag)
	{
		this.bepaaltDatumUitslag = bepaaltDatumUitslag;
	}

	public void setActieIndividueelKort(String actieIndividueelKort)
	{
		this.actieIndividueelKort = actieIndividueelKort;
	}

	public String getActieIndividueelKort()
	{
		return actieIndividueelKort;
	}

}
