package nl.topicus.eduarte.web.components.quicksearch.provincie;

import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.landelijk.Provincie;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.provincie.ProvincieSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.apache.wicket.model.IModel;

public class ProvincieSearchEditor extends AbstractSearchEditor<Provincie>
{
	private static final long serialVersionUID = 1L;

	private static final LandelijkCodeNaamZoekFilter<Provincie> getDefaultFilter()
	{
		LandelijkCodeNaamZoekFilter<Provincie> filter =
			LandelijkCodeNaamZoekFilter.of(Provincie.class);
		filter.addOrderByProperty("code");
		filter.addOrderByProperty("naam");
		return filter;
	}

	private LandelijkCodeNaamZoekFilter<Provincie> filter;

	public ProvincieSearchEditor(String id)
	{
		this(id, null, getDefaultFilter());
	}

	public ProvincieSearchEditor(String id, IModel<Provincie> model)
	{
		this(id, model, getDefaultFilter());
	}

	public ProvincieSearchEditor(String id, IModel<Provincie> model,
			LandelijkCodeNaamZoekFilter<Provincie> filter)
	{
		super(id, model);
		this.filter = filter;
	}

	@Override
	public AbstractZoekenModalWindow<Provincie> createModelWindow(String id, IModel<Provincie> model)
	{
		return new ProvincieSelectieModalWindow(id, model, filter);
	}

	@Override
	public QuickSearchField<Provincie> createSearchField(String id, IModel<Provincie> model)
	{
		return new ProvincieQuickSearchField(id, model,
			new ZoekFilterCopyManager().copyObject(filter));
	}
}
