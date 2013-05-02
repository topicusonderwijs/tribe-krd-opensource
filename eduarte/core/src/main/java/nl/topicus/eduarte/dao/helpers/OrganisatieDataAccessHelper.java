/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.EncryptionProvider;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.entities.organisatie.Organisatie;

/**
 * Helper voor de verschillende organisaties.
 * 
 * @author marrink
 */
public interface OrganisatieDataAccessHelper extends BatchDataAccessHelper<Organisatie>
{
	/**
	 * Lijst met organisatie waarop ingelogd kan worden. n.b. beheer en instellingen.
	 * 
	 * @return lijst
	 */
	public List< ? extends Organisatie> getInlogDomeinen();

	/**
	 * Haalt de Clusius Organisatie op, tbv Sharepoint
	 * 
	 * @param naam
	 *            The organisatie to get
	 * @return Organisatie
	 */
	public Organisatie getOrganisatie(String naam);

	public Organisatie getOrganisatieByAPIKey(String apiKey);

	/**
	 * @param key
	 * @param encryptionProvider
	 * @return true als de gegeven module afgenomen is door de gegeven instelling.
	 */
	public boolean isModuleAfgenomen(EduArteModuleKey key, EncryptionProvider encryptionProvider);

	public List<Instelling> getInstellingen();
}
