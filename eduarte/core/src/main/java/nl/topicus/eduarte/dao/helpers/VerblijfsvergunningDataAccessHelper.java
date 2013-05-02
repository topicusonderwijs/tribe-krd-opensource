package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Verblijfsvergunning;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

public interface VerblijfsvergunningDataAccessHelper
		extends
		BatchZoekFilterDataAccessHelper<Verblijfsvergunning, LandelijkCodeNaamZoekFilter<Verblijfsvergunning>>
{
	public Verblijfsvergunning get(String code);
}
