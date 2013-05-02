package nl.topicus.eduarte.web.components.modalwindow.opleiding;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;

import org.apache.wicket.model.IModel;

public class OpleidingSelectieModalWindow extends AbstractZoekenModalWindow<Opleiding>
{
	private static final long serialVersionUID = 1L;

	private OpleidingZoekFilter filter;

	private boolean niveauZoekveld;

	public OpleidingSelectieModalWindow(String id, IModel<Opleiding> model,
			OpleidingZoekFilter filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Opleiding selecteren");
	}

	public OpleidingSelectieModalWindow(String id, IModel<Opleiding> model,
			OpleidingZoekFilter filter, boolean niveauZoekveld)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Opleiding selecteren");
		this.niveauZoekveld = niveauZoekveld;
	}

	@Override
	protected CobraModalWindowBasePanel<Opleiding> createContents(String id)
	{
		if (niveauZoekveld)
			return new OpleidingSelectiePanel(id, this, filter, niveauZoekveld);
		else
			return new OpleidingSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
