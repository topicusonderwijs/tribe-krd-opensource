package nl.topicus.eduarte.web.components.modalwindow.deelnemer;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DeelnemerTable;
import nl.topicus.eduarte.web.components.panels.filter.DeelnemerZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.Component;

/**
 * @author Henzen
 */
public class DeelnemerSelectiePanel extends AbstractZoekenPanel<Verbintenis, VerbintenisZoekFilter>
{
	private static final long serialVersionUID = 1L;

	private static final VerbintenisZoekFilter getDefaultFilter()
	{
		VerbintenisZoekFilter filter = new VerbintenisZoekFilter();
		filter.addOrderByProperty("persoon.achternaam");
		return filter;
	}

	public DeelnemerSelectiePanel(String id, CobraModalWindow<Verbintenis> window,
			VerbintenisZoekFilter filter)
	{
		super(id, window, filter == null ? getDefaultFilter() : filter,
			VerbintenisDataAccessHelper.class, new DeelnemerTable());
	}

	@Override
	protected Component createFilterPanel(String id, VerbintenisZoekFilter filter,
			CustomDataPanel<Verbintenis> datapanel)
	{
		return new DeelnemerZoekFilterPanel(id, filter, datapanel);
	}
}
