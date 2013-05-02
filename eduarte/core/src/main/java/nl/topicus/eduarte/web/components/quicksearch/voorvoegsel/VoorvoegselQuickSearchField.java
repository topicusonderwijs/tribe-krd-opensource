package nl.topicus.eduarte.web.components.quicksearch.voorvoegsel;

import java.util.List;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.VoorvoegselDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Voorvoegsel;
import nl.topicus.eduarte.zoekfilters.VoorvoegselZoekFilter;

import org.apache.wicket.model.IModel;

public class VoorvoegselQuickSearchField extends QuickSearchField<Voorvoegsel>
{
	private static final long serialVersionUID = 1L;

	private VoorvoegselZoekFilter filter;

	public VoorvoegselQuickSearchField(String id, IModel<Voorvoegsel> model,
			VoorvoegselZoekFilter filter)
	{
		super(id, model, QuickSearchModel.of(VoorvoegselDataAccessHelper.class, filter),
			new IdObjectRenderer<Voorvoegsel>());
		this.filter = filter;
	}

	@Override
	protected Voorvoegsel convertInvalidInput(String input, List<Voorvoegsel> possibleResults)
	{
		for (Voorvoegsel curVoorvoegsel : possibleResults)
		{
			if (curVoorvoegsel.getNaam().equals(input))
				return curVoorvoegsel;
		}
		return null;
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
