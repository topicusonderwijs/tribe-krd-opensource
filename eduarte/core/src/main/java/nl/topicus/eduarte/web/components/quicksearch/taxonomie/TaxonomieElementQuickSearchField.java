package nl.topicus.eduarte.web.components.quicksearch.taxonomie;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.TaxonomieElementDataAccessHelper;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.zoekfilters.TaxonomieElementZoekFilter;

import org.apache.wicket.model.IModel;

public class TaxonomieElementQuickSearchField extends QuickSearchField<TaxonomieElement>
{
	private static final long serialVersionUID = 1L;

	private TaxonomieElementZoekFilter filter;

	public TaxonomieElementQuickSearchField(String id, IModel<TaxonomieElement> model,
			TaxonomieElementZoekFilter filter)
	{
		super(id, model, QuickSearchModel.of(TaxonomieElementDataAccessHelper.class, filter),
			new IdObjectRenderer<TaxonomieElement>());
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
