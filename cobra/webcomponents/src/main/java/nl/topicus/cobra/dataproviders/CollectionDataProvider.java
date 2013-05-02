package nl.topicus.cobra.dataproviders;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.modelsv2.ModelFactory;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * Data provider voor collecties.
 * 
 * @author loite
 */
public class CollectionDataProvider<T> implements IDataProvider<T>
{
	private static final long serialVersionUID = 1L;

	private final IModel< ? extends Collection<T>> collectionModel;

	private final boolean useCollectionItemModel;

	public CollectionDataProvider(Collection<T> collection)
	{
		this.collectionModel = ModelFactory.getListModel(collection);
		useCollectionItemModel = false;
	}

	public CollectionDataProvider(IModel< ? extends Collection<T>> collectionModel)
	{
		this.collectionModel = collectionModel;
		useCollectionItemModel = false;
	}

	public CollectionDataProvider(IModel< ? extends Collection<T>> collectionModel,
			boolean useCollectionItemModel)
	{
		this.collectionModel = collectionModel;
		this.useCollectionItemModel = useCollectionItemModel;
	}

	protected Collection<T> getCollection()
	{
		return collectionModel.getObject();
	}

	public IModel< ? extends Collection<T>> getCollectionModel()
	{
		return collectionModel;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<T> iterator(int first, int count)
	{
		Collection<T> collection = getCollection();
		if (collection == null)
			return ((Collection<T>) Collections.emptyList()).iterator();

		if (collection instanceof List< ? >)
			return ((List<T>) collection).subList(first, Math.min(first + count, size()))
				.iterator();

		return getCollection().iterator();
	}

	@Override
	public IModel<T> model(T object)
	{
		if (useCollectionItemModel)
		{
			int index = getIndex(object);
			return new CompoundPropertyModel<T>(new CollectionItemModel(index));
		}
		if (object instanceof IdObject)
		{
			return ModelFactory.getCompoundModelForObject(object);
		}

		return ModelFactory.getModel(object);
	}

	@Override
	public int size()
	{
		if (getCollection() == null)
			return 0;
		return getCollection().size();
	}

	@Override
	public void detach()
	{
		collectionModel.detach();
	}

	private final int getIndex(Object object)
	{
		int counter = 0;
		if (getCollection() == null)
			return -1;
		Iterator< ? > it = getCollection().iterator();
		while (it.hasNext())
		{
			Object obj = it.next();
			if (obj.equals(object))
			{
				return counter;
			}
			counter++;
		}
		return -1;
	}

	private final class CollectionItemModel implements IModel<T>
	{
		private static final long serialVersionUID = 1L;

		private final int index;

		private CollectionItemModel(int index)
		{
			this.index = index;
		}

		@Override
		public T getObject()
		{
			int counter = 0;
			if (getCollection() == null)
				return null;
			Iterator<T> it = getCollection().iterator();
			while (it.hasNext())
			{
				T object = it.next();
				if (counter == index)
				{
					return object;
				}
				counter++;
			}
			return null;
		}

		@Override
		public void setObject(T object)
		{
			throw new UnsupportedOperationException("Model is read-only");
		}

		@Override
		public void detach()
		{
		}

	}

}
