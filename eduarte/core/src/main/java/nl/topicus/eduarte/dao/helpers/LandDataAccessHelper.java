/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

/**
 * @author loite
 */
public interface LandDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Land, LandelijkCodeNaamZoekFilter<Land>>
{

	/**
	 * Geeft het land met de gegeven code.
	 * 
	 * @param code
	 * @return Het land met de gegeven code.
	 */
	public Land get(String code);

	/**
	 * Geeft het land met de tweeletterige iso-code
	 * 
	 * @param isoCode
	 *            case insensitive
	 * @return Het land met de gegeven code; null indien niet gevonden.
	 */
	public Land getByISO(String isoCode);

	/**
	 * Geeft het land met de gegeven naam
	 * 
	 * @param naam
	 *            case insensitive
	 * @return Het land met de gegeven naam; null indien niet gevonden.
	 */
	public Land getByNaam(String naam);

}
