package nl.topicus.eduarte.web.components.modalwindow.provincie;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.ProvincieDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Provincie;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CodeNaamTable;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.apache.wicket.Component;

public class ProvincieSelectiePanel extends
		AbstractZoekenPanel<Provincie, LandelijkCodeNaamZoekFilter<Provincie>>
{
	private static final long serialVersionUID = 1L;

	private static final LandelijkCodeNaamZoekFilter<Provincie> getDefaultFilter()
	{
		LandelijkCodeNaamZoekFilter<Provincie> filter =
			LandelijkCodeNaamZoekFilter.of(Provincie.class);
		filter.addOrderByProperty("code");
		filter.addOrderByProperty("naam");
		return filter;
	}

	public ProvincieSelectiePanel(String id, CobraModalWindow<Provincie> window,
			LandelijkCodeNaamZoekFilter<Provincie> filter)
	{
		super(id, window, filter == null ? getDefaultFilter() : filter,
			ProvincieDataAccessHelper.class, new CodeNaamTable<Provincie>("Provincies"));
	}

	@Override
	protected Component createFilterPanel(String id, LandelijkCodeNaamZoekFilter<Provincie> filter,
			CustomDataPanel<Provincie> datapanel)
	{
		return new CodeNaamZoekFilterPanel(id, filter, datapanel);
	}
}
