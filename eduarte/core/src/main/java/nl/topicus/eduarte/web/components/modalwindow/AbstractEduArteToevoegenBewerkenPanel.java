package nl.topicus.eduarte.web.components.modalwindow;

import java.util.List;

import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.CustomColumn.Positioning;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenModalWindow;
import nl.topicus.cobra.web.components.modal.toevoegen.AbstractToevoegenBewerkenPanel;
import nl.topicus.cobra.web.components.panels.datapanel.columns.AjaxDeleteColumn;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

public abstract class AbstractEduArteToevoegenBewerkenPanel<T extends IdObject> extends
		AbstractToevoegenBewerkenPanel<T>
{
	private static final long serialVersionUID = 1L;

	public AbstractEduArteToevoegenBewerkenPanel(String id, IModel<List<T>> model,
			ModelManager manager, IDetachable table)
	{
		super(id, model, manager, table);
	}

	@Override
	protected Panel createCustomDataPanel(String id, ListModelDataProvider<T> provider,
			IDetachable table, final AbstractToevoegenBewerkenModalWindow<T> modalWindow)
	{
		@SuppressWarnings("unchecked")
		CustomDataPanelContentDescription<T> contents =
			(CustomDataPanelContentDescription<T>) table;
		EduArteDataPanel<T> datapanel = new EduArteDataPanel<T>(id, provider, contents);
		datapanel.setRowFactory(new CustomDataPanelAjaxClickableRowFactory<T>(modalWindow
			.getModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target, Item<T> item)
			{
				if (isClickable())
				{
					modalWindow.setAddEntity(false);
					modalWindow.show(target);
				}
			}

			@Override
			protected boolean isSelected(CustomDataPanel<T> panel, Item<T> item, IModel<T> itemModel)
			{
				return false;
			}
		});

		contents.addColumn(new AjaxDeleteColumn<T>("delete", "")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(WebMarkupContainer item, IModel<T> rowModel,
					AjaxRequestTarget target)
			{
				onDelete(rowModel.getObject(), target);
				deleteT(rowModel.getObject());
				target.addComponent(AbstractEduArteToevoegenBewerkenPanel.this);
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && isDeletable() && isEditable();
			}

			@Override
			public boolean isContentsVisible(IModel<T> rowModel)
			{
				return super.isContentsVisible(rowModel) && isDeletable(rowModel.getObject());
			}
		}.setPositioning(Positioning.FIXED_RIGHT));
		return datapanel;
	}

	/**
	 * Bedoeld om overschreven te worden in subklasse
	 */
	@SuppressWarnings("unused")
	public boolean isDeletable(T object)
	{
		return true;
	}

	/**
	 * Bedoeld om overschreven te worden in subklasse
	 */
	@SuppressWarnings("unused")
	public void onDelete(T object, AjaxRequestTarget target)
	{
	}

	@Override
	@SuppressWarnings("unchecked")
	public EduArteDataPanel<T> getCustomDataPanel()
	{
		return (EduArteDataPanel<T>) super.getCustomDataPanel();
	}
}
