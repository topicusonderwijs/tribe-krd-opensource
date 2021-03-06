/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.shared;

import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.cobra.web.components.menu.main.MainMenuItem;
import nl.topicus.eduarte.web.components.menu.GroepCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.PageContext;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

public class GroepCollectiefPageContext implements PageContext
{
	private static final long serialVersionUID = 1L;

	private String name;

	private MenuItemKey menuItem;

	public GroepCollectiefPageContext(String name, MenuItemKey menuItem)
	{
		this.name = name;
		this.menuItem = menuItem;
	}

	@Override
	public MainMenuItem getCurrentMainMenuItem()
	{
		return CoreMainMenuItem.Groep;
	}

	@Override
	public AbstractMenuBar getMenu(String id)
	{
		return new GroepCollectiefMenu(id, menuItem);
	}

	@Override
	public Component getTitle(String id)
	{
		return new Label(id, name);
	}

	@Override
	public void detach()
	{
	}
}