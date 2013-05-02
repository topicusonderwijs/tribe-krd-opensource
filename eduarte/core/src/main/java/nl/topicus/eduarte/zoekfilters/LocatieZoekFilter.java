package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;

/**
 * Zoekfilter voor locaties.
 * 
 * @author loite
 */
public class LocatieZoekFilter extends AbstractZoekFilter<Locatie>
{
	private static final long serialVersionUID = 1L;

	private OrganisatieEenheid organisatieEenheid;

	private String afkorting;

	@AutoForm(label = "Afkorting")
	private String afkortingZoeken;

	private String naam;

	@AutoForm(label = "Naam")
	private String naamZoeken;

	public LocatieZoekFilter()
	{
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;
	}

	public String getAfkorting()
	{
		return afkorting;
	}

	public void setAfkorting(String afkorting)
	{
		this.afkorting = afkorting;
	}

	public String getAfkortingZoeken()
	{
		return afkortingZoeken;
	}

	public void setAfkortingZoeken(String afkortingZoeken)
	{
		this.afkortingZoeken = afkortingZoeken;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public String getNaamZoeken()
	{
		return naamZoeken;
	}

	public void setNaamZoeken(String naamZoeken)
	{
		this.naamZoeken = naamZoeken;
	}
}
