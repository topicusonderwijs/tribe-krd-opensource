package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.personen.RelatieSoort;

/**
 * 
 * 
 * @author vanharen
 */
public class RelatieSoortZoekFilter extends AbstractCodeNaamActiefZoekFilter<RelatieSoort>
		implements ICodeNaamActiefZoekFilter<RelatieSoort>
{

	private static final long serialVersionUID = 1L;

	private String naam;

	private Boolean persoonOpname;

	private Boolean organisatieOpname;

	public RelatieSoortZoekFilter()
	{
		super(RelatieSoort.class);
	}

	@Override
	public String getNaam()
	{
		return naam;
	}

	@Override
	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public Boolean getPersoonOpname()
	{
		return persoonOpname;
	}

	public void setPersoonOpname(Boolean persoonOpname)
	{
		this.persoonOpname = persoonOpname;
	}

	public Boolean getOrganisatieOpname()
	{
		return organisatieOpname;
	}

	public void setOrganisatieOpname(Boolean organisatieOpname)
	{
		this.organisatieOpname = organisatieOpname;
	}

}
