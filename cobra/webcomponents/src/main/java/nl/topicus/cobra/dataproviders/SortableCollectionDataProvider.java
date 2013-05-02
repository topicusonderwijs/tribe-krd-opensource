package nl.topicus.cobra.dataproviders;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import nl.topicus.cobra.comparators.MultiFieldComparator;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState;
import org.apache.wicket.model.IModel;

/**
 * Collection dataprovider die ook sorteren ondersteunt.
 * 
 * @author loite
 */
public class SortableCollectionDataProvider<T> extends CollectionDataProvider<T> implements
		ISortStateLocator
{
	private static final long serialVersionUID = 1L;

	private ISortState sortState = new SingleSortState();

	public SortableCollectionDataProvider(Collection<T> collection)
	{
		super(collection);
	}

	public SortableCollectionDataProvider(IModel< ? extends Collection<T>> collectionModel)
	{
		super(collectionModel);
	}

	public SortableCollectionDataProvider(IModel< ? extends Collection<T>> collectionModel,
			boolean useCollectionItemModel)
	{
		super(collectionModel, useCollectionItemModel);
	}

	public SortableCollectionDataProvider(IModel< ? extends Collection<T>> collectionModel,
			String sortProperty)
	{
		super(collectionModel);
		sortState.setPropertySortOrder(sortProperty, ISortState.ASCENDING);
	}

	@Override
	public ISortState getSortState()
	{
		return sortState;
	}

	@Override
	public void setSortState(ISortState state)
	{
		sortState = state;
	}

	public String getSortProperty()
	{
		if (sortState != null && sortState instanceof SingleSortState)
		{
			SingleSortState state = (SingleSortState) sortState;
			return state.getSort() == null ? null : state.getSort().getProperty();
		}
		return null;
	}

	public boolean isAscending()
	{
		if (sortState != null && sortState instanceof SingleSortState)
		{
			SingleSortState state = (SingleSortState) sortState;
			return state.getSort() == null ? false : state.getSort().isAscending();
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<T> iterator(int first, int count)
	{
		Collection<T> collection = getCollection();
		if (collection == null)
			return ((Collection<T>) Collections.emptyList()).iterator();

		if (collection instanceof List< ? >)
		{
			if (getSortProperty() != null)
			{
				Collections.sort((List<T>) collection, new MultiFieldComparator(!isAscending(),
					getSortProperty()));
			}
			return ((List<T>) collection).subList(first, Math.min(first + count, size()))
				.iterator();
		}

		return getCollection().iterator();
	}

}
