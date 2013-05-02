/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Functie;
import nl.topicus.eduarte.zoekfilters.FunctieZoekFilter;

/**
 * @author hoeve
 */
public interface FunctieDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Functie, FunctieZoekFilter>
{
	public Functie get(String naam);
}
