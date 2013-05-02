package nl.topicus.eduarte.dao.participatie.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.MaatregelToekenning;
import nl.topicus.eduarte.participatie.zoekfilters.MaatregelToekenningZoekFilter;

/**
 * @author vandekamp
 */
public interface MaatregelToekenningDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<MaatregelToekenning, MaatregelToekenningZoekFilter>
{
}
