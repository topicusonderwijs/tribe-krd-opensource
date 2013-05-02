package nl.topicus.eduarte.web.components.quicksearch.brin;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.BrinDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.zoekfilters.BrinZoekFilter;

import org.apache.wicket.model.IModel;

public class BrinQuickSearchField extends QuickSearchField<Brin>
{
	private static final long serialVersionUID = 1L;

	private BrinZoekFilter filter;

	public BrinQuickSearchField(String id, IModel<Brin> model, BrinZoekFilter filter)
	{
		super(id, model, QuickSearchModel.of(BrinDataAccessHelper.class, filter),
			new IdObjectRenderer<Brin>());
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
