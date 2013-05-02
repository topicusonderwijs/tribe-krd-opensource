package nl.topicus.eduarte.web.components.quicksearch.deelnemer;

import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.deelnemer.DeelnemerSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author Henzen
 */

public class VerbintenisSearchEditor extends AbstractSearchEditor<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	private static final VerbintenisZoekFilter getDefaultFilter()
	{
		VerbintenisZoekFilter filter = new VerbintenisZoekFilter();
		filter.addOrderByProperty("persoon.achternaam");
		return filter;
	}

	private VerbintenisZoekFilter filter;

	public VerbintenisSearchEditor(String id, IModel<Verbintenis> model)
	{
		this(id, model, getDefaultFilter());
	}

	public VerbintenisSearchEditor(String id, IModel<Verbintenis> model,
			VerbintenisZoekFilter filter)
	{
		super(id, model);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		this.filter = filter;
	}

	@Override
	public AbstractZoekenModalWindow<Verbintenis> createModelWindow(String id,
			IModel<Verbintenis> model)
	{
		return new DeelnemerSelectieModalWindow(id, model, filter);
	}

	@Override
	public QuickSearchField<Verbintenis> createSearchField(String id, IModel<Verbintenis> model)
	{
		return new VerbintenisQuickSearchField(id, model, new ZoekFilterCopyManager()
			.copyObject(filter));
	}
}
