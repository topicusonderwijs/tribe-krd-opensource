package nl.topicus.cobra.util;

import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.reflection.copy.BeanCopyWorker;
import nl.topicus.cobra.reflection.copy.CopyManager;

public class HibernateObjectSubclassBeanCopyWorker<T> extends BeanCopyWorker<T>
{
	private Class<T> classToInstantiate;

	public HibernateObjectSubclassBeanCopyWorker(CopyManager manager, Class<T> classToInstantiate)
	{
		super(manager);
		this.classToInstantiate = classToInstantiate;
	}

	@Override
	public T createInstance(T object)
	{
		if (object.getClass().isAssignableFrom(classToInstantiate))
			return ReflectionUtil.invokeConstructor(classToInstantiate);

		return super.createInstance(object);
	}

}
