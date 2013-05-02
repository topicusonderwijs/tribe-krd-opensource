package nl.topicus.eduarte.web.components.quicksearch.bpvcriteria;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.bpv.BPVCriteriaDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVCriteria;
import nl.topicus.eduarte.zoekfilters.bpv.BPVCriteriaZoekFilter;

import org.apache.wicket.model.IModel;

public class BPVCriteriaQuickSearchField extends QuickSearchField<BPVCriteria>
{
	private static final long serialVersionUID = 1L;

	private BPVCriteriaZoekFilter filter;

	public BPVCriteriaQuickSearchField(String id, IModel<BPVCriteria> model,
			BPVCriteriaZoekFilter filter)
	{
		super(id, model, QuickSearchModel.of(BPVCriteriaDataAccessHelper.class, filter),
			new IdObjectRenderer<BPVCriteria>());
		this.filter = filter;
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}

	@Override
	public Integer getWidth()
	{
		return 300;
	}
}
