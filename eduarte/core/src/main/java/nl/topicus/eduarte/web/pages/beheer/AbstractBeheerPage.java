package nl.topicus.eduarte.web.pages.beheer;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.web.components.menu.BeheerMenu;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;

/**
 * Basispagina voor alle beheer pagina's
 * 
 * @author papegaaij
 */
public abstract class AbstractBeheerPage<T> extends SecurePage
{
	private SecurePage returnPage;

	private Class< ? extends SecurePage> returnPageClass;

	private MenuItemKey selectedMenuItem;

	public AbstractBeheerPage(MenuItemKey selectedMenuItem)
	{
		this(null, null, selectedMenuItem);
	}

	public AbstractBeheerPage(String name, MenuItemKey selectedMenuItem)
	{
		this(name, null, selectedMenuItem);
	}

	public AbstractBeheerPage(IModel<T> model, MenuItemKey selectedMenuItem)
	{
		this(null, model, selectedMenuItem);
	}

	public AbstractBeheerPage(String name, IModel<T> model, MenuItemKey selectedMenuItem)
	{
		super(name, model, CoreMainMenuItem.Beheer);
		this.selectedMenuItem = selectedMenuItem;
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new BeheerMenu(id, selectedMenuItem);
	}

	/**
	 * @return Het geselecteerde menuitem
	 */
	public MenuItemKey getSelectedMenuItem()
	{
		return selectedMenuItem;
	}

	public SecurePage getReturnPage()
	{
		return returnPage;
	}

	public void setReturnPage(SecurePage returnPage)
	{
		this.returnPage = returnPage;
	}

	public Class< ? extends SecurePage> getReturnPageClass()
	{
		if (returnPageClass != null)
			return returnPageClass;

		return returnPage.getClass();
	}

	public void setReturnPageClass(Class< ? extends SecurePage> returnPageClass)
	{
		this.returnPageClass = returnPageClass;
	}

	@Override
	protected Class< ? extends Page> getBasePageForContext()
	{
		return BeheerHomePage.class;
	}

	@SuppressWarnings("unchecked")
	public IModel<T> getContextModel()
	{
		return (IModel<T>) getDefaultModel();
	}

	public T getContextModelObject()
	{
		return getContextModel().getObject();
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(returnPage);
	}
}
