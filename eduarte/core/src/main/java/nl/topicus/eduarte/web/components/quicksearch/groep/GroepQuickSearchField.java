package nl.topicus.eduarte.web.components.quicksearch.groep;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.GroepDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;

import org.apache.wicket.model.IModel;

public class GroepQuickSearchField extends QuickSearchField<Groep>
{
	private static final long serialVersionUID = 1L;

	private GroepZoekFilter filter;

	public GroepQuickSearchField(String id, IModel<Groep> model, GroepZoekFilter filter)
	{
		super(id, model, QuickSearchModel.of(GroepDataAccessHelper.class, filter),
			new IdObjectRenderer<Groep>());
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
