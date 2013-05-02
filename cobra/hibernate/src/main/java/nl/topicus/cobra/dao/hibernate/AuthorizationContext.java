/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.dao.hibernate;

import java.util.List;

/**
 * Abstractie tussen wicket en de DataAccessHelpers.
 * 
 * @author marrink
 */
public interface AuthorizationContext<T>
{
	/**
	 * Heeft men rechten op instellingsniveau
	 * 
	 * @return true bij instellingsrechten, anders false.
	 */
	public boolean isInstellingClearance();

	/**
	 * Heeft men vestigingsrechten.
	 * 
	 * @return true bij vestigigingsrechten, anders false.
	 */
	public boolean isOrganisatieEenheidClearance();

	/**
	 * Lijst met toegestane elementen / entiteiten / id's.
	 * 
	 * @return lijst
	 */
	public List<T> getAllowedElements();
}
