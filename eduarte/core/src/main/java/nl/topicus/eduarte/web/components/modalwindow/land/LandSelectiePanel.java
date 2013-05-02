package nl.topicus.eduarte.web.components.modalwindow.land;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.LandDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CodeNaamTable;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.apache.wicket.Component;

public class LandSelectiePanel extends AbstractZoekenPanel<Land, LandelijkCodeNaamZoekFilter<Land>>
{
	private static final long serialVersionUID = 1L;

	private static final LandelijkCodeNaamZoekFilter<Land> getDefaultFilter()
	{
		LandelijkCodeNaamZoekFilter<Land> filter = LandelijkCodeNaamZoekFilter.of(Land.class);
		filter.addOrderByProperty("code");
		filter.addOrderByProperty("naam");
		return filter;
	}

	public LandSelectiePanel(String id, CobraModalWindow<Land> window,
			LandelijkCodeNaamZoekFilter<Land> filter)
	{
		super(id, window, filter == null ? getDefaultFilter() : filter, LandDataAccessHelper.class,
			new CodeNaamTable<Land>("Landen"));
	}

	@Override
	protected Component createFilterPanel(String id, LandelijkCodeNaamZoekFilter<Land> filter,
			CustomDataPanel<Land> datapanel)
	{
		return new CodeNaamZoekFilterPanel(id, filter, datapanel);
	}
}
