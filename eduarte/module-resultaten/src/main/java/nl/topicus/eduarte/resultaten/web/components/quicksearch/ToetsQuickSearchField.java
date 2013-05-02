package nl.topicus.eduarte.resultaten.web.components.quicksearch;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.model.IModel;

public class ToetsQuickSearchField extends QuickSearchField<Toets>
{
	private static final long serialVersionUID = 1L;

	private ToetsZoekFilter filter;

	public ToetsQuickSearchField(String id, IModel<Toets> model, ToetsZoekFilter filter)
	{
		super(id, model, QuickSearchModel.of(ToetsDataAccessHelper.class, filter),
			new IdObjectRenderer<Toets>());
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
