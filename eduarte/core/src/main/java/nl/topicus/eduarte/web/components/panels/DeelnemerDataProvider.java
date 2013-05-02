package nl.topicus.eduarte.web.components.panels;

import java.util.Iterator;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.CachingDataProvider;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.dataproviders.IndexedDataProvider;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper.DeelnemerVerbintenisCount;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.model.IModel;

public class DeelnemerDataProvider implements CachingDataProvider<Verbintenis>,
		IndexedDataProvider<Verbintenis>, ISortableDataProvider<Verbintenis>, IFilterStateLocator
{
	private static final long serialVersionUID = 1L;

	private DeelnemerVerbintenisCount cachedSize = null;

	private GeneralFilteredSortableDataProvider<Verbintenis, VerbintenisZoekFilter> innerProvider;

	public DeelnemerDataProvider(
			GeneralFilteredSortableDataProvider<Verbintenis, VerbintenisZoekFilter> innerProvider)
	{
		this.innerProvider = innerProvider;
	}

	@Override
	public void clearCache()
	{
		innerProvider.clearCache();
	}

	@Override
	public Iterator< ? extends Verbintenis> iterator(int first, int count)
	{
		return innerProvider.iterator(first, count);
	}

	@Override
	public IModel<Verbintenis> model(Verbintenis object)
	{
		return innerProvider.model(object);
	}

	@Override
	public int size()
	{
		return getSize().getVerbintenisCount();
	}

	public int getDeelnemerCount()
	{
		return getSize().getDeelnemerCount();
	}

	private DeelnemerVerbintenisCount getSize()
	{
		if (cachedSize == null)
		{
			VerbintenisDataAccessHelper dah =
				DataAccessRegistry.getHelper(VerbintenisDataAccessHelper.class);
			cachedSize = dah.getDeelnemerVerbintenisCount(innerProvider.getFilterState());
		}
		return cachedSize;
	}

	@Override
	public void detach()
	{
		cachedSize = null;
		innerProvider.detach();
	}

	@Override
	public int getIndex(Object obj)
	{
		return innerProvider.getIndex(obj);
	}

	@Override
	public ISortState getSortState()
	{
		return innerProvider.getSortState();
	}

	@Override
	public void setSortState(ISortState state)
	{
		innerProvider.setSortState(state);
	}

	@Override
	public Object getFilterState()
	{
		return innerProvider.getFilterState();
	}

	@Override
	public void setFilterState(Object state)
	{
		innerProvider.setFilterState(state);
	}
}
