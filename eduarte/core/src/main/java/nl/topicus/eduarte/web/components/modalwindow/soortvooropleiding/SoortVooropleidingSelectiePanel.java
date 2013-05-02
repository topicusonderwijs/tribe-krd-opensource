package nl.topicus.eduarte.web.components.modalwindow.soortvooropleiding;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.SoortVooropleidingDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.SoortVooropleidingTable;
import nl.topicus.eduarte.web.components.panels.filter.SoortVooropleidingZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.SoortVooropleidingZoekFilter;

import org.apache.wicket.Component;

public class SoortVooropleidingSelectiePanel extends
		AbstractZoekenPanel<SoortVooropleiding, SoortVooropleidingZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public SoortVooropleidingSelectiePanel(String id, CobraModalWindow<SoortVooropleiding> window,
			SoortVooropleidingZoekFilter filter)
	{
		super(id, window, filter == null ? new SoortVooropleidingZoekFilter() : filter,
			SoortVooropleidingDataAccessHelper.class, new SoortVooropleidingTable());
	}

	@Override
	protected Component createFilterPanel(String id, SoortVooropleidingZoekFilter filter,
			CustomDataPanel<SoortVooropleiding> datapanel)
	{
		return new SoortVooropleidingZoekFilterPanel(id, filter, datapanel, false);
	}
}
