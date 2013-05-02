package nl.topicus.cobra.web.components.datapanel;

import java.io.Serializable;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IDetachable;

public class CustomDataPanelRowFactory<T> implements Serializable, IDetachable
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	public void bind(CustomDataPanel<T> panel)
	{
	}

	public void onBeforeRender()
	{
	}

	@SuppressWarnings("unused")
	public WebMarkupContainer createRow(String id, CustomDataPanel<T> panel, Item<T> item)
	{
		return new WebMarkupContainer(id);
	}

	@SuppressWarnings("unused")
	public WebMarkupContainer createHeaderRow(String id, CustomDataPanel<T> panel, Item<T> item)
	{
		return new WebMarkupContainer(id);
	}

	@Override
	public void detach()
	{
	}
}
