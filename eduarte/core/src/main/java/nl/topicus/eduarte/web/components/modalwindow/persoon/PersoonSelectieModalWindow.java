package nl.topicus.eduarte.web.components.modalwindow.persoon;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.PersoonZoekFilter;

import org.apache.wicket.model.IModel;

public class PersoonSelectieModalWindow extends AbstractZoekenModalWindow<Persoon>
{
	private static final long serialVersionUID = 1L;

	private PersoonZoekFilter<Persoon> filter;

	public PersoonSelectieModalWindow(String id, IModel<Persoon> model,
			PersoonZoekFilter<Persoon> filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Persoon selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<Persoon> createContents(String id)
	{
		return new PersoonSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
