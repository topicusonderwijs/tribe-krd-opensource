package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.landelijk.Voorvoegsel;

/**
 * @author harmsen
 */
public class VoorvoegselZoekFilter extends AbstractZoekFilter<Voorvoegsel>
{
	private static final long serialVersionUID = 1L;

	private String naam;

	/**
	 * Constructor
	 */
	public VoorvoegselZoekFilter()
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
}
