package nl.topicus.eduarte.web.components.modalwindow.rol;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.RolDataAccessHelper;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.RolTable;
import nl.topicus.eduarte.web.components.panels.filter.RolZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.RolZoekFilter;

import org.apache.wicket.Component;

public class RolSelectiePanel extends AbstractZoekenPanel<Rol, RolZoekFilter>
{
	private static final long serialVersionUID = 1L;

	private static final RolZoekFilter getDefaultFilter()
	{
		RolZoekFilter filter = new RolZoekFilter();
		filter.addOrderByProperty("naam");
		return filter;
	}

	public RolSelectiePanel(String id, CobraModalWindow<Rol> window, RolZoekFilter filter)
	{
		super(id, window, filter == null ? getDefaultFilter() : filter, RolDataAccessHelper.class,
			new RolTable());
	}

	@Override
	protected Component createFilterPanel(String id, RolZoekFilter filter,
			CustomDataPanel<Rol> datapanel)
	{
		return new RolZoekFilterPanel(id, filter, datapanel);
	}
}
