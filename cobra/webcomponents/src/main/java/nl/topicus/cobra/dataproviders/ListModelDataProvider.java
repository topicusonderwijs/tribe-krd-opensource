/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.dataproviders;

import java.util.Iterator;
import java.util.List;

import nl.topicus.cobra.modelsv2.ModelFactory;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

/**
 * @author papegaaij, vandenbrink
 * @param <T>
 */
public class ListModelDataProvider<T> implements IDataProvider<T>
{

	private static final long serialVersionUID = 1L;

	private IModel< ? extends List<T>> listModel;

	public ListModelDataProvider(IModel< ? extends List<T>> listModel)
	{
		this.listModel = listModel;
	}

	public List<T> getList()
	{
		return listModel.getObject();
	}

	@Override
	public IModel<T> model(T object)
	{
		return ModelFactory.getModel(object);
	}

	/**
	 * @return first item from the contained list
	 */
	public T getFirst()
	{
		return getList().get(0);
	}

	@Override
	public Iterator<T> iterator(final int first, int total)
	{
		int toIndex = first + total;
		if (toIndex > getList().size())
		{
			toIndex = getList().size();
		}
		final int last = toIndex;
		return new Iterator<T>()
		{
			private int count = first;

			@Override
			public boolean hasNext()
			{
				return count < last;
			}

			@Override
			public T next()
			{
				return getList().get(count++);
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public int size()
	{
		return getList().size();
	}

	@Override
	public void detach()
	{
		listModel.detach();
	}

	public IModel< ? extends List<T>> getListModel()
	{
		return listModel;
	}

	public void setListModel(IModel< ? extends List<T>> listModel)
	{
		this.listModel = listModel;
	}
}
