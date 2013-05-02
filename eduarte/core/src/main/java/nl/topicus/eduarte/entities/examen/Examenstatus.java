package nl.topicus.eduarte.entities.examen;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Status van een examendeelname. Voor VO/VAVO en MBO/CGO zijn er landelijke statussen
 * gedefinieerd. Voor andere taxonomien kunnen er lokale statussen gedefinieerd worden.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class Examenstatus extends LandelijkOfInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private int volgnummer;

	@Column(nullable = false, length = 50)
	private String naam;

	/**
	 * Per examenworkflow moet er precies 1 beginstatus gedefinieerd worden.
	 */
	@Column(nullable = false)
	private boolean beginstatus;

	/**
	 * Per examenworkflow kunnen meerdere examenstatussen als eindstatus aangemerkt
	 * worden. Een eindstatus geeft aan dat de examendeelname hiermee in principe
	 * beeindigd is, en dat een nieuwe examendeelname aangemaakt mag worden voor het
	 * verbintenis.
	 */
	@Column(nullable = false)
	private boolean eindstatus;

	/**
	 * Deze status geeft aan dat de deelnemer voor dit examenonderdeel geslaagd is.
	 * Hiermee kan namelijk in de software geredeneerd worden over de examendeelnames,
	 * bijvoorbeeld of een examendeelname naar BRON toe moet of niet. Is niet specifiek
	 * voor &eacute;&eacute;n examendeelname, maar voor deze status (die gebruikt wordt
	 * bij meerdere deelnames).
	 */
	@Column(nullable = false)
	private boolean geslaagd;

	/**
	 * Boolean die aangeeft of een criteriumbankcontrole verplicht is om deze status te
	 * bereiken. Bij statusovergangen die deze status als naarstatus hebben moet een
	 * criteriumbankcontrole uitgevoerd worden.
	 */
	@Column(nullable = false)
	private boolean criteriumbankControle;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "examenWorkflow", nullable = false)
	@Index(name = "idx_ExStatus_workflow")
	private ExamenWorkflow examenWorkflow;

	public Examenstatus()
	{
	}

	public Examenstatus(EntiteitContext context)
	{
		super(context);
	}

	/**
	 * Constructor
	 * 
	 * @param examenWorkflow
	 */
	public Examenstatus(ExamenWorkflow examenWorkflow)
	{
		super(examenWorkflow.isLandelijk());
		setExamenWorkflow(examenWorkflow);
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
	 * @return Returns the naam.
	 */
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
	 * @return Returns the beginstatus.
	 */
	public boolean isBeginstatus()
	{
		return beginstatus;
	}

	/**
	 * @param beginstatus
	 *            The beginstatus to set.
	 */
	public void setBeginstatus(boolean beginstatus)
	{
		this.beginstatus = beginstatus;
	}

	/**
	 * @return Returns the eindstatus.
	 */
	public boolean isEindstatus()
	{
		return eindstatus;
	}

	/**
	 * @param eindstatus
	 *            The eindstatus to set.
	 */
	public void setEindstatus(boolean eindstatus)
	{
		this.eindstatus = eindstatus;
	}

	public boolean isAfgewezen()
	{
		return "Afgewezen".equalsIgnoreCase(getNaam());
	}

	public boolean isTeruggetrokken()
	{
		return "Teruggetrokken".equalsIgnoreCase(getNaam());
	}

	public boolean isGespreidExamen()
	{
		return "Gespreid examen".equalsIgnoreCase(getNaam());
	}

	public boolean isCertificaten()
	{
		return "Certificaten".equalsIgnoreCase(getNaam())
			|| "Schoolverklaring".equalsIgnoreCase(getNaam());
	}

	public boolean isVerwijderd()
	{
		return "Verwijderd".equalsIgnoreCase(getNaam());
	}

	public boolean isAangemeld()
	{
		return "Aangemeld".equalsIgnoreCase(getNaam());
	}

	public boolean isVoorlopigGeslaagd()
	{
		return "Voorlopig geslaagd".equalsIgnoreCase(getNaam());
	}

	public boolean isVoorlopigAfgewezen()
	{
		return "Voorlopig afgewezen".equalsIgnoreCase(getNaam());
	}

	/**
	 * @return Returns the criteriumbankControle.
	 */
	public boolean isCriteriumbankControle()
	{
		return criteriumbankControle;
	}

	/**
	 * @param criteriumbankControle
	 *            The criteriumbankControle to set.
	 */
	public void setCriteriumbankControle(boolean criteriumbankControle)
	{
		this.criteriumbankControle = criteriumbankControle;
	}

	/**
	 * 
	 * @return Een lijst met de toegestane statusovergangen voor deze examendeelname.
	 */
	public List<ToegestaneExamenstatusOvergang> getToegestaneExamenstatusOvergangen()
	{
		List<ToegestaneExamenstatusOvergang> res = new ArrayList<ToegestaneExamenstatusOvergang>();
		for (ToegestaneExamenstatusOvergang toeg : getExamenWorkflow()
			.getToegestaneExamenstatusOvergangen())
		{
			for (ToegestaneBeginstatus begin : toeg.getToegestaneBeginstatussen())
			{
				if (this.equals(begin.getExamenstatus()))
				{
					res.add(toeg);
					break;
				}
			}
		}
		return res;
	}

	public boolean isGeslaagd()
	{
		return geslaagd;
	}

	public void setGeslaagd(boolean geslaagd)
	{
		this.geslaagd = geslaagd;
	}

	@Override
	public String toString()
	{
		return getNaam();
	}

	public static boolean isNieuwVoorBronBo(Examenstatus vorige, Examenstatus huidige)
	{
		boolean vorigeNietGeslaagd = vorige == null || !vorige.isGeslaagd();
		boolean huidigeGeslaagd = huidige != null && huidige.isGeslaagd();

		return vorigeNietGeslaagd && huidigeGeslaagd;
	}

	public static boolean moetUitBronVerwijderdWordenBo(Examenstatus vorigeStatus,
			Examenstatus nieuweStatus)
	{
		boolean vorigeGeslaagd = vorigeStatus != null && vorigeStatus.isGeslaagd();
		boolean nieuweNietGeslaagd = nieuweStatus == null || !nieuweStatus.isGeslaagd();

		return vorigeGeslaagd && nieuweNietGeslaagd;
	}
}
