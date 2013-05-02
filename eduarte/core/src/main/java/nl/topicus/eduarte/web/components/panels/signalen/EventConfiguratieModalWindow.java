package nl.topicus.eduarte.web.components.panels.signalen;

import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ObjectState;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.entities.signalering.settings.AbstractEventAbonnementConfiguration;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

public abstract class EventConfiguratieModalWindow extends
		CobraModalWindow<AbstractEventAbonnementConfiguration< ? >>
{
	private static final long serialVersionUID = 1L;

	private ObjectState stateOnOpen;

	public EventConfiguratieModalWindow(String id,
			IModel<AbstractEventAbonnementConfiguration< ? >> configModel)
	{
		super(id, configModel);
		setTitle("Instellingen");
		setInitialHeight(300);
		setInitialWidth(700);
	}

	protected abstract IChangeRecordingModel< ? extends AbstractEventAbonnementConfiguration< ? >> getChangeRecordingModel();

	@Override
	public void show(AjaxRequestTarget target)
	{
		stateOnOpen = getChangeRecordingModel().getState();
		super.show(target);
	}

	public void revertChanges()
	{
		getChangeRecordingModel().setState(stateOnOpen);
	}

	@Override
	protected EventConfiguratiePanel createContents(String id)
	{
		return new EventConfiguratiePanel(id, this);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
	}
}
