package nl.topicus.eduarte.web.components.quicksearch.soortvooropleiding;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.SoortVooropleidingDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding;
import nl.topicus.eduarte.zoekfilters.SoortVooropleidingZoekFilter;

import org.apache.wicket.model.IModel;

public class SoortVooropleidingSearchField extends QuickSearchField<SoortVooropleiding>
{
	private static final long serialVersionUID = 1L;

	private SoortVooropleidingZoekFilter filter;

	public SoortVooropleidingSearchField(String id, IModel<SoortVooropleiding> model,
			SoortVooropleidingZoekFilter filter)
	{
		super(id, model, QuickSearchModel.of(SoortVooropleidingDataAccessHelper.class, filter),
			new IdObjectRenderer<SoortVooropleiding>());
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
