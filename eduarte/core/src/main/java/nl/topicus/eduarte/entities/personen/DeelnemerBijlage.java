package nl.topicus.eduarte.entities.personen;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.bijlage.BijlageEntiteit;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppeltabel tussen deelnemer en bijlage
 * 
 * @author hoeve
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class DeelnemerBijlage extends BijlageEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(nullable = true, name = "deelnemer")
	@Index(name = "idx_DeelBijlage_deelnemer")
	private Deelnemer deelnemer;

	public DeelnemerBijlage()
	{
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	@Override
	public IBijlageKoppelEntiteit< ? extends DeelnemerBijlage> getEntiteit()
	{
		return getDeelnemer();
	}
}
