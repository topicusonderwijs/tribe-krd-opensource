package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleidingBuitenlands;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.zoekfilters.SoortVooropleidingBuitenlandsZoekFilter;

public interface SoortVooropleidingBuitenlandsDataAccessHelper
		extends
		BatchZoekFilterDataAccessHelper<SoortVooropleidingBuitenlands, SoortVooropleidingBuitenlandsZoekFilter>
{
	public SoortVooropleidingBuitenlands get(Land land, String code);
}
