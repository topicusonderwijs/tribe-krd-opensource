/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.entities.adres.StandaardContactgegeven;
import nl.topicus.eduarte.entities.adres.TypeContactgegeven;
import nl.topicus.eduarte.zoekfilters.SoortContactgegevenZoekFilter;

/**
 * @author hoeve
 */
public interface SoortContactgegevenDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<SoortContactgegeven, SoortContactgegevenZoekFilter>
{
	/**
	 * Zoek een SoortContactgegeven op basis van code.
	 * 
	 * @param code
	 * @return contactgegeven met de gegeven code
	 */
	public SoortContactgegeven get(String code);

	/**
	 * Zoek een SoortContactgegeven op basis van code.
	 * 
	 * @param naam
	 * @return contactgegeven met de gegeven naam
	 */
	public SoortContactgegeven getByNaam(String naam);

	/**
	 * @return alle actieve soorten contactgegevens bij de gegeven instelling waarbij deze
	 *         gemarkeerd moet zijn als een van de standaard tonen opties (of eventueel
	 *         null).
	 */
	public List<SoortContactgegeven> list(List<StandaardContactgegeven> standaarden,
			boolean geenStandaardContactgegevenIngevuldMeenemen);

	public SoortContactgegeven get(TypeContactgegeven typegegeven);
}
