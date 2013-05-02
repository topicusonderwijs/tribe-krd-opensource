package nl.topicus.eduarte.web.components.modalwindow.bpvcriteria;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.bpv.BPVCriteria;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.bpv.BPVCriteriaZoekFilter;

import org.apache.wicket.model.IModel;

public class BPVCriteriaSelectieModalWindow extends AbstractZoekenModalWindow<BPVCriteria>
{
	private static final long serialVersionUID = 1L;

	private BPVCriteriaZoekFilter filter;

	public BPVCriteriaSelectieModalWindow(String id, IModel<BPVCriteria> model,
			BPVCriteriaZoekFilter filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("BPV-Criteria selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<BPVCriteria> createContents(String id)
	{
		return new BPVCriteriaSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}