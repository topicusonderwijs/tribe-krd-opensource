/*
 * Copyright (c) 2005-2007, Topicus b.v.
 * All rights reserved
 */
package nl.topicus.cobra.validation;

/**
 * Elfproef klasse voor het uitvoeren van elfproef checks op bankrekeningnummers en
 * sofinummers. De elfproef is een invoercontrole op rekeningnummers en sofinummers
 * waarmee een fout in een cijfer van het nummer kan worden opgespoord.
 * <p>
 * De berekening gebeurt door elk cijfer te vermenigvuldigen met een factor en de
 * resultaten bij elkaar op te tellen. De som moet dan modulo elf gelijk zijn aan nul.
 * <p>
 * Bij onderwijsnummers moet de som modulo elf gelijk zijn aan 5.
 * 
 * @author Martijn Dashorst
 * @author Laurens Hop
 */
public final class ElfProef
{
	/**
	 * Lijst van de multipliers voor elke digit in een sofinummer of onderwijsnummer. Let
	 * op de laatste multiplier, die is negatief. Bij een onderwijsnummer moet de som
	 * modulo elf gelijk zijn aan 5, bij een sofinummer aan 0.
	 */
	private static final long[] SOFI_ONDNR_MULTIPLIERS = {9, 8, 7, 6, 5, 4, 3, 2, -1};

	/**
	 * Lijst van de multipliers voor elke digit in een bank rekeningnummer.
	 */
	private static final long[] REKN_MULTIPLIERS = {9, 8, 7, 6, 5, 4, 3, 2, 1};

	/**
	 * Vereiste uitkomst van de som modulo elf in het geval van een onderwijsnummer.
	 */
	private static final long ONDNR_UITKOMST = 5;

	/**
	 * Bepaalt of het sofinummer voldoet aan de sofinummer elfproef.
	 * 
	 * @param sofinummer
	 *            het sofinummer dat gecontroleerd moet worden.
	 * @return <code>true</code> als het sofinummer voldoet aan de elf proef.
	 */
	public boolean isGeldigSofiNummer(String sofinummer)
	{
		// daadwerkelijke controle op een number via snelle controle
		try
		{
			Long.valueOf(sofinummer);
		}
		catch (NumberFormatException e)
		{
			return false;
		}

		return isElfProef(getDigits(sofinummer), SOFI_ONDNR_MULTIPLIERS);
	}

	/**
	 * Bepaalt of het sofinummer voldoet aan de sofinummer elfproef.
	 * 
	 * @param sofinummer
	 *            het sofinummer dat gecontroleerd moet worden.
	 * @return <code>true</code> als het sofinummer voldoet aan de elf proef.
	 */
	public boolean isGeldigSofiNummer(Long sofinummer)
	{
		return isGeldigSofiNummer(sofinummer.toString());
	}

	/**
	 * Bepaalt of het rekeningnummer voldoet aan de rekeningnummer elfproef.
	 * 
	 * @param rekeningnummer
	 *            het rekeningnummer dat gecontroleerd moet worden.
	 * @return <code>true</code> als het rekeningnummer voldoet aan de elf proef.
	 */
	public boolean isGeldigRekeningNummer(String rekeningnummer)
	{
		return isElfProef(getDigits(rekeningnummer), REKN_MULTIPLIERS);
	}

	/**
	 * Bepaalt of het rekeningnummer voldoet aan de rekeningnummer elfproef.
	 * 
	 * @param rekeningnummer
	 *            het rekeningnummer dat gecontroleerd moet worden.
	 * @return <code>true</code> als het rekeningnummer voldoet aan de elf proef.
	 */
	public boolean isGeldigRekeningNummer(Long rekeningnummer)
	{
		return isGeldigRekeningNummer(rekeningnummer.toString());
	}

	/**
	 * Bepaalt of het onderwijsnummer voldoet aan de onderwijsnummer elfproef.
	 * 
	 * @param onderwijsnummer
	 *            het onderwijsnummer dat gecontroleerd moet worden.
	 * @return <code>true</code> als het onderwijsnummer voldoet aan de elf proef.
	 */
	public boolean isGeldigOnderwijsNummer(String onderwijsnummer)
	{
		// daadwerkelijke controle op een number via snelle controle
		try
		{
			Long.valueOf(onderwijsnummer);
		}
		catch (NumberFormatException e)
		{
			return false;
		}

		return isElfProef(getDigits(onderwijsnummer), SOFI_ONDNR_MULTIPLIERS, ONDNR_UITKOMST);
	}

	/**
	 * Bepaalt of het onderwijsnummer voldoet aan de onderwijsnummer elfproef.
	 * 
	 * @param onderwijsnummer
	 *            het onderwijsnummer dat gecontroleerd moet worden.
	 * @return <code>true</code> als het onderwijsnummer voldoet aan de elf proef.
	 */
	public boolean isGeldigOnderwijsNummer(Long onderwijsnummer)
	{
		return isGeldigOnderwijsNummer(onderwijsnummer.toString());
	}

	/**
	 * Bepaalt of de som van elke digit vermenigvuldigd met zijn bijbehorende multiplier
	 * modulo elf gelijk is aan 0. Deze methode kan dus generiek met rekeningnummers en
	 * sofinummers overweg, gegeven de juiste invulling van de multipliers.
	 * 
	 * @param digits
	 *            het nummer dat elf proef moet zijn
	 * @param multipliers
	 *            de vermenigvuldigings factoren
	 * @return <code>true</code> als de som van elk digit vermenigvuldigd met multiplier
	 *         modulo elf 0 is.
	 */
	private boolean isElfProef(long[] digits, long[] multipliers)
	{
		return isElfProef(digits, multipliers, 0);
	}

	/**
	 * Bepaalt of de som van elke digit vermenigvuldigd met zijn bijbehorende multiplier
	 * modulo elf gelijk is aan een zekere uitkomst. Deze methode kan dus generiek met
	 * rekeningnummers, sofinummers en onderwijsnummers overweg, gegeven de juiste
	 * invulling van de multipliers.
	 * 
	 * @param digits
	 *            het nummer dat elf proef moet zijn
	 * @param multipliers
	 *            de vermenigvuldigings factoren
	 * @param uitkomst
	 *            de vereiste uitkomst
	 * @return <code>true</code> als de som van elk digit vermenigvuldigd met multiplier
	 *         modulo elf gelijk is aan de vereiste uitkomst is.
	 */
	private boolean isElfProef(long[] digits, long[] multipliers, long uitkomst)
	{
		long sum = 0;
		for (int i = 0; i < multipliers.length; i++)
		{
			sum += digits[i] * multipliers[i];
		}
		return sum % 11 == uitkomst;
	}

	/**
	 * Converteert de string nummer naar een array van longs met een lengte van 9 bedoelt
	 * voor bewerking in een elf-proef berekening. Voorbeeld "123456789" wordt
	 * [1,2,3,4,5,6,7,8,9], en "12" wordt [0,0,0,0,0,0,0,1,2].
	 * 
	 * @param nummer
	 *            het getal dat vertaald moet worden.
	 * @return een array van de
	 */
	long[] getDigits(String nummer)
	{
		long[] digits = new long[9];

		for (int i = nummer.length() - 1, j = 1; i >= 0; i--, j++)
		{
			digits[digits.length - j] = Character.getNumericValue(nummer.charAt(i));
		}
		return digits;
	}
}
