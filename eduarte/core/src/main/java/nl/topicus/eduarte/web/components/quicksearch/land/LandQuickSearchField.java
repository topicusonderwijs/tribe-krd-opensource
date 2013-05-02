package nl.topicus.eduarte.web.components.quicksearch.land;

import java.util.List;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.LandDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.apache.wicket.model.IModel;

public class LandQuickSearchField extends QuickSearchField<Land>
{
	private static final long serialVersionUID = 1L;

	private LandelijkCodeNaamZoekFilter<Land> filter;

	public LandQuickSearchField(String id, IModel<Land> model,
			LandelijkCodeNaamZoekFilter<Land> filter)
	{
		super(id, model, QuickSearchModel.of(LandDataAccessHelper.class, filter),
			new IdObjectRenderer<Land>());
		this.filter = filter;
	}

	@Override
	protected Land convertInvalidInput(String input, List<Land> possibleResults)
	{
		for (Land curLand : possibleResults)
		{
			if (curLand.getNaam().equalsIgnoreCase(input)
				|| curLand.getCode().equalsIgnoreCase(input))
				return curLand;
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
