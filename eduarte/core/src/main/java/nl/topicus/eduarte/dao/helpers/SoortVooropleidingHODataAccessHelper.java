package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleidingHO;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

public interface SoortVooropleidingHODataAccessHelper
		extends
		BatchZoekFilterDataAccessHelper<SoortVooropleidingHO, LandelijkCodeNaamZoekFilter<SoortVooropleidingHO>>
{
	public SoortVooropleidingHO get(String code);
}
