package nl.topicus.cobra.comparators;

import java.io.Serializable;
import java.util.Comparator;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.reflection.ReflectionUtil;

public class MultiFieldComparator implements Comparator<Object>, Serializable
{
	private static final long serialVersionUID = 1L;

	private String[] properties;

	private int factor;

	public MultiFieldComparator(String... properties)
	{
		this(false, properties);
	}

	public MultiFieldComparator(boolean reverse, String... properties)
	{
		this.factor = reverse ? -1 : 1;
		this.properties = properties;
	}

	/**
	 * kost aller duurst! Zie {@link ReflectionUtil#findProperty(Class, String)}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int compare(Object o1, Object o2)
	{
		if (o1.equals(o2))
			return 0;

		for (String curProperty : properties)
		{
			Property< ? , ? , ? > p1 = ReflectionUtil.findProperty(o1.getClass(), curProperty);
			Property< ? , ? , ? > p2 = ReflectionUtil.findProperty(o2.getClass(), curProperty);
			if (p1 == null || p2 == null)
			{
				throw new IllegalArgumentException(o1.getClass() + " and " + o2.getClass()
					+ " do not both have the property " + curProperty);
			}
			Object v1 = p1.getValueNull(o1);
			Object v2 = p2.getValueNull(o2);
			if (v1 != null && v2 == null)
				return -factor;
			else if (v1 == null && v2 != null)
				return factor;
			else if (v1 instanceof Comparable< ? > && v2 instanceof Comparable< ? >)
			{
				int ret = ((Comparable<Object>) v1).compareTo(v2);
				if (ret != 0)
					return ret * factor;
			}
		}
		return 0;
	}
}
