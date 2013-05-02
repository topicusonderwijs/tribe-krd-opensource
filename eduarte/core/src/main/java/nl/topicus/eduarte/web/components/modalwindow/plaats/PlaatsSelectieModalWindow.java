package nl.topicus.eduarte.web.components.modalwindow.plaats;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.landelijk.Plaats;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.PlaatsZoekFilter;

import org.apache.wicket.model.IModel;

public class PlaatsSelectieModalWindow extends AbstractZoekenModalWindow<Plaats>
{
	private static final long serialVersionUID = 1L;

	private PlaatsZoekFilter filter;

	public PlaatsSelectieModalWindow(String id)
	{
		this(id, null, null);
	}

	public PlaatsSelectieModalWindow(String id, IModel<Plaats> model)
	{
		this(id, model, null);
	}

	public PlaatsSelectieModalWindow(String id, IModel<Plaats> model, PlaatsZoekFilter filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Plaats selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<Plaats> createContents(String id)
	{
		return new PlaatsSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
