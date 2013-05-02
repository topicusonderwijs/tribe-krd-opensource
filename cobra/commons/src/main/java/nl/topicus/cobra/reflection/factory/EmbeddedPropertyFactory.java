package nl.topicus.cobra.reflection.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import nl.topicus.cobra.reflection.EmbeddedProperty;
import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.util.Asserts;

public class EmbeddedPropertyFactory<X> implements IPropertyFactory<X>
{
	private Property<X, ? , ? > parentProperty;

	public EmbeddedPropertyFactory(Property<X, ? , ? > parent)
	{
		Asserts.assertNotEmpty("parent", parent);

		this.parentProperty = parent;
	}

	@Override
	public Property<X, ? , ? > createProperty(Method method)
	{
		return new EmbeddedProperty<X, Object, Object>(parentProperty, method);
	}

	@Override
	public Property<X, ? , ? > createProperty(Field field)
	{
		return new EmbeddedProperty<X, Object, Object>(parentProperty, field);
	}

	@Override
	public <Y, Z> Property<X, Y, Z> createProperty(Class<Y> declaringClass, String name,
			Class<Z> type, Field field, Method getMethod, Method setMethod)
	{
		return new EmbeddedProperty<X, Y, Z>(parentProperty, declaringClass, name, type, field,
			getMethod, setMethod);
	}
}
