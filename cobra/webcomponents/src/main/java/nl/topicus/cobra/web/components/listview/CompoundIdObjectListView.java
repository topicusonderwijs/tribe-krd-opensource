/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.listview;

import java.util.List;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.modelsv2.ModelFactory;

import org.apache.wicket.markup.html.list.ListItemModel;
import org.apache.wicket.model.IModel;

/**
 * Subclass van IdObjectListView die compound models op de listitem zet ipv normale
 * models.
 * 
 * @author loite
 */
public abstract class CompoundIdObjectListView<T extends IdObject> extends IdObjectListView<T>
{
	private static final long serialVersionUID = 1L;

	public CompoundIdObjectListView(String id)
	{
		super(id);
	}

	public CompoundIdObjectListView(String id, IModel< ? extends List<T>> model)
	{
		super(id, model);
	}

	@Override
	protected IModel<T> getListItemModel(final IModel< ? extends List<T>> listViewModel,
			final int index)
	{
		// return ModelFactory.getCompoundModel(getListItem(listViewModel, index));
		return ModelFactory.getCompoundModelForModel(new ListItemModel<T>(this, index));
	}
}
