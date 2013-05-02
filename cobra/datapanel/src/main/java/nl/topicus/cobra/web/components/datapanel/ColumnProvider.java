/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel;

import java.util.Iterator;
import java.util.List;

import nl.topicus.cobra.models.EqualsPreservingModel;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

public final class ColumnProvider<T> implements IDataProvider<CustomColumn<T>>
{
	private static final long serialVersionUID = 1L;

	private IModel<List<CustomColumn<T>>> model;

	/**
	 * @param model
	 */
	public ColumnProvider(IModel<List<CustomColumn<T>>> model)
	{
		super();
		this.model = model;
	}

	private List<CustomColumn<T>> getList()
	{
		return model.getObject();
	}

	/**
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(int, int)
	 */
	@Override
	public Iterator<CustomColumn<T>> iterator(int first, int count)
	{
		if (first + count <= getList().size())
			return getList().subList(first, first + count).iterator();
		return getList().iterator();
	}

	/**
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
	 */
	@Override
	public IModel<CustomColumn<T>> model(CustomColumn<T> object)
	{
		return new EqualsPreservingModel<CustomColumn<T>>(object);
	}

	/**
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
	 */
	@Override
	public int size()
	{
		return getList().size();
	}

	/**
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	@Override
	public void detach()
	{
		model.detach();
	}

}