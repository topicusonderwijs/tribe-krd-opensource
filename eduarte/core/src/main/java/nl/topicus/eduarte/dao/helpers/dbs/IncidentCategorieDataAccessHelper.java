package nl.topicus.eduarte.dao.helpers.dbs;

import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.gedrag.IncidentCategorie;
import nl.topicus.eduarte.zoekfilters.CodeNaamActiefZoekFilter;

public interface IncidentCategorieDataAccessHelper
		extends
		CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper<IncidentCategorie, CodeNaamActiefZoekFilter<IncidentCategorie>>
{
}
