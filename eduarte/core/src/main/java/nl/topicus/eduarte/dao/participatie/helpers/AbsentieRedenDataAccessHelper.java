package nl.topicus.eduarte.dao.participatie.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.AbsentieReden;
import nl.topicus.eduarte.participatie.zoekfilters.AbsentieRedenZoekFilter;

/**
 * @author loite
 */
public interface AbsentieRedenDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<AbsentieReden, AbsentieRedenZoekFilter>
{
	/**
	 * @param zoekFilter
	 * @return List<AbsentieReden>
	 */
	List<AbsentieReden> getTonenBijWaarnemingen(AbsentieRedenZoekFilter zoekFilter);

	/**
	 * @param absentieReden
	 * @return true als de omschrijving al bestaat en actief is bij de meegegeven of
	 *         hogere of lagere of organisatieeenheid. anders false
	 */
	boolean bestaatEnActief(AbsentieReden absentieReden);

	public AbsentieReden get(String afkorting);

}
