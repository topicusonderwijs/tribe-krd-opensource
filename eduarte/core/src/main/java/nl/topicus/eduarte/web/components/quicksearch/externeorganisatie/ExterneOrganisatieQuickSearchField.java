package nl.topicus.eduarte.web.components.quicksearch.externeorganisatie;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.ExterneOrganisatieDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieZoekFilter;

import org.apache.wicket.model.IModel;

public class ExterneOrganisatieQuickSearchField extends QuickSearchField<ExterneOrganisatie>
{
	private static final long serialVersionUID = 1L;

	private ExterneOrganisatieZoekFilter filter;

	public ExterneOrganisatieQuickSearchField(String id, IModel<ExterneOrganisatie> model,
			ExterneOrganisatieZoekFilter filter)
	{
		super(id, model, QuickSearchModel.of(ExterneOrganisatieDataAccessHelper.class, filter),
			new IdObjectRenderer<ExterneOrganisatie>());
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
