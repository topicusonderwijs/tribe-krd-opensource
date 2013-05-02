package nl.topicus.eduarte.resultaten.web.components.quicksearch;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.model.IModel;

public class ToetsSelectieModalWindow extends AbstractZoekenModalWindow<Toets>
{
	private static final long serialVersionUID = 1L;

	private ToetsZoekFilter filter;

	public ToetsSelectieModalWindow(String id, IModel<Toets> model, ToetsZoekFilter filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Toets selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<Toets> createContents(String id)
	{
		return new ToetsSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
