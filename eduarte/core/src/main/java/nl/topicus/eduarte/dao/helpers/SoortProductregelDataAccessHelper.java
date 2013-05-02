package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.productregel.SoortProductregel;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.zoekfilters.SoortProductregelZoekFilter;

/**
 * @author loite
 */
public interface SoortProductregelDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<SoortProductregel, SoortProductregelZoekFilter>
{

	/**
	 * @param taxonomie
	 * @return Alle actieve soorten productregels bij de gegeven taxonomie en instelling,
	 *         inclusief alle landelijke soorten productregels.
	 */
	public List<SoortProductregel> list(Taxonomie taxonomie);

	public boolean isSoortProductregelInGebruik(SoortProductregel soortProductregel);

	public int getVolgendeVolgnummer();

	public SoortProductregel get(Taxonomie taxonomie, String naam);

}
