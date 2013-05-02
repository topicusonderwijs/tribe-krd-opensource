package nl.topicus.eduarte.krd.web.pages.intake;

import java.io.Serializable;

import nl.topicus.cobra.modelsv2.ExtendedModel;
import nl.topicus.cobra.modelsv2.ObjectState;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;

import org.apache.wicket.model.IDetachable;

public class VerbintenisUndo implements IDetachable
{
	private static final long serialVersionUID = 1L;

	private ObjectState verbintenisBackup;

	private Serializable verbintenisId;

	public VerbintenisUndo(Verbintenis nieuweVerbintenis)
	{
		verbintenisId = nieuweVerbintenis.getCurrentId();
	}

	public VerbintenisUndo(IntakeWizardModel model, VerbintenisKeuze keuze)
	{
		Verbintenis verbintenis = keuze.getVerbintenis(model);
		ExtendedModel<Verbintenis> verbintenisModel =
			(ExtendedModel<Verbintenis>) model.getManager().getModel(verbintenis, null);
		this.verbintenisId = verbintenis.getCurrentId();
		this.verbintenisBackup = verbintenisModel.getState();
	}

	public void undo(IntakeWizardModel model)
	{
		Verbintenis verbintenis = null;
		for (Verbintenis curVerbintenis : model.getDeelnemer().getVerbintenissen())
		{
			if (curVerbintenis.getCurrentId().equals(verbintenisId))
			{
				verbintenis = curVerbintenis;
				break;
			}
		}
		if (verbintenisBackup == null)
		{
			model.getDeelnemer().getVerbintenissen().remove(verbintenis);
		}
		else
		{
			ExtendedModel<Verbintenis> verbintenisModel =
				(ExtendedModel<Verbintenis>) model.getManager().getModel(verbintenis, null);
			verbintenisModel.setState(verbintenisBackup);
		}
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(verbintenisBackup);
	}
}
