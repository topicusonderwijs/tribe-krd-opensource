/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.security.authorization.AuthorisatieNiveau;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.zoekfilters.RolZoekFilter;

/**
 * helper interface for {@link Rol}.
 * 
 * @author marrink
 */
public interface RolDataAccessHelper extends BatchZoekFilterDataAccessHelper<Rol, RolZoekFilter>
{
	/**
	 * Gets all the roles of an organisation for a certain niveau.
	 * 
	 * @param niveau
	 * @return a list of roles
	 */
	public List<Rol> getRollen(AuthorisatieNiveau niveau);

	/**
	 * Geeft de rol met de gegeven naam bij de gegeven organisatie, of null indien geen
	 * rol gevonden wordt.
	 * 
	 * @param naam
	 * @return the role
	 */
	public Rol getRol(String naam);

	/**
	 * Geeft een lijst van de categorieen die beginnen met het gegeven fragment.
	 */
	public List<String> getCategorieen(String fragment);

	public List<Integer> getZorglijnen(Integer max);
}
