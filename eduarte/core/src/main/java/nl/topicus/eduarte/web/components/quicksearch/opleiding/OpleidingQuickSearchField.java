package nl.topicus.eduarte.web.components.quicksearch.opleiding;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.OpleidingDataAccessHelper;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;

import org.apache.wicket.model.IModel;

public class OpleidingQuickSearchField extends QuickSearchField<Opleiding>
{
	private static final long serialVersionUID = 1L;

	private OpleidingZoekFilter filter;

	public OpleidingQuickSearchField(String id, IModel<Opleiding> model, OpleidingZoekFilter filter)
	{
		super(id, model, QuickSearchModel.of(OpleidingDataAccessHelper.class, filter),
			new IdObjectRenderer<Opleiding>());
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
