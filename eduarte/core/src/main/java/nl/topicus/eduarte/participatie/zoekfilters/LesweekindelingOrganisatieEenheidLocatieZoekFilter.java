package nl.topicus.eduarte.participatie.zoekfilters;

import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.entities.participatie.LesweekIndelingOrganisatieEenheidLocatie;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

/**
 * @author henzen
 */
public class LesweekindelingOrganisatieEenheidLocatieZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<LesweekIndelingOrganisatieEenheidLocatie>
{
	private static final long serialVersionUID = 1L;

	private LesweekIndeling lesweekIndeling;

	public LesweekindelingOrganisatieEenheidLocatieZoekFilter()
	{
	}

	public LesweekIndeling getLesweekIndeling()
	{
		return lesweekIndeling;
	}

	public void setLesweekIndeling(LesweekIndeling lesweekIndeling)
	{
		this.lesweekIndeling = lesweekIndeling;
	}

}
