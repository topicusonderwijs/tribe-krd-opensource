package nl.topicus.cobra.web.components.datapanel;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;

public class AbstractRowFactoryDecorator<T> extends CustomDataPanelRowFactory<T> implements
		IRowFactoryDecorator<T>
{
	private static final long serialVersionUID = 1L;

	protected CustomDataPanelRowFactory<T> innerRowFactory;

	public AbstractRowFactoryDecorator(CustomDataPanelRowFactory<T> innerRowFactory)
	{
		this.innerRowFactory = innerRowFactory;
	}

	@Override
	public void bind(CustomDataPanel<T> panel)
	{
		innerRowFactory.bind(panel);
	}

	@Override
	public void onBeforeRender()
	{
		innerRowFactory.onBeforeRender();
	}

	@Override
	public WebMarkupContainer createHeaderRow(String id, CustomDataPanel<T> panel, Item<T> item)
	{
		return innerRowFactory.createHeaderRow(id, panel, item);
	}

	@Override
	public WebMarkupContainer createRow(String id, CustomDataPanel<T> panel, Item<T> item)
	{
		return innerRowFactory.createRow(id, panel, item);
	}

	@Override
	public void detach()
	{
		innerRowFactory.detach();
	}

	@Override
	public CustomDataPanelRowFactory<T> getInnerRowFactory()
	{
		return innerRowFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setInnerRowFactory(CustomDataPanelRowFactory<T> inner)
	{
		if (innerRowFactory instanceof IRowFactoryDecorator< ? >)
			((IRowFactoryDecorator<T>) innerRowFactory).setInnerRowFactory(inner);
		else
			this.innerRowFactory = inner;
	}
}