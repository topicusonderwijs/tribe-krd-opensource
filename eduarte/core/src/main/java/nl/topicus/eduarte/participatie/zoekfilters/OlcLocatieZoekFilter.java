package nl.topicus.eduarte.participatie.zoekfilters;

import nl.topicus.eduarte.entities.participatie.olc.OlcLocatie;
import nl.topicus.eduarte.providers.OrganisatieEenheidProvider;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;
import nl.topicus.eduarte.zoekfilters.ICodeNaamActiefZoekFilter;

public class OlcLocatieZoekFilter extends AbstractOrganisatieEenheidLocatieZoekFilter<OlcLocatie>
		implements OrganisatieEenheidProvider, ICodeNaamActiefZoekFilter<OlcLocatie>
{
	private static final long serialVersionUID = 1L;

	private Boolean actief;

	private String code;

	private String naam;

	@Override
	public Boolean getActief()
	{
		return actief;
	}

	@Override
	public void setActief(Boolean actief)
	{
		this.actief = actief;
	}

	@Override
	public String getCode()
	{
		return code;
	}

	@Override
	public void setCode(String code)
	{
		this.code = code;
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

	@Override
	public Class<OlcLocatie> getEntityClass()
	{
		return OlcLocatie.class;
	}
}
