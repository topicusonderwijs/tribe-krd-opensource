package nl.topicus.eduarte.web.components.quicksearch.verblijfsvergunning;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.VerblijfsvergunningDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Verblijfsvergunning;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.apache.wicket.model.IModel;

public class VerblijfsvergunningQuickSearchField extends QuickSearchField<Verblijfsvergunning>
{
	private static final long serialVersionUID = 1L;

	private LandelijkCodeNaamZoekFilter<Verblijfsvergunning> filter;

	public VerblijfsvergunningQuickSearchField(String id, IModel<Verblijfsvergunning> model,
			LandelijkCodeNaamZoekFilter<Verblijfsvergunning> landelijkCodeNaamZoekFilter)
	{
		super(id, model, QuickSearchModel.of(VerblijfsvergunningDataAccessHelper.class,
			landelijkCodeNaamZoekFilter), new IdObjectRenderer<Verblijfsvergunning>());
		this.filter = landelijkCodeNaamZoekFilter;
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
