package nl.topicus.eduarte.web.components.quicksearch.gemeente;

import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.landelijk.Gemeente;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.gemeente.GemeenteSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.GemeenteZoekFilter;

import org.apache.wicket.model.IModel;

public class GemeenteSearchEditor extends AbstractSearchEditor<Gemeente>
{
	private static final long serialVersionUID = 1L;

	private static final GemeenteZoekFilter getDefaultFilter()
	{
		GemeenteZoekFilter filter = new GemeenteZoekFilter();
		filter.addOrderByProperty("code");
		filter.addOrderByProperty("naam");
		return filter;
	}

	private GemeenteZoekFilter filter;

	public GemeenteSearchEditor(String id)
	{
		this(id, null, getDefaultFilter());
	}

	public GemeenteSearchEditor(String id, IModel<Gemeente> model)
	{
		this(id, model, getDefaultFilter());
	}

	public GemeenteSearchEditor(String id, IModel<Gemeente> model, GemeenteZoekFilter filter)
	{
		super(id, model);
		this.filter = filter;
	}

	@Override
	public AbstractZoekenModalWindow<Gemeente> createModelWindow(String id, IModel<Gemeente> model)
	{
		return new GemeenteSelectieModalWindow(id, model, filter);
	}

	@Override
	public QuickSearchField<Gemeente> createSearchField(String id, IModel<Gemeente> model)
	{
		return new GemeenteQuickSearchField(id, model, new ZoekFilterCopyManager()
			.copyObject(filter));
	}
}
