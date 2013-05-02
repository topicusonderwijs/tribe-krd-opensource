package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.opleiding.OpleidingAanbod;
import nl.topicus.eduarte.zoekfilters.OpleidingAanbodZoekFilter;

public interface OpleidingAanbodDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<OpleidingAanbod, OpleidingAanbodZoekFilter>
{
	long getOpleidingCount(OpleidingAanbodZoekFilter zoekFilter);

}
