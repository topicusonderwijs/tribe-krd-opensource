package nl.topicus.cobra.modelsv2;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.entities.ObjectKey;
import nl.topicus.cobra.entities.TransientIdObject;

/**
 * ChangeRecordingModel biedt een implementatie van IChangeRecordingModel. Bij het
 * aanmaken van het model wordt de complete object graaf doorlopen, waarbij een set wordt
 * opgebouwd, bestaande uit de class en id van alle objecten in de graaf. Bij het opslaan
 * wordt een nieuwe set gebouwd, die vergeleken wordt met de eerder opgeslagen set en de
 * wijzigingen worden in de database opgeslagen.
 * <p>
 * Voor dit model is het van belang dat de classes die aan de model manager meegegeven
 * worden in delete-order staan. Dit wil zeggen dat instances van deze classes verwijderd
 * moeten kunnen worden in de aangegeven volgorde. Stel, een auto heeft 4 wielen. De
 * wielen hebben een referentie naar de auto waar ze bij horen. Als nu eerst de auto
 * verwijderd zou worden, zou dit een integrity constraint violation opleveren. De
 * volgorde van classes moet dus zijn: Wiel, Auto.
 * 
 * @author papegaaij
 * @param <T>
 */
public class ChangeRecordingModel<T extends TransientIdObject> extends ExtendedHibernateModel<T>
		implements IChangeRecordingModel<T>
{
	private static final long serialVersionUID = 1L;

	/**
	 * De DeleteKey is een key die een object uniek identificeert ahv de class en id. De
	 * natural ordering van DeleteKey komt neer op de delete-order van de objecten.
	 * 
	 * @author papegaaij
	 */
	@SuppressWarnings("unchecked")
	public class DeleteKey implements Comparable<DeleteKey>, Serializable
	{
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("hiding")
		private Long id;

		private Class< ? extends IdObject> clazz;

		@SuppressWarnings("unused")
		private DeleteKey()
		{
		}

		/**
		 * Maakt een nieuwe DeleteKey voor het gegeven object.
		 * 
		 * @param obj
		 */
		public DeleteKey(TransientIdObject obj)
		{
			clazz = detectClass(obj);

			Serializable tId;
			if (obj.isSaved())
			{
				tId = obj.getIdAsSerializable();
			}
			else
			{
				tId = obj.getTemporaryId();
				if (tId == null)
				{
					// the manager is responsible for filling the id, make it do so
					manager.getModel(obj, null);
					tId = obj.getTemporaryId();
				}
			}

			if (tId instanceof Number)
			{
				this.id = ((Number) tId).longValue();
			}
		}

		/**
		 * Geeft het id van de key.
		 * 
		 * @return het id.
		 */
		public Long getId()
		{
			return id;
		}

		/**
		 * Geeft de class van de key.
		 * 
		 * @return de class.
		 */
		public Class< ? extends IdObject> getClazz()
		{
			return clazz;
		}

		@Override
		public int compareTo(DeleteKey o)
		{
			int thisIndex = manager.getManagedClasses().indexOf(clazz);
			int otherIndex = manager.getManagedClasses().indexOf(o.getClazz());
			if (thisIndex == -1)
				throw new IllegalArgumentException(clazz + " not added to ModelManager");
			if (otherIndex == -1)
				throw new IllegalArgumentException(o.getClazz() + " not added to ModelManager");
			if (thisIndex != otherIndex)
			{
				return thisIndex - otherIndex;
			}
			return id.compareTo(o.getId());
		}

		@Override
		public String toString()
		{
			return clazz.getSimpleName() + ":" + id;
		}
	}

	/** bevat alle objecten zoals die aanwezig waren toen dit model gemaakt werd. */
	private SortedSet<DeleteKey> objectGraph;

	private Set<ObjectKey> objectsNotToDelete = new HashSet<ObjectKey>();

	/**
	 * Maakt een nieuw ChangeRecordingModel voor het gegeven model, onder hoede van de
	 * gegeven manager. Deze is package private, want hij mag alleen aangeroepen worden
	 * door de modelmanager zelf
	 */
	ChangeRecordingModel(T object, ModelManager manager)
	{
		super(object, manager);
		if (object.isSaved())
			objectGraph = new TreeSet<DeleteKey>(collectObjectGraph(object).keySet());
	}

	private SortedMap<DeleteKey, TransientIdObject> collectObjectGraph(T object)
	{
		SortedMap<DeleteKey, TransientIdObject> ret = new TreeMap<DeleteKey, TransientIdObject>();
		LinkedList<TransientIdObject> objectsToCheck = new LinkedList<TransientIdObject>();
		objectsToCheck.add(object);
		while (!objectsToCheck.isEmpty())
		{
			TransientIdObject curObject =
				(TransientIdObject) getObjectAccess().getActualObject(objectsToCheck.removeFirst());
			DeleteKey key = new DeleteKey(curObject);
			if (!ret.containsKey(key))
			{
				ret.put(key, curObject);
				if (curObject != null)
				{
					for (Field curField : getFieldsToPersist(detectClass(curObject)))
					{
						for (TransientIdObject curFieldValue : getValue(curObject, curField))
						{
							if (manager.isManaged(curFieldValue, curField))
								objectsToCheck.add(curFieldValue);
						}
					}
				}
			}
		}
		return ret;
	}

	@Override
	public void doNotDelete(IdObject object)
	{
		objectsNotToDelete.add(new ObjectKey(object));
	}

	@Override
	public void recalculate()
	{
		T object = getObject();
		if (object.isSaved())
			objectGraph = new TreeSet<DeleteKey>(collectObjectGraph(object).keySet());
		else
		{
			objectGraph = null;
		}
	}

	@Override
	public void deleteObject(IModificationCallback... callback)
	{
		detach();
		if (objectGraph == null)
			return;
		delete(objectGraph, callback);
	}

	@Override
	public void saveObject(IModificationCallback... callback)
	{
		SortedMap<DeleteKey, TransientIdObject> newGraph = collectObjectGraph(getObject());
		if (objectGraph != null)
		{
			objectGraph.removeAll(newGraph.keySet());
			delete(objectGraph, callback);
		}
		LinkedList<SortedMap.Entry<DeleteKey, TransientIdObject>> entities =
			new LinkedList<SortedMap.Entry<DeleteKey, TransientIdObject>>(newGraph.entrySet());
		ListIterator<SortedMap.Entry<DeleteKey, TransientIdObject>> it =
			entities.listIterator(entities.size());
		while (it.hasPrevious())
		{
			SortedMap.Entry<DeleteKey, TransientIdObject> curEntry = it.previous();
			for (IModificationCallback curCallBack : callback)
				curCallBack.saveOrUpdate(curEntry.getValue(), curEntry.getKey().getClazz());
			getObjectAccess().save(Arrays.asList(curEntry.getValue()));
		}
	}

	private void delete(SortedSet<DeleteKey> objectsToDelete, IModificationCallback... callback)
	{
		for (DeleteKey curKey : objectsToDelete)
		{
			IdObject object =
				getObjectAccess().load(curKey.getClazz(),
					Arrays.asList((Serializable) curKey.getId())).get(0);
			if (isSafeToDelete(object, objectsToDelete))
			{
				for (IModificationCallback curCallBack : callback)
					curCallBack.delete(object, curKey.getClazz());
				getObjectAccess().delete(Arrays.asList(object));
			}
		}
	}

	private boolean isSafeToDelete(IdObject object, SortedSet<DeleteKey> objectsToDelete)
	{
		if (objectsNotToDelete.contains(new ObjectKey(object)))
			return false;

		for (Field curField : getFields(object))
		{
			if (curField.isAnnotationPresent(CheckIncomingReferences.class))
			{
				for (TransientIdObject curFieldValue : getValue(object, curField))
				{
					if (!manager.getManagedClasses().contains(detectClass(curFieldValue)))
						return false;
					if (!objectsToDelete.contains(new DeleteKey(curFieldValue)))
						return false;
				}
			}
		}
		return true;
	}

	private List<Field> getFields(IdObject object)
	{
		List<Field> ret = new ArrayList<Field>();
		Class< ? > clazz = detectClass(object);
		while (clazz != null)
		{
			ret.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}
		return ret;
	}

	private List<TransientIdObject> getValue(Object object, Field field)
	{
		List<TransientIdObject> ret = new ArrayList<TransientIdObject>();
		Object curFieldValue = getPropertyValue(getKey(field), object);
		if (curFieldValue instanceof Collection< ? >)
		{
			for (Object curValue : (Collection< ? >) curFieldValue)
				if (curValue instanceof TransientIdObject)
					ret.add((TransientIdObject) curValue);
		}
		else if (curFieldValue instanceof TransientIdObject)
			ret.add((TransientIdObject) curFieldValue);
		return ret;
	}
}
