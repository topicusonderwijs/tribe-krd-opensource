package nl.topicus.eduarte.web.components.modalwindow.onderwijsproduct;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;

import org.apache.wicket.model.IModel;

public class OnderwijsproductSelectieModalWindow extends
		AbstractZoekenModalWindow<Onderwijsproduct>
{
	private static final long serialVersionUID = 1L;

	private OnderwijsproductZoekFilter filter;

	public OnderwijsproductSelectieModalWindow(String id, IModel<Onderwijsproduct> model,
			OnderwijsproductZoekFilter filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Onderwijsproduct selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<Onderwijsproduct> createContents(String id)
	{
		return new OnderwijsproductSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
