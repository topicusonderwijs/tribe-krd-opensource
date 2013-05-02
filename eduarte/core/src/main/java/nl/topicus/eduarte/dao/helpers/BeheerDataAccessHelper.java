package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Beheer;

/**
 * Helper voor de verschillende organisaties.
 * 
 * @author marrink
 */
public interface BeheerDataAccessHelper extends BatchDataAccessHelper<Beheer>
{
	/**
	 * Landelijk beheer, hier kan er maar 1 van zijn. Alleen tijdens de allereerste
	 * initialisatie stappen van de applicatie zal dit null zijn.
	 * 
	 * @return beheer of null als deze niet bestaat.
	 */
	public Beheer getBeheer();

}
