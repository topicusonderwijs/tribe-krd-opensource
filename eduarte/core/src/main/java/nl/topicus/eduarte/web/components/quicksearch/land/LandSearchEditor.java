package nl.topicus.eduarte.web.components.quicksearch.land;

import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.land.LandSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.apache.wicket.model.IModel;

public class LandSearchEditor extends AbstractSearchEditor<Land>
{
	private static final long serialVersionUID = 1L;

	private static final LandelijkCodeNaamZoekFilter<Land> getDefaultFilter()
	{
		LandelijkCodeNaamZoekFilter<Land> filter = LandelijkCodeNaamZoekFilter.of(Land.class);
		filter.addOrderByProperty("code");
		filter.addOrderByProperty("naam");
		return filter;
	}

	private LandelijkCodeNaamZoekFilter<Land> filter;

	public LandSearchEditor(String id)
	{
		this(id, null, getDefaultFilter());
	}

	public LandSearchEditor(String id, IModel<Land> model)
	{
		this(id, model, getDefaultFilter());
	}

	public LandSearchEditor(String id, IModel<Land> model, LandelijkCodeNaamZoekFilter<Land> filter)
	{
		super(id, model);
		this.filter = filter;
	}

	@Override
	public AbstractZoekenModalWindow<Land> createModelWindow(String id, IModel<Land> model)
	{
		return new LandSelectieModalWindow(id, model, filter);
	}

	@Override
	public QuickSearchField<Land> createSearchField(String id, IModel<Land> model)
	{
		return new LandQuickSearchField(id, model, new ZoekFilterCopyManager().copyObject(filter));
	}
}
