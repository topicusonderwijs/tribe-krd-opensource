package nl.topicus.eduarte.web.components.modalwindow.nationaliteit;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.NationaliteitDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Nationaliteit;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CodeNaamTable;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.apache.wicket.Component;

public class NationaliteitSelectiePanel extends
		AbstractZoekenPanel<Nationaliteit, LandelijkCodeNaamZoekFilter<Nationaliteit>>
{
	private static final long serialVersionUID = 1L;

	private static final LandelijkCodeNaamZoekFilter<Nationaliteit> getDefaultFilter()
	{
		LandelijkCodeNaamZoekFilter<Nationaliteit> filter =
			LandelijkCodeNaamZoekFilter.of(Nationaliteit.class);
		filter.addOrderByProperty("code");
		filter.addOrderByProperty("naam");
		return filter;
	}

	public NationaliteitSelectiePanel(String id, CobraModalWindow<Nationaliteit> window,
			LandelijkCodeNaamZoekFilter<Nationaliteit> filter)
	{
		super(id, window, filter == null ? getDefaultFilter() : filter,
			NationaliteitDataAccessHelper.class,
			new CodeNaamTable<Nationaliteit>("Nationaliteiten"));
	}

	@Override
	protected Component createFilterPanel(String id,
			LandelijkCodeNaamZoekFilter<Nationaliteit> filter,
			CustomDataPanel<Nationaliteit> datapanel)
	{
		return new CodeNaamZoekFilterPanel(id, filter, datapanel);
	}
}
