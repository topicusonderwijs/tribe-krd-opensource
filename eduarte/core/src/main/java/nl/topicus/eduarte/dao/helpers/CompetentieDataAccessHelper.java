package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Competentie;
import nl.topicus.eduarte.zoekfilters.CompetentieZoekFilter;

/**
 * Data access helper voor het ophalen van competenties.
 */
public interface CompetentieDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Competentie, CompetentieZoekFilter>
{
	/**
	 * Haalt alle competenties en hun componenten.
	 * 
	 * @return alle competenties.
	 */
	public List<Competentie> getCompetenties();
}
