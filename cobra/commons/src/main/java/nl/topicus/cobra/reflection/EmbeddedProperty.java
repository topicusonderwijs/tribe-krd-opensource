package nl.topicus.cobra.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import nl.topicus.cobra.util.Asserts;

/**
 * EmbeddedProperty is een beschrijving van een property van een class welke is gevonden
 * via een (Embedded)Property.
 * 
 * @see nl.topicus.cobra.reflection.Property
 * @author papegaaij
 */
public class EmbeddedProperty<X, Y, Z> extends Property<X, Y, Z>
{
	private Property<X, ? , ? > parentProperty;

	/**
	 * Maakt een nieuw Property object ahv een get of set methode.
	 * 
	 * @param parent
	 *            De parent waarmee deze property is gevonden.
	 * @param method
	 *            De method aan hand waarvan de Property gemaakt moet worden.
	 */
	public EmbeddedProperty(Property<X, ? , ? > parent, Method method)
	{
		super(method);
		Asserts.assertNotNull("parent", parent);
		this.parentProperty = parent;
	}

	/**
	 * Maakt een nieuw Property object ahv een field.
	 * 
	 * @param parent
	 *            De parent waarmee deze property is gevonden.
	 * @param field
	 *            Het field aan hand waarvan de Property gemaakt moet worden.
	 */
	public EmbeddedProperty(Property<X, ? , ? > parent, Field field)
	{
		super(field);
		Asserts.assertNotNull("parent", parent);
		this.parentProperty = parent;
	}

	/**
	 * Maakt een nieuw Property object met volledig custom inhoud.
	 * 
	 * @param parent
	 *            De parent waarmee deze property is gevonden.
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
	public EmbeddedProperty(Property<X, ? , ? > parent, Class<Y> declaringClass, String name,
			Class<Z> type, Field field, Method getMethod, Method setMethod)
	{
		super(declaringClass, name, type, field, getMethod, setMethod);
		Asserts.assertNotNull("parent", parent);
		this.parentProperty = parent;
	}

	/**
	 * @return de Property waarmee deze EmbeddedProperty is gevonden.
	 */
	public Property<X, ? , ? > getParentProperty()
	{
		return parentProperty;
	}

	/**
	 * Geeft het echte pad van deze property terug. Dit bestaat uit parent.getPath() en
	 * super.getPath() gescheiden door een punt.
	 * 
	 * @see nl.topicus.cobra.reflection.Property#getPath()
	 */
	@Override
	public String getPath()
	{
		return parentProperty.getPath() + "." + super.getPath();
	}

	@Override
	public Class<X> getBaseClass()
	{
		return parentProperty.getBaseClass();
	}

	@Override
	public Z getValueNull(Object base)
	{
		Object baseValue = parentProperty.getValueNull(base);
		if (baseValue == null)
			return null;
		return super.getValue(baseValue);
	}

	@Override
	public Z getValue(Object base)
	{
		return super.getValue(parentProperty.getValue(base));
	}

	@Override
	public void setValue(Object base, Object value)
	{
		super.setValue(parentProperty.getValue(base), value);
	}

	@Override
	public int hashCode()
	{
		return super.hashCode() ^ parentProperty.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		return super.equals(obj)
			&& parentProperty.equals(((EmbeddedProperty< ? , ? , ? >) obj).parentProperty);
	}
}
