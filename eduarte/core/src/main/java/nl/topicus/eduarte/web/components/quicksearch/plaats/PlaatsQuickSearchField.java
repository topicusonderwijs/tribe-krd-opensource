package nl.topicus.eduarte.web.components.quicksearch.plaats;

import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.PlaatsDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.landelijk.Plaats;
import nl.topicus.eduarte.zoekfilters.PlaatsZoekFilter;

import org.apache.wicket.model.IModel;

public class PlaatsQuickSearchField extends QuickSearchField<Plaats>
{
	private static final long serialVersionUID = 1L;

	private PlaatsZoekFilter filter;

	private IModel<Land> landModel;

	public PlaatsQuickSearchField(String id, IModel<Plaats> model, IModel<Land> landModel,
			PlaatsZoekFilter filter)
	{
		super(id, model, QuickSearchModel.of(PlaatsDataAccessHelper.class, filter),
			new IdObjectRenderer<Plaats>());
		this.filter = filter;
		this.landModel = landModel;
	}

	@Override
	protected Plaats convertInvalidInput(String input, List<Plaats> possibleResults)
	{
		if (StringUtil.isNotEmpty(input) && !isNederland())
			return new Plaats(input);

		for (Plaats curPlaats : possibleResults)
		{
			if (curPlaats.getNaam().equals(input))
				return curPlaats;
		}
		return null;
	}

	@Override
	protected void convertInput()
	{
		if (!isNederland())
			setHiddenValueOverride("");

		super.convertInput();
	}

	@Override
	public List<Plaats> getChoices(String query)
	{
		if (!isNederland())
			return Collections.emptyList();

		return super.getChoices(query);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}

	private boolean isNederland()
	{
		if (landModel == null)
			return true;

		Land land = landModel.getObject();
		return land != null && land.isNederland();
	}

}
