package nl.topicus.eduarte.krd.web.pages.deelnemer.examens.individueel;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindowEmptyPanel;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.examen.ToegestaneExamenstatusOvergang;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Modal window voor het invoeren van de benodigde gegevens bij een examenstatusovergang
 * voor een individuele deelnemer.
 * 
 * @author loite
 * 
 */
public abstract class ExamenstatusOvergangModalWindow extends CobraModalWindow<Void>
{
	private static final long serialVersionUID = 1L;

	private final ModelManager modelManager;

	public ExamenstatusOvergangModalWindow(String id, ModelManager modelManager)
	{
		super(id);
		setInitialHeight(400);
		setInitialWidth(700);
		setResizable(false);
		this.modelManager = modelManager;
	}

	public void show(AjaxRequestTarget target, Examendeelname examendeelname)
	{
		getModalWindow()
			.setContent(createContents(getModalWindow().getContentId(), examendeelname));
		getModalWindow().show(target);
	}

	@Override
	protected CobraModalWindowBasePanel<Void> createContents(String id)
	{
		return createContents(id, null);
	}

	protected CobraModalWindowBasePanel<Void> createContents(String id,
			Examendeelname examendeelname)
	{
		ToegestaneExamenstatusOvergang overgang =
			(ToegestaneExamenstatusOvergang) this.getDefaultModelObject();
		if (overgang == null || examendeelname == null)
		{
			return new CobraModalWindowEmptyPanel<Void>(id, this);
		}
		setTitle(overgang.getActieIndividueel());
		if (overgang.isTijdvakAangeven())
		{
			this.setInitialHeight(600);
		}
		else
		{
			this.setInitialHeight(400);
		}
		return new ExamenstatusOvergangBasePanel(id, this, examendeelname, overgang, modelManager);
	}

}
