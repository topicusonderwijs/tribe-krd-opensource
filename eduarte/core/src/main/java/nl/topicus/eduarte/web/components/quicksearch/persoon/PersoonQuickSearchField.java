package nl.topicus.eduarte.web.components.quicksearch.persoon;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.PersoonDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.zoekfilters.PersoonZoekFilter;

import org.apache.wicket.model.IModel;

public class PersoonQuickSearchField extends QuickSearchField<Persoon>
{
	private static final long serialVersionUID = 1L;

	private PersoonZoekFilter<Persoon> filter;

	public PersoonQuickSearchField(String id, IModel<Persoon> model,
			PersoonZoekFilter<Persoon> filter)
	{
		super(id, model, QuickSearchModel.of(PersoonDataAccessHelper.class, filter),
			new IdObjectRenderer<Persoon>());
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
