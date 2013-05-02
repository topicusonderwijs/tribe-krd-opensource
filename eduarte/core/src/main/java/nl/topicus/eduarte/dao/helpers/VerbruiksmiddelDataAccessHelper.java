package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Verbruiksmiddel;
import nl.topicus.eduarte.zoekfilters.VerbruiksmiddelZoekFilter;

/**
 * @author vandekamp
 */
public interface VerbruiksmiddelDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Verbruiksmiddel, VerbruiksmiddelZoekFilter>
{

	/**
	 * @return actieve Verbruiksmiddelen bij de gegeven instelling
	 */
	public List<Verbruiksmiddel> list();

	/**
	 * @param code
	 * @return Het Verbruiksmiddel met de gegeven code bij de gegeven instelling
	 */
	public Verbruiksmiddel get(String code);

}
