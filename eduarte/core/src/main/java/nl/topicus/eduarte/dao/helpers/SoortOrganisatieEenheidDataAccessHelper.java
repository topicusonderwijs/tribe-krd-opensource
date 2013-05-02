/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.SoortOrganisatieEenheid;
import nl.topicus.eduarte.zoekfilters.SoortOrganisatieEenheidZoekFilter;

/**
 * @author loite
 */
public interface SoortOrganisatieEenheidDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<SoortOrganisatieEenheid, SoortOrganisatieEenheidZoekFilter>
{
	public SoortOrganisatieEenheid get(Long id);

	public List<SoortOrganisatieEenheid> list(boolean alleenActieve);

	public SoortOrganisatieEenheid getSoortOrganisatieEenheid(String code);
}
