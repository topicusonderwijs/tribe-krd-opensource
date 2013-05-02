package nl.topicus.eduarte.entities.dbs.gedrag;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.eduarte.entities.dbs.NietTonenInZorgvierkant;
import nl.topicus.eduarte.entities.dbs.incident.IrisIncident;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class IrisIncidentNietTonenInZorgvierkant extends NietTonenInZorgvierkant
{
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "irisIncident", nullable = true)
	@Index(name = "idx_TonenInZorgvierkant_incident")
	@IgnoreInGebruik
	private IrisIncident irisIncident;

	public IrisIncidentNietTonenInZorgvierkant()
	{
	}

	public IrisIncidentNietTonenInZorgvierkant(IrisIncident irisIncident, Medewerker medewerker)
	{
		super(medewerker);
		setIrisIncident(irisIncident);
	}

	public void setIrisIncident(IrisIncident irisIncident)
	{
		this.irisIncident = irisIncident;
	}

	public IrisIncident getIrisIncident()
	{
		return irisIncident;
	}

}
