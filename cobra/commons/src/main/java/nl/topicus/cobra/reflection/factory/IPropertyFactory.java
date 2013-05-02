package nl.topicus.cobra.reflection.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import nl.topicus.cobra.reflection.Property;

/**
 * Interface voor factories welke een Property (sub)class instantie kan maken
 * 
 * @author hoeve
 */
public interface IPropertyFactory<T>
{
	/**
	 * Maakt een nieuw Property object ahv een get of set methode.
	 * 
	 * @param method
	 *            De method aan hand waarvan de Property gemaakt moet worden.
	 */
	public Property<T, ? , ? > createProperty(Method method);

	/**
	 * Maakt een nieuw Property object ahv een field.
	 * 
	 * @param field
	 *            Het field aan hand waarvan de Property gemaakt moet worden.
	 */
	public Property<T, ? , ? > createProperty(Field field);

	/**
	 * Maakt een nieuw Property object met volledig custom inhoud.
	 * 
	 * @param declaringClass
	 *            De class waarin de property gedeclareerd is.
	 * @param name
	 *            De naam van de property.
	 * @param type
	 *            Het type van de property.
	 * @param field
	 *            Het field, of null als deze niet aanwezig is.
	 * @param getMethod
	 *            De get method, of null als deze niet aanwezig is.
	 * @param setMethod
	 *            De set method, of null als deze niet aanwezig is.
	 */
	public <Y, Z> Property<T, Y, Z> createProperty(Class<Y> declaringClass, String name,
			Class<Z> type, Field field, Method getMethod, Method setMethod);
}
