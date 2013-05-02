/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Gemeente;
import nl.topicus.eduarte.zoekfilters.GemeenteZoekFilter;

public interface GemeenteDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Gemeente, GemeenteZoekFilter>
{
	public Gemeente get(String code);
}
