package nl.topicus.eduarte.krd.web.components.modalwindow.bron;

import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;

/**
 * Modal window voor het toevoegen van een nieuwe bookmark.
 * 
 * @author loite
 */
public class AanleverpuntLocatieToevoegenModalWindow extends CobraModalWindow<Void>
{
	private static final long serialVersionUID = 1L;

	public AanleverpuntLocatieToevoegenModalWindow(String id)
	{
		super(id);
		setTitle("Locatie toevoegen");
		setInitialHeight(200);
		setInitialWidth(450);
		setResizable(false);
	}

	@Override
	protected CobraModalWindowBasePanel<Void> createContents(String id)
	{
		return new AanleverpuntLocatieToevoegenPanel(id, this);
	}

}
