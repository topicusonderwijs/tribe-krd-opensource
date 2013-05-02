package nl.topicus.eduarte.web.components.modalwindow.persoon;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.PersoonDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.PersoonTable;
import nl.topicus.eduarte.web.components.panels.filter.PersoonZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.PersoonZoekFilter;

import org.apache.wicket.Component;

public class PersoonSelectiePanel extends AbstractZoekenPanel<Persoon, PersoonZoekFilter<Persoon>>
{
	private static final long serialVersionUID = 1L;

	public PersoonSelectiePanel(String id, CobraModalWindow<Persoon> window,
			PersoonZoekFilter<Persoon> filter)
	{
		super(id, window, filter, PersoonDataAccessHelper.class, new PersoonTable());
	}

	@Override
	protected Component createFilterPanel(String id, PersoonZoekFilter<Persoon> filter,
			CustomDataPanel<Persoon> datapanel)
	{
		return new PersoonZoekFilterPanel(id, filter, datapanel);
	}
}
