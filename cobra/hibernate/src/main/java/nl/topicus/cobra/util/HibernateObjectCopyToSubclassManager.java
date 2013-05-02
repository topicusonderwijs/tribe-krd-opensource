package nl.topicus.cobra.util;

import nl.topicus.cobra.reflection.copy.CopyWorker;

public class HibernateObjectCopyToSubclassManager extends HibernateObjectCopyManager
{
	private Class< ? > classToInstantiate;

	public HibernateObjectCopyToSubclassManager(Class< ? > classToInstantiate,
			Class< ? >... classesToCopy)
	{
		super(classesToCopy);
		this.classToInstantiate = classToInstantiate;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> CopyWorker<T> createCopyWorker(T object)
	{
		if (object.getClass().isAssignableFrom(classToInstantiate))
		{
			return new HibernateObjectSubclassBeanCopyWorker<T>(this, (Class<T>) classToInstantiate);
		}
		return super.createCopyWorker(object);
	}

}
