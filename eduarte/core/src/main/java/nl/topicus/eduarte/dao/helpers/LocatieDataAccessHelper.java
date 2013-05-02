package nl.topicus.eduarte.dao.helpers;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.zoekfilters.LocatieZoekFilter;

/**
 * @author loite
 */
public interface LocatieDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Locatie, LocatieZoekFilter>
{
	/**
	 * @return Alle actieve locaties van de gegeven instelling.
	 */
	public List<Locatie> list(Date peildatum);

	/**
	 * 
	 * @param afkorting
	 * @return de organistatie met de meegeveen afkorting
	 */
	public Locatie get(String afkorting);

	/**
	 * 
	 * @param id
	 * @return de organistatie met de meegeveen afkorting
	 */
	public Locatie get(Long id);

}
