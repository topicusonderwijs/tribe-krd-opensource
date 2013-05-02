package nl.topicus.eduarte.dao.helpers.dbs;

import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.bijzonderheden.BijzonderheidCategorie;
import nl.topicus.eduarte.zoekfilters.CodeNaamActiefZoekFilter;

public interface BijzonderheidCategorieDataAccessHelper
		extends
		CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper<BijzonderheidCategorie, CodeNaamActiefZoekFilter<BijzonderheidCategorie>>
{
}
