package nl.topicus.eduarte.web.components.datapanel;

import nl.topicus.cobra.reflection.InvocationFailedException;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelRowFactory;
import nl.topicus.cobra.web.components.datapanel.items.LinkItem;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.cobra.web.security.TargetBasedSecurityCheck;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

public class CustomDataPanelModuleEditPageLinkRowFactory<T> extends CustomDataPanelRowFactory<T>
{
	private static final long serialVersionUID = 1L;

	private final Class< ? extends Page> target;

	public CustomDataPanelModuleEditPageLinkRowFactory(Class<T> entityClass, MenuItemKey key)
	{
		target = getTarget(entityClass, key);
	}

	@Override
	public void bind(CustomDataPanel<T> panel)
	{
		if (target != null)
		{
			panel.addBehaviorToTable(new AppendingAttributeModifier("class", "tblClick", " "));
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public final WebMarkupContainer createRow(String id, CustomDataPanel<T> panel,
			final Item<T> item)
	{
		if (target == null)
		{
			return new WebMarkupContainer(id);
		}
		LinkItem ret = new LinkItem(id, item.getIndex(), item.getModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onLinkClicked()
			{
				CustomDataPanelModuleEditPageLinkRowFactory.this.onClick(item);
			}
		};
		ret.setModel(getCustomLinkModel(item.getModel()));
		TargetBasedSecurityCheck check = new TargetBasedSecurityCheck(ret, target);
		check.setUseAlternativeRenderCheck(true);
		ret.setSecurityCheck(check);
		return ret;
	}

	protected IModel< ? > getCustomLinkModel(IModel<T> baseModel)
	{
		return baseModel;
	}

	@SuppressWarnings("unchecked")
	private Class< ? extends Page> getTarget(Class<T> entityClass, MenuItemKey key)
	{
		final Class< ? > pageClass = EduArteApp.get().getModuleEditPage(entityClass, key);
		return (Class< ? extends Page>) pageClass;
	}

	protected void onClick(Item<T> item)
	{
		if (AbstractDeelnemerPage.class.isAssignableFrom(target)
			&& AbstractDeelnemerPage.class.isAssignableFrom(item.getPage().getClass()))
		{
			Deelnemer deelnemer = ((AbstractDeelnemerPage) item.getPage()).getContextDeelnemer();
			Verbintenis verbintenis =
				((AbstractDeelnemerPage) item.getPage()).getContextVerbintenis();
			try
			{
				item.setResponsePage(ReflectionUtil.invokeConstructor(target, deelnemer,
					verbintenis, item.getModelObject()));
			}
			catch (InvocationFailedException e)
			{
				if (e.isCausedByMethodNotFound())
				{
					item.setResponsePage(ReflectionUtil.invokeConstructor(target, deelnemer,
						verbintenis, item.getModelObject(), item.getPage()));
				}
				else
					throw e;
			}
		}
		else
		{
			try
			{
				item.setResponsePage(ReflectionUtil
					.invokeConstructor(target, item.getModelObject()));
			}
			catch (InvocationFailedException e)
			{
				if (e.isCausedByMethodNotFound())
				{
					item.setResponsePage(ReflectionUtil.invokeConstructor(target, item
						.getModelObject(), item.getPage()));
				}
				else
					throw e;
			}
		}
	}
}
