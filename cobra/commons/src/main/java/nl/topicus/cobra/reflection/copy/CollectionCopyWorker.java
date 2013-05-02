package nl.topicus.cobra.reflection.copy;

import java.util.Collection;

import nl.topicus.cobra.reflection.ReflectionUtil;

public class CollectionCopyWorker<T extends Collection<Object>> implements CopyWorker<T>
{
	private CopyManager manager;

	public CollectionCopyWorker(CopyManager manager)
	{
		this.manager = manager;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T createInstance(T object)
	{
		return (T) ReflectionUtil.invokeConstructor(object.getClass());
	}

	@Override
	public void fillInstance(T source, T destination)
	{
		for (Object curItem : source)
		{
			destination.add(manager.copyObject(curItem));
		}
	}

}
