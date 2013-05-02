package nl.topicus.eduarte.entities.dbs.bijlagen;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.eduarte.entities.bijlage.BijlageEntiteit;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.dbs.incident.IrisIncident;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Bijlage die gekoppeld is aan een irisincident.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class IrisIncidentBijlage extends BijlageEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@Basic(optional = false)
	@JoinColumn(name = "irisIncident", nullable = true)
	@Index(name = "idx_Bijlage_irisInc")
	@IgnoreInGebruik
	private IrisIncident irisIncident;

	@Override
	public IBijlageKoppelEntiteit<IrisIncidentBijlage> getEntiteit()
	{
		return getIrisIncident();
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
