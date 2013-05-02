package nl.topicus.cobra.web.components.datapanel;

import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;
import nl.topicus.cobra.web.components.datapanel.items.LinkItem;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * @author hoeve
 * 
 *         RowFactory welke een gewone click doet.
 * 
 */
public abstract class CustomDataPanelClickableRowFactory<T> extends CustomDataPanelRowFactory<T>
{
	private static final long serialVersionUID = 1L;

	public CustomDataPanelClickableRowFactory()
	{
	}

	@Override
	public void bind(CustomDataPanel<T> panel)
	{
		panel.addBehaviorToTable(new AppendingAttributeModifier("class", "tblClick", " "));
	}

	@Override
	public WebMarkupContainer createRow(String id, final CustomDataPanel<T> panel,
			final Item<T> item)
	{
		return createClickableRow(id, item, item.getModel());
	}

	protected LinkItem<T> createClickableRow(String id, final Item<T> item,
			final IModel<T> itemModel)
	{
		return new LinkItem<T>(id, item.getIndex(), itemModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onLinkClicked()
			{
				CustomDataPanelClickableRowFactory.this.onClick(getModel());
			}
		};
	}

	abstract protected void onClick(IModel<T> model);

	@SuppressWarnings("unused")
	public final void setResponsePage(Object o) throws Exception
	{
		throw new AssertionError("Deze methode dient puur om te zorgen dat je niet "
			+ "CustomDataPanelClickableRowFactory gebruikt met setResponsePage daarvoor is "
			+ "de CustomDataPanelPageLinkRowFactory");
	}
}
