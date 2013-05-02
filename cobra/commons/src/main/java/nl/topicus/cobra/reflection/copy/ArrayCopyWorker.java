package nl.topicus.cobra.reflection.copy;

import java.lang.reflect.Array;

public class ArrayCopyWorker<T extends Object> implements CopyWorker<T>
{
	private CopyManager manager;

	public ArrayCopyWorker(CopyManager manager)
	{
		this.manager = manager;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T createInstance(T object)
	{
		return (T) Array.newInstance(object.getClass().getComponentType(), Array.getLength(object));
	}

	@Override
	public void fillInstance(T source, T destination)
	{
		int length = Array.getLength(source);
		for (int count = 0; count < length; count++)
		{
			Array.set(destination, count, manager.copyObject(Array.get(source, count)));
		}
	}
}
