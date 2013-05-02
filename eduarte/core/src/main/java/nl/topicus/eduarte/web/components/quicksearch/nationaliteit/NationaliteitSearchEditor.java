package nl.topicus.eduarte.web.components.quicksearch.nationaliteit;

import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.landelijk.Nationaliteit;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.nationaliteit.NationaliteitSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.apache.wicket.model.IModel;

public class NationaliteitSearchEditor extends AbstractSearchEditor<Nationaliteit>
{
	private static final long serialVersionUID = 1L;

	private static final LandelijkCodeNaamZoekFilter<Nationaliteit> getDefaultFilter()
	{
		LandelijkCodeNaamZoekFilter<Nationaliteit> filter =
			LandelijkCodeNaamZoekFilter.of(Nationaliteit.class);
		filter.addOrderByProperty("code");
		filter.addOrderByProperty("naam");
		return filter;
	}

	private LandelijkCodeNaamZoekFilter<Nationaliteit> filter;

	public NationaliteitSearchEditor(String id)
	{
		this(id, null, getDefaultFilter());
	}

	public NationaliteitSearchEditor(String id, IModel<Nationaliteit> model)
	{
		this(id, model, getDefaultFilter());
	}

	public NationaliteitSearchEditor(String id, IModel<Nationaliteit> model,
			LandelijkCodeNaamZoekFilter<Nationaliteit> filter)
	{
		super(id, model);
		this.filter = filter;
	}

	@Override
	public AbstractZoekenModalWindow<Nationaliteit> createModelWindow(String id,
			IModel<Nationaliteit> model)
	{
		return new NationaliteitSelectieModalWindow(id, model, filter);
	}

	@Override
	public QuickSearchField<Nationaliteit> createSearchField(String id, IModel<Nationaliteit> model)
	{
		return new NationaliteitQuickSearchField(id, model,
			new ZoekFilterCopyManager().copyObject(filter));
	}
}
