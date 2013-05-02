package nl.topicus.cobra.reflection.copy;

import java.lang.reflect.Field;

import nl.topicus.cobra.reflection.ReflectionUtil;

public class BeanCopyWorker<T> implements CopyWorker<T>
{
	private CopyManager manager;

	public BeanCopyWorker(CopyManager manager)
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
		Class< ? > copyClass = source.getClass();
		while (copyClass != null)
		{
			for (Field curField : copyClass.getDeclaredFields())
			{
				if (manager.getMustCopyField(curField))
				{
					curField.setAccessible(true);
					try
					{
						curField.set(destination, manager.copyObject(curField.get(source)));
					}
					catch (IllegalAccessException e)
					{
						throw new RuntimeException(e);
					}
				}
			}
			copyClass = copyClass.getSuperclass();
		}
	}
}
