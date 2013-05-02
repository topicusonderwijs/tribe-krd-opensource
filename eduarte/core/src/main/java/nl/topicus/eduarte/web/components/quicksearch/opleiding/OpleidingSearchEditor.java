package nl.topicus.eduarte.web.components.quicksearch.opleiding;

import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.opleiding.OpleidingSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.model.IModel;

public class OpleidingSearchEditor extends AbstractSearchEditor<Opleiding>
{
	private static final long serialVersionUID = 1L;

	private OpleidingZoekFilter filter;

	private boolean niveauZoekveld;

	public OpleidingSearchEditor(String id, IModel<Opleiding> model)
	{
		this(id, model, OpleidingZoekFilter.createDefaultFilter());
	}

	public OpleidingSearchEditor(String id, OpleidingZoekFilter filter)
	{
		this(id, null, filter);
	}

	public OpleidingSearchEditor(String id, IModel<Opleiding> model, OpleidingZoekFilter filter)
	{
		super(id, model);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		this.filter = filter;
	}

	public OpleidingSearchEditor(String id, IModel<Opleiding> model, OpleidingZoekFilter filter,
			boolean niveauZoekveld)
	{
		super(id, model);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		this.filter = filter;
		this.niveauZoekveld = niveauZoekveld;
	}

	@Override
	public AbstractZoekenModalWindow<Opleiding> createModelWindow(String id, IModel<Opleiding> model)
	{
		if (niveauZoekveld)
			return new OpleidingSelectieModalWindow(id, model, filter, niveauZoekveld);
		else
			return new OpleidingSelectieModalWindow(id, model, filter);
	}

	@Override
	public QuickSearchField<Opleiding> createSearchField(String id, IModel<Opleiding> model)
	{
		return new OpleidingQuickSearchField(id, model, new ZoekFilterCopyManager()
			.copyObject(filter));
	}
}
