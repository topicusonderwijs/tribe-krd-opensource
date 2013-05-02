package nl.topicus.cobra.entities;

/**
 * Geeft aan op welke manier een field opgeslagen of gekopieerd moet worden.
 * 
 * @author papegaaij
 */
public enum FieldPersistenceMode
{
	/**
	 * De waarde van het veld moet opgeslagen worden en de opgeslagen waarde moet
	 * gemanaged worden (als dat van toepassing is).
	 */
	SAVE_AND_FOLLOW,

	/**
	 * De waarde van het veld moet opgeslagen worden, maar moet niet gemanaged worden.
	 */
	SAVE,

	/**
	 * De waarde moet niet opgeslagen of gekopieerd worden.
	 */
	SKIP
}
