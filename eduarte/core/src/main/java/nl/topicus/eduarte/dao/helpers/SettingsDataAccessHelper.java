/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.settings.AccountSetting;
import nl.topicus.eduarte.entities.settings.OrganisatieEenheidSetting;
import nl.topicus.eduarte.entities.settings.OrganisatieSetting;

/**
 * Helper voor de verschillende settings van een organisatie.
 * 
 * @author marrink
 */
public interface SettingsDataAccessHelper extends BatchDataAccessHelper<OrganisatieSetting< ? >>
{
	/**
	 * Haalt alle settings van 1 organisatie op.
	 * 
	 * @return lijst met settings
	 */
	public List<OrganisatieSetting< ? >> getSettings();

	/**
	 * Haalt alle settings van 1 organisatie eenheid op.
	 * 
	 * @param eenheid
	 * @return lijst met settings
	 */
	public List<OrganisatieEenheidSetting< ? >> getSettings(OrganisatieEenheid eenheid);

	/**
	 * Haalt alle settings van 1 account op.
	 * 
	 * @param account
	 * @return lijst met settings
	 */
	public List<AccountSetting< ? >> getSettings(Account account);

	/**
	 * De default waarde indien een setting nog niet bekend is.
	 * 
	 * @param <S>
	 * @param clazz
	 * @param args
	 * @return default setting
	 * @throws IllegalArgumentException
	 *             indien de setting niet herkend wordt (lees: iemand vergeten is een
	 *             default op te geven)
	 */
	public <S extends OrganisatieSetting< ? >> S getDefaultSetting(Class<S> clazz, Object... args);

	/**
	 * Haalt 1 specifieke setting per instelling op.
	 * 
	 * @param <S>
	 * @param clazz
	 *            type setting
	 * @return de setting of null indien niet gevonden
	 */
	public <S extends OrganisatieSetting< ? >> S getSetting(Class<S> clazz);

	/**
	 * Haalt 1 specifieke organisatie eenheid setting op.
	 * 
	 * @param <S>
	 * @param clazz
	 *            type setting
	 * @param eenheid
	 * @return setting of null als deze niet bestaat
	 */
	public <S extends OrganisatieEenheidSetting< ? >> S getSetting(Class<S> clazz,
			OrganisatieEenheid eenheid);

	/**
	 * Haalt 1 specifieke accountsetting op.
	 * 
	 * @param <S>
	 * @param clazz
	 *            type setting
	 * @param account
	 * @return setting of null als deze niet bestaat
	 */
	public <S extends AccountSetting< ? >> S getSetting(Class<S> clazz, Account account);

}
