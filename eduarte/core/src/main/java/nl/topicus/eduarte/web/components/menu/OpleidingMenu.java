package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.AbstractOpleidingPage;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.OpleidingToetsfiltersOverzichtPage;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.OpleidingkaartPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;

/**
 * Menu voor opleidingpagina's.
 * 
 * @author loite
 */
public class OpleidingMenu extends AbstractMenuBar
{
	private static final long serialVersionUID = 1L;

	private final IModel<Opleiding> opleidingModel;

	public OpleidingMenu(String id, IModel<Opleiding> opleidingModel, MenuItemKey selected)
	{
		super(id, selected);
		setDefaultModel(opleidingModel);
		this.opleidingModel = opleidingModel;
		addItem(new MenuItem(createPageLink(OpleidingkaartPage.class, getOpleidingModel()),
			OpleidingMenuItem.Opleidingkaart));
		addItem(new MenuItem(createPageLink(OpleidingToetsfiltersOverzichtPage.class,
			getOpleidingModel()), OpleidingMenuItem.Toetsfilters));
		addModuleMenuItems();
	}

	public DropdownMenuItem createDropdown(IModel<String> label, IMenuItem... items)
	{
		return new OnderwijsDownMenu(label, items);
	}

	public DropdownMenuItem createDropdown(String label)
	{
		return new OnderwijsDownMenu(label);
	}

	private final class OnderwijsDownMenu extends DropdownMenuItem
	{
		private static final long serialVersionUID = 1L;

		public OnderwijsDownMenu(IModel<String> label, IMenuItem... items)
		{
			super(label, items);
		}

		public OnderwijsDownMenu(String label)
		{
			super(label);
		}
	}

	public static IPageLink createPageLink(Class< ? extends AbstractOpleidingPage> pageClass,
			IModel<Opleiding> opleidingModel)
	{
		return new OpleidingPageLink(pageClass, opleidingModel);
	}

	private static final class OpleidingPageLink implements IPageLink
	{
		private static final long serialVersionUID = 1L;

		private final Class< ? extends AbstractOpleidingPage> pageClass;

		private final IModel<Opleiding> opleidingModel;

		private OpleidingPageLink(Class< ? extends AbstractOpleidingPage> pageClass,
				IModel<Opleiding> opleidingModel)
		{
			this.pageClass = pageClass;
			this.opleidingModel = opleidingModel;
		}

		@Override
		public Page getPage()
		{
			Opleiding opleiding = opleidingModel.getObject();
			return ReflectionUtil.invokeConstructor(pageClass, opleiding);
		}

		@Override
		public Class< ? extends AbstractOpleidingPage> getPageIdentity()
		{
			return pageClass;
		}

	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		ComponentUtil.detachQuietly(opleidingModel);
	}

	public IModel<Opleiding> getOpleidingModel()
	{
		return opleidingModel;
	}

}
