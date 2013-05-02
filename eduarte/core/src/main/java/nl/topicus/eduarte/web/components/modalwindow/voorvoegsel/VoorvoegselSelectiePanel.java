package nl.topicus.eduarte.web.components.modalwindow.voorvoegsel;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.VoorvoegselDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Voorvoegsel;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.NaamTable;
import nl.topicus.eduarte.web.components.panels.filter.NaamZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.VoorvoegselZoekFilter;

import org.apache.wicket.Component;

public class VoorvoegselSelectiePanel extends
		AbstractZoekenPanel<Voorvoegsel, VoorvoegselZoekFilter>
{
	private static final long serialVersionUID = 1L;

	private static final VoorvoegselZoekFilter getDefaultFilter()
	{
		VoorvoegselZoekFilter filter = new VoorvoegselZoekFilter();
		filter.addOrderByProperty("naam");
		return filter;
	}

	public VoorvoegselSelectiePanel(String id, CobraModalWindow<Voorvoegsel> window,
			VoorvoegselZoekFilter filter)
	{
		super(id, window, filter == null ? getDefaultFilter() : filter,
			VoorvoegselDataAccessHelper.class, new NaamTable<Voorvoegsel>("Voorvoegsels"));
	}

	@Override
	protected Component createFilterPanel(String id, VoorvoegselZoekFilter filter,
			CustomDataPanel<Voorvoegsel> datapanel)
	{
		return new NaamZoekFilterPanel(id, filter, datapanel);
	}
}
