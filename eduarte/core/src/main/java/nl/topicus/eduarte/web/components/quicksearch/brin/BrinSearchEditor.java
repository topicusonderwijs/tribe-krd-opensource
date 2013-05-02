package nl.topicus.eduarte.web.components.quicksearch.brin;

import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.brin.BrinSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.BrinZoekFilter;

import org.apache.wicket.model.IModel;

public class BrinSearchEditor extends AbstractSearchEditor<Brin>
{
	private static final long serialVersionUID = 1L;

	private static final BrinZoekFilter getDefaultFilter()
	{
		BrinZoekFilter filter = new BrinZoekFilter();
		filter.addOrderByProperty("code");
		return filter;
	}

	private BrinZoekFilter filter;

	public BrinSearchEditor(String id, IModel<Brin> model)
	{
		this(id, model, getDefaultFilter());
	}

	public BrinSearchEditor(String id, IModel<Brin> model, BrinZoekFilter filter)
	{
		super(id, model);
		this.filter = filter;
	}

	@Override
	public AbstractZoekenModalWindow<Brin> createModelWindow(String id, IModel<Brin> model)
	{
		return new BrinSelectieModalWindow(id, model, filter);
	}

	@Override
	public QuickSearchField<Brin> createSearchField(String id, IModel<Brin> model)
	{
		return new BrinQuickSearchField(id, model, new ZoekFilterCopyManager().copyObject(filter));
	}
}
