package nl.topicus.eduarte.providers;

import java.io.Serializable;

import nl.topicus.eduarte.entities.organisatie.Locatie;

/**
 * Objecten die een Locatie kunnen leveren.
 * 
 * @author loite
 */
public interface LocatieProvider extends Serializable
{

	/**
	 * @return De locatie van dit object.
	 */
	public Locatie getLocatie();

}
