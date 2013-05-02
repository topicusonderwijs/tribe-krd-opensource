package nl.topicus.eduarte.krd.web.components.modalwindow.bron;

import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;

/**
 * Modal window voor het wijzigen van bronschooljaarstatussen
 * 
 * @author vandekamp
 */
public class WijzigStatusModalWindow extends CobraModalWindow<Void>
{
	private static final long serialVersionUID = 1L;

	public WijzigStatusModalWindow(String id)
	{
		super(id);
		setTitle("Schooljaar status wijzigen");
		setInitialHeight(200);
		setInitialWidth(400);
		setResizable(false);
	}

	@Override
	protected CobraModalWindowBasePanel<Void> createContents(String id)
	{
		return new WijzigStatusPanel(id, this);
	}
}
