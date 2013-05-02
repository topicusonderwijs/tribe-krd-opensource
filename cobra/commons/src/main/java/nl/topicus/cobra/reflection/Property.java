package nl.topicus.cobra.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import nl.topicus.cobra.util.JavaUtil;

/**
 * Property is een beschrijving van een property van een class. Naast de getter- en/of
 * setter-method, bevat deze class ook het field, de naam en type van de property en de
 * class waarin de property gedeclareerd is. Via deze class is het ook makkelijk om te
 * zoeken naar annotations op een property als deze op het field, get en/of set method
 * kunnen staan.
 * 
 * @author papegaaij
 */
public class Property<X, Y, Z>
{
	private Class<Y> declaringClass;

	private String name;

	private Class<Z> type;

	private Field field;

	private Method getMethod;

	private Method setMethod;

	protected Property()
	{
	}

	@SuppressWarnings("unchecked")
	public Property(Method method)
	{
		if (method.getName().startsWith("get") || method.getName().startsWith("is"))
		{
			getMethod = method;
			declaringClass = (Class<Y>) method.getDeclaringClass();
			if (method.getName().startsWith("get"))
			{
				name =
					Character.toLowerCase(method.getName().charAt(3))
						+ method.getName().substring(4);
			}
			else
			{
				name =
					Character.toLowerCase(method.getName().charAt(2))
						+ method.getName().substring(3);
			}
			type = (Class<Z>) method.getReturnType();
		}
		else if (method.getName().startsWith("set"))
		{
			setMethod = method;
			declaringClass = (Class<Y>) method.getDeclaringClass();
			name =
				Character.toLowerCase(method.getName().charAt(3)) + method.getName().substring(4);
			type = (Class<Z>) method.getParameterTypes()[0];
		}
	}

	@SuppressWarnings("unchecked")
	public Property(Field field)
	{
		this.field = field;
		declaringClass = (Class<Y>) field.getDeclaringClass();
		name = field.getName();
		type = (Class<Z>) field.getType();
	}

	public Property(Class<Y> declaringClass, String name, Class<Z> type, Field field,
			Method getMethod, Method setMethod)
	{
		this.declaringClass = declaringClass;
		this.name = name;
		this.type = type;
		this.field = field;
		this.getMethod = getMethod;
		this.setMethod = setMethod;
	}

	/**
	 * Voegt de gegeven property samen met deze property. Beide properties moeten dezelfde
	 * naam hebben. Deze methode kan gebruikt worden om meerdere Property objecten voor de
	 * getter/setter/field samen te voegen tot 1.
	 * 
	 * @param other
	 *            Het object waar deze Property mee samengevoegd moet worden.
	 */
	@SuppressWarnings("unchecked")
	public void merge(Property< ? , ? , ? > other)
	{
		if (other.getDeclaringClass().isAssignableFrom(declaringClass))
			declaringClass = (Class<Y>) other.getDeclaringClass();
		if (other.getType().isAssignableFrom(type) || other.getGetMethod() != null
			|| other.getSetMethod() != null)
			type = (Class<Z>) other.getType();
		if (field == null)
			field = other.getField();
		if (getMethod == null)
			getMethod = other.getGetMethod();
		if (setMethod == null)
			setMethod = other.getSetMethod();
	}

	public Class<Y> getDeclaringClass()
	{
		return declaringClass;
	}

	public String getName()
	{
		return name;
	}

	/**
	 * @return Geeft de path naar het property, defaults op getName()
	 */
	public String getPath()
	{
		return getName();
	}

	/**
	 * @return Geeft de class terug van de parent, defaults op getDeclaringClass().
	 */
	@SuppressWarnings("unchecked")
	public Class<X> getBaseClass()
	{
		return (Class<X>) getDeclaringClass();
	}

	public Class<Z> getType()
	{
		return type;
	}

	public Field getField()
	{
		return field;
	}

	public Method getGetMethod()
	{
		return getMethod;
	}

	public Method getSetMethod()
	{
		return setMethod;
	}

	/**
	 * Zoekt naar de gegeven annotation op het field, de getter en de setter, in die
	 * volgorde.
	 * 
	 * @param clazz
	 *            De annotation om naar te zoeken.
	 * @return True als de annotation aanwezig is, anders false.
	 */
	public boolean isAnnotationPresent(Class< ? extends Annotation> clazz)
	{
		return getAnnotation(clazz) != null;
	}

	/**
	 * Zoekt naar de gegeven annotation op het field, de getter en de setter, in die
	 * volgorde.
	 * 
	 * @param <T>
	 *            Het type van de annotation.
	 * @param clazz
	 *            De annotation om naar te zoeken.
	 * @return De gevonden annotation of null.
	 */
	public <T extends Annotation> T getAnnotation(Class<T> clazz)
	{
		Class< ? > annotationDeclaringClass = null;
		T ret = null;
		if (field != null && field.isAnnotationPresent(clazz))
		{
			ret = field.getAnnotation(clazz);
			annotationDeclaringClass = field.getDeclaringClass();
		}

		if (getMethod != null && getMethod.isAnnotationPresent(clazz))
		{
			if (annotationDeclaringClass == null
				|| !getMethod.getDeclaringClass().isAssignableFrom(annotationDeclaringClass))
			{
				ret = getMethod.getAnnotation(clazz);
				annotationDeclaringClass = getMethod.getDeclaringClass();
			}
		}
		if (setMethod != null && setMethod.isAnnotationPresent(clazz))
		{
			if (annotationDeclaringClass == null
				|| !setMethod.getDeclaringClass().isAssignableFrom(annotationDeclaringClass))
			{
				ret = setMethod.getAnnotation(clazz);
			}
		}
		return ret;
	}

	public boolean isWriteAllowed()
	{
		return field != null || setMethod != null;
	}

	public boolean isReadAllowed()
	{
		return field != null || getMethod != null;
	}

	public Z getValueNull(Object base)
	{
		return getValue(base);
	}

	@SuppressWarnings("unchecked")
	public Z getValue(Object base)
	{
		if (getMethod != null)
			return (Z) ReflectionUtil.invokeMethod(getMethod, base);
		if (field != null)
			try
			{
				field.setAccessible(true);
				return (Z) field.get(base);
			}
			catch (IllegalArgumentException e)
			{
				throw new InvocationFailedException(e);
			}
			catch (IllegalAccessException e)
			{
				throw new InvocationFailedException(e);
			}
		throw new UnsupportedOperationException(declaringClass.getName() + "#" + getName()
			+ " is niet leesbaar");
	}

	public void setValue(Object base, Object value)
	{
		if (setMethod != null)
			ReflectionUtil.invokeMethod(setMethod, base, value);
		else if (field != null)
			try
			{
				field.setAccessible(true);
				field.set(base, value);
			}
			catch (IllegalArgumentException e)
			{
				throw new InvocationFailedException(e);
			}
			catch (IllegalAccessException e)
			{
				throw new InvocationFailedException(e);
			}
		else
			throw new UnsupportedOperationException(declaringClass.getName() + "#" + getName()
				+ " is niet schrijfbaar");
	}

	@Override
	public int hashCode()
	{
		int ret = declaringClass.hashCode();
		ret ^= name.hashCode();
		ret ^= type.hashCode();
		if (field != null)
			ret ^= field.hashCode();
		if (getMethod != null)
			ret ^= getMethod.hashCode();
		if (setMethod != null)
			ret ^= setMethod.hashCode();
		return ret;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Property< ? , ? , ? >)
		{
			Property< ? , ? , ? > other = (Property< ? , ? , ? >) obj;
			return declaringClass.equals(other.declaringClass) && name.equals(other.name)
				&& type.equals(other.type) && JavaUtil.equalsOrBothNull(field, other.field)
				&& JavaUtil.equalsOrBothNull(getMethod, other.getMethod)
				&& JavaUtil.equalsOrBothNull(setMethod, other.setMethod);
		}
		return false;
	}

	@Override
	public String toString()
	{
		return type + " " + declaringClass + "." + name;
	}
}
