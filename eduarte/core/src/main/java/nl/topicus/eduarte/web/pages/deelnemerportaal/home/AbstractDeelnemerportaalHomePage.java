package nl.topicus.eduarte.web.pages.deelnemerportaal.home;

import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.components.menu.deelnemerPortaal.DeelnemerportaalHomeMenu;
import nl.topicus.eduarte.web.components.menu.main.DeelnemerportaalMainMenuItem;
import nl.topicus.eduarte.web.pages.SelfServiceSecurePage;

/**
 * Abstracte pagina voor alle home pagina's. Let op subclasses dienen nog wel
 * {@link #createComponents()} aan te roepen.
 * 
 * @author ambrosius, papegaaij
 */
public abstract class AbstractDeelnemerportaalHomePage extends SelfServiceSecurePage
{
	private final MenuItemKey selectedTab;

	protected AbstractDeelnemerportaalHomePage(MenuItemKey selectedTab, Verbintenis verbintenis)
	{
		super(DeelnemerportaalMainMenuItem.Home, verbintenis);
		this.selectedTab = selectedTab;
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerportaalHomeMenu(id, selectedTab, getContextVerbintenisModel());
	}
}
