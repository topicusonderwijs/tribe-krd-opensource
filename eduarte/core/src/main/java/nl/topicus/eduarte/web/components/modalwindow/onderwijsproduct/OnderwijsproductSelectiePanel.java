package nl.topicus.eduarte.web.components.modalwindow.onderwijsproduct;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OnderwijsproductTable;
import nl.topicus.eduarte.web.components.panels.filter.OnderwijsproductZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;

import org.apache.wicket.Component;

public class OnderwijsproductSelectiePanel extends
		AbstractZoekenPanel<Onderwijsproduct, OnderwijsproductZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public OnderwijsproductSelectiePanel(String id, CobraModalWindow<Onderwijsproduct> window,
			OnderwijsproductZoekFilter filter)
	{
		super(id, window, filter, OnderwijsproductDataAccessHelper.class,
			new OnderwijsproductTable(true));
	}

	@Override
	protected Component createFilterPanel(String id, OnderwijsproductZoekFilter filter,
			CustomDataPanel<Onderwijsproduct> datapanel)
	{
		return new OnderwijsproductZoekFilterPanel(id, filter, datapanel);
	}
}
