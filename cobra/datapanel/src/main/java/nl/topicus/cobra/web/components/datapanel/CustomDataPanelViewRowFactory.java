package nl.topicus.cobra.web.components.datapanel;

import nl.topicus.cobra.util.JavaUtil;
import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Alleen display van de tabel,bijv te gebruiken icm een decorator op een edit page.
 * 
 * @author vanharen
 */
public abstract class CustomDataPanelViewRowFactory<T> extends CustomDataPanelRowFactory<T>
{
	private static final long serialVersionUID = 1L;

	protected IModel<T> selectedObject;

	protected int selectedIndex = -1;

	protected int selectedPage = -1;

	public CustomDataPanelViewRowFactory()
	{
	}

	public CustomDataPanelViewRowFactory(IModel<T> selectedObject)
	{
		this.selectedObject = selectedObject;
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
		WebMarkupContainer ret = new WebMarkupContainer(id);
		if (isSelected(panel, item, item.getModel()))
			ret.add(new AppendingAttributeModifier("class", "tblSelected"));
		return ret;
	}

	protected boolean isSelected(CustomDataPanel<T> panel, Item<T> item, IModel<T> itemModel)
	{
		return (selectedObject == null && item.getIndex() == selectedIndex && panel
			.getCurrentPage() == selectedPage)
			|| (selectedObject != null && JavaUtil.equalsOrBothNull(selectedObject.getObject(),
				itemModel.getObject()));
	}

	public IModel<T> getSelected()
	{
		return selectedObject;
	}

	@Override
	public void detach()
	{
		super.detach();
		if (selectedObject != null)
			selectedObject.detach();
	}
}
