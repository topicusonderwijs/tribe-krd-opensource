package nl.topicus.cobra.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.topicus.cobra.reflection.copy.CollectionCopyWorker;
import nl.topicus.cobra.reflection.copy.CopyManager;

public class HibernateCollectionCopyWorker<T extends Collection<Object>> extends
		CollectionCopyWorker<T>
{

	public HibernateCollectionCopyWorker(CopyManager manager)
	{
		super(manager);
	}

	@Override
	@SuppressWarnings("unchecked")
	public T createInstance(T object)
	{
		if (object instanceof List)
			return (T) new ArrayList<Object>();
		if (object instanceof Set)
			return (T) new HashSet<Object>();
		return super.createInstance(object);
	}
}
