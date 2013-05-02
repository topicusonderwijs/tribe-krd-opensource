package nl.topicus.eduarte.web.components.quicksearch.nationaliteit;

import java.util.List;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.NationaliteitDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Nationaliteit;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.apache.wicket.model.IModel;

public class NationaliteitQuickSearchField extends QuickSearchField<Nationaliteit>
{
	private static final long serialVersionUID = 1L;

	private LandelijkCodeNaamZoekFilter<Nationaliteit> filter;

	public NationaliteitQuickSearchField(String id, IModel<Nationaliteit> model,
			LandelijkCodeNaamZoekFilter<Nationaliteit> filter)
	{
		super(id, model, QuickSearchModel.of(NationaliteitDataAccessHelper.class, filter),
			new IdObjectRenderer<Nationaliteit>());
		this.filter = filter;
	}

	@Override
	protected Nationaliteit convertInvalidInput(String input, List<Nationaliteit> possibleResults)
	{
		for (Nationaliteit curNationaliteit : possibleResults)
		{
			if (curNationaliteit.getNaam().equalsIgnoreCase(input)
				|| curNationaliteit.getCode().equalsIgnoreCase(input))
				return curNationaliteit;
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
