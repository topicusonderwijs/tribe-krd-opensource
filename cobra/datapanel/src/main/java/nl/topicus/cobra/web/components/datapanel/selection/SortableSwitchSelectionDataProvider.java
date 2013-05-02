package nl.topicus.cobra.web.components.datapanel.selection;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.markup.repeater.data.IDataProvider;

public class SortableSwitchSelectionDataProvider<T> extends SwitchSelectionDataProvider<T>
		implements ISortStateLocator
{
	private static final long serialVersionUID = 1L;

	public SortableSwitchSelectionDataProvider(IDataProvider<T> wrappedProvider,
			final ISelectionComponent< ? , T> selectionComponent)
	{
		super(wrappedProvider, selectionComponent);
	}

	@Override
	public ISortState getSortState()
	{
		return ((ISortStateLocator) wrappedProvider).getSortState();
	}

	@Override
	public void setSortState(ISortState state)
	{
		((ISortStateLocator) wrappedProvider).setSortState(state);
	}
}
