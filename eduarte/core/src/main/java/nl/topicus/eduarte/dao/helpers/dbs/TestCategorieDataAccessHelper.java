package nl.topicus.eduarte.dao.helpers.dbs;

import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.testen.TestCategorie;
import nl.topicus.eduarte.zoekfilters.CodeNaamActiefZoekFilter;

public interface TestCategorieDataAccessHelper
		extends
		CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper<TestCategorie, CodeNaamActiefZoekFilter<TestCategorie>>
{
}
