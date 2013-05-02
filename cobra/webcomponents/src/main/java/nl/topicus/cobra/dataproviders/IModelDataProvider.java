/*
 * Copyright (c) 2005-2007, Topicus b.v.
 * All rights reserved
 */
package nl.topicus.cobra.dataproviders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.Asserts;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

/**
 * Een {@link IDataProvider dataprovider} die zijn lijst ophaalt uit een IModel.
 * 
 * @author Martijn Dashorst
 * @param <T>
 */
public class IModelDataProvider<T> implements IDataProvider<T>, IDetachable
{
	/** Voor serializatie. */
	private static final long serialVersionUID = 1L;

	private IModel< ? extends Collection<T>> listModel;

	/**
	 * Constructor.
	 * 
	 * @param listModel
	 *            een IModel die een Collection moet teruggeven als model object.
	 */
	public IModelDataProvider(IModel< ? extends Collection<T>> listModel)
	{
		Asserts.assertNotNull("listModel", listModel);
		this.listModel = listModel;
	}

	/**
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(int, int)
	 */
	@SuppressWarnings("unchecked")
	public Iterator<T> iterator(int first, int count)
	{
		Collection<T> collection = getCollection();
		if (collection == null)
		{
			return (Iterator<T>) Collections.emptyList().iterator();
		}
		if (first > 0 || count > 0)
		{
			Collection<T> c = new ArrayList<T>();
			int counter = 0;
			Iterator<T> iterator = collection.iterator();
			while (iterator.hasNext())
			{
				T o = iterator.next();
				if (counter >= first && counter < first + count)
				{
					c.add(o);

					if (counter >= first + count)
						break;
				}

				counter++;

			}
			return c.iterator();

		}
		return collection.iterator();
	}

	public int size()
	{
		Collection<T> collection = getCollection();
		if (collection == null)
			return 0;
		return collection.size();
	}

	public IModel<T> model(T object)
	{
		return ModelFactory.getCompoundModelForObject(object);
	}

	@Override
	public void detach()
	{
		listModel.detach();
	}

	private Collection<T> getCollection()
	{
		return listModel.getObject();
	}
}
