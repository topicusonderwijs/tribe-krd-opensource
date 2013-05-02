package nl.topicus.eduarte.participatie.zoekfilters;

import nl.topicus.eduarte.entities.participatie.ExterneAgendaKoppeling;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;
import nl.topicus.eduarte.zoekfilters.IActiefZoekFilter;

/**
 */
public class ExterneAgendaKoppelingZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<ExterneAgendaKoppeling> implements
		IActiefZoekFilter<ExterneAgendaKoppeling>

{
	private static final long serialVersionUID = 1L;

	private Boolean actief;

	private String naam;

	public ExterneAgendaKoppelingZoekFilter()
	{
	}

	public Boolean getActief()
	{
		return actief;
	}

	public void setActief(Boolean actief)
	{
		this.actief = actief;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}
}
