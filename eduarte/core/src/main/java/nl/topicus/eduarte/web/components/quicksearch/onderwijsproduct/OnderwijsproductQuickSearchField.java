package nl.topicus.eduarte.web.components.quicksearch.onderwijsproduct;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;

import org.apache.wicket.model.IModel;

public class OnderwijsproductQuickSearchField extends QuickSearchField<Onderwijsproduct>
{
	private static final long serialVersionUID = 1L;

	private OnderwijsproductZoekFilter filter;

	public OnderwijsproductQuickSearchField(String id, IModel<Onderwijsproduct> model,
			OnderwijsproductZoekFilter filter)
	{
		super(id, model, QuickSearchModel.of(OnderwijsproductDataAccessHelper.class, filter),
			new IdObjectRenderer<Onderwijsproduct>());
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
