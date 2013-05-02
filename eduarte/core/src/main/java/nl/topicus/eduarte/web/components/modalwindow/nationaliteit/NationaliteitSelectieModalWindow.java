package nl.topicus.eduarte.web.components.modalwindow.nationaliteit;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.landelijk.Nationaliteit;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.apache.wicket.model.IModel;

public class NationaliteitSelectieModalWindow extends AbstractZoekenModalWindow<Nationaliteit>
{
	private static final long serialVersionUID = 1L;

	private LandelijkCodeNaamZoekFilter<Nationaliteit> filter;

	public NationaliteitSelectieModalWindow(String id)
	{
		this(id, null, null);
	}

	public NationaliteitSelectieModalWindow(String id, IModel<Nationaliteit> model)
	{
		this(id, model, null);
	}

	public NationaliteitSelectieModalWindow(String id, IModel<Nationaliteit> model,
			LandelijkCodeNaamZoekFilter<Nationaliteit> filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Nationaliteit selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<Nationaliteit> createContents(String id)
	{
		return new NationaliteitSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
