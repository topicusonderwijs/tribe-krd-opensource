package nl.topicus.eduarte.web.components.quicksearch.persoon;

import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.persoon.PersoonSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.PersoonZoekFilter;

import org.apache.wicket.model.IModel;

public class PersoonSearchEditor extends AbstractSearchEditor<Persoon>
{
	private static final long serialVersionUID = 1L;

	private static final PersoonZoekFilter<Persoon> getDefaultFilter()
	{
		PersoonZoekFilter<Persoon> filter = new PersoonZoekFilter<Persoon>();
		filter.addOrderByProperty("achternaam");
		return filter;
	}

	private PersoonZoekFilter<Persoon> filter;

	public PersoonSearchEditor(String id, IModel<Persoon> model)
	{
		this(id, model, getDefaultFilter());
	}

	public PersoonSearchEditor(String id, IModel<Persoon> model, PersoonZoekFilter<Persoon> filter)
	{
		super(id, model);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		this.filter = filter;
	}

	@Override
	public AbstractZoekenModalWindow<Persoon> createModelWindow(String id, IModel<Persoon> model)
	{
		return new PersoonSelectieModalWindow(id, model, filter);
	}

	@Override
	public QuickSearchField<Persoon> createSearchField(String id, IModel<Persoon> model)
	{
		return new PersoonQuickSearchField(id, model, new ZoekFilterCopyManager()
			.copyObject(filter));
	}
}
