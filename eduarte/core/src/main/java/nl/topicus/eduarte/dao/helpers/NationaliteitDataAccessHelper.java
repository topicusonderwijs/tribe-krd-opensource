/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Nationaliteit;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

public interface NationaliteitDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Nationaliteit, LandelijkCodeNaamZoekFilter<Nationaliteit>>
{
	public Nationaliteit get(String code);
}
