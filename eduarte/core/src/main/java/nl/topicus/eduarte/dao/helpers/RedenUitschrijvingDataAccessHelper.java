/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.zoekfilters.RedenUitschrijvingZoekFilter;

/**
 * @author hoeve
 */
public interface RedenUitschrijvingDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<RedenUitschrijving, RedenUitschrijvingZoekFilter>
{
	public RedenUitschrijving get(Instelling organisatie, String naam);

	/**
	 * @return De {@link RedenUitschrijving} welke correspondeert met de reden van
	 *         uitschijving ivm overlijden.
	 */
	public RedenUitschrijving getOverlijden();
}
