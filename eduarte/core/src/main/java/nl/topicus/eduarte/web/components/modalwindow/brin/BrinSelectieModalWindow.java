package nl.topicus.eduarte.web.components.modalwindow.brin;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.BrinZoekFilter;

import org.apache.wicket.model.IModel;

public class BrinSelectieModalWindow extends AbstractZoekenModalWindow<Brin>
{
	private static final long serialVersionUID = 1L;

	private BrinZoekFilter filter;

	public BrinSelectieModalWindow(String id)
	{
		this(id, null, null);
	}

	public BrinSelectieModalWindow(String id, IModel<Brin> model)
	{
		this(id, model, null);
	}

	public BrinSelectieModalWindow(String id, IModel<Brin> model, BrinZoekFilter filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Brin selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<Brin> createContents(String id)
	{
		return new BrinSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
