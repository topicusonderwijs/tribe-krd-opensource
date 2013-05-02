package nl.topicus.eduarte.web.components.modalwindow.locatie;

import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.LocatieZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * 
 * 
 * @author vanharen
 */
public class LocatieSelectieModalWindow extends AbstractZoekenModalWindow<Locatie>
{

	private static final long serialVersionUID = 1L;

	private LocatieZoekFilter filter;

	public LocatieSelectieModalWindow(String id, IModel<Locatie> model, LocatieZoekFilter filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Locatie selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<Locatie> createContents(String id)
	{
		return new LocatieSelectiePanel(id, this, filter);
	}
}
