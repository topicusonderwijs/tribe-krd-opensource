package nl.topicus.cobra.reflection.copy;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCopyManager implements CopyManager
{
	private Map<ObjectIdentityKey, Object> copiedObjects = new HashMap<ObjectIdentityKey, Object>();

	public AbstractCopyManager()
	{
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T copyObject(T object)
	{
		if (object == null)
			return null;

		if (!getMustCopyInstanceOf(object.getClass()))
		{
			return object;
		}

		ObjectIdentityKey key = new ObjectIdentityKey(object);
		if (copiedObjects.containsKey(key))
		{
			return (T) copiedObjects.get(key);
		}

		CopyWorker<T> copyWorker = createCopyWorker(object);
		T copy = copyWorker.createInstance(object);
		copiedObjects.put(key, copy);
		copyWorker.fillInstance(object, copy);
		return copy;
	}

	@SuppressWarnings("unchecked")
	protected <T> CopyWorker<T> createCopyWorker(T object)
	{
		if (object.getClass().isArray())
			return new ArrayCopyWorker<T>(this);
		if (object instanceof Collection)
			return (CopyWorker<T>) new CollectionCopyWorker<Collection<Object>>(this);
		return new BeanCopyWorker<T>(this);
	}
}
