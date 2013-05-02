package nl.topicus.eduarte.entities.dbs.gedrag;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppeling tussen incident en medewerker.
 * 
 * @author harmsen
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BetrokkenMedewerker extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medewerker", nullable = false)
	@Index(name = "idx_BetrokkenMed_medewerker")
	private Medewerker medewerker;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "incident", nullable = false)
	@Index(name = "idx_BetrokkenMed_incident")
	@IgnoreInGebruik
	private Incident incident;

	public BetrokkenMedewerker()
	{
	}

	public BetrokkenMedewerker(Incident incident, Medewerker medewerker)
	{
		setIncident(incident);
		setMedewerker(medewerker);
	}

	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
	}

	public Incident getIncident()
	{
		return incident;
	}

	public void setIncident(Incident incident)
	{
		this.incident = incident;
	}
}
