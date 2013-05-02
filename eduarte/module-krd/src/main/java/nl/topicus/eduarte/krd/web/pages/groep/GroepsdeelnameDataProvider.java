package nl.topicus.eduarte.krd.web.pages.groep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dataproviders.CachingDataProvider;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.dataproviders.IndexedDataProvider;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.zoekfilters.DeelnemerZoekFilter;
import nl.topicus.eduarte.zoekfilters.GroepsdeelnameZoekFilter;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class GroepsdeelnameDataProvider implements CachingDataProvider<Groepsdeelname>,
		IndexedDataProvider<Groepsdeelname>, IFilterStateLocator,
		ISortableDataProvider<Groepsdeelname>
{
	private static final long serialVersionUID = 1L;

	private GeneralFilteredSortableDataProvider<Deelnemer, DeelnemerZoekFilter> deelnemerProvider;

	private GroepModel groepModel;

	private IModel<List<Groepsdeelname>> deelnameModel;

	private ISortState sortStateWrapper;

	private GroepsdeelnameZoekFilter deelnameFilter;

	public GroepsdeelnameDataProvider(GroepsdeelnameZoekFilter deelnameFilter, GroepModel groepModel)
	{
		this.deelnameFilter = deelnameFilter;
		this.groepModel = groepModel;
		deelnameFilter.getDeelnemerFilter().setDeelnemerIdsInModel(
			new LoadableDetachableModel<List<Long>>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Long> load()
				{
					return getModelDeelnemerIds();
				}
			});
		deelnemerProvider =
			GeneralFilteredSortableDataProvider.of(deelnameFilter.getDeelnemerFilter(),
				DeelnemerDataAccessHelper.class);
		this.deelnameModel = groepModel.getDeelnamesListModel();
		sortStateWrapper = new ISortState()
		{
			private static final long serialVersionUID = 1L;

			private final int STRIP = "deelnemer.".length();

			@Override
			public int getPropertySortOrder(String property)
			{
				if (property.startsWith("deelnemer."))
					return getWrappedState().getPropertySortOrder(property.substring(STRIP));
				return getWrappedState().getPropertySortOrder(property);
			}

			@Override
			public void setPropertySortOrder(String property, int state)
			{
				if (property.startsWith("deelnemer."))
					getWrappedState().setPropertySortOrder(property.substring(STRIP), state);
				else
					getWrappedState().setPropertySortOrder(property, state);
			}

			private ISortState getWrappedState()
			{
				return GroepsdeelnameDataProvider.this.deelnemerProvider.getSortState();
			}
		};
	}

	@Override
	public Iterator<Groepsdeelname> iterator(int first, int count)
	{
		if (first == -1 || count == -1)
			return getDeelnames().iterator();

		List<Groepsdeelname> ret = getDeelnames();
		if (ret.isEmpty())
			return ret.iterator();

		int useFirst = Math.max(0, Math.min(ret.size() - 1, first));
		int useCount = Math.max(0, Math.min(ret.size(), count));
		return ret.subList(useFirst, useFirst + useCount).iterator();
	}

	@Override
	public int size()
	{
		return getDeelnames().size();
	}

	@Override
	public int getIndex(Object obj)
	{
		return getDeelnames().indexOf(obj);
	}

	private List<Groepsdeelname> getDeelnames()
	{
		Iterator<Deelnemer> deelnemers = deelnemerProvider.iterator(0, Integer.MAX_VALUE);
		Map<Deelnemer, List<Groepsdeelname>> deelnames =
			new HashMap<Deelnemer, List<Groepsdeelname>>();
		for (Groepsdeelname curDeelname : getModelDeelnames())
		{
			List<Groepsdeelname> curDeelnames = deelnames.get(curDeelname.getDeelnemer());
			if (curDeelnames == null)
			{
				curDeelnames = new ArrayList<Groepsdeelname>();
				deelnames.put(curDeelname.getDeelnemer(), curDeelnames);
			}
			curDeelnames.add(curDeelname);
		}

		List<Groepsdeelname> ret = new ArrayList<Groepsdeelname>();
		while (deelnemers.hasNext())
		{
			List<Groepsdeelname> curDeelnames = deelnames.get(deelnemers.next());
			if (curDeelnames != null)
			{
				for (Groepsdeelname curDeelname : curDeelnames)
				{
					if (matchesFilter(curDeelname))
						ret.add(curDeelname);
				}
			}
		}

		return ret;
	}

	private boolean matchesFilter(Groepsdeelname deelname)
	{
		if (deelnameFilter.getPeildatum() != null)
		{
			if (deelname.getEinddatum() != null
				&& !deelname.getEinddatum().after(deelnameFilter.getPeildatum()))
				return false;
			if (!deelnameFilter.isToonToekomstigeDeelnames()
				&& !deelname.getBegindatum().before(deelnameFilter.getPeildatum()))
				return false;
		}
		return true;
	}

	public List<Long> getModelDeelnemerIds()
	{
		List<Long> ret = new ArrayList<Long>();
		for (Groepsdeelname curDeelname : getModelDeelnames())
			ret.add(curDeelname.getDeelnemer().getId());
		return ret;
	}

	private List<Groepsdeelname> getModelDeelnames()
	{
		return deelnameModel.getObject();
	}

	@Override
	public void clearCache()
	{
		deelnemerProvider.clearCache();
	}

	@Override
	public IModel<Groepsdeelname> model(Groepsdeelname object)
	{
		return groepModel.getEntiteitManager().getModel(object, null);
	}

	@Override
	public void detach()
	{
		deelnemerProvider.detach();
		deelnameModel.detach();
		deelnameFilter.detach();
	}

	@Override
	public Object getFilterState()
	{
		return deelnemerProvider.getFilterState();
	}

	@Override
	public void setFilterState(Object state)
	{
		deelnemerProvider.setFilterState(state);
	}

	@Override
	public ISortState getSortState()
	{
		return sortStateWrapper;
	}

	@Override
	public void setSortState(ISortState state)
	{
		throw new UnsupportedOperationException();
	}
}
