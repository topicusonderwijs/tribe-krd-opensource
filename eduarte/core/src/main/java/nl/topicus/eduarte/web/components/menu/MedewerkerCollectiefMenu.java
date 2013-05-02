package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.web.pages.medewerker.MedewerkerZoekenPage;

/**
 * Collectiefmenu voor medewerkers
 * 
 * @author loite
 */
public class MedewerkerCollectiefMenu extends AbstractMenuBar
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param selectedItem
	 */
	public MedewerkerCollectiefMenu(String id, MenuItemKey selectedItem)
	{
		super(id, selectedItem);
		addItem(new MenuItem(MedewerkerZoekenPage.class, MedewerkerCollectiefMenuItem.Zoeken));
		addModuleMenuItems();
	}

}
