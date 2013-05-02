package nl.topicus.eduarte.participatie.zoekfilters;

import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

/**
 * @author henzen
 */
public class LesweekindelingZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<LesweekIndeling>
{
	private static final long serialVersionUID = 1L;

	private String code;

	private String naam;

	private boolean actief;

	private String omschrijving;

	public LesweekindelingZoekFilter()
	{
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getCode()
	{
		return code;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}

	public boolean isActief()
	{
		return actief;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

}
