package nl.topicus.eduarte.web.components.modalwindow.bpvcriteria;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.bpv.BPVCriteriaDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVCriteria;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.criteria.BPVCriteriaZoekFilterPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.BPVCriteriaTable;
import nl.topicus.eduarte.zoekfilters.bpv.BPVCriteriaZoekFilter;

import org.apache.wicket.Component;

public class BPVCriteriaSelectiePanel extends
		AbstractZoekenPanel<BPVCriteria, BPVCriteriaZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public BPVCriteriaSelectiePanel(String id, CobraModalWindow<BPVCriteria> window,
			BPVCriteriaZoekFilter filter)
	{
		super(id, window, filter, BPVCriteriaDataAccessHelper.class, new BPVCriteriaTable());
	}

	@Override
	protected Component createFilterPanel(String id, BPVCriteriaZoekFilter filter,
			CustomDataPanel<BPVCriteria> datapanel)
	{
		return new BPVCriteriaZoekFilterPanel(id, filter, datapanel);
	}
}