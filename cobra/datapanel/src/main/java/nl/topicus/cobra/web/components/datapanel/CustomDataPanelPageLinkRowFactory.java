package nl.topicus.cobra.web.components.datapanel;

import nl.topicus.cobra.reflection.InvocationFailedException;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;
import nl.topicus.cobra.web.components.datapanel.items.LinkItem;
import nl.topicus.cobra.web.security.TargetBasedSecurityCheck;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

public class CustomDataPanelPageLinkRowFactory<T> extends CustomDataPanelRowFactory<T>
{
	private static final long serialVersionUID = 1L;

	private Class< ? extends Page> target;

	public CustomDataPanelPageLinkRowFactory(Class< ? extends Page> target)
	{
		this.target = target;
	}

	@Override
	public void bind(CustomDataPanel<T> panel)
	{
		panel.addBehaviorToTable(new AppendingAttributeModifier("class", "tblClick", " "));
	}

	@Override
	public final WebMarkupContainer createRow(String id, CustomDataPanel<T> panel,
			final Item<T> item)
	{
		LinkItem<T> ret = new LinkItem<T>(id, item.getIndex(), item.getModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onLinkClicked()
			{
				CustomDataPanelPageLinkRowFactory.this.onClick(item);
			}

			@Override
			public boolean isEnabled()
			{
				return CustomDataPanelPageLinkRowFactory.this.isEnabled(item);
			}
		};
		ret.setModel(getCustomLinkModel(item.getModel()));
		TargetBasedSecurityCheck check = new TargetBasedSecurityCheck(ret, getTarget(item));
		check.setUseAlternativeRenderCheck(true);
		ret.setSecurityCheck(check);

		onRowCreated(ret);
		return ret;
	}

	@SuppressWarnings("unused")
	protected void onRowCreated(Item<T> item)
	{
	}

	protected IModel<T> getCustomLinkModel(IModel<T> baseModel)
	{
		return baseModel;
	}

	@SuppressWarnings("unused")
	protected Class< ? extends Page> getTarget(Item<T> item)
	{
		return target;
	}

	protected void onClick(Item<T> item)
	{
		try
		{
			item.setResponsePage(ReflectionUtil.invokeConstructor(getTarget(item), item
				.getModelObject()));
		}
		catch (InvocationFailedException e)
		{
			if (e.isCausedByMethodNotFound())
			{
				item.setResponsePage(ReflectionUtil.invokeConstructor(getTarget(item), item
					.getModelObject(), item.getPage()));
			}
			else
				throw e;
		}
	}

	@SuppressWarnings("unused")
	protected boolean isEnabled(Item<T> item)
	{
		return true;
	}
}
