package nl.topicus.eduarte.web.components.modalwindow.voorvoegsel;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.landelijk.Voorvoegsel;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.VoorvoegselZoekFilter;

import org.apache.wicket.model.IModel;

public class VoorvoegselSelectieModalWindow extends AbstractZoekenModalWindow<Voorvoegsel>
{
	private static final long serialVersionUID = 1L;

	private VoorvoegselZoekFilter filter;

	public VoorvoegselSelectieModalWindow(String id)
	{
		this(id, null, null);
	}

	public VoorvoegselSelectieModalWindow(String id, IModel<Voorvoegsel> model)
	{
		this(id, model, null);
	}

	public VoorvoegselSelectieModalWindow(String id, IModel<Voorvoegsel> model,
			VoorvoegselZoekFilter filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Voorvoegsel selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<Voorvoegsel> createContents(String id)
	{
		return new VoorvoegselSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
