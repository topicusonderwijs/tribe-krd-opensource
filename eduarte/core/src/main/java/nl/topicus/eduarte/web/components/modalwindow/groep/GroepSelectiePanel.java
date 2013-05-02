package nl.topicus.eduarte.web.components.modalwindow.groep;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.GroepDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.GroepTable;
import nl.topicus.eduarte.web.components.panels.filter.GroepZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;

import org.apache.wicket.Component;

public class GroepSelectiePanel extends AbstractZoekenPanel<Groep, GroepZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public GroepSelectiePanel(String id, CobraModalWindow<Groep> window, GroepZoekFilter filter)
	{
		super(id, window, filter, GroepDataAccessHelper.class, new GroepTable());
	}

	@Override
	protected Component createFilterPanel(String id, GroepZoekFilter filter,
			CustomDataPanel<Groep> datapanel)
	{
		return new GroepZoekFilterPanel(id, filter, datapanel);
	}
}
