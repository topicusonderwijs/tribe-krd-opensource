package nl.topicus.eduarte.web.components.modalwindow;

import java.util.List;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

public abstract class AbstractEduarteSelectieToevoegenBewerkenPanel<T extends IdObject, R extends IdObject>
		extends AbstractEduArteToevoegenBewerkenPanel<T>
{
	private static final long serialVersionUID = 1L;

	private IModel<List<R>> choicesModel;

	private ModelManager manager;

	private CustomDataPanelContentDescription<R> tableChoices;

	public AbstractEduarteSelectieToevoegenBewerkenPanel(String id, IModel<List<T>> model,
			ModelManager manager, CustomDataPanelContentDescription<T> table,
			CustomDataPanelContentDescription<R> tableChoices, IModel<List<R>> choicesModel)
	{
		super(id, model, manager, table);
		this.choicesModel = choicesModel;
		this.manager = manager;
		this.tableChoices = tableChoices;
	}

	@Override
	public AbstractToevoegenBewerkenModalWindowPanel<T> createModalWindowPanel(String id,
			AbstractToevoegenBewerkenModalWindow<T> modalWindow)
	{
		return new AbstractEduarteSelectieToevoegenBewerkenModalWindow<T, R>(id, modalWindow, this)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target, Item<R> item)
			{
				onItemSelect(target, item);
			}
		};
	}

	public IModel<List<R>> getChoicesModel()
	{
		return choicesModel;
	}

	public ModelManager getModelManager()
	{
		return manager;
	}

	public CustomDataPanelContentDescription<R> getContentDescriptionChoices()
	{
		return tableChoices;
	}

	protected abstract void onItemSelect(AjaxRequestTarget target, Item<R> item);

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(choicesModel);
	}

}
