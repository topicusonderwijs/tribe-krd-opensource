package nl.topicus.eduarte.dao.participatie.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.Maatregel;
import nl.topicus.eduarte.participatie.zoekfilters.MaatregelZoekFilter;

/**
 * @author vandekamp
 */
public interface MaatregelDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Maatregel, MaatregelZoekFilter>
{

	/**
	 * @param maatregel
	 * @return true als de omschrijving al bestaat en actief is bij de meegegeven of
	 *         hogere of lagere of organisatieeenheid. anders false
	 */
	boolean bestaatEnActief(Maatregel maatregel);
}
