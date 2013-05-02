package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.eduarte.entities.taxonomie.SoortTaxonomieElement;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElementType;

/**
 * @author loite
 */
public interface TaxonomieElementTypeDataAccessHelper extends
		BatchDataAccessHelper<TaxonomieElementType>
{

	/**
	 * @return Het speciale type 'Taxonomie'.
	 */
	public TaxonomieElementType getTaxonomieType();

	/**
	 * @param taxonomie
	 *            De taxonomie waarvoor de types opgevraagd moeten worden.
	 * @param soort
	 *            Het soort taxonomie-elementen die teruggegeven moeten worden. Mag null
	 *            zijn
	 * @return Alle taxonomieelementtypes van de gegeven taxonomie
	 */
	public List<TaxonomieElementType> list(Taxonomie taxonomie, SoortTaxonomieElement soort);

	/**
	 * @param type
	 * @return true indien het taxonomie-elementtype in gebruik is door een
	 *         taxonomie-element of door een ander taxonomie-elementtype.
	 */
	public boolean isInGebruik(TaxonomieElementType type);

	/**
	 * @param naam
	 * @param taxonomie
	 * @return Het taxonomieelementtype met de gegeven naam
	 */
	public TaxonomieElementType get(String naam, Taxonomie taxonomie);

}
