package nl.topicus.eduarte.krd.web.components.modalwindow.bron;

import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.BronBveTerugkoppelRecord;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.BronVoSignaal;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.IBronSignaal;

/**
 * Modal window voor het toevoegen van een opmerking aan een signaal
 * 
 * @author vandekamp
 */
public class BronSignaalOpmerkingModalWindow extends CobraModalWindow<IBronSignaal>
{
	private static final long serialVersionUID = 1L;

	public BronSignaalOpmerkingModalWindow(String id)
	{
		super(id, ModelFactory.getCompoundModel((IBronSignaal) null, new DefaultModelManager(
			BronBveTerugkoppelRecord.class, BronVoSignaal.class)));
		setTitle("Opmerkingen bij signaal");
		setInitialHeight(150);
		setInitialWidth(500);
		setResizable(false);
	}

	public void setSignaal(IBronSignaal signaal)
	{
		setModelObject(signaal);
	}

	@Override
	protected CobraModalWindowBasePanel<IBronSignaal> createContents(String id)
	{
		return new BronSignaalOpmerkingPanel(id, this, getModel());
	}
}
