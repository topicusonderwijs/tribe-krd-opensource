package nl.topicus.cobra.web.components.modal.toevoegen;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.modelsv2.ExtendedModel;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.modelsv2.ObjectState;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.model.IModel;

/**
 * Abstract modal window voor het bewerken van een Object.
 * 
 * @author hoeve
 */
public abstract class AbstractToevoegenBewerkenModalWindow<T extends IdObject> extends
		CobraModalWindow<T>
{
	private static final long serialVersionUID = 1L;

	private boolean isDeleteOnClose = false;

	private boolean isAddEntity = false;

	private List<ObjectState> statesOnOpen;

	private ModelManager manager;

	public AbstractToevoegenBewerkenModalWindow(String id, IModel<T> model, ModelManager manager,
			String title)
	{
		super(id, model);
		setInitialHeight(500);
		setInitialWidth(650);
		setTitle(title);
		this.manager = manager;

		setCloseButtonCallback(new ModalWindow.CloseButtonCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean onCloseButtonClicked(AjaxRequestTarget target)
			{
				setDeleteOnClose(true);
				return true;
			}
		});
	}

	private List<ExtendedModel< ? >> getManagedModels()
	{
		List<ExtendedModel< ? >> ret = new ArrayList<ExtendedModel< ? >>();
		for (IdObject curObject : getEditableObjects())
			ret.add(getManagedModel(curObject));
		return ret;
	}

	protected abstract List<IdObject> getEditableObjects();

	private ExtendedModel< ? > getManagedModel(IdObject object)
	{
		IModel< ? > managedModel = manager.getModel(object, null);
		if (!(managedModel instanceof ExtendedModel< ? >))
		{
			throw new IllegalArgumentException(object
				+ " wordt niet gemanaged door de gegeven manager en kan dus niet bewerkt worden");
		}
		return (ExtendedModel< ? >) managedModel;
	}

	@Override
	public void show(AjaxRequestTarget target)
	{
		statesOnOpen = new ArrayList<ObjectState>();
		for (ExtendedModel< ? > curModel : getManagedModels())
			statesOnOpen.add(curModel.getState());
		super.show(target);
	}

	@Override
	public MarkupContainer setDefaultModel(IModel< ? > model)
	{
		throw new UnsupportedOperationException(
			"Gebruik niet setModel op een ModalWindow, dat gaat nooit goed!");
	}

	public boolean isDeleteOnClose()
	{
		return isDeleteOnClose;
	}

	public void setDeleteOnClose(boolean isDeleteOnClose)
	{
		this.isDeleteOnClose = isDeleteOnClose;
	}

	public boolean isAddEntity()
	{
		return isAddEntity;
	}

	public void setAddEntity(boolean isAddEntity)
	{
		this.isAddEntity = isAddEntity;
	}

	public void revertData()
	{
		List<ExtendedModel< ? >> managedModels = getManagedModels();
		for (int count = 0; count < managedModels.size(); count++)
			managedModels.get(count).setState(statesOnOpen.get(count));
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(manager);
		ComponentUtil.detachQuietly(statesOnOpen);
	}
}
