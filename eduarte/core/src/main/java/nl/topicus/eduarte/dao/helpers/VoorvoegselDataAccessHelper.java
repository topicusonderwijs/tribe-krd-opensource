/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Voorvoegsel;
import nl.topicus.eduarte.zoekfilters.VoorvoegselZoekFilter;

/**
 * @author hoeve
 */
public interface VoorvoegselDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Voorvoegsel, VoorvoegselZoekFilter>
{
	/**
	 * @param naam
	 * @return voorvoegsel met de gegeven naam
	 */
	public Voorvoegsel get(String naam);

	public Voorvoegsel get(Long id);
}
