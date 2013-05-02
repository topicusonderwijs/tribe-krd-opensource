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
 * Koppeltabel tussen leerpunt en competentiecomponent.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class LeerpuntComponent extends LandelijkEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "leerpunt")
	@Index(name = "idx_leerpuntComp_lp")
	private Leerpunt leerpunt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "competentieComponent")
	@Index(name = "idx_leerpuntComp_comp")
	private CompetentieComponent competentieComponent;

	/**
	 * Default constructor
	 */
	public LeerpuntComponent()
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
	 * @return Returns the competentieComponent.
	 */
	public CompetentieComponent getCompetentieComponent()
	{
		return competentieComponent;
	}

	/**
	 * @param competentieComponent
	 *            The competentieComponent to set.
	 */
	public void setCompetentieComponent(CompetentieComponent competentieComponent)
	{
		this.competentieComponent = competentieComponent;
	}

}
