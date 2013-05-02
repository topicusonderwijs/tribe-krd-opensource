package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.LandelijkEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppeltabel tussen leerpunt en vaardigheid.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class LeerpuntVaardigheid extends LandelijkEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "leerpunt")
	@Index(name = "idx_leerpuntVaar_lp")
	private Leerpunt leerpunt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "vaardigheid")
	@Index(name = "idx_leerpuntVaar_vaard")
	private Vaardigheid vaardigheid;

	/**
	 * Default constructor
	 */
	public LeerpuntVaardigheid()
	{
	}

	/**
	 * @return Returns the leerpunt.
	 */
	public Leerpunt getLeerpunt()
	{
		return leerpunt;
	}

	/**
	 * @param leerpunt
	 *            The leerpunt to set.
	 */
	public void setLeerpunt(Leerpunt leerpunt)
	{
		this.leerpunt = leerpunt;
	}

	/**
	 * @return Returns the vaardigheid.
	 */
	public Vaardigheid getVaardigheid()
	{
		return vaardigheid;
	}

	/**
	 * @param vaardigheid
	 *            The vaardigheid to set.
	 */
	public void setVaardigheid(Vaardigheid vaardigheid)
	{
		this.vaardigheid = vaardigheid;
	}

}
