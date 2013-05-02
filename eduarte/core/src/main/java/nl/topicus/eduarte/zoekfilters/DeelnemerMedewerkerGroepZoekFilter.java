package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.participatie.DeelnemerMedewerkerGroep;
import nl.topicus.eduarte.entities.participatie.enums.DeelnemerMedewerkerGroepEnum;

public class DeelnemerMedewerkerGroepZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<DeelnemerMedewerkerGroep>
{
	private static final long serialVersionUID = 1L;

	private String omschrijving;

	private DeelnemerMedewerkerGroepEnum type;

	private DeelnemerMedewerkerGroepEnum excludeType;

	public DeelnemerMedewerkerGroepZoekFilter()
	{
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setType(DeelnemerMedewerkerGroepEnum type)
	{
		this.type = type;
	}

	public DeelnemerMedewerkerGroepEnum getType()
	{
		return type;
	}

	public void setExcludeType(DeelnemerMedewerkerGroepEnum excludeType)
	{
		this.excludeType = excludeType;
	}

	public DeelnemerMedewerkerGroepEnum getExcludeType()
	{
		return excludeType;
	}
}
