package nl.topicus.cobra.templates.resolvers;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.templates.FieldInfo;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.StringUtil;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resolvet een field m.b.v. beanutils. Notaties als entity.property, map.key, map(key),
 * list[index] en combinaties zijn mogelijk. Daaraan is de list[*].property-notatie
 * toegevoegd om properties van alle elementen uit de lijst op te vragen. De context kan
 * een map, een enkelvoudig object of een list zijn.
 * 
 * @author Laurens Hop
 */
public class BeanPropertyResolver implements FieldResolver
{
	private static final Logger log = LoggerFactory.getLogger(BeanPropertyResolver.class);

	/**
	 * The delimiter that preceeds the zero-relative subscript for an indexed reference.
	 * 
	 */
	public static final char INDEXED_DELIM = '[';

	/**
	 * The delimiter that follows the zero-relative subscript for an indexed reference.
	 * 
	 */
	public static final char INDEXED_DELIM2 = ']';

	/**
	 * The delimiter that preceeds the key of a mapped property.
	 * 
	 */
	public static final char MAPPED_DELIM = '(';

	/**
	 * The delimiter that follows the key of a mapped property.
	 * 
	 */
	public static final char MAPPED_DELIM2 = ')';

	/**
	 * The delimiter that separates the components of a nested reference.
	 * 
	 */
	public static final char NESTED_DELIM = '.';

	/**
	 * Foutmelding bij niet bestaande veldnamen
	 */
	protected class BeanPropertyException extends Exception
	{
		private static final long serialVersionUID = 1L;

		public BeanPropertyException(String message)
		{
			super(message);
		}
	}

	private final Object context;

	private final Map<String, Iterator<Object>> iterators;

	private Map<String, Object> currentObjects;

	/**
	 * Maakt een nieuwe Bean Property Resolver aan op basis van een bepaalde context (map
	 * of bean).
	 * 
	 * @param context
	 */
	public BeanPropertyResolver(Object context)
	{
		this.context = context;
		this.iterators = new HashMap<String, Iterator<Object>>();
		this.currentObjects = new HashMap<String, Object>();
	}

	/**
	 * @see nl.topicus.cobra.templates.resolvers.FieldResolver#resolve(java.lang.String)
	 */
	public Object resolve(String name)
	{
		String subname = name;
		String iteratorKeyName =
			(subname.contains(".") ? subname.substring(0, subname.indexOf(".")) : subname);
		Object iteratorCurrentObject = null;
		if (currentObjects.containsKey(iteratorKeyName))
		{
			iteratorCurrentObject = currentObjects.get(iteratorKeyName);

			if (!subname.contains("."))
				return iteratorCurrentObject;
			else if (subname.substring(subname.indexOf(".")).length() > 0)
			{
				subname = subname.substring(subname.indexOf(".") + 1);
				return resolve(iteratorCurrentObject, subname);
			}
		}

		return resolve(context, subname);
	}

	/**
	 * Zoekt op basis van propertynaam een object in de gegeven context
	 */
	@SuppressWarnings("unchecked")
	protected Object resolve(Object currentContext, String name)
	{
		Object result = "";
		try
		{
			int indexAll = name.indexOf("[*]");
			int indexCurrent = name.indexOf("[#]");
			int indexMapping = name.indexOf("(");
			if (indexAll >= 0)
			{
				String listName = name.substring(0, indexAll);
				String restName = name.substring(indexAll + 3);
				if (restName.startsWith("."))
					restName = restName.substring(1);
				Object listAsObject = PropertyUtils.getProperty(currentContext, listName);
				if (listAsObject instanceof Iterable)
				{
					Iterable<Object> list = (Iterable<Object>) listAsObject;
					List<Object> resultList = new ArrayList<Object>();
					Iterator<Object> it = list.iterator();
					while (it.hasNext())
					{
						if (restName.length() > 0)
							resultList.add(resolve(it.next(), restName));
						else
							resultList.add(it.next());
					}
					result = resultList;
				}
			}
			else if (indexCurrent >= 0)
			{
				String listName = name.substring(0, indexCurrent);
				// currentListName = listName;

				String restName = name.substring(indexCurrent + 3);
				if (restName.startsWith("."))
					restName = restName.substring(1);
				Object currentObject = currentObjects.get(listName);
				if (currentObject == null)
					currentObject = next(listName);
				if (currentObject != null)
				{
					if (restName.length() > 0)
						result = resolve(currentObject, restName);
					else
						result = currentObject;
				}
			}
			else if (indexMapping >= 0)
			{
				indexMapping = name.substring(0, indexMapping).lastIndexOf(NESTED_DELIM);
				int indexMapping2 = name.indexOf(")");

				String listName = name.substring(0, indexMapping);
				/*
				 * regex verwacht voor een elkele \ en dus moet alles 2x.
				 */
				String mappingName =
					name.substring(indexMapping + 1, indexMapping2 + 1).replaceAll("'", "");
				String restName = name.substring(indexMapping2 + 1);
				if (restName.startsWith("."))
					restName = restName.substring(1);
				Object mappingAsObject = PropertyUtils.getProperty(currentContext, listName);
				mappingAsObject = PropertyUtils.getMappedProperty(mappingAsObject, mappingName);
				if (mappingAsObject != null)
				{
					if (restName.length() > 0)
						result = resolve(mappingAsObject, restName);
					else
						result = mappingAsObject;
				}
			}
			else
				result = PropertyUtils.getProperty(currentContext, name);
			// null properties worden vervangen door een lege string
			if (result == null)
				result = "";
		}
		catch (NestedNullException e)
		{
			// silently ignore
		}
		catch (Exception e)
		{
			// niet teveel loggen, anders loopt de log zo vol door fouten van de gebruiker
			// (fout template)
			log.warn(e.getMessage());
		}
		return result;
	}

	/**
	 * Geeft fieldinfo van deze property op basis van de BeanUtils notatie.
	 * 
	 * @see nl.topicus.cobra.templates.resolvers.FieldResolver#getInfo(java.lang.String)
	 */
	@Override
	public FieldInfo getInfo(String name)
	{
		if (context instanceof Map< ? , ? >)
		{
			String firstName = name;
			int indexOfNESTED_DELIM = name.indexOf(NESTED_DELIM);
			if (indexOfNESTED_DELIM >= 0)
				firstName = name.substring(0, indexOfNESTED_DELIM);

			String nameNoIndex;
			boolean indexed = false;
			if (firstName.contains(Character.toString(INDEXED_DELIM)))
			{
				nameNoIndex = firstName.substring(0, firstName.indexOf(INDEXED_DELIM));
				indexed = true;
			}
			else
				nameNoIndex = firstName;

			Object bean = ((Map< ? , ? >) context).get(nameNoIndex);

			if (indexed)
			{
				if (!(bean instanceof Collection< ? >))
					return new FieldInfo(name, null, false, "Veld '" + nameNoIndex
						+ "' is geen lijst");
				bean = ((Collection< ? >) bean).iterator().next();
			}

			if (bean != null)
			{
				if (indexOfNESTED_DELIM < 0)
					return new FieldInfo(name, bean.getClass(), true, null);
				else
				{
					String next = name.substring(indexOfNESTED_DELIM + 1);
					FieldInfo info = getInfo(bean.getClass(), next);
					info.setName(firstName + NESTED_DELIM + info.getName());
					return info;
				}
			}
			return new FieldInfo(name, null, false, "Veld '" + name + "' bestaat niet");
		}
		return getInfo(context.getClass(), name);
	}

	/**
	 * @return <strong>name</strong> met de eerste letter vervangen door een hoofdletter
	 */
	private String firstCharUppercase(String name)
	{
		return new StringBuffer(name.length()).append(String.valueOf(name.charAt(0)).toUpperCase())
			.append(name.substring(1)).toString();
	}

	/**
	 * @return Het return type van de getProperty (of isProperty) methode van
	 *         <strong>beanClass</strong>.
	 */
	@SuppressWarnings("unchecked")
	private Class getSimplePropertyType(Class beanClass, String name) throws BeanPropertyException
	{
		Method getter = getGetter(beanClass, name);
		if (getter == null)
			throw new BeanPropertyException("Eigenschap '" + name + "' bestaat niet");
		return getter.getReturnType();
	}

	/**
	 * Als de gegeven method een generic type (bijv <code>List&lt;XX&gt;</code>)
	 * retourneert, dan geeft deze methode het actuele type-argument ( <code>XX</code>).
	 * In het geval van meerdere type-argumenten (bijv. <code>Map&lt;String, XX&gt;</code>
	 * ) geeft deze het laatste.
	 */
	private Class< ? > getGenericType(Method method)
	{
		Type generics = method.getGenericReturnType();
		return getGenericType(generics);
	}

	private Class< ? > getGenericType(Type generics)
	{
		Class< ? > result = Object.class;
		if (generics != null && generics instanceof ParameterizedType)
		{
			Type actual[] = ((ParameterizedType) generics).getActualTypeArguments();
			if (actual.length >= 1)
			{
				Type type = actual[actual.length - 1];
				if (type instanceof Class< ? >)
				{
					// List<XX>
					result = (Class< ? >) type;
				}
				else if (type instanceof WildcardType)
				{
					// List<? extends XX>
					try
					{
						Type bounds[] = ((WildcardType) type).getUpperBounds();
						if (bounds.length > 0 && bounds[0] instanceof Class< ? >)
						{
							result = (Class< ? >) bounds[0];
						}
					}
					catch (Exception e)
					{
						// do nothing
					}
				}
				else if (type instanceof ParameterizedType)
				{
					// List<XX<YY>>
					String typeName = type.toString();
					int indexBracket = typeName.indexOf("<");
					String className = typeName.substring(0, indexBracket);
					try
					{
						result = Class.forName(className);
					}
					catch (ClassNotFoundException e)
					{
						// do nothing
					}
				}
				else if (type instanceof TypeVariable< ? >)
				{
					// List<XX[]>
					try
					{
						Type bounds[] = ((TypeVariable< ? >) type).getBounds();
						if (bounds.length > 0 && bounds[0] instanceof Class< ? >)
						{
							result = (Class< ? >) bounds[0];
						}
					}
					catch (Exception e)
					{
						// do nothing
					}
				}
			}
		}
		return result;
	}

	/**
	 * Als <strong>name</strong> een indexed property is ( <code>property[index]</code>),
	 * geeft deze methode het type van die property terug. De methode
	 * <code>getProperty</code> van <strong>beanClass</strong> moet in dat geval een
	 * <code>List&lt;XX&gt;</code> teruggeven of een array (<code>XX[]</code>).
	 */
	@SuppressWarnings("unchecked")
	private Class getIndexedPropertyType(Class beanClass, String name) throws BeanPropertyException
	{
		int indexOfINDEXED_DELIM = name.indexOf(INDEXED_DELIM);
		String propertyName = name.substring(0, indexOfINDEXED_DELIM);
		Method getter = getGetter(beanClass, propertyName);
		if (getter == null)
			throw new BeanPropertyException("Eigenschap '" + name + "' bestaat niet");
		Class returnType = getter.getReturnType();
		if (returnType.isArray())
		{
			return returnType;
		}
		else if (Iterable.class.isAssignableFrom(returnType))
		{
			return getGenericType(getter);
		}
		// TODO: nog iets met getXX(Integer index) methoden
		return null;
	}

	/**
	 * Als <strong>name</strong> een mapped property is (<code>map(key)</code> of
	 * <code>map.key</code>), geeft deze methode het type van die property terug. De
	 * methode <code>getMap</code> van <strong>beanClass</strong> moet in dat geval een
	 * <code>Map&lt;String, XX&gt;</code> teruggeven.
	 */
	@SuppressWarnings("unchecked")
	private Class getMappedPropertyType(Class beanClass, String name) throws BeanPropertyException
	{
		int indexOfMAPPED_DELIM = name.indexOf(MAPPED_DELIM);
		int indexOfMAPPED_DELIM2 = name.indexOf(MAPPED_DELIM2);
		String propertyName = name;
		String argumentValue = null;

		if (indexOfMAPPED_DELIM >= 0)
			propertyName = name.substring(0, indexOfMAPPED_DELIM);
		if (indexOfMAPPED_DELIM2 >= 0)
			argumentValue = name.substring(indexOfMAPPED_DELIM + 1, indexOfMAPPED_DELIM2);

		Method getter = getGetter(beanClass, propertyName);
		if (getter == null && !StringUtil.isEmpty(argumentValue))
		{
			// controleer of er een argument is
			if (argumentValue != null)
				getter = getGetter(beanClass, propertyName, String.class);
		}
		if (getter == null)
			throw new BeanPropertyException("Eigenschap '" + name + "' bestaat niet");

		else if (Map.class.isAssignableFrom(getter.getReturnType())
			|| List.class.isAssignableFrom(getter.getReturnType()))
		{
			return getGenericType(getter);
		}
		return getter.getReturnType();
	}

	/**
	 * @return De get- of is- method van <strong>beanClass</strong> die bij property
	 *         <strong>propertyName</strong> hoort.
	 * @throws NoSuchMethodException
	 *             indien method niet gevonden.
	 */
	@SuppressWarnings("unchecked")
	private Method getGetter(Class beanClassParam, String propertyName, Class... parameterTypes)
			throws BeanPropertyException
	{
		Class beanClass = beanClassParam;
		Method getter = null;
		if (isProxyClass(beanClass))
		{
			beanClass = beanClass.getSuperclass();
		}

		if (propertyName != null && propertyName.length() >= 1
			&& (beanClass.isAnnotationPresent(Exportable.class) || isAbstractClass(beanClass)))
		{
			if (propertyName.charAt(0) >= 'A' && propertyName.charAt(0) <= 'Z')
			{
				if (propertyName.length() == 1
					|| !(propertyName.charAt(1) >= 'A' && propertyName.charAt(1) <= 'Z'))
				{
					throw new BeanPropertyException("Eigenschap '" + propertyName
						+ "' mag niet beginnen met een hoofdletter");
				}
			}
			try
			{
				if (parameterTypes.length == 0)
					getter = beanClass.getMethod("get" + firstCharUppercase(propertyName));
				else
					getter =
						beanClass.getMethod("get" + firstCharUppercase(propertyName),
							parameterTypes);
			}
			catch (NoSuchMethodException e)
			{
				// herkansing voor isXX methode (boolean)
				try
				{
					if (parameterTypes.length == 0)
						getter = beanClass.getMethod("is" + firstCharUppercase(propertyName));
					else
						getter =
							beanClass.getMethod("is" + firstCharUppercase(propertyName),
								parameterTypes);

					if (getter != null && !boolean.class.equals(getter.getReturnType()))
					{
						// alleen geldig indien getter een primitive boolean
						// teruggeeft.
						getter = null;
					}
				}
				catch (SecurityException e1)
				{
				}
				catch (NoSuchMethodException e1)
				{
					// herkansing voor methoden zonder get/is, zoals de size()
					// method van een list
					try
					{
						getter = beanClass.getMethod(propertyName);
					}
					catch (SecurityException e2)
					{
					}
					catch (NoSuchMethodException e2)
					{
					}
				}
			}
			catch (SecurityException e1)
			{
			}
		}
		if (getter != null)
		{
			boolean exportable = false;
			if (getter.isAnnotationPresent(Exportable.class))
				exportable = true;

			if (!exportable)
				getter = null;
		}

		return getter;
	}

	/**
	 * @param beanClass
	 * @return of het een abstract class is
	 */
	private boolean isAbstractClass(Class< ? > beanClass)
	{
		boolean isAbstract = false;

		int modifier = beanClass.getModifiers();
		if (Modifier.isAbstract(modifier))
			isAbstract = true;

		return isAbstract;
	}

	private boolean isProxyClass(Class< ? > beanClass)
	{
		// Dit is wel een beetje vies, maar we hebben hier geen hibernate, dus we kunnen
		// niet kijken of het een hibernate proxy is via de hibernate api
		for (Class< ? > curInterface : beanClass.getInterfaces())
		{
			if (curInterface.getName().equals("org.hibernate.proxy.HibernateProxy"))
				return true;
		}
		return false;
	}

	/**
	 * Geeft fieldinfo bij de gegeven contextClass. Bevat code gebaseerd op PropertyUtils.
	 * PropertyUtils was niet bruikbaar omdat deze alleen van geinstantieerde objecten het
	 * type kan teruggeven. Deze methode baseert alles op reflectie.
	 */
	@SuppressWarnings("unchecked")
	protected FieldInfo getInfo(Class contextClassParam, String name)
	{
		Class contextClass = contextClassParam;
		FieldInfo result = null;
		String currentName = name;
		try
		{
			// stuk ge-copy-paste uit PropertyUtilsBean
			int indexOfINDEXED_DELIM = -1;
			int indexOfMAPPED_DELIM = -1;
			int indexOfMAPPED_DELIM2 = -1;
			int indexOfNESTED_DELIM = -1;
			// lus ivm geneste properties (property.property.property)
			while (true)
			{
				indexOfNESTED_DELIM = currentName.indexOf(NESTED_DELIM);
				indexOfMAPPED_DELIM = currentName.indexOf(MAPPED_DELIM);
				indexOfMAPPED_DELIM2 = currentName.indexOf(MAPPED_DELIM2);
				if (indexOfMAPPED_DELIM2 >= 0 && indexOfMAPPED_DELIM >= 0
					&& (indexOfNESTED_DELIM < 0 || indexOfNESTED_DELIM > indexOfMAPPED_DELIM))
				{
					indexOfNESTED_DELIM = currentName.indexOf(NESTED_DELIM, indexOfMAPPED_DELIM2);
				}
				else
				{
					indexOfNESTED_DELIM = currentName.indexOf(NESTED_DELIM);
				}
				if (indexOfNESTED_DELIM < 0)
				{
					break;
				}
				String next = currentName.substring(0, indexOfNESTED_DELIM);
				indexOfINDEXED_DELIM = next.indexOf(INDEXED_DELIM);
				indexOfMAPPED_DELIM = next.indexOf(MAPPED_DELIM);
				if (Map.class.isAssignableFrom(contextClass) || indexOfMAPPED_DELIM >= 0)
				{
					contextClass = getMappedPropertyType(contextClass, next);
				}
				else if (indexOfINDEXED_DELIM >= 0)
				{
					contextClass = getIndexedPropertyType(contextClass, next);
				}
				else
				{
					contextClass = getSimplePropertyType(contextClass, next);
				}
				if (contextClass == null)
				{
					throw new NestedNullException("Null property value for '"
						+ currentName.substring(0, indexOfNESTED_DELIM) + "'");
				}
				currentName = currentName.substring(indexOfNESTED_DELIM + 1);
			}

			indexOfINDEXED_DELIM = currentName.indexOf(INDEXED_DELIM);
			indexOfMAPPED_DELIM = currentName.indexOf(MAPPED_DELIM);

			if (Map.class.isAssignableFrom(contextClass) || indexOfMAPPED_DELIM >= 0)
			{
				contextClass = getMappedPropertyType(contextClass, currentName);
			}
			else if (indexOfINDEXED_DELIM >= 0)
			{
				contextClass = getIndexedPropertyType(contextClass, currentName);
			}
			else
			{
				contextClass = getSimplePropertyType(contextClass, currentName);
			}
			result = new FieldInfo(name, contextClass, true, null);

		}
		catch (BeanPropertyException e)
		{
			result = new FieldInfo(name, null, false, name + ": " + e.getMessage());
		}
		return result;
	}

	/**
	 * Zorgt dat naar het volgende element van een lijst wordt gegaan.
	 * 
	 * @param name
	 *            naam van de lijst.
	 * @return het volgende element van de lijst. Null indien niet meer elementen.
	 */
	@SuppressWarnings("unchecked")
	public Object next(String name)
	{
		Object currentObject = null;
		Object listAsObject;
		try
		{
			listAsObject = PropertyUtils.getProperty(context, name);
			if (listAsObject instanceof Iterable)
			{
				Iterator<Object> it = iterators.get(name);
				if (it == null)
				{
					Iterable<Object> list = (Iterable<Object>) listAsObject;
					it = list.iterator();
					iterators.put(name, it);
				}
				if (it.hasNext())
				{
					currentObject = it.next();
				}
			}
		}
		catch (Exception e)
		{
			// silently ignore; return null
		}

		currentObjects.put(name, currentObject);
		return currentObject;
	}
}
