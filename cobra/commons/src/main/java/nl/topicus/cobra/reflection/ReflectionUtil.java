package nl.topicus.cobra.reflection;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.reflection.factory.EmbeddedPropertyFactory;
import nl.topicus.cobra.reflection.factory.IPropertyFactory;
import nl.topicus.cobra.reflection.factory.PropertyFactory;
import nl.topicus.cobra.util.StringUtil;

/**
 * Bevat een aantal utility methods voor reflection. Deze methodes zijn vooral gericht op
 * het zoeken en aanroepen van methodes en constructors en het zoeken naar properties.
 * 
 * @author papegaaij
 */
public final class ReflectionUtil
{
	private ReflectionUtil()
	{
	}

	/**
	 * Zoekt een {@link Property}. kost duur!
	 * 
	 * @param clazz
	 *            De class om in te zoeken.
	 * @param propertyName
	 *            De naam van de property on te zoeken.
	 * @return De {@link Property} of null.
	 * @see ReflectionUtil#findProperties(Class)
	 */
	public static <X> Property<X, ? , ? > findProperty(Class<X> clazz, String propertyName)
	{
		if (StringUtil.isEmpty(propertyName))
			return null;

		String[] propertyPath = propertyName.split("\\.");
		Map<String, Property<X, ? , ? >> properties =
			internalFindProperties(new PropertyFactory<X>(), clazz);

		for (int i = 0; i < propertyPath.length - 1; i++)
		{
			Property<X, ? , ? > currentProperty = properties.get(propertyPath[i]);
			// geen property? dan is het pad fout!
			if (currentProperty == null)
				return null;

			properties =
				internalFindProperties(new EmbeddedPropertyFactory<X>(currentProperty),
					currentProperty.getType());
		}

		return properties.get(propertyPath[propertyPath.length - 1]);
	}

	/**
	 * Zoekt alle {@link Property Properties} van een class. Bij het doorzoeken worden
	 * niet alleen public getters en setters doorzocht, maar ook (private) fields. Het is
	 * hierbij dus van belang dat een field dezelfde naam heeft als de property, anders
	 * kan een enkele property meerdere keren gevonden worden. Bij getters en setters die
	 * door Eclipse gegenereerd worden, of objecten die voor Hibernate gebruikt worden zou
	 * dit altijd het geval moeten zijn.
	 * 
	 * @param clazz
	 *            De class om te doorzoeken.
	 * @return Een lijst met alle properties.
	 */
	public static <X> List<Property<X, ? , ? >> findProperties(Class<X> clazz)
	{
		return new ArrayList<Property<X, ? , ? >>(internalFindProperties(new PropertyFactory<X>(),
			clazz).values());
	}

	/**
	 * Zoekt alle {@link Property Properties} van een property. Bij het doorzoeken worden
	 * niet alleen public getters en setters doorzocht, maar ook (private) fields. Het is
	 * hierbij dus van belang dat een field dezelfde naam heeft als de property, anders
	 * kan een enkele property meerdere keren gevonden worden. Bij getters en setters die
	 * door Eclipse gegenereerd worden, of objecten die voor Hibernate gebruikt worden zou
	 * dit altijd het geval moeten zijn.
	 * 
	 * @param parentProperty
	 *            De parentProperty om te doorzoeken.
	 * @return Een lijst met alle properties.
	 */
	public static <X> List<Property<X, ? , ? >> findProperties(Property<X, ? , ? > parentProperty)
	{
		return new ArrayList<Property<X, ? , ? >>(internalFindProperties(
			new EmbeddedPropertyFactory<X>(parentProperty), parentProperty.getType()).values());
	}

	/**
	 * Doorzoekt de gegeven class op properties.
	 * 
	 * @param clazz
	 *            De class om te doorzoeken.
	 * @return De lijst met properties.
	 */
	private static <X> Map<String, Property<X, ? , ? >> internalFindProperties(
			IPropertyFactory<X> factory, Class< ? > clazz)
	{
		Map<String, Property<X, ? , ? >> properties =
			new LinkedHashMap<String, Property<X, ? , ? >>();

		// verzamel alle velden van de class en zijn superclasses
		Class< ? > checkClass = clazz;
		while (checkClass != null)
		{
			for (Field curField : checkClass.getDeclaredFields())
			{
				int modifiers = curField.getModifiers();
				if (!Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers))
					properties.put(curField.getName(), factory.createProperty(curField));
			}
			checkClass = checkClass.getSuperclass();
		}

		// doorzoek alle public methods van de class
		for (Method curMethod : clazz.getMethods())
		{
			// ignore bridge methods, aangezien deze niet bestaan in de source files
			if (curMethod.isBridge() && curMethod.isSynthetic())
			{
				continue;
			}

			int modifiers = curMethod.getModifiers();
			if (!Modifier.isStatic(modifiers))
			{
				Property<X, ? , ? > methodProperty = null;
				if (curMethod.getName().startsWith("get")
					&& curMethod.getParameterTypes().length == 0
					&& !Void.TYPE.equals(curMethod.getReturnType()))
				{
					methodProperty = factory.createProperty(curMethod);
				}
				else if (curMethod.getName().startsWith("is")
					&& curMethod.getParameterTypes().length == 0
					&& (Boolean.TYPE.equals(curMethod.getReturnType()) || Boolean.class
						.equals(curMethod.getReturnType())))
				{
					methodProperty = factory.createProperty(curMethod);
				}

				else if (curMethod.getName().startsWith("set")
					&& curMethod.getParameterTypes().length == 1
					&& Void.TYPE.equals(curMethod.getReturnType()))
				{
					methodProperty = factory.createProperty(curMethod);
				}
				// Als de method bij een property hoort, voeg hem toe aan de
				// lijst
				if (methodProperty != null)
				{
					if (properties.containsKey(methodProperty.getName()))
						properties.get(methodProperty.getName()).merge(methodProperty);
					else
						properties.put(methodProperty.getName(), methodProperty);
				}
			}
		}
		return properties;
	}

	/**
	 * Zoekt een methode op een object en roept deze aan. Als de methode niet gevonden kan
	 * worden, of een exception treed op tijdens de uitvoering van de methode, dan wordt
	 * een {@link InvocationFailedException} gegooid.
	 * 
	 * @param obj
	 *            Het object waar de methode op aangeroepen moet worden.
	 * @param name
	 *            De naam van de methode.
	 * @param args
	 *            De argumenten die meegegeven moeten worden.
	 * @return Het resultaat van de method call.
	 * @throws InvocationFailedException
	 *             als er een fout is opgetreden.
	 * @see #findMethod(boolean, Class, String, Class...)
	 */
	public static <T> Object invokeMethod(T obj, String name, Object... args)
			throws InvocationFailedException
	{
		try
		{
			Class< ? >[] argTypes = new Class< ? >[args.length];
			for (int count = 0; count < args.length; count++)
			{
				if (args[count] == null)
					argTypes[count] = null;
				else if (isWrapperClass(args[count].getClass()))
					argTypes[count] = getPrimitiveClass(args[count].getClass());
				else
					argTypes[count] = args[count].getClass();
			}
			Method method = findMethod(false, obj.getClass(), name, argTypes);
			return invokeMethod(method, obj, convertArguments(new Callable<T>(method), args));
		}
		catch (MethodNotFoundException e)
		{
			throw new InvocationFailedException(e);
		}
	}

	/**
	 * Zoekt een static methode op een object en roept deze aan. Als de methode niet
	 * gevonden kan worden, of een exception treed op tijdens de uitvoering van de
	 * methode, dan wordt een {@link InvocationFailedException} gegooid.
	 * 
	 * @param clazz
	 *            Het class waarvan de static methode aangeroepen moet worden.
	 * @param name
	 *            De naam van de methode.
	 * @param args
	 *            De argumenten die meegegeven moeten worden.
	 * @return Het resultaat van de method call.
	 * @throws InvocationFailedException
	 *             als er een fout is opgetreden.
	 * @see #findMethod(boolean, Class, String, Class...)
	 */
	public static <T> Object invokeStaticMethod(Class<T> clazz, String name, Object... args)
			throws InvocationFailedException
	{
		try
		{
			Class< ? >[] argTypes = new Class< ? >[args.length];
			for (int count = 0; count < args.length; count++)
			{
				if (args[count] == null)
					argTypes[count] = null;
				else if (isWrapperClass(args[count].getClass()))
					argTypes[count] = getPrimitiveClass(args[count].getClass());
				else
					argTypes[count] = args[count].getClass();
			}
			Method method = findMethod(true, clazz, name, argTypes);
			return invokeMethod(method, (Object) null, convertArguments(new Callable<T>(method),
				args));
		}
		catch (MethodNotFoundException e)
		{
			throw new InvocationFailedException(e);
		}
	}

	/**
	 * Roept een methode aan. Als een exception optreed tijdens de uitvoering van de
	 * methode, dan wordt een {@link InvocationFailedException} gegooid.
	 * 
	 * @param method
	 *            De aan te roepen methode.
	 * @param obj
	 *            Het object waar de methode op aangeroepen moet worden.
	 * @param args
	 *            De argumenten die meegegeven moeten worden.
	 * @return Het resultaat van de method call.
	 * @throws InvocationFailedException
	 *             als er een fout is opgetreden.
	 */
	public static <T> Object invokeMethod(Method method, T obj, Object... args)
			throws InvocationFailedException
	{
		try
		{
			return method.invoke(obj, convertArguments(new Callable<T>(method), args));
		}
		catch (IllegalAccessException e)
		{
			throw new InvocationFailedException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new InvocationFailedException(e);
		}
	}

	/**
	 * Zoekt op de class de most specific method met de gegeven naam en parameters. Het
	 * zoek algoritme is een implementatie van JLS 15.12.2. In het kort komt het er op
	 * neer dat ook methodes gevonden worden die als parameters superclasses hebben van de
	 * gegeven parameter types, boxen en/of unboxing conversions nodig zijn of varargs
	 * gebruikt worden.
	 * <p>
	 * LET OP: Omdat primitive parameters van de methode in boxed-form gegeven worden
	 * (Object array), kan deze methode geen onderscheid maken tussen primitives en
	 * wrappers. Er wordt daarom altijd uitgegaan van de primitive vorm.
	 * 
	 * @param staticMethod
	 *            Geeft aan of de methode static moet zijn of niet
	 * @param clazz
	 *            De class waar de methode in gezocht moet worden.
	 * @param name
	 *            De naam van de methode.
	 * @param args
	 *            De parameter types.
	 * @return De gevonden methode.
	 * @throws MethodNotFoundException
	 *             als de methode niet gevonden kon worden.
	 */
	public static <T> Method findMethod(boolean staticMethod, Class<T> clazz, String name,
			Class< ? >... args) throws MethodNotFoundException
	{
		Callable<T> ret = findCallable(Callable.getMethods(staticMethod, clazz, name), args);
		if (ret == null)
			throw new MethodNotFoundException(clazz, name, args);
		return ret.getMethod();
	}

	/**
	 * Zoekt een constructor op een object en roept deze aan. Als de constructor niet
	 * gevonden kan worden, of een exception treed op tijdens de uitvoering van de
	 * constructor, dan wordt een {@link InvocationFailedException} gegooid.
	 * 
	 * @param <T>
	 *            Het type van de class die geinstantieerd moet worden.
	 * @param clazz
	 *            De class die geinstantieerd moet worden.
	 * @param args
	 *            De argumenten die meegegeven moeten worden.
	 * @return Het geinstantieerde object.
	 * @throws InvocationFailedException
	 *             als er een fout is opgetreden.
	 * @see #findMethod(boolean, Class, String, Class...)
	 */
	public static <T> T invokeConstructor(Class<T> clazz, Object... args)
	{
		try
		{
			Class< ? >[] argTypes = getArgumentTypes(args);
			Constructor<T> constructor = findConstructor(clazz, argTypes);
			return constructor.newInstance(convertArguments(new Callable<T>(constructor), args));
		}
		catch (MethodNotFoundException e)
		{
			throw new InvocationFailedException(e);
		}
		catch (InstantiationException e)
		{
			throw new InvocationFailedException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new InvocationFailedException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new InvocationFailedException(e);
		}
	}

	private static Class< ? >[] getArgumentTypes(Object... args)
	{
		Class< ? >[] argTypes = new Class< ? >[args.length];
		for (int count = 0; count < args.length; count++)
			if (args[count] == null)
				argTypes[count] = null;
			else if (isWrapperClass(args[count].getClass()))
				argTypes[count] = getPrimitiveClass(args[count].getClass());
			else
				argTypes[count] = args[count].getClass();
		return argTypes;
	}

	/**
	 * Zoekt een constructor volgens hetzelfde algorithme als
	 * {@link #findMethod(boolean, Class, String, Class...)}.
	 * 
	 * @param <T>
	 *            Het type van de class.
	 * @param clazz
	 *            De class waar in gezocht moet worden.
	 * @param args
	 *            De parameter types.
	 * @return De gevonden constructor.
	 * @throws MethodNotFoundException
	 *             als de constructor niet gevonden kon worden.
	 */
	public static <T> Constructor<T> findConstructor(Class<T> clazz, Class< ? >... args)
			throws MethodNotFoundException
	{
		if (args.length == 0)
		{
			try
			{
				return clazz.getConstructor();
			}
			catch (NoSuchMethodException e)
			{
			}
		}
		Callable<T> ret = findCallable(Callable.getConstructors(clazz), args);
		if (ret == null)
			throw new MethodNotFoundException(clazz, "<init>", args);
		return ret.getConstructor();
	}

	/**
	 * Zoekt een constructor volgens hetzelfde algorithme als
	 * {@link #findMethod(boolean, Class, String, Class...)}.
	 * 
	 * @param <T>
	 *            Het type van de class.
	 * @param clazz
	 *            De class waar in gezocht moet worden.
	 * @param args
	 *            De parameters.
	 * @return De gevonden constructor.
	 * @throws MethodNotFoundException
	 *             als de constructor niet gevonden kon worden.
	 */
	public static <T> Constructor<T> findConstructor(Class<T> clazz, Object... args)
			throws MethodNotFoundException
	{
		Class< ? >[] argTypes = getArgumentTypes(args);
		return findConstructor(clazz, argTypes);
	}

	/**
	 * Converteert de argumenten voor de aanroep, in het bijzonder voor vararg aanroepen.
	 * 
	 * @param callable
	 *            De methode of constructor die aangeroepen moet worden.
	 * @param args
	 *            De gegeven argumenten.
	 * @return De geconverteerde argumenten.
	 */
	private static <T> Object[] convertArguments(Callable<T> callable, Object[] args)
	{
		if (!callable.isVariableArity())
			return args;

		int size = callable.getParameterTypes().length;
		Object[] ret = new Object[size];
		System.arraycopy(args, 0, ret, 0, size - 1);
		int varargSize = args.length - size + 1;
		Object vararg =
			Array
				.newInstance(callable.getParameterTypes()[size - 1].getComponentType(), varargSize);
		ret[size - 1] = vararg;
		for (int curParm = 0; curParm < varargSize; curParm++)
		{
			Array.set(vararg, curParm, args[size - 1 + curParm]);
		}
		return ret;
	}

	/**
	 * Implementeert de supertype relatie, zoals beschreven in JLS 4.10 (classA :&gt;
	 * classB).
	 * 
	 * @param classA
	 * @param classB
	 *            , mag null zijn voor het type van 'null' (anytype)
	 * @return Als classA een supertype is van classB.
	 */
	public static boolean isSupertypeOf(Class< ? > classA, Class< ? > classB)
	{
		if (classA == classB)
			return true;
		if (classA.isPrimitive())
		{
			if (Short.TYPE.equals(classA))
				return Byte.TYPE.equals(classB);
			if (Integer.TYPE.equals(classA))
				return Character.TYPE.equals(classB) || Short.TYPE.equals(classB)
					|| Byte.TYPE.equals(classB);
			if (Long.TYPE.equals(classA))
				return Integer.TYPE.equals(classB) || Character.TYPE.equals(classB)
					|| Short.TYPE.equals(classB) || Byte.TYPE.equals(classB);
			if (Float.TYPE.equals(classA))
				return Long.TYPE.equals(classB) || Integer.TYPE.equals(classB)
					|| Character.TYPE.equals(classB) || Short.TYPE.equals(classB)
					|| Byte.TYPE.equals(classB);
			if (Double.TYPE.equals(classA))
				return Float.TYPE.equals(classB) || Long.TYPE.equals(classB)
					|| Integer.TYPE.equals(classB) || Character.TYPE.equals(classB)
					|| Short.TYPE.equals(classB) || Byte.TYPE.equals(classB);
			return false;
		}
		return classB == null || classA.isAssignableFrom(classB);
	}

	/**
	 * Kijkt of het type van een expressie geconverteerd kan worden naar het type van een
	 * methode parameter, zoals gedefinieerd in JLS 5.3.
	 * 
	 * @param classParam
	 *            Het type van de methode parameter.
	 * @param classExpr
	 *            Het type van de expressie.
	 * @return True als de expressie geconverteerd kan worden naar het type van de
	 *         parameter.
	 */
	public static boolean isMethodConvertableTo(Class< ? > classParam, Class< ? > classExpr)
	{
		// checks 5.1.1, 5.1.2 and 5.1.5
		if (isSupertypeOf(classParam, classExpr))
			return true;
		// check 5.1.7
		if (classExpr != null && classExpr.isPrimitive())
			return isSupertypeOf(classParam, getWrapperClass(classExpr));
		// check 5.1.8
		if (classExpr != null && isWrapperClass(classExpr))
			return isSupertypeOf(classParam, getPrimitiveClass(classExpr));
		return false;
	}

	/**
	 * Geeft de primitive class voor de gegeven wrapper. Dit is de inverse van
	 * {@link #getWrapperClass(Class)}.
	 * 
	 * @param wrapper
	 *            De wrapper class.
	 * @return De primitive class die bij de wrapper class hoort.
	 */
	public static Class< ? > getPrimitiveClass(Class< ? > wrapper)
	{
		if (wrapper.equals(Boolean.class))
			return Boolean.TYPE;
		else if (wrapper.equals(Character.class))
			return Character.TYPE;
		else if (wrapper.equals(Long.class))
			return Long.TYPE;
		else if (wrapper.equals(Integer.class))
			return Integer.TYPE;
		else if (wrapper.equals(Short.class))
			return Short.TYPE;
		else if (wrapper.equals(Byte.class))
			return Byte.TYPE;
		else if (wrapper.equals(Double.class))
			return Double.TYPE;
		else if (wrapper.equals(Float.class))
			return Float.TYPE;
		throw new IllegalArgumentException(wrapper + " is not a wrapper");
	}

	/**
	 * Geeft de wrapper class voor de gegeven primitive class. Dit is de inverse van
	 * {@link #getPrimitiveClass(Class)}.
	 * 
	 * @param primitive
	 *            De primitive class.
	 * @return De wrapper class die bij de primitive class hoort.
	 */
	public static Class< ? > getWrapperClass(Class< ? > primitive)
	{
		if (primitive.equals(Boolean.TYPE))
			return Boolean.class;
		else if (primitive.equals(Character.TYPE))
			return Character.class;
		else if (primitive.equals(Long.TYPE))
			return Long.class;
		else if (primitive.equals(Integer.TYPE))
			return Integer.class;
		else if (primitive.equals(Short.TYPE))
			return Short.class;
		else if (primitive.equals(Byte.TYPE))
			return Byte.class;
		else if (primitive.equals(Double.TYPE))
			return Double.class;
		else if (primitive.equals(Float.TYPE))
			return Float.class;
		throw new IllegalArgumentException(primitive + " is not a primitive");
	}

	/**
	 * Kijkt of de gegeven class een wrapper class is. Wrapper classes zijn: Boolean,
	 * Character, Long, Integer, Short, Byte, Double en Float.
	 * 
	 * @param clazz
	 *            De mogelijke wrapper class.
	 * @return True als de gegeven class een wrapper is, anders false.
	 */
	public static boolean isWrapperClass(Class< ? > clazz)
	{
		return clazz.equals(Boolean.class) || clazz.equals(Character.class)
			|| clazz.equals(Long.class) || clazz.equals(Integer.class) || clazz.equals(Short.class)
			|| clazz.equals(Byte.class) || clazz.equals(Double.class) || clazz.equals(Float.class);
	}

	private static <T> Callable<T> findCallable(List<Callable<T>> callables, Class< ? >... args)
	{
		Callable<T> ret = findCallablePhase1(callables, args);
		if (ret != null)
			return ret;
		ret = findCallablePhase2(callables, args);
		if (ret != null)
			return ret;
		return findCallablePhase3(callables, args);
	}

	private static <T> Callable<T> findCallablePhase1(List<Callable<T>> callables,
			Class< ? >... args)
	{
		List<Callable<T>> applicable = new ArrayList<Callable<T>>();
		for (Callable<T> curCallable : callables)
		{
			if (args.length == curCallable.getParameterTypes().length)
			{
				boolean fits = true;
				for (int arg = 0; arg < args.length; arg++)
				{
					if (!isSupertypeOf(curCallable.getParameterTypes()[arg], args[arg]))
					{
						fits = false;
						break;
					}
				}
				if (fits)
				{
					addApplicableMethod(applicable, curCallable);
				}
			}
		}

		if (applicable.size() == 0)
		{
			return null;
		}
		if (applicable.size() > 1)
		{
			throw new AmbiguousMethodException(applicable, args);
		}

		return applicable.get(0);
	}

	private static <T> Callable<T> findCallablePhase2(List<Callable<T>> callables,
			Class< ? >... args)
	{
		List<Callable<T>> applicable = new ArrayList<Callable<T>>();
		for (Callable<T> curCallable : callables)
		{
			if (args.length == curCallable.getParameterTypes().length)
			{
				boolean fits = true;
				for (int arg = 0; arg < args.length; arg++)
				{
					if (!isMethodConvertableTo(curCallable.getParameterTypes()[arg], args[arg]))
					{
						fits = false;
						break;
					}
				}
				if (fits)
				{
					addApplicableMethod(applicable, curCallable);
				}
			}
		}

		if (applicable.size() == 0)
		{
			return null;
		}
		if (applicable.size() > 1)
		{
			throw new AmbiguousMethodException(applicable, args);
		}

		return applicable.get(0);
	}

	private static <T> Callable<T> findCallablePhase3(List<Callable<T>> callables,
			Class< ? >... args)
	{
		List<Callable<T>> applicable = new ArrayList<Callable<T>>();
		for (Callable<T> curCallable : callables)
		{
			int formalParmLength = curCallable.getParameterTypes().length;
			if (curCallable.isVariableArity() && args.length + 1 >= formalParmLength)
			{
				boolean fits = true;
				for (int arg = 0; arg < formalParmLength - 1; arg++)
				{
					if (!isMethodConvertableTo(curCallable.getParameterTypes()[arg], args[arg]))
					{
						fits = false;
						break;
					}
				}
				for (int arg = formalParmLength - 1; arg < args.length; arg++)
				{
					if (!isMethodConvertableTo(
						curCallable.getParameterTypes()[formalParmLength - 1].getComponentType(),
						args[arg]))
					{
						fits = false;
						break;
					}
				}

				if (fits)
				{
					addApplicableMethod(applicable, curCallable);
				}
			}
		}

		if (applicable.size() == 0)
		{
			return null;
		}
		if (applicable.size() > 1)
		{
			throw new AmbiguousMethodException(applicable);
		}

		return applicable.get(0);
	}

	/**
	 * Add the method to the list if non of the methods in the list is more specific than
	 * the given method, all methods that are less specific than the given method are
	 * removed from the list.
	 */
	private static <T> void addApplicableMethod(List<Callable<T>> allMethods, Callable<T> callable)
	{
		Iterator<Callable<T>> it = allMethods.iterator();
		while (it.hasNext())
		{
			Callable<T> curEntry = it.next();
			if (curEntry.isMoreSpecificThan(callable))
			{
				return;
			}
			else if (callable.isMoreSpecificThan(curEntry))
			{
				it.remove();
			}
		}
		allMethods.add(callable);
	}
}

/**
 * Wrapper class for a method or constructor, because these don't have a common (method
 * related) superclass.
 * 
 * @author papegaaij
 */
class Callable<T>
{
	private Method method;

	private Constructor<T> constructor;

	public Callable(Method method)
	{
		this.method = method;
	}

	public Callable(Constructor<T> constructor)
	{
		this.constructor = constructor;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<Callable<T>> getConstructors(Class<T> clazz)
	{
		List<Callable<T>> ret = new ArrayList<Callable<T>>();
		for (Constructor< ? > curConstructor : clazz.getConstructors())
		{
			ret.add(new Callable<T>((Constructor<T>) curConstructor));
		}
		return ret;
	}

	public static <T> List<Callable<T>> getMethods(boolean staticMethod, Class<T> clazz, String name)
	{
		List<Callable<T>> ret = new ArrayList<Callable<T>>();
		if (staticMethod)
		{
			for (Method curMethod : clazz.getDeclaredMethods())
			{
				if (Modifier.isStatic(curMethod.getModifiers())
					&& Modifier.isPublic(curMethod.getModifiers())
					&& curMethod.getName().equals(name))
					ret.add(new Callable<T>(curMethod));
			}
		}
		else
		{
			for (Method curMethod : clazz.getMethods())
			{
				if (curMethod.getName().equals(name))
					ret.add(new Callable<T>(curMethod));
			}
		}
		return ret;
	}

	public Method getMethod()
	{
		return method;
	}

	public Constructor<T> getConstructor()
	{
		return constructor;
	}

	public String getName()
	{
		if (method == null)
			return "<init>";
		return method.getName();
	}

	@SuppressWarnings("unchecked")
	public Class<T> getDeclaringClass()
	{
		if (method == null)
			return constructor.getDeclaringClass();
		return (Class<T>) method.getDeclaringClass();
	}

	public Class< ? >[] getParameterTypes()
	{
		if (method == null)
			return constructor.getParameterTypes();
		return method.getParameterTypes();
	}

	public boolean isVariableArity()
	{
		if (method == null)
			return constructor.isVarArgs();
		return method.isVarArgs();
	}

	@Override
	public String toString()
	{
		if (method == null)
			return constructor.toString();
		return method.toString();
	}

	public boolean isMoreSpecificThan(Callable< ? > o)
	{
		if (isVariableArity() || o.isVariableArity())
			throw new UnsupportedOperationException(
				"method overloading with varargs not yet supported");
		if (getParameterTypes().length != o.getParameterTypes().length)
			throw new IllegalArgumentException("Parameter count not equal");
		for (int count = 0; count < getParameterTypes().length; count++)
		{
			Class< ? > thisParam = getParameterTypes()[count];
			Class< ? > otherParam = o.getParameterTypes()[count];
			if (!ReflectionUtil.isSupertypeOf(otherParam, thisParam))
				return false;
		}
		return true;
	}
}
