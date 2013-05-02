/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Instelling;

/**
 * Helper voor de verschillende organisaties.
 * 
 * @author marrink
 */
public interface InstellingDataAccessHelper extends BatchDataAccessHelper<Instelling>
{
	/**
	 * Haal instelling op bij id.
	 * 
	 * @param id
	 * @return instelling of null als deze niet bestaat
	 */
	public Instelling get(Long id);

	/**
	 * Haal instelling op bij naam.
	 * 
	 * @param naam
	 * @return de instelling met de gegeven naam
	 */
	public Instelling get(String naam);

	public Instelling getInstellingByBrinNr(String brinNr);

	/**
	 * Doet de default setup voor een nieuwe instelling.
	 * 
	 * @param instelling
	 *            nieuwe instelling (geen id)
	 * @param rootUsername
	 * @param rootPassword
	 */
	public void setup(Instelling instelling, String rootUsername, String rootPassword);

	public void createAppAccount(String username, String password);

	/**
	 * @return Alle actieve instellingen.
	 */
	public List<Instelling> list();

	/**
	 * 
	 * @return De naam van de applicatie voor de huidige instelling. De naam kan per
	 *         instelling verschillen (EduArte, Tribe KRD, ...)
	 */
	public String getApplicationTitle();
}
