/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Schooladvies;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.zoekfilters.SchooladviesZoekFilter;

/**
 * @author hop
 */
public interface SchooladviesDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Schooladvies, SchooladviesZoekFilter>
{
	public Schooladvies get(Instelling organisatie, String naam);
}
