package nl.topicus.cobra.util;

import java.util.Collection;
import java.util.Iterator;

public class CollectionUtil
{
	/**
	 * @return een int array van alle elementen uit de <tt>collection</tt>.
	 */
	public static int[] toIntArray(Collection<Integer> collection)
	{
		int[] res = new int[collection.size()];
		Iterator<Integer> iterator = collection.iterator();
		for (int i = 0; i < collection.size(); i++)
		{
			res[i] = iterator.next();
		}
		return res;
	}

	/**
	 * Voegt alle <tt>values</tt> toe aan de <tt>collection</tt>.
	 * 
	 * @return de <tt>collection</tt>
	 */
	public static <T extends Collection<Integer>> T addAll(T collection, int... values)
	{
		for (int value : values)
		{
			collection.add(value);
		}
		return collection;
	}

	/**
	 * Bepaalt de hoogste waarde in de lijst van integers.
	 */
	public static int max(int... values)
	{
		int max = Integer.MIN_VALUE;
		for (int value : values)
		{
			if (value > max)
			{
				max = value;
			}
		}
		return max;
	}
}
