package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItemKey;

/**
 * 
 * 
 * @author vanharen
 */
public class RelatieBeheerMenu extends AbstractMenuBar
{

	private static final long serialVersionUID = 1L;

	public RelatieBeheerMenu(String id, MenuItemKey selectedTab)
	{
		super(id, selectedTab);
		addModuleMenuItems();
	}
}
