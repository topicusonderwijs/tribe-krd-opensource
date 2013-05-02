package nl.topicus.eduarte.web.components.modalwindow.verblijfsvergunning;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.landelijk.Verblijfsvergunning;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.apache.wicket.model.IModel;

public class VerblijfsvergunningSelectieModalWindow extends
		AbstractZoekenModalWindow<Verblijfsvergunning>
{
	private static final long serialVersionUID = 1L;

	private LandelijkCodeNaamZoekFilter<Verblijfsvergunning> filter;

	public VerblijfsvergunningSelectieModalWindow(String id, IModel<Verblijfsvergunning> model,
			LandelijkCodeNaamZoekFilter<Verblijfsvergunning> filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Verblijfsvergunning selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<Verblijfsvergunning> createContents(String id)
	{
		return new VerblijfsvergunningSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
