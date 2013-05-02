/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.providers;

import java.io.Serializable;

import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;

/**
 * @author loite
 */
public interface OrganisatieEenheidProvider extends Serializable
{
	/**
	 * @return de geselecteerde organisatie-eenheid
	 */
	public OrganisatieEenheid getOrganisatieEenheid();

}
