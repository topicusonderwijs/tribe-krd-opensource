package nl.topicus.eduarte.entities.productregel;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.LandelijkEntiteit;
import nl.topicus.eduarte.entities.taxonomie.Deelgebied;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Landelijke koppeltabel tussen landelijke productregels en deelgebieden die aangeeft
 * welke deelgebieden toegestaan zijn voor een bepaalde productregel.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class ToegestaanDeelgebied extends LandelijkEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "productregel")
	@Index(name = "idx_ToegDeel_regel")
	private Productregel productregel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "deelgebied")
	@Index(name = "idx_ToegDeel_deelgebied")
	private Deelgebied deelgebied;

	public ToegestaanDeelgebied()
	{
	}

	public Productregel getProductregel()
	{
		return productregel;
	}

	public void setProductregel(Productregel productregel)
	{
		this.productregel = productregel;
	}

	public Deelgebied getDeelgebied()
	{
		return deelgebied;
	}

	public void setDeelgebied(Deelgebied deelgebied)
	{
		this.deelgebied = deelgebied;
	}
}
