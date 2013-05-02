package nl.topicus.cobra.web.components.datapanel.selection;

import java.util.Iterator;
import java.util.List;

import nl.topicus.cobra.dataproviders.ListModelDataProvider;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class SwitchSelectionDataProvider<T> implements IDataProvider<T>
{
	private static final long serialVersionUID = 1L;

	protected IDataProvider<T> wrappedProvider;

	protected IDataProvider<T> selectionProvider;

	protected boolean useSelection = false;

	public SwitchSelectionDataProvider(IDataProvider<T> wrappedProvider,
			final ISelectionComponent< ? , T> selectionComponent)
	{
		this.wrappedProvider = wrappedProvider;
		selectionProvider = new ListModelDataProvider<T>(new LoadableDetachableModel<List<T>>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected List<T> load()
			{
				return selectionComponent.getSelectedSearchElements();
			}
		});
	}

	public boolean isUseSelection()
	{
		return useSelection;
	}

	public void setUseSelection(boolean useSelection)
	{
		this.useSelection = useSelection;
	}

	@Override
	public Iterator< ? extends T> iterator(int first, int count)
	{
		if (useSelection)
			return selectionProvider.iterator(first, count);
		return wrappedProvider.iterator(first, count);
	}

	@Override
	public IModel<T> model(T object)
	{
		return wrappedProvider.model(object);
	}

	@Override
	public int size()
	{
		if (useSelection)
			return selectionProvider.size();
		return wrappedProvider.size();
	}

	@Override
	public void detach()
	{
		wrappedProvider.detach();
		selectionProvider.detach();
	}
}
