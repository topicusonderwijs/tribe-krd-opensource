package nl.topicus.eduarte.web.components.modalwindow.taxonomie;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.TaxonomieElementDataAccessHelper;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.TaxonomieElementTable;
import nl.topicus.eduarte.web.components.panels.filter.TaxonomieElementZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.TaxonomieElementZoekFilter;

import org.apache.wicket.Component;

public class TaxonomieElementSelectiePanel extends
		AbstractZoekenPanel<TaxonomieElement, TaxonomieElementZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public TaxonomieElementSelectiePanel(String id, CobraModalWindow<TaxonomieElement> window,
			TaxonomieElementZoekFilter filter)
	{
		super(id, window, filter, TaxonomieElementDataAccessHelper.class,
			new TaxonomieElementTable("Taxonomieën"));
	}

	@Override
	protected Component createFilterPanel(String id, TaxonomieElementZoekFilter filter,
			CustomDataPanel<TaxonomieElement> datapanel)
	{
		return new TaxonomieElementZoekFilterPanel(id, filter, datapanel, true);
	}
}
