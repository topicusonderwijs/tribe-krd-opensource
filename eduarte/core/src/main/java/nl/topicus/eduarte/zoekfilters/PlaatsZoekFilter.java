package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.landelijk.Gemeente;
import nl.topicus.eduarte.entities.landelijk.Plaats;
import nl.topicus.eduarte.entities.landelijk.Provincie;

/**
 * @author hop
 */
public class PlaatsZoekFilter extends AbstractZoekFilter<Plaats>
{
	private static final long serialVersionUID = 1L;

	private String naam;

	private Gemeente gemeente;

	private Provincie provincie;

	private Boolean uniek;

	/**
	 * Constructor
	 */
	public PlaatsZoekFilter()
	{
		super();
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

	public void setGemeente(Gemeente gemeente)
	{
		this.gemeente = gemeente;
	}

	public Gemeente getGemeente()
	{
		return gemeente;
	}

	public void setProvincie(Provincie provincie)
	{
		this.provincie = provincie;
	}

	public Provincie getProvincie()
	{
		return provincie;
	}

	public void setUniek(Boolean uniek)
	{
		this.uniek = uniek;
	}

	public Boolean getUniek()
	{
		return uniek;
	}
}
