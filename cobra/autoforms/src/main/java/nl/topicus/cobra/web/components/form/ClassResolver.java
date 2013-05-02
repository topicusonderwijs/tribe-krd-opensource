package nl.topicus.cobra.web.components.form;

/**
 * ClassResolver biedt een abstractie voor het achterhalen van de echte class van een
 * object. Dit kan bijvoorbeeld gebruikt worden als het object een proxy is.
 * 
 * @author papegaaij
 */
public interface ClassResolver
{
	/**
	 * Bepaald de class van het gegeven object.
	 * 
	 * @param object
	 *            Het object
	 * @return De class, of null als deze resolver de class niet kan resolven.
	 */
	public Class< ? > resolveClass(Object object);
}
