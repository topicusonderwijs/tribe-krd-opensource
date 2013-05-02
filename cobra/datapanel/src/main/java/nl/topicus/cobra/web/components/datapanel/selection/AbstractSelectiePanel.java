package nl.topicus.cobra.web.components.datapanel.selection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.topicus.cobra.commons.interfaces.ZoekFilter;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.CustomColumn.Positioning;
import nl.topicus.cobra.web.pages.FeedbackComponent;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

public abstract class AbstractSelectiePanel<R, S, Z extends ZoekFilter<S>> extends Panel implements
		ISelectionComponent<R, S>
{
	private static final long serialVersionUID = 1L;

	private Selection<R, S> selection;

	private IDataProvider<S> innerProvider;

	private CustomDataPanel<S> datapanel;

	private Z filter;

	private CustomDataPanelContentDescription<S> contents;

	private List<ISelectieUpdateListener> updateListeners =
		new ArrayList<ISelectieUpdateListener>();

	private int maxResults = Integer.MAX_VALUE;

	public AbstractSelectiePanel(String id, Z filter, Selection<R, S> selection)
	{
		super(id);
		this.selection = selection;
		this.filter = filter;
		this.contents = createContentDescription();
		CheckboxSelectionColumn<R, S> selectionColumn = createSelectionColumn();
		selectionColumn.addUpdateListener(new IUpdateListener<S>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onUpdate(IModel<Boolean> checkboxModel, IModel<S> rowModel,
					AjaxRequestTarget target)
			{
				onSelectionChanged(target);
			}
		});
		contents.addColumn(selectionColumn.setPositioning(Positioning.FIXED_LEFT));
		addSelectieUpdateListener(new ISelectieUpdateListener()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSelectieUpdate(AjaxRequestTarget target)
			{
				datapanel.updateTitle(target);
			}
		});
	}

	@Override
	protected void onBeforeRender()
	{
		if (!hasBeenRendered())
		{
			innerProvider = createDataProvider(filter);
			datapanel =
				createDataPanel("datapanel", createSwitchableProvider(innerProvider), contents);
			datapanel.setItemsPerPage(getItemsPerPage());
			datapanel.setTitleModel(new SelectieTitleModel(contents.getTitle(), datapanel,
				getSelection(), maxResults));
			add(datapanel);
			add(createZoekFilterPanel("filter", filter, datapanel));
		}
		super.onBeforeRender();
	}

	public abstract int getItemsPerPage();

	private SwitchSelectionDataProvider<S> createSwitchableProvider(IDataProvider<S> provider)
	{
		if (provider instanceof ISortStateLocator)
			return new SortableSwitchSelectionDataProvider<S>(provider, this);
		return new SwitchSelectionDataProvider<S>(provider, this);
	}

	protected CheckboxSelectionColumn<R, S> createSelectionColumn()
	{
		return new CheckboxSelectionColumn<R, S>("selection", "", selection);
	}

	@SuppressWarnings("hiding")
	protected abstract CustomDataPanel<S> createDataPanel(String id, IDataProvider<S> provider,
			CustomDataPanelContentDescription<S> contents);

	@SuppressWarnings("hiding")
	protected abstract IDataProvider<S> createDataProvider(Z filter);

	protected abstract CustomDataPanelContentDescription<S> createContentDescription();

	@SuppressWarnings("hiding")
	protected abstract Panel createZoekFilterPanel(String id, Z filter,
			CustomDataPanel<S> customDataPanel);

	public CustomDataPanel<S> getDataPanel()
	{
		return datapanel;
	}

	public CustomDataPanelContentDescription<S> getContentDescription()
	{
		return contents;
	}

	@Override
	public Z getFilter()
	{
		return filter;
	}

	public void setFilter(Z filter)
	{
		this.filter = filter;
	}

	@Override
	public Selection<R, S> getSelection()
	{
		return selection;
	}

	@Override
	public List<S> getSelectedSearchElements()
	{
		Iterator< ? extends S> it = innerProvider.iterator(0, Integer.MAX_VALUE);
		List<S> ret = new ArrayList<S>();
		while (it.hasNext())
		{
			S curItem = it.next();
			if (getSelection().isSelected(curItem))
				ret.add(curItem);
		}
		return ret;
	}

	public int getMaxResults()
	{
		return maxResults;
	}

	public void setMaxResults(int maxResults)
	{
		this.maxResults = maxResults;
	}

	@Override
	public void onSelectionChanged(AjaxRequestTarget target)
	{
		for (ISelectieUpdateListener curListener : updateListeners)
			curListener.onSelectieUpdate(target);
		if (getSelection().size() > getMaxResults())
		{
			error("Er kunnen maximaal " + getMaxResults() + " " + getEntityName()
				+ " geselecteerd worden.");
			FeedbackComponent feedback = findParent(FeedbackComponent.class);
			if (feedback != null)
				feedback.refreshFeedback(target);
		}
	}

	protected abstract String getEntityName();

	public void addSelectieUpdateListener(ISelectieUpdateListener listener)
	{
		updateListeners.add(listener);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
		ComponentUtil.detachQuietly(selection);
		ComponentUtil.detachQuietly(contents);
	}
}
