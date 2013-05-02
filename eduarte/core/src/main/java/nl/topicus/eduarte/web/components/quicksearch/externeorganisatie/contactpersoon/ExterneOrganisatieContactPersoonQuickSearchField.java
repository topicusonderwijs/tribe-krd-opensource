package nl.topicus.eduarte.web.components.quicksearch.externeorganisatie.contactpersoon;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.helpers.ExterneOrganisatieContactPersoonDataAccessHelper;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieContactPersoonZoekFilter;

import org.apache.wicket.model.IModel;

public class ExterneOrganisatieContactPersoonQuickSearchField extends
		QuickSearchField<ExterneOrganisatieContactPersoon>
{
	private static final long serialVersionUID = 1L;

	private ExterneOrganisatieContactPersoonZoekFilter filter;

	public ExterneOrganisatieContactPersoonQuickSearchField(String id,
			IModel<ExterneOrganisatieContactPersoon> model,
			ExterneOrganisatieContactPersoonZoekFilter filter)
	{
		super(id, model, QuickSearchModel.of(
			ExterneOrganisatieContactPersoonDataAccessHelper.class, filter),
			new IdObjectRenderer<ExterneOrganisatieContactPersoon>());
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
