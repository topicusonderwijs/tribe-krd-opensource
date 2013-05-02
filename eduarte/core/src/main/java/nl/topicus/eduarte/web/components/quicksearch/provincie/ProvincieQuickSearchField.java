package nl.topicus.eduarte.web.components.quicksearch.provincie;

import java.util.List;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.ProvincieDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Provincie;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.apache.wicket.model.IModel;

public class ProvincieQuickSearchField extends QuickSearchField<Provincie>
{
	private static final long serialVersionUID = 1L;

	private LandelijkCodeNaamZoekFilter<Provincie> filter;

	public ProvincieQuickSearchField(String id, IModel<Provincie> model,
			LandelijkCodeNaamZoekFilter<Provincie> filter)
	{
		super(id, model, QuickSearchModel.of(ProvincieDataAccessHelper.class, filter),
			new IdObjectRenderer<Provincie>());
		this.filter = filter;
	}

	@Override
	protected Provincie convertInvalidInput(String input, List<Provincie> possibleResults)
	{
		for (Provincie curProvincie : possibleResults)
		{
			if (curProvincie.getNaam().equalsIgnoreCase(input)
				|| curProvincie.getCode().equalsIgnoreCase(input))
				return curProvincie;
		}
		return null;
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
