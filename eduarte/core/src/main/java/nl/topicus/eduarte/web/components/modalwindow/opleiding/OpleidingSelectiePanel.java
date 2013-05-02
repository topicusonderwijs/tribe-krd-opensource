package nl.topicus.eduarte.web.components.modalwindow.opleiding;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.OpleidingDataAccessHelper;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OpleidingTable;
import nl.topicus.eduarte.web.components.panels.filter.OpleidingZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;

import org.apache.wicket.Component;

public class OpleidingSelectiePanel extends AbstractZoekenPanel<Opleiding, OpleidingZoekFilter>
{
	private static final long serialVersionUID = 1L;

	private boolean niveauZoekveld;

	public OpleidingSelectiePanel(String id, CobraModalWindow<Opleiding> window,
			OpleidingZoekFilter filter)
	{
		super(id, window, filter, OpleidingDataAccessHelper.class, new OpleidingTable());
	}

	public OpleidingSelectiePanel(String id, CobraModalWindow<Opleiding> window,
			OpleidingZoekFilter filter, boolean niveauZoekveld)
	{
		super(id, window, filter, OpleidingDataAccessHelper.class, new OpleidingTable());
		this.niveauZoekveld = niveauZoekveld;
	}

	@Override
	protected Component createFilterPanel(String id, OpleidingZoekFilter filter,
			CustomDataPanel<Opleiding> datapanel)
	{
		if (niveauZoekveld)
			return new OpleidingZoekFilterPanel(id, filter, datapanel, niveauZoekveld);
		else
			return new OpleidingZoekFilterPanel(id, filter, datapanel);
	}
}