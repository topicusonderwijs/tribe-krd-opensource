package nl.topicus.eduarte.web.components.quicksearch.bpvcriteria;

import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.bpv.BPVCriteria;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.bpvcriteria.BPVCriteriaSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.bpv.BPVCriteriaZoekFilter;

import org.apache.wicket.model.IModel;

public class BPVCriteriaSearchEditor extends AbstractSearchEditor<BPVCriteria>
{
	private static final long serialVersionUID = 1L;

	private BPVCriteriaZoekFilter filter;

	public BPVCriteriaSearchEditor(String id, IModel<BPVCriteria> model)
	{
		this(id, model, BPVCriteriaZoekFilter.createDefaultFilter());
	}

	public BPVCriteriaSearchEditor(String id, BPVCriteriaZoekFilter filter)
	{
		this(id, null, filter);
	}

	public BPVCriteriaSearchEditor(String id, IModel<BPVCriteria> model,
			BPVCriteriaZoekFilter filter)
	{
		super(id, model);
		// TODO Ralf: filter.setAuthorizationContext(new
		// OrganisatieEenheidLocatieAuthorizationContext(this));
		this.filter = filter;
	}

	@Override
	public AbstractZoekenModalWindow<BPVCriteria> createModelWindow(String id,
			IModel<BPVCriteria> model)
	{
		return new BPVCriteriaSelectieModalWindow(id, model, filter);
	}

	@Override
	public QuickSearchField<BPVCriteria> createSearchField(String id, IModel<BPVCriteria> model)
	{
		return new BPVCriteriaQuickSearchField(id, model, new ZoekFilterCopyManager()
			.copyObject(filter));
	}
}