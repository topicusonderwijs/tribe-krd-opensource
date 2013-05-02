package nl.topicus.eduarte.web.components.modalwindow.externeorganisatie;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.BPVBedrijfsgegevenZoekFilter;

import org.apache.wicket.model.IModel;

public class BPVBedrijfsgegevenSelectieModalWindow extends
		AbstractZoekenModalWindow<BPVBedrijfsgegeven>
{
	private static final long serialVersionUID = 1L;

	private BPVBedrijfsgegevenZoekFilter filter;

	public BPVBedrijfsgegevenSelectieModalWindow(String id)
	{
		this(id, null, null);
	}

	public BPVBedrijfsgegevenSelectieModalWindow(String id, IModel<BPVBedrijfsgegeven> model)
	{
		this(id, model, null);
	}

	public BPVBedrijfsgegevenSelectieModalWindow(String id, IModel<BPVBedrijfsgegeven> model,
			BPVBedrijfsgegevenZoekFilter filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Externe organisatie selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<BPVBedrijfsgegeven> createContents(String id)
	{
		return new BPVBedrijfsgegevenSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
