package nl.topicus.eduarte.web.components.modalwindow.medewerker;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.MedewerkerDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.MedewerkerTable;
import nl.topicus.eduarte.web.components.panels.filter.MedewerkerZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.MedewerkerZoekFilter;

import org.apache.wicket.Component;

public class MedewerkerSelectiePanel extends AbstractZoekenPanel<Medewerker, MedewerkerZoekFilter>
{
	private static final long serialVersionUID = 1L;

	private static final MedewerkerZoekFilter getDefaultFilter()
	{
		MedewerkerZoekFilter filter = new MedewerkerZoekFilter();
		filter.addOrderByProperty("persoon.achternaam");
		return filter;
	}

	private boolean lockHeeftAccount;

	public MedewerkerSelectiePanel(String id, CobraModalWindow<Medewerker> window,
			MedewerkerZoekFilter filter, boolean lockHeeftAccount)
	{
		super(id, window, filter == null ? getDefaultFilter() : filter,
			MedewerkerDataAccessHelper.class, new MedewerkerTable());
		this.lockHeeftAccount = lockHeeftAccount;
	}

	@Override
	protected Component createFilterPanel(String id, MedewerkerZoekFilter filter,
			CustomDataPanel<Medewerker> datapanel)
	{
		return new MedewerkerZoekFilterPanel(id, filter, datapanel, !lockHeeftAccount);
	}
}
