package nl.topicus.eduarte.web.components.modalwindow.brin;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.BrinDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.BrinTable;
import nl.topicus.eduarte.web.components.panels.filter.BrinZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.BrinZoekFilter;

import org.apache.wicket.Component;

public class BrinSelectiePanel extends AbstractZoekenPanel<Brin, BrinZoekFilter>
{
	private static final long serialVersionUID = 1L;

	private static final BrinZoekFilter getDefaultFilter()
	{
		BrinZoekFilter filter = new BrinZoekFilter();
		filter.addOrderByProperty("code");
		return filter;
	}

	public BrinSelectiePanel(String id, CobraModalWindow<Brin> window, BrinZoekFilter filter)
	{
		super(id, window, filter == null ? getDefaultFilter() : filter, BrinDataAccessHelper.class,
			new BrinTable());
	}

	@Override
	protected Component createFilterPanel(String id, BrinZoekFilter filter,
			CustomDataPanel<Brin> datapanel)
	{
		return new BrinZoekFilterPanel(id, filter, datapanel);
	}
}
