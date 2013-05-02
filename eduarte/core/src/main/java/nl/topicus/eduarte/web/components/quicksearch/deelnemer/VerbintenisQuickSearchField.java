package nl.topicus.eduarte.web.components.quicksearch.deelnemer;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author Henzen
 */

public class VerbintenisQuickSearchField extends QuickSearchField<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	private VerbintenisZoekFilter filter;

	public VerbintenisQuickSearchField(String id, IModel<Verbintenis> model,
			VerbintenisZoekFilter filter)
	{
		super(id, model, QuickSearchModel.of(VerbintenisDataAccessHelper.class, filter),
			new IdObjectRenderer<Verbintenis>());
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
		return 400;
	}
}
