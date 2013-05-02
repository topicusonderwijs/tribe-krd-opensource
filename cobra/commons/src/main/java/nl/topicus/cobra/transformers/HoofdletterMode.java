package nl.topicus.cobra.transformers;

/**
 * Enum voor het aangeven hoe een HoofdletterAjaxHandler zich moet gedragen. EersteLetter:
 * De eerste letter van de string wordt een hoofdletter. ElkWoord: De eerste letter van
 * elk woord in de string wordt een hoofdletter. Alles: De gehele string wordt upper case.
 * Geen: De gehele string wordt lower case.
 * 
 * @author loite
 */
public enum HoofdletterMode
{
	/**
	 * Eerste letter van de string in het tekstveld wordt op hoofdletter gezet.
	 */
	EersteLetter,
	/**
	 * De eerste letter wordt een hoofdletter alleen als de string uit 1 woord bestaat en
	 * begint met een kleine letter.
	 */
	EersteLetterEenWoord,
	/**
	 * Elk woord in de string wordt op hoofdletter gezet.
	 */
	ElkWoord,
	/**
	 * Na elke punt wordt de eerste letter op hoofdletter gezet.
	 */
	PuntSeperated,
	/**
	 * Alle letters worden hoofdletters.
	 */
	Alles,
	/**
	 * Alle letters worden kleine letters.
	 */
	Geen
}
