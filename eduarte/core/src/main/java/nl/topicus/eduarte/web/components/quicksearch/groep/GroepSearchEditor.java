package nl.topicus.eduarte.web.components.quicksearch.groep;

import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.groep.GroepSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.model.IModel;

public class GroepSearchEditor extends AbstractSearchEditor<Groep>
{
	private static final long serialVersionUID = 1L;

	private GroepZoekFilter filter;

	public GroepSearchEditor(String id, IModel<Groep> model)
	{
		this(id, model, GroepZoekFilter.createDefaultFilter());
	}

	public GroepSearchEditor(String id, IModel<Groep> model, GroepZoekFilter filter)
	{
		super(id, model);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		this.filter = filter;
	}

	@Override
	public AbstractZoekenModalWindow<Groep> createModelWindow(String id, IModel<Groep> model)
	{
		return new GroepSelectieModalWindow(id, model, filter);
	}

	@Override
	public QuickSearchField<Groep> createSearchField(String id, IModel<Groep> model)
	{
		return new GroepQuickSearchField(id, model, new ZoekFilterCopyManager().copyObject(filter));
	}
}
