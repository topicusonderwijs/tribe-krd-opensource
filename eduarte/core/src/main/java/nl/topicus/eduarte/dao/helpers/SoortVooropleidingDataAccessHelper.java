/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding.SoortOnderwijs;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.zoekfilters.SoortVooropleidingZoekFilter;

/**
 * @author hop
 */
public interface SoortVooropleidingDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<SoortVooropleiding, SoortVooropleidingZoekFilter>
{
	public SoortVooropleiding get(Instelling organisatie, String naam);

	/**
	 * @return de soort vooropleiding van het gegeven type (met diploma), mits daarvan
	 *         precies 1 is geregistreerd (en anders null)
	 */
	public SoortVooropleiding get(SoortOnderwijs soortOnderwijs);
}
