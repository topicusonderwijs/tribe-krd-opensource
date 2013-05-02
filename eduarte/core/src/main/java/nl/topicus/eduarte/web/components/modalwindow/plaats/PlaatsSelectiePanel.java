package nl.topicus.eduarte.web.components.modalwindow.plaats;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.PlaatsDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Plaats;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.PlaatsTable;
import nl.topicus.eduarte.web.components.panels.filter.NaamZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.PlaatsZoekFilter;

import org.apache.wicket.Component;

public class PlaatsSelectiePanel extends AbstractZoekenPanel<Plaats, PlaatsZoekFilter>
{
	private static final long serialVersionUID = 1L;

	private static final PlaatsZoekFilter getDefaultFilter()
	{
		PlaatsZoekFilter filter = new PlaatsZoekFilter();
		filter.addOrderByProperty("naam");
		return filter;
	}

	public PlaatsSelectiePanel(String id, CobraModalWindow<Plaats> window, PlaatsZoekFilter filter)
	{
		super(id, window, filter == null ? getDefaultFilter() : filter,
			PlaatsDataAccessHelper.class, new PlaatsTable());
	}

	@Override
	protected Component createFilterPanel(String id, PlaatsZoekFilter filter,
			CustomDataPanel<Plaats> datapanel)
	{
		return new NaamZoekFilterPanel(id, filter, datapanel);
	}
}
