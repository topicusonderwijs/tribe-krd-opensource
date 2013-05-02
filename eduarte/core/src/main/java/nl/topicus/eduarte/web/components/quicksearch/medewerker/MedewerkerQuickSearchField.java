package nl.topicus.eduarte.web.components.quicksearch.medewerker;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.MedewerkerDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.zoekfilters.MedewerkerZoekFilter;

import org.apache.wicket.model.IModel;

public class MedewerkerQuickSearchField extends QuickSearchField<Medewerker>
{
	private static final long serialVersionUID = 1L;

	private MedewerkerZoekFilter filter;

	public MedewerkerQuickSearchField(String id, IModel<Medewerker> model,
			MedewerkerZoekFilter filter)
	{
		super(id, model, QuickSearchModel.of(MedewerkerDataAccessHelper.class, filter),
			new IdObjectRenderer<Medewerker>());
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
