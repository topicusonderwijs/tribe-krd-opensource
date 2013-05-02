package nl.topicus.eduarte.web.components.modalwindow.gemeente;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.landelijk.Gemeente;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.GemeenteZoekFilter;

import org.apache.wicket.model.IModel;

public class GemeenteSelectieModalWindow extends AbstractZoekenModalWindow<Gemeente>
{
	private static final long serialVersionUID = 1L;

	private GemeenteZoekFilter filter;

	public GemeenteSelectieModalWindow(String id)
	{
		this(id, null, null);
	}

	public GemeenteSelectieModalWindow(String id, IModel<Gemeente> model)
	{
		this(id, model, null);
	}

	public GemeenteSelectieModalWindow(String id, IModel<Gemeente> model, GemeenteZoekFilter filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Gemeente selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<Gemeente> createContents(String id)
	{
		return new GemeenteSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
