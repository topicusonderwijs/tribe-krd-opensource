package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.VooropleidingVak;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

public interface VooropleidingVakDataAccessHelper
		extends
		BatchZoekFilterDataAccessHelper<VooropleidingVak, LandelijkCodeNaamZoekFilter<VooropleidingVak>>
{
	public VooropleidingVak get(String code);
}
