package nl.topicus.eduarte.web.components.modalwindow.deelnemermedewerkergroep;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.participatie.helpers.DeelnemerMedewerkerGroepDataAccesHelper;
import nl.topicus.eduarte.entities.participatie.DeelnemerMedewerkerGroep;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DeelnemerMedewerkerGroepTable;
import nl.topicus.eduarte.web.components.panels.filter.DeelnemerMedewerkerGroepZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.DeelnemerMedewerkerGroepZoekFilter;

import org.apache.wicket.Component;

public class DeelnemerMedewerkerGroepSelectiePanel extends
		AbstractZoekenPanel<DeelnemerMedewerkerGroep, DeelnemerMedewerkerGroepZoekFilter>
{
	private static final long serialVersionUID = 1L;

	private static final DeelnemerMedewerkerGroepZoekFilter getDefaultFilter()
	{
		DeelnemerMedewerkerGroepZoekFilter filter = new DeelnemerMedewerkerGroepZoekFilter();
		filter.addOrderByProperty("omschrijving");
		return filter;
	}

	public DeelnemerMedewerkerGroepSelectiePanel(String id,
			CobraModalWindow<DeelnemerMedewerkerGroep> window,
			DeelnemerMedewerkerGroepZoekFilter filter)
	{
		super(id, window, filter == null ? getDefaultFilter() : filter,
			DeelnemerMedewerkerGroepDataAccesHelper.class, new DeelnemerMedewerkerGroepTable());
	}

	@Override
	protected Component createFilterPanel(String id, DeelnemerMedewerkerGroepZoekFilter filter,
			CustomDataPanel<DeelnemerMedewerkerGroep> datapanel)
	{
		return new DeelnemerMedewerkerGroepZoekFilterPanel(id, filter, datapanel);
	}

}
