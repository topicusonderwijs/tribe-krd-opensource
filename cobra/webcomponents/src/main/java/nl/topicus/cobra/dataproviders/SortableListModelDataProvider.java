package nl.topicus.cobra.dataproviders;

import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.comparators.MultiFieldComparator;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.model.IModel;

public class SortableListModelDataProvider<T> extends ListModelDataProvider<T> implements
		ISortStateLocator
{
	private static final long serialVersionUID = 1L;

	private ISortState sortState = new ISortState()
	{
		private static final long serialVersionUID = 1L;

		private String sProperty;

		private int sState;

		@Override
		public int getPropertySortOrder(String property)
		{
			return property.equals(this.sProperty) ? sState : 0;
		}

		@Override
		public void setPropertySortOrder(String property, int state)
		{
			sProperty = property;
			sState = state;
			Collections.sort(getList(), new MultiFieldComparator(state == DESCENDING, property));
		}
	};

	public SortableListModelDataProvider(IModel<List<T>> listModel)
	{
		super(listModel);
	}

	@Override
	public ISortState getSortState()
	{
		return sortState;
	}

	@Override
	public void setSortState(ISortState state)
	{
		throw new UnsupportedOperationException("Cannot set the sortstate");
	}
}
