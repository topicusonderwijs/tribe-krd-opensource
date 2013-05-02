package nl.topicus.eduarte.krd.web.pages.beheer.bron;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.cobra.web.components.menu.main.MainMenuItem;
import nl.topicus.eduarte.web.components.menu.BeheerMenu;
import nl.topicus.eduarte.web.components.menu.BronMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

@PageInfo(title = "BRON", menu = "Deelnemer")
public abstract class AbstractBronPage extends SecurePage
{
	public AbstractBronPage()
	{
		super(CoreMainMenuItem.Beheer);
	}

	public AbstractBronPage(MainMenuItem menuItem)
	{
		super(menuItem);
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new BeheerMenu(id, getSelectedMenuBarItem());
	}

	protected MenuItemKey getSelectedMenuBarItem()
	{
		return BronMenuItem.BRON;
	}
}
