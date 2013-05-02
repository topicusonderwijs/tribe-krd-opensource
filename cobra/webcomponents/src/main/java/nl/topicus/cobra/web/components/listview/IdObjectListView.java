/*
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.listview;

import java.util.List;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.modelsv2.ExtendedModel;
import nl.topicus.cobra.modelsv2.HibernateObjectListModel;
import nl.topicus.cobra.modelsv2.ModelFactory;

import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;

/**
 * Listview voor {@link IdObject}s.
 * 
 * @author jcompagner
 */
public abstract class IdObjectListView<T extends IdObject> extends ListView<T>
{
	private static final long serialVersionUID = 1L;

	public IdObjectListView(String id)
	{
		super(id);
	}

	public IdObjectListView(String id, IModel< ? extends List<T>> model)
	{
		super(id, model);
	}

	@SuppressWarnings("unchecked")
	protected T getListItem(final IModel< ? extends List<T>> listViewModel, final int index)
	{
		if (listViewModel instanceof HibernateObjectListModel< ? , ? >)
			return ((HibernateObjectListModel< ? extends List<T>, T>) listViewModel).get(index)
				.getObject();

		T object = ((List<T>) listViewModel.getObject()).get(index);
		if (listViewModel instanceof IWrapModel< ? >)
		{
			IModel< ? > wrapped = ((IWrapModel< ? >) listViewModel).getWrappedModel();
			if (wrapped instanceof ExtendedModel< ? >)
				return ((ExtendedModel< ? >) wrapped).getManager().getModel(object, null)
					.getObject();
		}

		return object;
	}

	@Override
	protected IModel<T> getListItemModel(final IModel< ? extends List<T>> listViewModel,
			final int index)
	{
		return ModelFactory.getModel(getListItem(listViewModel, index));

	}
}
