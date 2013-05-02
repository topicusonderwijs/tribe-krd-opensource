package nl.topicus.eduarte.web.components.quicksearch.locatie;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.LocatieDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.zoekfilters.LocatieZoekFilter;

import org.apache.wicket.model.IModel;

public class LocatieQuickSearchField extends QuickSearchField<Locatie>
{
	private static final long serialVersionUID = 1L;

	private LocatieZoekFilter filter;

	public LocatieQuickSearchField(String id, IModel<Locatie> model, LocatieZoekFilter filter)
	{
		super(id, model, QuickSearchModel.of(LocatieDataAccessHelper.class, filter),
			new IdObjectRenderer<Locatie>());
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
