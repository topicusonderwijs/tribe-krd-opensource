package nl.topicus.cobra.web.components.datapanel;

import java.io.Serializable;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IDetachable;

public interface IRowFactoryDecorator<T> extends Serializable, IDetachable
{
	public abstract void bind(CustomDataPanel<T> panel);

	public void onBeforeRender();

	public abstract WebMarkupContainer createHeaderRow(String id, CustomDataPanel<T> panel, Item<T> item);

	public abstract WebMarkupContainer createRow(String id, CustomDataPanel<T> panel, Item<T> item);

	public void setInnerRowFactory(CustomDataPanelRowFactory<T> inner);

	public CustomDataPanelRowFactory<T> getInnerRowFactory();

}