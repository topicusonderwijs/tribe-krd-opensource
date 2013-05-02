package nl.topicus.eduarte.entities.dbs.bijlagen;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.dbs.gedrag.Incident;
import nl.topicus.eduarte.entities.personen.DeelnemerBijlage;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Bijlage die gekoppeld is aan een incident en een deelnemer.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class IncidentBijlage extends DeelnemerBijlage
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@Basic(optional = false)
	@JoinColumn(name = "incident", nullable = true)
	@Index(name = "idx_Bijlage_incident")
	private Incident incident;

	public Incident getIncident()
	{
		return incident;
	}

	public void setIncident(Incident incident)
	{
		this.incident = incident;
	}

	@Override
	public IBijlageKoppelEntiteit<IncidentBijlage> getEntiteit()
	{
		return getIncident();
	}
}
