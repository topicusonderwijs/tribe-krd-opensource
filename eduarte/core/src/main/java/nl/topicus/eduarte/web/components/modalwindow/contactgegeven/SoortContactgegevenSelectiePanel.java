package nl.topicus.eduarte.web.components.modalwindow.contactgegeven;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.SoortContactgegevenDataAccessHelper;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.CodeNaamActiefTable;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamActiefZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.SoortContactgegevenZoekFilter;

import org.apache.wicket.Component;

public class SoortContactgegevenSelectiePanel extends
		AbstractZoekenPanel<SoortContactgegeven, SoortContactgegevenZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public SoortContactgegevenSelectiePanel(String id,
			CobraModalWindow<SoortContactgegeven> window, SoortContactgegevenZoekFilter filter)
	{

		super(id, window, filter, SoortContactgegevenDataAccessHelper.class,
			new CodeNaamActiefTable<SoortContactgegeven>("Soort contactgegevens"));
	}

	@Override
	protected Component createFilterPanel(String id, SoortContactgegevenZoekFilter filter,
			CustomDataPanel<SoortContactgegeven> datapanel)
	{
		return new CodeNaamActiefZoekFilterPanel(id, filter, datapanel);
	}
}
