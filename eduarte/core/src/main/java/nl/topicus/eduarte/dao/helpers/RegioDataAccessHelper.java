package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Regio;

/**
 * @author loite
 */
public interface RegioDataAccessHelper extends BatchDataAccessHelper<Regio>
{

	/**
	 * @param code
	 * @return de regio met de gegeven code
	 */
	public Regio get(String code);

}
