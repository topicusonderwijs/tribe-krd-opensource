package nl.topicus.eduarte.entities.landelijk;

import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.LandelijkEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "Landelijk")
public class Voorvoegsel extends LandelijkEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(length = 50, nullable = false)
	@Index(name = "idx_Voorvoegsel_naam")
	private String naam;

	/**
	 * Default constructor voor Hibernate.
	 */
	public Voorvoegsel()
	{
	}

	/**
	 * @return Returns the naam.
	 */
	public String getNaam()
	{
		return naam;
	}

	/**
	 * @param naam
	 *            The naam to set.
	 */
	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	/**
	 * @see nl.topicus.eduarte.entities.Entiteit#toString()
	 */
	@Override
	public String toString()
	{
		return getNaam();
	}
}
