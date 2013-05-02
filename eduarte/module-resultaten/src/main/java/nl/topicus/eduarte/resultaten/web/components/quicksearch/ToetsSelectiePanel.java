package nl.topicus.eduarte.resultaten.web.components.quicksearch;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.resultaten.web.components.filter.ToetsSelectieZoekFilterPanel;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ToetsTable;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.Component;

public class ToetsSelectiePanel extends AbstractZoekenPanel<Toets, ToetsZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public ToetsSelectiePanel(String id, CobraModalWindow<Toets> window, ToetsZoekFilter filter)
	{
		super(id, window, filter, ToetsDataAccessHelper.class, new ToetsTable(null, false, true));
	}

	@Override
	protected Component createFilterPanel(String id, ToetsZoekFilter filter,
			CustomDataPanel<Toets> datapanel)
	{
		return new ToetsSelectieZoekFilterPanel(id, filter, datapanel);
	}
}
