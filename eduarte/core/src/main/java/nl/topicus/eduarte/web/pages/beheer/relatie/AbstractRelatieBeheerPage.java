package nl.topicus.eduarte.web.pages.beheer.relatie;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.web.components.menu.RelatieBeheerMenu;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.model.IModel;

/**
 * 
 * 
 * @author vanharen
 */

public abstract class AbstractRelatieBeheerPage<T> extends SecurePage
{

	private SecurePage returnPage;

	private Class< ? extends SecurePage> returnPageClass;

	private MenuItemKey selectedMenuItem;

	public AbstractRelatieBeheerPage(MenuItemKey selectedMenuItem)
	{
		this(null, null, selectedMenuItem);
	}

	public AbstractRelatieBeheerPage(String name, MenuItemKey selectedMenuItem)
	{
		this(name, null, selectedMenuItem);
	}

	public AbstractRelatieBeheerPage(IModel<T> model, MenuItemKey selectedMenuItem)
	{
		this(null, model, selectedMenuItem);
	}

	public AbstractRelatieBeheerPage(String name, IModel<T> model, MenuItemKey selectedMenuItem)
	{
		super(name, model, CoreMainMenuItem.Relatie);
		this.selectedMenuItem = selectedMenuItem;
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new RelatieBeheerMenu(id, selectedMenuItem);
	}

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
	public void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(returnPage);
	}

	@SuppressWarnings("unchecked")
	public IModel<T> getContextModel()
	{
		return (IModel<T>) getDefaultModel();
	}

	@SuppressWarnings("unchecked")
	public T getContextModelObject()
	{
		return (T) getDefaultModelObject();
	}
}
