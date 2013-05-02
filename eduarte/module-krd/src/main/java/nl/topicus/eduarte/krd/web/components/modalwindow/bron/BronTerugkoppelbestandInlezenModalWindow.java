package nl.topicus.eduarte.krd.web.components.modalwindow.bron;

import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;

public class BronTerugkoppelbestandInlezenModalWindow extends CobraModalWindow<Void>
{
	private static final long serialVersionUID = 1L;

	public BronTerugkoppelbestandInlezenModalWindow(String id)
	{
		super(id);
		setTitle("Terugkoppelingbestand inlezen");
		setInitialHeight(200);
		setInitialWidth(450);
		setResizable(false);
	}

	@Override
	protected CobraModalWindowBasePanel<Void> createContents(String id)
	{
		return new BronTerugkoppelbestandInlezenPanel(id, this);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
	}
}
