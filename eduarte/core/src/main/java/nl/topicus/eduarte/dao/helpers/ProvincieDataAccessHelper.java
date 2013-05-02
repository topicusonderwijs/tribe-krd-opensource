package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Provincie;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

public interface ProvincieDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Provincie, LandelijkCodeNaamZoekFilter<Provincie>>
{
	public Provincie get(String code);

	public Provincie getByNaam(String naam);
}
