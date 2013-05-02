package nl.topicus.eduarte.tester.hibernate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.entities.ObjectKey;
import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.entities.Entiteit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockDatabase
{
	private static final Logger log = LoggerFactory.getLogger(MockDatabase.class);

	private final Map<ObjectKey, IdObject> mockDatabase = new LinkedHashMap<ObjectKey, IdObject>();

	private final ArrayList<IdObject> objectsInTransaction = new ArrayList<IdObject>();

	private final ArrayList<DatabaseAction> transactionLog = new ArrayList<DatabaseAction>();

	private Long idSequence = 100000L;

	public <T extends IdObject> T get(Class<T> clz, Serializable id)
	{
		T result = null;
		for (IdObject object : objectsInTransaction)
		{
			if (object.getIdAsSerializable().equals(id))
			{
				result = clz.cast(object);
			}
		}
		if (result == null)
		{
			ObjectKey key = new ObjectKey(clz, id);
			result = clz.cast(mockDatabase.get(key));
		}
		return result;
	}

	public void insert(IdObject object)
	{
		Asserts.assertNull("object.id", object.getIdAsSerializable());
		cascadeSave(object);
		DatabaseAction action = DatabaseAction.insert(object);
		transactionLog.add(action);
		objectsInTransaction.add(object);
	}

	@SuppressWarnings("unchecked")
	private void cascadeSave(IdObject object)
	{
		generateId(object);

		List<Property<IdObject, ? , ? >> properties =
			ReflectionUtil.findProperties((Class<IdObject>) object.getClass());
		for (Property<IdObject, ? , ? > property : properties)
		{
			if (property.isAnnotationPresent(OneToMany.class))
			{
				OneToMany oneToMany = property.getAnnotation(OneToMany.class);
				List<CascadeType> cascades = Arrays.asList(oneToMany.cascade());
				if (cascades.contains(CascadeType.ALL) || cascades.contains(CascadeType.MERGE)
					|| cascades.contains(CascadeType.PERSIST))
				{
					Collection< ? extends IdObject> collection =
						(Collection< ? extends IdObject>) property.getValue(object);
					cascadeSave(collection);
				}
			}
			else if (property.isAnnotationPresent(ManyToMany.class))
			{
				ManyToMany manyToMany = property.getAnnotation(ManyToMany.class);
				List<CascadeType> cascades = Arrays.asList(manyToMany.cascade());
				if (cascades.contains(CascadeType.ALL) || cascades.contains(CascadeType.MERGE)
					|| cascades.contains(CascadeType.PERSIST))
				{
					Collection< ? extends IdObject> collection =
						(Collection< ? extends IdObject>) property.getValue(object);
					cascadeSave(collection);
				}
			}

			else if (property.isAnnotationPresent(ManyToOne.class))
			{
				ManyToOne manyToOne = property.getAnnotation(ManyToOne.class);
				List<CascadeType> cascades = Arrays.asList(manyToOne.cascade());
				if (cascades.contains(CascadeType.ALL) || cascades.contains(CascadeType.MERGE)
					|| cascades.contains(CascadeType.PERSIST))
				{
					cascadeSave((IdObject) property.getValue(object));
				}
			}
		}
	}

	private void cascadeSave(Collection< ? extends IdObject> objects)
	{
		for (IdObject object : objects)
		{
			cascadeSave(object);
		}
	}

	private void generateId(IdObject object)
	{
		try
		{
			Field id = Entiteit.class.getDeclaredField("id");
			if (Long.class.isAssignableFrom(id.getType()))
			{
				id.setAccessible(true);
				id.set(object, idSequence++);
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
		}
	}

	public void update(IdObject object)
	{
		Asserts.assertNotNull("object.id", object.getIdAsSerializable());

		DatabaseAction action = DatabaseAction.update(object);
		transactionLog.add(action);
		if (!objectsInTransaction.contains(object))
			objectsInTransaction.add(object);
	}

	public void delete(IdObject object)
	{
		DatabaseAction action = DatabaseAction.delete(object);
		transactionLog.add(action);
		objectsInTransaction.remove(object);
	}

	public void commit()
	{
		for (DatabaseAction action : transactionLog)
		{
			IdObject object = action.getObject();
			ObjectKey key = new ObjectKey(object);
			switch (action.getAction())
			{
				case SAVE:
					mockDatabase.put(key, object);
					break;
				case UPDATE:
					mockDatabase.put(key, object);
					break;
				case DELETE:
					mockDatabase.remove(key);
					break;
			}
		}
		transactionLog.clear();
		objectsInTransaction.clear();
	}

	public void rollback()
	{
		transactionLog.clear();
		objectsInTransaction.clear();
	}

	public List<IdObject> getObjectsFromTransaction()
	{
		ArrayList<IdObject> result = new ArrayList<IdObject>();
		result.addAll(objectsInTransaction);
		return result;
	}

	public List<DatabaseAction> getTransactionLog()
	{
		return Collections.unmodifiableList(transactionLog);
	}

	public void clearTransactionLog()
	{
		transactionLog.clear();
	}
}
