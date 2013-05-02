/*
 * Copyright (c) 2005-2009, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class voor Java zaken zoals het verkrijgen van de class op basis van een
 * String.
 * 
 * @author Martijn Dashorst
 */
public final class JavaUtil
{
	/** Voor logging. */
	private static final Logger log = LoggerFactory.getLogger(JavaUtil.class);

	/**
	 * Geeft de class terug gebaseerd op de classname.
	 * 
	 * @param classname
	 *            de fully qualified classname van de class
	 * @return <code>null</code> als er geen class gevonden kan worden.
	 */
	public static Class< ? > getClass(String classname)
	{
		try
		{
			return Class.forName(classname);
		}
		catch (ClassNotFoundException e)
		{
			log.debug("Kan class voor " + classname + " niet verkrijgen", e);
		}
		catch (NoClassDefFoundError e)
		{
			log.error(e.getMessage(), e);
		}
		// geef null terug.
		return null;
	}

	/**
	 * Utility class constructor.
	 */
	private JavaUtil()
	{
		// niets te doen.
	}

	/**
	 * Ontdubbeld de items van deze lijst. De lijst zelf wordt gewijzigd.
	 * 
	 * @param <T>
	 *            het type van de lijst
	 * @param list
	 *            de lijst;
	 * @return de (gewijzigde) lijst na ontdubbeling
	 */
	public static <T> List<T> removeEqualItems(List<T> list)
	{
		if (list == null || list.isEmpty())
			return list;
		T temp = null;
		List<T> sublist;
		for (int i = 0; i < list.size() - 1; i++)
		{
			temp = list.get(i);
			sublist = list.subList(i + 1, list.size());
			// mischien nog wat doen aan performance omdat remove alle volgende items
			// een index omhoog laat opschuiven
			while (sublist.remove(temp))
			{// remove all occurances
			}
		}
		return list;
		// om de performance te boosten zouden we alles in een array kunnen dumpen,
		// daar de dubbelen uitzoeken, indien die gevonden worden de lijst leeggooien en
		// weer vullen met wat er over is in de array
	}

	/**
	 * Maakt een nieuwe lijst die alle items bevat.
	 * 
	 * <pre>
	 *   asList(5); leverd een lijst van Integers op met 1 item in de lijst nl. 5.
	 *   List&lt;String&gt; temp=asList(); leverd een lege lijst op van Strings.
	 *   List&lt;String&gt; temp=asList((String)null); leverd een lijst van Strings op met 1 item nl. null.
	 *   List&lt;Object&gt; temp=asList((Object[])null); leverd een lege lijst op van Objecten.
	 * </pre>
	 * 
	 * @param <T>
	 *            het type van de items
	 * @param items
	 *            een x aantal items
	 * @return een nieuwe lijst, leeg indien geen items zijn opgegeven.
	 */
	public static <T> List<T> asList(T... items)
	{
		int length = 0;
		if (items != null)
			length = items.length;
		ArrayList<T> list = new ArrayList<T>(length);
		if (items != null)
		{
			for (T item : items)
				list.add(item);
		}
		return list;
	}

	public static boolean equalsOrBothNull(String string1, String string2)
	{
		if (string1 == null && string2 == null)
		{
			return true;
		}
		if (string1 != null && string2 != null)
		{
			return string1.equals(string2);
		}
		return false;
	}

	/**
	 * Controleert of beide strings gelijk aan elkaar zijn, rekeninghoudend met lege
	 * strings en null waarden.
	 * <p>
	 * "".equals(null) levert in dit geval <code>true</code> op.
	 */
	public static boolean equalsOrBothEmpty(String string1, String string2)
	{
		if (StringUtil.isEmpty(string1) && StringUtil.isEmpty(string2))
		{
			return true;
		}
		if (string1 != null && string2 != null)
		{
			return string1.equals(string2);
		}
		return false;
	}

	public static boolean equalsOrBothNull(Object obj1, Object obj2)
	{
		if (obj1 == null && obj2 == null)
		{
			return true;
		}
		if (obj1 != null && obj2 != null)
		{
			return obj1.equals(obj2);
		}
		return false;
	}

	public static boolean bigDecimalEqualsOrBothNull(BigDecimal obj1, BigDecimal obj2)
	{
		if (obj1 == null && obj2 == null)
		{
			return true;
		}
		if (obj1 != null && obj2 != null)
		{
			return obj1.compareTo(obj2) == 0;
		}
		return false;
	}

	public static boolean containsSameItemsInOrder(List< ? > col1, List< ? > col2)
	{
		if (col1 == col2)
			return true;
		if (col1 == null || col2 == null)
			return false;
		if (col1.size() != col2.size())
			return false;
		Iterator< ? > it1 = col1.iterator();
		Iterator< ? > it2 = col2.iterator();
		while (it1.hasNext())
		{
			if (!it1.next().equals(it2.next()))
				return false;
		}
		return true;
	}

	public static boolean containsSameItems(Collection< ? > col1, Collection< ? > col2)
	{
		if (col1 == col2)
			return true;
		if (col1 == null || col2 == null)
			return false;
		if (col1.size() != col2.size())
			return false;
		List<Object> checkList = new ArrayList<Object>(col1);
		checkList.removeAll(col2);
		return checkList.isEmpty();
	}

	/**
	 * Gets the classname of a class, without the package.
	 * 
	 * @param class1
	 * @return just the classname or null if the class is null
	 */
	public static String getClassName(Class< ? > class1)
	{
		if (class1 == null)
			return null;
		String fullName = class1.getName();
		int index = fullName.lastIndexOf('.');
		return fullName.substring(index + 1);
	}

	public static BitSet longToBitSet(long bits)
	{
		BitSet ret = new BitSet();
		for (int count = 0;; count++)
		{
			long curBit = 1L << count;
			ret.set(count, (bits & curBit) != 0);
			if (curBit >= bits)
				break;
		}
		return ret;
	}

	public static long bitSetToLong(BitSet set)
	{
		int max = set.length();
		long bits = 0;
		for (int count = 0; count < max; count++)
		{
			if (set.get(count))
				bits |= (1L << count);
		}
		return bits;
	}

	public static boolean areNullOrEmpty(Object... values)
	{
		for (Object value : values)
		{
			if (StringUtil.isNotEmpty(value))
				return false;
		}
		return true;
	}

	public static boolean areNotNullOrEmpty(Object... values)
	{
		for (Object value : values)
		{
			if (StringUtil.isNotEmpty(value))
				return true;
		}
		return false;
	}

	public static Integer nullOrInteger(String value)
	{
		if (value != null)
		{
			try
			{
				return Integer.valueOf(value);
			}
			catch (NumberFormatException e)
			{
				log.info("Kon " + value + " niet converteren naar een Integer", e);
				return null;
			}
		}
		return null;
	}

	public static Integer nullOrInteger(Long value)
	{
		if (value != null)
		{
			return value.intValue();
		}
		return null;
	}
}
