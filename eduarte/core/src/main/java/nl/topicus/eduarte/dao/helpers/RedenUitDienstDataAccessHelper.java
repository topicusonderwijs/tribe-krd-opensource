/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.entities.personen.RedenUitDienst;
import nl.topicus.eduarte.zoekfilters.RedenUitDienstZoekFilter;

/**
 * @author hoeve
 */
public interface RedenUitDienstDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<RedenUitDienst, RedenUitDienstZoekFilter>
{
	public RedenUitDienst get(Instelling organisatie, String naam);
}
