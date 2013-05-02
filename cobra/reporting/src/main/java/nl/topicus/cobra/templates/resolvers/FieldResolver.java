package nl.topicus.cobra.templates.resolvers;

import nl.topicus.cobra.templates.FieldInfo;

/**
 * Generieke interface voor het resolven van een veld op basis van de naam.
 * 
 * @author Laurens Hop
 */
public interface FieldResolver
{
	/**
	 * Resolvet een field op basis van de naam.
	 * 
	 * @param name
	 * @return het gevonden object
	 */
	public abstract Object resolve(String name);

	/**
	 * Valideert het field op basis van de naam.
	 * 
	 * @param name
	 * @return info over het field
	 */
	public abstract FieldInfo getInfo(String name);

	/**
	 * Zorgt dat naar het volgende element van een lijst wordt gegaan.
	 * 
	 * @param name
	 *            naam van de lijst, indien bekend. Indien null, dan wordt uitgegaan van
	 *            de laatstgebruikte lijst.
	 * @return het volgende element van de lijst. Null indien niet meer elementen.
	 */
	public abstract Object next(String name);

}
