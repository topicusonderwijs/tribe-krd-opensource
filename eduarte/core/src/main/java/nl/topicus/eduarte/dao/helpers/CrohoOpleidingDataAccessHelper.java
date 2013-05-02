package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.entities.taxonomie.ho.CrohoOpleiding;

public interface CrohoOpleidingDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<CrohoOpleiding, DetachableZoekFilter<CrohoOpleiding>>
{
	public CrohoOpleiding getCrohoOpleiding(String isatCode);
}
