package nl.topicus.eduarte.web.components.modalwindow.locatie;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.LocatieDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.LocatieTable;
import nl.topicus.eduarte.web.components.panels.filter.LocatieZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.LocatieZoekFilter;

import org.apache.wicket.Component;

/**
 * 
 * 
 * @author vanharen
 */
public class LocatieSelectiePanel extends AbstractZoekenPanel<Locatie, LocatieZoekFilter>
{

	private static final long serialVersionUID = 1L;

	public LocatieSelectiePanel(String id, CobraModalWindow<Locatie> window,
			LocatieZoekFilter filter)
	{

		super(id, window, filter, LocatieDataAccessHelper.class, new LocatieTable());
	}

	@Override
	protected Component createFilterPanel(String id, LocatieZoekFilter filter,
			CustomDataPanel<Locatie> datapanel)
	{
		return new LocatieZoekFilterPanel(id, filter, datapanel);
	}

}
