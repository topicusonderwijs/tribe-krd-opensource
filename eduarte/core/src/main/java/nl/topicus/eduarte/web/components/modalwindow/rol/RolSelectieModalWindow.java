package nl.topicus.eduarte.web.components.modalwindow.rol;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.RolZoekFilter;

import org.apache.wicket.model.IModel;

public class RolSelectieModalWindow extends AbstractZoekenModalWindow<Rol>
{
	private static final long serialVersionUID = 1L;

	private RolZoekFilter filter;

	public RolSelectieModalWindow(String id)
	{
		this(id, null, null);
	}

	public RolSelectieModalWindow(String id, IModel<Rol> model)
	{
		this(id, model, null);
	}

	public RolSelectieModalWindow(String id, IModel<Rol> model, RolZoekFilter filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Rol selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<Rol> createContents(String id)
	{
		return new RolSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
