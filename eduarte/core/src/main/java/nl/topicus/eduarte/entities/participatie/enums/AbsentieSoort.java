package nl.topicus.eduarte.entities.participatie.enums;

/**
 * Geeft aan wat voor absentiesoort een absentiereden is.
 * 
 * @author loite
 */
public enum AbsentieSoort
{
	/**
	 * Absent, dus zal niet aanwezig zijn in de les.
	 */
	Absent,
	/**
	 * Te laat, dus wel aanwezig in de les. De begintijd van de melding zou gelijk moeten
	 * zijn aan de begintijd van de les/afpraak waarvoor de deelnemer te laat is, en de
	 * eindtijd zou gelijk moeten zijn aan de tijd dat de deelnemer wel komt opdagen.
	 */
	Telaat,
	/**
	 * Uit de les verwijderd, dus wel aanwezig in de les. De begintijd van de melding zou
	 * gelijk moeten zijn aan de tijd dat de deelnemer uit de les verwijderd werd, en de
	 * eindtijd zou gelijk moeten zijn aan de tijd dat de deelnemer weer toegelaten werd
	 * tot de les, of gelijk aan de eindtijd van de les.
	 */
	Verwijderd;

}
