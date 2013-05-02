package nl.topicus.eduarte.participatie.zoekfilters;

import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.Basisrooster;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

/**
 * Zoekfilter voor basisroosters.
 * 
 * @author loite
 */
public class BasisroosterZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<Basisrooster>
{
	private static final long serialVersionUID = 1L;

	private String naam;

	public BasisroosterZoekFilter()
	{

	}

	/**
	 * Constructor
	 * 
	 * @param organisatieEenheid
	 */
	public BasisroosterZoekFilter(OrganisatieEenheid organisatieEenheid)
	{
		setOrganisatieEenheid(organisatieEenheid);
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
