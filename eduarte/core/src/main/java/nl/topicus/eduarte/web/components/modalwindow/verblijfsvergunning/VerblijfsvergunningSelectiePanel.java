package nl.topicus.eduarte.web.components.modalwindow.verblijfsvergunning;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.VerblijfsvergunningDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Verblijfsvergunning;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.VerblijfsvergunningTable;
import nl.topicus.eduarte.web.components.panels.filter.VerblijfsvergunningZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.apache.wicket.Component;

public class VerblijfsvergunningSelectiePanel extends
		AbstractZoekenPanel<Verblijfsvergunning, LandelijkCodeNaamZoekFilter<Verblijfsvergunning>>
{
	private static final long serialVersionUID = 1L;

	public VerblijfsvergunningSelectiePanel(String id,
			CobraModalWindow<Verblijfsvergunning> window,
			LandelijkCodeNaamZoekFilter<Verblijfsvergunning> filter)
	{
		super(id, window, filter, VerblijfsvergunningDataAccessHelper.class,
			new VerblijfsvergunningTable());
	}

	@Override
	protected Component createFilterPanel(String id,
			LandelijkCodeNaamZoekFilter<Verblijfsvergunning> filter,
			CustomDataPanel<Verblijfsvergunning> datapanel)
	{
		return new VerblijfsvergunningZoekFilterPanel(id, filter, datapanel);
	}
}
