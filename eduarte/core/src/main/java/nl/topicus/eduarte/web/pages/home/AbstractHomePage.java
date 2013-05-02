/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.home;

import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.web.components.menu.HomeMenu;
import nl.topicus.eduarte.web.components.menu.HomeMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.model.IModel;

/**
 * Abstracte pagina voor alle home pagina's. Let op subclasses dienen nog wel
 * {@link #createComponents()} aan te roepen.
 * 
 * @author marrink
 */
public abstract class AbstractHomePage<T> extends SecurePage
{

	private final MenuItemKey selectedTab;

	/**
	 */
	public AbstractHomePage()
	{
		this(null, HomeMenuItem.Home);
	}

	/**
	 * Keuze welke tab geselecteerd is.
	 * 
	 * @param model
	 * @param selectedTab
	 */
	protected AbstractHomePage(IModel<T> model, MenuItemKey selectedTab)
	{
		super(model, CoreMainMenuItem.Home);
		this.selectedTab = selectedTab;
	}

	/**
	 * Keuze welke tab geselecteerd is.
	 * 
	 * @param selectedTab
	 */
	protected AbstractHomePage(MenuItemKey selectedTab)
	{
		this(null, selectedTab);
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new HomeMenu(id, selectedTab);
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
