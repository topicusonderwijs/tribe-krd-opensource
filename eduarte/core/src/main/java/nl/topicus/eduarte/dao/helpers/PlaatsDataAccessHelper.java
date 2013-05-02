/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Gemeente;
import nl.topicus.eduarte.entities.landelijk.Plaats;
import nl.topicus.eduarte.entities.landelijk.Provincie;
import nl.topicus.eduarte.zoekfilters.PlaatsZoekFilter;

public interface PlaatsDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Plaats, PlaatsZoekFilter>
{
	public Plaats get(String naam);

	/**
	 * @param naam
	 *            de naam van de plaats
	 * @param provincie
	 *            mag null zijn
	 * @param gemeente
	 *            mag null zijn
	 */
	public Plaats get(String naam, Provincie provincie, Gemeente gemeente);

	public Plaats getPlaatsVanCaseInsensitiveNaam(String naam);
}
