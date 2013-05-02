package nl.topicus.eduarte.dao.participatie.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.PeriodeIndeling;
import nl.topicus.eduarte.participatie.zoekfilters.PeriodeIndelingZoekFilter;

public interface PeriodeIndelingDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<PeriodeIndeling, PeriodeIndelingZoekFilter>
{
	/**
	 * @param periodeIndeling
	 * @return true als de omschrijving al bestaat en actief is bij de meegegeven of
	 *         hogere of lagere of organisatieeenheid. anders false
	 */
	public boolean bestaat(PeriodeIndeling periodeIndeling);

}
