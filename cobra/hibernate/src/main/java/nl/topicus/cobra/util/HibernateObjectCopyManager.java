package nl.topicus.cobra.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Id;
import javax.persistence.Transient;

import nl.topicus.cobra.entities.FieldPersistance;
import nl.topicus.cobra.entities.FieldPersistenceMode;
import nl.topicus.cobra.reflection.copy.AbstractCopyManager;
import nl.topicus.cobra.reflection.copy.CopyWorker;

import org.hibernate.proxy.HibernateProxy;

public class HibernateObjectCopyManager extends AbstractCopyManager
{

	private Set<Class< ? >> classesToCopy;

	public HibernateObjectCopyManager(Class< ? >... classesToCopy)
	{
		this.classesToCopy = new HashSet<Class< ? >>(Arrays.asList(classesToCopy));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T copyObject(T object)
	{
		T copyObject = object;
		if (object instanceof HibernateProxy)
			copyObject =
				(T) ((HibernateProxy) object).getHibernateLazyInitializer().getImplementation();
		return super.copyObject(copyObject);
	}

	public boolean getMustCopyInstanceOf(Class< ? > clazz)
	{
		if (classesToCopy.contains(clazz))
			return true;

		return Collection.class.isAssignableFrom(clazz);
	}

	@Override
	public boolean getMustCopyField(Field field)
	{
		if (Modifier.isStatic(field.getModifiers()))
			return false;
		if (field.isAnnotationPresent(Id.class))
			return false;
		if (field.isAnnotationPresent(Transient.class))
			return false;
		FieldPersistance fp = field.getAnnotation(FieldPersistance.class);
		if (fp != null && fp.value().equals(FieldPersistenceMode.SKIP))
			return false;

		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> CopyWorker<T> createCopyWorker(T object)
	{
		if (object instanceof Collection)
		{
			return (CopyWorker<T>) new HibernateCollectionCopyWorker<Collection<Object>>(this);
		}
		return super.createCopyWorker(object);
	}
}
