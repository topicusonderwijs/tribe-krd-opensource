/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groepstype;
import nl.topicus.eduarte.zoekfilters.GroepstypeZoekFilter;

/**
 * @author hoeve
 */
public interface GroepstypeDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Groepstype, GroepstypeZoekFilter>
{
}
