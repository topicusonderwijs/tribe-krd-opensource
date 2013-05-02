package nl.topicus.eduarte.web.components.quicksearch.externeorganisatie;

import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.externeorganisatie.ExterneOrganisatieSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieZoekFilter;

import org.apache.wicket.model.IModel;

public class ExterneOrganisatieSearchEditor extends AbstractSearchEditor<ExterneOrganisatie>
{
	private static final long serialVersionUID = 1L;

	private static ExterneOrganisatieZoekFilter getDefaultFilter()
	{
		ExterneOrganisatieZoekFilter filter = new ExterneOrganisatieZoekFilter();
		filter.addOrderByProperty("naam");
		return filter;
	}

	private ExterneOrganisatieZoekFilter filter;

	public ExterneOrganisatieSearchEditor(String id, IModel<ExterneOrganisatie> model)
	{
		this(id, model, getDefaultFilter());
	}

	public ExterneOrganisatieSearchEditor(String id, IModel<ExterneOrganisatie> model,
			ExterneOrganisatieZoekFilter filter)
	{
		super(id, model);
		this.filter = filter;
	}

	@Override
	public AbstractZoekenModalWindow<ExterneOrganisatie> createModelWindow(String id,
			IModel<ExterneOrganisatie> model)
	{
		return new ExterneOrganisatieSelectieModalWindow(id, model, filter);
	}

	@Override
	public QuickSearchField<ExterneOrganisatie> createSearchField(String id,
			IModel<ExterneOrganisatie> model)
	{
		return new ExterneOrganisatieQuickSearchField(id, model, new ZoekFilterCopyManager()
			.copyObject(filter));
	}
}
