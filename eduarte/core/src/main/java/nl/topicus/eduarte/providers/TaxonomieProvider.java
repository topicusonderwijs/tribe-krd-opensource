package nl.topicus.eduarte.providers;

import java.io.Serializable;

import nl.topicus.eduarte.entities.taxonomie.Taxonomie;

/**
 * Interface voor classes die een Taxonomie kunnen opleveren.
 * 
 * @author loite
 */
public interface TaxonomieProvider extends Serializable
{

	/**
	 * @return De taxonomie van deze provider.
	 */
	public Taxonomie getTaxonomie();

}
