package nl.topicus.eduarte.web.components.quicksearch.verblijfsvergunning;

import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.landelijk.Verblijfsvergunning;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.verblijfsvergunning.VerblijfsvergunningSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.apache.wicket.model.IModel;

public class VerblijfsvergunningSearchEditor extends AbstractSearchEditor<Verblijfsvergunning>
{
	private static final long serialVersionUID = 1L;

	private static final LandelijkCodeNaamZoekFilter<Verblijfsvergunning> getDefaultFilter()
	{
		LandelijkCodeNaamZoekFilter<Verblijfsvergunning> filter =
			LandelijkCodeNaamZoekFilter.of(Verblijfsvergunning.class);
		filter.addOrderByProperty("naam");
		return filter;
	}

	private LandelijkCodeNaamZoekFilter<Verblijfsvergunning> filter;

	public VerblijfsvergunningSearchEditor(String id, IModel<Verblijfsvergunning> model)
	{
		this(id, model, getDefaultFilter());
	}

	public VerblijfsvergunningSearchEditor(String id, IModel<Verblijfsvergunning> model,
			LandelijkCodeNaamZoekFilter<Verblijfsvergunning> filter)
	{
		super(id, model);
		this.filter = filter;
	}

	@Override
	public AbstractZoekenModalWindow<Verblijfsvergunning> createModelWindow(String id,
			IModel<Verblijfsvergunning> model)
	{
		return new VerblijfsvergunningSelectieModalWindow(id, model, filter);
	}

	@Override
	public VerblijfsvergunningQuickSearchField createSearchField(String id,
			IModel<Verblijfsvergunning> model)
	{
		return new VerblijfsvergunningQuickSearchField(id, model,
			new ZoekFilterCopyManager().copyObject(filter));
	}
}
