package nl.topicus.eduarte.web.components.modalwindow.land;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.apache.wicket.model.IModel;

public class LandSelectieModalWindow extends AbstractZoekenModalWindow<Land>
{
	private static final long serialVersionUID = 1L;

	private LandelijkCodeNaamZoekFilter<Land> filter;

	public LandSelectieModalWindow(String id)
	{
		this(id, null, null);
	}

	public LandSelectieModalWindow(String id, IModel<Land> model)
	{
		this(id, model, null);
	}

	public LandSelectieModalWindow(String id, IModel<Land> model,
			LandelijkCodeNaamZoekFilter<Land> filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Land selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<Land> createContents(String id)
	{
		return new LandSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
