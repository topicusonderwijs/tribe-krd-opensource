package nl.topicus.eduarte.web.components.modalwindow.externeorganisatie;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.BPVBedrijfsgegevenDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.BPVBedrijfsgegevenTable;
import nl.topicus.eduarte.web.components.panels.filter.BPVBedrijfsgegevenZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.BPVBedrijfsgegevenZoekFilter;

import org.apache.wicket.Component;

public class BPVBedrijfsgegevenSelectiePanel extends
		AbstractZoekenPanel<BPVBedrijfsgegeven, BPVBedrijfsgegevenZoekFilter>
{
	private static final long serialVersionUID = 1L;

	/**
	 * @return Default zoekfilter voor de ingelogde gebruiker.
	 */
	private static final BPVBedrijfsgegevenZoekFilter getDefaultFilter()
	{
		BPVBedrijfsgegevenZoekFilter filter = new BPVBedrijfsgegevenZoekFilter();
		filter.addOrderByProperty("externeOrganisatie.naam");
		return filter;
	}

	public BPVBedrijfsgegevenSelectiePanel(String id, CobraModalWindow<BPVBedrijfsgegeven> window,
			BPVBedrijfsgegevenZoekFilter filter)
	{
		super(id, window, filter == null ? getDefaultFilter() : filter,
			BPVBedrijfsgegevenDataAccessHelper.class, new BPVBedrijfsgegevenTable(true));
	}

	@Override
	protected Component createFilterPanel(String id, BPVBedrijfsgegevenZoekFilter filter,
			CustomDataPanel<BPVBedrijfsgegeven> datapanel)
	{
		return new BPVBedrijfsgegevenZoekFilterPanel(id, filter, datapanel);
	}
}
