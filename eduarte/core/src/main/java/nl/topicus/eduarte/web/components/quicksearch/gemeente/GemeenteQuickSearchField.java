package nl.topicus.eduarte.web.components.quicksearch.gemeente;

import java.util.List;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.GemeenteDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Gemeente;
import nl.topicus.eduarte.zoekfilters.GemeenteZoekFilter;

import org.apache.wicket.model.IModel;

public class GemeenteQuickSearchField extends QuickSearchField<Gemeente>
{
	private static final long serialVersionUID = 1L;

	private GemeenteZoekFilter filter;

	public GemeenteQuickSearchField(String id, IModel<Gemeente> model, GemeenteZoekFilter filter)
	{
		super(id, model, QuickSearchModel.of(GemeenteDataAccessHelper.class, filter),
			new IdObjectRenderer<Gemeente>());
		this.filter = filter;
	}

	@Override
	protected Gemeente convertInvalidInput(String input, List<Gemeente> possibleResults)
	{
		for (Gemeente curGemeente : possibleResults)
		{
			if (curGemeente.getNaam().equalsIgnoreCase(input)
				|| curGemeente.getCode().equalsIgnoreCase(input))
				return curGemeente;
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
