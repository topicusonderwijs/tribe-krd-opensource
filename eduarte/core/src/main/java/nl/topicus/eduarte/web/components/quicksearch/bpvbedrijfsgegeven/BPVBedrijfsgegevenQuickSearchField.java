package nl.topicus.eduarte.web.components.quicksearch.bpvbedrijfsgegeven;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.BPVBedrijfsgegevenDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.zoekfilters.BPVBedrijfsgegevenZoekFilter;

import org.apache.wicket.model.IModel;

public class BPVBedrijfsgegevenQuickSearchField extends QuickSearchField<BPVBedrijfsgegeven>
{
	private static final long serialVersionUID = 1L;

	private BPVBedrijfsgegevenZoekFilter filter;

	public BPVBedrijfsgegevenQuickSearchField(String id, IModel<BPVBedrijfsgegeven> model,
			BPVBedrijfsgegevenZoekFilter filter)
	{
		super(id, model, QuickSearchModel.of(BPVBedrijfsgegevenDataAccessHelper.class, filter),
			new IdObjectRenderer<BPVBedrijfsgegeven>());
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
