package nl.topicus.cobra.web.components.datapanel;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;

public class CollapsableGroupRowFactoryDecorator<T> extends CollapsableRowFactoryDecorator<T>
{
	private static final long serialVersionUID = 1L;

	private WebMarkupContainer lastGroup;

	public CollapsableGroupRowFactoryDecorator(CustomDataPanelRowFactory<T> inner)
	{
		super(inner);
	}

	@Override
	public void onBeforeRender()
	{
		super.onBeforeRender();
		lastGroup = null;
	}

	@Override
	public WebMarkupContainer createHeaderRow(String id, CustomDataPanel<T> panel, Item<T> item)
	{
		WebMarkupContainer ret = super.createHeaderRow(id, panel, item);
		makeParent(ret, item.getModel());
		lastGroup = ret;
		return ret;
	}

	@Override
	public WebMarkupContainer createRow(String id, CustomDataPanel<T> panel, Item<T> item)
	{
		WebMarkupContainer ret = super.createRow(id, panel, item);
		if (lastGroup != null)
			makeChild(lastGroup, ret);
		return ret;
	}
}
