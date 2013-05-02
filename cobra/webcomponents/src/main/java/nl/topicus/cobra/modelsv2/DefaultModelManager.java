/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.modelsv2;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.entities.FieldPersistance;
import nl.topicus.cobra.entities.FieldPersistenceMode;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.entities.TransientIdObject;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.Counter;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Default implementatie van een ModelManager. Kan overweg met hibernate entiteiten,
 * serializable objecten en collecties van hibernate entiteiten.
 * 
 * @author marrink
 */
public class DefaultModelManager implements ModelManager
{
	private static final long serialVersionUID = 1L;

	private Map<Key, IModel< ? >> keyModelCache;

	private Map<Object, IModel< ? >> objectModelCache;

	private Counter idGenerator;

	private boolean attached = false;

	private FieldPersistanceFilter filter;

	private List< ? extends Class< ? extends TransientIdObject>> managedEntities;

	@SpringBean
	private IObjectAccess objectAccess;

	public DefaultModelManager(Class< ? >... managedEntities)
	{
		this(DefaultFieldPersistanceFilter.INSTANCE, arrayToList(managedEntities));
	}

	public DefaultModelManager(List< ? extends Class< ? extends TransientIdObject>> managedEntities)
	{
		this(DefaultFieldPersistanceFilter.INSTANCE, managedEntities);
	}

	/**
	 * Maakt een nieuwe model manager voor 1 class.
	 * 
	 * @param managedEntity
	 */
	public DefaultModelManager(Class< ? extends TransientIdObject> managedEntity)
	{
		this(DefaultFieldPersistanceFilter.INSTANCE, arrayToList(managedEntity));
	}

	public DefaultModelManager(FieldPersistanceFilter filter, Class< ? >... managedEntities)
	{
		this(filter, arrayToList(managedEntities));
	}

	private static List< ? extends Class< ? extends TransientIdObject>> arrayToList(
			Class< ? >... managedEntities)
	{
		List<Class< ? extends TransientIdObject>> ret =
			new ArrayList<Class< ? extends TransientIdObject>>();
		for (Class< ? > curClass : managedEntities)
			ret.add(curClass.asSubclass(TransientIdObject.class));
		return ret;
	}

	/**
	 * Maakt een nieuwe model manager voor een serie classes. De gegeven classes zijn de
	 * managed classes, waarin wijzigingen gemaakt kunnen worden. De classes dienen in
	 * delete-order gegeven te worden, dwz. dat de instances van de classes verwijderd
	 * worden in de gegeven volgorde.
	 * 
	 * @param filter
	 * @param managedEntities
	 */
	public DefaultModelManager(FieldPersistanceFilter filter,
			List< ? extends Class< ? extends TransientIdObject>> managedEntities)
	{
		this.filter = filter;
		this.managedEntities = managedEntities;
		keyModelCache = new HashMap<Key, IModel< ? >>();
		objectModelCache = new HashMap<Object, IModel< ? >>();
		idGenerator = new Counter();
		InjectorHolder.getInjector().inject(this);
	}

	protected IObjectAccess getObjectAccess()
	{
		if (objectAccess == null)
			InjectorHolder.getInjector().inject(this);

		return objectAccess;
	}

	@SuppressWarnings("unchecked")
	protected Object getKey(Object object)
	{
		if (object instanceof TransientIdObject)
		{
			TransientIdObject obj = (TransientIdObject) object;
			if (!obj.isSaved())
			{
				if (obj.getTemporaryId() == null)
					obj.setTemporaryId(idGenerator.next());
				return new Key(obj.getClass(), obj.getTemporaryId());
			}
		}
		if (object instanceof IdObject)
		{
			Class< ? > objectClass = getObjectAccess().getClass((IdObject) object);
			return new Key(objectClass, ((IdObject) object).getIdAsSerializable());
		}
		if (object instanceof Collection< ? >)
		{
			return new CollectionKey((Collection< ? extends IdObject>) object);
		}
		return object;
	}

	@SuppressWarnings("unchecked")
	public <T> IModel<T> getModel(T object, Field field)
	{
		attached = true;
		if (object == null)
			return createHibernateObject(object);
		if (object instanceof TransientIdObject)
		{
			updateId((TransientIdObject) object);
		}
		Object key = getKey(object);
		IModel<T> temp =
			(IModel<T>) (key instanceof Key ? keyModelCache.get(key) : objectModelCache.get(key));
		if (temp == null)
		{
			if (object instanceof TransientIdObject && isManaged(object, field))
			{
				temp = new ExtendedHibernateModel<T>(object, this);
			}
			else if (object instanceof IdObject)
			{
				temp = createHibernateObject(object);
			}
			else if (object instanceof Collection< ? >)
			{
				// bij een collectie van een ander type als IdObject wordt vanzelf een
				// classcast gegooid, zal met huidige entiteiten niet zo snel voorkomen.
				temp =
					(IModel<T>) new ExtendedHibernateListModel<Collection<IdObject>, IdObject>(
						(Collection<IdObject>) object, this, field);
			}
			else if (object instanceof Serializable)
			{
				temp = (IModel<T>) new Model<Serializable>((Serializable) object);
			}
			else
				throw new IllegalArgumentException("Can not manage object: " + object);
			if (key instanceof Key)
				keyModelCache.put((Key) key, temp);
			else
				objectModelCache.put(key, temp);
		}
		return temp;
	}

	private <T> IModel<T> createHibernateObject(T object)
	{
		return new HibernateModel<T>(object)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSetObject(Object oldObjectOrClass, Serializable oldId,
					IdObject newObject)
			{
				updateModelObject(this, oldObjectOrClass, oldId, newObject);
			}
		};
	}

	public <T extends TransientIdObject> IChangeRecordingModel<T> getChangeRecordingModel(T object)
	{
		attached = true;
		if (object == null)
			throw new IllegalArgumentException("object cannot be null");
		Object key = getKey(object);
		if (!isManaged(object, null))
			throw new IllegalArgumentException("objects of class " + object.getClass()
				+ " are not managed by this ModelManager");

		ChangeRecordingModel<T> ret = new ChangeRecordingModel<T>(object, this);
		IModel< ? > oldModel = keyModelCache.put((Key) key, ret);
		if (oldModel != null)
			oldModel.detach();
		return ret;
	}

	@Override
	public void updateModelObject(IModel< ? > model, Object oldObjectOrClass, Serializable oldId,
			IdObject newObject)
	{
		Object oldKey;
		if (oldObjectOrClass instanceof Class< ? > && oldId != null)
		{
			oldKey = new Key((Class< ? >) oldObjectOrClass, oldId);
		}
		else
		{
			oldKey = getKey(oldObjectOrClass);
		}
		if (oldKey instanceof Key)
		{
			keyModelCache.remove(oldKey);
			Object newKey = getKey(newObject);
			if (newKey instanceof Key)
			{
				keyModelCache.put((Key) newKey, model);
			}
			else
			{
				objectModelCache.put(newKey, model);
			}
		}
	}

	@Override
	public boolean isManaged(Object object, Field field)
	{
		if (object == null || !(object instanceof TransientIdObject))
			return false;
		if (field != null
			&& field.isAnnotationPresent(FieldPersistance.class)
			&& !field.getAnnotation(FieldPersistance.class).value().equals(
				FieldPersistenceMode.SAVE_AND_FOLLOW))
			return false;
		TransientIdObject tIdObject = (TransientIdObject) object;
		Class< ? > objClass = getObjectAccess().getClass(tIdObject);
		for (Class< ? > curClass : managedEntities)
		{
			if (curClass.isAssignableFrom(objClass))
			{
				return true;
			}
		}
		return false;
	}

	public List< ? extends Class< ? extends TransientIdObject>> getManagedClasses()
	{
		return managedEntities;
	}

	@Override
	public void detach()
	{
		if (attached)
		{
			attached = false;
			objectModelCache.clear();
		}
	}

	private static final class Key implements Serializable
	{
		private static final long serialVersionUID = 1L;

		private final Class< ? > entityClass;

		private final Serializable id;

		/**
		 * @param entityClass
		 * @param id
		 */
		public Key(Class< ? > entityClass, Serializable id)
		{
			super();
			this.entityClass = entityClass;
			this.id = id;
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((entityClass == null) ? 0 : entityClass.hashCode());
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final Key other = (Key) obj;
			if (entityClass == null)
			{
				if (other.entityClass != null)
					return false;
			}
			else if (!entityClass.equals(other.entityClass))
				return false;
			if (id == null)
			{
				if (other.id != null)
					return false;
			}
			else if (!id.equals(other.id))
				return false;
			return true;
		}
	}

	private static final class CollectionKey implements Serializable
	{
		private static final long serialVersionUID = 1L;

		private Collection< ? extends IdObject> collection;

		/**
		 * @param collection
		 */
		public CollectionKey(Collection< ? extends IdObject> collection)
		{
			super();
			Asserts.assertNotNull("collection", collection);
			this.collection = collection;
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((collection == null) ? 0 : collection.hashCode());
			return result;
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final CollectionKey other = (CollectionKey) obj;
			if (collection == null)
			{
				if (other.collection != null)
					return false;
			}
			else if (collection != other.collection)
				return false;
			return true;
		}

	}

	private void updateId(TransientIdObject objectIfIdIsNull)
	{
		if (!objectIfIdIsNull.isSaved())
		{
			return;
		}
		Key oldKey = new Key(objectIfIdIsNull.getClass(), objectIfIdIsNull.getTemporaryId());
		if (keyModelCache.containsKey(oldKey))
		{
			objectIfIdIsNull.setTemporaryId(null);
			IModel< ? > model = keyModelCache.get(oldKey);
			Object newKey = getKey(objectIfIdIsNull);
			keyModelCache.put((Key) newKey, model);
			keyModelCache.remove(oldKey);
		}
	}

	@Override
	public FieldPersistanceFilter getFilter()
	{
		return filter;
	}
}
