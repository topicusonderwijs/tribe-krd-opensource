package nl.topicus.eduarte.krd.web.components.modalwindow.bron;

import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;

import org.apache.wicket.model.IModel;

/**
 * Modal window voor het toevoegen van een nieuwe bookmark.
 * 
 * @author loite
 */
public class NieuwAanleverpuntModalWindow extends CobraModalWindow<BronAanleverpunt>
{
	private static final long serialVersionUID = 1L;

	public NieuwAanleverpuntModalWindow(String id, IModel<BronAanleverpunt> aanleverpuntModel)
	{
		super(id, aanleverpuntModel);
		setTitle("Nieuw aanleverpunt aanmaken");
		setInitialHeight(200);
		setInitialWidth(400);
		setResizable(false);
	}

	@Override
	protected CobraModalWindowBasePanel<BronAanleverpunt> createContents(String id)
	{
		return new NieuwAanleverpuntPanel(id, this, getModel());
	}
}
