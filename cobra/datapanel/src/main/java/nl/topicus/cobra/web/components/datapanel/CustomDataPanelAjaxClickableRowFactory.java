package nl.topicus.cobra.web.components.datapanel;

import nl.topicus.cobra.util.JavaUtil;
import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;
import nl.topicus.cobra.web.components.datapanel.items.AjaxClickableItem;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

public abstract class CustomDataPanelAjaxClickableRowFactory<T> extends
		CustomDataPanelRowFactory<T>
{
	private static final long serialVersionUID = 1L;

	protected IModel<T> selectedObject;

	protected int selectedIndex = -1;

	protected int selectedPage = -1;

	protected boolean firstSelected;

	public CustomDataPanelAjaxClickableRowFactory()
	{
	}

	public CustomDataPanelAjaxClickableRowFactory(IModel<T> selectedObject)
	{
		this.selectedObject = selectedObject;
	}

	public CustomDataPanelAjaxClickableRowFactory<T> selectFirstOrGiven(T selected)
	{
		if (selected == null || selectedObject == null)
		{
			firstSelected = true;
			selectedIndex = 0;
			selectedPage = 0;
			if (selectedObject != null)
				selectedObject.setObject(null);
		}
		else
			selectedObject.setObject(selected);

		return this;
	}

	@Override
	public void bind(CustomDataPanel<T> panel)
	{
		panel.addBehaviorToTable(new AppendingAttributeModifier("class", "tblClick", " "));
		if (selectedObject != null && selectedObject.getObject() != null)
		{
			panel.ensureVisible(selectedObject.getObject());
		}
	}

	@Override
	public WebMarkupContainer createRow(String id, final CustomDataPanel<T> panel,
			final Item<T> item)
	{
		return createClickableRow(id, panel, item, item.getModel());
	}

	protected boolean isSelected(CustomDataPanel<T> panel, Item<T> item, IModel<T> itemModel)
	{
		return (firstSelected && item.getIndex() == 0 && panel.getCurrentPage() == 0)
			|| (selectedObject == null && item.getIndex() == selectedIndex && panel
				.getCurrentPage() == selectedPage)
			|| (selectedObject != null && JavaUtil.equalsOrBothNull(selectedObject.getObject(),
				itemModel.getObject()));
	}

	public IModel<T> getSelected()
	{
		return selectedObject;
	}

	protected AjaxClickableItem<T> createClickableRow(String id, final CustomDataPanel<T> panel,
			final Item<T> item, final IModel<T> itemModel)
	{
		AjaxClickableItem<T> ret = new AjaxClickableItem<T>(id, item.getIndex(), itemModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				selectedIndex = item.getIndex();
				selectedPage = panel.getCurrentPage();
				if (selectedObject != null)
					selectedObject.setObject(itemModel.getObject());
				CustomDataPanelAjaxClickableRowFactory.this.onClick(target, item);
			}
		};
		if (isSelected(panel, item, itemModel))
		{
			ret.add(new AppendingAttributeModifier("class", "tblSelected"));
			if (firstSelected)
			{
				firstSelected = false;
				if (selectedObject != null)
					selectedObject.setObject(itemModel.getObject());
			}
		}
		return ret;
	}

	abstract protected void onClick(AjaxRequestTarget target, Item<T> item);

	@Override
	public void detach()
	{
		super.detach();
		if (selectedObject != null)
			selectedObject.detach();
	}
}
