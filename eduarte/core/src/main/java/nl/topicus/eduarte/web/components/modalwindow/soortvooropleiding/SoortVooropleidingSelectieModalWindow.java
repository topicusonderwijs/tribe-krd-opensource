package nl.topicus.eduarte.web.components.modalwindow.soortvooropleiding;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.SoortVooropleidingZoekFilter;

import org.apache.wicket.model.IModel;

public class SoortVooropleidingSelectieModalWindow extends
		AbstractZoekenModalWindow<SoortVooropleiding>
{
	private static final long serialVersionUID = 1L;

	private SoortVooropleidingZoekFilter filter;

	public SoortVooropleidingSelectieModalWindow(String id)
	{
		this(id, null, null);
	}

	public SoortVooropleidingSelectieModalWindow(String id, IModel<SoortVooropleiding> model)
	{
		this(id, model, null);
	}

	public SoortVooropleidingSelectieModalWindow(String id, IModel<SoortVooropleiding> model,
			SoortVooropleidingZoekFilter filter)
	{
		super(id, model, filter);
		this.filter = filter;
		filter.addOrderByProperty("naam");
		setTitle("Soort vooropleiding selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<SoortVooropleiding> createContents(String id)
	{
		return new SoortVooropleidingSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
