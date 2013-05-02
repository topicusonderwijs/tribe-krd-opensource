package nl.topicus.eduarte.dao.participatie.helpers;

import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.olc.OlcLocatie;
import nl.topicus.eduarte.participatie.zoekfilters.OlcLocatieZoekFilter;

public interface OlcLocatieDataAccessHelper
		extends
		CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper<OlcLocatie, OlcLocatieZoekFilter>
{
	public OlcLocatie getByOlcCode(String code);
}
