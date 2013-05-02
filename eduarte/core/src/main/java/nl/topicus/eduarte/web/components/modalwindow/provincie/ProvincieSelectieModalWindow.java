package nl.topicus.eduarte.web.components.modalwindow.provincie;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.landelijk.Provincie;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.apache.wicket.model.IModel;

public class ProvincieSelectieModalWindow extends AbstractZoekenModalWindow<Provincie>
{
	private static final long serialVersionUID = 1L;

	private LandelijkCodeNaamZoekFilter<Provincie> filter;

	public ProvincieSelectieModalWindow(String id)
	{
		this(id, null, null);
	}

	public ProvincieSelectieModalWindow(String id, IModel<Provincie> model)
	{
		this(id, model, null);
	}

	public ProvincieSelectieModalWindow(String id, IModel<Provincie> model,
			LandelijkCodeNaamZoekFilter<Provincie> filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Provincie selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<Provincie> createContents(String id)
	{
		return new ProvincieSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
