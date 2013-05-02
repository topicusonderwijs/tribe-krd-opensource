/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.zoekfilters.VooropleidingZoekFilter;

public interface VooropleidingDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Vooropleiding, VooropleidingZoekFilter>
{
	public Vooropleiding get(Long id);

}
