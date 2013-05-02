package nl.topicus.eduarte.web.components.modalwindow.gemeente;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.GemeenteDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Gemeente;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.GemeenteTable;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.GemeenteZoekFilter;

import org.apache.wicket.Component;

public class GemeenteSelectiePanel extends AbstractZoekenPanel<Gemeente, GemeenteZoekFilter>
{
	private static final long serialVersionUID = 1L;

	private static final GemeenteZoekFilter getDefaultFilter()
	{
		GemeenteZoekFilter filter = new GemeenteZoekFilter();
		filter.addOrderByProperty("code");
		filter.addOrderByProperty("naam");
		return filter;
	}

	public GemeenteSelectiePanel(String id, CobraModalWindow<Gemeente> window,
			GemeenteZoekFilter filter)
	{
		super(id, window, filter == null ? getDefaultFilter() : filter,
			GemeenteDataAccessHelper.class, new GemeenteTable());
	}

	@Override
	protected Component createFilterPanel(String id, GemeenteZoekFilter filter,
			CustomDataPanel<Gemeente> datapanel)
	{
		return new CodeNaamZoekFilterPanel(id, filter, datapanel);
	}
}
