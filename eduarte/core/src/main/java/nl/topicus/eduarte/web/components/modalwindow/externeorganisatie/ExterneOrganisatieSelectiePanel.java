package nl.topicus.eduarte.web.components.modalwindow.externeorganisatie;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.ExterneOrganisatieDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ExterneOrganisatieTable;
import nl.topicus.eduarte.web.components.panels.filter.ExterneOrganisatieZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieZoekFilter;

import org.apache.wicket.Component;

/**
 * Pagina waarmee een externe organisatie geselecteerd kan worden
 */
public class ExterneOrganisatieSelectiePanel extends
		AbstractZoekenPanel<ExterneOrganisatie, ExterneOrganisatieZoekFilter>
{
	private static final long serialVersionUID = 1L;

	/**
	 * @return Default zoekfilter voor de ingelogde gebruiker.
	 */
	private static final ExterneOrganisatieZoekFilter getDefaultFilter()
	{
		ExterneOrganisatieZoekFilter filter = new ExterneOrganisatieZoekFilter();
		filter.addOrderByProperty("naam");
		return filter;
	}

	public ExterneOrganisatieSelectiePanel(String id, CobraModalWindow<ExterneOrganisatie> window,
			ExterneOrganisatieZoekFilter filter)
	{
		super(id, window, filter == null ? getDefaultFilter() : filter,
			ExterneOrganisatieDataAccessHelper.class, new ExterneOrganisatieTable());
	}

	@Override
	protected Component createFilterPanel(String id, ExterneOrganisatieZoekFilter filter,
			CustomDataPanel<ExterneOrganisatie> datapanel)
	{
		return new ExterneOrganisatieZoekFilterPanel(id, filter, datapanel, true);
	}
}
