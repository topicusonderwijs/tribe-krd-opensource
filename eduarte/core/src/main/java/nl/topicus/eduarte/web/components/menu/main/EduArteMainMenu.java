/*
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.menu.main;

import nl.topicus.cobra.web.components.menu.main.CobraMainMenu;
import nl.topicus.cobra.web.components.menu.main.MainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * @author loite
 */
public class EduArteMainMenu extends CobraMainMenu
{
	private static final long serialVersionUID = 1L;

	public EduArteMainMenu(String menuId, MainMenuItem selectedItem, final SecurePage page)
	{
		super(menuId, selectedItem);
		setEnabled(!page.isEditable());
		createComponents();
		add(new WebMarkupContainer("appTitleBox").setVisible(false));
		add(new MainMenuSearchBox("searchBox").setVisible(!page.isEditable()));
		add(new MainMenuCurrentUserBox("eduArteUserBox", page));
		add(new WebMarkupContainer("currentUserBox").setVisible(false));
	}
}
