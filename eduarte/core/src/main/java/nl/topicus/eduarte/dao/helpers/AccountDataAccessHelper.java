/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.EncryptionProvider;
import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authentication.DeelnemerAccount;
import nl.topicus.eduarte.zoekfilters.AccountZoekFilter;

public interface AccountDataAccessHelper extends EncryptionProvider,
		BatchZoekFilterDataAccessHelper<Account, AccountZoekFilter>
{
	/**
	 * Returns the account with the specified userame and password in the specified
	 * domain.
	 * 
	 * @param username
	 * @param password
	 * @return the account or null if the username password combination is not recognized
	 *         or the account is in another way not authorized to log in
	 */
	public Account authenticate(String username, String password);

	/**
	 * Authenticatie methode voor inloggen via sharepoint.
	 * 
	 * @param gebruikersnaam
	 * @return actieve account of null als er geen (actieve) account gevonden is.
	 */
	public Account authenticate(String gebruikersnaam);

	/**
	 * Checks if anyone else besides the account specified has the specified username.
	 * 
	 * @param username
	 * @return true als de naam in gebruik is door een ander account, anders false
	 */
	public boolean usernameExists(String username);

	/**
	 * Checks if anyone else besides the account specified has the specified username.
	 * 
	 * @param username
	 * @param account
	 * @return true als de naam in gebruik is door een ander account, anders false
	 */
	public boolean usernameExists(String username, Account account);

	/**
	 * Suggereerd een usernaam, uitgaande van een basis.
	 * 
	 * @param namebase
	 * @param organisatie
	 * @return username
	 */
	public String suggestUsername(String namebase, Organisatie organisatie);

	/**
	 * Geeft het account met de gegeven gebruikersnaam bij de huidige organisatie.
	 * 
	 * @param gebruikersnaam
	 * @return account
	 */
	public Account getAccount(String gebruikersnaam);

	/**
	 * Geeft op basis van de geselecteerde medewerker het account terug.
	 * 
	 * @param medewerker
	 * @return account
	 */
	public Account get(Medewerker medewerker);

	/**
	 * Geeft het account dat aan de gegeven deelnemer is gekoppeld.
	 * 
	 * @param deelnemer
	 * @return het account van de deelnemer
	 */
	public Account get(Deelnemer deelnemer);

	/**
	 * Geeft het account dat aan de gegeven persoon is gekoppeld (via de medewerker of
	 * deelnemer).
	 * 
	 * @param persoon
	 * @return het account van de persoon
	 */
	public Account get(Persoon persoon);

	/**
	 * @param gebruikersnaam
	 * @param wachtwoord
	 * @return Het deelnemeraccount met de gegeven gebruikersnaam/wachtwoord combinatie,
	 *         indien de deelnemer actief (ingeschreven) is.
	 */
	public DeelnemerAccount authenticateDeelnemer(String gebruikersnaam, String wachtwoord);

}
