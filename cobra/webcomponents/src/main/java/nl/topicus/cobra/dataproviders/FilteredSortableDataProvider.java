package nl.topicus.cobra.dataproviders;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;

/**
 * Basis klasse voor sortable, filterable dataproviders voor gebruik in datatables.
 * 
 * @author Martijn Dashorst
 * @param <T>
 */
public abstract class FilteredSortableDataProvider<T> extends SortableDataProvider<T> implements
		IFilterStateLocator
{
	private static final long serialVersionUID = 1L;
}
