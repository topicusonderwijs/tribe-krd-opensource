package nl.topicus.eduarte.web.components.quicksearch.onderwijsproduct;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.onderwijsproduct.OnderwijsproductSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.model.IModel;

public class OnderwijsproductSearchEditor extends AbstractSearchEditor<Onderwijsproduct>
{
	private static final long serialVersionUID = 1L;

	private OnderwijsproductZoekFilter filter;

	public OnderwijsproductSearchEditor(String id, IModel<Onderwijsproduct> model)
	{
		this(id, model, OnderwijsproductZoekFilter.createDefaultFilter());
	}

	public OnderwijsproductSearchEditor(String id, IModel<Onderwijsproduct> model,
			OnderwijsproductZoekFilter filter)
	{
		super(id, model);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		this.filter = filter;
	}

	@Override
	public AbstractZoekenModalWindow<Onderwijsproduct> createModelWindow(String id,
			IModel<Onderwijsproduct> model)
	{
		return new OnderwijsproductSelectieModalWindow(id, model, filter);
	}

	@Override
	public QuickSearchField<Onderwijsproduct> createSearchField(String id,
			IModel<Onderwijsproduct> model)
	{
		return new OnderwijsproductQuickSearchField(id, model, new ZoekFilterCopyManager()
			.copyObject(filter));
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
