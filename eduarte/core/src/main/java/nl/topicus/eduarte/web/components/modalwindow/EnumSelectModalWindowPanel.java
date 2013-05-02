package nl.topicus.eduarte.web.components.modalwindow;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;

public class EnumSelectModalWindowPanel<T extends Enum<T>> extends CobraModalWindowBasePanel<T>
{
	private static final long serialVersionUID = 1L;

	public EnumSelectModalWindowPanel(String id, CobraModalWindow<T> modalWindow)
	{
		super(id, modalWindow);

		IObjectClassAwareModel<T> model = getClassAwareModel();
		CollectionDataProvider<T> provider = new CollectionDataProvider<T>(findChoices(model));

		CustomDataPanelAjaxClickableRowFactory<T> rowFactory =
			new CustomDataPanelAjaxClickableRowFactory<T>(model)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onClick(AjaxRequestTarget target, Item<T> item)
				{
					EnumSelectModalWindowPanel.this.onClick(target, item);
				}
			};

		EduArteDataPanel<T> datapanel =
			new EduArteDataPanel<T>("datapanel", provider, new EnumTable(getEnumTitle()));
		datapanel.setRowFactory(rowFactory);
		datapanel.setButtonsVisible(false);
		datapanel.setItemsPerPage(20);

		add(datapanel);
	}

	private class EnumTable extends CustomDataPanelContentDescription<T>
	{
		private static final long serialVersionUID = 1L;

		public EnumTable(String title)
		{
			super(title);

			addColumn(new AbstractCustomColumn<T>("Omschrijving", "Omschrijving")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void populateItem(WebMarkupContainer cell, String componentId,
						WebMarkupContainer row, IModel<T> rowModel, int span)
				{
					T enumRow = rowModel.getObject();
					cell.add(new Label(componentId, enumRow.toString()));
				}

			});
		}
	}

	private String getEnumTitle()
	{
		return StringUtil.convertCamelCase(getClassAwareModel().getClass().getSimpleName());
	}

	private static <T extends Enum<T>> List<T> findChoices(IObjectClassAwareModel<T> model)
	{
		Class<T> clazz = model.getObjectClass();
		if (clazz == null)
			throw new IllegalArgumentException("Model cannot determine object class");
		return new ArrayList<T>(EnumSet.allOf(clazz));
	}

	private IObjectClassAwareModel<T> getClassAwareModel()
	{
		return (IObjectClassAwareModel<T>) getModalWindow().getModel();
	}

	@SuppressWarnings("unused")
	protected void onClick(AjaxRequestTarget target, Item<T> item)
	{
	}
}
