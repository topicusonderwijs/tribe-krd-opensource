package nl.topicus.eduarte.web.components.modalwindow;

import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

public abstract class AbstractEduarteSelectieToevoegenBewerkenModalWindow<T extends IdObject, R extends IdObject>
		extends AbstractToevoegenBewerkenModalWindowPanel<T>
{
	private static final long serialVersionUID = 1L;

	public AbstractEduarteSelectieToevoegenBewerkenModalWindow(String id,
			AbstractToevoegenBewerkenModalWindow<T> modalWindow,
			AbstractEduarteSelectieToevoegenBewerkenPanel<T, R> editPanel)
	{
		super(id, modalWindow, editPanel);
		modalWindow.setInitialHeight(300);

		ListModelDataProvider<R> provider =
			new ListModelDataProvider<R>(editPanel.getChoicesModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<R> model(R object)
				{
					return ModelFactory.getModel(object, getSelectieEditPanel().getModelManager());
				}
			};

		EduArteDataPanel<R> panel =
			new EduArteDataPanel<R>("selectiePanel", provider, getSelectieEditPanel()
				.getContentDescriptionChoices());

		panel.setRowFactory(new CustomDataPanelAjaxClickableRowFactory<R>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target, Item<R> item)
			{
				AbstractEduarteSelectieToevoegenBewerkenModalWindow.this.onClick(target, item);
				getModalWindow().close(target);
			}

		});

		getFormContainer().add(panel);
		createComponents();
	}

	@SuppressWarnings("unchecked")
	public AbstractEduarteSelectieToevoegenBewerkenPanel<T, R> getSelectieEditPanel()
	{
		return (AbstractEduarteSelectieToevoegenBewerkenPanel<T, R>) getEditPanel();
	}

	abstract protected void onClick(AjaxRequestTarget target, Item<R> item);

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		// Geen knoppen onder ModalWindow
	}
}