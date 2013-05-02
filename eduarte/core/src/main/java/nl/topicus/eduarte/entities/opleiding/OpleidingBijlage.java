package nl.topicus.eduarte.entities.opleiding;

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
 * Koppeltabel tussen opleiding en bijlage
 * 
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class OpleidingBijlage extends BijlageEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "opleiding")
	@Index(name = "idx_OplBijlage_opleiding")
	private Opleiding opleiding;

	public OpleidingBijlage()
	{
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = opleiding;
	}

	public Opleiding getOpleiding()
	{
		return opleiding;
	}

	@Override
	public IBijlageKoppelEntiteit<OpleidingBijlage> getEntiteit()
	{
		return getOpleiding();
	}
}
