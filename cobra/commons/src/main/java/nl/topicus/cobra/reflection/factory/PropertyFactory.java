package nl.topicus.cobra.reflection.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import nl.topicus.cobra.reflection.Property;

public class PropertyFactory<T> implements IPropertyFactory<T>
{
	public PropertyFactory()
	{

	}

	@Override
	public Property<T, T, ? > createProperty(Method method)
	{
		return new Property<T, T, Object>(method);
	}

	@Override
	public Property<T, T, ? > createProperty(Field field)
	{
		return new Property<T, T, Object>(field);
	}

	@Override
	public <Y, Z> Property<T, Y, Z> createProperty(Class<Y> declaringClass, String name,
			Class<Z> type, Field field, Method getMethod, Method setMethod)
	{
		return new Property<T, Y, Z>(declaringClass, name, type, field, getMethod, setMethod);
	}
}
