package nl.topicus.eduarte.entities.organisatie;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.bijlage.Bijlage;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppeltabel tussen instelling en bijlage
 * 
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class InstellingsLogo extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "bijlage")
	@Index(name = "idx_InstellingsLogo_bijlage")
	private Bijlage logo;

	public InstellingsLogo()
	{
	}

	public void setLogo(Bijlage logo)
	{
		this.logo = logo;
	}

	public Bijlage getLogo()
	{
		return logo;
	}

}
