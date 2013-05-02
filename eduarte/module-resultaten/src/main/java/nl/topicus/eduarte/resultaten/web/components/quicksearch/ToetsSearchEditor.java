package nl.topicus.eduarte.resultaten.web.components.quicksearch;

import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.model.IModel;

public class ToetsSearchEditor extends AbstractSearchEditor<Toets>
{
	private static final long serialVersionUID = 1L;

	private ToetsZoekFilter filter;

	public ToetsSearchEditor(String id, IModel<Toets> model, ToetsZoekFilter filter)
	{
		super(id, model);
		this.filter = filter;
		filter.setResultaatstructuurFilter(new ResultaatstructuurZoekFilter());
		filter.getResultaatstructuurFilter().setAuthorizationContext(
			new OrganisatieEenheidLocatieAuthorizationContext(this));
		filter.getResultaatstructuurFilter().setCohort(Cohort.getHuidigCohort());
	}

	@Override
	public AbstractZoekenModalWindow<Toets> createModelWindow(String id, IModel<Toets> model)
	{
		return new ToetsSelectieModalWindow(id, model, filter);
	}

	@Override
	public QuickSearchField<Toets> createSearchField(String id, IModel<Toets> model)
	{
		return new ToetsQuickSearchField(id, model, new ZoekFilterCopyManager().copyObject(filter));
	}
}
