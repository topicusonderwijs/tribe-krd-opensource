package nl.topicus.eduarte.web.components.modalwindow.externeorganisatie.contactpersoon;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.ExterneOrganisatieContactPersoonDataAccessHelper;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ExterneOrganisatieContactPersoonTable;
import nl.topicus.eduarte.web.components.panels.filter.NaamZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieContactPersoonZoekFilter;

import org.apache.wicket.Component;

public class ExterneOrganisatieContactPersoonSelectiePanel
		extends
		AbstractZoekenPanel<ExterneOrganisatieContactPersoon, ExterneOrganisatieContactPersoonZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public ExterneOrganisatieContactPersoonSelectiePanel(String id,
			CobraModalWindow<ExterneOrganisatieContactPersoon> window,
			ExterneOrganisatieContactPersoonZoekFilter filter)
	{
		super(id, window, filter, ExterneOrganisatieContactPersoonDataAccessHelper.class,
			new ExterneOrganisatieContactPersoonTable());
	}

	@Override
	protected Component createFilterPanel(String id,
			ExterneOrganisatieContactPersoonZoekFilter filter,
			CustomDataPanel<ExterneOrganisatieContactPersoon> datapanel)
	{
		return new NaamZoekFilterPanel(id, filter, datapanel);
	}
}
