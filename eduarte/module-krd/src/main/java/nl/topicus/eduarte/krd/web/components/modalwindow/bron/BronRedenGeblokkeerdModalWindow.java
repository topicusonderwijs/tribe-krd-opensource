package nl.topicus.eduarte.krd.web.components.modalwindow.bron;

import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;

import org.apache.wicket.model.IModel;

public class BronRedenGeblokkeerdModalWindow extends CobraModalWindow<IBronMelding>
{
	private static final long serialVersionUID = 1L;

	private boolean saved = false;

	public BronRedenGeblokkeerdModalWindow(String id, IModel<IBronMelding> meldingModel)
	{
		super(id, meldingModel);
		setTitle("Reden blokkering");
		setInitialHeight(150);
		setInitialWidth(500);
		setResizable(false);
	}

	@Override
	protected CobraModalWindowBasePanel<IBronMelding> createContents(String id)
	{
		return new BronRedenGeblokkeerdPanel(id, this, getModel());
	}

	public boolean isSaved()
	{
		return saved;
	}

	public void setSaved(boolean saved)
	{
		this.saved = saved;
	}
}
